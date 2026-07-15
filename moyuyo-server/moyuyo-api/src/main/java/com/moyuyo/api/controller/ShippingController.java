package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.dto.shipping.ShippingRateResponse;
import com.moyuyo.service.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "运费管理")
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @Operation(summary = "获取默认运费")
    @GetMapping("/rate")
    public Result<ShippingRateResponse> getDefaultRate() {
        return Result.success(shippingService.getDefaultRate());
    }

    @Operation(summary = "估算运费（根据地址+重量）")
    @GetMapping("/estimate")
    public Result<List<ShippingRateResponse>> estimateRates(
            @RequestParam(required = false) String country,
            @RequestParam(required = false, defaultValue = "1") double weight) {
        com.moyuyo.dao.entity.AddressEntity addr = new com.moyuyo.dao.entity.AddressEntity();
        addr.setCountry(country != null ? country : "US");
        return Result.success(shippingService.estimateRates(addr, java.math.BigDecimal.valueOf(weight)));
    }
}
