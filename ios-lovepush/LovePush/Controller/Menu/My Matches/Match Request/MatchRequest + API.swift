//
//  MatchRequest + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

extension MatchRequestViewController {
    func getNotificationList() {
        
        let param = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.notifications, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let newList = (response as! [String:Any])["data"] as? [[String:Any]] {
                        print(newList)
                        self.myRequestList = newList.filter({($0["event_type"] as! String == "S_L_R") || ($0["event_type"] as! String == "S_C_R")})
                        if self.myRequestList.count > 0 {
                             self.requestListTableView.reloadData()
                        }else {
                            self.showAlert(title: "Love Push", message: "No Data Found", completion: { (isYes) in
                                self.navigationController?.popViewController(animated: true)
                            })
                        }
                    }
                    
                }else {
                    
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (isYEs) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: { (isYEs) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    
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
                    
                    self.getNotificationList()
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
                        if self.currentUser.isNotificationEnable {
                            self.createAlertPayload("\(User.shared.name!) has accepted your date request.", requestType: "Like", qbID: qbID, eventID: 0)
                        }
                        
                    }else {
                        
                    }
                    self.getNotificationList()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
}
