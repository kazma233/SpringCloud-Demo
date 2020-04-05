package com.product.server.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.product.common.Cons;
import com.product.common.ProductInfoOutput;
import com.product.server.dao.ProductDao;
import com.product.server.dto.CartDTO;
import com.product.server.entity.ProductInfo;
import com.product.server.enums.ProductStatusEnum;
import com.product.server.enums.ResultEnum;
import com.product.server.exception.ProductException;
import com.product.server.service.ProductService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 查询在架的商品
     */
    @Override
    public List<ProductInfo> findUpAll() {
        return productDao.findAllByProductStatus(ProductStatusEnum.UP.getCode());
    }

    /**
     * 通过id查询商品
     */
    @Override
    public List<ProductInfo> findList(List<String> productIdList) {
        return productDao.findByProductIdIn(productIdList);
    }

    /**
     * 扣库存
     */
    @Override
    public void decreaseStock(List<CartDTO> cartDTOS) {
        List<ProductInfo> productInfos = decStock(cartDTOS);

        // 完成整个购物车后才扣库存
        List<ProductInfoOutput> productInfoOutputs = productInfos.stream().map(productInfo -> {
            // 使用Common下的对象
            ProductInfoOutput productInfoOutput = new ProductInfoOutput();
            BeanUtils.copyProperties(productInfo, productInfoOutput);
            return productInfoOutput;
        }).collect(Collectors.toList());

        amqpTemplate.convertAndSend(Cons.QUEUE_NAME, gson.toJson(productInfoOutputs));
    }

    @Transactional
    public List<ProductInfo> decStock(List<CartDTO> cartDTOS) {
        List<ProductInfo> productInfos = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOS) {
            Optional<ProductInfo> productInfo = productDao.findById(cartDTO.getProductId());
            // 商品是否存在
            if (!productInfo.isPresent()) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            // 库存是否可以够
            ProductInfo pi = productInfo.get();
            Integer last = pi.getProductStock() - cartDTO.getProductQuantity();
            if (last < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            pi.setProductStock(last);
            productDao.save(pi);

            productInfos.add(pi);
        }
        return productInfos;
    }

}
