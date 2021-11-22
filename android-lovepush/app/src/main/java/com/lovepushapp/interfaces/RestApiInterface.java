package com.lovepushapp.interfaces;


import com.google.gson.JsonObject;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.model.DeleteAccountModel;
import com.lovepushapp.model.FAQModel.GetFaqResponse;
import com.lovepushapp.model.PlanUrl;
import com.lovepushapp.model.homePosts.GetHomePostLikeDislike;
import com.lovepushapp.model.homePosts.addsmodel.GetPostsAdResponse;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.homePosts.postAdDeleteResponse;
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
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.RecmmendProflie_Package.RecommendedProfile;
import com.lovepushapp.model.response.SaveForgotPassWordResponse;
import com.lovepushapp.model.response.SendVerificationCodeResponse;
import com.lovepushapp.model.response.SettingResponse;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.model.response.SubmitPerQuesResponse;
import com.lovepushapp.model.response.UserDislikeResponse;
import com.lovepushapp.model.response.UserLikeResponse;
import com.lovepushapp.model.response.blockUserResponse;
import com.lovepushapp.model.response.clearNotificationResponse;
import com.lovepushapp.model.response.deleteChatResponse;
import com.lovepushapp.model.response.deleteNotificationResponse;
import com.lovepushapp.model.response.deleteTestResponse;
import com.lovepushapp.model.response.readNotificationResponse;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.unmatchUserResponse;
import com.lovepushapp.model.response.updateUserLocationResponse;
import com.lovepushapp.model.searchUsers.GetSearchUsersResponse;
import com.lovepushapp.referral.InviteLink;
import com.lovepushapp.referral.model.GetMyTeamResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RestApiInterface {


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.login)
    Call<GetProfile> apiLogin(
            @HeaderMap HashMap<String, String> header,
            @Field("email") String email,
            @Field("password") String password

    );

    @GET(API_GLOBALS.API_NAME.appLinks)
    Call<InviteLink> appLinks();

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.social)
    Call<SocialLoginModel> social_login(
            @HeaderMap HashMap<String, String> header,
            @Field("device_type") String type,
            @Field("email") String email, @Field("login_type") String logintype, @Field("social_id") String socialid, @Field("name") String name

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.isSocial)
    Call<SocialLoginModel> checkId(
            @HeaderMap HashMap<String, String> header,
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.instalogin)
    Call<SocialLoginModel> social_instal_login(
            @HeaderMap HashMap<String, String> header,
            @Field("device_type") String type
            , @Field("login_type") String logintype, @Field("social_id") String socialid, @Field("name") String name

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.save_social)
    Call<SocialLoginModel> apisavesocial(
            @HeaderMap HashMap<String, String> header,
            @FieldMap HashMap<String, String> option_id


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.register)
    Call<GetProfile> apiRegister(
            @HeaderMap HashMap<String, String> header,

            @Field("username") String username,
            @Field("name") String name,
            @Field("gender") String gender,

            @Field("email") String email,
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,

            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("device_type") String device_type,

            @Field("login_type") String login_type,
            @Field("referalBy") String referalBy

           /*   @Field("latitude") String latitude,
            @Field("address") String address,
            @Field("longitude") String longitude,
            @Field("country") String country,
            @Field("city") String city,*/
    );

    ;

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.createProfile)
    Call<GetProfile> apiCreateProfileWithpuImage(
            @HeaderMap HashMap<String, String> header,
            @Field("name") String name,
            @Field("love_push_intention") String love_push_intention,
            @Field("relationship") String relationship,
            @Field("language") String language,
            @Field("size") String size,
            @Field("haircolor") String haircolor,
            @Field("education") String education,
            @Field("job") String job,
            @Field("employer") String employer,
            @Field("hobbies") String hobbies,
            @Field("sexual_orientation") String sexual_orientation,
            @Field("aboutme") String aboutme,
            @Field("user_id") String user_id,
            @Field("eyecolor") String eyecolor,
            @Field("age") String age,
            @Field("physique") String physique

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.setProfileImage)
    Call<GetProfile> apiSetProfileImageByImageId(
            @HeaderMap HashMap<String, String> header, @Field("id") int image_id
    );

    @Multipart
    @POST(API_GLOBALS.API_NAME.createProfile)
    Call<GetProfile> apiCreateProfile(
            @Part MultipartBody.Part image,
            @HeaderMap HashMap<String, String> header,
            @PartMap() Map<String, RequestBody> partMap

    );


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.changePassword)
    Call<ChangePassResponse> apiChangePassword(
            @HeaderMap HashMap<String, String> header,
            @Field("new_password") String new_password,
            @Field("old_password") String old_password,
            @Field("new_password_confirmation") String new_password_confirmation

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.getProfile)
    Call<GetProfile> apiGetProfile(
            @HeaderMap HashMap<String, String> header, @Field("user_id") String id

    );


    @GET(API_GLOBALS.API_NAME.personalityQuestions)
    Call<PersonalitytestModel> apiPersonalityQuestions(
            @HeaderMap HashMap<String, String> header

    );

    @GET(API_GLOBALS.API_NAME.subscribe)
    Call<PlanUrl> subscribe(
            @Header("Authorization") String header, @Query("plan_id") String plan_id

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.submitPersonalityTest)
    Call<SubmitPerQuesResponse> apiSubmitPersonalityTest(
            @HeaderMap HashMap<String, String> header,
            @FieldMap HashMap<String, String> option_id


    );


    @Multipart
    @POST(API_GLOBALS.API_NAME.addPostAds)
    Call<PostAdsResponse> apiAddPostAds(
            @HeaderMap HashMap<String, String> header,
            @Part MultipartBody.Part image,
            @PartMap Map<String, RequestBody> params
    );


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.forgotPassword)
    Call<ForgotPassResponse> hit_forgot_password(
            @HeaderMap HashMap<String, String> header,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.verifyCode)
    Call<GetProfile> apiVerifyCode(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id,
            @Field("verification_code") String verification_code
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.sendVerificationCode)
    Call<SendVerificationCodeResponse> apiSendVerificationCode(
            @HeaderMap HashMap<String, String> header,
            /*  @Field("user_id") String user_id,*/
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.saveForgotPassword)
    Call<SaveForgotPassWordResponse> apiSaveForgotPassword(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.updateProfileDelete)
    Call<GetProfile> updaeprofiledelete(
            @HeaderMap HashMap<String, String> header,
            @Field("id") String user_id
    );


    @POST(API_GLOBALS.API_NAME.logout)
    Call<LogoutResponse> apiLogout(
            @HeaderMap HashMap<String, String> header


    );


    @GET(API_GLOBALS.API_NAME.explore)
    Call<Explore> apiExplore(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header, @Query("user_id") String receiver_id, @Query("latitude") String latitude, @Query("longitude") String longitude


    );

    //api/explores?page=1&user_id=2&min_age:18&max_age:100&gender:1
    @GET(API_GLOBALS.API_NAME.explore)
    Call<Explore> apiExploreFilter(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header, @Query("user_id") String user_id, @Query("min_age") String min_age, @Query("max_age") String max_age,
            @Query("gender") int gender, @Query("latitude") String latitude, @Query("longitude") String longitude

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.like)
    Call<LikeResponse> apiLikeDislike(
            @Field("to_user_id") int page,
            /*@HeaderMap HashMap<String, String> header,*/@Field("from_user_id") String receiver_id, @Field("status") String status


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.exploreRadar)
    Call<GetExploreRadarResponse> apiExploreRadar(
            @Field("page") int page,
            @HeaderMap HashMap<String, String> header, @Field("user_id") String user_id, @Field("latitude") String latitude, @Field("longitude") String longitude


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.exploreRadar)
    Call<GetExploreRadarResponse> apiExploreRadarFilter(
            @Field("page") int page,
            @HeaderMap HashMap<String, String> header, @Field("user_id") String user_id, @Query("min_age") String min_age, @Query("max_age") String max_age,
            @Query("gender") int gender, @Field("latitude") String latitude, @Field("longitude") String longitude


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.connect)
    Call<LikeResponse> apiConnect(
            @Field("to_user_id") int page,
            @HeaderMap HashMap<String, String> header, @Field("from_user_id") String receiver_id


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.accept_reject_ConnectRequest)
    Call<LikeResponse> apiAcceotConnectRequest(
            @Field("connect_id") String connect_id,
            @HeaderMap HashMap<String, String> header, @Field("status") String status


    );


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.accept_reject_ChatRequest)
    Call<LikeResponse> apiAcceptRejectChatRequest(
            @Field("chat_id") String connect_id,
            @HeaderMap HashMap<String, String> header, @Field("status") String status


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.newest_MatchList)
    Call<UserChatList> apiMyMatchList(
            @HeaderMap HashMap<String, String> header, @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.newest_MatchList)
    Call<UserChatList> apiMyMatchList1(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header, @Field("user_id") String user_id


    );


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.chatRequest)
    Call<LikeResponse> apiChatRequest(
            @Field("to_user_id") String user_id,
            @HeaderMap HashMap<String, String> header, @Field("from_user_id") String receiver_id


    );


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.notification)
    Call<NotificationResponse> apiNotification(
            @HeaderMap HashMap<String, String> header, @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.recommendedProfile)
    Call<RecommendedProfile> apiRecommendedProfile(
            @HeaderMap HashMap<String, String> header, @Field("user_id") String receiver_id
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.userLike)
    Call<UserLikeResponse> apiUserLike(
            @HeaderMap HashMap<String, String> header,
            @Field("receiver_id") String receiver_id
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.userDislike)
    Call<UserDislikeResponse> apiUserDislike(
            @HeaderMap HashMap<String, String> header,
            @Field("receiver_id") String receiver_id
    );


    @Multipart
    @POST(API_GLOBALS.API_NAME.updateProfile)
    Call<GetProfile> updateProfile(
            @HeaderMap HashMap<String, String> header,
            @Part MultipartBody.Part image,
            @PartMap Map<String, RequestBody> params
    );

    @Multipart
    @POST(API_GLOBALS.API_NAME.updateProfileAdd)
    Call<JsonObject> updateProfileaddImages(
            @HeaderMap HashMap<String, String> header,
            @Part List<MultipartBody.Part> image

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.saveQbDialogId)
    Call<SaveDialogResponse> saveQbDialogId(
            @HeaderMap HashMap<String, String> header,
            @Field("match_id") String match_id,
            @Field("qb_dialog_id") String qb_dialog_id

    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteMatchRecord)
    Call<DeleteMatchResponse> deleteMatchRecord(
            @HeaderMap HashMap<String, String> header,
            @Field("match_id") String match_id

    );

    // get My Posts in profile
    @GET(API_GLOBALS.API_NAME.myPosts)
    Call<getHomePostResponse> getMyPosts(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header, @Query("user_id") String receiver_id


    );


    @GET(API_GLOBALS.API_NAME.plans)
    Call<PlansEntity> getPlans();


    @GET(API_GLOBALS.API_NAME.cancelSubscription)
    Call<PlanUrl> cancelSubscription(@Header("Authorization") String header);

    // get users with search username and name
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.getSearchUsers)
    Call<GetSearchUsersResponse> getSearchUsers(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header,
            @Field("search_term") String match_id

    );

    //add dashboard post
    @Multipart
    @POST(API_GLOBALS.API_NAME.addPost)
    Call<PostAdsResponse> apiAddHomePost(
            @HeaderMap HashMap<String, String> header,
            @Part MultipartBody.Part image, @PartMap Map<String, RequestBody> params
    );

    //add dashboard post
    @Multipart
    @POST(API_GLOBALS.API_NAME.addPost)
    Call<PostAdsResponse> apiAddHomePostWithoutImage(
            @HeaderMap HashMap<String, String> header,
            @PartMap Map<String, RequestBody> params
    );

    // get home posts
    @GET(API_GLOBALS.API_NAME.getHomePosts)
    Call<getHomePostResponse> apiGetHomePosts(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header
    );

    // hit for like dilike home post
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.hitHomePostLikeDislike)
    Call<GetHomePostLikeDislike> apiHomePostLikeDislike(
            @HeaderMap HashMap<String, String> header,
            @Field("post_id") String post_id, @Field("user_id") String user_id, @Field("status") String status

    );

    // get Ad posts
    @GET(API_GLOBALS.API_NAME.getAdPosts)
    Call<GetPostsAdResponse> apiGetAdPosts(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header, @Query("user_id") String user_id
            , @Query("latitude") String latitude, @Query("longitude") String longitude

    );


    // get my Ad posts
    @GET(API_GLOBALS.API_NAME.getMyAdPosts)
    Call<GetPostsAdResponse> apiGetMyAdPosts(
            @Query("page") int page,
            @HeaderMap HashMap<String, String> header
            , @Query("latitude") String latitude, @Query("longitude") String longitude

    );

    // hit for delete Ad post
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteMyAdPost)
    Call<postAdDeleteResponse> apiDeleteMyAdPost(
            @HeaderMap HashMap<String, String> header,
            @Field("id") int post_id

    );

    // update post ad
    @Multipart
    @POST(API_GLOBALS.API_NAME.updatePostAds)
    Call<PostAdsResponse> apiUpdatePostAds(
            @HeaderMap HashMap<String, String> header,
            @Part MultipartBody.Part image,
            @PartMap Map<String, RequestBody> params
    );

    // get my Ad posts
    @GET(API_GLOBALS.API_NAME.getMyTeam)
    Call<GetMyTeamResponse> apiGetMyTeam(
            /* @Query("page") int page,*/
            @HeaderMap HashMap<String, String> header

    );

    // hit for delete Home post
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteMyHomePost)
    Call<postAdDeleteResponse> apiDeleteMyHomePost(
            @HeaderMap HashMap<String, String> header,
            @Field("id") int post_id

    );

    // hit for delete Home post
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.reportImage)
    Call<CommonResponse> reportImage(
            @HeaderMap HashMap<String, String> header,
            @Field("image_id") int imageId,
            @Field("type") String type

    );

    // update post Home
    @Multipart
    @POST(API_GLOBALS.API_NAME.updateHomePost)
    Call<PostAdsResponse> apiUpdateHomePost(
            @HeaderMap HashMap<String, String> header,
            @Part MultipartBody.Part image,
            @PartMap Map<String, RequestBody> params
    );

    // hit for report chat
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.reportChat)
    Call<CommonResponse> hitReportChat(
            @HeaderMap HashMap<String, String> header,
            @Field("reported_user_id") String reported_user_id, @Field("message") String des, @Field("user_id") String user_id);

    // hit for delete chat (1 - delete, 0 - restore)
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteChat)
    Call<deleteChatResponse> deleteChat(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("match_id") String match_id, @Field("user_id") String user_id);


    @GET(API_GLOBALS.API_NAME.getSetting)
    Call<SettingResponse> apiGetSettings(
            /* @Query("page") int page,*/
            @HeaderMap HashMap<String, String> header, @Query("user_id") String user_id

    );

    /*set notification to update notification setting*/
    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.setNotification)
    Call<SettingResponse> setNotification(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("id") String id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.setChatRequest)
    Call<SettingResponse> setChatRequest(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("id") String id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.setLikeRequest)
    Call<SettingResponse> setLikeRequest(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("id") String id);


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.setHideFromSearch)
    Call<SettingResponse> setHideFromHome(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("id") String id);


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.blockUser)
    Call<blockUserResponse> hitBlockUser(
            @HeaderMap HashMap<String, String> header,
            @Field("status") String status, @Field("blocked_user_id") String blocked_user_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.support)
    Call<blockUserResponse> hitSupportUser(
            @HeaderMap HashMap<String, String> header,
            @Field("name") String name, @Field("subject") String subject, @Field("message") String message);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.unmatch)
    Call<unmatchUserResponse> hitUnMatchUser(
            @HeaderMap HashMap<String, String> header,
            @Field("match_id") String match_id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.clearNotifications)
    Call<clearNotificationResponse> hitClearNotifications(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.readNotifications)
    Call<readNotificationResponse> hitReadNotifications(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteNotification)
    Call<deleteNotificationResponse> hitDeleteNotification(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id, @Field("notification_id") String notification_id);


    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deleteAccount)
    Call<DeleteAccountModel> hitDeleteAccount(
            @HeaderMap HashMap<String, String> header, @FieldMap HashMap<String, String> body
    );

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.deletePersonalityTest)
    Call<deleteTestResponse> hitDeletePersonalityTest(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id);

    @GET(API_GLOBALS.API_NAME.faq)
    Call<GetFaqResponse> hitFAQ(
            @HeaderMap HashMap<String, String> header, @Query("lang_id") String lang_id);

    @FormUrlEncoded
    @POST(API_GLOBALS.API_NAME.updateLocation)
    Call<updateUserLocationResponse> apiUpdateLocation(
            @HeaderMap HashMap<String, String> header,
            @Field("user_id") String user_id, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("address") String address);

    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<Map> getInstaToken(@FieldMap HashMap<String, String> hashMap);

    @GET("me?")
    Call<Map> getUserName(@QueryMap HashMap<String, String> data);
}