//
//  UserTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 28/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class UserTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var imgView            : UIImageView!
    @IBOutlet weak var nameLbl            : UILabel!
    @IBOutlet weak var lastMessageLabel   : UILabel!
    @IBOutlet weak var lastUpdateTime     : UILabel!
    @IBOutlet weak var messageCountLabel  : UILabel!
    @IBOutlet weak var profileImageButton : UIButton!
    
    //MARK:- Variable
    var userOccupantID  = 0
    var user : UserInfo!
    var userDict = ["profile_image":"","name":"","quickblox_id":"","id":0,"event_type":"","chat_status":0] as [String : Any]
    var matchID = 0
    
    //MARK:- Cell Life Cycle
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    //MARK:- Variables to set Data
    var dialog: QBChatDialog! {
        
        didSet {
            if let imageURL = dialog.photo {
                self.userDict["profile_image"] = imageURL
                self.imgView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            }else {
                let opponanents = dialog.occupantIDs
                let strOpponentsArr = opponanents?.filter({"\($0)" != "\(User.shared.qBUserID!)"})
                self.userOccupantID = strOpponentsArr?.first as! Int
                self.userDict["quickblox_id"] = "\(String(describing: self.userOccupantID))"
            }
            
            self.messageCountLabel.isHidden = dialog.unreadMessagesCount == 0
            
            self.messageCountLabel.text = "\(dialog.unreadMessagesCount)"
            
            self.nameLbl.text = dialog.name ?? ""
            self.userDict["name"] = dialog.name ?? ""
            
            self.lastMessageLabel.text = dialog.lastMessageText ?? ""
            
            self.lastUpdateTime.text = (dialog.lastMessageDate != nil) ? "\(Date().offset(from: dialog.lastMessageDate!))" : ""
        }
    }
    
    var localdata: [String:Any]! {
        
        didSet {
            if var data = getOtherUserData(from: localdata) {
                
                data["event_type"] = localdata["event_type"]
                self.matchID       = localdata["id"] as? Int ?? 0
                
                if let qbChatDialog = localdata["qb_chat_dialog"] as? [String:Any] {
                    data["chat_status"] = qbChatDialog["chat_status"] as? Int ?? 1
                }
                
                data["isDeleted"] = localdata["isDeleted"] as? Int ?? 0
                data["deletedBy"] = localdata["deletedBy"] as? Int ?? 0
                
                data["isBlocked"] = localdata["isBlocked"] as? Int ?? 0
                data["blockedBy"] = localdata["blockedBy"] as? Int ?? 0
                
                self.user = UserInfo(data)
                
                if let imageURL = self.user.imageURL {
                    self.imgView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
                
                if let name = self.user.name {
                    self.nameLbl.text = name
                }
            }
        }
    }
    
    /**
     Get other user data on bases of user_info1 or user_info2
     - parameter data: Match Data in myMatches List
     - returns: Other User Data
     **/
    func getOtherUserData(from data: [String:Any])-> [String:Any]? {
        var finalData = [String:Any]()
        DispatchQueue.global().sync {
            if let userInfo1 = data["user_info1"] as? [String:Any], let userInfo2 = data["user_info2"] as? [String:Any] {
                if let userID1 = userInfo1["id"] as? Int {
                    if "\(userID1)" == User.shared.id! {
                        finalData = userInfo2
                    }else {
                        finalData = userInfo1
                    }
                }
            }
        }
        return finalData
    }
}

