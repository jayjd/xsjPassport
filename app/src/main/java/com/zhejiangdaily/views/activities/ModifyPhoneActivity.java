package com.zhejiangdaily.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.zhejiangdaily.R;
import com.zhejiangdaily.contracts.ModifyPhoneContract;
import com.zhejiangdaily.presenters.ModifyPhonePresenterImpl;
import com.zhejiangdaily.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Function: ModifyPhoneActivity
 * <p>
 * Author: chen.h
 * Date: 2018/7/5
 */
public class ModifyPhoneActivity extends AppCompatActivity implements ModifyPhoneContract.View {


    ModifyPhoneContract.Presenter modifyPresenter;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        modifyPresenter = new ModifyPhonePresenterImpl(this);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_send, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_send:
                modifyPresenter.checkPhone(etPhone.getText().toString());
                break;
            case R.id.tv_complete:
                modifyPresenter.modifyPhone(etPhone.getText().toString(), etCaptcha.getText().toString());
                break;
        }
    }

    @Override
    public void checkPhone(boolean isSuccess, boolean isBind, String errorMsg) {
        if (isSuccess && !isBind) {
            modifyPresenter.sendCaptcha(etPhone.getText().toString());
        } else {
            ToastUtil.show(errorMsg);
        }
    }

    @Override
    public void sendCaptcha(boolean isSuccess, String errorMsg) {
        if (isSuccess) {
            ToastUtil.show("验证码发送成功");
        } else {
            ToastUtil.show(errorMsg);
        }
    }

    @Override
    public void modifyPhone(boolean isSuccess, String phone, String errorMsg) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("phone", phone);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            ToastUtil.show(errorMsg);
        }
    }

    @Override
    public Activity getIActivity() {
        return this;
    }
}
