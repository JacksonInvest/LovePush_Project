//
//  Register+API.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/04/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit

extension RegisterVC {
    
    /**
     Validate All Data field entries
     **/
    func isValidAllField()-> Bool {
        
        var gender = 0
        if (self.maleBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Male.rawValue
        }else if (self.femaleBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Female.rawValue
        }else if (self.transBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Trans.rawValue
        }
        
        if(userNameTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "Please enter your Username.", viewController: self, finished: {})
        }else if userNameTxt.text!.count < 3 || userNameTxt.text!.count > 20 {
            Helper.sharedInstance.ShowAlert(message: "Username length should be minimum 3 and maximum 20 characters.", viewController: self, finished: {})
        }else if(emailTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "Please enter your Email.", viewController: self, finished: {})
        }else if(Helper.sharedInstance.isValidEmail(testStr: self.emailTxt.text!) == false) {
            Helper.sharedInstance.ShowAlert(message: "Please enter valid Email.", viewController: self, finished: {})
        }else if (gender == 0) || ((self.femaleBtn.currentImage == UIImage(named: "radioOff")) &&  (self.transBtn.currentImage == UIImage(named: "radioOff")) && (self.maleBtn.currentImage == UIImage(named: "radioOff"))) {
            self.showAlert(title: "Love Push", message: "Please Select Gender", completion: nil)
        }else if(passwordTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "Please enter your Password.", viewController: self, finished: {})
        }else if (passwordTxt.text?.isValidPassword() == false) {
            Helper.sharedInstance.ShowAlert(message: "Password length should be between 6 and 20 characters.", viewController: self, finished: {})
        }else if ((confirmPasswordTxt.text?.checkIsEmpty())!) {
            Helper.sharedInstance.ShowAlert(message: "Please enter confirm password.", viewController: self, finished: {})
        }else if(self.passwordTxt.text != self.confirmPasswordTxt.text) {
            Helper.sharedInstance.ShowAlert(message: "Your password not confirmed.", viewController: self, finished: {})
        }else if !self.isAgreeTerms {
            Helper.sharedInstance.ShowAlert(message: "Please accept terms & conditions and Legacy.", viewController: self, finished: {})
        } else {
            return true
        }
        return false
    }
    
    /**
     Create param to register user with manual data entry
     **/
    func createParams()-> [String: Any] {
        
        self.view.endEditing(true)
        
        var gender = Gender.Male.rawValue
        if (self.femaleBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Female.rawValue
        }else if (self.transBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Trans.rawValue
        }

        let parameters: [String: Any] = [
                            "username":self.userNameTxt.text!,
                            "email":self.emailTxt.text!,
                            "phone":self.phoneTxt.text!,
                            "phone_code":self.codeBtn.currentTitle!,
//                            "address":"",
//                            "city":self.cityName,
//                            "country":self.countryName,
                            "gender": gender,
                            "password":self.passwordTxt.text!,
                            "password_confirmation":self.confirmPasswordTxt.text!,
                            "device_type": 2,
//                            "latitude":self.selectedPlaceCoordinates.latitude,
//                            "longitude":self.selectedPlaceCoordinates.longitude,
                            "login_type": 1,
//                            "referalBy" : self.referralTxt.text!
//            "name":"ASDWQDF",
//            "username":"RGHRFGH",
//            "email":"RTHRTHR@mailinator.com",
//            "password":"123456",
//            "phone":"",
//            "address":"Chandigarh",
//            //latitude:28.4456789
//            //longitude:36.1123456
//            "gender":"1",
//            "password_confirmation":"123456",
//            "device_type":"2",
//            //country:India
//            //city:chandigarh
//            "phone_code":"+1",
//            "login_type":"1"
//
                        ] as [String : Any]
        
        return parameters
    }
    
    /**
     Register with with simple email or user name and password
     - parameter socialUser:- Social Logged in user info
     **/
    func registerWithEmail() {
       
       let params = self.createParams()
        
        print(params)
        
        let header = ["locale": Singleton.shared.language,
                      "Content-Type": "application/json"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.register, params: params, headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let userData = (response as! [String:Any])["data"] as? [String:Any] {
                        User(userData)
                        UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                        let vc = mainStoryboard.instantiateViewController(withIdentifier: "VerifyOtpID") as! VerifyOtpVC
                        vc.detailDict = userData
                        vc.isFromLogin = false;
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
    
    /**
     Register with Social Media
     - parameter socialUser:- Social Logged in user info
     **/
    func loginWithSocialMedial(_ socialUser: [String:Any]) {
        let param = self.createParam(socialUser)
        
        let header = ["locale": Singleton.shared.language]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.socialLogin, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        if (userData["is_profile_complete"] as! Bool == true) {
                            UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                            self.loginSetup(userData)
                        }else {
                            let gender   = userData["gender"]
                            let userName = userData["username"]
                            let email    = userData["email"] as? String ?? ""
                            User(userData)
                            if gender is NSNull || userName is NSNull {
                                self.completeSocialProfilePopup(email, userData: userData)
                            }else {
                                UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                                self.loginSetup(userData)
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
    
    
    func completeSocialProfile() {
        
        let parameters = [
            "user_id": User.shared.id!,
            "username":self.completeWithSocialView.userNameTextField.text!,
            "phone": self.completeWithSocialView.phoneNumberTextField.text!,
            "gender": "\(self.completeWithSocialView.gender)",
            "referalBy" : self.completeWithSocialView.referralTextField.text!,
            "device_type":"2",
            "phone_code": (self.completeWithSocialView.phoneNumberTextField.text?.isEmpty)! ? "" : self.completeWithSocialView.phoneCodeLabel.currentTitle!,
            "login_type":"\(self.loginType)",
            "email" : self.completeWithSocialView.emailTextField.text!
            ] as [String : Any]
        
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
                        self.loginSetup(userData)
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
     Create param to Add Profile info with social media login
     - parameter socialUser:- Social user info
     **/
    func createParam(_ socialUser: [String:Any])-> [String:Any] {
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
            param["gender"] = gender
        }
        
        if let userName = socialUser["username"] as? Int {
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
     Show Add Profile info with social media login
     - parameter userData:- Social user info
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
            vc.isFromLogin = false;
            self.navigationController?.pushViewController(vc, animated: true)
        }else if(userData["is_profile_complete"] as! Bool == false) {//Check all type login but profile completed or not
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "CreateProfileID") as! CreateProfileVC
            self.navigationController?.pushViewController(vc, animated: true)
        } else {// All Registration process completed
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    
    @objc func phoneCodePickButtonTapped(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "SRCountryPickerControllerID") as! SRCountryPickerController
        vc.countryDelegate = self;
        self.present(vc, animated: true, completion: nil)
    }
    
    //MARK:- Instagram Login
    /**
     Fetch Insta User info
     - parameter accessToken: Access Token to redirect and get user info
     - parameter completion: Completion with successful user data recieved
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
     Fetch Insta User info
     - parameter instagramUSer: Insta User info to login in local server
     **/
    func loginWithInstagram(_ instagramUser: [String:Any]) {
        let param = self.createParam(instagramUser)
        
        let header = ["locale": Singleton.shared.language]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.instagramLogin, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let userData = (response as! [String: Any])["data"] as? [String:Any] {
                        
                        let gender = userData["gender"]
                        let userName = userData["username"]
                        let email = userData["email"] as? String ?? ""
                        User(userData)
                        
//                        self.completeSocialProfile()
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
extension RegisterVC : AddProfileToSocialDelegate {
    
    func didSubmitProfile(_ userData: [String : Any]?) {
        if self.completeWithSocialView.emailTextField.isEmpty {
            self.showAlert(title: "Love Push", message: "Please Enter Email Address", completion: nil)
        }else {
            
//            if self.loginType == 4 {
//                self.loginWithInstagram(userData!)
//            }else {
                self.completeSocialProfile()
//            }
            //            self.completeSocialProfile()
        }
    }
    

}

