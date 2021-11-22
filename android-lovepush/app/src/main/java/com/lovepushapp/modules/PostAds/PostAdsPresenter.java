package com.lovepushapp.modules.PostAds;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.model.homePosts.GetHomePostLikeDislike;
import com.lovepushapp.model.homePosts.addsmodel.GetPostsAdResponse;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.homePosts.postAdDeleteResponse;
import com.lovepushapp.model.local.PostAddRequest;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.mvpBase.BasePresenter;
import com.lovepushapp.network.RestCallback;
import com.lovepushapp.network.RestProcess;
import com.lovepushapp.network.intracter.BaseCallBack;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.referral.model.GetMyTeamResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class PostAdsPresenter extends BasePresenter<PostAdsMvp> implements RestCallback {
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


    public PostAdsPresenter(Activity context) {
        this.context = context;
        MyApplication.getAppComponent(context).inject(this);

    }

    @Override
    public void attachView(PostAdsMvp mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void onFailure(Call call, Throwable t, String serviceMode) {
        Log.d("postAdsPresenter", "onFailure: " + t.getMessage());

        appUtils.showToast(context.getString(R.string.api_crashed));
    }

    @Override
    public void onSuccess(Call call, Response model, String serviceMode) {
        switch (serviceMode) {
            case API_GLOBALS.API_NAME.addPostAds:
                getMvpView().onApiResponse(model.body(), serviceMode);

                break;
            case API_GLOBALS.API_NAME.addPost:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.getHomePosts:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.hitHomePostLikeDislike:
                getMvpView().onApiResponse(model.errorBody(), serviceMode);
                break;

            case API_GLOBALS.API_NAME.getAdPosts:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.getMyAdPosts:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteMyAdPost:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.updatePostAds:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.getMyTeam:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.deleteMyHomePost:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;
            case API_GLOBALS.API_NAME.updateHomePost:
                getMvpView().onApiResponse(model.body(), serviceMode);
                break;


        }

    }


    @Override
    public void onTimeOut(Call call, Throwable t, String serviceMode) {
        Log.d("postAdsPresenter", "onTimedOut: ");
        appUtils.showToast(context.getString(R.string.api_time_out));
    }


    public void hitAddPostAds(PostAddRequest req, String imagePath) {
//        Log.e("hitAddPostAds", new Gson().toJson(req));
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

           /* File file = new File(imagePath);
            RequestBody requestFile = appUtils.toRequestBodyForImage(file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.png", requestFile);
*/
            MultipartBody.Part body = null;

            if (!imagePath.equals("")) {
                File file = new File(imagePath);
                RequestBody requestFile = appUtils.toRequestBodyForImage(file);
                body = MultipartBody.Part.createFormData("image", "image.png", requestFile);
            }


            Map<String, RequestBody> params = new HashMap<>();
            params.put("title", appUtils.toRequestBody(req.title));
            params.put("description", appUtils.toRequestBody(req.description));
            params.put("post_type", appUtils.toRequestBody(req.post_type));
            params.put("location", appUtils.toRequestBody(req.location));
            params.put("latitude", appUtils.toRequestBody(req.latitude));
            params.put("longitude", appUtils.toRequestBody(req.longitude));
            params.put("is_post_anonymously", appUtils.toRequestBody(req.is_post_anonymously));
            params.put("radius", appUtils.toRequestBody(req.radius));

            Call<PostAdsResponse> call = apiInterface.apiAddPostAds(headerMap, body, params);
            call.enqueue(new RestProcess<PostAdsResponse>(API_GLOBALS.API_NAME.addPostAds, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitAddPostHome(PostAddRequest req) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            MultipartBody.Part body = null;
            Call<PostAdsResponse> call = null;
            if (!req.image.isEmpty()) {
                File file = new File(req.image);
                RequestBody requestFile = appUtils.toRequestBodyForImage(file);

                body = MultipartBody.Part.createFormData("image", "image.png", requestFile);

            }
            Map<String, RequestBody> params = new HashMap<>();
            if (req.description != null)
                params.put("description", appUtils.toRequestBody(req.description));
            if (req.title !=null)
                params.put("title", appUtils.toRequestBody(req.title));
            params.put("is_post_anonymously", appUtils.toRequestBody(req.is_post_anonymously));
            if (body == null) {
                call = apiInterface.apiAddHomePostWithoutImage(headerMap, params);
            } else {
                call = apiInterface.apiAddHomePost(headerMap, body, params);
            }
            call.enqueue(new RestProcess<>(API_GLOBALS.API_NAME.addPost, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetHomePosts(int page, boolean showProgress) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            Call<getHomePostResponse> call = apiInterface.apiGetHomePosts(page, headerMap);
            call.enqueue(new RestProcess<getHomePostResponse>(API_GLOBALS.API_NAME.getHomePosts, this, context, showProgress));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    public void hitHomePostLikeDislike(String isLike, String postId) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);


            String userId = (String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<GetHomePostLikeDislike> call = apiInterface.apiHomePostLikeDislike(headerMap, postId, userId, isLike);
            call.enqueue(new RestProcess<GetHomePostLikeDislike>(API_GLOBALS.API_NAME.hitHomePostLikeDislike, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetAdPosts(int page, String latitude, String longitude, boolean showProgress) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            String userId = (String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
            Call<GetPostsAdResponse> call = apiInterface.apiGetAdPosts(page, headerMap, userId, latitude, longitude);


            call.enqueue(new RestProcess<GetPostsAdResponse>(API_GLOBALS.API_NAME.getAdPosts, this, context, showProgress));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetMyAdPosts(int page, String latitude, String longitude, boolean showProgress) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Log.d("hitMYGetAdPosts", "hitMYAdPosts: page : "+page +", Headers : "+headerMap + " , user_id : " + ", latitude : "+latitude + " , longitude : "+longitude);
            Call<GetPostsAdResponse> call = apiInterface.apiGetMyAdPosts(page, headerMap, latitude, longitude);
            call.enqueue(new RestProcess<GetPostsAdResponse>(API_GLOBALS.API_NAME.getMyAdPosts, this, context, showProgress));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitDeleteMyAdPost(int post_id) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<postAdDeleteResponse> call = apiInterface.apiDeleteMyAdPost(headerMap, post_id);
            call.enqueue(new RestProcess<postAdDeleteResponse>(API_GLOBALS.API_NAME.deleteMyAdPost, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitUpdatePostAds(PostAddRequest req, String imagePath, Integer id) {
//        Log.e("hitAddPostAds", new Gson().toJson(req));
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            MultipartBody.Part body = null;

            if (imagePath.equals("")) {
                body = null;
            } else {

                File file = new File(imagePath);
                RequestBody requestFile = appUtils.toRequestBodyForImage(file);
                body = MultipartBody.Part.createFormData("image", "image.png", requestFile);
            }


            Map<String, RequestBody> params = new HashMap<>();
            params.put("id", appUtils.toRequestBody(String.valueOf(id)));
            params.put("title", appUtils.toRequestBody(req.title));
            params.put("description", appUtils.toRequestBody(req.description));
            params.put("post_type", appUtils.toRequestBody(req.post_type));
            params.put("location", appUtils.toRequestBody(req.location));
            params.put("latitude", appUtils.toRequestBody(req.latitude));
            params.put("longitude", appUtils.toRequestBody(req.longitude));
            params.put("is_post_anonymously", appUtils.toRequestBody(req.is_post_anonymously));
            params.put("radius", appUtils.toRequestBody(req.radius));

            Call<PostAdsResponse> call = apiInterface.apiUpdatePostAds(headerMap, body, params);
            call.enqueue(new RestProcess<PostAdsResponse>(API_GLOBALS.API_NAME.updatePostAds, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }

    public void hitGetMyTeam() {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<GetMyTeamResponse> call = apiInterface.apiGetMyTeam(headerMap);
            call.enqueue(new RestProcess<GetMyTeamResponse>(API_GLOBALS.API_NAME.getMyTeam, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }

    }

    public void hitDeleteMyHomePost(Integer post_id, ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<postAdDeleteResponse> call = apiInterface.apiDeleteMyHomePost(headerMap, post_id);
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

    public void reportImage(Integer image_id,String type, ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<CommonResponse> call = apiInterface.reportImage(headerMap, image_id,type);
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
    public void blockUnblock(Integer image_id,String type, ResponseListner listner) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);
            //String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

            Call<CommonResponse> call = apiInterface.reportImage(headerMap, image_id,type);
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

    public void hitUpdateHomePost(PostAddRequest req, String imagePath, Integer id) {
        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            MultipartBody.Part body = null;

            if (imagePath.equals("")) {
                body = null;
            } else {

                File file = new File(imagePath);
                RequestBody requestFile = appUtils.toRequestBodyForImage(file);
                body = MultipartBody.Part.createFormData("image", "image.png", requestFile);
            }

            Map<String, RequestBody> params = new HashMap<>();
            params.put("id", appUtils.toRequestBody(String.valueOf(id)));
            params.put("title", appUtils.toRequestBody(req.title));
            params.put("description", appUtils.toRequestBody(req.description));
            params.put("is_post_anonymously", appUtils.toRequestBody(req.is_post_anonymously));
            Call<PostAdsResponse> call = apiInterface.apiUpdateHomePost(headerMap, body, params);
            call.enqueue(new RestProcess<PostAdsResponse>(API_GLOBALS.API_NAME.updateHomePost, this, context, true));
        } else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }
}
