package com.moyuyo.service.impl;

import com.moyuyo.common.dto.shipping.ShippingRateResponse;
import com.moyuyo.dao.entity.AddressEntity;
import com.moyuyo.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ShippingServiceImpl implements ShippingService {

    @Override
    public List<ShippingRateResponse> estimateRates(AddressEntity address, BigDecimal weight) {
        BigDecimal baseRate = BigDecimal.valueOf(5.99);
        String country = address != null ? address.getCountry() : "US";

        if (weight != null && weight.compareTo(BigDecimal.TEN) > 0) {
            baseRate = BigDecimal.valueOf(12.99);
        }

        return Arrays.asList(
                new ShippingRateResponse("USPS", "Standard", baseRate, "3-5", false),
                new ShippingRateResponse("USPS", "Express", baseRate.add(BigDecimal.valueOf(8)), "1-2", false),
                new ShippingRateResponse("FedEx", "Ground", BigDecimal.valueOf(7.99), "2-4", false),
                new ShippingRateResponse("FedEx", "Overnight", BigDecimal.valueOf(19.99), "1", false)
        );
    }

    @Override
    public ShippingRateResponse getDefaultRate() {
        return new ShippingRateResponse("USPS", "Standard", BigDecimal.valueOf(5.99), "3-5", false);
    }
}
