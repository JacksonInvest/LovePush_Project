//
//  MatchRequest + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 30/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import UIKit

extension MatchRequestViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.myRequestList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchRequestTableViewCell", for: indexPath) as! MatchRequestTableViewCell
        
        cell.data = self.myRequestList[indexPath.row]
        
        cell.acceptButton.tag = indexPath.row
        cell.acceptButton.addTarget(self, action: #selector(acceptButtonTapped), for: .touchUpInside)
        
        cell.rejectButton.tag = indexPath.row
        cell.rejectButton.addTarget(self, action: #selector(rejectButtonTapped), for: .touchUpInside)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let vc         = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID      = "\(self.myRequestList[indexPath.row]["user_id"] as! Int)"
        vc.eventType   = self.myRequestList[indexPath.row]["event_type"] as! String
        vc.eventID     = self.myRequestList[indexPath.row]["event_id"] as! Int
        vc.fromPage    = "Notification"
        if let messageEventInfo = self.myRequestList[indexPath.row]["mess_event_info"] as? [String:Any] {
            vc.msgInfoStatus = messageEventInfo["status"] as? String ?? ""
        }
        self.navigationController?.pushViewController(vc, animated: true)
        //        }
    }
    
    //MARK:- Cell Button Action
    @objc func acceptButtonTapped(_ sender: UIButton) {
        self.acceptOrRejectReceivedRequest(sender.tag, status: "A")
    }
    
    @objc func rejectButtonTapped(_ sender: UIButton) {
        self.acceptOrRejectReceivedRequest(sender.tag, status: "R")
    }
    
    func acceptOrRejectReceivedRequest(_ index: Int, status: String) {
        
        self.currentUser = UserInfo(self.myRequestList[index])
        
        let eventType = self.myRequestList[index]["event_type"] as! String
        let eventID = self.myRequestList[index]["event_id"] as! Int
        let qbID = "\((self.myRequestList[index]["user_info"] as! [String:Any])["quickblox_id"]!)"
        
        let userID  = self.myRequestList[index]["user_id"] as! Int
        
        if eventType == "S_L_R" {
            self.likeOrDislikUserRequest((status == "A") ? 1 : 0, eventType: eventType, eventID: eventID, qbID: qbID, userID: userID)
        }else {
            self.acceptOrRejectRequest(status, eventType: eventType, eventID: eventID, qbID: qbID)
        }
    }
}
