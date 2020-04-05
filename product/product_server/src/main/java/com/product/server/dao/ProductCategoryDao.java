package com.product.server.dao;

import com.product.server.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findAllByCategoryTypeIn(List<Integer> categoryTypes);

}
