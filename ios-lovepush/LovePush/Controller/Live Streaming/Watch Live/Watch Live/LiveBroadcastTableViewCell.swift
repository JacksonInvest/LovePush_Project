//
//  LiveBroadcastTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 12/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class LiveBroadcastTableViewCell: UITableViewCell, BambuserPlayerDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var broadcastImageView : UIImageView!
    @IBOutlet weak var liveLabel          : UILabel!
    @IBOutlet weak var dateLabel          : UILabel!
    @IBOutlet weak var timeLabel          : UILabel!
    @IBOutlet weak var playerView         : UIView!

    //MARK:- Variable
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
        
        
    }
    
    var broadcast :Broadcast! {
        didSet {
            self.liveLabel.text = (broadcast.type! == "archived") ? "Was Live" : "Live"
            self.liveLabel.isHidden = (broadcast.type! == "archived")
            self.broadcastImageView.setImageWithoutBaseURL(broadcast.preview!, PlaceholderImage: #imageLiteral(resourceName: "default_image"))
            self.convertTimeStampToDate()
        }
    }
    
    
    func convertTimeStampToDate() {
        let date = Date(timeIntervalSince1970: broadcast.createdAt!)
        
        self.timeLabel.text = "\(Date().offset(from: date))"
        
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "GMT") //Set timezone that you want
        dateFormatter.locale = NSLocale.current
        dateFormatter.dateFormat = "dd/MM/yyyy" //Specify your format that you want
        
        let strDate = dateFormatter.string(from: date)
        
        self.dateLabel.text = strDate
    }
    
}
