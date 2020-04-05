package order.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    PARAM_ERROR(1, "参数错误"),
    ITEM_EMPTY(2, "购物车为空"),
    ORDER_STATU_ERROR(3, "订单状态错误"),
    ORDER_DETAIL_NOT_EXITS(4, "订单详情为空")
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
