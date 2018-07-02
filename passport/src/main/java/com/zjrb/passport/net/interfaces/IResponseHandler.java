package com.zjrb.passport.net.interfaces;


import android.os.Handler;
import android.os.Looper;

import com.zjrb.passport.net.CallBack;
import com.zjrb.passport.net.Request;
import com.zjrb.passport.net.Response;

import java.io.IOException;

/**
 * Date: 2018/6/30 下午9:56
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 处理响应的接口
 */
public interface IResponseHandler {

    /**
     * 处理成功的请求
     * @param callback
     * @param response
     */
    void handleSuccess(CallBack callback, Response response);

    /**
     * 处理失败
     * @param callBack
     * @param request
     * @param e
     */
    void handleFail(CallBack callBack, Request request, IOException e);

    IResponseHandler RESPONSE_HANDLER = new IResponseHandler() {

        Handler handler = new Handler(Looper.getMainLooper()); // 用于切换到主线程执行

        /**
         * 切换线程
         * @param runnable
         */
        private void execute(Runnable runnable) {
            handler.post(runnable);
        }
        @Override
        public void handleSuccess(final CallBack callback, final Response response) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        callback.onSuccess(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            execute(runnable);
        }

        public void removeAllMessage() {
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public void handleFail(final CallBack callBack, final Request request, final IOException e) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    callBack.onFail(request, e);
                }
            };
            execute(runnable);
        }
    };

}