package me.zhaotb.oauth.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Configuration
public class AuthWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UnderlineAttributeProcessor(true));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pathPermissionInterceptor()).addPathPatterns("/resources/**", "/user/**").order(1);
    }

    @Bean
    public PathPermissionInterceptor pathPermissionInterceptor() {
        return new PathPermissionInterceptor();
    }
}
