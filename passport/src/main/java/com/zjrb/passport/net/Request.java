package com.zjrb.passport.net;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.zjrb.passport.net.util.Util;

import java.util.Map;

/**
 * Date: 2018/6/28 下午3:34
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 建造者模式,封装请求,参考okhttp
 */
public class Request {

    final HttpMethod method;
    final String url;
    final Map<String, String> headers;
    final @Nullable RequestBody requestBody;

    public Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers;
        this.requestBody = builder.body;
    }

    public static class Builder {

        HttpMethod method;
        String url;
        Map<String, String> headers;
        RequestBody body;

        public Builder() {
            this.method = HttpMethod.GET; // 默认get请求
            this.headers = new ArrayMap<>();
            this.url = ApiManager.getBaseUri();
        }

        /**
         * 支持直接传url的形式,不推荐使用
         * @param url
         * @return
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置接口名,并拼接url
         * @param api
         * @return
         */
        public Builder api(String api) {
            if (!TextUtils.isEmpty(api) && api.startsWith("/")) {
                this.url = ApiManager.jointUrl(api);
            }
            return this;
        }

        /**
         * 设置域名,正式环境不建议使用(测试环境使用,在api方法前调用)
         * @param host
         * @return
         */
        public Builder host(String host) {
            if (!TextUtils.isEmpty(host)) {
                this.url = ApiManager.changeHost(host);
            }
            return this;
        }

        public Builder get() {
            method(HttpMethod.GET, null);
            return this;
        }

        public Builder post(RequestBody body) {
            method(HttpMethod.POST, body);
            return this;
        }

        public Builder put(RequestBody body) {
            method(HttpMethod.PUT, body);
            return this;
        }

        public Builder delete(RequestBody body) {
            method(HttpMethod.DELETE, body);
            return this;
        }

        public Builder header(String name, String value) {
            if (!TextUtils.isEmpty(name)) {
                headers.put(name, value);
            }
            return this;
        }

        private Builder method(HttpMethod method, RequestBody body) {
            Util.checkMethod(method, body); // 检测请求方式是否合法
            this.method = method;
            this.body = body;
            return this;
        }

        public Request build() {
            if (url == null) throw new IllegalStateException("url == null");

            // 初始化headers
            if (body != null && !TextUtils.isEmpty(body.contentType())) {
                headers.put("Content-Type", body.contentType());
            }
            headers.put("Connection", "Keep-Alive");
            headers.put("Charset", "UTF-8");
            return new Request(this);
        }

    }

    public enum HttpMethod {

        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        public String method;

        HttpMethod(String method) {
            this.method = method;
        }

        /**
         * 是否需要请求体
         * @param method
         * @return
         */
        public static boolean checkNeedBody(HttpMethod method) {
            return POST.equals(method) || PUT.equals(method);
        }

        /**
         * 是否不需要请求体
         * @param method
         * @return
         */
        public static boolean checkNoBody(HttpMethod method) {
            return GET.equals(method) || DELETE.equals(method);
        }
    }

}
