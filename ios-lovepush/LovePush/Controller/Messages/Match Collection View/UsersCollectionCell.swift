//
//  UsersCollectionCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 28/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class UsersCollectionCell: UICollectionViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImageButton : UIButton!
    
    //MARK:- Variables
    var quickBloxID : String!
    var user: UserInfo!
    var matchID = 0
    
    var newdata: [String:Any]! {
        
        didSet {
            if var data = self.getOtherUserData(from: newdata) {
                data["event_type"] = newdata["event_type"]
                self.matchID = newdata["id"] as! Int
                self.user = UserInfo(data)
                
                if let imageURL = self.user.imageURL {
                    self.imgView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
                if let name = self.user.name {
                    let nameComponents = name.components(separatedBy: " ")
                    if nameComponents.count != 0 {
                        self.nameLbl.text = nameComponents.first ?? ""
                    }
                }
                self.quickBloxID = self.user.qbID!
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


