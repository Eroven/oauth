package me.zhaotb.oauth.server.service;


import me.zhaotb.oauth.server.bean.AuthInfo;

/**
 * 管理auth 2.0 的token，包含临时授权码、访问码、刷新码
 * @author zhaotangbo
 * @date 2021/2/4
 */
public interface AuthTokenService {

    enum CacheType {
        /**
         * 临时授权码 temporaryToken
         */
        TT,
        /**
         * 访问码 accessToken
         */
        AT,
        /**
         * 刷新码 refreshToken
         */
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

}
