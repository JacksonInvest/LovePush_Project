package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

public class TestActivity extends BaseActivity implements LoginMvp {


    LoginRegisterProfilePresenter profilePresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pass;
    }

    @Override
    public void manageToolBar() {

    }

    private Activity context = TestActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, TestActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profilePresenter = new LoginRegisterProfilePresenter(this);
        profilePresenter.attachView(this);


    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
