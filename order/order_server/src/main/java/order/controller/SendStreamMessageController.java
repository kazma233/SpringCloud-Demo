package order.controller;

import order.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * StreamClient 和 StreamReciver 使用
 */
@RestController
public class SendStreamMessageController {

    @Autowired
    private StreamClient streamClient;

    @GetMapping("/sendStream")
    public boolean process() {
        return streamClient.output().send(
                MessageBuilder.withPayload(
                        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                ).build()
        );
    }


}
