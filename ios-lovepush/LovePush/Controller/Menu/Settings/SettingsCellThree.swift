//
//  SettingsCellThree.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/07/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

class SettingsCellThree: UITableViewCell {
    
    @IBOutlet weak var titleLbl     : UILabel!
    @IBOutlet weak var switchButton : UISwitch!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    var index : Int!
    
    var setting: Setting! {
        didSet {
            switch index {
            case 1:
                switchButton.isOn      = setting.isNotificationEnable
                break;
//            case 2:
//                self.switchButton.isOn = setting.isRecieveChatRequest
//                break;
            case 2:
                self.switchButton.isOn = setting.isRecieveLoveRequest
                break;
            case 3:
                self.switchButton.isOn = setting.isHideFromSearch
                break;
            default:
                print("Default")
            }
        }
    }
    
    
    
}
