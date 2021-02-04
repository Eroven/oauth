package me.zhaotb.oauth.server.util;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean对象操作工具类
 * @author zhaotangbo
 * @date 2021/2/3
 */
public final class BeanUtils {

    private static Map<Class<?>, Field[]> fieldsCache = new ConcurrentHashMap<>();

    /**
     * 反射方法：从缓存中获取字段
     * @param clazz 目标类
     * @return 字段数组
     */
    public static Field[] decleardFields(Class<?> clazz) {
        return fieldsCache.computeIfAbsent(clazz, Class::getDeclaredFields);
    }

}
