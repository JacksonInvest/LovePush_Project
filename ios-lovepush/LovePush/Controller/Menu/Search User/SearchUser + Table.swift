//
//  SearchUser + Table.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import UIKit
extension SearchUserViewController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.usersArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SearchUserTableViewCell", for: indexPath) as! SearchUserTableViewCell
        
        cell.userData = self.usersArr[indexPath.row]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let cell = tableView.cellForRow(at: indexPath) as! SearchUserTableViewCell
        
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID     = "\((self.usersArr[indexPath.row])["id"] as! Int)"
        vc.isOnlyShow = cell.isMatched
        vc.fromPage   = "Search"
        vc.userInfo   = UserInfo(self.usersArr[indexPath.row])
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
        DispatchQueue.global().sync {
            if (((scrollView.contentOffset.y + scrollView.frame.size.height) > scrollView.contentSize.height ) ){
                if (self.pageNo < self.lastPageNo) && !self.isAPIEnabled {
                    self.isAPIEnabled = true
                    self.pageNo += 1
                    self.requestToSearchByName(searchTextField.text!)
                }
            }
        }
    }
}
