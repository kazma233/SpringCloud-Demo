package com.ali.order.controller;

import com.ali.entity.Prodcut;
import com.ali.rpc.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Reference(version = "1.0")
    private EchoService echoService;

    @PostMapping
    public void create() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://product-service/products").build();
        List<Prodcut> prodcutList = restTemplate.exchange(
                uriComponents.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Prodcut>>() {
                }
        ).getBody();

        log.info("product list is: {}", prodcutList);
    }

    @GetMapping("echo")
    public String list(String echo) {
        return echoService.echo(echo);
    }

}
