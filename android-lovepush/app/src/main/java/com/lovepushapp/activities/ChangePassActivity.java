package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.ChangePassResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity implements LoginMvp {


    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.oldPasswordET)
    AppCompatEditText oldPasswordET;
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;
    @BindView(R.id.confirmPassET)
    AppCompatEditText confirmPassET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pass;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.change_pass));
    }

    private Activity context = ChangePassActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, ChangePassActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

    }

    @OnClick({R.id.backIV, R.id.changeTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.changeTV:
                if (validation()) {
                    LoginRegisterRequest request = new LoginRegisterRequest();
                    request.new_password = appUtils.getFieldValue(passwordET);
                    request.old_password = appUtils.getFieldValue(oldPasswordET);
                    request.new_password_confirmation = appUtils.getFieldValue(confirmPassET);
                    /*Hit change password api*/
                    loginRegisterProfilePresenter.hitChangePass(request);
                }
                break;
        }
    }

    private boolean validation() {
        appUtils.hideKeyBoard(context);
        if (appUtils.isNotValidPassword(oldPasswordET)) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_old_password));
            return false;
        }
        if (appUtils.isNotValidPassword(passwordET)) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_password));
            return false;
        }
        if (!appUtils.getFieldValue(passwordET).equalsIgnoreCase(appUtils.getFieldValue(confirmPassET))) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_pass_not_matched));
            return false;
        }
        return true;
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof ChangePassResponse) {
            ChangePassResponse response = (ChangePassResponse) test;
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                appUtils.showToast(response.getMessage());
                goback();
            } else {
                appUtils.showToast(response.getMessage());
            }
        }

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
