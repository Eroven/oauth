package me.zhaotb.oauth.server.web;


import me.zhaotb.oauth.server.bean.UserAccountDesensitized;
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
    public String nickname(@RequestAttribute("userAccount") UserAccountDesensitized userAccount) {
        return userAccount.getNickname();
    }

    @RequestMapping("user/friends")
    public List<String> friends(@RequestAttribute("userAccount")UserAccountDesensitized userAccount) {
        return userAccount.getFriends();
    }


}
