package me.zhaotb.oauth.server.service;


import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.bean.UserAccount;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
public interface UserService {

    /**
     * 通过账号获取信息
     * @param account 账号
     * @return 用户账号信息
     */
    UserAccount getByAccount(String account);

    /**
     * 登录
     * @param account 账号
     * @param password 密码
     * @return 用户账号信息
     */
    UserAccount login(String account, String password);

    /**
     * 获取临时授权码. 需要参数： 账号、 密码、 client_id、 redirect_uri、 state（可选）
     * 其中，账号、密码是授权用户输入，其他的是请求方上一次请求缓存下来的参数
     * @param authInfo 授权信息
     * @return 授权码，实效3分钟
     */
    String authCode(AuthInfo authInfo);

    /**
     * 根据临时授权码获取accessToken和refreshToken
     * @param authInfo 请求参数，包含： grant_type，code，redirect_uri, client_id, client_secret
     * @return
     */
    AuthToken authToken(AuthInfo authInfo);

}
