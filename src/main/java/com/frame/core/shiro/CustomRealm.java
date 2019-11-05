package com.frame.core.shiro;

import com.frame.web.base.login.BaseUser;
import com.frame.web.base.login.LoginUserDao;
import com.frame.web.base.login.LoginUserRoleDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginUserDao loginUserdao;
    @Autowired
    private LoginUserRoleDao loginUserRoleDao;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        BaseUser loginUser = (BaseUser) (principalCollection.getPrimaryPrincipal());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user"); // 初始角色user
        // 获取人员角色
        List<String> roles = loginUserRoleDao.findAllByAccount(loginUser.getAccount()).parallelStream()
                .map(ref -> ref.getRole()).collect(Collectors.toList());
        authorizationInfo.addRoles(roles);
        // 获取人员权限
        //        List<String> permissions = new ArrayList<>();
        //        permissions.add("userInfo:add");
        //        authorizationInfo.addStringPermissions(permissions);
        // 缓存角色(对jwt无效)
        //        ShiroUtil.getSession().setAttribute("roles", roles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String account = JWTUtil.getAccount(token);
        String password = JWTUtil.getPassword(token);
        String loginType = JWTUtil.getLoginType(token);
        if (StringUtils.isEmpty(account)) {
            throw new UnknownAccountException("账号不能为空!");
        }
        if (StringUtils.isEmpty(password) &&
                LoginType.PASSWORD.getCode().equals(loginType)) {
            throw new IncorrectCredentialsException("账号或密码错误!");
        }
        BaseUser loginUser = loginUserdao.findByAccount(account);
        if (loginUser == null) {
            throw new UnknownAccountException("不存在的账号");
        }
        return new SimpleAuthenticationInfo(loginUser, token, this.getName());
    }
}
