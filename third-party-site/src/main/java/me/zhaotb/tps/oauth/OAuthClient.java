package me.zhaotb.tps.oauth;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.tps.bean.AuthToken;
import me.zhaotb.tps.config.AuthConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * oauth2 授权码模式的客户端方
 * @author zhaotangbo
 * @date 2021/2/18
 */
@Slf4j
public abstract class OAuthClient {

    public static AuthToken doAuth(String code, String redirectUri, AuthConfig config) {
        try {
            URL url = new URL("http", config.getServerHost(), config.getServerPort(), config.getTokenUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(HttpMethod.POST.name());
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "gzip");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream outputStream = conn.getOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append("grantType=").append(OauthProtocolConst.GrantType.AUTHORIZATION_CODE)
                    .append("&code=").append(code)
                    .append("&redirectUri=").append(redirectUri)
                    .append("&clientId=").append(config.getClientId())
                    .append("&clientSecret=").append(config.getClientPassword());
            outputStream.write(sb.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            sb.delete(0, sb.length());
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            AuthToken authToken = mapper.readValue(sb.toString(), AuthToken.class);
            log.info("{}", authToken);
            return authToken;

        } catch (MalformedURLException e) {
            log.error("url格式错误", e);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
