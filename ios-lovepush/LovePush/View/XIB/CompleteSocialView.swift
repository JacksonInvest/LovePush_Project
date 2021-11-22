//
//  CompleteSocialView.swift
//  LovePush
//
//  Created by Lavi Kumar on 08/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

protocol AddProfileToSocialDelegate {
    /**
     Submit user data to add in profile info
     - parameter userData: User Pending info to be addedd
     **/
    func didSubmitProfile(_ userData: [String:Any]?)
}

class CompleteSocialView: UIView {
    
    //MARK:- Outlet
    @IBOutlet weak var userNameTextField    : UITextField!
    @IBOutlet weak var phoneNumberTextField : UITextField!
    @IBOutlet weak var emailTextField       : UITextField!
    @IBOutlet weak var referralTextField    : UITextField!
    @IBOutlet weak var emailSuperView       : UIView!
    @IBOutlet weak var referralView         : UIView!
    @IBOutlet weak var phoneCodeLabel       : UIButton!
    @IBOutlet weak var maleButton           : UIButton!
    @IBOutlet weak var femaleButton         : UIButton!
    @IBOutlet weak var transgenderButton    : UIButton!
    @IBOutlet weak var emailTopSpace        : NSLayoutConstraint!
    @IBOutlet weak var emailHeight          : NSLayoutConstraint!
    
    
    //MARK:- Variable
    var target : UIViewController!
    var gender = -1
    var delegate : AddProfileToSocialDelegate?
    var userData : [String:Any]?
    
    //MARK:- Button Action
    @IBAction func closeButtonTapped(_ sender: UIButton) {
        self.removeFromSuperview()
    }
    
    @IBAction func femaleButtonTapped(_ sender: UIButton) {
            self.femaleButton.setImage(UIImage(named: "radioOn"), for: .normal)
            self.maleButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.gender = 2
    }
    
    @IBAction func maleButtonTapped(_ sender: UIButton) {
            self.maleButton.setImage(UIImage(named: "radioOn"), for: .normal)
            self.femaleButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.gender = 1
    }
    
    @IBAction func transgenderButtonTapped(_ sender: UIButton) {
            self.maleButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.femaleButton.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOn"), for: .normal)
            self.gender = 3
    }
    
    @IBAction func continueButtonTapped(_ sender: UIButton) {
        if (self.userNameTextField.text?.isEmpty)! {
            self.target.showAlert(title: "Love Push", message: "Please enter your Username.", completion: nil)
        } else if userNameTextField.text!.count < 3 || userNameTextField.text!.count > 20 {
            self.target.showAlert(title: "Love Push", message: "Username length should be minimum 3 and maximum 20 characters.", completion: nil)
//        }else if(phoneNumberTextField.text!.checkIsEmpty()) {
//            self.target.showAlert(title: "Love Push", message: "Please enter your phone number.", completion: nil)
//        }else if ((self.phoneNumberTextField.text?.count)! < 10) || ((self.phoneNumberTextField.text?.count)! > 15) {
//           self.target.showAlert(title: "Love Push", message: "Phone number should be of 10 to 15 digits.", completion: nil)
        }else if (self.emailTextField.text?.isEmpty)! {
            self.target.showAlert(title: "Love Push", message: "Please Enter Email Address.", completion: nil)
            
        }else if self.gender == -1 {
            self.target.showAlert(title: "Love Push", message: "Please Select Gender", completion: nil)
        }else {
            userData!["email"] = self.emailTextField.text!
            userData!["name"] = userData!["name"] as? String ?? ""
            userData!["username"] = self.userNameTextField.text!
            userData!["gender"] = self.gender
            self.delegate!.didSubmitProfile(userData)
        }
    }
}
