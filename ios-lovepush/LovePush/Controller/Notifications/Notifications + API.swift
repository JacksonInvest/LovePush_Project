//
//  Notifications + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

extension NotificationsVC {
    
    /**
     Clear all notification list
    **/
    
    func clearNotifications() {
        
        let param  = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.clearNotifications, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    self.notificationArr.removeAll()
                    self.noDataLbl.isHidden = false;
                    self.tableView.isHidden = true;
                    self.tableView.reloadData()
                }else {
                    self.noDataLbl.isHidden = false;
                    self.tableView.isHidden = true;
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.noDataLbl.isHidden = false;
                self.tableView.isHidden = true;
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
        Get all notification list
     **/
    func getNotifications() {
        
        let param = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.notifications, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let newList = (response as! [String:Any])["data"] as? [[String:Any]] {
                        
                        let notifCount = (response as! [String:Any])["noti_count"] as? Int ?? 0
                        
                        let tabBarVC = (self.tabBarController as! TabBarController)
                        
                        tabBarVC.setUnreadNotificationCount(notifCount)
                        
                        if(newList.count != 0) {
                            
                            self.notificationArr = self.sortNewestNotification(newList)
                            
                            self.tableView.isHidden = false;
                            self.noDataLbl.isHidden = true;
                            self.tableView.reloadData()
                        }else {
                            self.noDataLbl.isHidden = false;
                            self.tableView.isHidden = true;
                            self.tableView.reloadData()
                        }
                    }
                }else {
                    self.noDataLbl.isHidden = false;
                    self.tableView.isHidden = true;
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.noDataLbl.isHidden = false;
                self.tableView.isHidden = true;
//                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
    Accept reject Connect or Message Request
     - parameter status: 1 for accept or 0 for reject the request for user
     - parameter eventType: Event Type Message request or Connect request
     - parameter qbID: User Quickblox id
     - parameter eventID: Request ID
    **/
    func acceptOrRejectRequest(_ status: String, eventType: String, eventID: Int, qbID: String) {
        
        let eventKey = (eventType == "S_M_R") ? "chat_id" : "connect_id"
        
        let param = [
            eventKey: eventID,//Other User ID
            "status": status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))", "X-Requested-With": "XMLHttpRequest"    ]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        let requestURL = (eventType == "S_M_R") ? URLConstant.chatStatusUpdate : URLConstant.connectStatusUpdate
        
        WebAPI.requestToPostBodyWithHeader(requestURL, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if status == "A" {
                        if self.currentUser.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has accepted your \((eventType == "S_M_R") ? "chat" : "connect") request.", requestType: "Accept", qbID: qbID, eventID: 0)
                        }
                    }
                    
                    (self.tabBarController as! TabBarController).getNotifications { (newArr) in
                        if newArr.count == 0 {
                            self.noDataLbl.isHidden = false;
                            self.tableView.isHidden = true;
                            self.tableView.reloadData()
                        }else {
                            
                            self.notificationArr = newArr
                            self.tableView.isHidden = false;
                            self.noDataLbl.isHidden = true;
                            self.tableView.reloadData()
                        }
                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Like Dislike Request, When User A likes User B then a love request will sent A to B, B likes A then B will accept Love request Sent by A. So we have common API 'Like' to send or accept and reject Love request
     - parameter status: 1 for like or 0 for dislike the request for user
     - parameter eventType: Event Type Message request or Connect request
     - parameter qbID: User Quickblox id
     - parameter eventID: Request ID
     - parameter userID: User local server id
     **/
    func likeOrDislikUserRequest(_ status: Int, eventType: String, eventID: Int, qbID: String, userID: Int) {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": userID,//Other User ID
            "status": status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.likeDislike, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if status == 1 {
                        self.getMyRemainingPlanDetails()
                        if self.currentUser.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has accepted your date request.", requestType: "Like", qbID: qbID, eventID: 0)
                        }
                    }else {
                        
                    }
                    self.getNotifications()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Sorting Newest Notifications
     - parameter newestNotifications: All new notification list
     **/
    func sortNewestNotification(_ newestNotifications: [[String:Any]])-> [[String:Any]] {
        
        let sortNoties = newestNotifications.sorted { (noti1, noti2) -> Bool in
            
            let noti1CreatedAt = noti1["created_at"] as! String
            let noti2CreatedAt = noti2["created_at"] as! String
            
            let noti1Date  = self.getDateFormate(noti1CreatedAt)
            let noti2Date  = self.getDateFormate(noti2CreatedAt)
            
            return (noti1Date > noti2Date)
        }
        
        return sortNoties
    }
    
    /**
     Request to delete accepted request sent by me
     - parameter notificationID: Notification which you have to delete
     **/
    func deleteAcceptedNotifications(_ notificationID: Int, completion : @escaping(_ isReadSuccess: Bool)->Void) {
        
        let param = [
                     "user_id": User.shared.id!,
                     "notification_id":notificationID
            ] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        //        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.deleteNotification, param, header: header) { (response, isSuccess) in
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
}
