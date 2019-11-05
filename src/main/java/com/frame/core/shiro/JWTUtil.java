package com.frame.core.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public static final String ACCESS_TOKEN_CODE = "access_token";

    public static final String PASSWORD_CODE = "access_password";

    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
//    public static final long EXPIRE_TIME = 60 * 1000;

    public static final String PWD_TYPE_CODE = "pwdType";

    public static final String LOGIN_TYPE_CODE = "loginType";

    private static final String ACCESS_KEY = "theAccessKey20190820";

    public static String sign(String account, String sessionId) {
        return sign(account, sessionId, null, null, LoginType.NOPASSWD);
    }

    public static String sign(String account, String sessionId, String password) {
        return sign(account, sessionId, password, null, LoginType.PASSWORD);
    }

    public static String sign(String account, String sessionId, String password, String pwdType, LoginType loginType) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(ACCESS_KEY);
        Builder builder = JWT.create().withJWTId(sessionId).withIssuer(account).withExpiresAt(date);
        builder.withClaim(LOGIN_TYPE_CODE, loginType.getCode());
        if (!StringUtils.isEmpty(pwdType)) {
            builder.withClaim(PWD_TYPE_CODE, password);
        }
        if (!StringUtils.isEmpty(password)) {
            builder.withClaim(PASSWORD_CODE, password);
        }
        return builder.sign(algorithm);
    }

    public static String getSessionId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getId();
    }

    public static String getAccount(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getIssuer();
    }

    public static String getPassword(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(PASSWORD_CODE).asString();
    }

    public static String getLoginType(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(LOGIN_TYPE_CODE).asString();
    }

    public static String getPwdType(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(PWD_TYPE_CODE).asString();
    }

    public static JwtVerifyResult verify(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(ACCESS_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(username).build();
            verifier.verify(token);
            logger.info(">>>>>>>>>>>>>>>>jwt token 验证通过");
            return JwtVerifyResult.pass;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            logger.warn(">>>>>>>>>>token验证失败***IllegalArgumentException********{}", e.getMessage());
            return JwtVerifyResult.fail_other;
        } catch (JWTVerificationException e) {
            logger.warn(">>>>>>>>>>token验证失败***JWTVerificationException******{}", e.getMessage());
            if (e instanceof TokenExpiredException) {
                return JwtVerifyResult.fail_expired;
            }
            return JwtVerifyResult.fail_other;
        }
    }

    public enum JwtVerifyResult {
        pass("验证通过"), fail_expired("验证失败，token过期"), fail_other("验证失败,参数有误或编码错误");

        private final String title;

        JwtVerifyResult(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }

    }
}

