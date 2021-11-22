//
//  AppDelegate.swift
//  LovePush
//
//  Created by Jatinder Singh on 15/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import IQKeyboardManager
import GoogleMaps
import FBSDKCoreKit
import FBSDKLoginKit
import GoogleSignIn
import PushKit
import UserNotifications
import CoreLocation

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    var shouldRotate = true
    var fcmToken = ""
    
    
    //    public weak var session: QBRTCSession?
    lazy var voipRegistry: PKPushRegistry = {
        let voipRegistry = PKPushRegistry(queue: DispatchQueue.main)
        return voipRegistry
    }()
    public var callUUID: UUID?
    lazy var backgroundTask: UIBackgroundTaskIdentifier = {
        let backgroundTask = UIBackgroundTaskInvalid //UIBackgroundTaskIdentifier.invalid
        return backgroundTask
    }()
    
    var notificationDialogID : String?
    var notificationUser: UserInfo?
    var requestNotificationUser : [String:Any]?
    let locationManager = CLLocationManager()
    var isChatMessageNotification = false
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        Singleton.shared.currentAddress = ""
        Singleton.shared.currentLocation = nil
        
        UIApplication.shared.statusBarStyle = .lightContent
        UINavigationBar.appearance().barStyle = .blackOpaque
        
        IQKeyboardManager.shared().isEnabled = true
        IQKeyboardManager.shared().shouldResignOnTouchOutside = true;
        IQKeyboardManager.shared().keyboardDistanceFromTextField = 20.0;
        
        GMSServices.provideAPIKey(GooglePlaces_APIKey)
        
        //        GMSPlacesClient.provideAPIKey(GooglePlaces_APIKey)
        
        self.locationManagerSetup()
        
        self.noificationSetup(application)
        self.quickBloxSetup()
        
        self.defaultLogin()
        
        
        //VOIP Register
        self.voipRegistry.delegate = self
        self.voipRegistry.desiredPushTypes = Set<PKPushType>([.voIP])
        
        
        //        GIDSignIn.sharedInstance().clientID = FirebaseApp.app()!.options.clientID!
        GIDSignIn.sharedInstance().clientID = "78223436660-cc2510uiok186a9s7qtho2b5bp0n2u10.apps.googleusercontent.com"
        //        GIDSignIn.sharedInstance().delegate = self
        
        
        // You can skip this line if you have the latest version of the SDK installed
        //  FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        
        
        // Fetch data once an hour.
        UIApplication.shared.setMinimumBackgroundFetchInterval(3600)
        
        return true
    }
    
    func application(_ application: UIApplication,
                     performFetchWithCompletionHandler completionHandler:
        @escaping (UIBackgroundFetchResult) -> Void) {
        //        // Check for new data.
        //        if let newData = fetchUpdates() {
        //            addDataToFeed(newData: newData)
        //            completionHandler(.newData)
        //        }
        //        completionHandler(.noData)
    }
    
    
    func application(_ application: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        
        var handled: Bool = FBSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String, annotation: options[UIApplication.OpenURLOptionsKey.annotation])
        
        if !handled {
            handled = GIDSignIn.sharedInstance().handle(url as URL?,
                                                        sourceApplication: options[UIApplicationOpenURLOptionsKey.sourceApplication] as? String,
                                                        annotation: options[UIApplicationOpenURLOptionsKey.annotation])
        }
        
        return handled
    }
    
    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
        var _: [String: AnyObject] = [UIApplicationOpenURLOptionsKey.sourceApplication.rawValue: sourceApplication as AnyObject,
                                      UIApplicationOpenURLOptionsKey.annotation.rawValue: annotation as AnyObject]
        return GIDSignIn.sharedInstance().handle(url as URL,
                                                 sourceApplication: sourceApplication,
                                                 annotation: annotation)
    }
    
    
    func defaultLogin() {
        
//        if SubscriptionPlan.shared == nil {
//            self.defaultSubscription()
//        }
        
        if(UserDefaults.standard.bool(forKey: "isLogin") == true)
        {
            let data = UserDefaults.standard.object(forKey: "userData") as? Data
            if let aData = data {
                let result = NSKeyedUnarchiver.unarchiveObject(with: aData) as! [String : Any]
                
                
                User(result)
                
                if (result["is_profile_complete"] as! Bool == true) {
                    
                    
                    let state = UIApplication.shared.applicationState
                    //                if state == .background || state == .inactive {
                    if let qbUserData = UserDefaults.standard.object(forKey: "QBUser") as? Data {
                        if let unArchivedQBUser = NSKeyedUnarchiver.unarchiveObject(with: qbUserData) as? QBUUser {
                            Singleton.shared.quickbloxUserdata = unArchivedQBUser
                            self.connectWithQB()
                        }
                    }
                    
                    self.loginSetup(result)
                }
                
            }
        }
    }
    
    /**
     Default Subscription to avoid crash for old user,
     **/
    func defaultSubscription() {
        let planData : [String:Any] = [
            "ads" : "1 monthly",
            "id" : 1,
            "messages" : "1 daily",
            "name" : "Free",
            "plan_id" : 1,
            "price" : 0,
            "recommended_profiles" : "1 weekly",
            "see_love_requests" : "2 daily",
            "sending_requests" : "20 daily",
            "status" : 1,
            "type" : "FREE",
        ]
        
        SubscriptionPlan(planData)
    }
    
    /**
     Show Add Profile info with social media login
     - parameter userData:- Social user info
     **/
    func loginSetup(_ userData : [String:Any]) {
        
        User(userData)
        var nav = UINavigationController()
        let isEmailVerify = userData["email_verified_at"] as? String ?? ""
        let socialID = userData["social_id"] as? String
        if isEmailVerify == "" && socialID == nil {//Check Login without social media and email not verified
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "VerifyOtpID") as! VerifyOtpVC
            vc.detailDict = userData
            vc.isFromLogin = true;
            nav = UINavigationController(rootViewController: vc)
        }else if(userData["is_profile_complete"] as! Bool == false) {//Check all type login but profile completed or not
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "CreateProfileID") as! CreateProfileVC
            nav = UINavigationController(rootViewController: vc)
        } else {//Check all type login but profile completed or not
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID") as! TabBarController
            nav = UINavigationController(rootViewController: vc)
            
            DispatchQueue.global().sync {
                
                if self.notificationUser != nil && self.notificationDialogID != nil {
                    vc.selectedIndex = 2
                    QuickbloxHandling.shared.loadDialog(self.notificationDialogID!, complition: { (dialog) in
                        if let loadedDialog = dialog {
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                            vc.dialog       = loadedDialog
                            vc.user         = self.notificationUser
                            vc.deleteStatus = 0
                            //                            vc.matchID      =
                            self.notificationUser = nil
                            self.notificationDialogID = nil
                            nav.pushViewController(vc, animated: false)
                        }
                    })
                    
                }else if self.requestNotificationUser != nil {
                    vc.selectedIndex = 1
                    if let notificatioVc = vc.viewControllers![1] as? NotificationsVC {
                        notificatioVc.getNotifications()
                    }
                    self.requestNotificationUser = nil
                }
            }
        }
        nav.navigationBar.isHidden = true
        Singleton.shared.floatingNavigationController = nav
        self.window?.rootViewController = nav
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        Singleton.shared.currentAddress = ""
        Singleton.shared.currentLocation = nil
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
        if Singleton.shared.quickbloxUserdata != nil {
            self.connectWithQB()
            //            QBChat.instance.connect(with: qbUser, completion: nil)
        }
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        FBSDKAppEvents.activateApp()
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        Singleton.shared.currentAddress = ""
        Singleton.shared.currentLocation = nil
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
}

//MARK:- Notification Setup

extension AppDelegate: UNUserNotificationCenterDelegate {
    func noificationSetup(_ application: UIApplication) {
        
        
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
        } else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        
        application.registerForRemoteNotifications()
        
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
        print(userInfo)
    }
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        if let userInfo = response.notification.request.content.userInfo as? [String:Any] {
            print(userInfo)
            
            DispatchQueue.global().sync {
                if (userInfo["dialog_id"] as? String) != nil {
                    self.gotoChat(userInfo as! [String:Any])
                }else {
                    self.requestNotificationUser = userInfo as? [String : Any]
                    self.defaultLogin()
                }
            }
        }
        
        completionHandler()
    }
    
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        if let userInfo = notification.request.content.userInfo as? [String:Any] {
            print(userInfo)
            
            if Singleton.shared.navigationController?.topViewController is ChatViewController {
                print("Chat VC")
                let dialogID = userInfo["dialog_id"] as? String ?? ""
                if ServicesManager.instance().currentDialogID != dialogID {
                    completionHandler([.alert, .badge, .sound])
                }
            }else if Singleton.shared.navigationController?.topViewController is TabBarController {
                
                if let dialogID = userInfo["dialog_id"] as? String { //New Message
                    Singleton.shared.isMessageRead = false
                    if (Singleton.shared.navigationController?.topViewController as! TabBarController).selectedIndex == 2 {
                        if let messageVC = (Singleton.shared.navigationController?.topViewController as! TabBarController).viewControllers![2] as? MessagesVC {
                            
                            if ServicesManager.instance().currentDialogID != dialogID {
                                messageVC.getMatchedUser()
                                if ServicesManager.instance().isChatVC {//Chat Open for A to B, New msg from C then alert will show
                                    completionHandler([.alert, .badge, .sound])
                                }
                            }
                        }else {
                            if ServicesManager.instance().currentDialogID != dialogID {
                                completionHandler([.alert, .badge, .sound])
                            }
                        }
                    }else {
                        (Singleton.shared.navigationController?.topViewController as! TabBarController).quickbloxUnreadMessageCount()
                        completionHandler([.alert, .badge, .sound])
                    }
                }else {// New Request Notification
                    //                    if (Singleton.shared.navigationController?.topViewController as! TabBarController).selectedIndex == 1 {
                    if let notificationVC = (Singleton.shared.navigationController?.topViewController as! TabBarController).viewControllers![1] as? NotificationsVC {
                        notificationVC.getNotifications()
                    }else {
                        completionHandler([.alert, .badge, .sound])
                    }
                    //                    }else {
                    if let messageVC = (Singleton.shared.navigationController?.topViewController as! TabBarController).viewControllers![2] as? MessagesVC {
                        messageVC.getMatchedUser()
                    }
                    completionHandler([.alert, .badge, .sound])
                    //                    }
                }
            }else{
                completionHandler([.alert, .badge, .sound])
            }
        }
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        
        Singleton.shared.deviceToken = deviceToken
    }
    
    func gotoChat(_ userInfo: [String:Any]) {
        
        if userInfo["user_name"] != nil {
            var userDict = ["profile_image":"","name":"","quickblox_id":"","id":0,"event_type":"","chat_status":0] as [String : Any]
            
            self.quickBloxSetup()
            
            userDict["profile_image"]   = userInfo["user_image"] as! String
            userDict["name"]            = userInfo["user_name"] as! String
            userDict["quickblox_id"]    = userInfo["user_qb_id"] as! String
            userDict["id"]              = Int(userInfo["user_id"] as! String)!
            userDict["chat_status"]     = Int(userInfo["chat_status"] as! String)!
            userDict["event_type"]      = userInfo["event_type"] as! String
            
            let user = UserInfo(userDict)
            self.notificationUser = user
            self.isChatMessageNotification = false
            self.notificationDialogID = userInfo["dialog_id"] as? String ?? nil
        }else {
            self.isChatMessageNotification = true
        }
        
        self.defaultLogin()
    }
}



extension Data {
    var hexString: String {
        //        let hexString = self.map{ String(format: "%02.2hhx", $0) }
        //        return hexString.joined()
        
        let tokenParts = self.map { data -> String in
            return String(format: "%02.2hhx", data)
        }
        
        return tokenParts.joined()
    }
}
