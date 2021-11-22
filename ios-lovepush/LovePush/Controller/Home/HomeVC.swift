//
//  HomeVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 16/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class HomeVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var postTableView    : UITableView!
    @IBOutlet weak var postHeaderHeight : NSLayoutConstraint!
    @IBOutlet weak var postHeaderView   : UIView!
    @IBOutlet weak var titleLabel       : UILabel!
    @IBOutlet weak var noPostLabel      : UILabel!
    
    //MARK:- Variables
    var pageNo = 1
    var lastPage = 1
    var postList = [[String:Any]]()
    var isScrollingEnabled = false
    var isHome = true
    var url = URLConstant.getAllPosts
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.postTableView.rowHeight   = UITableViewAutomaticDimension
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.pageNo = 1
        self.lastPage = 1
        self.postList.removeAll()
        self.postTableView.reloadData()
        self.requestToGetPost(self.url)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    //MARK:- Button Action
    //Action to add new post 
    @IBAction func PostAction(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "PostAdsID") as! PostAdsVC
        vc.isHome = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
