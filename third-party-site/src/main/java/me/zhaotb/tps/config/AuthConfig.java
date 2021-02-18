package me.zhaotb.tps.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaotangbo
 * @date 2021/2/18
 */
@Data
@ConfigurationProperties(prefix = "auth")
@Configuration
public class AuthConfig {

    /**
     * 授权服务主机
     */
    private String serverHost;

    /**
     * 授权服务端口
     */
    private int serverPort;

    /**
     * 发起授权的url
     */
    private String authUrl;

    /**
     * 根据授权码获取token的url
     */
    private String tokenUrl;

    /**
     * 授权方给的id
     */
    private String clientId;

    /**
     * 授权方给的密码
     */
    private String clientPassword;

}
