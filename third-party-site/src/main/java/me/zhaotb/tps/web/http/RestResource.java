package me.zhaotb.tps.web.http;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author zhaotangbo
 * @since 2021/2/19
 */
public interface RestResource {

    @Get(value = "http://localhost:9001/user/friends")
    List<String> friends(@Header(HttpHeaders.AUTHORIZATION) String accessToken);

}
