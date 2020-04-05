package order.message;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface StreamClient {

    public String INPUT = "msgInput";
    public String OUTPUT = "msgOuput";

    // 1. version 1
    @Input(StreamClient.INPUT)
    SubscribableChannel input();

    @Output(StreamClient.OUTPUT)
    MessageChannel output();

    // 回答 回复消费

    public String ACK_INPUT = "ackMsgInput";
    public String ACK_OUTPUT = "ackMsgOuput";

    @Input(StreamClient.ACK_INPUT)
    SubscribableChannel ackInput();

    @Output(StreamClient.ACK_OUTPUT)
    MessageChannel ackOuput();
}
