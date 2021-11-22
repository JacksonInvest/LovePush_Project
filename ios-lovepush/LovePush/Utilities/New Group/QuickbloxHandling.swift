//
//  QuickbloxHandling.swift
//  LovePush
//
//  Created by Lavi Kumar on 10/06/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

class QuickbloxHandling: NSObject, QMChatServiceDelegate, QMChatConnectionDelegate, QMAuthServiceDelegate {
    
    static var shared = QuickbloxHandling()
    
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
    
    func getDialogList(completion: @escaping(_ dialogs: [QBChatDialog])->Void) {
        
        let extendRequest = ["sort_desc":String(describing: User.shared.qBUserID!)]
        
        QBRequest.dialogs(for: QBResponsePage.init(limit: 100), extendedRequest: extendRequest, successBlock: { (Responce, dialogs, number, pages) in
            
//            print(dialogs)
            completion(dialogs)
        
        })
    }
    
    func loadDialog(_ diaogID: String, complition: @escaping(_ dialog: QBChatDialog?)-> Void) {
        ServicesManager.instance().chatService.loadDialog(withID: diaogID) { (dialog) in
            complition(dialog)
        }
    }
    
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
    
    func logInChatWithUser(user: QBUUser) {
        
        user.password = "12345678LOVEPUSH_APP"// password
        // Logging to Quickblox REST API and chat.
        ServicesManager.instance().logIn(with: user, completion:{
            (success, errorMessage) -> Void in
            
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
    
    func updateCurrentUser(_ qbUserParam: QBUpdateUserParameters) {
        QBRequest.updateCurrentUser(qbUserParam, successBlock: { response, user in
        }, errorBlock: { response in
        })
    }
}
