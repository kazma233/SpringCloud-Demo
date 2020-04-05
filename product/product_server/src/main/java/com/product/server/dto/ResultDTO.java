package com.product.server.dto;

import lombok.Data;

// Http 响应对象
@Data
public class ResultDTO<T> {

    private Integer code;
    private String msg;
    private T data;

}
