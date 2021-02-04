package me.zhaotb.oauth.server.config;

/**
 * oauth 2.0 协议相关的常量
 * @author zhaotangbo
 * @date 2021/2/4
 */
public final class OauthProtocolConst {

    /**
     * 简化模式
     */
    public static final String IMPLICIT = "accessToken";

    /**
     * 密码模式
     */
    public static final String RESOURCE_OWNER_PASSWORD_CREDENTIALS = "password";

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "clientcredentials";

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


}
