//
//  SubscriptionDetailsViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 21/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class SubscriptionDetailsViewController: UIViewController, UIScrollViewDelegate, UITableViewDelegate, UITableViewDataSource {
    
    //MARK:- Outlets
    @IBOutlet weak var planTableView: UITableView!
    @IBOutlet weak var tableViewHeight : NSLayoutConstraint!
    @IBOutlet weak var planButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var currentCheckIcon : UIImageView!
    @IBOutlet weak var pageControl : UIPageControl!
    @IBOutlet weak var scrollView:UIScrollView!
    
    //MARK:- Variables
    var planDetails : [String:Any]!
    var index = 0
    
    var planKeys = [String]()
    var keys    = [
        "messages",
        "sending_requests",
        "see_love_requests",
        "recommended_profiles",
        "ads"
    ]
    
    var plansTitle = [
                    "messages"              : "Message to new users",
                    "sending_requests"      : "Send Requests",
                    "see_love_requests"     : "See Love Requests",
                    "recommended_profiles"  : "Recommended Profile",
                    "ads"                   : "Post Ads"
                    ]
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
    }
    
    override func viewDidLayoutSubviews() {
//        self.tableViewHeight.constant = self.planTableView.contentSize.height
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if planDetails == nil {
            self.planKeys.removeAll()
            self.scrollView.isHidden = true
        }else {
            self.planKeys = self.keys
            self.scrollView.isHidden = false
            self.planTableView.reloadData()
            self.initialSetup()
            self.pageControl.currentPage = self.index
            self.titleSetup()
        }
    }
    
    func titleSetup() {
        
        let title = self.planDetails!["type"] as? String ?? ""
        let price = self.planDetails!["price"]!
        
        self.titleLabel.text = (title == "FREE") ? "" : title
        self.amountLabel.text = (title == "FREE") ? title : "$\(price)"
        
        self.currentCheckIcon.isHidden = (title != SubscriptionPlan.shared.planType)
        
        if (title == "FREE") {
            self.planButton.isHidden = true
        }else {
            let planButtonTitle = (title == SubscriptionPlan.shared.planType) ? "CANCEL" : "SUBSCRIBE"
            
            self.planButton.setTitle(planButtonTitle, for: .normal)
            
            self.planButton.backgroundColor = (self.planButton.currentTitle! == "CANCEL") ? UIColor.gray : UIColor(red: 184/255, green: 02/255, blue: 25/255, alpha: 1.0)
        }
    }
    
    func initialSetup() {
        self.planTableView.layer.cornerRadius = 12
        self.planTableView.layer.masksToBounds = true
        self.planTableView.layer.borderColor = UIColor.red.cgColor
        self.planTableView.layer.borderWidth = 1
    }
    
    //MARK:- Button Actions
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func planButtonTapped(_ sender : UIButton) {
        
        if sender.currentTitle == "CANCEL" {
            self.cancelCurrentPlanOrSetToFree()
        }else {
            print(self.index)
            if self.index == 0 {//Free
                self.cancelCurrentPlanOrSetToFree()
            }else {
                if let planID = self.planDetails["plan_id"] as? String {
                    self.requestToSubscribePlan(planID)
                }else {
                    showAlert(title: "Love Push", message: "Selected Plan Invalid.", completion: nil)
                }
            }
        }
    }
    
    //MARK:- Table View Delegate And Data Source
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.planKeys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SubscriptionPlanTableViewCell", for: indexPath) as! SubscriptionPlanTableViewCell
        
        let key = self.planKeys[indexPath.row]
        let titleValue = self.plansTitle[key] ?? ""
        
        let normalText = titleValue + " "
        let boldText = (self.planDetails[key] as? String ?? "")
        
        cell.planTitleLabel.attributedText = self.attributedText(withString: normalText + boldText, boldString: boldText, font: cell.planTitleLabel.font)
        
        return cell
    }
    
    func attributedText(withString string: String, boldString: String, font: UIFont) -> NSAttributedString {
        let attributedString = NSMutableAttributedString(string: string,
                                                         attributes: [NSAttributedString.Key.font: font])
        let boldFontAttribute: [NSAttributedString.Key: Any] = [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: font.pointSize)]
        let range = (string as NSString).range(of: boldString)
        attributedString.addAttributes(boldFontAttribute, range: range)
        return attributedString
    }
}


//MARK:- APIs
extension SubscriptionDetailsViewController {
    
    func requestToSubscribePlan(_ planID : String) {
        
        let header = ["locale"  : Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.subscribePlan + "plan_id=\(planID)", header: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let paymentURL = (response as! [String:Any])["data"] as? String {
                        let vc = mainStoryboard.instantiateViewController(withIdentifier: "PaymentViewController") as! PaymentViewController
                        vc.paypalURL = paymentURL
                        vc.selectedPlan = self.planDetails
                        self.navigationController?.pushViewController(vc, animated: true)
                    }else {
                        self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    func cancelCurrentPlanOrSetToFree() {
        
        let header = ["locale"  : Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.cancelSubscription, header: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    self.showAlert(title: "Love push", message: "Plan has been subscribed successfully.", completion: { (ok) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
