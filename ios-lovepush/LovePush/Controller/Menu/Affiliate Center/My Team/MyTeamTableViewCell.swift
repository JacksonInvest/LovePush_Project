//
//  MyTeamTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class MyTeamTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var userImageView : UIImageView!
    @IBOutlet weak var userNameLabel : UILabel!

    //MARK:- Cell Life Cycle Methods
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    //MARK:- Set Team member info data
    var userData : [String:Any]! {
        didSet {
            let user = UserInfo(userData)
            self.userNameLabel.text = user.name!
            self.userImageView.setImageWithoutBaseURL(user.imageURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        }
    }
}
