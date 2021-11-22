package com.lovepushapp.modules.Chat;

import android.app.Activity;

import com.lovepushapp.R;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.mvpBase.BasePresenter;
import com.lovepushapp.network.RestCallback;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Response;

public class ChatPresenter extends BasePresenter<ChatMvp> implements RestCallback {
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


        public ChatPresenter(Activity context) {
            this.context = context;
            MyApplication.getAppComponent(context).inject(this);

        }

        @Override
        public void attachView(ChatMvp mvpView) {
            super.attachView(mvpView);
        }

        @Override
        public void detachView() {
            super.detachView();
        }


        @Override
        public void onFailure(Call call, Throwable t, String serviceMode) {
            appUtils.showToast(context.getString(R.string.api_crashed));
        }

        @Override
        public void onSuccess(Call call, Response model, String serviceMode) {
            switch (serviceMode) {
//                case API_GLOBALS.API_NAME.recommendedProfile:
//                    getMvpView().onApiResponse(model.body());
//                    break;
//                case API_GLOBALS.API_NAME.userLike:
//                    getMvpView().onApiResponse(model.body());
//                    break;
//                case API_GLOBALS.API_NAME.userDislike:
//                    getMvpView().onApiResponse(model.body());
//                    break;
            }

        }


        @Override
        public void onTimeOut(Call call, Throwable t, String serviceMode) {
            appUtils.showToast(context.getString(R.string.api_time_out));
        }




//        public void hitRecommendedProfile() {
//            if (appUtils.isInternetConnection(context)) {
//
//                String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
//                HashMap<String, String> headerMap = new HashMap<>();
//                headerMap = appUtils.getApiHeaders(context);
//                Call<RecommendedProfile> call = apiInterface.apiRecommendedProfile(headerMap,userId);
//                call.enqueue(new RestProcess<RecommendedProfile>(API_GLOBALS.API_NAME.recommendedProfile, this, context, true));
//            } else {
//                appUtils.showToast(context.getString(R.string.internet_not_available));
//            }
//        }
//
//
//        public void hitUserLike(String recID) {
//            if (appUtils.isInternetConnection(context)) {
//                HashMap<String, String> headerMap = new HashMap<>();
//                headerMap = appUtils.getApiHeaders(context);
//                Call<UserLikeResponse> call = apiInterface.apiUserLike(headerMap, recID);
//                call.enqueue(new RestProcess<UserLikeResponse>(API_GLOBALS.API_NAME.userLike, this, context, true));
//            } else {
//                appUtils.showToast(context.getString(R.string.internet_not_available));
//            }
//        }
//
//        public void hitUserDislike(String recID) {
//            if (appUtils.isInternetConnection(context)) {
//                HashMap<String, String> headerMap = new HashMap<>();
//                headerMap = appUtils.getApiHeaders(context);
//                Call<UserDislikeResponse> call = apiInterface.apiUserDislike(headerMap, recID);
//                call.enqueue(new RestProcess<UserDislikeResponse>(API_GLOBALS.API_NAME.userDislike, this, context, true));
//            } else {
//                appUtils.showToast(context.getString(R.string.internet_not_available));
//            }
//        }


    }
