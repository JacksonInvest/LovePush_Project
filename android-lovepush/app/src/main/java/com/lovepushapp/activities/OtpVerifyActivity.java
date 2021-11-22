package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.SendVerificationCodeResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.utils.ErrorUtils;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.OnClick;

public class OtpVerifyActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.otpET)
    AppCompatEditText otpET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    private String FROM = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_otp_verify;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText("");
    }

    private Activity context = OtpVerifyActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, OtpVerifyActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

        try {
            FROM = getIntent().getStringExtra(GlobalsVariables.BUNDLE.FROM);
            switch (FROM) {
                case GlobalsVariables.BUNDLE.FROM_FORGOT:
                    String verificationCode = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.verification_code, GlobalsVariables.STORAGE.TYPE_STRING);
                    otpET.setText(verificationCode);
                    break;
                case GlobalsVariables.BUNDLE.FROM_LOGIN_REGISTER:

                    String verificationCode1 = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.verification_code, GlobalsVariables.STORAGE.TYPE_STRING);

                    Log.e("onCreate: ", verificationCode1);

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.backIV, R.id.resendTV,/* R.id.changeNumberTV,*/ R.id.verifyBTN})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verifyBTN:
                if (validation()) {
                    LoginRegisterRequest req = new LoginRegisterRequest();
                    req.otp = otpET.getText().toString().trim();
                    String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

                    req.user_id = userId;
                    req.otp = otpET.getText().toString().trim();

                    /*Verify user otp*/
                    loginRegisterProfilePresenter.hitVerifyCode(req);

                }
                break;
            case R.id.backIV:
                goback();
                break;
            case R.id.resendTV:
                hitResendNow();
                break;
           /* case R.id.changeNumberTV:
                goback();
                break;*/
        }
    }

    private void hitResendNow() {
        LoginRegisterRequest req1 = new LoginRegisterRequest();
        String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
        String email = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Email, GlobalsVariables.STORAGE.TYPE_STRING);
        /* String phone = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone, GlobalsVariables.STORAGE.TYPE_STRING);*/
        /*req1.user_id = userId;*/
        req1.email = email;
        loginRegisterProfilePresenter.hitSendVerificationCode(req1);
    }

    private boolean validation() {
        if (appUtils.isFieldEmpty(otpET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_otp));
            return false;
        }
        return true;
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof GetProfile) {
            GetProfile response11 = (GetProfile) test;
            appUtils.showToast(response11.getMessage());
            if (response11.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {


                switch (FROM) {
                    case GlobalsVariables.BUNDLE.FROM_FORGOT:
                        appUtils.startActivity(context,
                                ResetPassActivity.createIntent(context),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;
                    case GlobalsVariables.BUNDLE.FROM_LOGIN_REGISTER:
                        try {
                            GetProfile response = getProfileModel();
                            if (response.getData().getIsProfileComplete() == 0) {
                                gotoCreateProfile();
                            } else {
                                gotoDashBoard();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        break;
                }


            } else {

            }
        }
        if (test instanceof SendVerificationCodeResponse) {

            SendVerificationCodeResponse response = (SendVerificationCodeResponse) test;
            appUtils.showToast(response.getMessage());
            otpET.setText(response.getVerification_code() + "");
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.verification_code,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    response.getVerification_code() + ""
            );
        }

    }

    private void gotoDashBoard() {
        AlertDialogs progressDialog = new AlertDialogs(context);
        progressDialog.show();
        final QBUser user = new QBUser();

        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        //user.setCustomData(profile_url);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));


        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {


            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);

                SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                progressDialog.dismiss();

                appUtils.startActivity(context,
                        DashboardActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );


                // ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
                //ProgressDialogFragment.hide(getFragmentManager());
                ErrorUtils.showSnackbar(otpET, R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoDashBoard();
                            }
                        });
            }
        });
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    private void gotoCreateProfile() {

        appUtils.startActivity(context,
                CreateProfileActivity.createIntent(context),
                false,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
    }
}
