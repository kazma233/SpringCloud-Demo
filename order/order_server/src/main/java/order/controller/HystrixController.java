package order.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import order.util.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController

// 默认的熔断方法
@DefaultProperties(defaultFallback = "breakHandle")
public class HystrixController {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

//    // 超时配置
//    @HystrixCommand(fallbackMethod = "breakHandle", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//    })
    @HystrixCommand(fallbackMethod = "breakHandle",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            // 最小请求数 This property sets the minimum number of requests in a rolling window that will trip the circuit.
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            // 休眠时间窗口 This property sets the amount of time, after tripping the circuit, to reject requests before allowing attempts again to determine if the circuit should again be closed.
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000"),
            // 错误百分比 This property sets the error percentage at or above which the circuit should trip open and start short-circuiting requests to fallback logic.
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
    })
    @GetMapping("/getProductList")
    public String getProductList() {
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.postForObject(
                "http://127.0.0.1:9201/product/listForOrder",
                Collections.singletonList("164103465734242707"),
                String.class);
        return res;
    }

    private String breakHandle() {
        return gson.toJson(ResultUtils.error());
    }
}
