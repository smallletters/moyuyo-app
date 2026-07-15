package com.moyuyo.common.security;

public class UserContextHolder {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static void clear() {
        USER_ID_HOLDER.remove();
        TOKEN_HOLDER.remove();
    }
}
