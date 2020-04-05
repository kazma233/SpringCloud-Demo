package com.product.server.controller;

import com.product.server.dto.CartDTO;
import com.product.server.dto.ProductDTO;
import com.product.server.dto.ProductInfoDTO;
import com.product.server.dto.ResultDTO;
import com.product.server.entity.ProductCategory;
import com.product.server.entity.ProductInfo;
import com.product.server.service.ProductCategoryService;
import com.product.server.service.ProductService;
import com.product.server.util.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping(value = "/list", produces = {"application/json"})
    public ResultDTO<List<ProductDTO>> list() {

        // 查询所有在架商品
        List<ProductInfo> upAll = productService.findUpAll();

        // 获得商品type
        List<Integer> typeList = upAll.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());

        // 所有type
        List<ProductCategory> categoryTypes = productCategoryService.findByCategoryTypeIn(typeList);

        // 构造数据
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductCategory productCategory : categoryTypes) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setCategoryName(productCategory.getCategoryName());
            productDTO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoDTO> productInfoDTOList = new ArrayList<>();
            for (ProductInfo productInfo : upAll) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoDTO productInfoDTO = new ProductInfoDTO();
                    BeanUtils.copyProperties(productInfo, productInfoDTO);
                    productInfoDTOList.add(productInfoDTO);
                }
            }
            productDTO.setProductInfoDTOS(productInfoDTOList);
            productDTOList.add(productDTO);
        }
        return ResultUtils.success(productDTOList);
    }

    /**
     * 获取商品列表 给订单服务使用
     *
     * @param productIdList 产品id
     */
    @PostMapping("/listForOrder")
    public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList) {
        return productService.findList(productIdList);
    }

    /**
     * 减库存
     */
    @PostMapping("/decreaseStock")
    public String decreaseStock(@RequestBody List<CartDTO> cartDTOS) {
        productService.decreaseStock(cartDTOS);
        return "ok";
    }

}
