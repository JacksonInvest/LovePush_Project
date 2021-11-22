package com.lovepushapp.modules.SaveAfterSocialLogin;

import android.app.Activity;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.mvpBase.BasePresenter;
import com.lovepushapp.network.RestCallback;
import com.lovepushapp.network.RestProcess;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by vishal on 10/5/19.
 */

public class SocialPresenter extends BasePresenter<SocialMVP> implements RestCallback {

    @Inject
    public Util appUtils;
    @Inject
    public SharedStorage sharedStorage;
    @Named(GlobalsVariables.DAGGERS.NON_AUTH)
    @Inject
    public
    RestApiInterface apiInterface;
    @Named(GlobalsVariables.DAGGERS.AUTH)
    @Inject
    public
    RestApiInterface apiInterface_auth;
    Activity context;

    public SocialPresenter(Activity context) {
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);

    }

    @Override
    public void attachView(SocialMVP mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void onSuccess(Call call, Response model, String serviceMode) {
        switch (serviceMode) {
            case API_GLOBALS.API_NAME.save_social:
                SocialLoginModel res1 = (SocialLoginModel) model.body();
                manageSocialResponse(res1);
                getMvpView().onApiResponse(model.body(), serviceMode);

                break;
        }

    }

    public void save_social(HashMap<String,String> fieldMap) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
            fieldMap.put("user_id",userId);

            Call<SocialLoginModel> call = apiInterface.apisavesocial(headerMap, fieldMap);
            call.enqueue(new RestProcess<SocialLoginModel>(API_GLOBALS.API_NAME.save_social, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    @Override
    public void onTimeOut(Call call, Throwable t, String serviceMode) {

    }

    @Override
    public void onFailure(Call call, Throwable t, String serviceMode) {

    }

    private void manageSocialResponse(SocialLoginModel res) {
        if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {


            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Gender,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    String.valueOf(res.getData().getGender())
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getName()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.username,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getUsername()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    String.valueOf(res.getData().getProfileImage())
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getPhone()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getId() + "");

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Email,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getEmail() + "");

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId,
                    GlobalsVariables.STORAGE.TYPE_STRING,res.getData().getQuickblox_id()+"");

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.LoginResponse, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.referal_code,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getMyReferalCode() + "");

        }

    }

}
