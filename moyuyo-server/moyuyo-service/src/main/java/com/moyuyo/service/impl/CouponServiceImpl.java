package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.CouponEntity;
import com.moyuyo.dao.entity.UserCouponEntity;
import com.moyuyo.dao.mapper.CouponMapper;
import com.moyuyo.dao.mapper.UserCouponMapper;
import com.moyuyo.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    @Override
    public Page<CouponEntity> listAvailable(int page, int size) {
        return couponMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<CouponEntity>()
                        .eq(CouponEntity::getActive, true)
                        .le(CouponEntity::getStartTime, LocalDateTime.now())
                        .ge(CouponEntity::getEndTime, LocalDateTime.now())
                        .orderByDesc(CouponEntity::getCreateTime));
    }

    @Override
    public CouponEntity getCouponDetail(Long id) {
        CouponEntity entity = couponMapper.selectById(id);
        if (entity == null) throw new IllegalArgumentException("优惠券不存在");
        return entity;
    }

    @Override
    @Transactional
    public void claimCoupon(Long userId, Long couponId) {
        CouponEntity coupon = couponMapper.selectById(couponId);
        if (coupon == null) throw new IllegalArgumentException("优惠券不存在");
        if (!coupon.getActive()) throw new IllegalStateException("优惠券已失效");
        if (coupon.getEndTime().isBefore(LocalDateTime.now())) throw new IllegalStateException("优惠券已过期");

        long claimed = userCouponMapper.selectCount(
                new LambdaQueryWrapper<UserCouponEntity>()
                        .eq(UserCouponEntity::getUserId, userId)
                        .eq(UserCouponEntity::getCouponId, couponId));
        if (claimed > 0) throw new IllegalStateException("已领取过该优惠券");

        if (coupon.getTotalCount() != null && coupon.getClaimedCount() != null
                && coupon.getClaimedCount() >= coupon.getTotalCount()) {
            throw new IllegalStateException("优惠券已被领完");
        }

        UserCouponEntity userCoupon = new UserCouponEntity();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus("UNUSED");
        userCouponMapper.insert(userCoupon);

        coupon.setClaimedCount(coupon.getClaimedCount() == null ? 1 : coupon.getClaimedCount() + 1);
        couponMapper.updateById(coupon);

        log.info("Coupon claimed: userId={}, couponId={}", userId, couponId);
    }

    @Override
    public List<CouponEntity> listUserCoupons(Long userId, String status) {
        LambdaQueryWrapper<UserCouponEntity> uw = new LambdaQueryWrapper<UserCouponEntity>()
                .eq(UserCouponEntity::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            uw.eq(UserCouponEntity::getStatus, status);
        }

        List<UserCouponEntity> userCoupons = userCouponMapper.selectList(uw);
        if (userCoupons.isEmpty()) return java.util.Collections.emptyList();

        List<Long> couponIds = userCoupons.stream()
                .map(UserCouponEntity::getCouponId).collect(Collectors.toList());
        return couponMapper.selectBatchIds(couponIds);
    }

    @Override
    @Transactional
    public void useCoupon(Long userId, Long userCouponId, Long orderId) {
        UserCouponEntity userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) throw new IllegalArgumentException("优惠券记录不存在");
        if (!userCoupon.getUserId().equals(userId)) throw new IllegalArgumentException("无权操作此优惠券");
        if (!"UNUSED".equals(userCoupon.getStatus())) throw new IllegalStateException("优惠券已使用或已过期");

        userCoupon.setStatus("USED");
        userCoupon.setUsedTime(LocalDateTime.now());
        userCoupon.setUsedOrderId(orderId);
        userCouponMapper.updateById(userCoupon);

        CouponEntity coupon = couponMapper.selectById(userCoupon.getCouponId());
        if (coupon != null) {
            coupon.setUsedCount(coupon.getUsedCount() == null ? 1 : coupon.getUsedCount() + 1);
            couponMapper.updateById(coupon);
        }
        log.info("Coupon used: userCouponId={}, orderId={}", userCouponId, orderId);
    }
}
