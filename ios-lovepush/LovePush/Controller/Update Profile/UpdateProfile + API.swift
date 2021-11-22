//
//  UpdateProfile + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/05/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//
//https://i.diawi.com/8nKtnT
import Foundation
import UIKit

extension UpdateProfileVC {
    
    //Create Parameter to set updated data in user profile
    func createParam()-> [String:String] {
        
        var gender = Gender.Male.rawValue
        if(self.femaleBtn.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Female.rawValue
        }else if (self.transgenderButton.currentImage == UIImage(named: "radioOn")) {
            gender = Gender.Trans.rawValue
        }
        
        var sexualOrientation = 0
        if(self.heterosexualBtn.currentImage == UIImage(named: "radioOn")) {
            sexualOrientation = SexualOrientation.Hetero.rawValue
        }else if(self.bisexualBtn.currentImage == UIImage(named: "radioOn")) {
            sexualOrientation = SexualOrientation.Bi.rawValue
        }else {
            sexualOrientation = SexualOrientation.Homo.rawValue
        }
        
        let parameters:[String:String] = [
                                          "name"                :self.nameTxt.text!,
                                          "age"                 :self.ageTextField.text!,
                                          "language"            :self.languagesTxt.text!,
                                          "size"                :self.sizeTxt.text!,
                                          "education"           :self.educationTxt.text!,//Favorite Trevel Destination
                                          "job"                 :self.jobTxt.text!,
                                          "hobbies"             :self.hobbiesTxt.text!,
                                          "religion"            :self.religionTextField.text!,
                                          "gender"              :"\(gender)",
                                          "physique"            :self.selectdMusicArr.joined(separator: ","),//self.physiqueID == 0 ? "" : "\(self.physiqueID)",
                                          "aboutme"             :self.aboutMeTxt.text!.replacingOccurrences(of: "About me".localized(), with: ""),
                                          "user_id"             :"\(Helper.sharedInstance.userId())",
                                          "relationship"        :self.relationshipID == 0 ? "" :"\(self.relationshipID)",
                                          "sexual_orientation"  :"\(sexualOrientation)", "love_push_intention" :self.loveIntensionArr.joined(separator: ",")
                                        ]
        
        return parameters
    }
    
    /**
     Request to update user profile to change user new information
    **/
    func updateProfile() {
        
        let param = createParam()
        
        let imgData = UIImagePNGRepresentation(self.profileImg.image!)
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostWithData(URLConstant.updateProfile, params: param, header: header, fileData: imgData, fileKey: "profile_image") { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
//                    QuickbloxHandling.shared.updateQBProfile(self.profileImg.image!, name: self.nameTxt.text!)
//                    if self.activeIndex == -1 {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (ok) in
                        self.navigationController?.popViewController(animated: true)
                    })
//                    }else {
//                        self.activeIndex = -1
//                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }

    /**
     Add image to user images list, And First image will always be automatic set to User main profile image
    **/
    func addImages() {
        
        let imgDataArr = self.getImagesDataArr()
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostMultiImagesWithHeader(URLConstant.addImages, params: [:], header: header, fileData: imgDataArr, fileKey: "images") { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    self.manageImageCollection()
                    QuickbloxHandling.shared.updateQBProfile(self.profileImg.image!, name: self.nameTxt.text!)
                    if let updatedProfileData = (response as! [String:Any])["data"] as? [String:Any] {
                        self.detailDict.addEntries(from: updatedProfileData as [AnyHashable : Any])
                        self.imageDataSetup()
                    }
//                    self.navigationController?.popViewController(animated: true)
                }else {
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
//                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     All new images data array to send in Add image API
    **/
    func getImagesDataArr()-> [Data] {
        var finalImageDataArr = [Data]()
        for image in self.newAddedImages {
            if let data = UIImageJPEGRepresentation(image, 1.0) {
                finalImageDataArr.append(data)
            }
        }
        return finalImageDataArr
    }
    
    /**
     Remove image from images array in user profile
    **/
    func removeImage(_ index: Int) {
        let param = ["id": self.imagesArr[index]["id"] as! Int]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.removeImage, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    self.imageDataArr.remove(at: index)
                    self.imageCollectionView.reloadData()
                    if self.imageDataArr.count > 0 {//Update remaining images
                        self.addImages()
                    }else {
                        
                    }
                    self.manageImageCollection()
//                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
