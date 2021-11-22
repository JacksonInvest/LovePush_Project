//
//  MenuVC.swift
//  LovePush
//
//  Created by Jatinder Singh on 17/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class MenuVC: UIViewController, QMChatServiceDelegate, QMChatConnectionDelegate, QMAuthServiceDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var profileImg : UIImageView!
    @IBOutlet weak var nameLbl    : UILabel!
    @IBOutlet weak var emailLbl   : UILabel!
    @IBOutlet weak var tableView  : UITableView!
    
    //MARK:- Variable
    var selectedIndexes = NSMutableArray()
    var selectedIndex = -1
    private var observer: NSObjectProtocol?
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        self.nameLbl.text  = User.shared.username
        self.emailLbl.text = User.shared.name // Changed acc. to client
        
        self.profileImg.setImageWithoutBaseURL(User.shared.profileURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
    }
    
    //MARK:- Button Action
    
    @IBAction func searchButtonTapped(_ sender : UIButton) {
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "SearchUserViewController") as! SearchUserViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func profileButtonTapped(_ sender: UIButton) {
        let obj = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        obj.isMyProfile = true
        self.navigationController?.pushViewController(obj, animated: true)
    }
    
    @IBAction func HeaderTapped(_ sender: UIButton) {
        
         //Single selection at a time
        
        switch sender.tag {

        case 0://My Matches
            
            if self.selectedIndex != -1 {
                if self.selectedIndex != sender.tag {
                    self.selectedIndex = sender.tag
                }else {
                    self.selectedIndex = -1
                }
            }else {
                self.selectedIndex = sender.tag
            }
            self.tableView.reloadData()
            
        case 1:// Ads
            
            if self.selectedIndex != -1 {
                if self.selectedIndex != sender.tag {
                    self.selectedIndex = sender.tag
                }else {
                    self.selectedIndex = -1
                }
            }else {
                self.selectedIndex = sender.tag
            }
            self.tableView.reloadData()
            
        case 2:// Personality Matching
            if self.selectedIndex != -1 {
                if self.selectedIndex != sender.tag {
                    self.selectedIndex = sender.tag
                }else {
                    self.selectedIndex = -1
                }
            }else {
                self.selectedIndex = sender.tag
            }
            self.tableView.reloadData()
            
             break;
            
        case 3://Affiliate Center
            
            if self.selectedIndex != -1 {
                if self.selectedIndex != sender.tag {
                    self.selectedIndex = sender.tag
                }else {
                    self.selectedIndex = -1
                }
            }else {
                self.selectedIndex = sender.tag
            }
            self.tableView.reloadData()
            
            break;
            
        case 4:
            
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "SettingsID") as! SettingsVC
            self.navigationController?.pushViewController(vc, animated: true)
            
            break;
        case 5:
            //User tips
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "UserTipsViewController") as! UserTipsViewController
            vc.titleStr = "USER TIPS"
            self.navigationController?.pushViewController(vc, animated: true)
            break;
        case 6:
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "FAQViewController") as! FAQViewController
            self.navigationController?.pushViewController(vc, animated: true)
            break;
        case 7:
            self.requestToLogout()
            break;
        default:
            break;
        }
    }
}

//MARK:- TableView Delegate And Data Source
extension MenuVC : UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return menuTitleArray.count;
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if self.selectedIndex == section {  //(self.selectedIndexes.contains(section)) {
            if (section == 0) || (section == 2) {
                return 2
            }else  if (section == 3) { // Affiliate center
                return 5
            }
            return 3;
        }
        return 0;
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 70
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
       return  0.001
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! MenuTableCell

        if(indexPath.section == 0) {
            
            if(indexPath.row == 0) {
                cell.titleLbl.lzText = "Match List"
            }
            else if(indexPath.row == 1) {
                cell.titleLbl.lzText = "Match Request"
            }
        }
        
        if(indexPath.section == 1) {
            
            if(indexPath.row == 0) {
                cell.titleLbl.lzText = "Post Ad"
            }
            else if(indexPath.row == 1) {
                cell.titleLbl.lzText = "Ad Feed"
            }
            else {
                cell.titleLbl.lzText = "My Ads"
            }
        }
        
        if(indexPath.section == 2) {
            
            if(indexPath.row == 0) {
                cell.titleLbl.lzText = "Personality Test"
            }
            else if(indexPath.row == 1) {
                cell.titleLbl.lzText = "Recommended Profiles"
            }
        }
        
        if(indexPath.section == 3) {
            
            if(indexPath.row == 0) {
                cell.titleLbl.lzText = "Affiliate Overview"//"Referral Code"
            }else if(indexPath.row == 1) {
                cell.titleLbl.lzText = "My Team"
            }else if(indexPath.row == 2) {
                cell.titleLbl.text = "Commissions".localized()
            }else if(indexPath.row == 3) {
                cell.titleLbl.text = "Withdraw Settings".localized()
            }else if(indexPath.row == 4) {
                cell.titleLbl.text = "Tips & Media".localized()
            }
            
//            Referral Code, My Team, Commissions, Withdraw Settings, Links and Media, FAQ
        }
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "headerCell") as! MenuTableCell
        
        cell.titleLbl.lzText = menuTitleArray[section]
        cell.imgView.image = menuImagesArray[section]
        
        if(section == 0 || section == 1 || section == 2 || section == 3) {
            cell.downArrowImg.isHidden = false;
        }else {
            cell.downArrowImg.isHidden = true;
        }
        
        cell.mainBtn.tag = section
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(indexPath.section == 0) {
            
            switch indexPath.row {
                
            case 0:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "MatchListViewController") as! MatchListViewController
                self.navigationController?.pushViewController(vc, animated: true)
            case 1:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "MatchRequestViewController") as! MatchRequestViewController
                self.navigationController?.pushViewController(vc, animated: true)
            default:
                break;
            }
        }
        
        if(indexPath.section == 1) {
            
            switch indexPath.row {
                
            case 0:
                let vc = mainStoryboard.instantiateViewController(withIdentifier: "PostAdsID") as! PostAdsVC
                self.navigationController?.pushViewController(vc, animated: true)
            case 1:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "ADFeedID") as! ADFeedVC
                vc.isAdFeed = true
                self.navigationController?.pushViewController(vc, animated: true)
            case 2:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "ADFeedID") as! ADFeedVC
                vc.isAdFeed = false
                self.navigationController?.pushViewController(vc, animated: true)
            default:
                break;
            }
        }
        
        if(indexPath.section == 2) {
            
            switch indexPath.row {
                
            case 0:
                let vc = mainStoryboard.instantiateViewController(withIdentifier: "PersonalityTestViewController") as! PersonalityTestViewController
                self.navigationController?.pushViewController(vc, animated: true)
            case 1:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "RecommendedProfilesID") as! RecommendedProfilesVC
                self.navigationController?.pushViewController(vc, animated: true)
            default:
                break;
            }
        }
        
        if(indexPath.section == 3) {
            
            switch indexPath.row {
                
            case 0:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "ReferralCodeViewController") as! ReferralCodeViewController
                self.navigationController?.pushViewController(vc, animated: true)
                break;
            case 1:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "MyTeamViewController") as! MyTeamViewController
                self.navigationController?.pushViewController(vc, animated: true)
                break;
            case 2:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "CommisionViewController") as! CommisionViewController
                vc.titleStr = "COMMISSIONS"
                self.navigationController?.pushViewController(vc, animated: true)
                break;
            case 3:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "CommisionViewController") as! CommisionViewController
                vc.titleStr = "WITHDRAW SETTINGS"
                self.navigationController?.pushViewController(vc, animated: true)
                break;
            case 4:
                let vc = homeStoryboard.instantiateViewController(withIdentifier: "UserTipsViewController") as! UserTipsViewController
                vc.titleStr = "TIPS & MEDIA"
                self.navigationController?.pushViewController(vc, animated: true)
                break;
            default:
                break;
            }
        }
    }
}

extension MenuVC {
    
    /**
     Request Unsubscribe user from quickblox with logout in App
     **/
    func unSubscribeQBRequest() {
        
        let logoutGroup = DispatchGroup()
        
        logoutGroup.enter()
        
        let deviceIdentifier = UIDevice.current.identifierForVendor!.uuidString
        
        QBRequest.unregisterSubscription(forUniqueDeviceIdentifier: deviceIdentifier, successBlock: { (response) -> Void in
            
            print("Successfuly unsubscribed from push notifications")
            UserDefaults.standard.removeObject(forKey: "userData")
            UserDefaults.standard.removeObject(forKey: "token")
            
            logoutGroup.leave()
        }, errorBlock: { (error) -> Void in
            
            print("Push notifications unsubscribe failed")
            logoutGroup.leave()
        })
    }
    
    /**
     Request to logout user from local server
     **/
    func requestToLogout() {
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.logout, [:], header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    Singleton.shared.isMessageRead = false
                    self.unSubscribeQBRequest()
                    self.logoutFromQuickBlox()
                    self.simpleLogout()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
      Go To login screen with transition after logout
     **/
    func simpleLogout() {
        
        //        ApiHandler.sharedInstance.Logout()
        let language = Localize.currentLanguage()
        let domain = Bundle.main.bundleIdentifier!
        UserDefaults.standard.removePersistentDomain(forName: domain)
        UserDefaults.standard.synchronize()
        
        Singleton.shared.currentAddress = ""
        Singleton.shared.currentLocation = nil
        User.shared = nil
        
        Localize.setCurrentLanguage(language)
        
        let obj = mainStoryboard.instantiateViewController(withIdentifier: "LoginViewController") as! LoginViewController
        
        let naviGationController = UINavigationController.init(rootViewController: obj)
        naviGationController.isNavigationBarHidden = true
        
        UIView.transition(with: UIApplication.shared.keyWindow!, duration: 0.8, options: UIViewAnimationOptions.transitionFlipFromLeft, animations: {
            UIApplication.shared.keyWindow?.rootViewController = naviGationController
        }, completion: nil)
    }
    
    /**
        Logout from Quickblox 
     **/
    func logoutFromQuickBlox() {
        
        if !QBChat.instance.isConnected {
            
            return
        }
        
        ServicesManager.instance().lastActivityDate = nil
        ServicesManager.instance().logoutUserWithCompletion { [weak self] (boolValue) -> () in
            
            guard let strongSelf = self else { return }
            if boolValue {
                
                NotificationCenter.default.removeObserver(strongSelf)
                
                if strongSelf.observer != nil {
                    NotificationCenter.default.removeObserver(strongSelf.observer!)
                    strongSelf.observer = nil
                }
                
                ServicesManager.instance().chatService.removeDelegate(strongSelf)
                ServicesManager.instance().authService.remove(strongSelf)
                ServicesManager.instance().lastActivityDate = nil;
            }
        }
    }
}


