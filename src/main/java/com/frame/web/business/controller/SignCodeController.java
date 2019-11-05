package com.frame.web.business.controller;

import com.frame.core.http.ResponseEntity;
import com.frame.core.utils.CommonUtil;
import com.frame.core.utils.HttpClientUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证码模块
 */
@RestController
@RequestMapping("/signer")
public class SignCodeController {

    @GetMapping("/sign")
    public ResponseEntity sign(@Param("signCode")String signCode, HttpServletRequest request){
        String code = request.getSession().getAttribute("signCode").toString();
        if(signCode.equals(code)){
            return ResponseEntity.ok();
        }else{
            return ResponseEntity.error("验证失败");
        }
    }

    @GetMapping("/code")
    public ResponseEntity getCode(@Param("mobile")String mobile, HttpServletRequest request){
        String code = String.valueOf(CommonUtil.signCode(6));
        request.getSession().setAttribute("signCode",code);
        CommonUtil.timerTask((obj->{
            obj.getSession().removeAttribute("signCode");
        }),request,60);
        sendMessage(mobile,code);
        return ResponseEntity.ok();
    }

    private void sendMessage(String mobile,String code){

    }

    public static void main(String[] args) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("Username", "admin");
        variables.put("Password", "123456");
        Map response = HttpClientUtil.post("http://localhost:8081/login/plogin", null, Map.class);
        System.out.println(response);
    }
}
