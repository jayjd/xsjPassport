package com.zhejiangdaily.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangdaily.R;
import com.zhejiangdaily.utils.ToastUtil;
import com.zhejiangdaily.utils.ZbUtil;
import com.zhejiangdaily.views.dialogs.ZBDialog;
import com.zjrb.passport.StatusCode;
import com.zjrb.passport.ZbPassport;
import com.zjrb.passport.constant.ZbConstants;
import com.zjrb.passport.domain.ZbInfoEntity;
import com.zjrb.passport.listener.ZbListener;
import com.zjrb.passport.listener.ZbRegisterListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Date: 2018/7/9 上午11:39
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 注册页面
 */
public class RegisterActvity extends AppCompatActivity {

    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_captcha)
    EditText mEtCaptcha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_register, R.id.tv_login, R.id.tv_send})
    public void onClick(View v) {
        String phoneNum = mEtPhone.getText().toString();
        String passWord = mEtPassword.getText().toString();
        String captcha = mEtCaptcha.getText().toString();
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_register:
                if (TextUtils.isEmpty(captcha)) {
                    showToast("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(phoneNum)) {
                    showToast("请输入手机号");
                    return;
                } else if (TextUtils.isEmpty(passWord)) {
                    showToast("请输入密码");
                    return;
                }
                ZbPassport.register(phoneNum, passWord, captcha, new ZbRegisterListener() {
                    @Override
                    public void onSuccess(ZbInfoEntity info) {
                        // todo屏幕中间的Toast
                        ToastUtil.showTextWithImage(R.id.iv_qq, "注册成功");
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        if (errorCode == StatusCode.ERROR_SMS_INVALID) { // 短信验证码无效
                            ToastUtil.showTextWithImage(R.mipmap.ic_qq, "验证码错误");
                        } else if (errorCode == StatusCode.ERROR_PHONE_REGISTERED) {
                            final ZBDialog dialog = new ZBDialog(RegisterActvity.this);
                            dialog.setBuilder(new ZBDialog.Builder().setTitle("提示")
                                                                    .setMessage("此手机号已经存在,可直接登录")
                                                                    .setLeftText("取消")
                                                                    .setRightText("登录")
                                                                    .setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (v.getId() == R.id.btn_right) {
                                                                                Intent intent = new Intent(
                                                                                        RegisterActvity.this,
                                                                                        LoginActivity.class);
                                                                                startActivity(intent);
                                                                            }
                                                                            if (v.getId() == R.id.btn_left) {
                                                                                if (dialog.isShowing()) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            }
                                                                        }
                                                                    }));
                            dialog.show();
                        }
                    }
                });
                break;
            case R.id.tv_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_send:
                // 校验手机号
                if (ZbUtil.isMobileNum(phoneNum)) {
                    ZbPassport.sendCaptcha(ZbConstants.SMS_REGISTER, phoneNum, new ZbListener() {
                        @Override
                        public void onSuccess() {
                            showToast("下发注册短信验证码接口 success");
                        }

                        @Override
                        public void onFailure(int errorCode, String errorMessage) {
                            showToast(errorMessage);
                        }
                    });
                } else {
                    if (TextUtils.isEmpty(phoneNum)) {
                        showToast("请输入手机号");
                    } else {
                        showToast("非手机号格式");
                    }
                }

                break;
        }
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}