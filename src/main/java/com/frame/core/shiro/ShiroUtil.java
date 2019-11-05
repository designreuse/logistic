package com.frame.core.shiro;

import com.frame.web.base.login.BaseUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Collection;

public class ShiroUtil {

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        return getSubject().getSession();
    }

    public static BaseUser getCurrentUser() {
        return (BaseUser) getSubject().getPrincipal();
    }

    public static boolean hasRole(String s){
        return getSubject().hasRole(s);
    }

    public static boolean hasAllRoles(Collection<String> roles){
        return getSubject().hasAllRoles(roles);
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null && SecurityUtils.getSubject().isAuthenticated();
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

}
