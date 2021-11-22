//
//  HomeTableCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class HomeTableCell: UITableViewCell {

    //MARK:- Outlets
    @IBOutlet weak var topView          : UIView!
    
    @IBOutlet weak var profileImageView : UIImageView!
    @IBOutlet weak var postImageView    : UIImageView!
    
    @IBOutlet weak var userNameLabel    : UILabel!
    @IBOutlet weak var timeLabel        : UILabel!
    @IBOutlet weak var titleLabel       : UILabel!
    @IBOutlet weak var descriptionLabel : UILabel!
    
    @IBOutlet weak var deleteButton     : UIButton!
    @IBOutlet weak var profileButton    : UIButton!
    @IBOutlet weak var likeButton       : UIButton!
    @IBOutlet weak var chatButton       : UIButton!
    @IBOutlet weak var reportButton     : UIButton!
    
    @IBOutlet weak var userImageWidth   : NSLayoutConstraint!
    @IBOutlet weak var postImageHeight  : NSLayoutConstraint!
    
    //MARK:- Variables
    var user : UserInfo!
    var matchID = 0
    var isLiked = false
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.topView.shadowAllSides()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    var post : Post! {
        didSet {
            
            self.titleLabel.text        = post.title!
            self.descriptionLabel.text  = post.description!
            self.timeLabel.text         = "\(Date().offset(from: self.getDateFormate(post.date!)))"
            
            if post.url != nil {
                self.postImageView.isHidden = false
                self.postImageHeight.constant = 236
                self.postImageView.setImageWithoutBaseURL(post.url!, PlaceholderImage: #imageLiteral(resourceName: "default_image"))
            }else {
                self.postImageView.isHidden = true
                self.postImageHeight.constant = 130
            }
            
//            if (post.anonymouslStatus == 1)  {
//                self.userImageWidth.constant = 0
//                self.userNameLabel.text      = ""
//                self.profileImageView.isHidden = true
//            }else {
//                self.userImageWidth.constant = 60
                self.profileImageView.isHidden = false
                var imageURL = ""
                if let user = post.user {
                    self.userNameLabel.text = post.user?.userName!
                    imageURL                = user.imageURL!
                }else {
                    self.userNameLabel.text = User.shared.username!
                    imageURL                = User.shared.profileURL
                }
                self.profileImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
//            }
            
            self.deleteButton.isHidden  = !("\(post.user!.id!)" == User.shared.id!)
            self.profileButton.isHidden = ("\(post.user!.id!)" == User.shared.id!)
            self.reportButton.isHidden  = ("\(post.user!.id!)" == User.shared.id!)
            
//            self.likeButton.isHidden  = ("\(post.user!.id!)" == User.shared.id!)
            self.chatButton.isHidden    = ("\(post.user!.id!)" == User.shared.id!) || (post.anonymouslStatus == 1)
            
            if post.isLiked! {
                self.likeButton.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
            }else {
                self.likeButton.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
            }
        }
    }
    
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

    /**
     Date Format to show post added time
     - parameter dateStr: Default date format string
     **/
    func getDateFormate(_ dateStr: String)-> Date {
        
        let dateFormatter        = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone   = TimeZone(identifier: "UTC")
        dateFormatter.locale     = Locale.current
        let date = dateFormatter.date(from: dateStr)
        
        return date!
    }
}
