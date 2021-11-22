//
//  Profile + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 29/05/19.
//  Copyright © 2019 Lavi Kumara. All rights reserved.
//

import Foundation

extension ProfileVC {
    
    /**
     Get Current user profile
    **/
    func GetProfile() {
        
        let param = ["user_id": self.userID]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.getProfile, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
           print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        let blockedBy = userData["blockedBy"] as? Int ?? 0
                    
                        if blockedBy == 0 {
                            self.getUserPost()//Get User Post
                        }
                        
                        if self.isMyProfile {
                            User(userData)
                            
                            let dataSave = NSKeyedArchiver.archivedData(withRootObject: userData)
                            UserDefaults.standard.set(dataSave, forKey: "userData")
                            UserDefaults.standard.synchronize()
                        }else {
                            self.userData = UserInfo(userData)
                            self.userInfo = self.userData
                            
                            
                        }
                        
                        let lookingFor = ["1" : "Man","2" : "Women","3" : "Transgender"]
                        
                        if let fQueAns = (response as! [String: Any])["f_que_ans"] as? [Int] {
                            var fQueAnsArr = [String]()
                            for key in fQueAns {
                                fQueAnsArr.append(lookingFor["\(key)"]!)
                            }
                            if fQueAnsArr.count > 0 {
                                let localizedArr       = fQueAnsArr.map({$0.localized()})
                                self.lookingLabel.text = localizedArr.joined(separator: ",")
                            }else {
                                self.lookingLabel.text = ""
                            }
                        }else {
                            self.lookingLabel.text = ""
                        }
                        self.SetData(details: response.value(forKey: "data") as! NSDictionary)
                        
                        self.manageHideShowData()
                        

                    }
                    
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (isYes) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: { (isYes) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    
    /**
     Get all posts added by current user
    **/
    
    func getUserPost() {
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.getMyPost + "?user_id=\(self.userID)&page=\(self.pageNo)", header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let dataDict = (response as! [String:Any])["data"] as? [String:Any] {
                        
                        self.isAPIEnable = false
                        self.lastPage = dataDict["last_page"] as! Int
                        
                        if let postArr = dataDict["data"] as? [[String:Any]] {
                            
                            let postWithImageArr = postArr.filter({($0["image"] as? String) != nil})
                            
                            self.postArr = self.postArr + postWithImageArr
                            self.postCollectionView.reloadData()
                        }
                    }else {
                        self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
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
    func likeOrDislikUserRequest(_ status: Int) {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id"  : self.userID,//Other User ID
            "status"      : status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.likeDislike, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if status == 1 {
                        Singleton.shared.isToReload = true
                        if let data = (response as! [String:Any])["data"] as? [String:Any]  {
                            
                            if self.isLikeAcceptBtn {
                                if self.userData.isNotificationEnable {
                                    self.createAlertPayload("\(User.shared.name!) has sent you a date request.", requestType: "Like", qbID: self.qbID, eventID: 0)
                                }
                                self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                                    Singleton.shared.isSwipeProfileReloadable = self.isFromSwipe
                                    self.navigationController?.popViewController(animated: false)
                                })
                            }else {
                                if self.userData.isNotificationEnable {
                                    self.createAlertPayload("\(User.shared.name!) accepted your date request.", requestType: "Like", qbID: self.qbID, eventID: 0)
                                }
                                let vc = homeStoryboard.instantiateViewController(withIdentifier: "LoveMatchViewController") as! LoveMatchViewController
                                vc.userInfo = UserInfo(data)
                                self.navigationController!.pushViewController(vc, animated: false)
                            }
                            
                           
                        }else {
                            if self.userData.isNotificationEnable {
                                self.createAlertPayload("\(User.shared.name!) has sent you a date request.", requestType: "Like", qbID: self.qbID, eventID: 0)
                            }
                            self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                                Singleton.shared.isSwipeProfileReloadable = self.isFromSwipe
                                self.navigationController?.popViewController(animated: false)
                            })
                        }
                    }else {
                        self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                            self.navigationController?.popViewController(animated: false)
                        })
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
     Accept or Reject received request sent to me by other users
     - parameter index: index number of request listed in table
     - parameter status: Status 0 for reject or 1 for accept request
     **/
    func acceptOrRejectRequest(_ status: String) {
        
        let eventKey = (self.eventType == "S_M_R") ? "chat_id" : "connect_id"
        
        let param = [
            eventKey: self.eventID,//Other User ID
            "status": status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        let requestURL = (self.eventType == "S_M_R") ? URLConstant.chatStatusUpdate : URLConstant.connectStatusUpdate
        
        WebAPI.requestToPostBodyWithHeader(requestURL, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    Singleton.shared.isToReload = true
                    if status == "A" {
                        if self.userData.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has accepted your \((self.eventType == "S_M_R") ? "chat" : "connect") request.", requestType: "Accept", qbID: self.qbID, eventID: 0)
                        }
                    }
                    self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                        Singleton.shared.isSwipeProfileReloadable = self.isFromSwipe
                        self.navigationController?.popViewController(animated: false)
                    })
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Send connect request to other user
     **/
    func connectUserRequest() {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": self.userID,//Other User ID
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.connectUser, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    Singleton.shared.isToReload = true
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        if self.userData.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has sent you a connect request.", requestType: "Connect", qbID: self.qbID, eventID: data["id"] as! Int)
                        }
                    }
                    self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                        Singleton.shared.isSwipeProfileReloadable = self.isFromSwipe
                        self.navigationController?.popViewController(animated: false)
                    })
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Send Message or Chat request to other users
     **/
    func sendChatRequest() {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": self.userID,//Other User ID
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.sendChatRequest, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    Singleton.shared.isToReload = true
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        if self.userData.isNotificationEnable {//If other user enalbe receive to notification.
                            self.createAlertPayload("\(User.shared.name!) has sent you a chat request.", requestType: "Chat", qbID: self.qbID, eventID: data["id"] as! Int)
                        }
                    }
                    self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                        Singleton.shared.isSwipeProfileReloadable = self.isFromSwipe
                        self.navigationController?.popViewController(animated: false)
                    })
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
