package com.lovepushapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.response.LoginResponse;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.utils.LocaleUtils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.lovepushapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {

    private Activity context = SplashActivity.this;
    private int SPLASH_TIME_OUT = 5000;


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void manageToolBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        intent.toString();
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.i("Base64", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }

//        Toast.makeText(context, "set language", Toast.LENGTH_SHORT).show();
        setLanguage();
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                appFlow();
            }
        });


    }


    private void setLanguage() {
        String current_locale = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING);
//        Toast.makeText(context, "current language "+current_locale, Toast.LENGTH_SHORT).show();
//                    String english=getString(R.string.english);
//                    String german=getString(R.string.german);

        if (current_locale.equalsIgnoreCase("en"))
        {
            LocaleUtils.setLocale(SplashActivity.this,"en");
//            English
//            change_languageTV.setText(english);


        }
        if (current_locale.equalsIgnoreCase("de"))
        {
            LocaleUtils.setLocale(SplashActivity.this,"de");
//            change_languageTV.setText(german);
        }
    }

    private void appFlow() {

        new Handler().postDelayed(() -> {
            try {

                setLanguage();

                LoginResponse response = getLoginModel();
                if (TextUtils.isEmpty(response.getData().getEmail_verified_at())) {
                    goToLoginScreen();
                } else if (response.getData().getIs_profile_complete() == 0) {
                    goToLoginScreen();
                } else if (response.getToken().equalsIgnoreCase("null") || TextUtils.isEmpty(response.getToken())) {
                    goToLoginScreen();
                } else {
                    goToDashboardScreen();
                }
            } catch (Exception e) {
                e.printStackTrace();
                goToLoginScreen();
            }
        }, SPLASH_TIME_OUT);

    }

    private void goToLoginScreen() {


        setLanguage();

        appUtils.startActivity(context,
                LoginActivity.createIntent(context),
                true,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );


    }


    private void goToDashboardScreen() {


        goToDashBoardScreen2();

       /* try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent();
                String packageName = getPackageName();
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {

                    *//*for disabling the battery optimizations*//*

                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                }
                else
                {
//                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                    intent.setData(Uri.parse("package:" + packageName));
//                    startActivity(intent);

                    goToDashBoardScreen2();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            goToDashBoardScreen2();
            Log.e("PERMISSION_ERROR", String.valueOf(e));
        }*/


    }


    private void goToDashBoardScreen2(){

        SharedPrefsHelper.getInstance().removeSharedDialogId();

        appUtils.startActivity(context,
                DashboardActivity.createIntent(context),
                true,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );


    }

}
