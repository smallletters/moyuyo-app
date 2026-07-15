package com.moyuyo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.FlashSaleEntity;

public interface FlashSaleService {

    Page<FlashSaleEntity> listActive(int page, int size);

    FlashSaleEntity getFlashSaleDetail(Long id);

    void placeFlashOrder(Long userId, Long flashSaleId, Integer quantity);
}
