package me.zhaotb.oauth.server.bean;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhaotangbo
 * @since 2021/2/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥
     */
    private String secret;

    /**
     * AccessToken有效时长（毫秒），默认1小时
     */
    private long accessTokenExpiration =  1000 * 60 * 60;

    /**
     * refreshToken有效时长（毫秒），默认1天
     */
    private long refreshTokenExpiration =  1000 * 60 * 60 * 24;

    /**
     * 匹配需要校验的链接.注意前后不要留空格。与servlet的pattern格式一致，支持*和**通配
     */
    private List<String> patterns;

}
