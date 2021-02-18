package me.zhaotb.oauth.server.config;


import me.zhaotb.oauth.server.bean.AuthInfo;
import me.zhaotb.oauth.server.bean.UserAccountJwtPayload;
import me.zhaotb.oauth.server.service.AuthTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通过路径直接判定权限
 * @author zhaotangbo
 * @date 2021/2/4
 */
public class PathPermissionInterceptor implements HandlerInterceptor {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private AuthTokenService authTokenService;

    @Autowired(required = false)
    public void setAuthTokenService(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authTokenService == null) {
            //全部放行
            return true;
        }
        //格式： bearer token
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String[] split = header.split(" ");
        final int len = 2;
        if (split.length != len) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        UserAccountJwtPayload payload = authTokenService.getAccountFromAccessToken(split[1], UserAccountJwtPayload.class);
        if (payload == null || payload.getExpiration() < System.currentTimeMillis()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        List<String> permissions = payload.getPathPermissions();
        if (permissions == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //判定是否具有访问路径的权限
        for (String permission : permissions) {
            if (antPathMatcher.match(permission, request.getRequestURI())) {
                request.setAttribute("userAccount", payload.getUserAccount());
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

}
