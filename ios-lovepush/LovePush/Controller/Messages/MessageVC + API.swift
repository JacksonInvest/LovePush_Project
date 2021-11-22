//
//  MessageVC + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 27/06/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

extension MessagesVC {
    
    /**
     Request to get list of my matched user
     **/
    func getMatchedUser() {
        
        let param  = ["user_id" : User.shared.id!]
        let header = ["locale"  : Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.myMatchedUser + "?page=\(self.pageNo)", param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            self.isAPIEnable = false
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    
                    if let outerDataArr = (response as! [String:Any])["data"] as? [[String:Any]] {
                        
                        DispatchQueue.global().sync {
                            //if qb_chat_dialog of user info in list null means new mathed user
                            let matchedArr = outerDataArr.filter({$0["qb_chat_dialog"] is NSNull})
                            self.matchedList = self.sortNewestMatches(matchedArr)//Show newest match first in horizontal list
                            
                            let  dialogMatchedArr = outerDataArr.filter({!($0["qb_chat_dialog"] is NSNull) })//if qb_chat_dialog of user info in list not null means chat already started
                            self.chatList = dialogMatchedArr
                            
                            self.collectionView.reloadData()
                        }
                        
                        //                        if self.reqeustCount == 0 {
                        //                            self.requestButton.isHidden = true
                        //                            self.requestBtnHeight.constant = 0
                        //                        }
                        self.getDialogList()
                    }
                    
                    //                    if let requests = (response as! [String:Any])["requests"] as? Int {
                    //                        self.requestBtnHeight.constant = (requests == 0) ? 0 : 30
                    //                        self.requestButton.isHidden    = (requests == 0)
                    //                        self.requestButton.setTitle("\(requests) " + "Request".localized(), for: .normal)
                    //                    }
                    
                }else {
                    //                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Request to remove match with user after that user can't see each other in app
     - parameter id: Current user id
     **/
    func removeMatchWithUser(_ id: String,completion: @escaping(_ isSuccess: Bool)-> Void) {
        let param = ["match_id": id] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.unmatchUser, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    //                    self.getMatchedUser()
                    completion(true)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(false)
                }
            }else {
                completion(false)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Sorting for newest match first in horizontal List
     - parameter newestMathces: All Newest suffled matched list to be sorted
     **/
    func sortNewestMatches(_ newestMatches: [[String:Any]])-> [[String:Any]] {
        let sortMatches = newestMatches.sorted { (match1, match2) -> Bool in
            
            let match1CreatedAt = match1["created_at"] as! String
            let match2CreatedAt = match2["created_at"] as! String
            
            let match1Date  = self.getDateFormate(match1CreatedAt)
            let match2Date  = self.getDateFormate(match2CreatedAt)
            
            return (match1Date > match2Date)
        }
        
        return sortMatches
    }
}
