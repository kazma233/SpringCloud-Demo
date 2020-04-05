package com.sc.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class FallbackController {

    @RequestMapping("/order")
    public String fallbackOrder() {
        return "this order request is break";
    }

    @RequestMapping("/product")
    public String fallbackProduct() {
        return "this product request is break";
    }

}
