package me.zhaotb.oauth.server.config;


import me.zhaotb.oauth.server.bean.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
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

    private JwtConfig jwtConfig;

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UnderlineAttributeProcessor(true));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置配置的url pattern进行拦截
        registry.addInterceptor(pathPermissionInterceptor()).addPathPatterns(jwtConfig.getPatterns()).order(1);
    }

    @Bean
    public PathPermissionInterceptor pathPermissionInterceptor() {
        return new PathPermissionInterceptor();
    }
}
