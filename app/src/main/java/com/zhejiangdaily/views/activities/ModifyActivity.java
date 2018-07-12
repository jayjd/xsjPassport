package com.zhejiangdaily.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhejiangdaily.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Function: ModifyActivity
 * <p>
 * Author: chen.h
 * Date: 2018/7/12
 */
public class ModifyActivity extends AppCompatActivity {
    private int requestCode = 0x21;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.ll_modify_phone, R.id.ll_modify_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_modify_phone:
                startActivityForResult(new Intent(this, ModifyPhoneActivity.class), requestCode);
                break;
            case R.id.ll_modify_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }

    }
}
