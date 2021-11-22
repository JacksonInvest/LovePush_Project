//
//  SearchUser + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

extension SearchUserViewController {
    /**
     Request search user any where in word with name
     - parameter searchStr: Name String to search 
    **/
    func requestToSearchByName(_ searchStr : String) {
        
        let param = ["search_term" : searchStr]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPOSTWithHeader(URLConstant.searchUser + "?page=\(pageNo)", params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        self.lastPageNo = data["last_page"] as! Int
                        if let users = data["data"] as? [[String:Any]] {
                            self.usersArr = self.usersArr + users
                            let totalItemsFetched = data["to"] as! Int
                            let countStr      = totalItemsFetched > 0 ? "\(totalItemsFetched)" : "No".localized()
                            self.resultFoundLabel.text = "\(countStr)" + " " + "Results Founds".localized()
                            self.userListTableView.reloadData()
                            self.isAPIEnabled = false
                        }
                    }
                }else {
                    self.usersArr.removeAll()
//                    self.resultFoundLabel.text = "\(self.usersArr.count)".replacingOccurrences(of: "0", with: "No") + " Results Founds".localized()
                    self.userListTableView.reloadData()
                    let countStr      = "No".localized()
                    self.resultFoundLabel.text = "\(countStr)" + " " + "Results Founds".localized()
                     self.isAPIEnabled = false
                }
            }else {
                self.usersArr.removeAll()
//                self.resultFoundLabel.text = "\(self.usersArr.count)".replacingOccurrences(of: "0", with: "No") + " Results Founds".localized()
                self.userListTableView.reloadData()
                let countStr      = "No".localized()
                self.resultFoundLabel.text = "\(countStr)" + " " + "Results Founds".localized()
                 self.isAPIEnabled = false
            }
        }
    }
}
