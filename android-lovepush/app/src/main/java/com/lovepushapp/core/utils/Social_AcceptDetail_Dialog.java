package com.lovepushapp.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.lovepushapp.R;
import com.lovepushapp.activities.CreateProfileActivity;
import com.lovepushapp.activities.OtpVerifyActivity;
import com.lovepushapp.activities.qbLoginActivity;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.modules.SaveAfterSocialLogin.SocialMVP;
import com.lovepushapp.modules.SaveAfterSocialLogin.SocialPresenter;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lovepushapp.core.utils.ResourceUtils.getString;

/**
 * Created by vishal on 6/5/19.
 */

public class Social_AcceptDetail_Dialog implements SocialMVP {

    Context context;

    @BindView(R.id.socail_username)
    AppCompatEditText socailUsername;
    @BindView(R.id.social_ccp)
    CountryCodePicker socialCcp;
    @BindView(R.id.social_phone)
    AppCompatEditText socialPhone;
    @BindView(R.id.maleIV)
    ImageView maleIV;
    @BindView(R.id.maleLL)
    LinearLayout maleLL;
    @BindView(R.id.femaleIV)
    ImageView femaleIV;
    @BindView(R.id.femaleLL)
    LinearLayout femaleLL;
    @BindView(R.id.social_sendbtn)
    RelativeLayout socialSendbtn;
    @BindView(R.id.back_dial)
    ImageView backDial;
    String login_type;
    String mail;
    String gender = "";
    @BindView(R.id.emailET)
    AppCompatEditText emailET;
    @BindView(R.id.email_LL)
    LinearLayout emailLL;
    @BindView(R.id.transIV)
    ImageView transIV;
    @BindView(R.id.refralEt)
    AppCompatEditText refralEt;
    private boolean isSelected, isTickPrivacySelected, isMale = false, isTrans = false;
    SocialPresenter presenter;

    public Social_AcceptDetail_Dialog(Context context, String login_type, String mail) {
        this.context = context;
        this.login_type = login_type;
        this.mail=mail;
        presenter = new SocialPresenter(((AppCompatActivity) context));
        presenter.attachView(this);

        showWebDialog();
    }

    Dialog dialog;

    private void showWebDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.social_detail_dialog);

        ButterKnife.bind(this, dialog);
        if (mail!=null || login_type.equals("4")) {
            if (mail!=null && !mail.isEmpty())
            {
                emailET.setText(mail);
                emailET.setEnabled(false);
            }
            emailLL.setVisibility(View.VISIBLE);
        }
        else
            emailLL.setVisibility(View.GONE);

        //myWebView.setLayoutParams(FILL);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void manageGender() {
        if (isTrans) {
            femaleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
            maleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
            transIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_filled));
        } else {
            if (isMale) {
                maleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_filled));
                femaleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
                transIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
            } else {
                femaleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_filled));
                maleIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
                transIV.setImageDrawable(context.getResources().getDrawable(R.drawable.radio_unfilled));
            }
        }


    }


    @OnClick({R.id.back_dial, R.id.maleIV, R.id.maleLL, R.id.femaleIV, R.id.femaleLL, R.id.transLL, R.id.social_sendbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_dial:
                dialog.dismiss();
                break;
            case R.id.maleIV:
                break;
            case R.id.maleLL:
                isMale = true;
                isTrans = false;
                isSelected = true;
                manageGender();
                break;
            case R.id.femaleIV:
                break;
            case R.id.transLL:
                isMale = false;
                isTrans = true;
                isSelected = true;
                manageGender();
                break;
            case R.id.femaleLL:
                isMale = false;
                isTrans = false;
                isSelected = true;
                manageGender();
                break;
            case R.id.social_sendbtn:

                if (validation()) {

                    mail=emailET.getText().toString();
                    if (isTrans) {
                        gender = GlobalsVariables.GENDER.homo;
                    } else if (isMale) {
                        gender = GlobalsVariables.GENDER.Male;
                    } else {
                        gender = GlobalsVariables.GENDER.female;
                    }

                    String refrealCode = refralEt.getText().toString().trim().toUpperCase();


                    HashMap<String, String> fieldMap = new HashMap<>();

                    if (mail!=null) {
                        fieldMap.put("email", emailET.getText().toString());
                    }
                    fieldMap.put("username", socailUsername.getText().toString());
                    fieldMap.put("phone", socialPhone.getText().toString());
                    fieldMap.put("gender", gender);
                    fieldMap.put("device_type", "1");
                    fieldMap.put("phone_code", socialCcp.getSelectedCountryCodeWithPlus());
                    fieldMap.put("login_type", login_type);
                    fieldMap.put("referalBy", refrealCode);
                    presenter.save_social(fieldMap);

                }


                break;
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {


        SocialLoginModel res = (SocialLoginModel) test;

        dialog.dismiss();

        if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
            setSnackbar(socialPhone, res.getMessage());
            Toast.makeText(context, ""+res.getMessage(), Toast.LENGTH_SHORT).show();

            if (res.getData().getIsProfileComplete() == 0) {
                gotoCreateProfile();
            } else {
                gotoDashBoard();
            }

        } else {
            setSnackbar(socialPhone, res.getMessage());
            Toast.makeText(context, ""+res.getMessage(), Toast.LENGTH_SHORT).show();

        }

        Log.e("saveprofile: ", res.getMessage());

    }

    private void gotoPhoneVerify() {

        Intent intent = new Intent(context, OtpVerifyActivity.class);
        intent.putExtra(GlobalsVariables.BUNDLE.FROM, GlobalsVariables.BUNDLE.FROM_LOGIN_REGISTER);
        context.startActivity(intent);
    }

    private void gotoCreateProfile() {


        Intent intent = new Intent(context, CreateProfileActivity.class);
        context.startActivity(intent);


    }

    private void gotoDashBoard() {

        context.startActivity(new Intent(context, qbLoginActivity.class));

    }


    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }


    private boolean validation() {

        if (!isSelected) {

            setSnackbar(socailUsername, getString(R.string.error_gender));
            return false;
        }

        if (mail!=null || login_type.equals("4")) {
            if (emailET.getText().toString().isEmpty()) {

                setSnackbar(socailUsername, getString(R.string.error_email));
                return false;
            }
        }


        if (socailUsername.getText().toString().isEmpty()) {

            setSnackbar(socailUsername, getString(R.string.error_username));
            return false;
        }
        if (socailUsername.getText().toString().length() < 3) {
            setSnackbar(socailUsername, getString(R.string.length_username));
            return false;
        }
        return true;
    }

    public void setSnackbar(View view, String message) {
        Snackbar snackbar1 = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar1.show();
    }



}
