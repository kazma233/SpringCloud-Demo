package com.product.server.service;

import com.product.server.dto.CartDTO;
import com.product.server.entity.ProductInfo;

import java.util.List;

public interface ProductService {

    /**
     * 查询所有在架商品
     */
    public List<ProductInfo> findUpAll();

    /**
     * 通过id查询商品
     */
    public List<ProductInfo> findList(List<String> productIdList);

    /**
     * 减少库存
     */
    void decreaseStock(List<CartDTO> cartDTOS);

}
