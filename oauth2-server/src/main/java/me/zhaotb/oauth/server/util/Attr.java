package me.zhaotb.oauth.server.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 下划线命名方式的变量
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Attr {

    /**
     * @return 绑定变量变量名
     */
    String value() default "";
}
