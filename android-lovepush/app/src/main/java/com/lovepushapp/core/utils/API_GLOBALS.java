package com.lovepushapp.core.utils;


public interface API_GLOBALS {
    String SECRET_KEY = "";
    // String BASE_URL = "https://www.love-push.com/api/";   //live
    // String BASE_URL = "http://14.141.175.109/lovepush/public/api/"; //demo

    String BASE_URL = "http://lovepush.alcax.com/api/"; //testing
    String BASE_URL_FOR_PREVIEW_ACTIVITY = "http://lovepush.alcax.com/";

    //quick blox live id
    String APP_ID = "77171";
    String AUTH_KEY = "hhPNP9nfTYOLfNt";
    String AUTH_SECRET = "fQFhcFxfkH2tTNG";
    String ACCOUNT_KEY = "ibS26PbG9Ao2fneNBUAd";


    //quick blox dev id
/*
    public static final String APP_ID = "79613";
    public static final String AUTH_KEY = "85QhQ9yuXevuA3S";
    public static final String AUTH_SECRET = "7yAayFYOVms4usG";
    public static final String ACCOUNT_KEY = "EUV77LPYzhVwZBZedzps";

 */

    //quick blox dev id

   /* public static final String APP_ID = "94378";
    public static final String AUTH_KEY = "efWPr8YMC5guQ5A";
    public static final String AUTH_SECRET = "M4DLS2PtbR2rQfQ";
    public static final String ACCOUNT_KEY = "3ZsG2-r8MhHnWG1jGDYr";*/


    interface API_NAME {
        String login = "login";
        String save_social = "addUserInfo";
        String social = "socailLogin";
        String isSocial = "isSocialUser";
        String instalogin = "instaLogin";
        String register = "register";
        String createProfile = "create-profile";
        String appLinks = "appLinks";
        String changePassword = "change-password";
        String getProfile = "get-profile";
        String personalityQuestions = "getPersonalityQuestions";
        String subscribe = "subscribe/paypal";
        String submitPersonalityTest = "userAnswerSave";
        String addPostAds = "addPostAds";
        String forgotPassword = "forgot-password";
        String verifyCode = "verify-code";
        // Old API  String sendVerificationCode = "send-verification-code";
        String sendVerificationCode = "reSendVerificationCode";
        String saveForgotPassword = "save-forgot-password";
        String logout = "logout";
        String recommendedProfile = "getRecommededProfile";
        String userLike = "user-like";
        String explore = "explores";
        String userDislike = "user-dislike";
        String like = "like";
        String connect = "sendConnectRequest";
        String chatRequest = "sendChatRequest";
        String accept_reject_ConnectRequest = "connectRequestStatusUpdate";
        String accept_reject_ChatRequest = "chatRequestStatusUpdate";
        String newest_MatchList = "myMatches";
        String notification = "notifications";
        String updateProfile = "update-profile";
        String updateProfileAdd = "profile-images/add";
        String updateProfileDelete = "profile-image/delete";
        String saveQbDialogId = "saveQbDialogId";
        String deleteMatchRecord = "deleteMatchRecord";
        String exploreRadar = "radars";
        String myPosts = "myPost";
        String plans = "plans";
        String cancelSubscription = "cancel/subscribe/paypal";
        String getSearchUsers = "searchUser";
        String addPost = "addPost";
        String getHomePosts = "getPosts";
        String hitHomePostLikeDislike = "postLikeUnlike";
        String getAdPosts = "getAllPosts";
        String getMyAdPosts = "myPostads";
        String deleteMyAdPost = "deletePost";
        String updatePostAds = "editPostAds";
        String getMyTeam = "referalTree";
        String deleteMyHomePost = "removePost";
        String reportImage = "reportImage";
        String updateHomePost = "editPost";
        String reportChat = "reportChat";
        String deleteChat = "deleteChat";

        String getSetting = "getSettings";
        String setNotification = "setNotification";

        String setProfileImage = "set_profile_image";

        //    /api/hideFromSearch
///api/setLikeRequest
///api/setChatRequest

        String setHideFromSearch = "hideFromSearch";
        String setLikeRequest = "setLikeRequest";
        String setChatRequest = "setChatRequest";

        String blockUser = "blockUser";
        String support = "support";
        String unmatch = "unmatch";
        String clearNotifications = "ClearNotifications";
        String readNotifications = "readNotifications";
        String deleteNotification = "deleteNotification";
        String deleteAccount = "deleteAccount";
        String deletePersonalityTest = "deleteUserTestRecord";
        String faq = "faq";
        String updateLocation = "updateLocation";
    }

    interface API_FIELDS {


    }

    interface RESPONSE_CODE {
        int SUCCESS = 200;
        int SESSION_EXPIRE = 203;
        int UnAUTHORIZED = 401;


        String SUCCESS_STR = SUCCESS + "";
        String SESSION_EXPIRE_STR = SESSION_EXPIRE + "";
        String UnAUTHORIZED_STR = UnAUTHORIZED + "";

    }
}
