package com.product.server.exception;


import com.product.server.enums.ResultEnum;

/**
 * 商品异常
 */
public class ProductException extends RuntimeException {

    private Integer code;

    public ProductException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public ProductException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
