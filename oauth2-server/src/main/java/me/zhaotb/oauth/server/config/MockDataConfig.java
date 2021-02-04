package me.zhaotb.oauth.server.config;


import lombok.Data;
import me.zhaotb.oauth.server.bean.UserAccount;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 模拟数据配置
 *
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Data
@ConfigurationProperties(prefix = "mock")
@Configuration
public class MockDataConfig {

    /**
     * 域名
     */
    private String domain;

    /**
     * 用户信息
     */
    private UserAccount userAccount;

    /**
     * 注册过的第三方客户端
     */
    private Map<String, String> registerClient;

}
