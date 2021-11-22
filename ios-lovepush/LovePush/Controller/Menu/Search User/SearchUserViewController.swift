//
//  SearchUserViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class SearchUserViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var searchTextField   : UITextField!
    @IBOutlet weak var resultFoundLabel  : UILabel!
    @IBOutlet weak var userListTableView : UITableView!
    
    //MARK:- Variables
    var usersArr = [[String:Any]]()
    var pageNo = 1
    var lastPageNo = 1
    var isAPIEnabled = false

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.userListTableView.tableFooterView = UIView()
        self.searchTextField.addTarget(self, action: #selector(textFieldEditingChanged), for: .editingChanged)
    }
    
    //Mark:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}

//MARK:- Text Field Delegate
extension SearchUserViewController : UITextFieldDelegate {
   
    @objc func textFieldEditingChanged(_ textField: UITextField) {
            DispatchQueue.global().sync {
                self.pageNo = 1
                //            self.lastPageNo = 1
                self.usersArr.removeAll()
                self.userListTableView.reloadData()
            }
        if !self.isAPIEnabled {
            self.isAPIEnabled = true
            self.requestToSearchByName(textField.text!)
        }
    }
}
