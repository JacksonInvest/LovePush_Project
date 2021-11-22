//
//  AppDelegate + Quickblox.swift
//  LovePush
//
//  Created by Lavi Kumar on 13/06/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import PushKit

struct UsersConstant {
    static let pageSize: UInt = 50
    static let aps       = "aps"
    static let alert     = "alert"
    static let voipEvent = "VOIPCall"
}

extension AppDelegate: QBRTCClientDelegate {
    
    func quickBloxSetup(){
        
        //demo quickblox id
        QBSettings.applicationID = 79613  //77171 //
        QBSettings.authKey       = "85QhQ9yuXevuA3S" //"hhPNP9nfTYOLfNt" //
        QBSettings.authSecret    = "7yAayFYOVms4usG" //"fQFhcFxfkH2tTNG" 
        QBSettings.accountKey    = "EUV77LPYzhVwZBZedzps"// "ibS26PbG9Ao2fneNBUAd"
        
        //WebRTC Setting for audio video call
        QBSettings.keepAliveInterval = 60
        QBRTCClient.initializeRTC()
        QBRTCClient.instance().add(self)
        QBRTCSession.initialize()
        
        QBSettings.logLevel      = .debug
        QBSettings.enableXMPPLogging()
        
        if Singleton.shared.quickbloxUserdata != nil {
            self.connectWithQB()
        }
    }
   
    func connectWithQB(){
       if Singleton.shared.quickbloxUserdata != nil {
            QBRequest.logIn(withUserLogin: Singleton.shared.quickbloxUserdata.login!, password: "12345678LOVEPUSH_APP" , successBlock: { (response, user) in
                QBChat.instance.connect(with: user, completion: { (error) in
                    QBRTCClient.instance().add(self)
                    QBRTCConfig.setAnswerTimeInterval(60)
                    ServicesManager.instance().subscribeQBNotification()
                })
            }, errorBlock: { (response) in
            })
        }
    }
    
    func sessionDidClose(_ session: QBRTCSession) {
        print("sessionDid Close called")
       
    }
    
    
    func didReceiveNewSession(_ session: QBRTCSession, userInfo: [String : String]? = nil) {
        print("New Session")
        
        //        if Singleton.shared.isCallInitiated {
        //
        //            self.window?.rootViewController!.showAlert(title: "Love Push", message: "Call Already Initiated.", completion: nil)
        //
        //        }else {
        
        if userInfo != nil {
            Singleton.shared.isCallInitiated = true
            
            self.callKitHandlingWithNewSession(session, userInfo: userInfo!)
        }
        
        //        }
    }
    
    
    func session(_ session: QBRTCSession, userDidNotRespond userID: NSNumber) {
        Singleton.shared.session = nil
        Singleton.shared.isCallInitiated = false
    }
    
    
    func session(session: QBRTCSession!, rejectedByUser userID: NSNumber!, userInfo: [NSObject : AnyObject]!) {
        Singleton.shared.isCallInitiated = false
        Singleton.shared.session = nil
    }
    
    
    func session(_ session: QBRTCSession, acceptedByUser userID: NSNumber, userInfo: [String : String]? = nil)
    {
        
    }
    
    private func session(session: QBRTCSession!, connectedToUser userID: NSNumber!) {
        print("Connection is established with user \(String(describing: userID))")
    }
    
    func session(_ session: QBRTCBaseSession, connectionClosedForUser userID: NSNumber) {
        
        Singleton.shared.isCallInitiated = false
        Singleton.shared.session = nil
    }
    
    
    func session(_ session: QBRTCSession, hungUpByUser userID: NSNumber, userInfo: [String : String]? = nil)
    {
        print("call hang up by user")
        
    }
    
    func session(_ session: QBRTCBaseSession, receivedRemoteAudioTrack audioTrack: QBRTCAudioTrack, fromUser userID: NSNumber)
    {
        print("Audio received from another new user")
        audioTrack.isEnabled = true
    }
    
    func session(_ session: QBRTCBaseSession, receivedRemoteVideoTrack videoTrack: QBRTCVideoTrack, fromUser userID: NSNumber) {
        print("Video received from another new user")
        
    }
    
    
    func session(_ session: QBRTCBaseSession, didChange state: QBRTCSessionState) {
        
        
        if state == QBRTCSessionState.pending{
            
        }else if state == QBRTCSessionState.connecting{
            
        }else if state == QBRTCSessionState.connected{
            print("Checking New State \(state.rawValue)")
        }
        
        print(state)
    }
    
    func  session(_ session: QBRTCSession, rejectedByUser userID: NSNumber, userInfo: [String : String]? = nil){
        Singleton.shared.session = nil
        CallKitManager.instance.reportCallEnd()
    }
    
    func callKitHandlingWithNewSession(_ session: QBRTCSession, userInfo: [String:String]) {
        if Singleton.shared.session != nil {
            session.rejectCall(["reject": "busy"])
            return
        }
        Singleton.shared.session = session
        self.callUUID = UUID()
        var opponentIDs = [session.initiatorID]
        for userID in session.opponentsIDs {
            if userID.uintValue != UInt(User.shared.qBUserID)! {
                opponentIDs.append(userID)
            }
        }
        
        CallKitManager.instance.uUID = self.callUUID
        CallKitManager.instance.callerName = userInfo["caller_name"]!
        CallKitManager.instance.reportIncomingCall(withUserIDs: opponentIDs,
                                                   session: session,
                                                   uuid: self.callUUID,
                                                   onAcceptAction: { [weak self] in
                                                    guard let `self` = self else {
                                                        return
                                                    }
                                                    
                                                    if "\(String(describing: userInfo["call_type"]!))" == "audio" {
                                                        
                                                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "AudioCallingViewController") as! AudioCallingViewController
                                                        
                                                        vc.myQuickBloxId = UInt(User.shared.qBUserID)
                                                        
                                                        vc.otherUserQuickBloxId = Int(userInfo["id"]!)
                                                        
                                                        vc.otherUserName = userInfo["caller_name"]!
                                                        
                                                        vc.callByMe = false
                                                        
                                                        vc.isRepresented = false
                                                        
                                                        vc.currentSession = session
                                                        
                                                        Singleton.shared.floatingButtonController.currentCallingVC = vc
                                                        
                                                        self.window?.rootViewController!.present(vc, animated: false, completion: nil)
                                                    }else {
                                                        
                                                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "VideoCallingViewController") as! VideoCallingViewController
                                                        
                                                        vc.myQuickBloxId = UInt(User.shared.qBUserID)
                                                        
                                                        vc.otherUserQuickBloxId = Int(userInfo["id"]!)
                                                        
                                                        vc.otherUserName = userInfo["caller_name"]!
                                                        
                                                        vc.callByMe = false
                                                        
                                                        vc.isRepresented = false
                                                        
                                                        vc.currentSession = session
                                                        
                                                        Singleton.shared.floatingButtonController.currentCallingVC = vc
                                                        
                                                        self.window?.rootViewController!.present(vc, animated: false, completion: nil)
                                                    }
//                                                    CallKitManager.instance.reportCallEnd()
                                                    
            }, completion: { (end) in
                debugPrint("end")
        })
    }
}

//MARK:- Push kit
extension AppDelegate : PKPushRegistryDelegate {
    
    // MARK: - PKPushRegistryDelegate
    func pushRegistry(_ registry: PKPushRegistry, didUpdate pushCredentials: PKPushCredentials, for type: PKPushType) {
        //  New way, only for updated backend
        self.subscriptionForVoip()
    }
    
    func pushRegistry(_ registry: PKPushRegistry, didInvalidatePushTokenFor type: PKPushType) {
        
        guard let deviceIdentifier = UIDevice.current.identifierForVendor?.uuidString else {
            return
        }
        QBRequest.unregisterSubscription(forUniqueDeviceIdentifier: deviceIdentifier, successBlock: { response in
            print("Unregister Subscription request - Success")
        }, errorBlock: { error in
            print("Unregister Subscription request - Error")
        })
    }
    
    func pushRegistry(_ registry: PKPushRegistry, didReceiveIncomingPushWith payload: PKPushPayload, for type: PKPushType) {
        if CallKitManager.isCallKitAvailable() == true {
            if payload.dictionaryPayload[UsersConstant.voipEvent] != nil {
                let application = UIApplication.shared
                if application.applicationState == .background && backgroundTask == UIBackgroundTaskInvalid {
                    backgroundTask = application.beginBackgroundTask(expirationHandler: {
                        application.endBackgroundTask(self.backgroundTask)
                        self.backgroundTask = UIBackgroundTaskInvalid
                    })
                }
                if QBChat.instance.isConnected == false {
                    self.quickBloxSetup()
                    self.connectWithQB()
                }
            }
        }
    }
    
    func subscriptionForVoip() {
        guard let deviceIdentifier = UIDevice.current.identifierForVendor?.uuidString else {
            return
        }
        let subscription = QBMSubscription()
        subscription.notificationChannel = QBMNotificationChannel.APNSVOIP
        subscription.deviceUDID = deviceIdentifier
        subscription.deviceToken = voipRegistry.pushToken(for: .voIP)
        
        QBRequest.createSubscription(subscription, successBlock: { response, objects in
            print("Create Subscription request - Success")
        }, errorBlock: { response in
            print("Create Subscription request - Error")
        })
    }
}

