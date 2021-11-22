//
//  LoveIntentionTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class LoveIntentionTableViewCell: UITableViewCell {
    
    //Properties of Create Profile love push intension
    @IBOutlet weak var cPCheckIcon: UIImageView!
    @IBOutlet weak var cPLovePushTitleLabel: UILabel!
    
    //Properies of Update profile love push intention
    @IBOutlet weak var uPCheckIcon: UIImageView!
    @IBOutlet weak var uPLovePushTitleLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
