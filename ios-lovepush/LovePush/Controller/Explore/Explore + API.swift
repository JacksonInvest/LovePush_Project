//
//  Exploer + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 29/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

extension ExploreVC {
    
    //MARK:- Functions
    func getExploreList() { //Please Change URL
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
       
        WebAPI.requestToGetWithHeader(URLConstant.explores + "?page=\(self.pageNo)&user_id=\(User.shared.id!)&latitude=\(Singleton.shared.currentLocation!.latitude)&longitude=\(Singleton.shared.currentLocation!.longitude)", header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            self.noDataLbl.isHidden = self.isSwipeActive
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        self.totalUserCountFetch = data["to"] as? Int ?? 0
                        self.totalCountAvailable = data["total"] as? Int ?? 0
                        
                        if let userArr = data["data"] as? [[String:Any]] {
                            self.swipeUserArray = userArr
                            if self.swipeUserArray.count > 0 {
                                self.isAlreadyHitSwipeAPI = false
                                self.unHideViews()
                                self.noExploreUserLbl.isHidden = false
                                self.noDataLbl.isHidden = true
                                self.kolodaView.reloadData()
                                self.radarCollectionView.reloadData()
                            }else {
                                self.swipeUserArray.removeAll()
                                self.hideViews()
                            }
                        }else {
                            self.swipeUserArray.removeAll()
                            self.hideViews()
                        }
                    }else {
                        self.swipeUserArray.removeAll()
                        self.hideViews()
                    }
                    
                }else {
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    if self.swipeUserArray.count == 0 {
                        self.hideViews()
                    }
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                if self.swipeUserArray.count == 0 {
                    self.hideViews()
                }
            }
        }
    }
    
    func getFilteredData(minAge: Int, maxAge: Int, gender: Int, lat: Double, long: Double) { //Please Change URL
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.explores + "?user_id=\(User.shared.id!)&min_age=\(minAge)&max_age=\(maxAge)&gender=\(gender)&latitude=\(lat)&longitude=\(long)", header: header as [String : AnyObject]) { (response, isSuccess) in
//            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let data = (response as! [String: Any])["data"] as? [String:Any] {
                        self.totalUserCountFetch = data["to"] as? Int ?? 0
                        self.totalCountAvailable = data["total"] as? Int ?? 0
                        if let userArr = data["data"] as? [[String:Any]] {
                            self.swipeUserArray = userArr
                            
                            if self.isSwipeActive {
                                if self.swipeUserArray.count > 0 {
                                    self.unHideViews()
                                    self.noDataLbl.isHidden = true
                                    self.noExploreUserLbl.isHidden = (self.swipeUserArray.count > 0)
                                    self.kolodaView.reloadData()
                                }else {
                                    self.hideViews()
                                }
                               
//                            }else {
//                                self.noExploreUserLbl.isHidden = true
//                                self.noDataLbl.isHidden = self.radarUserArray.count > 0
                            }
                        }
                        self.radarCollectionView.reloadData()
                    }
                }else {
                    if self.swipeUserArray.count == 0  && self.isSwipeActive{
                         self.hideViews()
                        self.showAlert(title: "Love Push", message: "No User Found", completion: nil)
                    }
                    self.radarCollectionView.reloadData()
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
    func likeOrDislikUserRequest(_ status: Int, userID: Int) {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": userID,//Other User ID
            "status": status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.likeDislike, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
//            User has disabled date request
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    
                    if status == 1 {
                        
                        self.getMyRemainingPlanDetails()
                        
                        if let data = (response as! [String:Any])["data"] as? [String:Any]  {//Second Time Like
                            
                            let userData = UserInfo(data)
                            
                            if userData.isNotificationEnable {
                                 self.createAlertPayload("\(User.shared.name!) accepted your date request.", requestType: "Like", qbID: self.currentQbID, eventID: 0)
                            }
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "LoveMatchViewController") as! LoveMatchViewController
                            vc.userInfo = userData
                            self.navigationController!.pushViewController(vc, animated: false)
                        }else {// Like First Time
                            if self.currentUser.isNotificationEnable {
                                self.createAlertPayload("\(User.shared.name!) has sent you a date request.", requestType: "Like", qbID: self.currentQbID, eventID: 0)
                            }
                        }
                    }
                }else {
                    let message = (response as! [String:Any])["message"] as! String

                    if (message == "User has disabled date request." ) && status == 1{
                        self.showAlert(title: "Love Push", message: message, completion: nil)
                    }
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    func sendChatRequest(_ userID: Int, completion: @escaping(_ isSucces: Bool)->Void) {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": userID,
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.sendChatRequest, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    completion(true)
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        self.getMyRemainingPlanDetails()
                        if self.currentUser.isNotificationEnable {
                             self.createAlertPayload("\(User.shared.name!) has sent you a chat request.", requestType: "Chat", qbID: self.currentQbID, eventID: data["id"] as! Int)
                        }
                       
                    }
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (isYes) in
//                        completion(true)
//                    })
                }else {
                    completion(false)
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                completion(false)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    func connectUserRequest(_ userID: Int) {
        
        let param = [
            "from_user_id": User.shared.id!,
            "to_user_id": userID,//Other User ID
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.connectUser, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        self.getMyRemainingPlanDetails()
                        if self.currentUser.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has sent you a connect request.", requestType: "Connect", qbID: self.currentQbID, eventID: data["id"] as! Int)
                        }
                    }
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }else {
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    func getRadarList() { //Please Change URL
        
        let param  = [
                        "user_id":User.shared.id!,
                        "latitude":Singleton.shared.currentLocation!.latitude,
                        "longitude":Singleton.shared.currentLocation!.longitude
            ] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        let url = URLConstant.radars + "?page=\(self.radarCurrentPageNo)&user_id=\(User.shared.id!)&latitude=\(Singleton.shared.currentLocation!.latitude)&longitude=\(Singleton.shared.currentLocation!.longitude)"
        
        WebAPI.requestToPOSTWithHeader(url, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)

            self.isScrollingEnabled = false
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        if let radarArr = data["data"] as? [[String:Any]] {
                            self.radarCurrentPageNo = data["current_page"] as! Int
                            self.radarLastPageNo = data["last_page"] as! Int
                            self.radarUserArray = self.radarUserArray + radarArr
                        }
                    }
                    if self.isSwipeActive && self.swipeUserArray.count > 0 {
                        self.unHideViews()
                    }
                    self.noDataLbl.isHidden = (self.radarUserArray.count > 0) && !self.isSwipeActive
                    self.radarCollectionView.reloadData()
                }else {
                    self.radarCurrentPageNo -= 1
                    self.radarCollectionView.reloadData()
                }
            }else {
                self.radarCurrentPageNo -= 1
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                 self.radarCollectionView.reloadData()
            }
        }
    }
    
    func getRadarFilterList(_ gender: Int, minAge: Int, maxAge: Int, lat: Double, long: Double) { //Please Change URL
        
        let param  = [
                        "user_id":User.shared.id!,
                        "latitude":lat,//Singleton.shared.currentLocation!.latitude,
                        "longitude":long,//Singleton.shared.currentLocation!.longitude,
                        "min_age" : minAge,
                        "max_age" : maxAge,
                        "gender"  : gender
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        let url = URLConstant.radars + "?page=\(self.radarCurrentPageNo)&user_id=\(User.shared.id!)&min_age=\(minAge)&max_age=\(maxAge)&gender=\(gender)&latitude=\(lat)&longitude=\(long)"
        
        WebAPI.requestToPOSTWithHeader(url, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            self.isScrollingEnabled = false
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        if let radarArr = data["data"] as? [[String:Any]] {
                            DispatchQueue.global().sync {
                               
//                                self.noExploreUserLbl.isHidden = true
                                self.radarCurrentPageNo = data["current_page"] as! Int
                                self.radarLastPageNo = data["last_page"] as! Int
                                self.radarUserArray = self.radarUserArray + radarArr
                                
                                if !self.isSwipeActive {
                                    if self.swipeUserArray.count > 0 {
                                        self.unHideViews()
                                    }
                                    self.noExploreUserLbl.isHidden = true
                                    self.noDataLbl.isHidden = self.radarUserArray.count > 0
                                }
                                if self.swipeUserArray.count > 0 {
                                     self.kolodaView.reloadData()
                                }
                                self.radarCollectionView.reloadData()
                            }
                        }
                    }
                }else {
                    self.radarCurrentPageNo -= 1
                    if self.radarUserArray.count == 0 && !self.isSwipeActive {
                        self.noDataLbl.isHidden = false
                        self.showAlert(title: "Love Push", message: "No User Found", completion: nil)
                    }
                }
            }else {
                self.radarCurrentPageNo -= 1
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
