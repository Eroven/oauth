package me.zhaotb.oauth.server.bean;


import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhaotb.oauth.server.entity.UserAccount;

import java.io.Serializable;
import java.util.List;

/**
 * 用户脱敏信息
 * @author zhaotangbo
 * @date 2021/2/4
 */
@Data
@NoArgsConstructor
public class UserAccountDesensitized implements Serializable {

    private static final long serialVersionUID = 2157133532701843616L;
    private String account;
    private String nickname;
    private List<String> friends;

    public UserAccountDesensitized(UserAccount userAccount) {
        this.account = userAccount.getAccount();
        this.nickname = userAccount.getNickname();
        this.friends = userAccount.getFriends();
    }

}
