//
//  MatchRequestViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MatchRequestViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var requestListTableView : UITableView!
    
    //MARK:- Variables
    var myRequestList = [[String:Any]]()
    var currentUser : UserInfo!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.requestListTableView.tableFooterView = UIView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.getNotificationList()
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}
