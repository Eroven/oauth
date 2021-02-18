package me.zhaotb.oauth.server.service;


import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.bean.BaseJwtPayload;
import me.zhaotb.oauth.server.config.OauthProtocolConst;
import me.zhaotb.oauth.server.entity.UserAccount;

import java.util.Date;

/**
 * 管理auth 2.0 的token，包含临时授权码、访问码、刷新码，以及jwt的密钥
 * @author zhaotangbo
 * @date 2021/2/4
 */
public interface AuthTokenService {

    /**
     * 构建token对象
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     * @return AuthToken
     */
    AuthToken buildTokenObj(String accessToken, String refreshToken);

    enum CacheType {
        /**
         * 临时授权码 temporaryToken
         */
        TT,
        /**
         * 访问码 accessToken
         * @deprecated 使用jwt则不需要缓存访问码
         */
        @Deprecated
        AT,
        /**
         * 刷新码 refreshToken
         * @deprecated 使用jwt则不需要缓存刷新码
         */
        @Deprecated
        RT
    }

    /**
     * 缓存数据
     * @param type 缓存类型
     * @param token 码
     * @param authInfo 授权信息
     */
    void cacheCode(CacheType type, String token, AuthInfo authInfo);

    /**
     * 获取缓存
     * @param type 缓存类型
     * @param token 码
     * @return 授权信息
     */
    AuthInfo getCache(CacheType type, String token);

    /**
     * 失效
     * @param type 缓存类型
     * @param token 码
     */
    void invalidate(CacheType type, String token);

    /**
     * 根据授权信息生成accessToken的JWT
     * @param authInfo 授权信息
     * @param userAccount 用户信息
     * @return jwt
     */
    String genAccessToken(AuthInfo authInfo, UserAccount userAccount);

    /**
     * 根据授权信息生成refreshToken的JWT
     * @param authInfo 授权信息
     * @return jwt
     */
    String genRefreshToken(AuthInfo authInfo);

    /**
     *
     * 从accessToken从获取账号信息
     * @param accessToken  访问码
     * @param clazz 数据载体bean type
     * @return jwt数据载体
     */
    <T extends BaseJwtPayload> T getAccountFromAccessToken(String accessToken, Class<T> clazz);

    /**
     * 从refreshToken从获取账号信息
     * @param refreshToken  刷新码
     * @param clazz 数据载体bean type
     * @return jwt数据载体
     */
    <T extends BaseJwtPayload> T getAccountFromRefreshToken(String refreshToken, Class<T> clazz);

}
