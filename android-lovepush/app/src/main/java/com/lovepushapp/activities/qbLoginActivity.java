package com.lovepushapp.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;

import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class qbLoginActivity extends BaseActivity {
    private String TAG = RegisterActivity.class.getSimpleName();

    private AlertDialogs progressDialog;
    private QBUser userForSave;
    int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;
    int EXTRA_LOGIN_RESULT_CODE = 1002;
    protected QBResRequestExecutor requestExecutor;
    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    @Override
    public int getLayoutId() {
        return R.layout.activity_qb_login;
    }

    @Override
    public void manageToolBar() {

    }


    /*Quickblox login activity*/

    private Activity context = qbLoginActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, qbLoginActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new AlertDialogs(context);


        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();
        gotoDashBoard();
    }



    private void gotoDashBoard() {
        userForSave = createUserWithEnteredData();
        startSignUpNewUser(userForSave);


    }





    private QBUser createUserWithEnteredData() {

        return createQBUserWithCurrentData();
    }

    private QBUser createQBUserWithCurrentData() {
        final QBUser user = new QBUser();

        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
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
                progressDialog.dismiss();

                SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                progressDialog.dismiss();

                appUtils.startActivity(context,
                        DashboardActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );

                finish();
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

}
