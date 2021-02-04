package me.zhaotb.oauth.server.config;


import me.zhaotb.oauth.server.util.BeanUtils;
import me.zhaotb.oauth.server.util.Attr;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;

/**
 * 处理下划线属性转驼峰的情况.自定义注解，指定下划线格式
 * @see Attr
 * @author zhaotangbo
 * @date 2021/2/3
 */
public class UnderlineAttributeProcessor extends ServletModelAttributeMethodProcessor {
    /**
     * Class constructor.
     *
     * @param annotationNotRequired if "true", non-simple method arguments and
     *                              return values are considered model attributes with or without a
     *                              {@code @ModelAttribute} annotation
     */
    public UnderlineAttributeProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    /**
     * 请求参数带有{@link Attr}注解的才去解析
     * @param parameter 参数
     * @return true：可以解析；false:不解析
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Attr.class);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        super.bindRequestParameters(binder, request);
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        new UnderlineDataBinder(binder.getTarget()).bind(servletRequest);
    }

    private class UnderlineDataBinder extends ServletRequestDataBinder {
        public UnderlineDataBinder(Object target) {
            super(target);
        }

        public UnderlineDataBinder(Object target, String objectName) {
            super(target, objectName);
        }

        @Override
        protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
            Object target = getTarget();
            Field[] fields = BeanUtils.decleardFields(target.getClass());
            for (Field field : fields) {
                Attr attr = field.getAnnotation(Attr.class);
                if (attr != null && mpvs.contains(attr.value())) {
                    mpvs.addPropertyValue(field.getName(), mpvs.getPropertyValue(attr.value()).getValue());
                }
            }
        }
    }


}
