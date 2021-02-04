package me.zhaotb.oauth.server.bean;


import lombok.Data;

/**
 * jwt数据载体
 * @author zhaotangbo
 * @date 2021/2/4
 */
@Data
public abstract class BaseJwtPayload {

    /**
     * 创建时间
     */
    private long issuedAt;
    /**
     * 失效时间
     */
    private long expiration;


}
