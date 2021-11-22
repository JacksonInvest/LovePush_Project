//
//  ChangePasswordVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 15/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class ChangePasswordVC: UIViewController {
    
    @IBOutlet weak var oldPasswordTxt: UITextField!
    @IBOutlet weak var newPasswordTxt: UITextField!
    @IBOutlet weak var confirmTxt: UITextField!
    
    // #MARK:- View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // #MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func ChangeAction(_ sender: UIButton) {
        
        if(oldPasswordTxt.text!.checkIsEmpty() || newPasswordTxt.text!.checkIsEmpty() || confirmTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "All fields are required.", viewController: self, finished: {})
        }
        else if (newPasswordTxt.text?.isValidPassword() == false) {
            Helper.sharedInstance.ShowAlert(message: "Password length should be between 6 and 20 characters.", viewController: self, finished: {})
        }
        else if(self.newPasswordTxt.text != self.confirmTxt.text) {
            Helper.sharedInstance.ShowAlert(message: "Your password not confirmed.", viewController: self, finished: {})
        }
        else {
            self.view.endEditing(true)
            self.changePasswordRequest()
        }
    }
    
    func changePasswordRequest() {
        let parameters = ["old_password":self.oldPasswordTxt.text as AnyObject, "new_password":self.newPasswordTxt.text as AnyObject] as [String : AnyObject]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.changePassword, params: parameters, headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (yes) in
                        if yes {
                            self.navigationController?.popViewController(animated: true)
                        }
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
