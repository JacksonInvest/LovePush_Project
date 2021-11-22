package com.lovepushapp.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
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
import com.lovepushapp.model.local.RegisterData;
import com.lovepushapp.model.local.SocialLocalModel;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.NetworkRequests;
import com.lovepushapp.network.intracter.ResponseListner;
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

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class RegisterActivity extends BaseActivitySocial implements LoginMvp {
    private String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.usernameET)
    AppCompatEditText usernameET;
    @BindView(R.id.nameET)
    AppCompatEditText nameET;
    @BindView(R.id.emailET)
    AppCompatEditText emailET;
    @BindView(R.id.phoneET)
    AppCompatEditText phoneET;
    /* @BindView(R.id.addressET)
     AppCompatTextView addressET;*/
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;
    @BindView(R.id.confirmPassET)
    AppCompatEditText confirmPassET;
    @BindView(R.id.maleIV)
    ImageView maleIV;
    @BindView(R.id.femaleIV)
    ImageView femaleIV;

    @BindView(R.id.tickPrivacyIV)
    ImageView tickPrivacyIV;
    @BindView(R.id.transIV)
    ImageView transIV;
    @BindView(R.id.refralEt)
    AppCompatEditText refralEt;
    String logintype;


    private boolean isSelected = false, isTickPrivacySelected = false, isMale = false, isTrans = false;
    /* private String latitude = "", longitude = ""*/;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    // private ArrayList<String> locationList = new ArrayList<>();

    private AlertDialogs progressDialog;
    private QBUser userForSave;
    int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;
    int EXTRA_LOGIN_RESULT_CODE = 1002;
    protected QBResRequestExecutor requestExecutor;
    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";



    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.register));
    }

    private Activity context = RegisterActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedStorage.clearLocalStorage();
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);
        refralEt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        progressDialog = new AlertDialogs(context);
        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();

        findViewById(R.id.termsCondition).setOnClickListener(view->{
            Intent intent=new Intent(this,TermsCondition.class);
            //intent.putExtra("from","privacy_policy").putExtra("url","http://lovepush.alcax.com/legacy");
            intent.putExtra("from","privacy_policy").putExtra("url",TermsCondition.LEGACY);

            startActivityForResult(intent, 2);// Activity is started with requestCode 2

        });
    }

    @Override
    public void onSocialSuccess(SocialLocalModel socialLocalModel) {
        switch (socialLocalModel.socialType)
        {
            case SocialLocalModel.TYPE.FACEBOOK:
                logintype ="2";
                loginRegisterProfilePresenter.hit_social_login(socialLocalModel.email,logintype,socialLocalModel.id,socialLocalModel.fname);
                break;

            case SocialLocalModel.TYPE.GMAIL:
                logintype ="3";
                loginRegisterProfilePresenter.hit_social_login(socialLocalModel.email,logintype,socialLocalModel.id,socialLocalModel.fname);
                break;
        }
    }



    @Override
    public void onSocialError(String error, String type) {
        appUtils.validationToast(context, passwordET, error);

    }

    @OnClick({R.id.backIV, R.id.maleLL, R.id.femaleLL, R.id.transLL, /*R.id.addressET, */R.id.nextTV, R.id.facebookIV, R.id.tickPrivacyIV, R.id.instaIV,R.id.googleIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tickPrivacyIV:
                Intent intent=new Intent(this,TermsCondition.class);
                intent.putExtra("from","privacy_policy").putExtra("url",TermsCondition.LEGACY);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2
                break;
            case R.id.backIV:
                goback();
                break;
            case R.id.maleLL:
                isTrans = false;
                isMale = true;
                isSelected = true;
                manageGender();
                break;
            case R.id.femaleLL:
                isMale = false;
                isTrans = false;
                isSelected = true;
                manageGender();
                break;
            case R.id.transLL:
                isMale = false;
                isTrans = true;
                isSelected = true;
                manageGender();
                break;
           /* case R.id.addressET:

                getManualLocation();

                break;*/
            case R.id.nextTV:

                if (validation()) {
                    String gender = "";
                    if (isTrans)
                    {
                        gender = GlobalsVariables.GENDER.homo;

                    }
                   else if (isMale) {
                        gender = GlobalsVariables.GENDER.Male;
                    } else {
                        gender = GlobalsVariables.GENDER.female;
                    }

                    LoginRegisterRequest req = new LoginRegisterRequest();


                    req.username = appUtils.getFieldValue(usernameET);
                    req.name = appUtils.getFieldValue(nameET);
                    req.email = appUtils.getFieldValue(emailET);
                    req.phone_code = ccp.getSelectedCountryCodeWithPlus();
                    req.phone = appUtils.getFieldValue(phoneET);
                    req.gender = gender;
                    req.password = appUtils.getFieldValue(passwordET);
                    req.password_confirmation = appUtils.getFieldValue(confirmPassET);
                    req.device_type = GlobalsVariables.DEVICE_TYPE.android;
                    req.login_type = GlobalsVariables.LOGIN_TYPE.MANNUAL;

                    RegisterData RegisterData = new RegisterData("",appUtils.getFieldValue(usernameET),appUtils.getFieldValue(emailET),
                            appUtils.getFieldValue(phoneET),gender,appUtils.getFieldValue(passwordET),appUtils.getFieldValue(confirmPassET),ccp.getSelectedCountryCodeWithPlus(),
                            GlobalsVariables.LOGIN_TYPE.MANNUAL,GlobalsVariables.DEVICE_TYPE.android,appUtils.getFieldValue(refralEt));

                 /*  req.address = appUtils.getFieldValue(addressET);
                    req.latitude = latitude;
                    req.longitude = longitude;
                    req.country = locationList.get(0);
                    req.city = locationList.get(1);*/


                    loginRegisterProfilePresenter.hitRegister(RegisterData);

                }
                break;
            case R.id.facebookIV:
                facebookLoginStart();
                break;
            case R.id.instaIV:

                new InstagramLogin_Dialog(context);
                break;
            case R.id.googleIV:
                gmailLoginStart();
                break;

        }
    }


 /*   private void getManualLocation() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }*/
/*
 */


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
        else if(requestCode==2 && data!=null)
        {
            boolean term=data.getBooleanExtra("term",false);
            if (!term) {
                isTickPrivacySelected = false;
                tickPrivacyIV.setImageDrawable(getResources().getDrawable(R.drawable.uncheck));
            } else {
                isTickPrivacySelected = true;
                tickPrivacyIV.setImageDrawable(getResources().getDrawable(R.drawable.checked));
            }
        }
    }




    private boolean validation() {
        appUtils.hideKeyBoard(context);

        if (appUtils.isFieldEmpty(usernameET)) {
            appUtils.validationToast(context, usernameET, getString(R.string.error_username));
            return false;
        }
        if (usernameET.getText().toString().length() < 3) {
            appUtils.validationToast(context, usernameET, getString(R.string.length_username));
            return false;
        }

     /*   if (appUtils.isFieldEmpty(nameET)) {
            appUtils.validationToast(context, usernameET, getString(R.string.error_name));
            return false;
        }*/
        if (!isSelected) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_gender));
            return false;
        }

        if (!appUtils.isValidEmail(emailET)) {
            appUtils.validationToast(context, emailET, getString(R.string.error_email));
            return false;
        }

      /*  if (appUtils.isValidPhone(phoneET)) {
            appUtils.validationToast(context, phoneET, getString(R.string.error_phone));
            return false;
        }*/
       /* if (appUtils.isFieldEmpty(addressET)) {
            appUtils.validationToast(context, addressET, getString(R.string.error_address));
            return false;
        }*/


        if (appUtils.isNotValidPassword(passwordET)) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_password));
            return false;
        }
        if (appUtils.isFieldEmpty(confirmPassET))
        {
            appUtils.validationToast(context, passwordET, getString(R.string.confirm_password));
            return false;
        }
        if (!appUtils.getFieldValue(passwordET).equalsIgnoreCase(appUtils.getFieldValue(confirmPassET))) {
            appUtils.validationToast(context, passwordET, getString(R.string.error_pass_not_matched));
            return false;
        }
        if (!isTickPrivacySelected){
            appUtils.validationToast(context, passwordET, getString(R.string.error_tearm_condition));
            return false;

        }

        return true;
    }

    /*Manage gender*/
    private void manageGender() {

        if (isTrans) {
            femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
            maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
            transIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
        } else {
            if (isMale) {
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
            } else {
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
            }
        }


    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof GetProfile) {
            GetProfile res = (GetProfile) test;
            Log.d("RegisterActivity", "onApiResponse: "+new Gson().toJson(res));
            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                appUtils.showToast(res.getMessage());

                if (TextUtils.isEmpty(res.getData().getEmailVerifiedAt())) {
                    gotoVerifyOTP();
                }


            } else {
                appUtils.setSnackbar(maleIV, res.getMessage());
            }
        }

        if (test instanceof SocialLoginModel)
        {

            SocialLoginModel res = (SocialLoginModel) test;
            //appUtils.showToast(res.getMessage());

            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS){
                if (res.getData().getIsProfileComplete() == 0) {
//                    new Social_AcceptDetail_Dialog(context, logintype, logintype);
                    new Social_AcceptDetail_Dialog(context, logintype, res.getData().getEmail());
                } else {
                    gotoDashBoard();
                }

            } else {
                appUtils.setSnackbar(passwordET, res.getMessage());
            }

            Log.e( "onApiResponse: ", res.getMessage());

        }

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    /*Once user register successfully go to the otp verify otp activity*/
    private void gotoVerifyOTP() {

        appUtils.startActivity(context,
                OtpVerifyActivity.createIntent(context).putExtra(GlobalsVariables.BUNDLE.FROM, GlobalsVariables.BUNDLE.FROM_LOGIN_REGISTER),
                false,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
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
        user.setEmail(appUtils.getFieldValue(emailET));
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
