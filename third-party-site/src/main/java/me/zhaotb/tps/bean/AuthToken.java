package me.zhaotb.tps.bean;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 指令类, 包含了指令创建时间,值和生命时长
 * @author zhaotangbo
 */
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthToken implements Serializable {

    private static final long serialVersionUID = -7242512547819747971L;

    private String tokenType;

    private Date createTime;

    private String accessToken;

    private long tokenEffectiveSeconds;

    private String refreshToken;

    private long refreshTokenEffectiveSeconds;

}
