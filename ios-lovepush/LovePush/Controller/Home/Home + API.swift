//
//  Home + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation


extension HomeVC {
    
    /**
     Request to get list of all posts added by me and my match
     - parameter url: API URL to get list with pagination
     **/
    func requestToGetPost(_ url: String ) {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(url + "?page=\(self.pageNo)", header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            self.isScrollingEnabled = false
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let dataDict = (response as! [String:Any])["data"] as? [String:Any] {
                        
                        self.lastPage = dataDict["last_page"] as! Int
                        
                        if let postArr = dataDict["data"] as? [[String:Any]] {
                            
                            let filterNotBlockedUserPosts = postArr.filter({ (postData) -> Bool in
                                let blockedBy = postData["blockedBy"] as? Int ?? 0
                                //                                let userID = (postData["user_info"] as! [String:Any])["id"] as? Int ?? 0
                                return (blockedBy == 0)
                            })
                            
                            
                            self.postList = self.postList + filterNotBlockedUserPosts
                            self.postTableView.reloadData()
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
     Request to delete my own post added by me
     - parameter index: Index number of my post in Post List
     **/
    func requestToDeletePost(for index: Int) {
        
        let post = Post(self.postList[index])
        
        let param = ["id" : post.id!]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        print(param)
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.deletePost, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    self.postList.remove(at: index)
                    self.postTableView.reloadData()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    
   
}
