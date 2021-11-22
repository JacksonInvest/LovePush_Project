//
//  ReportView.swift
//  LovePush
//
//  Created by Lavi Kumar on 05/09/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

protocol ReportDelegate {
    func didSentChatReport(_ index: Int, isSuccess: Bool)
}

class ReportView: UIView, UITextViewDelegate {

    //MARK:- Outlets
    @IBOutlet weak var nameLabel           : UILabel!
    @IBOutlet weak var userImageView       : UIImageView!
    @IBOutlet weak var descriptionTextView : UITextView!
    
    //MARK:- Variables
    var delegate : ReportDelegate?
    var target   : UIViewController!
    var index    : Int = 0
    var user     : UserInfo! {
        didSet {
            self.descriptionTextView.delegate = self
            self.nameLabel.text = user.name!
            self.userImageView.setImageWithoutBaseURL(user.imageURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            if user.eventType! == "L" {
                self.userImageView.layer.borderColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0).cgColor
            }else if user.eventType! == "M" {
                self.userImageView.layer.borderColor = UIColor.darkGray.cgColor
            }else {
                self.userImageView.layer.borderColor = UIColor.blue.cgColor
            }
        }
    }
    
    
    //MARK:- Button Action
    @IBAction func cancelButtonTapped(_ sender : UIButton) {
        self.removeFromSuperview()
    }
    @IBAction func submitReportButtonTapped(_ sender : UIButton) {
        
        if self.descriptionTextView.text! == "Description".localized() {
            self.target.showAlert(title: "Love Push", message: "Please enter report description.", completion: nil)
        }else {
            
            self.target.showAlertWith(yesBtnTitle: "Yes".localized(), noBtnTitle: "No".localized(), title: "If you request to report,It will be unmatch and you can't see this user in app.".localized(), message: "Are you sure, you want request to report?") { (isYes) in
                if isYes {
                    self.requestToReport()
                }else {
                    self.removeFromSuperview()
                }
            }
        }
        
    }
    
    //MARK:- Text View Delegate
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.text! == "Description".localized() {
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "Description".localized()
            textView.textColor = UIColor.lightGray
        }
    }
    
    //MARK:- API
    func requestToReport() {
        
        let param = [
                        "user_id"          : User.shared.id!,
                        "reported_user_id" : self.user.id!,
                        "message"          : self.descriptionTextView.text!
            
                    ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.reportChat, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    self.delegate?.didSentChatReport(self.index, isSuccess: true)
                    self.removeFromSuperview()
                }else {
                    self.delegate?.didSentChatReport(self.index, isSuccess: false)
                    self.target.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.delegate?.didSentChatReport(self.index, isSuccess: false)
                self.target.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
