//
//  AdFeedTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 02/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class AdFeedTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var topShadowView    : UIView!
    @IBOutlet weak var userImageView    : UIImageView!
    @IBOutlet weak var adImageView      : UIImageView!
    @IBOutlet weak var nameLabel        : UILabel!
    @IBOutlet weak var timeLabel        : UILabel!
    @IBOutlet weak var locationLabel    : UILabel!
    @IBOutlet weak var titleLabel       : UILabel!
    @IBOutlet weak var descriptionLabel : UILabel!
    @IBOutlet weak var adTypeButton     : UIButton!
    @IBOutlet weak var deleteButton     : UIButton!
    @IBOutlet weak var profileButtoon   : UIButton!
    @IBOutlet weak var reportButton     : UIButton!  //Report Photo
    @IBOutlet weak var userImageWidth   : NSLayoutConstraint!
    @IBOutlet weak var postImageHeight  : NSLayoutConstraint!

    //MARK:- Cell life cycle methods
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

    //MARK:- Variables
    var post : Post! {
        didSet {
            
            self.titleLabel.text        = post.title!
            self.descriptionLabel.text  = post.description!
            self.locationLabel.text     = post.address!
            self.timeLabel.text         = "\(Date().offset(from: self.getDateFormate(post.date!)))"
            
            if (post.anonymouslStatus != 1) || ("\(post.user!.id!)" == User.shared.id!) {
                
                //                self.userImageWidth.constant = 50
                //                self.userImageView.isHidden  = false
                var imageURL = ""
                if let user = post.user {
                    self.nameLabel.text     = post.user?.userName!
                    imageURL                = user.imageURL!
                }else {
                    self.nameLabel.text     = User.shared.username!
                    imageURL                = User.shared.profileURL
                }
                self.userImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))

            }else {
                //                self.userImageWidth.constant = 0
                //                self.userImageView.isHidden  = true
                self.userImageView.image = #imageLiteral(resourceName: "userPlaceholder")
                self.nameLabel.text          = ""
            }
            
            self.postImageHeight.constant = (post.url == nil) ? 20 : 200
            self.adImageView.isHidden     = (post.url == nil)
            
            let adType = (post.type! == 1) ? "Love Ad" : "Connect Ad"
            
            self.adTypeButton.setTitle(adType.localized(), for: .normal)
            
            if post.url != nil {
                self.adImageView.setImageWithoutBaseURL(post.url!, PlaceholderImage: #imageLiteral(resourceName: "default_image"))
            }else {
                self.topShadowView.layer.borderWidth = 0.5
                self.topShadowView.layer.borderColor = UIColor.lightGray.cgColor
            }
            
            self.topShadowView.shadowAllSides()
        }
    }
    
    func getDateFormate(_ dateStr: String)-> Date {
        
        let dateFormatter        = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone   = TimeZone(identifier: "UTC")
        dateFormatter.locale     = Locale.current
        let date = dateFormatter.date(from: dateStr)
        
        return date!
    }
}
