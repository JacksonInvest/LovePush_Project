//
//  WatchBroadcastList + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 12/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit

extension WatchLiveVC {
    func getLiveBroadcastList() {
        let header = [
                        "Authorization":"Bearer 8hs9txn38zk3lgd5953yjhdqa",
                        "Accept":"application/vnd.bambuser.v1+json",
                        "Content-Type": "application/json"
                     ]
        
        WebAPI.requestToGetWithHeader(URLConstant.getBroadcastList, header: header as [String : AnyObject], completion: { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if let resultArr = (response as! [String:Any])["results"] as? [[String:Any]] {
                   self.broadcastArr = resultArr
                   self.broadcastTableView.reloadData()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        })
    }
}


