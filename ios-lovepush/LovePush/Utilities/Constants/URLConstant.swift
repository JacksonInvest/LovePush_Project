//
//  Constant.swift
//  ONA
//
//  Created by Lavi Kumar on 27/03/19.
//  Copyright © 2019 XiCom Technologies. All rights reserved.
//

import Foundation

class URLConstant {
    
    static let baseURL              = "http://lovepush.alcax.com/api/"
//    static let baseURL                = "http://lovepush.alcax.com/api/"
    static let imageURL             = "http://14.141.175.109/lovepush/public/profile/"
//    static let legacy               = "http://14.141.175.109/lovepush/public/legacy_ios"
//    static let communityGuidlines   = "http://14.141.175.109/lovepush/public/community_guidelines_ios"
//    static let userTips             = "http://14.141.175.109/lovepush/public/UserTips.pdf"
    
    
//    static let baseURL              = "https://www.love-push.com/api/"
//    static let imageURL             = "https://www.love-push.com/profile"
    
    static let legacy               = "https://www.love-push.com/legacy_ios"

    static let communityGuidlines   = "https://www.love-push.com/community_guidelines_ios"
    static let userTips             = "http://lovepush.alcax.com/UserTips.pdf"
    static let tipsAndMedia         = "http://lovepush.alcax.com/AffiliateInfo_android.pdf"
    
    static let getProfile           = "get-profile"
    static let register             = "register"
    static let sendOTP              = "reSendVerificationCode"
    static let verifyOTP            = "verify-code"
    static let socialLogin          = "socailLogin"
    static let instagramLogin       = "instaLogin"
    static let login                = "login"
    static let logout               = "logout"
    static let createProfile        = "create-profile"
    static let changePassword       = "change-password"
    static let updateProfile        = "update-profile"
    static let addImages            = "profile-images/add"
    static let removeImage          = "profile-image/delete"
    static let personalityTest      = "getPersonalityQuestions"
    static let deleteUserTestRecord = "deleteUserTestRecord"
    static let userAnswerSave       = "userAnswerSave"
    static let addUserInfo          = "addUserInfo"
    static let recommendProfile     = "getRecommededProfile"
    static let likeDislike          = "like"
    static let connectUser          = "sendConnectRequest"
    static let connectStatusUpdate  = "connectRequestStatusUpdate"
    static let chatStatusUpdate     = "chatRequestStatusUpdate"
    static let myMatchedUser        = "myMatches"
    static let chatList             = "chatList"
    static let explores             = "explores"
    static let radars               = "radars"
    static let exploresFilter       = "explores"
    static let notifications        = "notifications"
    static let readNotifications    = "readNotifications"
    static let deleteNotification   = "deleteNotification"
    static let sendChatRequest      = "sendChatRequest"
    static let saveDialog           = "saveQbDialogId"
    static let deleteDialog         = "deleteQbDialogId"
    static let updateChatStatus     = "updateChatStatus"
    static let hideFromSearch       = "hideFromSearch"
    static let setLikeRequest       = "setLikeRequest"
    static let setChatRequest       = "setChatRequest"
    static let setNotification      = "setNotification"
    static let getSettings          = "getSettings"
    static let deleteMatchUser      = "deleteMatchRecord"
    static let unmatchUser          = "unmatch"
    static let deleteChat           = "deleteChat"
    static let blockUser            = "blockUser"
    static let updateLocation       = "updateLocation"
    static let reportChat           = "reportChat"
    static let blockStatus          = "checkBlock"
    static let reportImage          = "reportImage"
    
    static let addPostAds           = "addPostAds"
    static let getMyPostAds         = "myPostads"
    static let getAllPostsAds       = "getAllPosts"
    static let deletePostAd         = "deletePost"
    static let editPostAds          = "editPostAds"
    static let updatePlanData       = "update_plan_data"
    
    static let forgotPassword       = "forgot-password"
    
    //Home Post
    static let addPost              = "addPost"
    static let getMyPost            = "myPost"
    static let getAllPosts          = "getPosts"
    static let deletePost           = "removePost"
    static let editPost             = "editPost"
    static let likeUnlikePost       = "postLikeUnlike"
    static let searchUser           = "searchUser"
    static let geMyTeam             = "referalTree"
    static let support              = "support"
    static let deleteAccount        = "deleteAccount"
    static let clearNotifications   = "ClearNotifications"
    static let getFAQ               = "faq"
    static let getAppLinks          = "appLinks"
    static let isSocialLoggedIn     = "isSocialUser"
    static let myPlanDetails        = "my_plan_details"
    
    //Plans
    static let getPlan              = "plans"
    static let subscribePlan        = "subscribe/paypal?"
    static let cancelSubscription   = "cancel/subscribe/paypal"
    
    //Live Streaming
    static let getBroadcastList     = "https://api.bambuser.com/broadcasts"
}
