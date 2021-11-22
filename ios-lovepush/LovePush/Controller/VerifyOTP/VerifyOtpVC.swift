//
//  VerifyOtpVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class VerifyOtpVC: UIViewController {
    
    //MARK:- Outlet
    @IBOutlet weak var descriptionLbl: UILabel!
    @IBOutlet weak var otpView: KWVerificationCodeView!
    
    //MARK:- Variable
    var detailDict = [String:Any]()
    var isFromLogin = Bool()
    
    //MARK:- Controller LifeCycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        otpView.delegate = self;
        
        self.descriptionLbl.text = "We have sent an OTP to your email and also check spam folder."
        
        if(self.isFromLogin) {
            self.ResendOTP()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK:- Button Action
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func ResendAction(_ sender: UIButton) {
        self.otpView.clear()
        self.ResendOTP()
    }
    
    @IBAction func ChangeNumberAction(_ sender: UIButton) {
        
    }
    
    //MARK:- Resend OTP if not get on mail
    func ResendOTP() {
        
        let parameters = [
                            "email" : self.detailDict["email"] as! String
                         ] as [String : AnyObject]
        let header = ["locale": Singleton.shared.language]
        
        print(parameters)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.sendOTP, params: parameters, headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 400 {
                    self.showAlert(title: "Love Push", message: "Please enter verification code: \(String(describing: (response as! [String:Any])["verification_code"]!))", completion: nil)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}

extension VerifyOtpVC : KWVerificationCodeViewDelegate {
    //When fill or change otp digits field
    func didChangeVerificationCode() {
        
        var code = self.otpView.getVerificationCode()
        code = code.replacingOccurrences(of: " ", with: "")
        if(code.count == 4) { // We set number of OTP Digits 4
            
            self.view.endEditing(true)
            
            let userid = self.detailDict["id"] as! Int
            
            let parameters = ["user_id":userid, "verification_code": self.otpView.getVerificationCode() as AnyObject] as [String : AnyObject]
            let header = ["locale": Singleton.shared.language]
            
            print(parameters)
            
            Helper.sharedInstance.ShowLoaderOnView(view: self.view)
            
            WebAPI.requestToPOSTWithHeader(URLConstant.verifyOTP, params: parameters, headers: header as [String : AnyObject]) { (response, isSuccess) in
                
                Helper.sharedInstance.HideLoaderFromView(view: self.view)
                
                print(response)
                if isSuccess {
                    if (response as! [String:Any])["status"] as! Int == 200 {
//                        if !(self.isFromLogin) {
                            if let userData = (response as! [String:Any])["data"] as? [String:Any] {
                                User(userData)
                                let dataSave = NSKeyedArchiver.archivedData(withRootObject: userData)
                                UserDefaults.standard.set(dataSave, forKey: "userData")
                                UserDefaults.standard.synchronize()
                                let vc = mainStoryboard.instantiateViewController(withIdentifier: "CreateProfileID") as! CreateProfileVC
                                self.navigationController?.pushViewController(vc, animated: true)
                            }
//                        }else {
//                            let vc = mainStoryboard.instantiateViewController(withIdentifier: "ResetPasswordID") as! ResetPasswordVC
//                            vc.detailDict = self.detailDict
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
                    }else {
                        self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                }
            }
        }
    }
}
