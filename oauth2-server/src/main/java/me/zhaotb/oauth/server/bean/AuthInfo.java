package me.zhaotb.oauth.server.bean;

import lombok.Data;
import me.zhaotb.oauth.server.config.OauthProtocolConst;
import me.zhaotb.oauth.server.entity.UserAccount;
import me.zhaotb.oauth.server.util.Attr;

import java.io.Serializable;

/**
 * OAuth 2.0 结构体
 * @author zhaotangbo
 * @date 2021/2/1
 */
@Data
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = 6360592293646708825L;
    /**
     * 授权模式 {@link OauthProtocolConst.ResponseType#RESPONSE_CODE}
     */
    @Attr("grant_type")
    private String grantType;

    @Attr("response_type")
    private String responseType;

    @Attr("client_id")
    private String clientId;

    @Attr("client_secret")
    private String clientSecret;

    private String state;

    @Attr("redirect_uri")
    private String redirectUri;

    private String scope;

    private String code;

    @Attr("token_type")
    private String tokenType;

    @Attr("refresh_token")
    private String refreshToken;

    @Attr("access_token")
    private String accessToken;

    private String account;

    private String password;

    private UserAccount userAccount;

}
