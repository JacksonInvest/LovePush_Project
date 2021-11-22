//
//  Setting + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/07/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

struct Setting {
    
    //MARK:- User Setting Variable
    var isNotificationEnable = false
    var isHideFromSearch     = false
    var isRecieveChatRequest = false
    var isRecieveLoveRequest = false
    
    //MARK:- Setting Object Custructer
    init(_ settingData: [String:Any]) {
        
        self.isNotificationEnable = (settingData["receive_notification"] as! String == "1")
        self.isHideFromSearch     = (settingData["hide_from_search"] as! String == "1")
        self.isRecieveChatRequest = (settingData["receive_chat_request"] as! String == "1")
        self.isRecieveLoveRequest = (settingData["receive_like_request"] as! String == "1")
    }
}

extension SettingsVC {
    /**
        Get User Setting Data to enable or disable like receive_notification, hide_from_search, receive_chat_request,receive_like_request
        - returns: Get All Settings data
    **/
    func getSettingsData(completion: @escaping(_ settingData: [String:Any]?)->Void) {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.getSettings, header: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if let settingsData = (response as! [String:Any])["settings"] as? [String:Any] {
                       completion(settingsData)
                    }else {
                         completion(nil)
                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (isYes) in
                        completion(nil)
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: { (isYes) in
                    completion(nil)
                })
            }
        }
        
    }
    
    /**
     Setting to enable or disable request as receive_notification, hide_from_search, receive_chat_request,receive_like_request
     - parameter url: URL for different type of request
     - parameter index: indx to update switch button with success or failure
     **/
    func enableDisableReuqest(_ url: String, status: Bool, index:Int, completion: @escaping(_ isSuccess: Bool)->Void ) {
        
        let param = ["id": Int(User.shared.id!)!, "status": status ? "1" : "0"] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPOSTWithHeader(url, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                   completion(true)
                }else {
                    completion(false)
//                    let indexPath = IndexPath(item: index, section: 0)
//                    self.settingTableView.reloadRows(at: [indexPath], with: .fade)
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                  completion(false)
//                let indexPath = IndexPath(item: 5, section: 0)
//                self.settingTableView.reloadRows(at: [indexPath], with: .fade)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
