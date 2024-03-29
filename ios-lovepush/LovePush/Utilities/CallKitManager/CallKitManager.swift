//
//  CallKitManager.swift
//  sample-videochat-webrtc-swift
//
//  Created by Vladimir Nybozhinsky on 12/10/18.
//  Copyright © 2018 QuickBlox. All rights reserved.
//

import UIKit
import QuickbloxWebRTC
import Quickblox
import CallKit

struct CallKitManagerConstant {
    static let maximumCallsPerCallGroup: Int = 1
    static let maximumCallGroups: Int = 1
}

typealias CompletionBlock = (() -> Void)

/**
 CallKitManager class interface.
 Used as manager of Apple CallKit.
 */
class CallKitManager: NSObject {
    
    //MARK: - Properties
    private var callStarted = false
    
    /**
     UserDataSource instance to get users information from.
     */
//    var usersDatasource: UsersDataSource?
    /**
     Action on microphone mute using CallKit UI.
     */
    var onMicrophoneMuteAction: CompletionBlock?
    
    private var provider: CXProvider?
    private var callController: CXCallController?
    private var actionCompletionBlock: CompletionBlock?
    private var onAcceptActionBlock: CompletionBlock?
    private var session: QBRTCSession?
    
    /**
     Class singleton instance.
     */
    static let instance = CallKitManager()
    
    
    class func configuration() -> CXProviderConfiguration? {
        let appName = Bundle.main.infoDictionary?["CFBundleDisplayName"] as? String
        let config = CXProviderConfiguration(localizedName: appName ?? "")
        config.supportsVideo = true
        config.maximumCallsPerCallGroup = CallKitManagerConstant.maximumCallsPerCallGroup
        config.maximumCallGroups = CallKitManagerConstant.maximumCallGroups
        let supportedHandleTypes: Set = [CXHandle.HandleType.generic, CXHandle.HandleType.phoneNumber]
        config.supportedHandleTypes = supportedHandleTypes
        if let image = UIImage(named: "CallKitLogo") {
            config.iconTemplateImageData = image.jpeg(.lowest)
        }
        config.ringtoneSound = "ringtone.wav"
        return config
    }
    
    var uUID : UUID?
    var callerName = ""
    
    static var callKitAvailable: Bool = {
        var callKitAvailable = false
        if #available(iOS 10.0, *) {
            callKitAvailable = true
        }
        return callKitAvailable
    }()
    
    class func isCallKitAvailable() -> Bool {
        // `dispatch_once()` call was converted to a static variable initializer
        return callKitAvailable
    }
    
    // MARK: - Life Cycle
    override init() {
        super.init()
        
        let configuration: CXProviderConfiguration? = CallKitManager.configuration()
        if let configuration = configuration {
            provider = CXProvider(configuration: configuration)
        }
        if let provider = provider {
            provider.setDelegate(self, queue: nil)
        }
        callController = CXCallController(queue: DispatchQueue.main)
    }
    
    // MARK: - Call management
    /**
     Start Call with user IDs.
     
     @param userIDs user IDs to perform call with
     @param session session instance
     @param uuid call uuid
     
     @discussion Use this to perform outgoing call with specific user ids.
     
     @see QBRTCSession
     */
    func startCall(withUserIDs userIDs: [NSNumber], session: QBRTCSession?, uuid: UUID?) {
        guard let session = session,
            let uuid = uuid else {
                return
        }
        self.session = session
        self.uUID = uuid
        let contactIdentifier = self.callerName
        let handle: CXHandle? = self.handle(forUserIDs: userIDs, outCallerName: contactIdentifier)
        var action: CXStartCallAction? = nil
        if let handle = handle {
            action = CXStartCallAction(call: uuid, handle: handle)
        }
        action?.contactIdentifier = contactIdentifier
        var transaction: CXTransaction? = nil
        if let action = action {
            transaction = CXTransaction(action: action)
        }
        if let transaction = transaction {
            request(transaction) { succeed in
                let update = CXCallUpdate()
                update.remoteHandle = handle
                update.localizedCallerName = contactIdentifier
                update.supportsHolding = false
                update.supportsGrouping = false
                update.supportsUngrouping = false
                update.supportsDTMF = false
                update.hasVideo = session.conferenceType == QBRTCConferenceType.video
                if let provider = self.provider {
                    provider.reportCall(with: uuid, updated: update)
                }
            }
        }
    }
    
    /**
     End call with uuid.
     @param uuid uuid of call
     @param completion completion block
     */
    func endCall(with uuid: UUID?, completion: @escaping () -> ()) {
        guard let _ = self.session,
            let uuid = uuid else {
                return
        }
        var action: CXEndCallAction? = nil
        action = CXEndCallAction(call: uuid)
        var transaction: CXTransaction? = nil
        if let action = action {
            transaction = CXTransaction(action: action)
        }
        dispatchOnMainThread(block: {
            if let transaction = transaction {
                self.request(transaction) { _ in }
            }
        })
        actionCompletionBlock = completion
    }
    
    /**
     Report incoming call with user IDs.
     
     @param userIDs user IDs of incoming call
     @param session session instance
     @param uuid call uuid
     @param onAcceptAction on call accept action
     @param completion completion block
     
     @discussion Use this to show incoming call screen.
     
     @see QBRTCSession
     */
    func reportIncomingCall(withUserIDs userIDs: [NSNumber], session: QBRTCSession?, uuid: UUID?, onAcceptAction: @escaping () -> Void, completion: @escaping (Bool) -> Void) {
        if let uuid = uuid {
            debugPrint("[CallKitManager] Report incoming call \(uuid)")
        }
        guard let session = session else {
            return
        }
        
        self.session = session
        onAcceptActionBlock = onAcceptAction
        
        let callerName = self.callerName
        let update = CXCallUpdate()
        update.remoteHandle = self.handle(forUserIDs: userIDs, outCallerName: callerName)
        update.localizedCallerName = callerName
        update.supportsHolding = false
        update.supportsGrouping = false
        update.supportsUngrouping = false
        update.supportsDTMF = false
        update.hasVideo = session.conferenceType == QBRTCConferenceType.video

        debugPrint("[CallKitManager] Activating audio session.")
        let audioSession = QBRTCAudioSession.instance()
        audioSession.useManualAudio = true
        // disabling audio unit for local mic recording in recorder to enable it later
        session.recorder?.isLocalAudioEnabled = false
        if audioSession.isInitialized == false {
            audioSession.initialize { configuration in
                // adding blutetooth support
                configuration.categoryOptions.insert(AVAudioSession.CategoryOptions.allowBluetooth)
                configuration.categoryOptions.insert(AVAudioSession.CategoryOptions.allowBluetoothA2DP)
                // adding airplay support
                configuration.categoryOptions.insert(AVAudioSession.CategoryOptions.allowAirPlay)

                if session.conferenceType == QBRTCConferenceType.video {
                    // setting mode to video chat to enable airplay audio and speaker only
                    configuration.mode = AVAudioSessionModeVideoChat.description
                }
            }
        }
        if let provider = provider,
            let uuid = uuid{
            provider.reportNewIncomingCall(with: uuid, update: update) { error in
                let errorDomain = (error as NSError?)?.domain
                let errorCode = (error as NSError?)?.code
                let silent = errorDomain == CXErrorDomainIncomingCall &&
                    errorCode == CXErrorCodeIncomingCallError.Code.filteredByDoNotDisturb.rawValue
                self.dispatchOnMainThread(block: {
                    completion(silent)
                })
            }
        }
        
//        self.reportCallEnd()
    }
    
    /**
     Update outgoing call with connecting date
     
     @param uuid call uuid
     @param date connecting started date
     */
    func updateCall(with uuid: UUID?, connectingAt date: Date?) {
        if let uuid = uuid,
            let provider = provider {
            provider.reportOutgoingCall(with: uuid, startedConnectingAt: date)
        }
    }
    
    /**
     Update outgoing call with connected date.
     
     @param uuid call uuid
     @param date connected date
     */
    func updateCall(with uuid: UUID?, connectedAt date: Date?) {
        if let uuid = uuid,
            let provider = provider {
            provider.reportOutgoingCall(with: uuid, connectedAt: date)
        }
    }
    
    // MARK: - Helpers
    func handle(forUserIDs userIDs: [NSNumber], outCallerName: String?) -> CXHandle? {
//        var outCallerName = outCallerName
//        // handle user from whatever database here
//        if let usersDatasource = usersDatasource,
//            outCallerName != nil {
//            var opponentNames = [String]()
//            for userID in userIDs {
//                let user = usersDatasource.user(withID: userID.uintValue )
//                opponentNames.append(user?.fullName ?? "\(userID.uintValue)")
//            }
//            outCallerName = opponentNames.joined(separator: ", ")
//        }
//        if userIDs.count == 1 {
//            if let userId = userIDs.first,
//                let usersDatasource = usersDatasource,
//                let user = usersDatasource.user(withID: userId.uintValue),
//                user.phone?.isEmpty == false {
//                return CXHandle(type: .phoneNumber, value: user.phone ?? "")
//            }
//        }
        let arrayUserIDs = userIDs.map({"\($0)"})
        return CXHandle(type: .generic, value: arrayUserIDs.joined(separator: ", "))
    }
    
    @inline(__always) private func dispatchOnMainThread(block: @escaping () -> Void) {
        if Thread.isMainThread == true {
            block()
        } else {
            DispatchQueue.main.async(execute: block)
        }
    }
    
    func request(_ transaction: CXTransaction, completion: @escaping (Error?) -> Void) {
        callController?.request(transaction) { error in
            if let error = error {
                debugPrint("[CallKitManager] Error: \(error)")
            }
        }
    }
}

extension CallKitManager: CXProviderDelegate {
    // MARK: - CXProviderDelegate protocol
    @available(iOS 10.0, *)
    func providerDidReset(_ provider: CXProvider){
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, perform action: CXStartCallAction) {
        guard let session = self.session else {
            action.fail()
            return
        }
        dispatchOnMainThread(block: {
            session.startCall(nil)
            self.callStarted = true
            action.fulfill()
        })
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, perform action: CXAnswerCallAction) {
        guard let session = self.session else {
            action.fail()
            return
        }
        CallKitManager.callKitAvailable = true
        // Workaround for webrtc on ios 10, because first incoming call does not have audio
        // due to incorrect category: AVAudioSessionCategorySoloAmbient
        // webrtc need AVAudioSessionCategoryPlayAndRecord
        if !((try?  AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayAndRecord,
                                                                mode: AVAudioSession.Mode.debugDescription(),
                                                                options: AVAudioSession.CategoryOptions.defaultToSpeaker)) != nil) {
            print("[CallKitManager] Error setting category for webrtc workaround.")
        }
        dispatchOnMainThread(block: {
            session.acceptCall(nil)
            self.callStarted = true
            action.fulfill()
            if let onAcceptActionBlock = self.onAcceptActionBlock {
                onAcceptActionBlock()
            }
        })
        
        action.fulfill()
    }
    
    func reportCallEnd(){
        if self.uUID != nil {
            self.provider!.reportCall(with: self.uUID!, endedAt: nil, reason: CXCallEndedReason.remoteEnded)
        }
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, perform action: CXEndCallAction) {
        guard let session = self.session else {
            action.fail()
            return
        }
        self.session = nil
        dispatchOnMainThread(block: {
            let audioSession = QBRTCAudioSession.instance()
            audioSession.isAudioEnabled = false
            audioSession.useManualAudio = false
            if self.callStarted == true {
                session.hangUp(nil)
                self.callStarted = false
            } else {
                session.rejectCall(nil)
            }
            action.fulfill(withDateEnded: Date())
            if let actionCompletionBlock = self.actionCompletionBlock {
                actionCompletionBlock()
            }
        })
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, perform action: CXSetMutedCallAction){
        guard let session = self.session else {
            action.fail()
            return
        }
        dispatchOnMainThread(block: {
            session.localMediaStream.audioTrack.isEnabled = !action.isMuted
            action.fulfill()
            if let onMicrophoneMuteAction = self.onMicrophoneMuteAction {
                onMicrophoneMuteAction()
            }
        })
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, didActivate audioSession: AVAudioSession){
        guard let session = self.session else {
            return
        }
        print("[CallKitManager] Activated audio session.")
        let rtcAudioSession = QBRTCAudioSession.instance()
        rtcAudioSession.audioSessionDidActivate(audioSession)
        // enabling audio now
        rtcAudioSession.isAudioEnabled = true
        // enabling local mic recording in recorder (if recorder is active) as of interruptions are over now
        session.recorder?.isLocalAudioEnabled = true
    }
    
    @available(iOS 10.0, *)
    func provider(_ provider: CXProvider, didDeactivate audioSession: AVAudioSession){
        print("[CallKitManager] Dectivated audio session.")
        QBRTCAudioSession.instance().audioSessionDidDeactivate(audioSession)
        // deinitializing audio session after iOS deactivated it for us
        let session = QBRTCAudioSession.instance()
        if session.isInitialized {
            print("Deinitializing session in CallKit callback.")
            session.deinitialize()
        }
    }
}
