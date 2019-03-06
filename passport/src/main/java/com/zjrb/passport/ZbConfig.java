package com.zjrb.passport;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.zjrb.passport.constant.ZbConstants;
import com.zjrb.passport.util.InnerConstant;
import com.zjrb.passport.util.Logger;
import com.zjrb.passport.util.SharedPreferencesUtil;

/**
 * Function: ZbConfig
 * <p>
 * Author: chen.h
 * Date: 2018/6/28
 */
public final class ZbConfig {
    private int clientId;
    private String appSecret;
    private int envType;
    private String token;
    private boolean isDebug;
    private String appVersion;
    private String appUuid;
    private String ua;
    private boolean isUseHttps; // 是否强制使用https
    private String cookie; // 用于保存init接口下发的cookie

    public SharedPreferencesUtil getSpUtil() {
        return spUtil;
    }

    private SharedPreferencesUtil spUtil;
    private String signatureKey; // 会话初始化过程中下发的signature_key

    ZbConfig(Context context) {
        if (context == null) {
            return;
        }
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager()
                          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage());
        }
        spUtil = SharedPreferencesUtil.init(context.getApplicationContext());
        if (info != null) {
            clientId = info.metaData.getInt(InnerConstant.META_ID);
            appSecret = info.metaData.getString(InnerConstant.META_SECRET);
            String env = info.metaData.getString(InnerConstant.META_ENV, InnerConstant.DEV);
            resolveEnv(env);
        }
    }

    private void resolveEnv(String env) {
        switch (env) {
            case InnerConstant.TEST:
                envType = ZbConstants.Env.TEST;
                isDebug = false;
                break;
            case InnerConstant.PRE:
                envType = ZbConstants.Env.PRE;
                isDebug = false;
                break;
            case InnerConstant.OFFICIAL:
                envType = ZbConstants.Env.OFFICIAL;
                isDebug = false;
                break;
            default:
                envType = ZbConstants.Env.DEV;
                isDebug = true;
                break;
        }
    }

    public int getClientId() {
        return clientId;
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

    void setToken(String token) {
        this.token = token;
        spUtil.putString(ZbConstants.PASSPORT_TOKEN, token);
    }

    public String getToken() {
        return spUtil.getString(ZbConstants.PASSPORT_TOKEN);
    }

    public String getSignatureKey() {
        return spUtil.getString(ZbConstants.PASSPORT_SIGNATURE_KEY);
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        spUtil.putString(ZbConstants.PASSPORT_COOKIE, cookie);
    }

    public String getCookie() {
        return spUtil.getString(ZbConstants.PASSPORT_COOKIE);
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
        spUtil.putString(ZbConstants.PASSPORT_SIGNATURE_KEY, signatureKey);
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getAppUuid() {
        return appUuid;
    }

    public void initUA() {
        StringBuilder sb = new StringBuilder();
        sb.append("ANDROID")
          .append(";")
          .append(Build.VERSION.RELEASE)
          .append(";")
          .append(clientId)
          .append(";")
          .append(appVersion)
          .append(";")
          .append(BuildConfig.PASSPORT_VERSION)
          .append(";")
          .append(appUuid)
          .append(";")
          .append(Build.MODEL);
        ua = sb.toString();
    }

    public String getUA() {
        return ua;
    }

    void setClientId(int clientId) {
        this.clientId = clientId;
    }

    void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    void setEnvType(int envType) {
        this.envType = envType;
    }

    void setDebug(boolean debug) {
        isDebug = debug;
        if (envType == ZbConstants.Env.OFFICIAL) {
            isDebug = false;
        }
    }


    void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    void setAppUuid(String appUuid) {
        this.appUuid = appUuid;
    }

    public boolean isUseHttps() {
        return isUseHttps;
    }

    /**
     * 设置是否强制使用Https,只有debug条件下设置才有效
     *
     * @param useHttps
     */
    public void setUseHttps(boolean useHttps) {
        if (isDebug) {
            isUseHttps = useHttps;
        }
        if (envType == ZbConstants.Env.OFFICIAL) {
            isUseHttps = false;
        }
    }

    /**
     * Function: ZbConfigBuilder
     */
    public static class Builder {
        private int clientId = -1;
        private String appSecret;
        private String token;
        private int envType = -1;
        private boolean isDebug;
        private boolean isSetDebug;
        private String appVersion;
        private String appUuid;

        public Builder setClientId(int clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

        public Builder setEnvType(@ZbConstants.EnvType int envType) {
            this.envType = envType;
            return this;
        }

        public Builder setDebug(boolean isDebug) {
            this.isSetDebug = true;
            this.isDebug = isDebug;
            return this;
        }

        public Builder setAppVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }



        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setAppUuid(String appUuid) {
            this.appUuid = appUuid;
            return this;
        }

        public void build(ZbConfig zbConfig) {
            if (clientId != -1) {
                zbConfig.setClientId(clientId);
            }
            if (!TextUtils.isEmpty(appSecret)) {
                zbConfig.setAppSecret(appSecret);
            }
            if (envType != -1) {
                zbConfig.setEnvType(envType);
            }
            if (isSetDebug) {
                zbConfig.setDebug(isDebug);
            }
            zbConfig.setAppVersion(appVersion);
            zbConfig.setAppUuid(appUuid);
            zbConfig.initUA();
            zbConfig.setToken(token);
        }
    }
}
