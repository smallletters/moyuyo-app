package com.moyuyo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.FlashSaleEntity;
import com.moyuyo.dao.entity.FlashSaleOrderEntity;
import com.moyuyo.dao.mapper.FlashSaleMapper;
import com.moyuyo.dao.mapper.FlashSaleOrderMapper;
import com.moyuyo.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleMapper flashSaleMapper;
    private final FlashSaleOrderMapper flashSaleOrderMapper;

    @Override
    public Page<FlashSaleEntity> listActive(int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        return flashSaleMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<FlashSaleEntity>()
                        .eq(FlashSaleEntity::getActive, true)
                        .le(FlashSaleEntity::getStartTime, now)
                        .ge(FlashSaleEntity::getEndTime, now)
                        .orderByAsc(FlashSaleEntity::getEndTime));
    }

    @Override
    public FlashSaleEntity getFlashSaleDetail(Long id) {
        FlashSaleEntity entity = flashSaleMapper.selectById(id);
        if (entity == null) throw new IllegalArgumentException("活动不存在");
        return entity;
    }

    @Override
    @Transactional
    public void placeFlashOrder(Long userId, Long flashSaleId, Integer quantity) {
        FlashSaleEntity flash = flashSaleMapper.selectById(flashSaleId);
        if (flash == null) throw new IllegalArgumentException("活动不存在");
        if (!flash.getActive()) throw new IllegalStateException("活动已结束");

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(flash.getStartTime())) throw new IllegalStateException("活动尚未开始");
        if (now.isAfter(flash.getEndTime())) throw new IllegalStateException("活动已结束");

        int remain = flash.getTotalStock() - flash.getSoldStock();
        if (remain < quantity) throw new IllegalStateException("库存不足，仅剩 " + remain + " 件");

        if (flash.getLimitPerUser() != null && flash.getLimitPerUser() > 0) {
            long userBought = flashSaleOrderMapper.selectCount(
                    new LambdaQueryWrapper<FlashSaleOrderEntity>()
                            .eq(FlashSaleOrderEntity::getFlashSaleId, flashSaleId)
                            .eq(FlashSaleOrderEntity::getUserId, userId));
            if (userBought + quantity > flash.getLimitPerUser()) {
                throw new IllegalStateException("每人限购 " + flash.getLimitPerUser() + " 件");
            }
        }

        FlashSaleOrderEntity order = new FlashSaleOrderEntity();
        order.setFlashSaleId(flashSaleId);
        order.setUserId(userId);
        order.setQuantity(quantity);
        flashSaleOrderMapper.insert(order);

        flash.setSoldStock(flash.getSoldStock() == null ? quantity : flash.getSoldStock() + quantity);
        flashSaleMapper.updateById(flash);

        log.info("Flash order placed: flashSaleId={}, userId={}, qty={}", flashSaleId, userId, quantity);
    }
}
