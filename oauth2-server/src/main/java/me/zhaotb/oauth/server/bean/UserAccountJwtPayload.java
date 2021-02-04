package me.zhaotb.oauth.server.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * jwt数据载体，增加了用户信息和路径权限。这里直接把accessToken格式和refreshToken合在一起了。
 * @author zhaotangbo
 * @date 2021/2/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountJwtPayload extends BaseJwtPayload {

    /**
     * 用户账号信息。accessToken用
     */
    private UserAccountDesensitized userAccount;

    /**
     * 路径权限。accessToken和refreshToken用
     */
    private List<String> pathPermissions;

    /**
     * 用户账号。refreshToken用
     */
    private String account;

}
