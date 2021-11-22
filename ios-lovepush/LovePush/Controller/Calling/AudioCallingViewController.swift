//
//  AudioCallingViewController.swift
//  Red
//
//  Created by promatics on 03/02/18.
//  Copyright © 2018 promatics. All rights reserved.
//

import UIKit
import AVFoundation

protocol AudioCallMessageDelegate {
    func sendAudioCallMessage(_ message: String)
}

class AudioCallingViewController: UIViewController, QBRTCClientDelegate,QBRTCAudioSessionDelegate, QBChatDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var transperentView: UIView!
    
    @IBOutlet weak var EndCallBtn: UIButton!
    
    @IBOutlet weak var muteBtn: UIButton!
    
    @IBOutlet weak var speakerBtn: UIButton!
    
    @IBOutlet weak var userName_lbl: UILabel!
    
    @IBOutlet weak var time_lbl: UILabel!
    
    @IBOutlet var connectedLabel: UILabel!
    
    @IBOutlet weak var minimize_button: UIButton!
    
    //MARK:- Variables
    var currentSession : QBRTCSession!
    
    var callByMe = false
    
    var myQuickBloxId : UInt!
    
    var otherUserQuickBloxId : Int!
    
    var userInfo :[String:String] = [:]
    
    var otherUserName = ""
    
    var voiceTimer = Timer()
    
    var voiceSecondTime = 0
    
    var audioTimer = Timer()
    
    var audioPlayer : AVAudioPlayer!
    
    var messageDelegate : AudioCallMessageDelegate?
    
    var lastControllerRotationStatus : Bool?
    
    var isRepresented = true
    
    var isCallInBackground = false
    
    var callUUID: UUID?
    
    //MARK:- Controller
    override func viewDidLoad() {
        super.viewDidLoad()
        
        UIApplication.shared.isIdleTimerDisabled = true
        
        do {
            
            try AVAudioSession.sharedInstance().overrideOutputAudioPort(.none)
            
        }catch {
        
        }
        
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            lastControllerRotationStatus = appDelegate.shouldRotate
            appDelegate.shouldRotate = false
        }
        
        isCallInBackground = false
        
        Singleton.shared.isCallInitiated = true

        QBChat.instance.addDelegate(self)
        
        QBRTCClient.initializeRTC()
        
        QBRTCClient.instance().add(self)
        
        QBRTCAudioSession.instance().addDelegate(self)
        
        self.userName_lbl.text = otherUserName
        
        if !isRepresented {
            
            sendTracks()
            
            connectedLabel.text = "Connecting..."
            
            if callByMe {
                
                let opponentsIDs = [otherUserQuickBloxId]
                
                currentSession = QBRTCClient.instance().createNewSession(withOpponents: opponentsIDs as! [NSNumber], with: .audio)
                
                // userInfo - the custom user information dictionary for the call. May be nil.
                
                if self.currentSession != nil{
                    
                    self.currentSession!.localMediaStream.audioTrack.isEnabled = true
                    
                    userInfo = ["id" : String(User.shared.qBUserID!), "call_type" : "audio", "caller_name": String(describing: User.shared.name!),"user_name": otherUserName]
                    
                    _ = Timer.scheduledTimer(withTimeInterval: 2, repeats: false, block: { (time) in
                        
                        self.currentSession.startCall(self.userInfo)
                        
                        if CallKitManager.isCallKitAvailable() == true,
                            self.currentSession.initiatorID.uintValue == UInt(User.shared.qBUserID)! {
                            self.callUUID = UUID()
                            CallKitManager.instance.updateCall(with: self.callUUID, connectingAt: Date())
                        }
                    })
                }
                
                connectedLabel.text = "Ringing..."
                
                self.playSound(soundName: "ringback")
                
                audioTimer = Timer.scheduledTimer(withTimeInterval: 2, repeats: true, block: { (timer) in
                    
                    self.audioPlayer.play()
                })
            }else {
                
                self.callAccept()
                CallKitManager.instance.reportCallEnd()
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        self.navigationController?.navigationBar.topItem?.title = ""
        
        self.minimize_button.layer.borderWidth = 1
        
        self.minimize_button.layer.borderColor = UIColor(red: 80.0/255.0, green: 0.0/255.0, blue: 0.0/255.0, alpha: 0.5).cgColor
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        
        if !isCallInBackground{
            
            endChat()
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            
            appDelegate.shouldRotate = lastControllerRotationStatus ?? true
        }
    }
   
    //MARK:- Custom Method's
    func callAccept() {
        
        let userInfo :[String:String] = ["id": String(myQuickBloxId!)]
        
        self.currentSession.acceptCall(userInfo)
        
        self.currentSession!.localMediaStream.audioTrack.isEnabled = true
    }
    
    //MARK:- Button Action
    
    @IBAction func EndCall(_ sender: UIButton) {
        
      
        if callByMe {
            
            callByMe = false
            
            if connectedLabel.text == "Connected."{
                
                self.messageDelegate?.sendAudioCallMessage("Audio Call Duration : \(String(describing: self.time_lbl.text!))")
            }else {
//                self.QBchat(messageString: "Audio Call Missed.", dialog: privateRoomInfo)
            }
            audioTimer.invalidate()
            audioPlayer.stop()
        }
        
        if self.currentSession != nil {
            // we already have a video/audio call session, so we reject another one
            // userInfo - the custom user information dictionary for the call from caller. May be nil.
            let userInfo :[String:String] = ["id": String(User.shared.qBUserID)]
            currentSession?.rejectCall(userInfo)
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func muteBtn(_ sender: UIButton) {
        
        self.currentSession!.localMediaStream.audioTrack.isEnabled = !self.currentSession!.localMediaStream.audioTrack.isEnabled
        
        self.currentMode()
    }
    
    @IBAction func speakerBtn(_ sender: UIButton) {
        
        do {
            if speakerBtn.currentImage == #imageLiteral(resourceName: "soundOnOff") {//.isSelected {
                speakerBtn.setImage(#imageLiteral(resourceName: "sound_Low"), for: .normal)
                speakerBtn.isSelected = false
                try AVAudioSession.sharedInstance().overrideOutputAudioPort(.none)
            }else {
                speakerBtn.setImage(#imageLiteral(resourceName: "soundOnOff"), for: .normal)
                speakerBtn.isSelected = true
                try AVAudioSession.sharedInstance().overrideOutputAudioPort(.speaker)
                
            }
        }catch {
            print("none")
        }
    }
    
    @IBAction func audioCallTrackSent(_ sender: Any) {
        
        isCallInBackground = true
        
        _ = self.dismiss(animated: true, completion: nil)
        
        Singleton.shared.floatingButtonController.window.isHidden = false
        self.currentMode()
    }
    
    func currentMode() {
        if self.currentSession!.localMediaStream.audioTrack.isEnabled {
            self.currentSession!.localMediaStream.videoTrack.isEnabled = true
            muteBtn.setImage(#imageLiteral(resourceName: "mike on white"), for: .normal)
        }else {
            self.currentSession!.localMediaStream.videoTrack.isEnabled = false
            muteBtn.setImage(#imageLiteral(resourceName: "micOnOff-white"), for: .normal)
        }
    }
    
    func sendTracks(){
        
        Singleton.shared.floatingButtonController.mainView.frame.size = CGSize(width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.y = 0
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.x = self.view.frame.size.width - Singleton.shared.floatingButtonController.mainView.frame.size.width
        
        Singleton.shared.floatingButtonController.playerButton.frame = CGRect(x: 0, y: 0, width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.logoImage.frame = CGRect(x: 20, y: 30, width: 60, height: 60)
        
        Singleton.shared.floatingButtonController.timerLabel.frame = CGRect(x: 0, y: 80, width: 100, height: 40)
        
        Singleton.shared.floatingButtonController.timerLabel.textColor = UIColor(red: 169/255, green: 16/255, blue: 36/255, alpha: 1.0)
        
        Singleton.shared.floatingButtonController.window.isHidden = true
        
        Singleton.shared.floatingButtonController.remoteVideoView.isHidden = true
        
        Singleton.shared.floatingButtonController.timerLabel.isHidden = true/////// Vaishali
        
        Singleton.shared.floatingButtonController.logoImage.isHidden = false
    }
        
    func startTimer() {
        
        connectedLabel.text = "Connected."

        self.time_lbl.text = "00:00:00"
        
        voiceTimer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(setVoiceTime), userInfo: nil, repeats: true)
    }
    
    @objc func setVoiceTime() {
        
        voiceSecondTime += 1
        
        self.hmsFrom(seconds: voiceSecondTime) { hours, minutes, seconds in
            
            let hours = self.getStringFrom(seconds: hours)
            
            let minutes = self.getStringFrom(seconds: minutes)
            
            let seconds = self.getStringFrom(seconds: seconds)
            
            let recordTimeStr = "\(hours):\(minutes):\(seconds)"
            
            self.time_lbl.text = recordTimeStr
            
            Singleton.shared.floatingButtonController.timerLabel.text = recordTimeStr
        }
    }
    
    func hmsFrom(seconds: Int, completion: @escaping (_ hours: Int, _ minutes: Int, _ seconds: Int)->()) {
        
        completion(seconds / 3600, (seconds % 3600) / 60, (seconds % 3600) % 60)
    }
    
    func getStringFrom(seconds: Int) -> String {
        
        return seconds < 10 ? "0\(seconds)" : "\(seconds)"
    }
    
    //MARK:- QBRTCClientDelegate
    
    func session(_ session: QBRTCBaseSession, updatedStatsReport report: QBRTCStatsReport, forUserID userID: NSNumber) {
        _ = report.audioReceivedBitrateTracker.bitrate
        _ = report.videoReceivedBitrateTracker.bitrate
        
        // works even if mic is disabled( audioTrack.isEnabled == false)
        _ = report.audioSendInputLevel
    }
    
    //Start Connection
    
    func session(_ session: QBRTCBaseSession, startedConnectingToUser userID: NSNumber) {
        
        print("Started connecting to user \(userID)")
    }
    
    //Call Accepted
    
    func session(_ session: QBRTCSession, acceptedByUser userID: NSNumber, userInfo: [String : String]? = nil) {
        
    }
    
    //connection closed
    
    func session(_ session: QBRTCBaseSession, connectionClosedForUser userID: NSNumber) {
        
        print("Connection is closed for user \(userID)")
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            endChat()
        }
    }
    
    //Connection Created
    
    func session(_ session: QBRTCBaseSession, connectedToUser userID: NSNumber) {
        
        if voiceSecondTime == 0{
            
            self.startTimer()
            
            if callByMe{
                
                audioTimer.invalidate()
                audioPlayer.stop()
            }
            print("Connection is established with user \(userID)")
//
//            do {
//                try AVAudioSession.sharedInstance().overrideOutputAudioPort(.none)
//            }catch {
//                print("none")
//            }
        }
        
    }
    
    //disconnected from user
    
    func session(_ session: QBRTCBaseSession, disconnectedFromUser userID: NSNumber) {

        print("Disconnected from user \(userID)");
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            Singleton.shared.isCallInitiated = false
            
            if callByMe {
                
                callByMe = false
                
                audioTimer.invalidate()
                audioPlayer.stop()
                
//                self.messageDelegate?.sendAudioCallMessage("Audio Call Duration : \(String(describing: self.time_lbl.text!))")
            }
            
            endChat()
        }
    }
    
    //Call Rejected
    
    func session(_ session: QBRTCSession, rejectedByUser userID: NSNumber, userInfo: [String : String]? = nil) {
        
        print("Rejected by user \(userID)")
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe {
                
                callByMe = false
                
                audioTimer.invalidate()
                
                audioPlayer.stop()
                
                self.messageDelegate?.sendAudioCallMessage("Audio Call Rejected ")
            }
            
            endChat()
        }
    }
    
    //User Not responding
    
    func session(_ session: QBRTCSession, userDidNotRespond userID: NSNumber) {
        
        print("User \(userID) did not respond to your call within timeout")
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe {
                
//                callByMe = false
//                
//                audioTimer.invalidate()
//                
//                audioPlayer.stop()
                
                if String(describing: Singleton.shared.callReceiverID) == String(describing: userID) && Singleton.shared.callReceiveForDialogID == privateRoomInfo.id! {
                    
//                    self.QBchat(messageString: "Audio Call Missed.", dialog: privateRoomInfo)
                    
                    Singleton.shared.callReceiverID = 0
                    
                    Singleton.shared.callReceiveForDialogID = ""
                }
            }
            
           
            endChat()
        }
    }
    
    // connection failed
    
    func session(_ session: QBRTCBaseSession, connectionFailedForUser userID: NSNumber) {
        
        print("Connection has failed with user \(userID)")
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe{
                
                callByMe = false
                
                audioTimer.invalidate()
                
                audioPlayer.stop()
                
                self.messageDelegate?.sendAudioCallMessage("Audio Call Connection Lost")
            }
            
            endChat()
        }
    }
    
    //DidChange State
    
    func session(_ session: QBRTCBaseSession, didChange state: QBRTCSessionState) {
        
        print("Session did change state to \(state.hashValue)")
    }
    
    //conectionState
    
    func session(_ session: QBRTCBaseSession, didChange state: QBRTCConnectionState, forUser userID: NSNumber) {
        
        print("Session did change state to \(state.hashValue) for userID \(userID)")

    }
    
    // receivedRemoteAudioTrack
    
    func session(_ session: QBRTCBaseSession, receivedRemoteAudioTrack audioTrack: QBRTCAudioTrack, fromUser userID: NSNumber) {
        
        
        audioTrack.isEnabled = true
    }
    
    @objc func endChat(forReporting: Bool = false) {
        
        if callByMe{
            
            audioTimer.invalidate()
            
            audioPlayer.stop()
            
            callByMe = false
        }
        
        voiceTimer.invalidate()
        
        let userInfo :[String:String] = ["id": String(myQuickBloxId!)]
        
        currentSession?.rejectCall(userInfo)
        
        currentSession?.hangUp(userInfo)
        
        self.dismiss(animated: true, completion: nil)
        
        Singleton.shared.floatingButtonController.window.isHidden = true
        
        Singleton.shared.isCallInitiated = false
        Singleton.shared.session = nil
        
    }   
    
    func playSound(soundName: String)
    {
        let coinSound = NSURL(fileURLWithPath: Bundle.main.path(forResource: soundName, ofType: "wav")!)
        do{
            self.audioPlayer = try AVAudioPlayer(contentsOf: coinSound as URL)
            self.audioPlayer.prepareToPlay()//there is also an async version
            self.audioPlayer.play()
        }catch {
            print("Error getting the audio file")
        }
    }
    
    func DateFormater(Str: String)-> String{
        
        let dateFormatter = DateFormatter()
        
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss Z"
        
        let NewDate = dateFormatter.date(from: Str)! as NSDate
        
        dateFormatter.dateFormat = "hh:mm a"
        
        return dateFormatter.string(from: NewDate as Date)
    }
}
