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
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.SaveForgotPassWordResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPassActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;
    @BindView(R.id.confirmPassET)
    AppCompatEditText confirmPassET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pass;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText("");
    }

    private Activity context = ResetPassActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, ResetPassActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);


    }


    @OnClick({R.id.backIV, R.id.submitBTN})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.submitBTN:
                if (validation()) {
                    String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

                    LoginRegisterRequest req = new LoginRegisterRequest();
                    req.password = passwordET.getText().toString().trim();
                    req.password_confirmation = confirmPassET.getText().toString().trim();
                    req.user_id = userId;

                    loginRegisterProfilePresenter.hitSaveForgotPassword(req);
                }
                break;
        }
    }

    private boolean validation() {
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
        if (test instanceof SaveForgotPassWordResponse) {
            SaveForgotPassWordResponse response = (SaveForgotPassWordResponse) test;
            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                appUtils.startActivity(context,
                        LoginActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
            } else {

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
