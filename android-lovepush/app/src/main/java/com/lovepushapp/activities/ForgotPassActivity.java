package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.hbb20.CountryCodePicker;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.ForgotPassResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPassActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.phoneET)
    AppCompatEditText phoneET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_pass;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText("");
    }

    private Activity context = ForgotPassActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, ForgotPassActivity.class);
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
                    LoginRegisterRequest req = new LoginRegisterRequest();
                    req.phone_code = ccp.getSelectedCountryCodeWithPlus();
                    req.phone = phoneET.getText().toString().trim();
                   // loginRegisterProfilePresenter.hitForgotPassword(req);
                }
                break;
        }
    }

    private boolean validation() {
        if (appUtils.isValidPhone(phoneET)) {
            appUtils.validationToast(context, phoneET, getString(R.string.error_phone));
            return false;
        }
        return true;
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof ForgotPassResponse) {
            ForgotPassResponse response = (ForgotPassResponse) test;
            appUtils.showToast(response.getMessage());
            /*if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        response.getId() + ""
                );

                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.verification_code,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        response.getVerification_code() + ""
                );

                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone_code,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        response.getPhone_code()
                );

                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        response.getPhone()
                );


                appUtils.startActivity(context,
                        OtpVerifyActivity.createIntent(context).putExtra(GlobalsVariables.BUNDLE.FROM, GlobalsVariables.BUNDLE.FROM_FORGOT),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
            } else {

            }*/
        }

    }

    @Override
    public <T> void onApiError(T test) {


    }

    @Override
    public void onException(Exception e) {

    }
}
