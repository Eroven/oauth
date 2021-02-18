package me.zhaotb.tps.config;


import lombok.Data;
import me.zhaotb.tps.bean.AuthToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zhaotangbo
 * @date 2021/2/18
 */
@Data
@Profile("mock")
@Configuration
@ConfigurationProperties(prefix = "user")
public class MockUser {

    private String account;
    private String password;

    private AuthToken token;

}
