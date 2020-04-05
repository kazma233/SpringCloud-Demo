package order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(StreamClient.class)
public class StreamReciver {

    // 1. version 1
    @StreamListener(StreamClient.INPUT)
    @SendTo(StreamClient.ACK_OUTPUT)
    public String process(String message) {
        log.info("接收到了数据: {}", message);
        return "确认被消费";
    }

    @StreamListener(StreamClient.ACK_INPUT)
    public void ackProcess(String message) {
        log.info("接收到了确认消费的消息: {}", message);
    }

}
