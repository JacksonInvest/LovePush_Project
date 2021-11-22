package com.lovepushapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivitySocial;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.InstagramLogin_Dialog;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Social_AcceptDetail_Dialog;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.local.SocialLocalModel;
import com.lovepushapp.model.response.ForgotPassResponse;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivitySocial implements LoginMvp {
    private String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.usernameET)
    AppCompatEditText usernameET;
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    String logintype;
    private AlertDialogs progressDialog;
    private QBUser userForSave;
    int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;
    int EXTRA_LOGIN_RESULT_CODE = 1002;
    protected QBResRequestExecutor requestExecutor;
    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void manageToolBar() {

    }

    private Activity context = LoginActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new AlertDialogs(context);

        sharedStorage.clearLocalStorage();
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();

    }

    @Override
    public void onSocialSuccess(SocialLocalModel socialLocalModel) {

        switch (socialLocalModel.socialType)
        {
            case SocialLocalModel.TYPE.FACEBOOK:
                logintype ="2";
                loginRegisterProfilePresenter.hit_social_login(socialLocalModel.email,"2",socialLocalModel.id,socialLocalModel.fname);

                break;

            case SocialLocalModel.TYPE.GMAIL:
                logintype ="3";
                loginRegisterProfilePresenter.hit_social_login(socialLocalModel.email,"3",socialLocalModel.id,socialLocalModel.fname);
                break;
        }

    }

    @Override
    public void onSocialError(String error, String type) {
        appUtils.validationToast(context, passwordET, error);
    }


    @OnClick({R.id.forgotTV, R.id.loginTV, R.id.signUpTV, R.id.facebookIV, R.id.googleTV, R.id.instaIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgotTV:
                /*appUtils.startActivity(context,
                        ForgotPassActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );*/

                appUtils.hideKeyBoard(context);
                if (!appUtils.isValidEmail(usernameET)) {
                  //  appUtils.validationToast(context, usernameET, getString(R.string.error_email));
                    alertPopup(getString(R.string.error_email));
                }else{
                    /*hit forgot password api*/
                    loginRegisterProfilePresenter.hit_forgot_password(usernameET.getText().toString().trim());

                }
                break;
            case R.id.loginTV:
                if (validation()) {
                    //gotoDashBoard();
                    LoginRegisterRequest req = new LoginRegisterRequest();
                    req.username = usernameET.getText().toString().trim();
                    req.password = passwordET.getText().toString().trim();
                    /*hit login api*/
                    loginRegisterProfilePresenter.hitLogin(req);
                }
                break;
            case R.id.signUpTV:
                appUtils.startActivity(context,
                        RegisterActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
            case R.id.facebookIV:
                facebookLoginStart();
                break;
            case R.id.instaIV:

                /*Init instagram login */
                new InstagramLogin_Dialog(context);
                break;
            case R.id.googleTV:
                gmailLoginStart();
                break;
        }
    }

    private void gotoDashBoard(String name) {
        userForSave = createUserWithEnteredData(name);
        startSignUpNewUser(userForSave);

        //Quickblox

    }
    private QBUser createUserWithEnteredData(String name) {
        return createQBUserWithCurrentData(name);
    }

    private QBUser createQBUserWithCurrentData(String name) {
        final QBUser user = new QBUser();

        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setEmail(usernameET.getText().toString().trim());
        user.setFullName(name);
        user.setPassword(GlobalsVariables.quickblox_password);
        return user;
    }

    private void startSignUpNewUser(final QBUser newUser) {
        Log.d(TAG, "SignUp New User");
        progressDialog.show();
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "SignUp Successful");
                        saveUserData(newUser);
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                         //   saveUserData(newUser);
                          //  loginToChat(newUser);
                            signInCreatedUser(newUser);
                        } else {
                            progressDialog.dismiss();
                           // hideProgressDialog();
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(GlobalsVariables.quickblox_password);
        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EXTRA_LOGIN_RESULT_CODE) {
            progressDialog.dismiss();

            boolean isLoginSuccess = data.getBooleanExtra(EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);
                signInCreatedUser(userForSave);
            } else {
                ToastUtils.longToast(getString(R.string.login_chat_login_error) + errorMessage);

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void signInCreatedUser(final QBUser qbUser) {
        Log.d(TAG, "SignIn Started");
        requestExecutor.signInUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.d(TAG, "SignIn Successful");
               // sharedPrefsHelper.saveQbUser(userForSave);
                SharedPrefsHelper.getInstance().saveQbUser(user);
                updateUserOnServer(qbUser);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d(TAG, "Error SignIn" + responseException.getMessage());
                progressDialog.dismiss();
                //hideProgressDialog();
                ToastUtils.longToast(R.string.sign_in_error_with_error);
            }
        });
    }

    private void updateUserOnServer(QBUser user) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {


                Log.e("SUBSCRIBE_NOTIFICATION","TRUE");



                progressDialog.dismiss();

                SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                progressDialog.dismiss();

                appUtils.startActivity(context,
                        DashboardActivity.createIntent(context),
                        true,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();

                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }


    private boolean validation() {
        appUtils.hideKeyBoard(context);
        if (!appUtils.isValidEmail(usernameET)) {
            appUtils.validationToast(context, usernameET, getString(R.string.error_email));
            return false;
        }
        if (appUtils.isNotValidPassword(passwordET)) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_password));
            return false;
        }

        return true;
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


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetProfile) {
            GetProfile res = (GetProfile) test;

            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                if (TextUtils.isEmpty(res.getData().getEmailVerifiedAt())) {
                    gotoPhoneVerify();
                } else if (res.getData().getIsProfileComplete() == 0) {
                    gotoCreateProfile();
                } else {

                    gotoDashBoard(res.getData().getName());
                }

            } else {
                appUtils.setSnackbar(passwordET, res.getMessage());
            }

        }

        if (test instanceof SocialLoginModel)
        {

            SocialLoginModel res = (SocialLoginModel) test;
            //appUtils.showToast(res.getMessage());

            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS){
                if (res.getData().getIsProfileComplete() == 0) {
                    new Social_AcceptDetail_Dialog(context,logintype,res.getData().getEmail());
                } else {
                    gotoDashBoard(res.getData().getName());
                }

            } else {
                appUtils.setSnackbar(passwordET, res.getMessage());
            }


            Log.e( "onApiResponse: ", res.getMessage());

        }

        if (test instanceof ForgotPassResponse) {
            ForgotPassResponse response = (ForgotPassResponse) test;
            alertPopup(response.getMessage());

        }
    }


    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void gotoPhoneVerify() {
        appUtils.startActivity(context,
                OtpVerifyActivity.createIntent(context).putExtra(GlobalsVariables.BUNDLE.FROM, GlobalsVariables.BUNDLE.FROM_LOGIN_REGISTER),
                false,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
    }

    @Override
    public <T> void onApiError(T test) {
        appUtils.validationToast(context, passwordET, (String) test);

    }

    @Override
    public void onException(Exception e) {

    }

}
