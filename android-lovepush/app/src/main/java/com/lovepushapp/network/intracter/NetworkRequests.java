package com.lovepushapp.network.intracter;

import android.content.Context;
import android.net.Uri;

import com.lovepushapp.R;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.network.ApiHitter;
import com.lovepushapp.network.RestProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Multipart;

public class NetworkRequests {


    public static void updateProfileImages(Context context, Uri upFileList, final ResponseListner listner) {
        Util util = new Util();
        List<MultipartBody.Part> parts = new ArrayList<>();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap = util.getApiHeaders(context);


            parts.add(util.prepareFilePart(context, "images[0]", upFileList));



        ApiHitter.ApiHitter().updateProfileaddImages(headerMap, parts).enqueue(new BaseCallBack() {
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


    public static void selectProfileImage(Context context,int image_id, final ResponseListner  listner){
        Util util = new Util();
        List<MultipartBody.Part> parts = new ArrayList<>();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap = util.getApiHeaders(context);

        ApiHitter.ApiHitter().apiSetProfileImageByImageId(headerMap,image_id).enqueue(new BaseCallBack(){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                listner.onComplete(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                listner.onError(t.getMessage());
            }
        });


    }

    public static void checkId(Context context, String email, String login_type, String id, final ResponseListner listner) {
        Util appUtils = new Util();

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("locale", "en");
            HashMap<String, String> params = new HashMap<>();
            headerMap.put("locale", "en");
            headerMap.put("login_type",login_type);
            headerMap.put("social_id",id);
            if (!email.isEmpty())
            headerMap.put("email",email);
            Call<SocialLoginModel> call = ApiHitter.ApiHitter().checkId(headerMap, params);

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

    public static void createProfile(Context context, Uri pic, LoginRegisterRequest req, final ResponseListner listner) {
        Util appUtils = new Util();
        Map<String, RequestBody> map = new HashMap<>();

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap;
            headerMap = appUtils.getApiHeaders(context);
            map.put("name", getRequest(req.name));
            map.put("love_push_intention", getRequest(req.love_push_intention));
            map.put("relationship", getRequest(req.relationship));
            map.put("language", getRequest(req.language));
            map.put("size", getRequest(req.size));
            map.put("haircolor", getRequest(req.haircolor));
            map.put("education", getRequest(req.education));
            map.put("job", getRequest(req.job));
            map.put("employer", getRequest(req.employer));
            map.put("hobbies", getRequest(req.hobbies));
            map.put("sexual_orientation", getRequest(req.sexual_orientation));
            map.put("aboutme", getRequest(req.aboutme));
            map.put("user_id", getRequest(req.user_id));
            map.put("eyecolor", getRequest(req.eyecolor));
            map.put("age", getRequest(req.age));
            map.put("physique", getRequest(req.physique));
            MultipartBody.Part image = appUtils.prepareFilePart(context, "profile_image", pic);
            Call<GetProfile> call = ApiHitter.ApiHitter().apiCreateProfile(image, headerMap, map);
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


    }


    public static void createProfileImages(Context context, List<Uri> upFileList, final ResponseListner listner) {
        Util util = new Util();
        List<MultipartBody.Part> parts = new ArrayList<>();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap = util.getApiHeaders(context);

        for (Uri imageUri : upFileList){
            parts.add(util.prepareFilePart(context,"images[]",imageUri));
        }
       // parts.add(util.prepareFilePart(context, "images[0]", upFileList));

        ApiHitter.ApiHitter().updateProfileaddImages(headerMap, parts).enqueue(new BaseCallBack() {
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

    static RequestBody getRequest(String text) {
        RequestBody body;
        if (text == null)
            body = RequestBody.create(MediaType.parse("text/plain"), "");
        else
            body = RequestBody.create(MediaType.parse("text/plain"), text);
        return body;
    }

    public static void appLinks(final ResponseListner listner) {


        ApiHitter.ApiHitter().appLinks().enqueue(new BaseCallBack() {
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


}
