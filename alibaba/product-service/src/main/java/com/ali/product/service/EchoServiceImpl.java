package com.ali.product.service;

import com.ali.rpc.EchoService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return message;
    }

}
