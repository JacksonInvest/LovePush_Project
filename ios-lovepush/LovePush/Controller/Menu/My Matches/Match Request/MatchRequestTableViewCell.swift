//
//  MatchRequestTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MatchRequestTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var userImageView : UIImageView!
    @IBOutlet weak var userNameLabel : UILabel!
    @IBOutlet weak var acceptButton  : UIButton!
    @IBOutlet weak var rejectButton  : UIButton!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    var data: [String:Any]! {
        didSet {
            let message = (data["notification_message"] as! [String:Any])["message"] as? String ?? ""
            if let userInfo = data["user_info"] as? [String:Any] {
                let userName = userInfo["name"] as? String ?? ""
                let titleText = message
                
                self.userNameLabel.attributedText = self.attributedText(withString: titleText, boldString: userName, font: self.userNameLabel.font)
                
                if let imageURL = userInfo["profile_image"] as? String {
                    self.userImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
                let eventType = data["event_type"] as! String
                self.userImageView.layer.borderColor = (eventType == "S_L_R") ? UIColor.red.cgColor : ((eventType == "S_C_R") ? UIColor.blue.cgColor : UIColor.lightGray.cgColor)
                self.userImageView.layer.borderWidth = 1.5
            }
        }
    }
    
    func attributedText(withString string: String, boldString: String, font: UIFont) -> NSAttributedString {
        let attributedString = NSMutableAttributedString(string: string,
                                                         attributes: [NSAttributedString.Key.font: font])
        let boldFontAttribute: [NSAttributedString.Key: Any] = [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: font.pointSize)]
        let colorAttribute:[NSAttributedString.Key: Any] = [NSAttributedString.Key.strokeColor: UIColor.red]
        let range = (string as NSString).range(of: boldString)
        attributedString.addAttributes(boldFontAttribute, range: range)
        attributedString.addAttributes(colorAttribute, range: range)
        return attributedString
    }
}
