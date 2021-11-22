//
//  Login+API.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/04/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit
import QMServices

extension LoginViewController {
//    https://www.love-push.com/api/isSocialUser
    
    /**
     Reguest reset password, A link will be send to user Email
     **/
    func requestForgotPassword() {
        let params = [
                        "email" : self.userNameTxt.text!
                     ]
        print(params)
        
        let header = ["locale": Singleton.shared.language]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.forgotPassword, params: params as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Reguest to login user with user name and password
     **/
    func requestToLogin() {
        let parameters = [
            "email"   : self.userNameTxt.text!,
            "password": self.passwordTxt.text!,
            "device_type" : 2,
            "device_token" : (Singleton.shared.deviceToken != nil) ?  Singleton.shared.deviceToken!.hexString : ""
            ] as [String : Any]
        let header = ["locale": Singleton.shared.language]
        
        print(parameters)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.login, params: parameters as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                        
                       self.loginSetup(userData)// Login Setup to redirect to Page  based ton registration status
                        
                        if Singleton.shared.currentAddress != "" {// Save User Current Location When App Launch and logged in
                            (UIApplication.shared.delegate as! AppDelegate).updateLocation(Singleton.shared.currentAddress)
                        }
                        
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
    
    /**
     Reguest to login user with social media
     - parameter socialUser: User Info from Social Login
     **/
    func loginWithSocialMedial(_ socialUser: [String:Any]) {
        
        let param = self.createParam(socialUser)
        
        let header = ["locale": Singleton.shared.language]
        
        print(param)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.socialLogin, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        if(userData["is_profile_complete"] as! Bool == true) {
                            UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                            self.loginSetup(userData)// Login Setup to redirect to Page  based ton registration status
                        }else {
                            let gender   = userData["gender"]
                            let userName = userData["username"]
                            let email    = userData["email"] as? String ?? ""
                            User(userData)
                            if gender is NSNull || userName is NSNull {// Gender or User Name is mendatory
                                self.completeSocialProfilePopup(email, userData: userData)
                            }else {
                                UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                                self.loginSetup(userData)// Login Setup to redirect to Page  based ton registration status
                            }
                        }
                        
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
    
    /**
     Reguest to add User info if Gender or USer name not exist with social media login
     **/
    func completeSocialProfile() {
        
        var parameters = [
                            "user_id": User.shared.id!,
                            "username":self.completeWithSocialView.userNameTextField.text!,
                            "phone": self.completeWithSocialView.phoneNumberTextField.text!,
                            "gender": "\(self.completeWithSocialView.gender)",
                            "referalBy" : self.completeWithSocialView.referralTextField.text!,
                            "device_type":"2",
                            "phone_code": (self.completeWithSocialView.phoneNumberTextField.text?.isEmpty)! ? "" : self.completeWithSocialView.phoneCodeLabel.currentTitle!,
                            "login_type":"\(self.loginType)",
                            "device_token" : (Singleton.shared.deviceToken != nil) ?  Singleton.shared.deviceToken!.hexString : "",
                            "email" : self.completeWithSocialView.emailTextField.text!
            ] as [String : Any]
        
        if self.loginType == 4 {
            parameters["email"] = self.completeWithSocialView.emailTextField.text!
        }
        
        let header = ["locale": Singleton.shared.language]
        
        print(parameters)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.addUserInfo, params: parameters as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                        self.completeWithSocialView.removeFromSuperview()
                        self.loginSetup(userData)// Login Setup to redirect to Page  based ton registration status
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
    
    /**
     Create Parameters for social media login
     - parameter socialUser: User Info from Social Login
     **/
    func createParam(_ socialUser: [String:Any])-> [String:Any] {
        
        print(socialUser)
        
        var param = [
                        "name": socialUser["name"] as? String ?? "",
                        "device_type":2,
                        "login_type":self.loginType,
                        "social_id": socialUser["id"] as? String ?? ""
                        ] as [String : Any]
        if let email = socialUser["email"] as? String {
            param["email"] = email
        }
        if let gender = socialUser["gender"] as? Int {
            param["gender"] = "\(gender)"
        }
        
        if let gender = socialUser["gender"] as? String {
            param["gender"] = gender
        }
        
        if let userName = socialUser["username"] as? String {
            param["username"] = userName
        }
        
        return param
    }
    
    /**
     Show Add Profile info with social media login
     - parameter email:- User Info from Social Login email already exist
     - parameter userData:- Social user info
     **/
    func completeSocialProfilePopup(_ email: String, userData: [String : Any]?) {
        self.completeWithSocialView = Bundle.main.loadNibNamed("CompleteSocialView", owner: self, options: nil)?.first as? CompleteSocialView
        self.completeWithSocialView.frame = self.view.frame
        self.completeWithSocialView.target = self
        self.completeWithSocialView.delegate = self
        self.completeWithSocialView.referralView.addDashedBorder()
        
        self.completeWithSocialView.emailTextField.text = email
 
        self.completeWithSocialView.emailTextField.isEnabled = (email == "")
        
        self.completeWithSocialView.userData = userData
        self.completeWithSocialView.phoneCodeLabel.addTarget(self, action: #selector(phoneCodePickButtonTapped), for: .touchUpInside)
        self.view.addSubview(self.completeWithSocialView)
    }
    
    /**
     Login user setup to redirect on page based on Profile ompletion status
     - parameter userData:- User Info from from login request
     **/
    func loginSetup(_ userData : [String:Any]) {
        User(userData)
        
        let dataSave = NSKeyedArchiver.archivedData(withRootObject: userData)
        UserDefaults.standard.set(dataSave, forKey: "userData")
        UserDefaults.standard.synchronize()
        
        UserDefaults.standard.set(User.shared.qBUserID, forKey: "userQuickbloxId")
        UserDefaults.standard.set(self.passwordTxt.text!, forKey: "userQuickbloxPassword")
        
        QuickbloxHandling.shared.quickBloxLogin(UInt(User.shared.qBUserID)!)
        
        UserDefaults.standard.set(true, forKey: "isLogin")
        
        let isEmailVerify = userData["email_verified_at"] as? String ?? ""
        let socialID = userData["social_id"] as? String
        if isEmailVerify == "" && socialID == nil {//Check Login without social media and email not verified
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "VerifyOtpID") as! VerifyOtpVC
            vc.detailDict = userData
            vc.isFromLogin = true;
            self.navigationController?.pushViewController(vc, animated: true)
        }else if(userData["is_profile_complete"] as! Bool == false) { //Check all type login but profile completed or not
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "CreateProfileID") as! CreateProfileVC
            self.navigationController?.pushViewController(vc, animated: true)
        } else {// All Registration process completed
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    /**
     Country Code Picker to add phone number
     - parameter sender:- Button Object
     **/
    @objc func phoneCodePickButtonTapped(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "SRCountryPickerControllerID") as! SRCountryPickerController
        vc.countryDelegate = self;
        self.present(vc, animated: true, completion: nil)
    }
    
    //MARK:- Instagram Login
    
    /**
     Fetch Insta User info
     - parameter accessToken:- Access Token to redirect and get user info
     - parameter completion:- Completion with successful user data recieved
     **/
    func fetchInstagramLoginDetails(_ accessToken: String,completion: @escaping(_ receivedData: [String:Any]?)-> Void) {
        
        let URl =  "https://api.instagram.com/v1/users/self/?access_token=\(accessToken)"
        
        let parameter = [ "access_token" : accessToken ]
        
        print(parameter)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostWithoutBaseURL(URl, params: parameter as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if ((response as! [String:Any])["meta"] as! [String:Any])["code"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        completion(userData)
                    }else {
                        self.showAlert(title: "Love Push", message: "Some Error Occured", completion: nil)
                        completion(nil)
                    }
                }else {
                    self.showAlert(title: "Love Push", message: "Some Error Occured", completion: nil)
                    completion(nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: "Some Error Occured", completion: nil)
                completion(nil)
            }
        }
    }
    
    /**
     Login Insta User info
     - parameter instagramUser: Insta User info to login in local server
     **/
    func loginWithInstagram(_ instagramUser: [String:Any]) {
        let param = self.createParam(instagramUser)
        
        let header = ["locale": Singleton.shared.language]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        print(param)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.instagramLogin, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        let gender = userData["gender"]
                        let userName = userData["username"]
                        let email   = userData["email"] as? String ?? ""
                        User(userData)
                        if gender is NSNull || userName is NSNull || (email == ""){
                            self.completeSocialProfilePopup(email, userData: userData)
                        }else {
                            UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                            self.loginSetup(userData)
                        }
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
}


//MARK:- Complete Social Profile Delegate
extension LoginViewController : AddProfileToSocialDelegate {
    
    func didSubmitProfile(_ userData: [String : Any]?) {
        if self.completeWithSocialView.emailTextField.isEmpty {
            self.showAlert(title: "Love Push", message: "Please Enter Email Address", completion: nil)
        }else {
//            if self.loginType == 4 {
//                self.loginWithInstagram(userData!)
//            }else {
                self.completeSocialProfile()
//            }
        }
    }
    

}

extension LoginViewController: CountrySelectedDelegate {
    
    func SRcountrySelected(countrySelected country: Country) {
        self.completeWithSocialView.phoneCodeLabel.setTitle(country.dial_code, for: .normal)
    }
}
