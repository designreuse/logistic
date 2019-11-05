package com.frame.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

/**
 * 用户密码加密算法公用类
 *
 * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
 *         十二月 06, 2016
 * @version 1.0
 */
public class EncryptUtil {

    /**
     * 产生一个随机的32位的UUID(不包含"-"字符)
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取字符串的MD5哈希值.
     *
     * @param source
     * @return
     */
    public static String md5Hash(String source) {
        return StringUtils.isBlank(source) ? null : new Md5Hash(source).toHex();
    }

    /**
     * 用户密码加密
     *
     * @param password
     * @param salt
     * @param hashIterations
     * @return
     */
    public static String simpleHash(String password, String salt, int hashIterations) {
        return (new SimpleHash("MD5", password, ByteSource.Util.bytes(salt), hashIterations)).toHex();
    }
}
