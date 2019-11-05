package com.frame.core.shiro;

import com.frame.core.shiro.filter.JWTFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        // 过滤链
        Map<String, Filter> filtersMap = bean.getFilters();
        filtersMap.put("jwt", new JWTFilter());
        bean.setFilters(filtersMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/rest/**", "anon");

        //        filterChainDefinitionMap.put("/user/**", "anon");
        filterChainDefinitionMap.put("/sign/**", "anon");

        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/unlogin", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("loginout", "anon");

//        filterChainDefinitionMap.put("/**", "authc"); //常规认证
        filterChainDefinitionMap.put("/**", "jwt"); //jwt认证

        bean.setLoginUrl("/main/hello");
        bean.setUnauthorizedUrl("/auth_fail");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }

    @Bean
    public SecurityManager securityManager(CustomRealm realm, EhCacheManager cacheManager,SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        realm.setCredentialsMatcher(new CustomCredentialsMatcher());
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
        // mySessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }


    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * @return
     * @描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
     * </br>不使用注解的话，可以注释掉这两个配置
     * @创建人：wyait
     * @创建时间：2018年5月21日 下午6:07:56
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
