package me.zhaotb.oauth.server.config;


import me.zhaotb.oauth.server.service.AuthTokenService;
import me.zhaotb.oauth.server.service.UserService;
import me.zhaotb.oauth.server.service.impl.AuthTokenServiceMockImpl;
import me.zhaotb.oauth.server.service.impl.UserServiceMockImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Profile("mock")
@Configuration
public class MockConfig {

    @Bean
    public UserService userServiceMock() {
        return new UserServiceMockImpl();
    }

    @Bean
    public AuthTokenService authTokenServiceMock() {
        return new AuthTokenServiceMockImpl();
    }


}
