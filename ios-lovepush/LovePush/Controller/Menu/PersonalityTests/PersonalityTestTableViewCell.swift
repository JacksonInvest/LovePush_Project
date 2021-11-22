//
//  PersonalityTestTableViewCell.swift
//  LovePush
//
//  Created by Lavi Kumar on 07/05/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class PersonalityTestTableViewCell: UITableViewCell {
    
    //MARK:- Outlets
    @IBOutlet weak var answerTitleLabel: UILabel!
    @IBOutlet weak var checkButton: UIButton!
    @IBOutlet weak var superView: UIView!

    //MARK:- Cell Life Cycle
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}
