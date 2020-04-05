package com.product.server.dao;

import com.product.server.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<ProductInfo, String> {

    List<ProductInfo> findAllByProductStatus(Integer productStatus);

    List<ProductInfo> findByProductIdIn(List<String> productIdList);

}
