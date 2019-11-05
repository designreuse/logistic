package com.frame.core.shiro.filter;

import com.frame.core.shiro.JWTUtil;
import com.frame.core.shiro.JWTUtil.JwtVerifyResult;
import com.frame.core.shiro.LoginUtil;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @ClassName: JWTFilter
 * @Description: TODO(token验证过滤器)
 * @date 2018年5月29日
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String JWT_VERIFY_FAIL_TYPE = "jwt_verify_fail_type";

    /**
     * 登录验证通过时处理
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String sessionToken = httpServletRequest.getHeader("access_token");
        if (sessionToken == null) {
            sessionToken = httpServletRequest.getParameter("access_token");
        }
        if (sessionToken != null) {
            String account = JWTUtil.getAccount(sessionToken);
            JwtVerifyResult jr = JWTUtil.verify(sessionToken, account);
            if (JwtVerifyResult.pass == jr) {
                LoginUtil.login(JWTUtil.getAccount(sessionToken));
            }
            httpServletRequest.setAttribute(JWT_VERIFY_FAIL_TYPE,jr);
            return JwtVerifyResult.pass == jr;
        } else {
            return false;
        }
    }

    /**
     * 验证失败处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        LOGGER.warn(">>>>token验证失败：{}", httpServletRequest.getAttribute(JWT_VERIFY_FAIL_TYPE));
        JwtVerifyResult verify = (JwtVerifyResult) httpServletRequest.getAttribute(JWT_VERIFY_FAIL_TYPE);
        /** token过期 */
        if (verify == JwtVerifyResult.fail_expired) {
            httpServletResponse.sendRedirect("/login/tokenExpire");
            return false;
        }
        /** 验证失败 */
        httpServletResponse.sendRedirect("/login/Illegaltoken");
        return false;
    }

}
