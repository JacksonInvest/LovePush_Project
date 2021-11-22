//
//  FAQViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class FAQViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var questionTableView : UITableView!
    @IBOutlet weak var categoryCollectionView : UICollectionView!
    
    //MARK:- Variables
    
    var faqCategoryList = [[String:Any]]()
    var questionArray   = [[String:Any]]()
    
    var selectedQsnArr = NSMutableArray()
    
    var selectedCategoryIndex = 0

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.questionTableView.tableFooterView = UIView()
        
        self.getFAQList()
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}

//MARK:- API

extension FAQViewController {
    /**
     Get FAQ List
    **/
    func getFAQList() {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        let languageID = (Singleton.shared.language == "en") ? 1 : 2
        
        WebAPI.requestToGetWithHeader(URLConstant.getFAQ + "?lang_id=\(languageID)", header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if let faq = (response as! [String:Any])["faq"] as? [[String:Any]] {
                        if faq.count > 0 {
                            self.faqCategoryList = faq
                            self.questionArray = self.faqCategoryList[0]["qsn_list"]  as? [[String:Any]] ?? []//Default Question List show for first Category index
                            self.categoryCollectionView.reloadData()
                            self.questionTableView.reloadData()
                        }
                    }
                }else {
                   self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}

