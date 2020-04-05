package order.controller;

import com.product.client.ProductClient;
import com.product.common.DecreaseStockInput;
import com.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import order.dto.CartDTO;
import order.entity.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RefreshScope()
public class ClientController {

//    @Autowired
//    private LoadBalancerClient loadBalancerClient;

    //    @Autowired
//    private RestTemplate restTemplate;

//    @GetMapping("/getProductMsg")
//    public String getProcutMessage() {
        // 1. 第一种方式（直接使用RestTemplate） 但是写死了IP，可是目标地址可能随时会变，甚至有两个
//        RestTemplate restTemplate = new RestTemplate();
//        String msg = restTemplate.getForObject("http://127.0.0.1:9201/msg", String.class);
//        log.info("msg = {}", msg);

        // 2. 第二种方式 (使用Spring Cloud的loadBalancerClient通过应用名获得url, 然后再使用RestTemplate) 负载均衡 获得ip和端口
//        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
//        String url = String.format("http://%s:%s/msg", serviceInstance.getHost(), serviceInstance.getPort());
//        String msg = restTemplate.getForObject(url, String.class);
//        log.info("msg = {}", msg);

        // 3. 第三种方式(使用@LoadBalanced 直接使用应用名字访问)
//        String msg = restTemplate.getForObject("http://PRODUCT/msg", String.class);
//        return msg;
//    }

   /* @Autowired
    private ProductClient productClient;

    @GetMapping("/getProductList")
    public List<ProductInfoOutput> getProductList() {
        List<ProductInfoOutput> productInfos = productClient.listForOrder(Arrays.asList("157875196366160022", "157875227953464068"));
        log.info("response = {}", productInfos);
        return productInfos;
    }

    @GetMapping("/doDec")
    public String doDec() {
        productClient.decreaseStock(Collections.singletonList(
                new DecreaseStockInput("157875196366160022", 3)
        ));
        return "ok";
    }*/

    @Value("${env}")
    private String env;

    @GetMapping("/port")
    public String printEnv() {
        return env;
    }

}
