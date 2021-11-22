//
//  EnterPhoneVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class EnterPhoneVC: UIViewController {

    @IBOutlet weak var codeBtn: UIButton!
    @IBOutlet weak var phoneTxt: UITextField!
    var selectedCode = "+1"
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    @IBAction func SelectCountryAction(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "SRCountryPickerControllerID") as! SRCountryPickerController
        vc.countryDelegate = self;
        self.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func BackAction(_ sender: UIButton) {
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func SubmitAction(_ sender: UIButton) {
        
        if(phoneTxt.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "Please enter your Phone Number.", viewController: self, finished: {})
        }
        else {
            self.view.endEditing(true)
            
            let code = self.selectedCode
            let phone = self.phoneTxt.text!
            
            let parameters = ["phone_code":code, "phone": phone] as [String : AnyObject]
            
//            ApiHandler.sharedInstance.ForgotPassword(parameters: parameters, viewCon: self, completionHandler: { (result) in
//
//                Helper.sharedInstance.ShowAlert(message: result.value(forKey: messageKey) as! String, viewController: self, finished: {
//
//                    let vc = mainStoryboard.instantiateViewController(withIdentifier: "VerifyOtpID") as! VerifyOtpVC
//                    vc.detailDict = result
//                    self.dismiss(animated: false, completion: nil)
//                })
//
//            })
        }
    }
    
}

extension EnterPhoneVC: CountrySelectedDelegate {
    func SRcountrySelected(countrySelected country: Country) {
        self.codeBtn.setTitle("\(country.country_code) (\(country.dial_code))", for: .normal)
        self.selectedCode = country.dial_code
    }
}
