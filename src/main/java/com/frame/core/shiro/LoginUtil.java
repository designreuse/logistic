package com.frame.core.shiro;

import com.frame.core.http.ResponseEntity;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

public class LoginUtil {

    /**
     * 常规登录
     * @param account 账号
     * @param password 密码
     * @return
     */
    public static ResponseEntity login(String account, String password) {
        try {
            Subject subject = ShiroUtil.getSubject();
            String sessionId = subject.getSession().getId().toString();
            String access_token = JWTUtil.sign(account, sessionId,null);
            String auth_token = JWTUtil.sign(account, sessionId, password);
            JWTToken token = new JWTToken(auth_token);
            subject.login(token);
            return ResponseEntity.ok().put(JWTUtil.ACCESS_TOKEN_CODE, access_token).setExpiresTime(JWTUtil.EXPIRE_TIME);
        } catch (UnknownAccountException uae) {
            return ResponseEntity.error("登陆名不存在或密码错误");  // 登陆名不存在
        } catch (IncorrectCredentialsException ice) {
            return ResponseEntity.error("登陆名不存在或密码错误");   // 密码错误
        } catch (LockedAccountException lae) {
            return ResponseEntity.error("账号[" + account + "]已锁定！");
        } catch (ExcessiveAttemptsException eae) {
            return ResponseEntity.error("账号[" + account + "]登录次数过多，已锁定！");
        } catch (AuthenticationException ae) {
            return ResponseEntity.error("对用户[" + account + "]验证未通关过，请联系系统管理员");
        }
    }

    /**
     * 免密码登录
     * @param username 账号
     * @return
     */
    public static ResponseEntity login(String username) {
        try {
            Subject subject = ShiroUtil.getSubject();
            String sessionId = subject.getSession().getId().toString();
            String access_token = JWTUtil.sign(username, sessionId);
            String auth_token = JWTUtil.sign(username, sessionId);
            JWTToken token = new JWTToken(auth_token);
            subject.login(token);
            return ResponseEntity.ok().put(JWTUtil.ACCESS_TOKEN_CODE, access_token).setExpiresTime(JWTUtil.EXPIRE_TIME);
        } catch (UnknownAccountException uae) {
            return ResponseEntity.error("登陆名不存在或密码错误");  // 登陆名不存在
        } catch (IncorrectCredentialsException ice) {
            return ResponseEntity.error("登陆名不存在或密码错误");   // 密码错误
        } catch (LockedAccountException lae) {
            return ResponseEntity.error("账号[" + username + "]已锁定！");
        } catch (ExcessiveAttemptsException eae) {
            return ResponseEntity.error("账号[" + username + "]登录次数过多，已锁定！");
        } catch (AuthenticationException ae) {
            return ResponseEntity.error("对用户[" + username + "]验证未通关过，请联系系统管理员");
        }
    }
}
