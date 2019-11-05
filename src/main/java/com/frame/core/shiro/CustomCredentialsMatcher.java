package com.frame.core.shiro;

import com.frame.core.utils.EncryptUtil;
import com.frame.web.base.login.BaseUser;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        String token = (String) authenticationToken.getCredentials();
        BaseUser loginUser = (BaseUser)(info.getPrincipals().getPrimaryPrincipal());
        if(LoginType.NOPASSWD.getCode().equals(JWTUtil.getLoginType(token))){
            return true;
        }else if(!loginUser.getPassword().equals(EncryptUtil.simpleHash(JWTUtil.getPassword(token), loginUser.getSalt(),2))){
            throw new IncorrectCredentialsException("密码错误!");
        }
        return super.doCredentialsMatch(authenticationToken,info);
    }
}
