//
//  MatchListViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MatchListViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var loveButton : UIButton!
    @IBOutlet weak var connectButton : UIButton!
    @IBOutlet weak var matchTableView : UITableView!
    @IBOutlet weak var selectIndicator : UIView!
    @IBOutlet weak var emptyLabel : UILabel!
    //MARK:- Variables
    var loveMatchList = [[String:Any]]()
    var connectMatchList = [[String:Any]]()
    var matchArr = [[String:Any]]()

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.matchTableView.tableFooterView = UIView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.getMatchedUser()
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender : UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func headerButtonTapped(_ sender : UIButton) {
        
        let redColor  = UIColor(red: 184/255, green: 2/255, blue: 24/255, alpha: 1.0)
        let blueColor = UIColor(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0)
        
        self.selectIndicator.center.x = sender.center.x
        
        self.selectIndicator.backgroundColor = (sender.tag == 0) ? redColor : blueColor

        self.matchArr = (sender.tag == 0) ? self.loveMatchList : self.connectMatchList
        self.matchTableView.reloadData()
    }
}
