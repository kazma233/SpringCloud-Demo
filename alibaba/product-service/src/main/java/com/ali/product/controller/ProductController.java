package com.ali.product.controller;

import com.ali.entity.Prodcut;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    List<Prodcut> productList = Lists.newArrayList(
            new Prodcut("1", "拿铁", new BigDecimal("31.8")),
            new Prodcut("2", "摩卡", new BigDecimal("32.8")),
            new Prodcut("3", "卡布奇诺", new BigDecimal("29.8"))
    );

    @GetMapping
    public List<Prodcut> getProduct() {
        return productList;
    }

}
