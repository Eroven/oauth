package me.zhaotb.tps.oauth;

/**
 * oauth 2.0 协议相关的常量
 * @author zhaotangbo
 * @date 2021/2/4
 */
public final class OauthProtocolConst {

    /**
     * 客户端请求类型
     */
    public final class GrantType {
        /**
         * 授权码请求
         */
        public static final String AUTHORIZATION_CODE = "authorization_code";

        /**
         * 刷新授权码请求
         */
        public static final String REFRESH_TOKEN = "refresh_token";

    }

    /**
     * 服务端响应类型
     */
    public final class ResponseType {
        /**
         * 访问码类型
         */
        public static final String RESPONSE_CODE = "code";
    }

    /**
     * 指令类型
     */
    public final class TokenType {
        public static final String BEARER = "bearer";

        public static final String MAC = "mac";
    }

    /**
     * scope分割符
     */
    public static final String SCOPE_PERMISSION_SEPARATOR = ";";


    /**
     * 访问码失效时间3分钟
     */
    public static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 3;

    /**
     * 刷新码失效时间1小时
     */
    public static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60;



}
