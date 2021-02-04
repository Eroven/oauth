package me.zhaotb.oauth.server.web;


import me.zhaotb.oauth.server.bean.UserAccount;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@RestController
public class UserController {

    @RequestMapping("user/nickname")
    public String nickname(@RequestAttribute("userAccount") UserAccount userAccount) {
        return userAccount.getNickname();
    }

    @RequestMapping("user/friends")
    public List<String> friends(@RequestAttribute("userAccount")UserAccount userAccount) {
        return userAccount.getFriends();
    }


}
