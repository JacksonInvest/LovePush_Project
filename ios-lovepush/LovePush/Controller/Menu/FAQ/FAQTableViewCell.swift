//
//  FAQTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 08/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class FAQTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var qsnLabel : UILabel!
    @IBOutlet weak var arrowIcon : UIImageView!
    @IBOutlet weak var answerLabel : UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
