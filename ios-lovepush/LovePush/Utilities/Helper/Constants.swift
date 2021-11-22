
import UIKit

let mainStoryboard = UIStoryboard(name: "Main", bundle: nil)
let homeStoryboard = UIStoryboard(name: "Home", bundle: nil)

let deviceUniqueIdentifier: String! = UIDevice.current.identifierForVendor?.uuidString
let successCode = 200
let logoutCode = 500
let sessionExpiredCode = 203
let errorCode = 400
let unauthorizedCode = 401

let appThemeColor_Red = UIColor(red: 184/255.0, green: 2/255.0, blue: 25/255.0, alpha: 1.0)
let appThemeColor_Blue = UIColor(red: 34/255.0, green: 122/255.0, blue: 207/255.0, alpha: 1.0)

let applicationDelegate = UIApplication.shared.delegate as! AppDelegate

// #MARK:- API Url's

let baseUrl = "http://14.141.175.109/lovepush/public/api/"
let profileImage_URL = "http://14.141.175.109/lovepush/public/profile/"

let login_ApiUrl = "login"
let SignUp_URL = "register"
let CreateProfile_URL = "create-profile"
let ChangePassword_URL = "change-password"
let PersonalityQuestions_URL = "personality-questions"
let SubmitAnswers_URL = "submit-personality-test"
let GetProfile_URL = "get-profile"
let GetNotifications_URL = "notifications"
let PostAd_URL = "add-post-ads"
let GetPosts_URL = "get-post-ads?post_type="
let ExploreUsers_URL = "explore-users"
let LikeUser_URL = "user-like"
let DislikeUser_URL = "user-dislike"
let ForgotPassword_URL = "forgot-password"
let VerifyCode_URL = "verify-code"
let ResetPassword_URL = "save-forgot-password"
let UpdateProfile_URL = "update-profile"
let UpdateSettings_URL = "update-setting"
let MatchedUsers_URL = "matched-users"
let RecommendedProfiles_URL = "recommended-profile"
let defaultProfileURL = "http://14.141.175.109/lovepush/public/images/system/default_user.png"

let Logout_URL = "logout"

// #MARK:- Reponse Keys

let statusKey = "status"
let messageKey = "message"

let deviceTypeAndroid = "1"
let deviceTypeIOS = "2"

let GooglePlaces_APIKey = "AIzaSyAzMX9ojQ_Ct2ww7B2wsDpn0T2F6aexqHs"

// #MARK:- Menu Screen Data
//                        "Live Streams", Temporary Remove From Menu it may be add at first index
var menuTitleArray = ["My Matches", "Ads", "Personal Matching",/*"Around",*/ "Affiliate Center", /*"Shop",*/ "Settings","User tips", "FAQ", "Logout"]
//                    UIImage(named: "liveStreamsIcon"), //////////////////////
var menuImagesArray = [UIImage(named: "my_matches"), UIImage(named: "adsIcon"), UIImage(named: "personalMatchingIcon"),/*UIImage(named: "around"),*/UIImage(named: "earnMoneyIcon"), /*UIImage(named: "shopIcon"),*/ UIImage(named: "settingsIcon"),UIImage(named: "user_tips_icon"), UIImage(named: "faqIcon"),  UIImage(named: "logoutIcon")]

//If you change setting array order you will have to change order of switch button action in setting or in setting table cell class
let settings  = [/*"Set Language",*/"Membership", "Notifications", /*"Receive Chat Request", */"Receive Love Request", "Hide Me From Search", "Change Password", "Community Guidlines", "T&C and Legacy", "Support", "Delete Account"]

enum SexualOrientation : Int {
    case Hetero = 1
    case Bi = 2
    case Homo = 3
}

enum AdType : Int {
    case Love = 1
    case Connect = 2
}

enum Gender : Int {
    case Male = 1
    case Female = 2
    case Trans = 3
}

enum PartnerPreferences : Int {
    case Men = 1
    case Women = 2
    case Everyone = 3
}

class Constants {
    
    class var QB_USERS_ENVIROMENT: String {
        
        #if DEBUG
        return "dev"
        #elseif QA
        return "qbqa"
        #else
        assert(false, "Not supported build configuration")
        return ""
        #endif
        
    }
}
