//package order.controller;
//
//import com.product.client.ProductClient;
//import com.product.common.ProductInfoOutput;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import order.dto.ResultDTO;
//import order.util.ResultUtils;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.function.Supplier;
//
//@RestController
//@RequestMapping("/breakers")
//@AllArgsConstructor
//@Slf4j
//public class BreakersController {
//
//    private ProductClient productClient;
//    private CircuitBreakerFactory circuitBreakerFactory;
//
//    @GetMapping("/")
//    public ResultDTO list() {
//        List<ProductInfoOutput> productInfoOutputs = circuitBreakerFactory.
//                create("product-list").
//                run(listProduct(Collections.singletonList("164103465734242707")),
//                        throwable -> {
//                            log.error("breakers", throwable);
//                            return Collections.emptyList();
//                        }
//                );
//
//        return ResultUtils.success(productInfoOutputs);
//    }
//
//    public Supplier<List<ProductInfoOutput>> listProduct(List<String> ids) {
//        return () -> productClient.listForOrder(ids);
//    }
//
//}
