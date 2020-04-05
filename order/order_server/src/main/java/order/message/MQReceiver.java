package order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * MQ 接收方
 */
@Slf4j
@Component
public class MQReceiver {

    // 1. 需要手动创建队列
    // @RabbitListener(queues = "myQueue")
    // 2. 自动创建队列
    //  @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    // 3. 自动创建和exchange绑定
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),
            exchange = @Exchange("myExchange")
    ))
    public void process(String message) {
        log.info(message);
    }

    // 假设成两个服务

    // 只接受电子产品的队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("computerQueue"),
            key = "computer", // 表示只接受computer的消息 routing-key
            exchange = @Exchange("myExchange")
    ))
    public void computer(String message) {
        log.info("computerQueue: " + message);
    }

    // 只接受水果的队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fruitQueue"),
            key = "fruit",
            exchange = @Exchange("myExchange")
    ))
    public void fruit(String message) {
        log.info("fruitQueue: " + message);
    }

}
