//
//  MatchListTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MatchListTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var userImageView : UIImageView!
    @IBOutlet weak var userNameLabel : UILabel!
    @IBOutlet weak var unblockButton : UIButton!
    
    var user: UserInfo!
    var eventType: String!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    var matchData : [String:Any]! {
        didSet {
            
            self.eventType = matchData!["event_type"] as! String ?? ""
           
            if let userData = self.getOtherUserData(from: matchData!) {
                
                self.user = UserInfo(userData)
                self.user.matchInfo = matchData
                if let imageURL = self.user.imageURL {
                    self.userImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
                if let name = self.user.name {
                    self.userNameLabel.text = name
                }
                
                self.unblockButton.isHidden = (matchData["isBlocked"] as! Int == 0)
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
