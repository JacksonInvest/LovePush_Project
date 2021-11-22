//
//  MatchList + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 28/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

extension MatchListViewController {
    func getMatchedUser() {
        
        let param  = ["user_id" : User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.myMatchedUser, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    
                    if let allMatchedArr = (response as! [String:Any])["data"] as? [[String:Any]] {
                        
                        self.loveMatchList = allMatchedArr.filter({$0["event_type"] as! String == "L"})
                        
                        self.connectMatchList = allMatchedArr.filter({($0["event_type"] as! String == "C") || ("\($0["deletedBy"] as! Int)" == User.shared.id!) && ($0["event_type"] as! String == "M")})
                        
                        if self.selectIndicator.center.x == self.loveButton.center.x {
                            self.matchArr = self.loveMatchList
                        }else {
                            self.matchArr = self.connectMatchList
                        }
                        self.emptyLabel.isHidden = (self.matchArr.count > 0)
                        self.matchTableView.reloadData()
                    }
                }else {
                    //                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
