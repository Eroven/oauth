package me.zhaotb.tps.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * OAuth 2.0 结构体
 * @author zhaotangbo
 * @date 2021/2/1
 */
@Data
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = 6360592293646708825L;

    private String grantType;

    private String responseType;

    private String clientId;

    private String clientSecret;

    private String state;

    private String redirectUri;

    private String scope;

    private String code;

    private String tokenType;

    private String refreshToken;

    private String accessToken;

    private String account;

    private String password;


}
