package com.zjrb.passport.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.zjrb.passport.constant.ZbConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Function: EncryptUtil
 * <p>
 * Author: chen.h
 * Date: 2018/7/4
 */
public class EncryptUtil {

    public static String encrypt(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("%%");
        if (params != null && !params.isEmpty()) {
            for (String s : params.keySet()) {
                try {
                    sb.append(s).append("=").append(TextUtils.isEmpty(params.get(s)) ? "" : URLEncoder.encode(params.get(s), InnerConstant.DEF_CODE)).append("&"); // queryString拼接的时候value就需要进行encode
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            sb.setLength(sb.length() - 1);
            sb.append("%%");
        }
        sb.append(InnerConstant.SALT);
        return encrypt(sb.toString());
    }

    public static String encrypt(String str) {
        String result = str;
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(str.getBytes(InnerConstant.DEF_CODE));
            byte[] r = sha256.digest();
            result = byte2Hex(r);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp;
        if (bytes != null) {
            for (byte b : bytes) {
                temp = Integer.toHexString(b & 0xFF);
                if (temp.length() == 1) {
                    //1得到一位的进行补0操作
                    sb.append("0");
                }
                sb.append(temp);
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64 加密
     * @param text
     * @return
     */
    public static String base64Encrypt(@NonNull String text) {
        byte[] bytes = new byte[0];
        try {
            bytes = text.getBytes(InnerConstant.DEF_CODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP); // NO_WRAP会略去换行符
    }


    /**
     * sha256_HMAC加密
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byte2Hex(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    /**
     * 对密码进行加密,先进行RSA加密,然后进行Base64编码
     * @param password
     * @return
     */
    public static String encryptPassWord(String password) {
        if (password != null) {
            try {
                return Base64Utils.encode(RSAUtils.encryptByPublicKey(password.getBytes(), ZbConstants.PASSPORT_PUBLIC_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
