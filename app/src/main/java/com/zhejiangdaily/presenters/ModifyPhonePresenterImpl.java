package com.zhejiangdaily.presenters;

import com.zhejiangdaily.contracts.ModifyPhoneContract;
import com.zjrb.passport.ZbPassport;
import com.zjrb.passport.listener.ZbResultListener;

/**
 * Function: ModifyPhonePresenterImpl
 * <p>
 * Author: chen.h
 * Date: 2018/7/12
 */
public class ModifyPhonePresenterImpl implements ModifyPhoneContract.Presenter {

    private final ModifyPhoneContract.View view;

    public ModifyPhonePresenterImpl(ModifyPhoneContract.View view) {this.view = view;}


    @Override
    public void checkPhone(String phone) {
        // TODO: 2019/3/5
//        ZbPassport.checkBindState(phone, new ZbCheckPhoneListener() {
//            @Override
//            public void onSuccess(boolean isBind) {
//                view.checkPhone(true, isBind, "该手机已经注册浙报通行证");
//            }
//
//            @Override
//            public void onFailure(int errorCode, String errorMessage) {
//                view.checkPhone(false, false, errorMessage);
//            }
//        });
    }

    @Override
    public void sendCaptcha(String phone) {
        ZbPassport.sendCaptcha( phone, "", new ZbResultListener() {
            @Override
            public void onSuccess() {
                view.sendCaptcha(true, null);
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                // TODO: 2019/3/1 判断图形验证码code 封装
                view.sendCaptcha(false, errorMessage);
            }
        });
//        ZbPassport.sendCaptcha(ZbConstants.Sms.BIND, phone, new ZbCaptchaSendListener() {
//            @Override
//            public void onSuccess() {
//                view.sendCaptcha(true, null);
//            }
//
//            @Override
//            public void onFailure(int errorCode, String errorMessage) {
//                view.sendCaptcha(false, errorMessage);
//            }
//        });
    }

    @Override
    public void modifyPhone(final String phone, String captcha) {
        // TODO: 2019/3/5
//        ZbPassport.bindPhone(phone, captcha, new ZbBindPhoneListener() {
//            @Override
//            public void onSuccess() {
//                view.modifyPhone(true, phone, null);
//            }
//
//            @Override
//            public void onFailure(int errorCode, String errorMessage) {
//                view.modifyPhone(false, phone, errorMessage);
//            }
//        });
    }
}
