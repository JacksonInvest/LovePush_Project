package com.lovepushapp.modules.Login;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lovepushapp.R;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.model.DeleteAccountModel;
import com.lovepushapp.model.FAQModel.GetFaqResponse;
import com.lovepushapp.model.PlanUrl;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.local.RegisterData;
import com.lovepushapp.model.plans.PlansEntity;
import com.lovepushapp.model.response.ChangePassResponse;
import com.lovepushapp.model.response.Chat.DeleteMatchResponse;
import com.lovepushapp.model.response.Chat.SaveDialogResponse;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.lovepushapp.model.response.Explore.Explore;
import com.lovepushapp.model.response.ExploreRadar.GetExploreRadarResponse;
import com.lovepushapp.model.response.ForgotPassResponse;
import com.lovepushapp.model.response.LikeResponse;
import com.lovepushapp.model.response.LogoutResponse;
import com.lovepushapp.model.response.NotificationResponse;
import com.lovepushapp.model.response.PersonalitytestModel.PersonalitytestModel;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.SaveForgotPassWordResponse;
import com.lovepushapp.model.response.SendVerificationCodeResponse;
import com.lovepushapp.model.response.SettingResponse;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.model.response.blockUserResponse;
import com.lovepushapp.model.response.SubmitPerQuesResponse;
import com.lovepushapp.model.response.clearNotificationResponse;
import com.lovepushapp.model.response.deleteChatResponse;
import com.lovepushapp.model.response.deleteNotificationResponse;
import com.lovepushapp.model.response.deleteTestResponse;
import com.lovepushapp.model.response.readNotificationResponse;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.unmatchUserResponse;
import com.lovepushapp.model.response.updateUserLocationResponse;
import com.lovepushapp.model.searchUsers.GetSearchUsersResponse;
import com.lovepushapp.mvpBase.BasePresenter;
import com.lovepushapp.network.RestCallback;
import com.lovepushapp.network.RestProcess;
import com.lovepushapp.network.intracter.BaseCallBack;
import com.lovepushapp.network.intracter.ResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class LoginRegisterProfilePresenter extends BasePresenter<LoginMvp> implements RestCallback {
    @Inject
    public Util appUtils;
    @Inject
    public SharedStorage sharedStorage;
    @Named  (GlobalsVariables.DAGGERS.NON_AUTH)
    @Inject
    public
    RestApiInterface apiInterface;
    @Named(GlobalsVariables.DAGGERS.AUTH)
    @Inject
    public
    RestApiInterface apiInterface_auth;
    Activity context;



    public LoginRegisterProfilePresenter(Activity context) {
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);

    }

    @Override
    public void attachView(LoginMvp mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void onFailure(Call call, Throwable t, String serviceMode) {

        Log.e( "onFailure: ", t.getMessage());

        appUtils.showToast(context.getString(R.string.api_crashed));
    }

    @Override
    public void onSuccess(Call call, Response model, String serviceMode) {

        if (model.body()==null && serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.like))
        {
            try {
                JSONObject jsonObject = new JSONObject(model.errorBody().string());
                getMvpView().onApiResponse(jsonObject.getString("message"), "request");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        switch (serviceMode) {

            case API_GLOBALS.API_NAME.register:
                GetProfile  response = (GetProfile) model.body();
                manageGetProfile(response);
                getMvpView().onApiResponse(model.body(), serviceMode);

                break;

            case API_GLOBALS.API_NAME.login:
                GetProfile res1 = (GetProfile) model.body();
                manageGetProfile(res1);
                getMvpView().onApiResponse(model.body(), serviceMode);

                break;

            case API_GLOBALS.API_NAME.social:
                SocialLoginModel res3 = (SocialLoginModel) model.body();
                manageSocialResponse(res3);
                getMvpView().onApiResponse(model.body(), serviceMode);

                break;
            case API_GLOBALS.API_NAME.createProfile:
                GetProfile res = (GetProfile) model.body();
                manageGetProfile(res);
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.updateProfileDelete:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.changePassword:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.getProfile:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.personalityQuestions:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.submitPersonalityTest:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.forgotPassword:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.instalogin:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.verifyCode:

                GetProfile response4 = (GetProfile) model.body();
                manageGetProfile(response4);
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.sendVerificationCode:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.saveForgotPassword:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.logout:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.updateProfile:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.explore:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.exploreRadar:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.like:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.connect:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.notification:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.accept_reject_ConnectRequest:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.chatRequest:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.accept_reject_ChatRequest:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.newest_MatchList:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.saveQbDialogId:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteMatchRecord:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.myPosts:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.getSearchUsers:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.reportChat:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteChat:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.getSetting:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.setNotification:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.setLikeRequest:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.setChatRequest:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.setHideFromSearch:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.blockUser:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.support:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.unmatch:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.clearNotifications:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.readNotifications:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteNotification:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteAccount:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.deletePersonalityTest:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;
            case API_GLOBALS.API_NAME.faq:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;

            case API_GLOBALS.API_NAME.updateLocation:
                getMvpView().onApiResponse(model.body(),serviceMode);
                break;



        }

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


    public void manageGetProfile(GetProfile res) {
        if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
            if (res.getToken()!=null && !res.getToken().isEmpty())
            {
                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.token,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        res.getToken()
                );
            }
            if (res.getData().getName()!=null && !res.getData().getName().isEmpty())
            {
                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name,
                        GlobalsVariables.STORAGE.TYPE_STRING,
                        res.getData().getName()
                );
            }
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone_code,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getPhoneCode()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.verification_code,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    String.valueOf(res.getData().getVerificationCode())
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    String.valueOf(res.getData().getProfileImage())
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Gender,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    String.valueOf(res.getData().getGender())
            );


            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.username,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getUsername()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.phone,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getPhone()
            );

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getId() + "");

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId,
                    GlobalsVariables.STORAGE.TYPE_STRING,res.getData().getQuickbloxId()+"");

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Email,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getEmail() + "");
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.LoginResponse, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());


            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());

            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.referal_code,
                    GlobalsVariables.STORAGE.TYPE_STRING,
                    res.getData().getMyReferalCode() + "");

        }

    }

    @Override
    public void onTimeOut(Call call, Throwable t, String serviceMode) {

    }


    public void hitLogin(LoginRegisterRequest request) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            Call<GetProfile> call = apiInterface.apiLogin(headerMap, request.username, request.password);
            call.enqueue(new RestProcess<GetProfile>(API_GLOBALS.API_NAME.login, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }


    }

    public void hit_social_login(String email,String login_type,String id,String name ) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<SocialLoginModel> call = apiInterface.social_login(headerMap, "1", email,login_type,id,name);
            call.enqueue(new RestProcess<>(API_GLOBALS.API_NAME.social, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_insta_social_login(String login_type,String id,String name ) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            Call<SocialLoginModel> call = apiInterface.social_instal_login(headerMap, "1", login_type,id,name);
            call.enqueue(new RestProcess<SocialLoginModel>(API_GLOBALS.API_NAME.social, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }



    public void hitRegister(RegisterData req) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Log.d("loginRegisterPresenter", "hitRegister: "+req.getName());

            Call<GetProfile> call = apiInterface.apiRegister(headerMap,
                    req.getUsername(),
                    req.getName(),
                    req.getGender(),

                    req.getEmail(),
                    req.getPhone_code(),
                    req.getPhone(),

                    req.getPassword(),req.getPassword_confirmation(),
                    req.getDevice_type(),

                    req.getLogin_type(),
                    req.getReferalBy()
                    /*req.address,
                    req.latitude,
                    req.longitude,
                    req.country,
                    req.city,*/

            );
            call.enqueue(new RestProcess<GetProfile>(API_GLOBALS.API_NAME.register, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitCreateProfile(LoginRegisterRequest req) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);


            Call<GetProfile> call = apiInterface.apiCreateProfileWithpuImage(headerMap,
                    req.name,
                    req.love_push_intention,
                    req.relationship,
                    req.language,
                    req.size,
                    req.haircolor,
                    req.education,
                    req.job,
                    req.employer,
                    req.hobbies,
                    req.sexual_orientation,
                    req.aboutme,
                    req.user_id,
                    req.eyecolor,
                    req.age,
                    req.physique);
            call.enqueue(new RestProcess<>(API_GLOBALS.API_NAME.createProfile, this, context, true));

        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    /*Change password api*/
    public void hitChangePass(LoginRegisterRequest req) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            Call<ChangePassResponse> call = apiInterface.apiChangePassword(headerMap,
                    req.new_password,
                    req.old_password,
                    req.new_password_confirmation);
            call.enqueue(new RestProcess<ChangePassResponse>(API_GLOBALS.API_NAME.changePassword, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetProfile(String userid,ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);
            Call<GetProfile> call = apiInterface.apiGetProfile(headerMap,userid);
            call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);
                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }



    public void hitPersonalityQuestions() {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            Call<PersonalitytestModel> call = apiInterface.apiPersonalityQuestions(headerMap);
            call.enqueue(new RestProcess<PersonalitytestModel>(API_GLOBALS.API_NAME.personalityQuestions, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitSubmitPersonalityTest(String userid,String questionid,String optionid) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            HashMap<String, String> fieldMap = new HashMap<>();
            fieldMap.put("user_id",userid);
            fieldMap.put("question_id",questionid);
            fieldMap.put("option_ids",optionid);

            Call<SubmitPerQuesResponse> call = apiInterface.apiSubmitPersonalityTest(headerMap, fieldMap);
            call.enqueue(new RestProcess<SubmitPerQuesResponse>(API_GLOBALS.API_NAME.submitPersonalityTest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


  /*  public void hitForgotPassword(LoginRegisterRequest req) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<ForgotPassResponse> call = apiInterface.apiForgotPassword(headerMap,
                    req.phone_code,
                    req.phone);
            call.enqueue(new RestProcess<ForgotPassResponse>(API_GLOBALS.API_NAME.forgotPassword, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }*/

    public void hitVerifyCode(LoginRegisterRequest req) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<GetProfile> call = apiInterface.apiVerifyCode(headerMap,
                    req.user_id,
                    req.otp);
            call.enqueue(new RestProcess<GetProfile>(API_GLOBALS.API_NAME.verifyCode, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitSendVerificationCode(LoginRegisterRequest req) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<SendVerificationCodeResponse> call = apiInterface.apiSendVerificationCode(headerMap,
                    /* req.user_id,*/
                    req.email);
            call.enqueue(new RestProcess<SendVerificationCodeResponse>(API_GLOBALS.API_NAME.sendVerificationCode, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitSaveForgotPassword(LoginRegisterRequest req) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<SaveForgotPassWordResponse> call = apiInterface.apiSaveForgotPassword(headerMap,
                    req.user_id,
                    req.password,
                    req.password_confirmation);
            call.enqueue(new RestProcess<SaveForgotPassWordResponse>(API_GLOBALS.API_NAME.saveForgotPassword, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hitLogout() {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<LogoutResponse> call = apiInterface.apiLogout(headerMap);
            call.enqueue(new RestProcess<LogoutResponse>(API_GLOBALS.API_NAME.logout, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetExplore(int page, double latitude, double longitude) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";

            String latitude1 = String.valueOf(latitude);
            String longitude1 = String.valueOf(longitude);
            Call<Explore> call = apiInterface.apiExplore(page,headerMap,userId,latitude1,longitude1);
            call.enqueue(new RestProcess<Explore>(API_GLOBALS.API_NAME.explore, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hitGetExploreFilter(int page, String min_age, String max_age, int gender, double latitude, double longitude) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";

            String latitude1 = String.valueOf(latitude);
            String longitude1 = String.valueOf(longitude);
            Call<Explore> call = apiInterface.apiExploreFilter(page,headerMap,userId,min_age,max_age,gender,latitude1,longitude1);
            call.enqueue(new RestProcess<Explore>(API_GLOBALS.API_NAME.explore, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetExploreRadar(int page, double latitude, double longitude) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            String latitude1 = String.valueOf(latitude);
            String longitude1 = String.valueOf(longitude);
            Call<GetExploreRadarResponse> call = apiInterface.apiExploreRadar(page,headerMap,userId,latitude1,longitude1);


            Log.d("hitGetExploreRadar", "hitGetExploreRadar: Headers :  "+headerMap.toString() +"user Id  "+userId + "latitude1 : "+latitude1 + "longitude2 "+longitude1);

            call.enqueue(new RestProcess<>(API_GLOBALS.API_NAME.exploreRadar, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetExploreFilterRadar(int page,String min_age, String max_age, int gender, double latitude, double longitude) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            String latitude1 = String.valueOf(latitude);
            String longitude1 = String.valueOf(longitude);
            Call<GetExploreRadarResponse> call = apiInterface.apiExploreRadarFilter(page,headerMap,userId,min_age,max_age,gender,latitude1,longitude1);
            call.enqueue(new RestProcess<GetExploreRadarResponse>(API_GLOBALS.API_NAME.exploreRadar, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void deleteprofile(String id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            Call<GetProfile> call = apiInterface.updaeprofiledelete(headerMap,id);
            call.enqueue(new RestProcess<>(API_GLOBALS.API_NAME.updateProfileDelete, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void updateProfile(LoginRegisterRequest req, ResponseListner listner) {
        try {
//            Log.e("updateProfile", new Gson().toJson(req).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            MultipartBody.Part body = null;

            if (!TextUtils.isEmpty(req.profileImage)) {
                File file = new File(req.profileImage);
                RequestBody requestFile = appUtils.toRequestBodyForImage(file);
                body = MultipartBody.Part.createFormData("profile_image", "profile_image.png", requestFile);
            }
            Map<String, RequestBody> params = new HashMap<>();

            params.put("name", appUtils.toRequestBody(req.name));
            params.put("address", appUtils.toRequestBody(req.address));
            params.put("gender", appUtils.toRequestBody(req.gender));
//            @Field("physique") String physique
            params.put("physique",appUtils.toRequestBody(req.physique));



            if (req.city!=null)
            {
                params.put("city", appUtils.toRequestBody(req.city));
            }

            params.put("age", appUtils.toRequestBody(req.age));
            /*params.put("latitude", appUtils.toRequestBody(req.latitude));
            params.put("longitude", appUtils.toRequestBody(req.longitude));*/

            params.put("love_push_intention", appUtils.toRequestBody(req.love_push_intention));
            params.put("relationship", appUtils.toRequestBody(req.relationship));
            params.put("language", appUtils.toRequestBody(req.language));
            if (req.city!=null)
            {params.put("language", appUtils.toRequestBody(req.language));}

            params.put("size", appUtils.toRequestBody(req.size));
            params.put("haircolor", appUtils.toRequestBody(req.haircolor));
            params.put("eyecolor", appUtils.toRequestBody(req.eyecolor));

            params.put("education", appUtils.toRequestBody(req.education));
            params.put("job", appUtils.toRequestBody(req.job));

            // params.put("employer", appUtils.toRequestBody(req.employer));

            params.put("hobbies", appUtils.toRequestBody(req.hobbies));
            params.put("gender",appUtils.toRequestBody(req.gender));
            params.put("sexual_orientation", appUtils.toRequestBody(req.sexual_orientation));
            params.put("aboutme", appUtils.toRequestBody(req.aboutme));


            Call<GetProfile> call = apiInterface.updateProfile(headerMap, body, params);
            call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);
                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void getPlans(ResponseListner listner){
        Call<PlansEntity> call = apiInterface.getPlans();
        call.enqueue(new BaseCallBack() {
            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                listner.onError(t.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                listner.onComplete(response);
            }
        });
    }
    public void subscriptionOrCancel(String token, String plan_id, ResponseListner listner){
        Call<PlanUrl> call;
        if (plan_id.isEmpty())
            call = apiInterface.cancelSubscription("Bearer "+token);
        else
            call = apiInterface.subscribe("Bearer "+token,plan_id);
        call.enqueue(new BaseCallBack() {
            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                listner.onError(t.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                listner.onComplete(response);
            }
        });
    }

    public void ProfileAddImages(List<Uri> upFileList) {

        if (appUtils.isInternetConnection(context)) {
            List<MultipartBody.Part> parts = new ArrayList<>();
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            for (int i=0; i < upFileList.size(); i++){
                parts.add(appUtils.prepareFilePart(context,"images["+i+"]", upFileList.get(i)));
            }


            Call<JsonObject> call = apiInterface.updateProfileaddImages(headerMap, parts);
            call.enqueue(new RestProcess<JsonObject>(API_GLOBALS.API_NAME.updateProfileAdd, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hitLike_Dislike(Integer id, String like) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<LikeResponse> call = apiInterface.apiLikeDislike(id,/*headerMap,*/userId,like);
            call.enqueue(new RestProcess<LikeResponse>(API_GLOBALS.API_NAME.like, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitConnect(Integer id) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<LikeResponse> call = apiInterface.apiConnect(id,headerMap,userId);
            call.enqueue(new RestProcess<LikeResponse>(API_GLOBALS.API_NAME.connect, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetNotification(boolean showProgress) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<NotificationResponse> call = apiInterface.apiNotification(headerMap,userId);
            call.enqueue(new RestProcess<NotificationResponse>(API_GLOBALS.API_NAME.notification, this, context, showProgress));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitAcceptConnectRequest(String connect_id, String status) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<LikeResponse> call = apiInterface.apiAcceotConnectRequest(connect_id,headerMap,status);
            call.enqueue(new RestProcess<LikeResponse>(API_GLOBALS.API_NAME.accept_reject_ConnectRequest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitChatRequest(String id) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<LikeResponse> call = apiInterface.apiChatRequest(id,headerMap,userId);
            call.enqueue(new RestProcess<LikeResponse>(API_GLOBALS.API_NAME.chatRequest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }

    }

    public void hitChatAccept_RejectRequest(String connect_id, String status) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<LikeResponse> call = apiInterface.apiAcceptRejectChatRequest(connect_id,headerMap,status);
            call.enqueue(new RestProcess<LikeResponse>(API_GLOBALS.API_NAME.accept_reject_ChatRequest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetMyMatches() {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<UserChatList> call = apiInterface.apiMyMatchList(headerMap,userId);
            call.enqueue(new RestProcess<UserChatList>(API_GLOBALS.API_NAME.newest_MatchList, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetMyMatches1(int current_page,ResponseListner listner) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<UserChatList> call = apiInterface.apiMyMatchList1(current_page,headerMap,userId);
            call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);

                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_create_dialog_chat(String match_id ,String qb_dialog_id) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<SaveDialogResponse> call = apiInterface.saveQbDialogId(headerMap, match_id, qb_dialog_id);
            call.enqueue(new RestProcess<SaveDialogResponse>(API_GLOBALS.API_NAME.saveQbDialogId, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_delete_match(String server_match_id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<DeleteMatchResponse> call = apiInterface.deleteMatchRecord(headerMap, server_match_id);
            call.enqueue(new RestProcess<DeleteMatchResponse>(API_GLOBALS.API_NAME.deleteMatchRecord, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_get_my_posts(int page,String userId) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<getHomePostResponse> call = apiInterface.getMyPosts(page,headerMap,userId);
            call.enqueue(new RestProcess<getHomePostResponse>(API_GLOBALS.API_NAME.myPosts, this, context, false));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetSearchUsers(String searchString, int current_page) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<GetSearchUsersResponse> call = apiInterface.getSearchUsers(current_page,headerMap,searchString);
            call.enqueue(new RestProcess<GetSearchUsersResponse>(API_GLOBALS.API_NAME.getSearchUsers, this, context, false));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_report_chat(String report_user_id, String des,ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);


            Call<CommonResponse> call = apiInterface.hitReportChat(headerMap,report_user_id,des,userId);
            call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);

                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_delete_chat(String deleteNeededMAtchId, String deleteStatus, String receiver_user_id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<deleteChatResponse> call = apiInterface.deleteChat(headerMap, deleteStatus,deleteNeededMAtchId,receiver_user_id);
            call.enqueue(new RestProcess<deleteChatResponse>(API_GLOBALS.API_NAME.deleteChat, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetSettings() {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";
//            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);


            Call<SettingResponse> call = apiInterface.apiGetSettings(headerMap,userId);
            call.enqueue(new RestProcess<SettingResponse>(API_GLOBALS.API_NAME.getSetting, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitSetNotification(int status) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<SettingResponse> call = apiInterface.setNotification(headerMap, status+"",userId);
            call.enqueue(new RestProcess<SettingResponse>(API_GLOBALS.API_NAME.setNotification, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }

    }


    public void hitSetChatRequest( int status) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<SettingResponse> call = apiInterface.setChatRequest(headerMap, ""+status,userId);
            call.enqueue(new RestProcess<SettingResponse>(API_GLOBALS.API_NAME.setChatRequest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitsetLikeRequest( int status) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<SettingResponse> call = apiInterface.setLikeRequest(headerMap, ""+status,userId);
            call.enqueue(new RestProcess<SettingResponse>(API_GLOBALS.API_NAME.setLikeRequest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitsetHideFromSearch( int status) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<SettingResponse> call = apiInterface.setHideFromHome(headerMap, ""+status,userId);
            call.enqueue(new RestProcess<SettingResponse>(API_GLOBALS.API_NAME.setHideFromSearch, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_block_user(String deleteStatus, String receiver_user_id,ResponseListner listner) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<blockUserResponse> call = apiInterface.hitBlockUser(headerMap, deleteStatus,receiver_user_id,userId);
            call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);

                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }

    }

    public void hitSupport(String name, String subject, String message) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<blockUserResponse> call = apiInterface.hitSupportUser(headerMap, name,subject,message);
            call.enqueue(new RestProcess<blockUserResponse>(API_GLOBALS.API_NAME.support, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hit_forgot_password(String email) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<ForgotPassResponse> call = apiInterface.hit_forgot_password(headerMap, email);
            call.enqueue(new RestProcess<ForgotPassResponse>(API_GLOBALS.API_NAME.forgotPassword, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_unmatch_user(String match_id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            Call<unmatchUserResponse> call = apiInterface.hitUnMatchUser(headerMap, match_id);
            call.enqueue(new RestProcess<unmatchUserResponse>(API_GLOBALS.API_NAME.unmatch, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }


    }

    public void hit_clear_notifications() {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<clearNotificationResponse> call = apiInterface.hitClearNotifications(headerMap, userId);
            call.enqueue(new RestProcess<clearNotificationResponse>(API_GLOBALS.API_NAME.clearNotifications, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }


    }

    public void hitReadNotifications(boolean showProgress) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<readNotificationResponse> call = apiInterface.hitReadNotifications(headerMap, userId);
            call.enqueue(new RestProcess<readNotificationResponse>(API_GLOBALS.API_NAME.readNotifications, this, context, showProgress));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitDeleteNotification(String noti_id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<deleteNotificationResponse> call = apiInterface.hitDeleteNotification(headerMap, userId,noti_id);
            call.enqueue(new RestProcess<deleteNotificationResponse>(API_GLOBALS.API_NAME.deleteNotification, this, context, false));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hit_delete_account(String reasons,String des,ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);
            HashMap<String, String> bodyMap=new HashMap<>();
            bodyMap.put("lang_id","en");
            bodyMap.put("reasons",reasons);
            bodyMap.put("description",des);


            Call<DeleteAccountModel> call = apiInterface.hitDeleteAccount(headerMap,bodyMap);
          call.enqueue(new BaseCallBack() {
                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    listner.onError(t.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    listner.onComplete(response);
                }
            });
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hit_delete_test() {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<deleteTestResponse> call = apiInterface.hitDeletePersonalityTest(headerMap, userId);
            call.enqueue(new RestProcess<deleteTestResponse>(API_GLOBALS.API_NAME.deletePersonalityTest, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetFAQ(String lang_id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            Call<GetFaqResponse> call = apiInterface.hitFAQ(headerMap,lang_id);
            call.enqueue(new RestProcess<GetFaqResponse>(API_GLOBALS.API_NAME.faq, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }

    }

    public void hitUpdateLocation(String lat, String lng, String address) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<updateUserLocationResponse> call = apiInterface.apiUpdateLocation(headerMap,userId,lat,lng,address);
            call.enqueue(new RestProcess<updateUserLocationResponse>(API_GLOBALS.API_NAME.updateLocation, this, context, false));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

}
