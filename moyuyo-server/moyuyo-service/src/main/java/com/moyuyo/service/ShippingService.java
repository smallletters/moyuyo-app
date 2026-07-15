package com.moyuyo.service;

import com.moyuyo.common.dto.shipping.ShippingRateResponse;
import com.moyuyo.dao.entity.AddressEntity;

import java.util.List;

public interface ShippingService {

    List<ShippingRateResponse> estimateRates(AddressEntity address, java.math.BigDecimal weight);

    ShippingRateResponse getDefaultRate();
}
