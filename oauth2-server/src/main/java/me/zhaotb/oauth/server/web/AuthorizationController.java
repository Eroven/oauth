package me.zhaotb.oauth.server.web;


import lombok.extern.slf4j.Slf4j;
import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.AuthToken;
import me.zhaotb.oauth.server.service.UserService;
import me.zhaotb.oauth.server.util.Attr;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
@Slf4j
@Controller
public class AuthorizationController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "authorization/auth")
    public String auth(Model model,@Attr AuthInfo authInfo) {
        model.addAttribute("auth", authInfo);
        return "auth";
    }

    @PostMapping("authorization/auth-login")
    public String login(Model model, @Attr AuthInfo authInfo, HttpServletResponse response) {
        String code = userService.authCode(authInfo);
        if (code != null) {
            StringBuilder uri = new StringBuilder(authInfo.getRedirectUri());
            uri.append("?code=").append(code);
            if (StringUtils.isNotBlank(authInfo.getState())) {
                uri.append("&state=").append(authInfo.getState());
            }
            try {
                response.sendRedirect(uri.toString());
            } catch (IOException e) {
                log.error("重定向异常: {}", uri.toString(), e);
            }
        }
        //重新授权
        model.addAttribute("auth", authInfo);
        model.addAttribute("error", true);
        return "auth";
    }

    @PostMapping("authorization/token")
    @ResponseBody
    public AuthToken getToken(@Attr AuthInfo authInfo) {
        AuthToken authToken = userService.authToken(authInfo);

        return authToken;
    }

}
