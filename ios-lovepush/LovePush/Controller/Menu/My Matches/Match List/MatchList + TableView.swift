//
//  MatchList + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import UIKit

extension MatchListViewController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        self.emptyLabel.isHidden = (self.matchArr.count > 0)
        return self.matchArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchListTableViewCell", for: indexPath) as! MatchListTableViewCell
        cell.matchData = self.matchArr[indexPath.row]
//        cell.userImageView.layer.borderColor = (self.connectButton.center.x == self.selectIndicator.center.x) ? UIColor.blue.cgColor : UIColor.red.cgColor
//        cell.userImageView.layer.borderWidth = 1
        
        cell.unblockButton.tag = indexPath.row
        cell.unblockButton.addTarget(self, action: #selector(unBlockButtonTapped), for: .touchUpInside)
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let cell = tableView.cellForRow(at: indexPath) as! MatchListTableViewCell
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID     = "\(cell.user.id!)"
        vc.isOnlyShow = true
        vc.userInfo   = cell.user
        vc.fromPage   = "Match List"
//        vc.isBlocked  = (self.matchArr[indexPath.row]["isBlocked"] as! Int == 1)
//        vc.deletedBy  = (self.matchArr[indexPath.row]["isDeleted"] as! Int == 1) ? Int(User.shared.id!)! : (self.matchArr[indexPath.row]["isDeleted"] as! Int == 0) && ((self.matchArr[indexPath.row]["deletedBy"] as! Int != 0)) ? self.matchArr[indexPath.row]["id"] as! Int : 0
        vc.matchId    = self.matchArr[indexPath.row]["id"] as! Int
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func unBlockButtonTapped(_ sender : UIButton) {
        
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "Love Push", message: "Are you sure, you want to unblock this user.") { (isYes) in
            let indxPath = IndexPath(row: sender.tag, section: 0)
            let cell = self.matchTableView.cellForRow(at: indxPath) as! MatchListTableViewCell
            self.blockUnblockUser("\(cell.user.id!)", status: "unblock", completion: { (isSuccess) in
                self.getMatchedUser()
            })
        }
    }
}
