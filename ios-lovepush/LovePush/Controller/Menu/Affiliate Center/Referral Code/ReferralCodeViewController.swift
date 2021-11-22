//
//  ReferralCodeViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 23/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class ReferralCodeViewController: UIViewController {
    
    //MARK:- Outlet
    @IBOutlet weak var referralCodeLabel : UILabel!
    @IBOutlet weak var scrollView        : UIScrollView!
    @IBOutlet weak var teamGraphView     : UIView!
    @IBOutlet weak var directCountLabel  : UILabel!
    @IBOutlet weak var othersCountLabel  : UILabel!
    @IBOutlet weak var pieChartView      : UIView!
    @IBOutlet weak var graphHieght       : NSLayoutConstraint!

    //MARK:- Local Variable
    var chartView : RKPieChartView!
    var totalTeamCount = 0 // Total number of user in all level
    var myDirectTeamCount = 0 // Number of members Referred direct by me
    var androidAppLink = "" // App Link Dynamically get from Back End and share with friends
    var appleAppLink   = ""
    var isTeamAwailable = false
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.referralCodeLabel.text = User.shared.referralCode!
        
        self.getShareAppLink()
        
        self.getMyTeam()
    }
    
    //Chat to show Ratio of Team Members Referred by me
    func setupPieChartView() {
        
        let otherCount = self.totalTeamCount - self.myDirectTeamCount
        self.directCountLabel.text = "Direct".localized() + ": \(self.myDirectTeamCount)"
        self.othersCountLabel.text = "Other".localized() + ": \(otherCount)"
        
        let redColor  = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
        let blueColor = UIColor(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0)
        
        
        if self.isTeamAwailable {// If Team Available then show actual team ratio on chart
            
            let directTeam: RKPieChartItem = RKPieChartItem(ratio: self.myDirectTeamCount > 0 ? uint(self.myDirectTeamCount) : 1, color: redColor, title: nil)// This Chart not accept 0 show needs to set 1
            let otherTeam: RKPieChartItem = RKPieChartItem(ratio: (otherCount > 0) ? uint(otherCount) : 1, color: (otherCount > 0) ? blueColor : redColor, title: nil)
            
            self.chartView = RKPieChartView(items: [otherTeam, directTeam], centerTitle: "Total".localized() + "\n\(self.totalTeamCount)")
            
        }else {// if team not available show static gray chart that indicates Team is empty
            let dummyCount1: RKPieChartItem = RKPieChartItem(ratio: 100, color: UIColor.lightGray, title: nil)
            let dummyCount2: RKPieChartItem = RKPieChartItem(ratio: 1, color: UIColor.lightGray, title: nil)
            
            self.chartView = RKPieChartView(items: [dummyCount2, dummyCount1], centerTitle: "Total".localized() + "\n\(0)")
        }
        
        self.chartView.circleColor          = .clear
        self.chartView.arcWidth             = 12
        self.chartView.isIntensityActivated = false
        self.chartView.style                = .butt
        self.chartView.isTitleViewHidden    = true
        self.chartView.isAnimationActivated = true
        self.chartView.tintColor            = UIColor.lightGray
        self.chartView.centerTitleSize      = 15
        self.chartView.centerTitleColor     = UIColor.darkGray
        self.chartView.frame                = self.pieChartView.frame
        
        self.teamGraphView.addSubview(chartView)
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func inviteButtonTapped(_ sender: UIButton) {
        
        if (self.appleAppLink != "") && (self.androidAppLink != "") {
            self.shareReferral()
        }else {
            
        }
    }
    
    /**
        Share Referral to invite friends to join team
    **/
    func shareReferral() {
        
        let image:UIImage = #imageLiteral(resourceName: "AppIconSquare")
        let textMessage:String = "Hey!\n\nJoin our international Community and my team with Code '\(self.referralCodeLabel.text!)'!\n\nLove Push - Connect the World\n\n\nDownload from appstore:-  \(self.appleAppLink)\n\nDownload from playstore:- \(self.androidAppLink)"
        
        let font = UIFont.systemFont(ofSize: 17)
        
        let boldFont = UIFont.boldSystemFont(ofSize: 17)
        
        let finalMessage = self.addBoldText(fullString: textMessage as NSString, boldPartOfString: "\(self.referralCodeLabel.text!)" as NSString, font: font, boldFont: boldFont)
        
        let shareAll = [finalMessage, /*appStore!, playStoreAppLink!,*/ image] as [Any]
        
        let activityViewController = UIActivityViewController(activityItems: shareAll, applicationActivities: nil)
        activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
        
        // exclude some activity types from the list (optional)
        activityViewController.excludedActivityTypes = [ UIActivityType.airDrop, UIActivityType.postToFacebook ]
        
        // present the view controller
        self.present(activityViewController, animated: true, completion: nil)
    }
    
    //Add Bold Text to Code to refer to friend
    func addBoldText(fullString: NSString, boldPartOfString: NSString, font: UIFont!, boldFont: UIFont!) -> NSAttributedString {
        let nonBoldFontAttribute = [NSAttributedString.Key.font:font!]
        let boldFontAttribute = [NSAttributedString.Key.font:boldFont!]
        let boldString = NSMutableAttributedString(string: fullString as String, attributes:nonBoldFontAttribute)
        boldString.addAttributes(boldFontAttribute, range: fullString.range(of: boldPartOfString as String))
        return boldString
    }
    
    
    
    //MARK:- API
    
    //MARK:- Get  App Link from Server to share with friends
    func getShareAppLink() {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.getAppLinks, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    
                    if let appLinks = (response as! [String:Any])["app_links"] as? [[String:Any]] {
                        
                        let androidLink = appLinks.filter({$0["name"] as! String == "android"})
                        
                        if androidLink.count > 0 {
                            let dict = androidLink.first
                            
                            self.androidAppLink = dict!["value"] as? String ?? ""
                        }
                        
                        let appleLink = appLinks.filter({$0["name"] as! String == "ios"})
                        if appleLink.count > 0 {
                            let dict = appleLink.first
                            
                            self.appleAppLink = dict!["value"] as? String ?? ""
                        }
                    }
                    
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion:{ (yes) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: { (yes) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    
    //Get My Team level wise
    func getMyTeam() {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.geMyTeam, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let data = (response as! [String:Any])["data"] {
                        let myTeamData = data as? [String : Any] ?? [:]
                        let levelCount = myTeamData.count
                        if levelCount > 0 {
                            self.isTeamAwailable = true
                            let level1 = myTeamData["level1"] as? [[String:Any]] ?? []// Users Referred directly by me
                            let level2 = myTeamData["level2"] as? [[String:Any]] ?? []//Users Referred by level 1 users
                            let level3 = myTeamData["level3"] as? [[String:Any]] ?? []// Users Referred by Level 2 users
                            let level4 = myTeamData["level4"] as? [[String:Any]] ?? []// Users Referred by Level 3 users
                            
                            let level5 = myTeamData["level5"] as? [[String:Any]] ?? []// Users Referred by Level 4 users
                            
                            self.totalTeamCount = level1.count + level2.count + level3.count + level4.count + level5.count
                            
                            self.myDirectTeamCount = level1.count
                            self.setupPieChartView()
                        }else {
                            self.isTeamAwailable = false
                            self.setupPieChartView()
                        }
                        
//                        self.teamGraphView.isHidden = (myTeamData.count == 0)
//                        self.graphHieght.constant   = (myTeamData.count == 0) ? 0 : 140
                    }else {

                        self.isTeamAwailable = false
                        self.setupPieChartView()
//                        self.teamGraphView.isHidden = true
//                        self.graphHieght.constant   = 0
                    }
                    
                }else {
                    
                    self.isTeamAwailable = false
                    self.setupPieChartView()
//                    self.teamGraphView.isHidden = true
//                    self.graphHieght.constant   = 0
                }
            }else {
                
                self.isTeamAwailable = false
                self.setupPieChartView()
//                self.teamGraphView.isHidden = true
//                self.graphHieght.constant   = 0
            }
        }
    }
}
