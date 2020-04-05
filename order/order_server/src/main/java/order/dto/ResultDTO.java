package order.dto;

import lombok.Data;

@Data
public class ResultDTO<T> {

    private Integer code;
    private String msg;
    private T data;

}
