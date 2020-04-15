package com.ali.product.service;

import com.ali.rpc.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

@Slf4j
@Service(version = "1.0")
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        log.info("{}", message);
        return message;
    }

}
