package me.zhaotb.oauth.server.service.impl;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.bean.UserAccountJwtPayload;
import me.zhaotb.oauth.server.config.MockDataConfig;
import me.zhaotb.oauth.server.config.OauthProtocolConst;
import me.zhaotb.oauth.server.entity.UserAccount;
import me.zhaotb.oauth.server.service.AuthTokenService;
import me.zhaotb.oauth.server.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

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
            return AuthToken.empty();
        }
        //grant_type为空，认为也是 authorization_code
        if (StringUtils.isEmpty(authInfo.getGrantType()) || OauthProtocolConst.GrantType.AUTHORIZATION_CODE.equals(authInfo.getGrantType())) {
            String key = getKey(authInfo);
            @Nullable AuthInfo cache = authTokenService.getCache(AuthTokenService.CacheType.TT, key);
            if (cache == null) {
                return AuthToken.empty();
            }
            //临时授权码置为无效
            authTokenService.invalidate(AuthTokenService.CacheType.TT, key);

            UserAccount userAccount = getByAccount(cache.getAccount());
            String accessToken = authTokenService.genAccessToken(cache, userAccount);
            String refreshToken = authTokenService.genRefreshToken(cache);
            return authTokenService.buildTokenObj(accessToken, refreshToken);
        } else if (OauthProtocolConst.GrantType.REFRESH_TOKEN.equals(authInfo.getGrantType())) {
            String refreshToken = authInfo.getRefreshToken();
            UserAccountJwtPayload payload = authTokenService.getAccountFromRefreshToken(refreshToken, UserAccountJwtPayload.class);
            if (payload == null || payload.getExpiration() < System.currentTimeMillis()) {
                return AuthToken.empty();
            }
            UserAccount userAccount = getByAccount(payload.getAccount());
            AuthInfo tmp = new AuthInfo();
            tmp.setScope(StringUtils.join(payload.getPathPermissions(), OauthProtocolConst.SCOPE_PERMISSION_SEPARATOR));
            String accessToken = authTokenService.genAccessToken(tmp, userAccount);
            return authTokenService.buildTokenObj(accessToken, authInfo.getRefreshToken());
        }
        log.warn("未知授权模式：{}", authInfo.getGrantType());
        return AuthToken.empty();
    }

}
