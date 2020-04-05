package com.product.server.service.impl;

import com.product.server.dao.ProductCategoryDao;
import com.product.server.entity.ProductCategory;
import com.product.server.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypes) {
        return productCategoryDao.findAllByCategoryTypeIn(categoryTypes);
    }

}
