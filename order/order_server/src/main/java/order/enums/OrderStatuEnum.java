package order.enums;

import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum OrderStatuEnum {
    NEW(0, "新订单"),
    FINSHED(1, "完成"),
    CALCEL(2, "取消");

    private Integer code;
    private String msg;

    OrderStatuEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
