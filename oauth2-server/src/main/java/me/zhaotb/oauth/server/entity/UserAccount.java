package me.zhaotb.oauth.server.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Data
public class UserAccount implements Serializable {

    private static final long serialVersionUID = -2047366827438800273L;
    private String account;
    private String password;
    private String nickname;
    private List<String> friends;

}
