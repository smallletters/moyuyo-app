package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyuyo.common.JwtUtil;
import com.moyuyo.common.dto.auth.*;
import com.moyuyo.dao.entity.UserEntity;
import com.moyuyo.dao.mapper.UserMapper;
import com.moyuyo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static final long ACCESS_TOKEN_EXPIRE_SECONDS = 7200;
    private static final long REFRESH_TOKEN_EXPIRE_SECONDS = 604800;
    private static final long VERIFICATION_CODE_EXPIRE_SECONDS = 300;
    private static final long RESET_TOKEN_EXPIRE_SECONDS = 1800;
    private static final long MAGIC_LINK_EXPIRE_SECONDS = 900;
    private static final long TWO_FACTOR_CODE_EXPIRE_SECONDS = 300;

    private static final String REDIS_KEY_REFRESH = "auth:refresh:";
    private static final String REDIS_KEY_BLACKLIST = "auth:blacklist:";
    private static final String REDIS_KEY_VERIFY_CODE = "auth:verify:";
    private static final String REDIS_KEY_RESET_TOKEN = "auth:reset:";
    private static final String REDIS_KEY_MAGIC_LINK = "auth:magiclink:";
    private static final String REDIS_KEY_2FA_CODE = "auth:2fa:";
    private static final String REDIS_KEY_2FA_VERIFIED = "auth:2fa-verified:";

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));
        if (count > 0) {
            throw new IllegalArgumentException("Email already registered");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setCountry(request.getCountry());
        user.setMarketingOptIn(request.getMarketingOptIn() != null && request.getMarketingOptIn());
        user.setEmailVerified(false);
        user.setStatus(1);
        user.setPoints(0);
        user.setTwoFactorEnabled(false);
        user.setLastLoginTime(LocalDateTime.now());

        userMapper.insert(user);

        log.info("User registered: id={}, email={}", user.getId(), user.getEmail());
        return generateTokenPair(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (!PASSWORD_ENCODER.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("User logged in: id={}, email={}", user.getId(), user.getEmail());
        return generateTokenPair(user);
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String key = REDIS_KEY_REFRESH + request.getRefreshToken();
        String userIdStr = redisTemplate.opsForValue().get(key);

        if (userIdStr == null) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        redisTemplate.delete(key);

        UserEntity user = userMapper.selectById(Long.parseLong(userIdStr));
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return generateTokenPair(user);
    }

    @Override
    public void logout(Long userId, String accessToken) {
        redisTemplate.opsForValue().set(
                REDIS_KEY_BLACKLIST + accessToken,
                String.valueOf(userId),
                ACCESS_TOKEN_EXPIRE_SECONDS,
                TimeUnit.SECONDS);
        log.info("User logged out: id={}", userId);
    }

    @Override
    public void sendEmailVerification(EmailVerifyRequest request) {
        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));

        if (user == null) {
            return;
        }

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        String code = String.format("%06d", (int) (Math.random() * 1000000));
        redisTemplate.opsForValue().set(
                REDIS_KEY_VERIFY_CODE + request.getEmail(),
                code,
                VERIFICATION_CODE_EXPIRE_SECONDS,
                TimeUnit.SECONDS);

        log.info("Verification code sent to {}: {}", request.getEmail(), code);
    }

    @Override
    public void confirmEmailVerification(EmailVerifyConfirmRequest request) {
        String key = REDIS_KEY_VERIFY_CODE + request.getEmail();
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null || !storedCode.equals(request.getCode())) {
            throw new IllegalArgumentException("Invalid or expired verification code");
        }

        redisTemplate.delete(key);

        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));

        if (user != null) {
            user.setEmailVerified(true);
            userMapper.updateById(user);
            log.info("Email verified: {}", request.getEmail());
        }
    }

    @Override
    public void sendPasswordReset(EmailVerifyRequest request) {
        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));

        if (user == null) {
            return;
        }

        String resetToken = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(
                REDIS_KEY_RESET_TOKEN + resetToken,
                request.getEmail(),
                RESET_TOKEN_EXPIRE_SECONDS,
                TimeUnit.SECONDS);

        log.info("Password reset token generated for {}: {}", request.getEmail(), resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String key = REDIS_KEY_RESET_TOKEN + request.getToken();
        String email = redisTemplate.opsForValue().get(key);

        if (email == null) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        redisTemplate.delete(key);

        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, email));

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setPasswordHash(PASSWORD_ENCODER.encode(request.getNewPassword()));
        userMapper.updateById(user);

        log.info("Password reset completed for: {}", email);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!PASSWORD_ENCODER.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(PASSWORD_ENCODER.encode(request.getNewPassword()));
        userMapper.updateById(user);

        log.info("Password changed for user: {}", userId);
    }

    @Override
    public UserEntity getCurrentUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setPasswordHash(null);
        return user;
    }

    @Override
    @Transactional
    public UserEntity updateCurrentUser(Long userId, UserEntity update) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (update.getNickname() != null) {
            user.setNickname(update.getNickname());
        }
        if (update.getAvatar() != null) {
            user.setAvatar(update.getAvatar());
        }
        if (update.getPhone() != null) {
            user.setPhone(update.getPhone());
        }
        if (update.getBirthday() != null) {
            user.setBirthday(update.getBirthday());
        }
        if (update.getCountry() != null) {
            user.setCountry(update.getCountry());
        }
        if (update.getLocale() != null) {
            user.setLocale(update.getLocale());
        }
        if (update.getTimezone() != null) {
            user.setTimezone(update.getTimezone());
        }
        if (update.getMarketingOptIn() != null) {
            user.setMarketingOptIn(update.getMarketingOptIn());
        }

        userMapper.updateById(user);
        user.setPasswordHash(null);
        return user;
    }

    @Override
    public void sendMagicLink(EmailVerifyRequest request) {
        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, request.getEmail()));

        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(
                REDIS_KEY_MAGIC_LINK + token,
                request.getEmail(),
                MAGIC_LINK_EXPIRE_SECONDS,
                TimeUnit.SECONDS);

        log.info("Magic link sent to {}: {}", request.getEmail(), token);
    }

    @Override
    public TokenResponse verifyMagicLink(String token) {
        String key = REDIS_KEY_MAGIC_LINK + token;
        String email = redisTemplate.opsForValue().get(key);

        if (email == null) {
            throw new IllegalArgumentException("Invalid or expired magic link");
        }

        redisTemplate.delete(key);

        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getEmail, email));

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new IllegalArgumentException("Account is disabled");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("User logged in via magic link: id={}, email={}", user.getId(), email);
        return generateTokenPair(user);
    }

    @Override
    public void sendTwoFactorCode(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String code = String.format("%06d", (int) (Math.random() * 1000000));
        redisTemplate.opsForValue().set(
                REDIS_KEY_2FA_CODE + userId,
                code,
                TWO_FACTOR_CODE_EXPIRE_SECONDS,
                TimeUnit.SECONDS);

        log.info("2FA code for user {} ({}): {}", userId, user.getEmail(), code);
    }

    @Override
    public void verifyTwoFactorCode(Long userId, String code) {
        String key = REDIS_KEY_2FA_CODE + userId;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null || !storedCode.equals(code)) {
            throw new IllegalArgumentException("Invalid or expired verification code");
        }

        redisTemplate.delete(key);

        String verifiedKey = REDIS_KEY_2FA_VERIFIED + userId;
        redisTemplate.opsForValue().set(verifiedKey, "1", 7200, TimeUnit.SECONDS);

        log.info("2FA verified for user: {}", userId);
    }

    private TokenResponse generateTokenPair(UserEntity user) {
        String accessToken = jwtUtil.generate(user.getId(), user.getEmail());

        String refreshToken = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(
                REDIS_KEY_REFRESH + refreshToken,
                String.valueOf(user.getId()),
                REFRESH_TOKEN_EXPIRE_SECONDS,
                TimeUnit.SECONDS);

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                ACCESS_TOKEN_EXPIRE_SECONDS);
    }
}
