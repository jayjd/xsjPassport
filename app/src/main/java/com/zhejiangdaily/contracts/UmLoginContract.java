package com.zhejiangdaily.contracts;

import android.content.Intent;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhejiangdaily.presenters.BasePresenter;

/**
 * Function: UmLoginContract
 * <p>
 * Author: chen.h
 * Date: 2018/7/9
 */
public interface UmLoginContract {

    interface Presenter extends BasePresenter {
        void umLogin(SHARE_MEDIA platform);

        void onUmCallback(int requestCode, int resultCode, Intent data);
    }

    interface View extends BaseView {

        void umLogin(boolean isSuccess, SHARE_MEDIA platform, String uid);
    }
}
