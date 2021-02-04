package me.zhaotb.oauth.server.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.BaseJwtPayload;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhaotangbo
 * @date 2021/2/4
 */
@Slf4j
public class JwtUtil {

    private static ObjectMapper jsonMapper = new JsonMapper();

    /**
     * 生成jwt
     * @param payload 数据载体
     * @param secret jwt密钥
     * @return jwt字符串；
     */
    public static String genJwt(BaseJwtPayload payload, String secret) {
        try {
            return Jwts.builder()
                    .setPayload(jsonMapper.writeValueAsString(payload))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析jwt字符串
     * @param <T> 目标类泛型
     * @param token jwt字符串
     * @param secret jwt密钥
     * @param clazz 目标bean
     * @return 目标对象；返回null表示解析失败，或者jwt已经过期
     */
    public static <T extends BaseJwtPayload> T parseJwt(String token, String secret, Class<T> clazz) {
        JwtParser parser = Jwts.parser();
        if (StringUtils.isBlank(token) || !parser.isSigned(token)) {
            return null;
        }
        try {
            parser.setSigningKey(secret);
            parser.parse(token);
            String payload = TextCodec.BASE64URL.decodeToString(token.split("\\.")[1]);
            log.debug("payload: {}", payload);
            T t = jsonMapper.readValue(payload, clazz);
            if (t.getExpiration() > System.currentTimeMillis()) {
                return t;
            }
        } catch (Exception e) {
            log.error(token, e);
        }
        return null;
    }

}
