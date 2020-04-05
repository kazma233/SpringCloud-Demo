package com.product.server.dto;

import lombok.Data;

/**
 * 减库存
 */
@Data
public class CartDTO {

    /**
     * 商品id
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productQuantity;

}
