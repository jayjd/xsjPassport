package com.zjrb.passport.net.request;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.zjrb.passport.ZbPassport;
import com.zjrb.passport.net.ApiManager;
import com.zjrb.passport.util.Util;

import java.util.Map;

/**
 * Date: 2018/6/28 下午3:34
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 建造者模式,封装请求,参考okhttp  说明:浙报通行证网络封装的每个请求要求根据get/post类型的不同,
 * 将querystring加到url或者requestbody中,所以这里每个请求都有对应的queryString
 */
public class Request {

    final HttpMethod method;
    final String url;
    final Map<String, String> headers;
    final @Nullable RequestBody requestBody;
    final Object tag;

    public Request(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers;
        this.requestBody = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
    }

    public Object tag() {
        return tag;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Nullable
    public RequestBody getRequestBody() {
        return requestBody;
    }

    public static class Builder {

        HttpMethod method;
        String url;
        Map<String, String> headers;
        Map<String, String> paramsMap; // 用于记录get请求的参数集合
        RequestBody body;
        String api; // 记录接口名
        Object tag;

        public Builder() {
            this.method = HttpMethod.GET; // 默认get请求
            this.headers = new ArrayMap<>();
            this.url = ApiManager.getBaseUri();
            this.paramsMap = new ArrayMap<>();
        }

        /**
         * 支持直接传url的形式,不推荐使用
         *
         * @param url
         * @return
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置接口名,并拼接url(如果需要设置host,在host方法后调用)
         *
         * @param api
         * @return
         */
        public Builder api(String api) {
            this.api = api;
            if (!TextUtils.isEmpty(api) && api.startsWith("/")) {
                this.url = ApiManager.joinUrl(api);
            }
            return this;
        }

        /**
         * 设置域名,正式环境不建议使用(测试环境使用,在api方法前调用)
         *
         * @param host
         * @return
         */
        public Builder host(String host) {
            if (!TextUtils.isEmpty(host)) {
                this.url = ApiManager.changeHost(host);
            }
            return this;
        }

        public Builder get(FormBody body) {
            if (body != null) {
                this.paramsMap = body.map;
            }
            method(HttpMethod.GET, null);
            return this;
        }

        public Builder post(RequestBody body) {
            if (body == null) { // post无参数的情况
                body = new FormBody.Builder().build();
            }
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

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Request build() {
            if (url == null) throw new NullPointerException("url == null");
            // 这里每个请求要根据get/post类型的不同,将querystring加到url或者requestbody中
            if (this.method.equals(HttpMethod.GET) && this.paramsMap != null && this.paramsMap.size() != 0) { // get请求url拼装
                this.url = Util.buildGetUrl(url, this.paramsMap);
            }
            // 初始化headers
            if (body != null && !TextUtils.isEmpty(body.contentType())) {
                headers.put("Content-Type", body.contentType());
            }
            headers.put("UserAgent", Util.getValueEncoded(ZbPassport.getZbConfig().getUA()));
            headers.put("Cache-Control", "no-cache");
            return new Request(this);
        }

    }

    /**
     * 这里暂时不支持put和delete方式
     */
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
         *
         * @param method
         * @return
         */
        public static boolean checkNeedBody(HttpMethod method) {
            return POST.equals(method) || PUT.equals(method);
        }

        /**
         * 是否不需要请求体
         *
         * @param method
         * @return
         */
        public static boolean checkNoBody(HttpMethod method) {
            return GET.equals(method) || DELETE.equals(method);
        }
    }

}