package com.frame.core.http;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lidong
 * @ClassName: ResponseEntity
 * @Description: TODO(数据实体)
 * @date 2018年12月11日
 */

public class ResponseEntity extends JSONObject {
    private static final long serialVersionUID = 1L;

    /**
     * 请求返回代码
     */
    public static final String RESULT_CODE = "code";
    /**
     * 自定义返回消息
     */
    public static final String RESULT_MSG = "msg";
    /**
     * 返回数据存储代码
     */
    public static final String DATA_CODE = "data";
    /**
     * token有效时间代码
     */
    public static final String EXPIRES_TIME_CODE = "expires_time";
    /**
     * 系统错误、异常信息描述
     */
    public static final String SYS_ERR_MSG = "sys_err_msg";


    public ResponseEntity() {
        put(RESULT_CODE, HttpStatus.OK.value());
        put(RESULT_MSG, HttpStatus.OK.getTitle());
        put(SYS_ERR_MSG, "");
        putData(new HashMap<>());
    }

    /*>>>>>>>>>>>>>>>>>>>>>>>>> OK - start >>>>>>>>>>>>>>>>>>>>>>>>*/

    /***
     * TODO(自定义返回消息) @param @param msg @param @return 参数 @return ResponseEntity
     * {"msg":"自定义消息","code":200} @Title: ok @Description:
     * 返回类型 @throws
     */
    public static ResponseEntity ok(String msg) {
        ResponseEntity responseEntity = ok();
        responseEntity.put(RESULT_MSG, msg);
        return responseEntity;
    }

    /**
     * TODO(返回map键值) @param @param map @param @return 参数 @return ResponseEntity
     * {"key1":"value1","key2":"value2","code":200} @Title: ok @Description:
     * {"key1":"value1","key2":"value2","code":200} @throws
     */
    public static ResponseEntity ok(Map<String, Object> map) {
        ResponseEntity responseEntity = ok();
        responseEntity.putAll(map);
        return responseEntity;
    }

    public static ResponseEntity ok() {
        return new ResponseEntity();
    }

    /*>>>>>>>>>>>>>>>>>>>>>>>>> OK - end >>>>>>>>>>>>>>>>>>>>>>>>*/

    /*>>>>>>>>>>>>>>>>>>>>>>>>> ERROR - start >>>>>>>>>>>>>>>>>>>*/

    public static ResponseEntity error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, msg, null);
    }

    public static ResponseEntity error(HttpStatus status) {
        return error(status, null, null);
    }

    /**
     * @param status
     * @param sysErrMsg 系统抛出的异常错误信息
     * @return
     */
    public static ResponseEntity error(HttpStatus status, String msg, String sysErrMsg) {
        ResponseEntity error = new ResponseEntity();
        error.put(RESULT_CODE, status.value());
        error.put(RESULT_MSG, msg != null ? msg : status.getTitle());
        error.put(SYS_ERR_MSG, sysErrMsg);
        return error;
    }

    /*>>>>>>>>>>>>>>>>>>>>>>>>> ERROR - end >>>>>>>>>>>>>>>>>>>>>*/


    public ResponseEntity putData(Object value) {
        super.put(DATA_CODE, value);
        return this;
    }

    public ResponseEntity put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 设置 access_token有效期（单位毫秒）
     *
     * @param value
     * @return
     */
    public ResponseEntity setExpiresTime(long value) {
        super.put(EXPIRES_TIME_CODE, value);
        return this;
    }

    public boolean isOk() {
        return (int) this.get(RESULT_CODE) == HttpStatus.OK.value();
    }
}
