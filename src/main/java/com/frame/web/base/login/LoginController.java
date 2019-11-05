package com.frame.web.base.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frame.core.http.HttpStatus;
import com.frame.core.http.ResponseEntity;
import com.frame.core.shiro.JWTUtil;
import com.frame.core.shiro.LoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/plogin")
    public ResponseEntity login(@RequestBody String loginInfo) {
        JSONObject obj = JSON.parseObject(loginInfo);
        ResponseEntity resp = LoginUtil.login(obj.getString("Username"), obj.getString("Password"));
        if (resp.isOk()) {
            return resp;
        } else {
            return resp;
        }
    }

    @PostMapping("/wxlogin")
    public ResponseEntity nopwdlogin() {
        ResponseEntity resp = LoginUtil.login("admin");
        if (resp.isOk()) {
            return resp.put("name", "admin");
        } else {
            return resp;
        }
    }

    @RequestMapping("/tokenExpire")
    public ResponseEntity tokenExpired() {
        return ResponseEntity.error(HttpStatus.TOKEN_TIMEOUT, JWTUtil.JwtVerifyResult.fail_expired.title(), JWTUtil.JwtVerifyResult.fail_expired.title());
    }

    @RequestMapping("/Illegaltoken")
    public ResponseEntity tokenIllega() {
        return ResponseEntity.error(HttpStatus.REQUEST_PARAMS_ILLEGAL, JWTUtil.JwtVerifyResult.fail_other.title(), JWTUtil.JwtVerifyResult.fail_other.title());
    }
}
