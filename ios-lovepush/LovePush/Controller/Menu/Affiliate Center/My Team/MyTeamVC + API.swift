//
//  MyTeamVC + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 28/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

extension MyTeamViewController {
    /**
     Get All Team member list referred by me and my reference
    **/
    func getMyTeam() {
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.geMyTeam, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let data = (response as! [String:Any])["data"] {
                        self.myTeamData = data as? [String : Any] ?? [:]
                        self.levelCount = self.myTeamData.count
                        if self.levelCount > 0 {
                           self.userArr = self.myTeamData["level1"] as! [[String:Any]]
                           self.levelCollectionView.reloadData()
                           self.teamMemberTableView.reloadData()
                        }else {
                            self.showAlert(title: "Love Push", message: "No Data Found", completion: { (yes) in
                                self.navigationController?.popViewController(animated: true)
                            })
                        }
                    }else {
                        self.showAlert(title: "Love Push", message: "No Data Found", completion: { (yes) in
                            self.navigationController?.popViewController(animated: true)
                        })
                    }
                }else {
                    self.showAlert(title: "Love Push", message: "No Data Found", completion:{ (yes) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: "No Data Found", completion: { (yes) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
}
