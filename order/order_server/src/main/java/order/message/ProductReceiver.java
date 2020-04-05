package order.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.product.common.Cons;
import com.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 扣库存消息的接受
 */

@Component
@Slf4j
public class ProductReceiver {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private static final String PRODUCT_STOCK_REDIS_KEY = "PRODUCT_STOCK_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue(Cons.QUEUE_NAME))
    public void processDecStock(String message) {
        List<ProductInfoOutput> productInfoOutputs = gson.fromJson(message, new TypeToken<List<ProductInfoOutput>>(){}.getType());
        log.info("[{}]接收到口库存消息: {}", Cons.QUEUE_NAME, productInfoOutputs);

        // 存到redis上
        for (ProductInfoOutput productInfoOutput : productInfoOutputs) {
            stringRedisTemplate.opsForValue().set(
                    String.format(PRODUCT_STOCK_REDIS_KEY, productInfoOutput.getProductId()),
                    String.valueOf(productInfoOutput.getProductStock())
            );
        }
    }

}
