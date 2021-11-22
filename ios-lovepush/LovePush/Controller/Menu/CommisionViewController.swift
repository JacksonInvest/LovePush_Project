//
//  CommisionViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 08/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class CommisionViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var titleLabel  : UILabel!
    @IBOutlet weak var inviteLabel : UILabel!
    
    var titleStr = "COMMISION"

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.titleLabel.lzText = titleStr
        self.inviteLabel.isHidden = (titleStr == "MEMBERSHIP")
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped() {
        self.navigationController?.popViewController(animated: true)
    }
}
