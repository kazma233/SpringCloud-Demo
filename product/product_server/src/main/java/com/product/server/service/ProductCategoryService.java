package com.product.server.service;


import com.product.server.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes);

}
