package com.zjrb.passport.net;

import java.io.UnsupportedEncodingException;

/**
 * Date: 2018/6/28 下午5:53
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 返回体封装,默认为字节流
 */
public class ResponseBody {

    byte[] bytes;

    public ResponseBody(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes() {
        return this.bytes;
    }

    public String string() {
        try {
            return new String(bytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("字节流转为UTF-8格式的String异常");
        }
    }

}
