package me.zhaotb.oauth.server.util;


import java.util.Random;
import java.util.UUID;

/**
 * @author zhaotangbo
 * @since 2020/12/29
 */
public class RandomUtil {

    /**
     * 随机一串数字
     * @param len 长度
     * @return 代码随机数字的字符串
     */
    public static String randomNumber(int len) {
        StringBuilder sb  = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
