package com.zhejiangdaily;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhejiangdaily.utils.ToastUtil;
import com.zhejiangdaily.utils.ZbUtil;
import com.zhejiangdaily.views.activities.LoginActivity;
import com.zhejiangdaily.views.activities.RegisterActvity;
import com.zjrb.passport.Entity.AuthInfo;
import com.zjrb.passport.ZbPassport;
import com.zjrb.passport.listener.ZbAuthListener;
import com.zjrb.passport.listener.ZbResultListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_captcha)
    EditText mEtCaptcha;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_password_login)
    TextView mTvPasswordLogin;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_send)
    TextView mTvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        String a = "get%%/api/some_resource?client_id=1111&passport_id=2000000&phone_number=13333333333&unbind=true%%64be2c39-ba09-443f-9aae-1a1f99004cbb%%bbbbbdddd8Tviqqqqeee";
//        String s = "928aihvQETH08TviNX0C";
//        String ss = EncryptUtil.sha256_HMAC(a, s);
//        System.out.println("ssssss: " + ss);
//        for (int i = 0; i < 10; i++) {
//            System.out.println("llllll: " + EncryptUtil.encryptPassWord("mac"));
//        }
    }

    @OnClick({R.id.tv_login, R.id.tv_password_login, R.id.tv_register, R.id.tv_send})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_login: // 手机号验证码登录
                if (!TextUtils.isEmpty(mEtCaptcha.getText().toString())) {
                    if (ZbUtil.isMobileNum(mEtPhone.getText().toString())) { // 手机号验证码登录不需要先进行绑定的校验,因为该接口未注册的手机号会自动注册通行证
                        doLogin(mEtPhone.getText().toString(), mEtCaptcha.getText().toString());
                    } else {
                        if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                            ToastUtil.show("请输入手机号");
                        } else {
                            ToastUtil.show("非手机号格式");
                        }
                    }
                } else {
                    ToastUtil.show("请输入验证码");
                }
                break;
            case R.id.tv_password_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(this, RegisterActvity.class);
                startActivity(registerIntent);
                break;
            case R.id.tv_send:
                if (ZbUtil.isMobileNum(mEtPhone.getText().toString())) {
                    ZbPassport.sendCaptcha(ZbPassport.getZbConfig().getAppId() + "", mEtPhone.getText().toString(), "", new ZbResultListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.show("下发登录短信验证码接口 success");
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMessage) {
                            // TODO: 2019/3/1 判断图形验证码code 封装
                            ToastUtil.show(errorMessage);

                        }
                    });
//                    ZbPassport.sendCaptcha(ZbConstants.Sms.LOGIN, mEtPhone.getText().toString(), new ZbCaptchaSendListener() {
//
//                        @Override
//                        public void onSuccess() {
//                            ToastUtil.show("下发登录短信验证码接口 success");
//                        }
//
//                        @Override
//                        public void onFailure(int errorCode, String errorMessage) {
//                            ToastUtil.show(errorMessage);
//                        }
//                    });
                } else {
                    if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                        ToastUtil.show("请输入手机号");
                    } else {
                        ToastUtil.show("非手机号格式");
                    }
                }
                break;
        }
    }

  /*  private void checkBind(final String phone, final String password) {
        ZbPassport.checkBindState(phone, new ZbCheckPhoneListener() {
            @Override
            public void onSuccess(boolean isBind) {
                if (isBind) {
                    doLogin(phone, password);
                } else {
                    TipDialog tipDialog = new TipDialog(MainActivity.this);
                    tipDialog.setTitle("提示")
                            .setMessage("此手机号尚未注册，请先注册")
                            .setLeft("取消")
                            .setRight("注册")
                            .setListener(new TipDialog.Listener() {
                                @Override
                                public void onLeft() {

                                }

                                @Override
                                public void onRight() {
                                    startActivity(new Intent(MainActivity.this, RegisterActvity.class));
                                }
                            });
                    tipDialog.show();
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                doLogin(phone, password); // 绑定态查询失败,尝试登录
            }
        });
    }*/

    /**
     * 手机号验证码登录
     * @param phone
     * @param captcha
     */
    private void doLogin(String phone, String captcha) {
        ZbPassport.loginCaptcha(ZbPassport.getZbConfig().getAppId() + "", phone, captcha, new ZbAuthListener() {
            @Override
            public void onSuccess(AuthInfo info) {
                
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {

            }
        });
        // TODO: 2019/3/1  
//        ZbPassport.loginCaptcha(phone, password, new ZbLoginListener() {
//
//            @Override
//            public void onSuccess(LoginInfo loginInfo) {
//                ToastUtil.show("登录成功");
//                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
//            }
//
//            @Override
//            public void onFailure(int errorCode, String errorMessage) {
//                ToastUtil.show(errorMessage);
//            }
//        });
    }

}
