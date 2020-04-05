package com.product.server.util;


import com.product.server.dto.ResultDTO;

public class ResultUtils {

    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setCode(0);
        resultDTO.setMsg("成功");
        resultDTO.setData(data);
        return resultDTO;
    }

}
