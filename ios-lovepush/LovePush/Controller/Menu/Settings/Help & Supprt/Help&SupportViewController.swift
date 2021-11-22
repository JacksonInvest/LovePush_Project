//
//  Help&SupportViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 21/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class Help_SupportViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var nameTextField    : UITextField!
    @IBOutlet weak var subjectTextField : UITextField!
    @IBOutlet weak var messageTextView  : UITextView!

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.nameTextField.text = User.shared.name!
    }
    
    //MARK: - Button Actions
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func submitButtonTapped(_ sender: UIButton) {
        if self.nameTextField.text!.isEmpty {
            self.showAlert(title: "Love Push", message: "Please enter name.", completion: nil)
        }else if self.subjectTextField.text!.isEmpty {
            self.showAlert(title: "Love Push", message: "Please enter subject.", completion: nil)
        }else if self.messageTextView.text!.isEmpty || self.messageTextView.text! == "Message".localized() {
            self.showAlert(title: "Love Push", message: "Please Enter the message", completion: nil)
        }else {
            self.sendHelpSupportRequest()
        }
    }
}

extension Help_SupportViewController : UITextViewDelegate {
   
    func textViewDidBeginEditing(_ textView: UITextView) {
        if !textView.text.isEmpty {
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "Message".localized()
            textView.textColor = UIColor.lightGray
        }
    }
}


extension Help_SupportViewController {
    
    /**
     Send Request for Help And Support
     - returns: nil
     **/
    func sendHelpSupportRequest() {
        let param = [
                        "name"    : self.nameTextField.text!,
                        "subject" : self.subjectTextField.text!,
                        "message" : self.messageTextView.text!
                    ]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.support, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                   
                    self.showAlert(title: "Love Push", message: "Request Sent Successful.", completion: { (isOk) in
                        self.navigationController?.popViewController(animated: true)
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
