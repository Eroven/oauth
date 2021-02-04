package me.zhaotb.oauth.server.service.impl;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.bean.UserAccount;
import me.zhaotb.oauth.server.config.OauthProtocolConst;
import me.zhaotb.oauth.server.config.MockDataConfig;
import me.zhaotb.oauth.server.service.AuthTokenService;
import me.zhaotb.oauth.server.service.UserService;
import me.zhaotb.oauth.server.util.RandomUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Slf4j
public class UserServiceMockImpl implements UserService {

    private MockDataConfig mockData;

    private AuthTokenService authTokenService;

    @Autowired
    public void setMockData(MockDataConfig mockData) {
        this.mockData = mockData;
    }

    @Autowired
    public void setAuthTokenService(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public UserAccount getByAccount(String account) {
        return mockData.getUserAccount().getAccount().equals(account) ? mockData.getUserAccount() : null;
    }

    @Override
    public UserAccount login(String account, String password) {
        UserAccount userAccount = mockData.getUserAccount();
        if (userAccount != null &&
                userAccount.getAccount().equals(account) &&
                userAccount.getPassword().equals(password)) {
            return userAccount;
        }
        return null;
    }

    @Override
    public String authCode(AuthInfo authInfo) {
        if (login(authInfo.getAccount(), authInfo.getPassword()) == null) {
            return null;
        }
        String code = DigestUtils.sha256Hex(authInfo.getAccount() + System.currentTimeMillis());
        authInfo.setCode(code);
        authTokenService.cacheCode(AuthTokenService.CacheType.TT, getKey(authInfo), authInfo);
        return code;
    }

    private String getKey(AuthInfo authInfo) {
        return authInfo.getCode() + "::" + authInfo.getClientId() + "::" + authInfo.getRedirectUri();
    }

    @Override
    public AuthToken authToken(AuthInfo authInfo) {
        Map<String, String> registerClient = mockData.getRegisterClient();
        String secret = registerClient.get(authInfo.getClientId());
        if (secret == null || !secret.equals(authInfo.getClientSecret())) {
            //未授权客户端
            return null;
        }
        //grant_type为空，认为也是 authorization_code
        if (StringUtils.isEmpty(authInfo.getGrantType()) || OauthProtocolConst.GrantType.AUTHORIZATION_CODE.equals(authInfo.getGrantType())) {
            String key = getKey(authInfo);
            @Nullable AuthInfo cache = authTokenService.getCache(AuthTokenService.CacheType.TT, key);
            if (cache == null) {
                return null;
            }
            //临时授权码置为无效
            authTokenService.invalidate(AuthTokenService.CacheType.TT, key);
            String accessToken = RandomUtil.randomNumber(64);
            String refreshToken = RandomUtil.randomNumber(64);
            cache.setAccessToken(accessToken);
            cache.setRefreshToken(refreshToken);
            UserAccount userAccount = getByAccount(cache.getAccount());
            cache.setUserAccount(userAccount);
            authTokenService.cacheCode(AuthTokenService.CacheType.AT, accessToken, cache);
            authTokenService.cacheCode(AuthTokenService.CacheType.RT, refreshToken, cache);
            return buildTokenObj(accessToken, refreshToken);
        } else if (OauthProtocolConst.GrantType.REFRESH_TOKEN.equals(authInfo.getGrantType())) {
            @Nullable AuthInfo cache = authTokenService.getCache(AuthTokenService.CacheType.RT, authInfo.getRefreshToken());
            if (cache == null) {
                return null;
            }
            //如果之前的accessToken还没过期，主动将之前的accessToken清掉
            authTokenService.invalidate(AuthTokenService.CacheType.AT, cache.getAccessToken());
            String accessToken = RandomUtil.randomNumber(64);
            cache.setAccessToken(accessToken);
            authTokenService.cacheCode(AuthTokenService.CacheType.AT, accessToken, cache);
            return buildTokenObj(accessToken, authInfo.getRefreshToken());
        }
        log.warn("未知授权模式：{}", authInfo.getGrantType());
        return null;
    }

    private AuthToken buildTokenObj(String token, String refreshToken) {
        AuthToken tokenObj = new AuthToken();
        tokenObj.setTokenType(OauthProtocolConst.TokenType.BEARER);
        tokenObj.setCreateTime(new Date());
        tokenObj.setAccessToken(token);
        tokenObj.setTokenEffectiveSeconds(60 * 3);
        tokenObj.setRefreshToken(refreshToken);
        tokenObj.setRefreshTokenEffectiveSeconds(60 * 60);
        return tokenObj;
    }
}
