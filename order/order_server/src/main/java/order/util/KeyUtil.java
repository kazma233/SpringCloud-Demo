package order.util;

import java.util.Random;

public class KeyUtil {

    /**
     * 生成唯一主键
     * 格式: 时间戳+随机数
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer ran = random.nextInt(900000) + 100000;
        return String.valueOf(System.currentTimeMillis() + ran);
    }

}
