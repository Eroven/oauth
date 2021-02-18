package me.zhaotb.tps.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.tps.bean.AuthInfo;
import me.zhaotb.tps.bean.AuthToken;
import me.zhaotb.tps.config.AuthConfig;
import me.zhaotb.tps.config.MockUser;
import me.zhaotb.tps.oauth.OAuthClient;
import me.zhaotb.tps.oauth.OauthProtocolConst;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author zhaotangbo
 * @date 2021/2/5
 */
@Slf4j
@Controller
public class IndexController implements ApplicationContextAware {

    /**
     * session属性，保存用户对象
     */
    public static final String S_USER = "S_USER";
    /**
     * session属性，保存oauth2的state参数
     */
    public static final String S_STATE = "S_STATE";
    public static final String S_AUTH_TOKEN = "S_AUTH_TOKEN";


    private ApplicationContext context;

    @Value("${server.port}")
    private int port;

    @Value("${server.address:localhost}")
    private String address;

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private MockUser user;

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("reqUrl", "http://" + authConfig.getServerHost() + ":"
                + authConfig.getServerPort() + authConfig.getAuthUrl() + "?response_type="
                + OauthProtocolConst.ResponseType.RESPONSE_CODE + "&client_id=" + authConfig.getClientId()
                + "&redirect_uri=" + redirectUri() + "&state=" + request.getSession().getAttribute(S_STATE)
                + "&scope=/user/*");
        return "index";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("do-login")
    public String doLogin(HttpServletRequest request, String account, String password) {
        if (user.getAccount().equals(account) && user.getPassword().equals(password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute(S_USER, user);
            session.setAttribute(S_STATE, DigestUtils.sha256Hex(request.getRemoteHost() + "." + account + "." + System.currentTimeMillis()));
        }
        return "redirect:/index";
    }

    @RequestMapping("/doAuth2")
    public String doAuth2(HttpServletRequest request, String state, String code, Model model) {
        HttpSession session = request.getSession();
        if (state != null && state.equals(session.getAttribute(S_STATE))) {
            AuthToken authToken = OAuthClient.doAuth(code, redirectUri(), authConfig);
            session.setAttribute(S_AUTH_TOKEN, authToken);
        }

        return "redirect:/index";
    }

    private String redirectUri() {
        try {
            return URLEncoder.encode("http://" + address + ":" + port + "/doAuth2", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
            return "";
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
