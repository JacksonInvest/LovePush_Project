//
//  SearchUserTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class SearchUserTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var userImageView : UIImageView!
    @IBOutlet weak var userNameLabel : UILabel!
    @IBOutlet weak var distanceLabel : UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    var user : UserInfo!
    var isMatched : Bool!

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    var userData : [String:Any]! {
        didSet {
            self.user = UserInfo(userData)
            
            let formattedString = NSMutableAttributedString()
            formattedString
                .bold(self.user.name!)
                .normal(" (\(self.user.userName!))")
            
            self.userNameLabel.attributedText = formattedString
            
//            self.distanceLabel.text =
            self.userImageView.setImageWithoutBaseURL(user.imageURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            
            let matchData1 : [[String:Any]] = userData["match_info1"] as? [[String:Any]] ?? []
            let matchData2 : [[String:Any]] = userData["match_info2"] as? [[String:Any]] ?? []
            
            let finalMatch = matchData1 + matchData2
            
            self.isMatched = (finalMatch.count > 0)
            
            var distance = userData["distance"] as? Double ?? 0
            distance = distance.rounded(.up)
            
            self.distanceLabel.text = ("\(distance)" != "") ? "\(distance) KM".replacingOccurrences(of: ".0", with: "") : ""
            
            if self.user.matchInfo != nil {//User Already have a match then you can start direct chat with user
                if self.user.matchInfo!["event_type"] as! String == "L" {
                    self.userImageView.layer.borderColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0).cgColor
                }else if self.user.matchInfo!["event_type"] as! String == "M" {
                    self.userImageView.layer.borderColor = UIColor.darkGray.cgColor
                }else if self.user.matchInfo!["event_type"] as! String == "C" {
                    self.userImageView.layer.borderColor = UIColor.blue.cgColor
                }
                self.userImageView.layer.borderWidth = 1.5
            }
        }
    }
}
