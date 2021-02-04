package me.zhaotb.oauth.server.service.impl;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.service.AuthTokenService;

import java.util.concurrent.TimeUnit;


/**
 * @author zhaotangbo
 * @date 2021/2/4
 */
@Slf4j
public class AuthTokenServiceMockImpl implements AuthTokenService {

    /**
     * 临时授权码缓存
     */
    private Cache<String, AuthInfo> temporaryTokenCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(3, TimeUnit.MINUTES)
                    .build();

    /**
     * 访问码缓存
     */
    private Cache<String, AuthInfo> accessTokenCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(3, TimeUnit.MINUTES)
                    .build();

    /**
     * 刷新码缓存
     */
    private Cache<String, AuthInfo> refreshTokenCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(60, TimeUnit.MINUTES)
                    .build();

    @Override
    public void cacheCode(CacheType type, String token, AuthInfo authInfo) {
        switch (type) {
            case TT:
                temporaryTokenCache.put(token, authInfo);
                break;
            case AT:
                accessTokenCache.put(token, authInfo);
                break;
            case RT:
                refreshTokenCache.put(token, authInfo);
                break;
                default:
        }
    }

    @Override
    public AuthInfo getCache(CacheType type, String token) {
        switch (type) {
            case TT:
                return temporaryTokenCache.getIfPresent(token);
            case AT:
                return accessTokenCache.getIfPresent(token);
            case RT:
                return refreshTokenCache.getIfPresent(token);
            default:
                return null;
        }
    }

    @Override
    public void invalidate(CacheType type, String token) {
        switch (type) {
            case TT:
                temporaryTokenCache.invalidate(token);
                break;
            case AT:
                accessTokenCache.invalidate(token);
                break;
            case RT:
                refreshTokenCache.invalidate(token);
                break;
            default:
        }
    }
}
