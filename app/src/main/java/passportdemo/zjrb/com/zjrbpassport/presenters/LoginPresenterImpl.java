package passportdemo.zjrb.com.zjrbpassport.presenters;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zjrb.passport.ZbPassport;
import com.zjrb.passport.constant.ZbConstants;
import com.zjrb.passport.domain.PhoneNumEntity;
import com.zjrb.passport.domain.ZbInfoEntity;
import com.zjrb.passport.listener.ZbCheckListener;
import com.zjrb.passport.listener.ZbLoginListener;

import passportdemo.zjrb.com.zjrbpassport.contracts.LoginContract;

/**
 * Function: LoginPresenterImpl
 * <p>
 * Author: chen.h
 * Date: 2018/7/9
 */
public class LoginPresenterImpl implements LoginContract.Presenter {

    private final LoginContract.View view;


    public LoginPresenterImpl(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String phone, String password) {
        checkBind(phone, password);
//        doLogin(phone, password);
    }

    @Override
    public void loginThird(SHARE_MEDIA platform, String uid) {
        switch (platform) {
            case WEIXIN:
                ZbPassport.loginThird(ZbConstants.WECHAT, uid, zbLoginListener);
                break;
            case QQ:
                ZbPassport.loginThird(ZbConstants.QQ, uid, zbLoginListener);
                break;
            case SINA:
                ZbPassport.loginThird(ZbConstants.SINA, uid, zbLoginListener);
                break;
            default:
                break;
        }

    }

    private void checkBind(final String phone, final String password) {
        ZbPassport.checkBindState(phone, new ZbCheckListener() {
            @Override
            public void onSuccess(PhoneNumEntity entity) {
                //TODO 太复杂，字面意思不好理解
                if (entity.isPhone_number_taken()) {
                    doLogin(phone, password);
                } else {
                    view.onPhoneNotExist();
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {

            }
        });
    }

    private void doLogin(String phone, String password) {
        ZbPassport.login(phone, password, zbLoginListener);
    }

    private ZbLoginListener zbLoginListener = new ZbLoginListener() {
        @Override
        public void onSuccess(ZbInfoEntity info) {
            view.onLoginSuccess();
        }

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            view.onLoginFail();
        }
    };
}