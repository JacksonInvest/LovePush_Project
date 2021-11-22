//
//  PostAdd + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 03/07/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation


extension PostAdsVC {
    
    
    func isValid()-> Bool {
        if(titleTxt.text!.checkIsEmpty()) && !self.isHome{
            Helper.sharedInstance.ShowAlert(message: "Please add Title.", viewController: self, finished: {})
            return false
        }else if(self.descriptionTxtView.text! == "Description".localized()) && !self.isHome {
            Helper.sharedInstance.ShowAlert(message: "Please enter description.", viewController: self, finished: {})
            return false
//        }else if(self.descriptionTxtView.text.removeSpaces().count <= 20) {
//            Helper.sharedInstance.ShowAlert(message: "Please add description of at least 20 characters.", viewController: self, finished: {})
//            return false
        }else if (self.connectAdBtn.currentImage == UIImage(named: "radioOff")) && (self.loveAdBtn.currentImage == UIImage(named: "radioOff")) && !self.isHome {
            Helper.sharedInstance.ShowAlert(message: "Please select post type.", viewController: self, finished: {})
            return false
            
        }else if(self.imgView.image == nil)  && self.isHome {
            Helper.sharedInstance.ShowAlert(message: "Please add image.", viewController: self, finished: {})
            return false
        }else if(self.locationLabel.text == "Select Location".localized()) && !self.isHome {
            Helper.sharedInstance.ShowAlert(message: "Please select location.", viewController: self, finished: {})
            return false
        }
        return true
    }
    
    
    func createParam()-> [String:String] {
        
        var addType = AdType.Love.rawValue
        if(self.connectAdBtn.currentImage == UIImage(named: "radioOn")) {
            addType = AdType.Connect.rawValue
        }
        
        var isAnonymously = false
        if(self.anonymouslySwitch.isOn) {
            isAnonymously = true
        }
        
        let parameters = [
                            "title"                 : self.titleTxt.text!,
                            "description"           : self.descriptionTxtView.text!,
                            "post_type"             : "\(addType)",
                            "location"              : self.locationLabel.text!,
                            "latitude"              : "\(self.selectedLocation.latitude)",
                            "longitude"             : "\(self.selectedLocation.longitude)",
                            "is_post_anonymously"   : "\(isAnonymously ? 1 : 0)",
                            "radius"                : "\(self.radiusSlider.value)"
                         ]
        
        return parameters
    }
    
    func requestToAddOrUpdatePost(_ id: Int?) {
        var param = self.createParam()
        if id != nil {
            param["id"] = "\(id!)"
        }
        
        var imgData : Data?
        
        imgData = (self.isImageSelected) ? UIImagePNGRepresentation(self.imgView.image!) : nil
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        print(param)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        var url = ""
        if self.isHome {
             url = (id == nil) ? URLConstant.addPost : URLConstant.editPost
        }else {
             url = (id == nil) ? URLConstant.addPostAds : URLConstant.editPostAds
        }
       
        WebAPI.requestToPostWithData(url, params: param , header: header, fileData: imgData, fileKey: "image") { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (isYes) in
                        
                        NotificationCenter.default.post(name: Notification.Name(rawValue: "Reload Post"), object: nil, userInfo: nil)
                        
                        if !self.isForUpdate {
                            self.navigationController?.popViewController(animated: true)
                        }else {
                            self.popBack(3)
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
    
    func popBack(_ nb: Int) {
        if let viewControllers: [UIViewController] = self.navigationController?.viewControllers {
            guard viewControllers.count < nb else {
                self.navigationController?.popToViewController(viewControllers[viewControllers.count - nb], animated: true)
                return
            }
        }
    }
}
