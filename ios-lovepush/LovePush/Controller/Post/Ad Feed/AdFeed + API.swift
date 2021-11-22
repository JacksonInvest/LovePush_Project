//
//  AdFeed + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 02/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

extension ADFeedVC {
    /**
        Request to get all ads list posted by me or other user
        - parameter url: URL "getAllPosts" to get All users ads or "myPostads" to get My Ads list
        - parameter latitude: Location Latitude where nearby you needs get ads list
        - parameter longitude: Location Longitude where nearby you needs get ads list
        - parameter isFilter: isFiler user for get ads list with manual address or current user location
     **/
    func requestToGetPost(_ url: String, latitude: Double, longitude: Double, isFilter: Bool) {
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        var urlStr = url + "?page=\(self.pageNo)&user_id=\(User.shared.id!)&latitude=\(latitude)&longitude=\(longitude)"
        if self.latitude == 0.0 {
           urlStr = url + "?page=\(self.pageNo)&user_id=\(User.shared.id!)"
        }
        
        
        WebAPI.requestToGetWithHeader(urlStr, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            self.isScrollingEnabled = false
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let dataDict = (response as! [String:Any])["data"] as? [String:Any] {
                        
                        self.lastPage = dataDict["last_page"] as! Int
                        
                        if var postArr = dataDict["data"] as? [[String:Any]] {
                            
                            let filterNotBlockedUserPosts = postArr.filter({ (postData) -> Bool in
                                let blockedBy = postData["blockedBy"] as? Int ?? 0
                                //                                let userID = (postData["user_info"] as! [String:Any])["id"] as? Int ?? 0
                                return (blockedBy == 0)
                            })
                            
                            postArr = filterNotBlockedUserPosts
                            
                            if isFilter {
                                let filteredAds = postArr.filter({$0["post_type"] as! Int == self.filterPostType})
                                
                                self.tempArr = self.postList + filteredAds
                                self.postList = self.tempArr
                            }else {
                                 self.tempArr = self.postList + postArr
                                self.postList = self.postList + postArr
                            }
                            self.adTableView.reloadData()
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
     Request to delete a ads posted by me
     - parameter index: Ads index listed in TableView
     **/
    func requestToDeletePost(for index: Int) {
        
        let post = Post(self.postList[index])
        
        let param = ["id" : post.id!]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        print(param)
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.deletePostAd, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    self.postList.remove(at: index)
                    self.adTableView.reloadData()
                    
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
