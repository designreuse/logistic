package com.frame.core.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 
    * @ClassName: MySessionManager  
    * @Description: TODO(自定义session管理)  
    * @author Administrator  
    * @date 2018年5月29日  
    *
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

	private static final String AUTHORIZATION = "access_token";

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {

		String token = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		//String sessionToken = WebUtils.toHttp(request).getHeader("Session_Token");

		// 如果请求头中有 Authorization 则其值为sessionId
		if (!StringUtils.isEmpty(token)) {
	    	String id = JWTUtil.getSessionId(token);
	    	
	        if (id != null) {  
	            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);  
	            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);  
	        } 

			return id;

		} else {

			// 否则按默认规则从cookie取sessionId
			return super.getSessionId(request, response);			
		}
	}
	
	

}
