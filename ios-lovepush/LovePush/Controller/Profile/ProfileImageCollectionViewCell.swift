//
//  ProfileImageCollectionViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class ProfileImageCollectionViewCell: UICollectionViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var reportButton: UIButton!
    
    //MARK:- Variables
    var user : UserInfo!
    var matchID = 0
    var isLiked = false
    var post : Post!
    
    var localdata: [String:Any]! {
        
        didSet {
            if var data = localdata["user_info"] as? [String:Any] {
                
                self.matchID  = data["match_id"] as? Int ?? 0
                
                //If other user blocked me then we can't sent any message to user
                //And B user delete chat A(me) then A will not be in Message inbox list of B.
                //So when either A or B send new message each other then A and B will be in Message inbox list
                //So deleted status use request for restore API to change deleted status
                data["deletedBy"]    = localdata["deletedBy"] as? Int ?? 0
                data["isDeleted"]    = 0
                data["blockedBy"]    = localdata["blockedBy"] as? Int ?? 0
                data["isBlocked"]    = 0
                
                if let matchData = self.post.matchData {
                    data["event_type"]  = (matchData["match_info"] as! [String:Any])["event_type"] as? Int ?? 0
                    self.matchID        = matchData["match_id"] as? Int ?? 0
                    data["chat_status"] = matchData["chat_status"] as? Int ?? 1
                }
                
                self.user = UserInfo(data)
                
                if let matchInfo = localdata["matchQbID"] as? [String:Any] {
                    self.user.matchInfo = ["qb_chat_dialog" : matchInfo]
                    self.user.eventType = (matchInfo["match_info"] as! [String:Any])["event_type"] as! String
                }
            }
        }
    }
}
