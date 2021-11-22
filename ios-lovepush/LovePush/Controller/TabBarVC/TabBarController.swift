//
//  TabBarController.swift
//  LovePush
//
//  Created by Lavi Kumar on 28/06/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import CoreLocation

class TabBarController: UITabBarController{

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        // Do any additional setup after loading the view.
        
        self.checkUsersLocationServicesAuthorization()
        
        self.getMyRemainingPlanDetails()
        
        UIApplication.shared.statusBarStyle = .lightContent
        
        //Get status of Current user blocked by admin
        
        if User.shared != nil {
           self.quickbloxUnreadMessageCount()
        }
        
        self.getBlockStatus { (isBlocked) in
            print(isBlocked)
            if isBlocked {
                self.showAlert(title: "Love Push", message: "You are blocked by admin, You can't login this app.", completion: { (isYes) in
                    let vc = mainStoryboard.instantiateViewController(withIdentifier: "Login") as! LoginViewController
                    self.navigationController?.setViewControllers([vc], animated: true)
                })
            }else {
                self.getNotifications { (newArr) in
                    Singleton.shared.notificationArr = newArr
                }
//                self.quickbloxUnreadMessageCount()
            }
        }
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    /**
     Get total new unread messages count and show to message tabbar button icon badge
     **/
    func quickbloxUnreadMessageCount() {
        
        QuickbloxHandling.shared.getDialogList(completion: { (dialogs) in
            let dialogIDs = dialogs.map({"\($0.id!)"})
            QBRequest.totalUnreadMessageCountForDialogs(withIDs: Set(dialogIDs), successBlock: { (response, count, result) in
                print(count)
                Singleton.shared.unreadMessageCount = Int(count)
                if !Singleton.shared.isMessageRead {
                    self.setUnreadChatCount()
                }
            }, errorBlock: { (response) in
                print(response)
            })
        })
        
        Singleton.shared.navigationController = self.navigationController
    }
    
    /**
     Set unread notification count on notification tab bar button
     **/
    func setUnreadNotificationCount(_ unreadCount :Int) {
        let num = unreadCount
        print(num)
        let tabArray = self.tabBar.items!
        let chatTab = tabArray[1]
        if num == 0 || self.selectedIndex == 1 {
            chatTab.badgeValue = nil
        }else{
            chatTab.badgeValue = "\(num)"
        }
        chatTab.badgeColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
    }
    
    func setUnreadChatCount() {
        let num = Singleton.shared.unreadMessageCount
        print(num)
        let tabArray = self.tabBar.items!
        let chatTab = tabArray[2]
        if num == 0 || self.selectedIndex == 2 {
            chatTab.badgeValue = nil
        }else{
            chatTab.badgeValue = "\(num)"
        }
        chatTab.badgeColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
    }
    
    //MARK:- Tab Bar Delegate
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        
        Singleton.shared.isMessageRead = false
        
        if item == self.tabBar.items![1]{
            self.setUnreadNotificationCount(0)
        }else if item == self.tabBar.items![2] {
            Singleton.shared.isMessageRead = true
            Singleton.shared.unreadMessageCount = 0
            self.setUnreadChatCount()
        }else if item == self.tabBar.items![3]{
            
            //Release Explore Filter Data
            Singleton.shared.isExploreFilter = false
            Singleton.shared.findAddress  = Singleton.shared.currentAddress
            Singleton.shared.findLocation = Singleton.shared.currentLocation
            Singleton.shared.minAge       = 18
            Singleton.shared.maxAge       = 100
            Singleton.shared.filterGender = 0
            
            self.checkUsersLocationServicesAuthorization()
        }
    }
    
    //MARK:- Current Location Handling
    /**
     Check My Current fetch status Allow
     **/
    func checkUsersLocationServicesAuthorization(){
        /// Check if user has authorized Total Plus to use Location Services
        if CLLocationManager.locationServicesEnabled() {
            switch CLLocationManager.authorizationStatus() {
                
            case .notDetermined:
                // Request when-in-use authorization initially
                // This is the first and the ONLY time you will be able to ask the user for permission
                self.gotoSetting()
                break
                
            case .restricted, .denied:
                // Disable location features
                self.gotoSetting()
                break
                
            case .authorizedWhenInUse, .authorizedAlways:
                // Enable features that require location services here.
                print("Full Access")
                break
            }
        }
    }
    
    /**
     Go to setting to enable current location update
     **/
    func gotoSetting() {
        let alert = UIAlertController(title: "Allow Location Access", message: "App needs to access your location. Turn on Location Services in your device settings.", preferredStyle: UIAlertController.Style.alert)
        
        // Button to Open Settings
        alert.addAction(UIAlertAction(title: "Settings", style: UIAlertAction.Style.default, handler: { action in
            guard let settingsUrl = URL(string: UIApplicationOpenSettingsURLString) else {
                return
            }
            if UIApplication.shared.canOpenURL(settingsUrl) {
                UIApplication.shared.open(settingsUrl, completionHandler: { (success) in
                    print("Settings opened: \(success)")
                })
            }
        }))
        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    
    //MARK:- Notification
    /**
     Request to get all new notifications
     **/
    func getNotifications(completion : @escaping(_ newArray : [[String:Any]])->Void ) {
        
        let param = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.notifications, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let newList = (response as! [String:Any])["data"] as? [[String:Any]] {
                        if(newList.count != 0) {
                            Singleton.shared.notificationArr = newList
                            completion(newList)
                           
                           let notifCount = (response as! [String:Any])["noti_count"] as? Int ?? 0
                            
                           self.setUnreadNotificationCount(notifCount)
                        }else {
                            completion([])
                        }
                    }else {
                        completion([])
                    }
                    
                }else {
                    completion([])
                }
            }else {
                completion([])
            }
        }
    }
    
    /**
     Request to update status for seen new notifications
     **/
    func readNewNotifications(completion : @escaping(_ isReadSuccess: Bool)->Void) {
        
        let param = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        //        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.readNotifications, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    completion(true)
                }else {
                    completion(false)
                }
            }else {
                completion(false)
            }
        }
    }
    
  
    
    /**
     Request to get status of Current user blocked by admin
     **/
    func getBlockStatus(completion : @escaping(_ isBlocked : Bool)->Void) {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.blockStatus, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let isBlocked = (response as! [String:Any])["isBlocked"] as? String {
                        completion((isBlocked == "1"))
                    }else {
                        completion(false)
                    }
                }else {
                    completion(false)
                }
            }else {
                completion(false)
            }
        }
    }
}
