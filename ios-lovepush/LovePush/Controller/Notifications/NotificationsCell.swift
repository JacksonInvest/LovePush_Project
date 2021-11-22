//
//  NotificationsCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class NotificationsCell: UITableViewCell {

    //MARK:- Outlet
    @IBOutlet weak var titleLbl             : UILabel!
    @IBOutlet weak var profileImageView     : UIImageView!
    @IBOutlet weak var acceptRejectStack    : UIStackView!
    @IBOutlet weak var acceptButton         : UIButton!
    @IBOutlet weak var rejectButton         : UIButton!
    @IBOutlet weak var buttonHieght         : NSLayoutConstraint!
    
    //MARK:- Cell Life cycle methods
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

    //MARK:- Variables
    //Set notification data to cell component
    var data: [String:Any]! {
        didSet {
            let message = (data["notification_message"] as! [String:Any])["message"] as? String ?? ""
            
            if let userInfo = data["user_info"] as? [String:Any] {
                let userName = userInfo["name"] as? String ?? ""
                let titleText = (!message.contains(".")) ? message + "." : message
                
                self.titleLbl.text = titleText
                
                if let imageURL = userInfo["profile_image"] as? String {
                    self.profileImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
                
                
                if let eventType = data["event_type"] as? String {
                    
                    if (eventType == "S_M_R") || (eventType == "S_C_R") || (eventType == "S_L_R") {
                        //Show accept or reject button for new request recieved
                        self.buttonHieght.constant = 25
                        self.acceptRejectStack.isHidden = false
                    }else {
                        //Hide accept or reject button if notification for my sent request accepted
                        self.buttonHieght.constant = 0
                        self.acceptRejectStack.isHidden = true
                    }
                }else {
                    self.acceptRejectStack.isHidden = true
                }
            }
        }
    }
}
