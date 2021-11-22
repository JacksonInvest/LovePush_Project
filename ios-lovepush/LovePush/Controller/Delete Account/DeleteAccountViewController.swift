//
//  DeleteAccountViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 21/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class DeleteAccountViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var optionTableView: UITableView!
    @IBOutlet weak var descTextView: UITextView!
    
    
    //MARK:- Variable
    var answerArr = ["Freezing",
                     "Bad User Experience"
        , "Intrusive ads"
        ,"Privacy concerns"
        ,"Forced social logins"
        ,"Disinterest"
        ,"Little activity in my area"
        ,"Other"]
    
    var selectedArr = [String]()

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    //MARK:- Button Action
    @IBAction func confirmButtonTapped(_ sender : UIButton) {
        
        if self.selectedArr.count == 0 {
            self.showAlert(title: "Love Push", message: "Please select answer option.", completion: nil)
        }else{
            self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "Love Push", message: "Are you sure, you want to delete your account?") { (isYesTapped) in
                if isYesTapped {
//                    self.deleteFromQuickBlox()
                    self.requestToDeleteAccount()
                }
            }
        }
        
    }
    @IBAction func cancelButtonTapped(_ sender : UIButton) {
        self.dismiss(animated: false, completion: nil)
    }
}

extension DeleteAccountViewController : UITextViewDelegate {
    //MARK:- Text View Delegate
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.text! == "Enter description here".localized() {
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "Enter description here".localized()
            textView.textColor = UIColor.lightGray
        }
    }
}

//MARK:- Delegate
extension DeleteAccountViewController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.answerArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LoveIntentionCreateProfileCell", for: indexPath) as! LoveIntentionTableViewCell
        cell.cPLovePushTitleLabel.text = self.answerArr[indexPath.row].localized()
        if self.selectedArr.lastIndex(of: self.answerArr[indexPath.row].localized()) != nil {
            cell.cPCheckIcon.image = #imageLiteral(resourceName: "checked_blue")
        }else {
            cell.cPCheckIcon.image = #imageLiteral(resourceName: "uncheck")
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let index = self.selectedArr.lastIndex(of: self.answerArr[indexPath.row].localized()) {
            self.selectedArr.remove(at: index)
        }else {
            self.selectedArr.append(self.answerArr[indexPath.row].localized())
        }
        
        tableView.reloadData()
    }
}


extension DeleteAccountViewController {
    func requestToDeleteAccount() {
        let languageID = (Singleton.shared.language == "en") ? 1 : 2
        let param = [
            "lang_id" : languageID,
            "reasons" : self.selectedArr.joined(separator: ","),
            "description" : self.descTextView.text!
            
            ] as [String : Any]
        
        print(param)
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.deleteAccount, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    //                    self.unSubscribeQBRequest()
//                    self.deleteFromQuickBlox()
                    self.simpleDelete()
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    func simpleDelete() {
        
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
    
    func deleteFromQuickBlox() {
        
        QBRequest.deleteCurrentUser(successBlock: { (response) in
            print(response)
        }) { (errorBlock) in
            print(errorBlock)
        }
    }
}
