package com.zjrb.passport;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.zjrb.passport.constant.ZbConstants;

/**
 * Function: ZbConfig
 * <p>
 * Author: chen.h
 * Date: 2018/6/28
 */
public final class ZbConfig {

    private int appId;
    private String appKey;
    private String appSecret;
    private int envType;
    private String token;
    private boolean isDebug;

    ZbConfig(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager()
                          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            appId = info.metaData.getInt(ZbConstants.META_ID);
            appKey = info.metaData.getString(ZbConstants.META_KEY);
            appSecret = info.metaData.getString(ZbConstants.META_SECRET);
            String env = info.metaData.getString(ZbConstants.META_ENV, ZbConstants.DEV);
            resolveEnv(env);
        }
    }

    private void resolveEnv(String env) {
        switch (env) {
            case ZbConstants.TEST:
                envType = ZbConstants.ENV_TEST;
                isDebug = false;
                break;
            case ZbConstants.PRE:
                envType = ZbConstants.ENV_PRE;
                isDebug = false;
                break;
            case ZbConstants.OFFICIAL:
                envType = ZbConstants.ENV_OFFICIAL;
                isDebug = false;
                break;
            default:
                envType = ZbConstants.ENV_DEV;
                isDebug = true;
                break;
        }
    }

    public int getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public int getEnvType() {
        return envType;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getToken() {
        return token;
    }

    void setAppId(int appId) {
        this.appId = appId;
    }

    void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    void setEnvType(int envType) {
        this.envType = envType;
    }

    void setDebug(boolean debug) {
        isDebug = debug;
        if (envType == ZbConstants.ENV_OFFICIAL) {
            isDebug = false;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

}
