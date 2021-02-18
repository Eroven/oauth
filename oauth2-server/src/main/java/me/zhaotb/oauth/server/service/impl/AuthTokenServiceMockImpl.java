package me.zhaotb.oauth.server.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.bean.JwtConfig;
import me.zhaotb.oauth.server.bean.BaseJwtPayload;
import me.zhaotb.oauth.server.bean.UserAccountDesensitized;
import me.zhaotb.oauth.server.bean.UserAccountJwtPayload;
import me.zhaotb.oauth.server.config.OauthProtocolConst;
import me.zhaotb.oauth.server.entity.UserAccount;
import me.zhaotb.oauth.server.service.AuthTokenService;
import me.zhaotb.oauth.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.zhaotb.oauth.server.config.OauthProtocolConst.ACCESS_TOKEN_EXPIRATION;
import static me.zhaotb.oauth.server.config.OauthProtocolConst.REFRESH_TOKEN_EXPIRATION;


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
                    .expireAfterWrite(ACCESS_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS)
                    .build();

    /**
     * 访问码缓存
     */
    private Cache<String, AuthInfo> accessTokenCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(ACCESS_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS)
                    .build();

    /**
     * 刷新码缓存
     */
    private Cache<String, AuthInfo> refreshTokenCache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS)
                    .build();

    private ObjectMapper jsonMapper = new JsonMapper();

    private JwtConfig jwtConfig;

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

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

    @Override
    public String genAccessToken(AuthInfo authInfo, UserAccount userAccount) {
        UserAccountJwtPayload payload = new UserAccountJwtPayload();
        payload.setIssuedAt(System.currentTimeMillis());
        payload.setExpiration(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration());
        payload.setUserAccount(new UserAccountDesensitized(userAccount));
        payload.setPathPermissions(scopeToPermission(authInfo.getScope()));
        return JwtUtil.genJwt(payload, jwtConfig.getSecret());
    }

    /**
     * scope到permission的转换
     *
     * @param scope 授权信息
     * @return 路径权限
     */
    private List<String> scopeToPermission(String scope) {
        return Arrays.asList(scope.split(OauthProtocolConst.SCOPE_PERMISSION_SEPARATOR));
    }

    @Override
    public String genRefreshToken(AuthInfo authInfo) {
        UserAccountJwtPayload payload = new UserAccountJwtPayload();
        payload.setIssuedAt(System.currentTimeMillis());
        payload.setExpiration(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration());
        payload.setAccount(authInfo.getAccount());
        payload.setPathPermissions(scopeToPermission(authInfo.getScope()));
        return JwtUtil.genJwt(payload, jwtConfig.getSecret());
    }

    @Override
    public <T extends BaseJwtPayload> T getAccountFromAccessToken(String accessToken, Class<T> clazz) {
        return JwtUtil.parseJwt(accessToken, jwtConfig.getSecret(), clazz);
    }

    @Override
    public <T extends BaseJwtPayload> T getAccountFromRefreshToken(String refreshToken, Class<T> clazz) {
        return JwtUtil.parseJwt(refreshToken, jwtConfig.getSecret(), clazz);
    }

    @Override
    public AuthToken buildTokenObj(String accessToken, String refreshToken) {
        AuthToken tokenObj = new AuthToken();
        tokenObj.setTokenType(OauthProtocolConst.TokenType.BEARER);
        tokenObj.setCreateTime(new Date());
        tokenObj.setAccessToken(accessToken);
        tokenObj.setTokenEffectiveSeconds(jwtConfig.getAccessTokenExpiration() / 1000);
        tokenObj.setRefreshToken(refreshToken);
        tokenObj.setRefreshTokenEffectiveSeconds(jwtConfig.getRefreshTokenExpiration() / 1000);
        return tokenObj;
    }

}
