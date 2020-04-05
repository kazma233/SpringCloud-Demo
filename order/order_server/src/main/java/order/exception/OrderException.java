package order.exception;

import order.enums.ResultEnum;

public class OrderException extends RuntimeException {

    private Integer code;

    public OrderException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public OrderException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
