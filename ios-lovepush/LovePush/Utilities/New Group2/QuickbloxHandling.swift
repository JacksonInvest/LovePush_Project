//
//  QuickbloxHandling.swift
//  LovePush
//
//  Created by lavi on 29/11/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

class QuickbloxHandling: NSObject, QMChatServiceDelegate, QMChatConnectionDelegate, QMAuthServiceDelegate {
    
    static var shared = QuickbloxHandling()
    
    /**
     Reguest to create new Quicklox dialog with a friends with user QB ID
     - parameter user: User Info contain user data on local server
     - parameter completion: Completion handler to give response success or failure
     **/
    func createDialog(_ user: UserInfo, complition: @escaping(_ dialog: QBChatDialog?, _ error: QBError?)-> Void) {
        
        if user.qbID != "" {
            
            let chatDialog: QBChatDialog = QBChatDialog(dialogID: nil, type: QBChatDialogType.private)
            
            chatDialog.occupantIDs = [NSNumber(value: Int(user.qbID!)!), NSNumber(value: Int(User.shared.qBUserID)!)]
            
            QBRequest.createDialog(chatDialog, successBlock: { (response, dialog) in
                complition(dialog, nil)
            }) { (resopnse) in
                complition(nil, resopnse.error)
            }
        }
    }
    
    /**
     Reguest to get all dailog list created with me
     - returns: Dialog List with completion handler
     - parameter completion: Completion handler to give response success or failure
     **/
    func getDialogList(completion: @escaping(_ dialogs: [QBChatDialog])->Void) {
        
        let extendRequest = ["sort_desc":String(describing: User.shared.qBUserID!)]
        
        QBRequest.dialogs(for: QBResponsePage.init(limit: 100), extendedRequest: extendRequest, successBlock: { (Responce, dialogs, number, pages) in
            
            //            print(dialogs)
            completion(dialogs)
            
        })
    }
    
    /**
     Reguest to load already existing dailog
     - parameter dialogID: dialog Id to get extisting dialog data
     - parameter completion: Completion handler to give response success or failure
     **/
    func loadDialog(_ diaogID: String, complition: @escaping(_ dialog: QBChatDialog?)-> Void) {
        ServicesManager.instance().chatService.loadDialog(withID: diaogID) { (dialog) in
            complition(dialog)
        }
    }
    
    /**
     Reguest to get user info from quickblox
     - parameter dialogID: dialog Id to get user Data
     - parameter completion: Completion handler to give response success or failure
     **/
    func getUserProfileImage(_ userID: UInt, completion: @escaping(_ profileImage: UIImage?, _ urlStr: String?)-> Void) {
        
        QBRequest.user(withID: userID, successBlock: { (response :QBResponse?, user:QBUUser?) in
            
            if let url = user?.customData {
                completion(nil, url)
            }else if user?.avatarUrl == nil {
                
                //no avatar. Download file by user's blob id
                let userProfileBlobUID = user?.blobID
                
                QBRequest.downloadFile(withID: userProfileBlobUID!, successBlock: { (response : QBResponse,imageData: Data) in
                    
                    let image = UIImage(data:imageData)
                    
                    completion(image,nil)
                    
                }, statusBlock: { (response, status) in
                    //                    print(status)
                }, errorBlock: { (response) in
                    print(response)
                    completion(nil,nil)
                })
            } else {
                //download image by url
                completion(nil, user?.avatarUrl)
            }
        }) { (response :QBResponse) in
            completion(nil,nil)
        }
    }
    
    /**
     Reguest to login in quickblox server
     - parameter dialogID: dialog Id to login in quicklbox
     - returns: nil
     **/
    
    func quickBloxLogin(_ quickBloxID: UInt) {
        QBRequest.user(withID: quickBloxID, successBlock: { (response, user) in
            
            if response.status.rawValue == 200{
                Singleton.shared.quickbloxUserdata = user
                let archivedData = NSKeyedArchiver.archivedData(withRootObject: user)
                UserDefaults.standard.set(archivedData, forKey: "QBUser")
                self.logInChatWithUser(user: user)
            }
            
        }) { (error) in
            //            self.showAlert(title: "Error", message: "User not found in Quickblox.", completion: nil)
        }
    }
    
    /**
      Logging to Quickblox REST API and chat.
     - parameter user: USer Info for parameter to login to quickblox
     - returns: nil
     **/
    func logInChatWithUser(user: QBUUser) {
        
        user.password = "12345678LOVEPUSH_APP"// password
        // Logging to Quickblox REST API and chat.
        ServicesManager.instance().logIn(with: user, completion:{
            (success, errorMessage) -> Void in
            
            print("Login Success:: \(success)")
            print("Login Error:: \(String(describing: errorMessage))")
            
            guard success else {
                //                self.showAlert(title: "Error", message: "User do not login with Quickblox.", completion: nil)
                return
            }
            
            ServicesManager.instance().subscribeQBNotification()
            
            //            if user.fullName == nil {
            //                let _userParam = QBUpdateUserParameters()
            //                _userParam.fullName = User.shared.name!
            //                self.updateCurrentUser(_userParam)
            //            }
            
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            appDelegate.subscriptionForVoip()
        })
    }
    
    /**
     Update quickblox profile info
     - parameter image: Image to update profile
     - parameter name:  Name to update profile
     - returns: nil
     **/
    func updateQBProfile(_ image: UIImage, name: String) {
        
        let imageData = UIImagePNGRepresentation(image)
        
        QBRequest.tUploadFile(imageData!, fileName: "MyAvatar", contentType: "image/png", isPublic: true, successBlock: { (response, qbBlob) in
            //            print(response)
            //            print(qbBlob.blobObjectAccess?.blobID)
            let _user = QBUpdateUserParameters()
            _user.fullName = name
            _user.blobID = (qbBlob.blobObjectAccess?.blobID)!
            _user.customData = qbBlob.privateUrl()
            self.updateCurrentUser(_user)
        }, statusBlock: nil) { (response) in
            //            print(response.error)
        }
    }
    
    /**
     Update quickblox profile info
     - parameter qbUserParam: Update User profile parameter
     - returns: nil
     **/
    func updateCurrentUser(_ qbUserParam: QBUpdateUserParameters) {
        QBRequest.updateCurrentUser(qbUserParam, successBlock: { response, user in
        }, errorBlock: { response in
        })
    }
}
