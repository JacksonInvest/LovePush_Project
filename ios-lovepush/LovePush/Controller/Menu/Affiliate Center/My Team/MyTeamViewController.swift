
//
//  MyTeamViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/08/19.
//  Copyright © 2019 Lavi Kumar All rights reserved.
//

import UIKit

class MyTeamViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var levelCollectionView : UICollectionView!
    @IBOutlet weak var teamMemberTableView : UITableView!
    
    //MARK:- Variables
    var myTeamData         = [String:Any]()
    var levelCount         = 0
    var userArr            = [[String:Any]]()
    var selectedLevelIndex = 0
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        self.getMyTeam()
        self.teamMemberTableView.tableFooterView = UIView()
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender : UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}
