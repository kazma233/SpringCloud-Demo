package order.util;

import order.dto.ResultDTO;

public class ResultUtils {

    public static ResultDTO success(Object o) {
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        resultDTO.setCode(0);
        resultDTO.setMsg("成功");
        resultDTO.setData(o);

        return resultDTO;
    }

    public static ResultDTO error() {
        ResultDTO<Object> resultDTO = new ResultDTO<>();
        resultDTO.setCode(-1);
        resultDTO.setMsg("失败");

        return resultDTO;
    }
}
