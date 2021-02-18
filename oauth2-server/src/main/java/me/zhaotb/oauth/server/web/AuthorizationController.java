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
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 请求授权
     * @param model view
     * @param authInfo 包含字段：response_type=code ，client_id，redirect_uri，scope，state
     * @return 登录页面
     */
    @GetMapping(value = "authorization/auth")
    public String auth(Model model,@Attr AuthInfo authInfo) {
        model.addAttribute("auth", authInfo);
        return "auth";
    }

    /**
     * 用户登录后重定向到redirect_uri去
     * @param model view
     * @param authInfo 授权信息
     * @param response HttpServletResponse
     * @return 授权失败，返回登录页面继续授权。授权成功，重定向到指定uri并传递临时授权码code
     */
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

    /**
     * 第三方客户端通过后台请求，已获取accessToken和refreshToken
     * @param authInfo 包含字段：
     * @return 授权码。返回json格式为下换线分割的格式 {@link com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy}
     * @see Attr 兼容下划线和驼峰两种格式
     */
    @PostMapping("authorization/token")
    @ResponseBody
    public AuthToken getToken(@Attr AuthInfo authInfo) {
        AuthToken authToken = userService.authToken(authInfo);

        return authToken;
    }

}
