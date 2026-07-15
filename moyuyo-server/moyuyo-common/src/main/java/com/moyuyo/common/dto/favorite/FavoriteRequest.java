package com.moyuyo.common.dto.favorite;

import lombok.Data;

@Data
public class FavoriteRequest {

    private Long productId;

    private Long skuId;

    private Long groupId;
}
