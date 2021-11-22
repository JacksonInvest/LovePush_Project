//
//  VideoCallingViewController.swift
//  Red
//
//  Created by promatics on 03/02/18.
//  Copyright © 2018 promatics. All rights reserved.
//

import UIKit

import AVFoundation

protocol VideoCallMessageDelegate {
    func sendVideoCallMessage(_ message: String)
}

var privateRoomInfo: QBChatDialog!

class VideoCallingViewController: UIViewController, QBRTCClientDelegate, QBRTCAudioSessionDelegate, QBChatDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var coverCameraView: UIView!
    
    @IBOutlet weak var smallCameraView: UIView!
    
    @IBOutlet weak var muteBtn: UIButton!
    
    @IBOutlet weak var EndCallBtn: UIButton!
    
    @IBOutlet weak var VideoBtn: UIButton!
    
    @IBOutlet var cameraButton: UIButton!
    
    @IBOutlet var remoteVideoView: QBRTCRemoteVideoView!
    
    @IBOutlet weak var userName_lbl: UILabel!
    
    @IBOutlet weak var time_lbl: UILabel!
    
    @IBOutlet var conncetedLabel: UILabel!
    
    //MARK:- Variables
    var videoCapture : QBRTCCameraCapture?
    
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
    
    var videoFormat : QBRTCVideoFormat!
    
    var isFirstLayer = true
    
    var lastControllerRotationStatus: Bool?
    
    var messageDelegate: VideoCallMessageDelegate?
    
    var isRepresented = true
    
    var isCallInBackground = false
    
    var callUUID: UUID?
    
    //MARK:- Controleller Default Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            lastControllerRotationStatus = appDelegate.shouldRotate
            appDelegate.shouldRotate = false
        }
        
        UIApplication.shared.isIdleTimerDisabled = true
        
        isCallInBackground = false
        
        QBChat.instance.addDelegate(self)
        
        QBRTCClient.initializeRTC()
        
        QBRTCClient.instance().add(self)
        
        Singleton.shared.isCallInitiated = true
        
        if !isRepresented {
            
            self.userName_lbl.text = otherUserName
            
            remoteVideoView.isHidden = false
            
            cameraButton.isSelected = true
            
            sendTracks()
            
            if callByMe{

                smallCameraView.isHidden = false

                let opponentsIDs:[NSNumber] = [otherUserQuickBloxId! as NSNumber]

                currentSession = QBRTCClient.instance().createNewSession(withOpponents: opponentsIDs , with: .video)

                if self.currentSession != nil{

                    userInfo = ["id" : String(User.shared.qBUserID!), "call_type" : "video", "caller_name": String(describing: User.shared.name!),"user_name": otherUserName]

                    currentSession.startCall(userInfo)
                    
                    if CallKitManager.isCallKitAvailable() == true,
                        currentSession.initiatorID.uintValue == UInt(User.shared.qBUserID)! {
                        self.callUUID = UUID()
                        CallKitManager.instance.updateCall(with: self.callUUID, connectingAt: Date())
                    }

                    conncetedLabel.text = "Ringing..."

                    self.sendVideo(isFront: true)
                }

                self.playSound(soundName: "ringback")

                audioTimer = Timer.scheduledTimer(withTimeInterval: 2, repeats: true, block: { (timer) in

                    self.audioPlayer.play()
                })

            }else {
            
                callAccept()
                CallKitManager.instance.reportCallEnd()
                do {
                    try AVAudioSession.sharedInstance().overrideOutputAudioPort(.speaker)
                }catch {
                    print("none")
                }
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        self.smallCameraView.layer.cornerRadius = 10
        
        self.smallCameraView.layer.masksToBounds = true
        
        self.navigationController?.isNavigationBarHidden = true
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            
            appDelegate.shouldRotate = lastControllerRotationStatus ?? true
        }
        self.navigationController?.isNavigationBarHidden = false
        
        if !isCallInBackground{
            
            endChat()
        }
    }
    
    func sendVideo(isFront:Bool) {
        
        videoFormat = QBRTCVideoFormat.init()
        videoFormat.frameRate = 30
        videoFormat.pixelFormat = QBRTCPixelFormat.format420f
        videoFormat.width = 640
        videoFormat.height = 480
        
        QBRTCConfig.setAnswerTimeInterval(60)
        
        if isFront{
            
            self.videoCapture = QBRTCCameraCapture.init(videoFormat: videoFormat, position: AVCaptureDevice.Position.front)
            
            cameraButton.setImage(#imageLiteral(resourceName: "ic_camera_front"), for: .normal)
            
        }else {
            
            self.videoCapture = QBRTCCameraCapture.init(videoFormat: videoFormat, position: AVCaptureDevice.Position.back)
            
            cameraButton.setImage(#imageLiteral(resourceName: "ic_camera_rear"), for: .normal)
        }
        
        // add video capture to session's local media stream
        // from version 2.3 you no longer need to wait for 'initializedLocalMediaStream:' delegate to do it
        
        self.currentSession.localMediaStream.videoTrack.videoCapture = self.videoCapture
        
        self.currentSession!.localMediaStream.videoTrack.isEnabled = true
        
        self.currentSession!.localMediaStream.audioTrack.isEnabled = true
        
        self.videoCapture!.previewLayer.frame = self.smallCameraView.bounds
        
        self.videoCapture!.startSession()
        
//        self.smallCameraView.contentMode = .scaleAspectFit
        
        if isFirstLayer {
            self.smallCameraView.layer.insertSublayer(self.videoCapture!.previewLayer, at: 0)
        }else { self.smallCameraView.layer.replaceSublayer(self.smallCameraView.layer.sublayers![0], with: (self.videoCapture!.previewLayer))
         }
        
        do {
            try AVAudioSession.sharedInstance().overrideOutputAudioPort(.speaker);
        }catch {
            print("none")
        }
    }
    
    func sendTracks(){
        
        Singleton.shared.floatingButtonController.mainView.frame.size = CGSize(width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.y = 0
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.x = self.view.frame.size.width - 100
        
        Singleton.shared.floatingButtonController.playerButton.frame = CGRect(x: 0, y: 0, width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.timerLabel.frame = CGRect(x: 0, y: 0, width: 100, height: 40)
        
        Singleton.shared.floatingButtonController.remoteVideoView.frame = CGRect(x: 0, y: 0, width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.window.isHidden = true
        
        Singleton.shared.floatingButtonController.remoteVideoView.isHidden = false
        
        Singleton.shared.floatingButtonController.timerLabel.isHidden = true
        
        Singleton.shared.floatingButtonController.logoImage.isHidden = false
        
    }
    
    func startTimer() {
        
        conncetedLabel.text = "Connected."
                
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
            
//            print(recordTimeStr)
            
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
    
    // MARK: QBRTCClientDelegate
    
    //Start Connection
    
    func didReceiveNewSession(_ session: QBRTCSession, userInfo: [String : String]? = nil) {
//        self.currentSession = session
//        self.callAccept()
    }
    
    func session(_ session: QBRTCBaseSession, startedConnectingToUser userID: NSNumber) {
        
        print("Started connecting to user \(userID)")
        
    }
    
    //Call Rejected
    
    func session(_ session: QBRTCSession, rejectedByUser userID: NSNumber, userInfo: [String : String]? = nil) {
        print("Rejected by user \(userID)")
        
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe {
                
                callByMe = false
                
                audioTimer.invalidate()
                audioPlayer.stop()
                
//                self.messageDelegate?.sendVideoCallMessage("Video Call Rejected.")
            }
            
            endChat()
       }
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
        
        print("Connection is established with user \(userID)")
        
        do {
            try AVAudioSession.sharedInstance().overrideOutputAudioPort(.speaker)
        }catch {
            print("none")
        }
//
            if voiceSecondTime == 0 {
                
                self.startTimer()
                
                if callByMe{
                    
                    audioTimer.invalidate()
                    audioPlayer.stop()
                }
            }
    }
    
    //disconnected from user
    
    func session(_ session: QBRTCBaseSession, disconnectedFromUser userID: NSNumber) {
        
        print("Disconnected from user \(userID)");
        
        self.conncetedLabel.text = "Call Disconnected"
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
            
            Singleton.shared.isCallInitiated = false
            
            if callByMe {
                
                callByMe = false
                
//                self.messageDelegate?.sendVideoCallMessage("Video Call Duration : \(String(describing: self.time_lbl.text!))")
            }
            
            endChat()
        }else {
            
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    //User Not responding
    
    func session(_ session: QBRTCSession, userDidNotRespond userID: NSNumber) {
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe{
                
                if String(describing: Singleton.shared.callReceiverID) == String(describing: userID) && Singleton.shared.callReceiveForDialogID == privateRoomInfo.id! {
                    
//                    self.QBchat(messageString: "Video Call Missed ", dialog: privateRoomInfo)
                    
                    Singleton.shared.callReceiverID = 0
                    
                    Singleton.shared.callReceiveForDialogID = ""
                }
                
                endChat()
            }
        }
        print("User \(userID) did not respond to your call within timeout")
        
    }
    
    // connection failed
    
    func session(_ session: QBRTCBaseSession, connectionFailedForUser userID: NSNumber) {
        
        if String(describing: userID) == String(describing: otherUserQuickBloxId!) {
        
            if callByMe{
                
                callByMe = false
                
                audioTimer.invalidate()
                audioPlayer.stop()
                
//                self.messageDelegate?.sendVideoCallMessage("Video Call Connection Failed")
                
                endChat()
            }
        }
        print("Connection has failed with user \(userID)")
    }
    
    //States
    
    //DidChange State
    
    func session(_ session: QBRTCBaseSession, didChange state: QBRTCSessionState) {
        
        print("Session did change state to \(state)")
    }
    
    //conectionState
    
    func session(_ session: QBRTCBaseSession, didChange state: QBRTCConnectionState, forUser userID: NSNumber) {
        
        print("Session did change state to \(state.rawValue) for userID \(userID)")
    }
    
//    76YF8Q
    
    //Manage remote media tracks
    
    //receivedRemoteVideoTrack
    
    func session(_ session: QBRTCBaseSession, receivedRemoteVideoTrack videoTrack: QBRTCVideoTrack, fromUser userID: NSNumber) {
        
        videoTrack.isEnabled = true
        
        self.remoteVideoView?.setVideoTrack(videoTrack)
        
        self.remoteVideoView.contentMode = .scaleAspectFit
        
        Singleton.shared.floatingButtonController.remoteVideoView.setVideoTrack(videoTrack)
        
        Singleton.shared.floatingButtonController.remoteVideoView.contentMode = .scaleAspectFit
        
        do {
            try AVAudioSession.sharedInstance().overrideOutputAudioPort(.speaker)
        }catch {
            print("none")
        }
    }
    
    // receivedRemoteAudioTrack
    
    func session(_ session: QBRTCBaseSession, receivedRemoteAudioTrack audioTrack: QBRTCAudioTrack, fromUser userID: NSNumber) {
        
        audioTrack.isEnabled = true
    }
    
    @objc func endChat(forReporting: Bool = false) {
        
        if callByMe{
            
            audioTimer.invalidate()
            
            audioPlayer.stop()
        }
        
        voiceTimer.invalidate()
        
        let userInfo :[String:String] = ["id": String(myQuickBloxId!)]
        
        currentSession?.rejectCall(userInfo)
        
        currentSession?.hangUp(userInfo)
        
        self.dismiss(animated: true, completion: nil)
        
        Singleton.shared.floatingButtonController.window.isHidden = true
        
        Singleton.shared.isCallInitiated = false
        
        if CallKitManager.isCallKitAvailable() == true {
            CallKitManager.instance.endCall(with: callUUID) {
                debugPrint("endCall")
                CallKitManager.instance.reportCallEnd()
            }
        }
    }
    
    func callAccept() {
        
        let userInfo :[String:String] = ["id": String(myQuickBloxId!)]
        
        self.currentSession.acceptCall(userInfo)
        
        self.currentSession!.localMediaStream.videoTrack.isEnabled = true
        
        self.currentSession!.localMediaStream.audioTrack.isEnabled = true
        
        self.sendVideo(isFront: true)
        
        self.smallCameraView.isHidden = false
        
        muteBtn.isHidden = false
        
        EndCallBtn.isHidden = false
        
        //remoteVideoView.isHidden = false
        
        cameraButton.isHidden = false
        
    }
    
    //MARK: Action's
    
    @IBAction func dismissWithCallButtonTapped(_ sender: Any) {
        
        isCallInBackground = true
        
        _ = self.dismiss(animated: false, completion: nil)
        
        Singleton.shared.floatingButtonController.window.isHidden = false
        
        self.currentMode()
    }
    
    @IBAction func muteBtn(_ sender: Any) {
        
        self.currentSession!.localMediaStream.audioTrack.isEnabled = !self.currentSession!.localMediaStream.audioTrack.isEnabled
        
        self.currentMode()
    }
    
    @IBAction func EndBtn(_ sender: Any) {
        
            // we already have a video/audio call session, so we reject another one
            // userInfo - the custom user information dictionary for the call from caller. May be nil.
            
        if callByMe {
            
            callByMe = false
            
            if conncetedLabel.text == "Connected."{
                
//                self.messageDelegate?.sendVideoCallMessage("Video Call Duration : \(String(describing: self.time_lbl.text!))")
            }else {
//                self.QBchat(messageString: "Video Call Missed.", dialog: privateRoomInfo)
            }
            
            audioTimer.invalidate()
        }
        
        if self.currentSession != nil {
            let userInfo :[String:String] = ["id": String(User.shared.qBUserID!)]
            
            currentSession?.rejectCall(userInfo)
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func cameraButtonTapped(_ sender: Any) {
    
        self.smallCameraView.clearsContextBeforeDrawing = true
        
        isFirstLayer = false
        
        cameraButton.isSelected = !cameraButton.isSelected

        self.sendVideo(isFront: cameraButton.isSelected)
        
        self.currentMode()
        
    }
    
    @IBAction func videoBtn(_ sender: Any) {
        
        self.currentSession!.localMediaStream.videoTrack.isEnabled = !self.currentSession!.localMediaStream.videoTrack.isEnabled
        self.currentMode()
    }
    
    func currentMode() {
        
        if self.currentSession!.localMediaStream.audioTrack.isEnabled {
            self.currentSession!.localMediaStream.audioTrack.isEnabled = true
            muteBtn.setImage(#imageLiteral(resourceName: "mike on white"), for: .normal)
        }else {
            self.currentSession!.localMediaStream.audioTrack.isEnabled = false
            muteBtn.setImage(#imageLiteral(resourceName: "micOnOff-white"), for: .normal)
        }
        
        if self.currentSession!.localMediaStream.videoTrack.isEnabled {
            self.currentSession!.localMediaStream.videoTrack.isEnabled = true
            VideoBtn.setImage(#imageLiteral(resourceName: "ic_video_on_24dp"), for: .normal)
            self.smallCameraView.isHidden = false
        }else {
            self.currentSession!.localMediaStream.videoTrack.isEnabled = false
            VideoBtn.setImage(#imageLiteral(resourceName: "ic_video_off_white_24dp"), for: .normal)
            self.smallCameraView.isHidden = true
        }
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

