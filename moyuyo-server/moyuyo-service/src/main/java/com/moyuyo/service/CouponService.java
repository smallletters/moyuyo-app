package com.moyuyo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.CouponEntity;

import java.util.List;

public interface CouponService {

    Page<CouponEntity> listAvailable(int page, int size);

    CouponEntity getCouponDetail(Long id);

    void claimCoupon(Long userId, Long couponId);

    List<CouponEntity> listUserCoupons(Long userId, String status);

    void useCoupon(Long userId, Long userCouponId, Long orderId);
}
