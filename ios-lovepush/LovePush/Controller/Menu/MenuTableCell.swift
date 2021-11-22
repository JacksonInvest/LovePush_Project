//
//  MenuTableCell.swift
//  LovePush
//
//  Created by Jatinder Singh on 17/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MenuTableCell: UITableViewCell {

    @IBOutlet weak var imgView: UIImageView!
    @IBOutlet weak var titleLbl: UILabel!
    @IBOutlet weak var downArrowImg: UIImageView!
    @IBOutlet weak var mainBtn: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
