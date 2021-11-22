package com.lovepushapp.activities;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class SupportActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.nameET)
    AppCompatEditText nameET;

    @BindView(R.id.subjectET)
    AppCompatEditText subjectET;

    @BindView(R.id.messageET)
    AppCompatEditText messageET;

    @BindView(R.id.submitBTN)
    AppCompatButton submitBTN;

    Context context;


    LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        loginRegisterProfilePresenter=new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

//        setContentView(R.layout.activity_support);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_support;
    }

    @Override
    public void manageToolBar() {

    }


    public static Intent createIntent(Context context) {
        return new Intent(context, SupportActivity.class);
    }

    @OnClick({R.id.backIV,R.id.submitBTN})    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case  R.id.submitBTN:
                if (validation())
                {

                    String name=nameET.getText().toString();
                    String subject=subjectET.getText().toString();
                    String message=messageET.getText().toString();


                    /*hit support api*/
                    loginRegisterProfilePresenter.hitSupport(name,subject,message);

//                    Toast.makeText(context, "Testing", Toast.LENGTH_SHORT).show();

                }
                break;




        }
    }

    /*Check all required field empty or not empty*/
    private boolean validation() {
           if (appUtils.isFieldEmpty(nameET)) {
                appUtils.validationToast(context, nameET, getString(R.string.error_name));
                return false;
            }

        else if (appUtils.isFieldEmpty(subjectET)) {
            appUtils.validationToast(context, nameET, getString(R.string.error_subject));
            return false;
        }
        else
        if (appUtils.isFieldEmpty(messageET)) {
            appUtils.validationToast(context, nameET, getString(R.string.error_message));
            return false;
        }

      /*  if (appUtils.isFieldEmpty(lovePushET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_love_push));
            return false;
        }

        if (appUtils.isFieldEmpty(relationShipET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_relationship));
            return false;
        }
        if (appUtils.isFieldEmpty(languageET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_language));
            return false;
        }
        if (appUtils.isFieldEmpty(sizeET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_please_enter_size));
            return false;
        }
        if (appUtils.isFieldEmpty(eyeColorET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_eye_color));
            return false;
        }
        if (appUtils.isFieldEmpty(hairColorET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_hair_color));
            return false;
        }
        if (appUtils.isFieldEmpty(educationET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_education));
            return false;
        }

        if (appUtils.isFieldEmpty(jobET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_job));
            return false;
        }
        if (appUtils.isFieldEmpty(employerET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_enter_employer));
            return false;
        }
        if (appUtils.isFieldEmpty(hobbiesET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_hobbies));
            return false;
        }
        if (TextUtils.isEmpty(gender)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_sexual_orientataion));
            return false;
        }
        if (appUtils.isFieldEmpty(aboutUsET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_about));
            return false;
        }*/
            return true;
        }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        
        if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.support))
        {
            try{

//                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Request Sent Successful.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            catch (Exception e)
            {
                e.printStackTrace();
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

