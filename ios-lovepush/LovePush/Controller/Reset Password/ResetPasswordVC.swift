//
//  ResetPasswordVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class ResetPasswordVC: UIViewController {

    @IBOutlet weak var passwordTxt: UITextField!
    @IBOutlet weak var confirmPasswordTxt: UITextField!
    
    var detailDict = [String:Any]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
    }
    
    @IBAction func ResetAction(_ sender: UIButton) {
        
        if(self.passwordTxt.text!.checkIsEmpty() || self.confirmPasswordTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "All fields are required.", viewController: self, finished: {})
        }
        else if (passwordTxt.text?.isValidPassword() == false) {
            Helper.sharedInstance.ShowAlert(message: "Password length should be between 6 and 20 characters.", viewController: self, finished: {})
        }
        else if (passwordTxt.text != confirmPasswordTxt.text) {
            Helper.sharedInstance.ShowAlert(message: "Your password not confirmed.", viewController: self, finished: {})
        }
        else {
            self.view.endEditing(true)
            
            let parameters = ["user_id":self.detailDict["id"] as! Int,"password":self.passwordTxt.text!,"password_confirmation":self.confirmPasswordTxt.text!] as [String : AnyObject]

//            ApiHandler.sharedInstance.ResetPassword(parameters: parameters, viewCon: self, completionHandler: { (result) in
//
//                Helper.sharedInstance.ShowAlert(message: result.value(forKey: messageKey) as! String, viewController: self, finished: {
//                    self.navigationController?.popToRootViewController(animated: false)
//                })
//            })
        }
    }
    
}
