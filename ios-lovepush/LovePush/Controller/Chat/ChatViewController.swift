//
//  ChatViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 10/06/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import Quickblox
import QMServices
import SafariServices
import QMChatViewController
import AGEmojiKeyboard
import IQKeyboardManager
//import ROGoogleTranslate

var messageTimeDateFormatter: DateFormatter {
    struct Static {
        static let instance : DateFormatter = {
            let formatter = DateFormatter()
            formatter.dateFormat = "HH:mm"
            return formatter
        }()
    }
    
    return Static.instance
}

extension String {
    var length: Int {
        return (self as NSString).length
    }
}

class ChatViewController: QMChatViewController, QMChatServiceDelegate, UIActionSheetDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, QMChatAttachmentServiceDelegate, QMChatConnectionDelegate, QMChatCellDelegate, QMDeferredQueueManagerDelegate, QMPlaceHolderTextViewPasteDelegate, RecorderViewDelegate {
    
    //MARK:- Variables
    let maxCharactersNumber = 1024 // 0 - unlimited
    
    var failedDownloads: Set<String> = []
    var dialog: QBChatDialog!
    var willResignActiveBlock: AnyObject?
    var attachmentCellsMap: NSMapTable<AnyObject, AnyObject>!
    var detailedCells: Set<String> = []
    var statusLabel:UILabel!
    var typingTimer: Timer?
    var popoverController: UIPopoverController?
    var user: UserInfo!
    var emojiKeyboardView : AGEmojiKeyboardView!
    var keyboardHeight = 0.0
    var emojViewY = 0.0
    var player :AVAudioPlayer?
    var audioPlayerTimer: Timer?
    var imageArr = [UIImage(named: "clock"),UIImage(named: "smile_ic"),UIImage(named: "giftbox"),UIImage(named: "frog"),UIImage(named: "home"),UIImage(named: "different-currencies")]
    var matchID = 0
    var localDialogID = 0
    var chatStatus    = 1
    var deleteStatus  = 0
    var isBlocked     = false
    var isChatStarted   = false
    
    lazy var imagePickerViewController : UIImagePickerController = {
        let imagePickerViewController = UIImagePickerController()
        imagePickerViewController.delegate = self
        
        return imagePickerViewController
    }()
    
    var unreadMessages: [QBChatMessage]?
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        // top layout inset for collectionView
        
        if self.user != nil {
            self.deleteStatus  = (self.user.deletedBy != 0) ? 1 : 0
        }
        
        self.topContentAdditionalInset = self.navigationController!.navigationBar.frame.size.height + UIApplication.shared.statusBarFrame.size.height;
        
        view.backgroundColor = UIColor.white
        self.collectionView?.backgroundColor = .clear
        
        if let currentUser = ServicesManager.instance().currentUser as? QBUUser {
//            self.getOnlineStatus()
            
            self.senderID = currentUser.id
            self.senderDisplayName = currentUser.login!
            
            ServicesManager.instance().chatService.addDelegate(self)
            ServicesManager.instance().chatService.chatAttachmentService.addDelegate(self)
            
            
//            self.updateTitle()
            
            self.inputToolbar?.isHidden = (self.chatStatus == 0)// || (self.user.blockedBy == self.user.id!)
            
            self.inputToolbar?.contentView?.backgroundColor = UIColor.white
            self.inputToolbar?.contentView?.textView?.placeHolder = "Type Here.."//
            
            
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTapTextView))
            self.inputToolbar?.contentView.textView.addGestureRecognizer(tapGesture)
            
            self.inputToolbar?.contentView.leftBarEmojiButtonItem.addTarget(self, action: #selector(emojiButtonTapped), for: .touchUpInside)
            self.inputToolbar?.contentView.rightBarMikeButtonItem.addTarget(self, action: #selector(micButtonTapped), for: .touchUpInside)
            
            self.attachmentCellsMap = NSMapTable(keyOptions: NSPointerFunctions.Options.strongMemory, valueOptions: NSPointerFunctions.Options.weakMemory)
            
            if self.dialog.type == QBChatDialogType.private {
                
                self.dialog.onUserIsTyping = {
                    [weak self] (userID)-> Void in
                    
                    if ServicesManager.instance().currentUser.id == userID {
                        return
                    }
//                    self?.statusLabel.text = "Typing..."
//                    self?.title = "SA_STR_TYPING"//
                }
                
                self.dialog.onUserStoppedTyping = {
                    [weak self] (userID)-> Void in
                    
                    if ServicesManager.instance().currentUser.id == userID {
                        return
                    }
//                    self?.statusLabel.text = "Online"
//                    self!.getOnlineStatus()
//                    self?.updateTitle()
                }
                
                self.dialog.onUpdateOccupant = {
                    [weak self] (userID)-> Void in
                    
                    if ServicesManager.instance().currentUser.id == userID {
                        return
                    }
//                    self?.statusLabel.text = "Update"
                    //                    self?.updateTitle()
                }
                
                self.dialog.onJoinOccupant = {
                    [weak self] (userID)-> Void in
                    
                    if ServicesManager.instance().currentUser.id == userID {
                        return
                    }
//                    self?.statusLabel.text = "Online"
                }
            }
            
            // Retrieving messages
            let messagesCount = self.storedMessages()?.count
            if (messagesCount == 0) {
                self.startSpinProgress()
            }
            else if (self.chatDataSource.messagesCount() == 0) {
                self.chatDataSource.add(self.storedMessages()!)
            }
            
            self.loadMessages()
            
            self.enableTextCheckingTypes = NSTextCheckingAllTypes
            
            self.registerKeyboardNotifications()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.queueManager().add(self)
//        self.getOnlineStatus()
        self.dialog.join(completionBlock: nil)
        
        self.willResignActiveBlock = NotificationCenter.default.addObserver(forName: NSNotification.Name.UIApplicationWillResignActive, object: nil, queue: nil) { [weak self] (notification) in
            
            self?.fireSendStopTypingIfNecessary()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.dialog.leave(completionBlock: nil)
        self.deRegisterKeyboardNotifications()
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Saving current dialog ID.
        ServicesManager.instance().currentDialogID = self.dialog.id!
        ServicesManager.instance().isChatVC = true
        if (self.user.blockedBy == self.user.id!) {
            self.addBlockedMessageView()
        }
        self.headerViewSetup()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        if let willResignActive = self.willResignActiveBlock {
            NotificationCenter.default.removeObserver(willResignActive)
        }
        
        // Resetting current dialog ID.
        ServicesManager.instance().currentDialogID = ""
        ServicesManager.instance().isChatVC = false
        
        // clearing typing status blocks
        self.dialog.clearTypingStatusBlocks()
        
        self.queueManager().remove(self)
        self.player?.stop()
        self.player = nil
    }
    
    func addBlockedMessageView() {
        let blockedLabel = UILabel()
        
        blockedLabel.text = "This user has blocked you!"
        
        blockedLabel.textColor = UIColor.red
        blockedLabel.textAlignment = .center
        blockedLabel.backgroundColor = UIColor.white
        
//        blockedLabel.topAnchor.constraint(equalTo: self.view.bottomAnchor, constant: -32)
//        blockedLabel.leadingAnchor.constraint(equalTo: self.view.leadingAnchor, constant: 0)
//        blockedLabel.trailingAnchor.constraint(equalTo: self.view.trailingAnchor, constant: 0)
//        blockedLabel.bottomAnchor.constraint(equalTo: self.view.bottomAnchor, constant: 0)
        
        blockedLabel.frame = self.inputToolbar!.contentView.frame
        self.inputToolbar?.contentView?.addSubview(blockedLabel)
    }
    
    
    deinit {
        
        NotificationCenter.default.removeObserver(self)
    }
    
//    func getOnlineStatus() {
//        QBRequest.user(withID: UInt(self.user.qbID!)!, successBlock: { (response, user) in
//            let currentTimeInterval = Int(Date().timeIntervalSince1970)
//            let userLastRequestAtTimeInterval = Int(user.lastRequestAt!.timeIntervalSince1970)
//
//            // if user didn't do anything last 1 minute (60 seconds)
//            if (currentTimeInterval - userLastRequestAtTimeInterval) > 100 {
//                // user is offline now
////                self.statusLabel.text = "Offline"
//            }else {
////                self.statusLabel.text = "Online"
//            }
//        }) { (error) in
////             self.statusLabel.text = "Online"
//        }
//    }
    
    //MARK:- Keyboard Handling
    fileprivate func registerKeyboardNotifications() {
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    @objc fileprivate func deRegisterKeyboardNotifications() {
        NotificationCenter.default.removeObserver(self, name: .UIKeyboardWillShow, object: nil)
        NotificationCenter.default.removeObserver(self, name: .UIKeyboardDidHide, object: nil)
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        DispatchQueue.global().sync {
            if let keyboardSize = (notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
                self.keyboardHeight = Double(keyboardSize.height)
                self.emojViewY = Double((view.frame.size.height - CGFloat(self.keyboardHeight)) + 16)
            }
        }
    }
    
    @objc func keyboardWillHide(sender: NSNotification) {
        //        if self.emojiKeyboardView != nil {
        //           self.hideEmojiView()
        //        }
    }
    
    //MARK:- Header Setup
    func headerViewSetup() {
        let headerView = UIView()
        headerView.frame = CGRect(x: 0, y: 0, width: self.view.frame.size.width, height: 90)
        headerView.backgroundColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
        
        let backButton = UIButton(frame: CGRect(x: 8, y: 45, width: 30, height: 30))
        backButton.setImage(#imageLiteral(resourceName: "backWhiteIcon"), for: .normal)
        backButton.addTarget(self, action: #selector(backButtonTapped), for: .touchUpInside)
        headerView.addSubview(backButton)
        
        let userImageView = UIImageView(frame: CGRect(x: 40, y: 38, width: 46, height: 46))
        
        if self.user.eventType == "L" {
            userImageView.layer.borderColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0).cgColor
        }else if self.user.eventType == "M" {
            userImageView.layer.borderColor = UIColor.darkGray.cgColor
        }else {
            userImageView.layer.borderColor = UIColor.blue.cgColor
        }
        userImageView.layer.borderWidth = 1
        userImageView.layer.cornerRadius = 23
        userImageView.layer.masksToBounds = true
        userImageView.setImageWithoutBaseURL(self.user.imageURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        
        headerView.addSubview(userImageView)
        
        let nameLabel = UILabel(frame: CGRect(x: 90, y: 50, width: 120, height: 22))
        nameLabel.text = user.name!
        nameLabel.font = nameLabel.font.withSize(17.0)
        nameLabel.textColor = UIColor.white
        headerView.addSubview(nameLabel)
        
//        self.statusLabel = UILabel(frame: CGRect(x: 84, y: 60, width: 120, height: 22))
//        self.statusLabel.text = "Online"
//        self.statusLabel.font = statusLabel.font.withSize(12.0)
//        self.statusLabel.textColor = UIColor.white
//        headerView.addSubview(statusLabel)
        
        if (self.user.blockedBy != self.user.id!) {// This user has blocked you, so you can't do audio video  chat with this user
            let videoButton = UIButton(frame: CGRect(x: self.view.frame.size.width - 50, y: 40, width: 30, height: 30))
            videoButton.setImage(#imageLiteral(resourceName: "videoCallIcon"), for: .normal)
            videoButton.addTarget(self, action: #selector(videoButtonTapped), for: .touchUpInside)
            headerView.addSubview(videoButton)
            
            let audioButton = UIButton(frame: CGRect(x: self.view.frame.size.width - 95, y: 40, width: 30, height: 30))
            audioButton.setImage(#imageLiteral(resourceName: "voiceCallIcon"), for: .normal)
            audioButton.addTarget(self, action: #selector(audioButtonTapped), for: .touchUpInside)
            headerView.addSubview(audioButton)
        }
        
        let profileButton = UIButton(frame: CGRect(x: 40, y: 38, width: 50, height: 50))
        
        profileButton.addTarget(self, action: #selector(gotoProfile), for: .touchUpInside)
        headerView.addSubview(profileButton)
        
        self.view.addSubview(headerView)
    }
    
    
    
    
    //MARK:- Header or Footer Button Action
    
    @objc func gotoProfile(_ sender: UITapGestureRecognizer) {
       
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(self.user.id!)"
        vc.isOnlyShow = true
        vc.fromPage = "Chat"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func backButtonTapped(_ sender: UIButton) {
        if (self.chatDataSource.messagesCount() == 0) && (self.dialog.lastMessageID == nil) && !self.isChatStarted {
            if self.localDialogID != 0 {
                self.deleteDialogFromLocalDB("\(self.localDialogID)") { (isSuccess) in
                    QBRequest.deleteDialogs(withIDs: [self.dialog.id!], forAllUsers: true, successBlock: nil, errorBlock: nil)
                    self.navigationController?.popViewController(animated: true)
//                    if isSuccess {
//                        QBRequest.deleteDialogs(withIDs: [self.dialog.id!], forAllUsers: true, successBlock: nil, errorBlock: nil)
//                        self.navigationController?.popViewController(animated: true)
//                    }
                }
            }else {
                self.navigationController?.popViewController(animated: true)
            }
        }else {
            
            if (self.chatDataSource.messagesCount() != 0) && (self.dialog.lastMessageID == nil) && self.isChatStarted{
                print("New User Message")
                self.getUpdatePlanDetails(.messages)
                self.navigationController?.popViewController(animated: true)
            }else {
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    
    
    @objc func videoButtonTapped(_ sender: UIButton) {
        
        if Singleton.shared.isCallInitiated{
            
            self.showAlert(title: "Love Push", message: "Call Already Initiated.", completion: nil)
           
        }else {
            self.isChatStarted = true
            if (self.chatDataSource.messagesCount() == 0) && (self.dialog.lastMessageID == nil) && self.isChatStarted {
                print("New User Message")
                self.getUpdatePlanDetails(.messages)
            }
            
            Singleton.shared.isCallInitiated = true
            
            let opponentName = String(describing: User.shared.username!)
            self.createPayload("\(opponentName) is calling you.")
            
            Singleton.shared.floatingNavigationController = self.navigationController
            
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "VideoCallingViewController") as! VideoCallingViewController
            
            vc.myQuickBloxId = UInt(User.shared.qBUserID)
            
            vc.otherUserQuickBloxId = Int(self.user.qbID!)
            
            vc.otherUserName = self.user.name!
            
            vc.callByMe = true
            
//            vc.messageDelegate = self
            
            vc.isRepresented = false
            
            Singleton.shared.floatingButtonController.currentCallingVC = vc
            
            self.present(vc, animated: false, completion: nil)
        }
    }
    
    @objc func audioButtonTapped(_ sender: UIButton) {
        
        if Singleton.shared.isCallInitiated{
            
            self.showAlert(title: "Love Push", message: "Call Already Initiated.", completion: nil)
            
        }else {
            
            self.isChatStarted = true
            if (self.chatDataSource.messagesCount() == 0) && (self.dialog.lastMessageID == nil) && self.isChatStarted {
                print("New User Message")
                self.getUpdatePlanDetails(.messages)
            }
            
            
            Singleton.shared.isCallInitiated = true
            
            let opponentName = String(describing: User.shared.username!)
            self.createPayload("\(opponentName) is calling you.")
            
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "AudioCallingViewController") as! AudioCallingViewController
            
            vc.myQuickBloxId = UInt(User.shared.qBUserID)
            
            vc.otherUserQuickBloxId = Int(self.user.qbID!)
            
            vc.otherUserName = self.user.name!
            
            vc.callByMe = true
            
            vc.isRepresented = false
            
            Singleton.shared.floatingButtonController.currentCallingVC = vc
            
            self.present(vc, animated: false, completion: nil)
        }
    }
    /**
    And B user delete chat A(me) then A will not be in Message inbox list of B.
    So when either A or B send new message each other then A and B will be in Message inbox list
    So deleted status use request for restore API to change deleted status
    **/
    func restoreDeletedChat() {
        let deletedUserID = ("\(self.user.deletedBy!)" == User.shared.id!) ? "\(self.user.id!)" : "\(User.shared.id!)"
        DispatchQueue.global().sync {
            self.deleteRestoreChatWithUser(deletedUserID, match_id: "\(self.matchID)", status: "0") { (isSuccess) in
            }
        }
    }
    
    /**
     Create Payload to send push notification for Calling
     Notification handle from quickblox Push Notification methods
     - parameter message: Message text to display in notification alert
     **/
    func createPayload(_ message : String) {
        
        let payload = [
                       "message": message,
                       "ios_voip": "1",
                       UsersConstant.voipEvent: "1",
                       "notify_type": "call"
                      ]
        let data = try? JSONSerialization.data(withJSONObject: payload,
                                               options: .prettyPrinted)
        var message = ""
        if let data = data {
            message = String(data: data, encoding: .utf8) ?? ""
        }
        let event = QBMEvent()
        event.notificationType = QBMNotificationType.push
        event.usersIDs = self.user.qbID!
        event.type = QBMEventType.oneShot
        event.message = message
        QBRequest.createEvent(event, successBlock: { response, events in
            print("Send voip push - Success")
        }, errorBlock: { response in
            print("Send voip push - Error")
        })
        
        let pushMessage:QBMPushMessage = QBMPushMessage(payload: payload)
        
        QBRequest.sendVoipPush(pushMessage, toUsers: "\(self.user.qbID!)", successBlock: { (response, event) in
            print(response)
        }) { (error) in
            print(error)
        }
        
        
    }
    
    /**
     Create Payload to send push notification alert when send new message,
     Notification handle from quickblox Push Notification methods
     - parameter message: Message text to display in notification alert
     **/
    func createAlertPayload(_ message : String) {
        
        
        self.isChatStarted = true
        
        let payload = [
                        "message"    : message,
                        "ios_alert"  : "1",
                        UsersConstant.alert : "1",
                        "user_qb_id" : "\(User.shared.qBUserID!)",
                        "user_id"    : "\(User.shared.id!)",
                        "dialog_id"  : "\(self.dialog.id!)",
                        "user_name"  : User.shared.name,
                        "chat_status": "\(User.shared.isEnableChat ? 0 : 1)",
                        "event_type" : self.user.eventType!,
                        "user_image" : User.shared.profileURL!,
                        "notify_type": "chat"
                     ]
        let data = try? JSONSerialization.data(withJSONObject: payload,
                                               options: .prettyPrinted)
        var message = ""
        if let data = data {
            message = String(data: data, encoding: .utf8) ?? ""
        }
        let event = QBMEvent()
        event.notificationType = QBMNotificationType.push
        event.usersIDs = self.user.qbID!
        event.type = QBMEventType.oneShot
        event.message = message
        print(event)
        QBRequest.createEvent(event, successBlock: { response, events in
            print("Send push - Success")
        }, errorBlock: { response in
            print("Send push - Error")
        })
    }
    
    //MARK:- Footer Button Action
    @objc func emojiButtonTapped(_ sender: UIButton) {
        if self.emojiKeyboardView == nil {
            self.setupEmojiView()
        }else {
            self.hideEmojiView()
            self.inputToolbar?.contentView.textView.reloadInputViews()
            self.inputToolbar?.contentView.textView.becomeFirstResponder()
        }
    }
    
    @objc func micButtonTapped(_ sender: UIButton) {
       let recorderView = homeStoryboard.instantiateViewController(withIdentifier: "RecorderViewController") as! RecorderViewController
        recorderView.delegate = self
        recorderView.createRecorder()
        //recorderView.view.backgroundColor = UIColor.green
        recorderView.modalTransitionStyle = .crossDissolve
        recorderView.modalPresentationStyle = UIModalPresentationStyle.overCurrentContext
        self.present(recorderView, animated: true, completion: nil)
        recorderView.startRecording()
    }
    
    func didFinishRecording(_ recorderViewController: RecorderViewController, filePath: URL, duration:Int) {
        print(filePath)
        
        self.sendAudioFile(filePath, duration: duration)
    }
    
    func hideEmojiView() {
        self.emojiKeyboardView.removeFromSuperview()
        self.emojiKeyboardView = nil
        self.inputToolbar?.contentView.textView.inputView = nil
//        self.inputToolbar?.contentView.textView.reloadInputViews()
    }
    
    // MARK:- Quickblox Update
    func updateTitle() {
        
        if self.dialog.type != QBChatDialogType.private {
            
            self.title = self.dialog.name
        }
        else {
            
            if let recipient = ServicesManager.instance().usersService.usersMemoryStorage.user(withID: UInt(self.dialog!.recipientID)) {
                
                self.title = recipient.login
            }
        }
    }
    
    func storedMessages() -> [QBChatMessage]? {
        return ServicesManager.instance().chatService.messagesMemoryStorage.messages(withDialogID: self.dialog.id!)
    }
    
    func loadMessages() {
        // Retrieving messages for chat dialog ID.
        guard let currentDialogID = self.dialog.id else {
            print ("Current chat dialog is nil")
            return
        }
        
        ServicesManager.instance().chatService.messages(withChatDialogID: currentDialogID, completion: {
            [weak self] (response, messages) -> Void in
            
            guard let strongSelf = self else { return }
            
            guard response.error == nil else {
                //SVProgressHUD.showError(withStatus: response.error?.error?Description)
                return
            }
            
            if messages?.count ?? 0 > 0 {
                if !(self?.progressView?.isHidden)! {
                    self?.stopSpinProgress()
                }
                strongSelf.chatDataSource.add(messages)
            }
            
            self?.stopSpinProgress()
            
            //SVProgressHUD.dismiss()
        })
    }
    
    func sendReadStatusForMessage(message: QBChatMessage) {
        
        guard QBSession.current.currentUser != nil else {
            return
        }
        guard message.senderID != QBSession.current.currentUser?.id else {
            return
        }
        
        if self.messageShouldBeRead(message: message) {
            ServicesManager.instance().chatService.read(message, completion: { (error) -> Void in
                
                guard error == nil else {
                    print("Problems while marking message as read! Error: %@", error!)
                    return
                }
                
                if UIApplication.shared.applicationIconBadgeNumber > 0 {
                    let badgeNumber = UIApplication.shared.applicationIconBadgeNumber
                    UIApplication.shared.applicationIconBadgeNumber = badgeNumber - 1
                }
            })
        }
    }
    
    func messageShouldBeRead(message: QBChatMessage) -> Bool {
        
//        let currentUserID = NSNumber(value: QBSession.current.currentUser!.id as UInt)
        
        return !message.isDateDividerMessage
            && message.senderID != self.senderID
    }
    
    func readMessages(messages: [QBChatMessage]) {
        
        if QBChat.instance.isConnected {
            
            ServicesManager.instance().chatService.read(messages, forDialogID: self.dialog.id!, completion: nil)
        }
        else {
            
            self.unreadMessages = messages
        }
        
        var messageIDs = [String]()
        
        for message in messages {
            messageIDs.append(message.id!)
        }
    }
    
    //MARK:- Actions
    
    
    func sendAudioFile(_ fileURL: URL, duration: Int) {
        
        let message: QBChatMessage = QBChatMessage()
        message.senderID = self.senderID
        message.dialogID = self.dialog.id
        message.dateSent = Date()
        do {
            if let audioData = try Data(contentsOf: fileURL) as? Data {
                
                DispatchQueue.main.async(execute: {
                    
                    let fileMimeType = "audio/m4a"
                    
                    let fileName = "audio.m4a"
                    
                    let attachment: QBChatAttachment = QBChatAttachment()
                    
                    attachment.type = "audio"
                    
                    attachment.duration = duration
                    
                    attachment.localFileURL = fileURL
                    
                    message.attachments = [attachment]
                    message.text = "audio"
                    
                    let params : NSMutableDictionary = NSMutableDictionary()
                    params["save_to_history"] = true
                    params["uploadPercentage"] = 0.0
                    params["event_type"] = "1"
                    message.customParameters = params
                    message.attachmentStatus = .uploading
                    
                    
                    self.chatDataSource.add(message)
                    
                    QBRequest.tUploadFile(audioData as Data, fileName: fileName, contentType: fileMimeType, isPublic: true,  successBlock: {(response: QBResponse!, blob: QBCBlob!) in
                        
                        attachment.name = blob.name
                        
                        attachment.type = "audio"
                        
                        attachment.url = blob.publicUrl()
                        
                        attachment.id = String(describing: blob.id)
                        
                        attachment.duration = duration
                        
                        attachment.localFileURL = fileURL
                        
                        message.attachments = [attachment]
                        message.text = "audio"
                        
                        let params : NSMutableDictionary = NSMutableDictionary()
                        params["save_to_history"] = true
                        message.attachmentStatus = .loaded
                        message.customParameters = params
                        
                        if self.deleteStatus == 1 {
                            self.deleteStatus = 2
                            self.restoreDeletedChat()
                        }//Restore Chat If  Deleted
                        
//                        self.saveAudio(with: "\(message.id!).m4a", data: audioData as Data)
                        //Send push if user notification enabled and user not blocked me
                        if self.user.isNotificationEnable && self.user.blockedBy != self.user.id! {
                            let opponentName = String(describing: User.shared.username!)
                            self.createAlertPayload("\(opponentName) sent audio.")
                        }
                        
                        ServicesManager.instance().chatService.sendAttachmentMessage(message, to: self.dialog, with: attachment, completion: { (error) in
                            if error != nil {
                                self.showAlert(title: "Love Push", message: "Audio Sending Falied", completion: nil)
                            }else {
                                self.attachmentCellsMap.removeObject(forKey: message.id as AnyObject?)
                                
                                guard error != nil else { return }
                                
                                self.chatDataSource.delete(message)
                                
                            }
                        })
                        
                    }, statusBlock: {(request: QBRequest?, status: QBRequestStatus?) in
                                            print(status.debugDescription)
                        params["uploadPercentage"] = status?.percentOfCompletion
                        message.customParameters = params
                        self.chatDataSource.update(message)
                    }, errorBlock: {(response: QBResponse!) in
                        print("audio error:- ", response.error.debugDescription)
                    })
                })
            }
        }catch{}
    }
    
    override func didPickAttachmentImage(_ image: UIImage) {
        
        let message = QBChatMessage()
        message.senderID = self.senderID
        message.dialogID = self.dialog.id
        message.dateSent = Date()
        let params = NSMutableDictionary()
        params["event_type"] = "1"
        message.customParameters = params
        DispatchQueue.global().async { [weak self] () -> Void in
            
            guard let strongSelf = self else { return }
            
            var newImage : UIImage! = image
            if newImage != nil {
                
                if strongSelf.imagePickerViewController.sourceType == UIImagePickerControllerSourceType.camera {
                    newImage = newImage.fixedOrientation()
                }
                
                let largestSide = newImage.size.width > newImage.size.height ? newImage.size.width : newImage.size.height
                let scaleCoeficient = largestSide/560.0
                let newSize = CGSize(width: newImage.size.width/scaleCoeficient, height: newImage.size.height/scaleCoeficient)
                
                if self!.deleteStatus == 1 {
                    self!.deleteStatus = 2
                    self!.restoreDeletedChat()
                }//Restore Chat If  Deleted
                
                // create smaller image
                
                UIGraphicsBeginImageContext(newSize)
                
                newImage.draw(in: CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
                let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
                
                UIGraphicsEndImageContext()
                
                // Sending attachment.
                DispatchQueue.main.async(execute: {
                    self?.chatDataSource.add(message)
                    // sendAttachmentMessage method always firstly adds message to memory storage
                    message.text = "image"
                    self?.deleteStatus = (self?.deleteStatus == 1) ? 2 : 0 //If Chat Deleted By Other User
                    ServicesManager.instance().chatService.sendAttachmentMessage(message, to: self!.dialog, withAttachmentImage: resizedImage!, completion: {
                        [weak self] (error) -> Void in
                        
                        self?.attachmentCellsMap.removeObject(forKey: message.id as AnyObject?)
                        
                        //Send push if user notification enabled and user not blocked me
                        if self!.user.isNotificationEnable && self!.user.blockedBy != self!.user.id! {
                            let opponentName = String(describing: User.shared.username!)
                            self!.createAlertPayload("\(opponentName) sent photo.")
                        }
                        guard error != nil else { return }
                        
                        self?.chatDataSource.delete(message)
                    })
                })
            }else {
                self?.showAlert(title: "Love Push", message: "Some Error occured to pick this photo.", completion: nil)
            }
        }
    }
    
    override func didPressSend(_ button: UIButton!, withMessageText text: String!, senderId: UInt, senderDisplayName: String!, date: Date!) {
        
        if !self.queueManager().shouldSendMessagesInDialog(withID: self.dialog.id!) {
            return
        }
        self.fireSendStopTypingIfNecessary()
        
        let message = QBChatMessage()
        message.text = text
        message.senderID = self.senderID
        message.deliveredIDs = [(NSNumber(value: self.senderID))]
        message.readIDs = [(NSNumber(value: self.senderID))]
        message.markable = true
        message.dateSent = date
        message.messageType = .text
        
        self.sendMessage(message: message)
    }
    
    override func didPressSend(_ button: UIButton!, withTextAttachments textAttachments: [Any]!, senderId: UInt, senderDisplayName: String!, date: Date!) {
        
        if let attachment = textAttachments.first as? NSTextAttachment {
            
            if (attachment.image != nil) {
                let message = QBChatMessage()
                message.senderID = self.senderID
                message.dialogID = self.dialog.id
                message.dateSent = Date()
                ServicesManager.instance().chatService.sendAttachmentMessage(message, to: self.dialog, withAttachmentImage: attachment.image!, completion: {
                    [weak self] (error: Error?) -> Void in
                    
                    self?.attachmentCellsMap.removeObject(forKey: message.id as AnyObject?)
                    
                    guard error != nil else { return }
                    
                    // perform local attachment message deleting if error
                    ServicesManager.instance().chatService.deleteMessageLocally(message)
                    
                    self?.chatDataSource.delete(message)
                    
                })
                
                self.finishSendingMessage(animated: true)
            }
        }
    }
    
    func sendMessage(message: QBChatMessage) {
        
        ServicesManager.instance().chatService.send(message, type: QMMessageType.text, to: self.dialog, saveToHistory: true, saveToStorage: true)
        if self.deleteStatus == 1 {
            self.deleteStatus = 2
            self.restoreDeletedChat()
        }//Restore Chat If  Deleted
        //Send push if user notification enabled and user not blocked me
        if self.user.isNotificationEnable && self.user.blockedBy != self.user.id! {
            let opponentName = String(describing: User.shared.username!)
            self.createAlertPayload("\(opponentName) sent message.")
        }
        self.finishSendingMessage(animated: true)
    }
    
    // MARK: Helper
    
    func placeHolderTextView(_ textView: QMPlaceHolderTextView, shouldPasteWithSender sender: Any) -> Bool {
        
        if UIPasteboard.general.image != nil {
            
            let textAttachment = NSTextAttachment()
            textAttachment.image = UIPasteboard.general.image!
            textAttachment.bounds = CGRect(x: 0, y: 0, width: 100, height: 100)
            
            let attrStringWithImage = NSAttributedString.init(attachment: textAttachment)
            self.inputToolbar?.contentView.textView.attributedText = attrStringWithImage
            self.textViewDidChange((self.inputToolbar?.contentView.textView)!)
            
            return false
        }
        
        return true
    }
    
    func showCharactersNumberError() {
        let title  = "SA_STR_ERROR";
        let subtitle = String(format: "The character limit is %lu.", maxCharactersNumber)
        QMMessageNotificationManager.showNotification(withTitle: title, subtitle: subtitle, type: .error)
    }
    
    /**
     Builds a string
     Read: login1, login2, login3
     Delivered: login1, login3, @12345
     
     If user does not exist in usersMemoryStorage, then ID will be used instead of login
     
     - parameter message: QBChatMessage instance
     
     - returns: status string
     */
    func statusStringFromMessage(message: QBChatMessage) -> String {
        
        var statusString = ""
        
        let currentUserID = NSNumber(value:self.senderID)
        
        var readLogins: [String] = []
        
        if message.readIDs != nil {
            
            let messageReadIDs = message.readIDs!.filter { (element) -> Bool in
                
                return !element.isEqual(to: currentUserID)
            }
            
            if !messageReadIDs.isEmpty {
                for readID in messageReadIDs {
                    let user = ServicesManager.instance().usersService.usersMemoryStorage.user(withID: UInt(truncating: readID))
                    
                    guard let unwrappedUser = user else {
                        let unknownUserLogin = "@\(readID)"
                        readLogins.append(unknownUserLogin)
                        
                        continue
                    }
                    
                    readLogins.append(unwrappedUser.login!)
                }
                
//                statusString += message.isMediaMessage() ? "SA_STR_SEEN_STATUS": "SA_STR_READ_STATUS";
//                statusString += ": " + readLogins.joined(separator: ", ")
            }
        }
        
        if message.deliveredIDs != nil {
            var deliveredLogins: [String] = []
            
            let messageDeliveredIDs = message.deliveredIDs!.filter { (element) -> Bool in
                return !element.isEqual(to: currentUserID)
            }
            
            if !messageDeliveredIDs.isEmpty {
                for deliveredID in messageDeliveredIDs {
                    let user = ServicesManager.instance().usersService.usersMemoryStorage.user(withID: UInt(truncating: deliveredID))
                    
                    guard let unwrappedUser = user else {
                        let unknownUserLogin = "@\(deliveredID)"
                        deliveredLogins.append(unknownUserLogin)
                        
                        continue
                    }
                    
                    if readLogins.contains(unwrappedUser.login!) {
                        continue
                    }
                    
                    deliveredLogins.append(unwrappedUser.login!)
                    
                }
                
//                if readLogins.count > 0 && deliveredLogins.count > 0 {
//                    statusString += "\n"
//                }
//
//                if deliveredLogins.count > 0 {
//                    statusString += "SA_STR_DELIVERED_STATUS" + ": " + deliveredLogins.joined(separator: ", ")
//                }
            }
        }
        
//        if statusString.isEmpty {
//
//            let messageStatus: QMMessageStatus = self.queueManager().status(for: message)
//
//            switch messageStatus {
//            case .sent:
//                statusString = "SA_STR_SENT_STATUS"
//            case .sending:
//                statusString = "SA_STR_SENDING_STATUS"
//            case .notSent:
//                statusString = "SA_STR_NOT_SENT_STATUS"
//            }
//
//        }
        
        return statusString
    }
    
    // MARK: Override
    
    override func viewClass(forItem item: QBChatMessage) -> AnyClass {
        // TODO: check and add QMMessageType.AcceptContactRequest, QMMessageType.RejectContactRequest, QMMessageType.ContactRequest
        
        if item.isNotificationMessage() || item.isDateDividerMessage {
            return QMChatNotificationCell.self
        }
        
        if (item.senderID != self.senderID) {
            
            if (item.isMediaMessage() && item.attachmentStatus != QMMessageAttachmentStatus.error) {
                
                if item.attachments?.first?.type == "image" {
                     return QMChatAttachmentIncomingCell.self
                }else {
                    return QMAudioIncomingCell.self
                }
            }else {
                return QMChatIncomingCell.self
            }
        }else {
            if (item.isMediaMessage() && item.attachmentStatus != QMMessageAttachmentStatus.error) {
                
                if item.attachments?.first?.type == "image" {
                    return QMChatAttachmentOutgoingCell.self
                }else {
                    return QMAudioOutgoingCell.self
                }
            }else {
                return QMChatOutgoingCell.self
            }
        }
    }
    
    // MARK: Strings builder
    
    override func attributedString(forItem messageItem: QBChatMessage!) -> NSAttributedString? {
        
        guard messageItem.text != nil else {
            return nil
        }
        
        var textColor = UIColor.darkGray //messageItem.senderID == self.senderID ? UIColor.white : UIColor.black
        if messageItem.isNotificationMessage() || messageItem.isDateDividerMessage {
            textColor = UIColor.black
        }
        
        var attributes = Dictionary<NSAttributedStringKey, AnyObject>()
        attributes[NSAttributedStringKey.foregroundColor] = textColor
        attributes[NSAttributedStringKey.font] = UIFont(name: "Helvetica", size: 17)
        
        let attributedString = NSAttributedString(string: messageItem.text!, attributes: attributes)
        
        return attributedString
    }
    
    
    /**
     Creates top label attributed string from QBChatMessage
     
     - parameter messageItem: QBCHatMessage instance
     
     - returns: login string, example: @SwiftTestDevUser1
     */
    override func topLabelAttributedString(forItem messageItem: QBChatMessage!) -> NSAttributedString? {
        
        guard messageItem.senderID != self.senderID else {
            return nil
        }
        
        guard self.dialog.type != QBChatDialogType.private else {
            return nil
        }
        
        let paragrpahStyle: NSMutableParagraphStyle = NSMutableParagraphStyle()
        paragrpahStyle.lineBreakMode = NSLineBreakMode.byTruncatingTail
        var attributes = Dictionary<NSAttributedStringKey, AnyObject>()
        attributes[NSAttributedStringKey.foregroundColor] = UIColor(red: 11.0/255.0, green: 96.0/255.0, blue: 255.0/255.0, alpha: 1.0)
        attributes[NSAttributedStringKey.font] = UIFont(name: "Helvetica", size: 17)
        attributes[NSAttributedStringKey.paragraphStyle] = paragrpahStyle
        
        var topLabelAttributedString : NSAttributedString?
        
        if let topLabelText = ServicesManager.instance().usersService.usersMemoryStorage.user(withID: messageItem.senderID)?.login {
            topLabelAttributedString = NSAttributedString(string: topLabelText, attributes: attributes)
        } else { // no user in memory storage
            topLabelAttributedString = NSAttributedString(string: "@\(messageItem.senderID)", attributes: attributes)
        }
        
        return topLabelAttributedString
    }
    
    /**
     Creates bottom label attributed string from QBChatMessage using self.statusStringFromMessage
     
     - parameter messageItem: QBChatMessage instance
     
     - returns: bottom label status string
     */
    override func bottomLabelAttributedString(forItem messageItem: (QBChatMessage?)) -> (NSAttributedString?) {
        
        let textColor = UIColor.darkGray//messageItem.senderID == self.senderID ? UIColor.white : UIColor.black
        
        let paragrpahStyle: NSMutableParagraphStyle = NSMutableParagraphStyle()
        paragrpahStyle.lineBreakMode = NSLineBreakMode.byWordWrapping
        
        var attributes = Dictionary<NSAttributedStringKey, AnyObject>()
        attributes[NSAttributedStringKey.foregroundColor] = textColor
        attributes[NSAttributedStringKey.font] = UIFont(name: "Helvetica", size: 13)
        attributes[NSAttributedStringKey.paragraphStyle] = paragrpahStyle
        
        let text = messageItem!.dateSent != nil ? messageTimeDateFormatter.string(from: messageItem!.dateSent!) : ""
        
//        if messageItem.senderID == self.senderID {
//            text = text + "\n" + self.statusStringFromMessage(message: messageItem)
//        }
        
        let bottomLabelAttributedString = NSAttributedString(string: text, attributes: attributes)
        
        return bottomLabelAttributedString
    }
    
    // MARK: Collection View Datasource
    
    override func collectionView(_ collectionView: QMChatCollectionView!, dynamicSizeAt indexPath: IndexPath!, maxWidth: CGFloat) -> CGSize {
        
        var size = CGSize.zero
        
        guard let message = self.chatDataSource.message(for: indexPath) else {
            return size
        }
        
        let messageCellClass: AnyClass! = self.viewClass(forItem: message)
        
        
        if messageCellClass === QMAudioIncomingCell.self {
            
            size = CGSize(width: min(200, maxWidth), height: 80)
        }else if messageCellClass === QMAudioOutgoingCell.self {
            
            size = CGSize(width: min(200, maxWidth), height: 80)
        }else if messageCellClass === QMChatAttachmentIncomingCell.self {
            
            size = CGSize(width: min(200, maxWidth), height: 200)
        }else if messageCellClass === QMChatAttachmentOutgoingCell.self {
            
            let attributedString = self.bottomLabelAttributedString(forItem: message)
            
            let bottomLabelSize = TTTAttributedLabel.sizeThatFitsAttributedString(attributedString, withConstraints: CGSize(width: min(200, maxWidth), height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines: 0)
            size = CGSize(width: min(200, maxWidth), height: 200 + ceil(bottomLabelSize.height))
        }else if messageCellClass === QMChatNotificationCell.self {
            
            let attributedString = self.attributedString(forItem: message)
            size = TTTAttributedLabel.sizeThatFitsAttributedString(attributedString, withConstraints: CGSize(width: maxWidth, height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines: 0)
        }else {
            
            let attributedString = self.attributedString(forItem: message)
            
            size = TTTAttributedLabel.sizeThatFitsAttributedString(attributedString, withConstraints: CGSize(width: maxWidth, height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines: 0)
        }
        
        return size
    }
    
    override func collectionView(_ collectionView: QMChatCollectionView!, minWidthAt indexPath: IndexPath!) -> CGFloat {
        
        var size = CGSize.zero
        
        guard let item = self.chatDataSource.message(for: indexPath) else {
            return 0
        }
        
        if self.detailedCells.contains(item.id!) {
            
            let str = self.bottomLabelAttributedString(forItem: item)
            let frameWidth = collectionView.frame.width/2
            let maxHeight = CGFloat.greatestFiniteMagnitude
            
            size = TTTAttributedLabel.sizeThatFitsAttributedString(str, withConstraints: CGSize(width:frameWidth - 40.0, height: maxHeight), limitedToNumberOfLines:0)
        }
        
        if self.dialog.type != QBChatDialogType.private {
            
            let topLabelSize = TTTAttributedLabel.sizeThatFitsAttributedString(self.topLabelAttributedString(forItem: item), withConstraints: CGSize(width: collectionView.frame.width/2 - 40.0, height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines:0)
            
            if topLabelSize.width > size.width {
                size = topLabelSize
            }
        }
        
        return size.width
    }
    
    override func collectionView(_ collectionView: QMChatCollectionView!, layoutModelAt indexPath: IndexPath!) -> QMChatCellLayoutModel {
        
        var layoutModel: QMChatCellLayoutModel = super.collectionView(collectionView, layoutModelAt: indexPath)
        
        layoutModel.avatarSize = CGSize(width: 0, height: 0)
        layoutModel.topLabelHeight = 0.0
        layoutModel.spaceBetweenTextViewAndBottomLabel = 5
        layoutModel.maxWidthMarginSpace = 20.0
        
        guard let item = self.chatDataSource.message(for: indexPath) else {
            return layoutModel
        }
        
        let viewClass: AnyClass = self.viewClass(forItem: item) as AnyClass
        
        if viewClass === QMChatIncomingCell.self || viewClass === QMChatAttachmentIncomingCell.self || viewClass === QMAudioIncomingCell.self {
            
            if self.dialog.type != QBChatDialogType.private {
                let topAttributedString = self.topLabelAttributedString(forItem: item)
                let size = TTTAttributedLabel.sizeThatFitsAttributedString(topAttributedString, withConstraints: CGSize(width: collectionView.frame.width/2 - 40.0, height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines:1)
                layoutModel.topLabelHeight = size.height
            }
            
            layoutModel.spaceBetweenTopLabelAndTextView = 5
        }
        
        var size = CGSize.zero
        
        if self.detailedCells.contains(item.id!) {
            
            let bottomAttributedString = self.bottomLabelAttributedString(forItem: item)
            size = TTTAttributedLabel.sizeThatFitsAttributedString(bottomAttributedString, withConstraints: CGSize(width: collectionView.frame.width/2 - 40.0, height: CGFloat.greatestFiniteMagnitude), limitedToNumberOfLines:0)
        }
        
        layoutModel.bottomLabelHeight = floor(size.height)
        
        
        return layoutModel
    }
    
    override func collectionView(_ collectionView: QMChatCollectionView!, configureCell cell: UICollectionViewCell!, for indexPath: IndexPath!) {
        
        super.collectionView(collectionView, configureCell: cell, for: indexPath)
        
        // subscribing to cell delegate
        let chatCell = cell as! QMChatCell
        
        chatCell.delegate = self
        
        let message = self.chatDataSource.message(for: indexPath)
        
        if cell as? QMAudioIncomingCell != nil || cell as? QMAudioOutgoingCell != nil {
            
            let attachmentCell = (cell as? QMAudioIncomingCell != nil) ? cell as? QMAudioIncomingCell : cell as? QMAudioOutgoingCell
            
            if attachmentCell is QMAudioIncomingCell {
                chatCell.containerView?.bgColor = UIColor(red: 226.0/255.0, green: 226.0/255.0, blue: 226.0/255.0, alpha: 1.0)
            }else if attachmentCell is QMAudioOutgoingCell {
                chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
            }
            
            if let attachment = message?.attachments?.first {

//                self.attachmentCellsMap.setObject(attachmentCell, forKey: attachment.id as AnyObject?)
                self.hmsFrom(duration: attachment.duration) { hours, minutes, seconds in
                    
                    let hours = self.getStringFrom(hours)
                    
                    let minutes = self.getStringFrom(minutes)
                    
                    let seconds = self.getStringFrom(seconds)
                    
                    let recordTimeStr = "\(hours):\(minutes):\(seconds)"
                    
                    attachmentCell?.durationLabel.text = recordTimeStr
                    
                    if message?.attachmentStatus.rawValue == 2 {
                        attachmentCell?.mediaPlayButton.setImage(#imageLiteral(resourceName: "mikeIcon"), for: .normal)
                        attachmentCell?.mediaPlayButton.loadingIndicator(true)
                    }else {
                        self.getDownloadedFilePath(message!, completion: { (fileURL) in
                            attachmentCell?.mediaPlayButton.loadingIndicator(false)
                            if fileURL != nil {
                                
                                attachmentCell?.mediaPlayButton.setImage(#imageLiteral(resourceName: "play-button"), for: .normal)
                                
                            }else {
                             //   attachmentCell?.mediaPlayButton.setImage(#imageLiteral(resourceName: "send_ic"), for: .normal)
                            }
                            attachmentCell?.mediaPlayButton.tag = indexPath.row
                            attachmentCell?.mediaPlayButton.accessibilityHint = "\(indexPath.section)"
                            
                            attachmentCell?.mediaPlayButton.addTarget(self, action: #selector(self.audioPlayButtonTapped), for: .touchUpInside)
                        })
                    }
                }
                
             cell.updateConstraints()
//                ServicesManager.instance().chatService.chatAttachmentService.attachment(withID: attachment.id!, message: message!, progressBlock: { (progress) in
//                    attachmentCell?.durationLabel.text = "\(progress)"
//                    cell.updateConstraints()
//                }) { (attOperation) in
////                    print(attOperation.error)
//                }
            }
            
           
            
        }else if let attachmentCell = cell as? QMChatAttachmentCell {
            
            if attachmentCell is QMChatAttachmentIncomingCell {
                chatCell.containerView?.bgColor = UIColor(red: 226.0/255.0, green: 226.0/255.0, blue: 226.0/255.0, alpha: 1.0)
            }else if attachmentCell is QMChatAttachmentOutgoingCell {
                chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
            }
            
            if let attachment = message?.attachments?.first {
                
                var keysToRemove: [String] = []
                
                let enumerator = self.attachmentCellsMap.keyEnumerator()
                
                while let existingAttachmentID = enumerator.nextObject() as? String {
                    let cachedCell = self.attachmentCellsMap.object(forKey: existingAttachmentID as AnyObject?)
                    if cachedCell === cell {
                        keysToRemove.append(existingAttachmentID)
                    }
                }
                
                for key in keysToRemove {
                    self.attachmentCellsMap.removeObject(forKey: key as AnyObject?)
                }
                
                if let attachmentID = attachment.id {
                    if self.failedDownloads.contains(attachmentID) {
                        attachmentCell.setAttachmentImage(UIImage(named:"error_image"))
                        return
                    }
                }
                
                self.attachmentCellsMap.setObject(attachmentCell, forKey: attachment.id as AnyObject?)
                
                attachmentCell.attachmentID = attachment.id
        
                // Getting image from chat attachment cache.
                
                attachmentCell.setAttachmentImage(#imageLiteral(resourceName: "AppIconSquare"))
                
//              attachmentCell.setAttachmentMessageText("s dfsaf asfsas")
                
                ServicesManager.instance().chatService.chatAttachmentService.image(forAttachmentMessage: message!, completion: { [weak self] (error, image) in
                    
                    guard attachmentCell.attachmentID == attachment.id else {
                        return
                    }
                    
                    self?.attachmentCellsMap.removeObject(forKey: attachment.id as AnyObject?)
                    
                    guard error == nil else {
                        if (error! as NSError).code == 404 {
                            self?.failedDownloads.insert(attachment.id!)
                            
                            attachmentCell.setAttachmentImage(#imageLiteral(resourceName: "default_image"))
                        }
                        print("Error downloading image from server: \(error!.localizedDescription)")
                        return
                    }
                    
                    if image == nil {
                        print("Image is nil")
                    }
                    
                    attachmentCell.setAttachmentImage(image)
                    cell.updateConstraints()
                })
            }
            
        }else if cell is QMChatIncomingCell || cell is QMChatAttachmentIncomingCell {
            
            chatCell.containerView?.bgColor = UIColor(red: 226.0/255.0, green: 226.0/255.0, blue: 226.0/255.0, alpha: 1.0)
        }else if cell is QMChatOutgoingCell {
            
            let status: QMMessageStatus = self.queueManager().status(for: message!)
            
            switch status {
            case .sent:
                chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
            case .sending:
                chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
            case .notSent:
                chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
            }
            
        }else if cell is QMChatAttachmentOutgoingCell {
            chatCell.containerView?.bgColor = UIColor(red: 247.0/255.0, green: 229.0/255.0, blue: 231.0/255.0, alpha: 1.0)
        }else if cell is QMChatNotificationCell {
            cell.isUserInteractionEnabled = false
            chatCell.containerView?.bgColor = self.collectionView?.backgroundColor
        }
    }
    
    /**
     Allows to copy text from QMChatIncomingCell and QMChatOutgoingCell
     */
    override func collectionView(_ collectionView: UICollectionView, canPerformAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) -> Bool {
        
        guard let item = self.chatDataSource.message(for: indexPath) else {
            return false
        }
        
        let viewClass: AnyClass = self.viewClass(forItem: item) as AnyClass
        
        if  viewClass === QMChatNotificationCell.self ||
            viewClass === QMChatContactRequestCell.self {
            return false
        }
        
        return super.collectionView(collectionView, canPerformAction: action, forItemAt: indexPath, withSender: sender)
    }
    
    override func collectionView(_ collectionView: UICollectionView, performAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) {
        
        let item = self.chatDataSource.message(for: indexPath)
        
        if (item?.isMediaMessage())! {
            ServicesManager.instance().chatService.chatAttachmentService.localImage(forAttachmentMessage: item!, completion: { (image) in
                
                if image != nil {
                    guard let imageData = UIImageJPEGRepresentation(image!, 1) else { return }
                    
                    let pasteboard = UIPasteboard.general
                    
//                    pasteboard.setValue(imageData, forPasteboardType:kUTType.JPEG as String)
                }
            })
        }
        else {
            UIPasteboard.general.string = item?.text
        }
    }
    
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let lastSection = self.collectionView!.numberOfSections - 1
        
        if (indexPath.section == lastSection && indexPath.item == (self.collectionView?.numberOfItems(inSection: lastSection))! - 1) {
            // the very first message
            // load more if exists
            // Getting earlier messages for chat dialog identifier.
            
            guard let dialogID = self.dialog.id else {
                print("DialogID is nil")
                return super.collectionView(collectionView, cellForItemAt: indexPath)
            }
            
            ServicesManager.instance().chatService.loadEarlierMessages(withChatDialogID: dialogID).continueWith(block: {[weak self](task) -> Any? in
                
                guard let strongSelf = self else { return nil }
                
                if (task.result?.count ?? 0 > 0) {
                    
                    strongSelf.chatDataSource.add(task.result as! [QBChatMessage]!)
                }
                
                return nil
            })
        }
        
        // marking message as read if needed
        if let message = self.chatDataSource.message(for: indexPath) {
            self.sendReadStatusForMessage(message: message)
        }
        
        return super.collectionView(collectionView, cellForItemAt
            : indexPath)
    }
    
    // MARK: QMChatCellDelegate
    
    /**
     Removes size from cache for item to allow cell expand and show read/delivered IDS or unexpand cell
     */
    func chatCellDidTapContainer(_ cell: QMChatCell!) {
        let indexPath = self.collectionView?.indexPath(for: cell)
        
        guard let currentMessage = self.chatDataSource.message(for: indexPath) else {
            return
        }
        
        let messageStatus: QMMessageStatus = self.queueManager().status(for: currentMessage)
        
        if messageStatus == .notSent {
            self.handleNotSentMessage(currentMessage, forCell:cell)
            return
        }
        
        if self.detailedCells.contains(currentMessage.id!) {
            self.detailedCells.remove(currentMessage.id!)
        } else {
            self.detailedCells.insert(currentMessage.id!)
        }
        
        self.collectionView?.collectionViewLayout.removeSizeFromCache(forItemID: currentMessage.id)
        self.collectionView?.performBatchUpdates(nil, completion: nil)
    }
    
    func chatCell(_ cell: QMChatCell!, didTapAtPosition position: CGPoint) {
        print(cell)
    }
    
    func chatCell(_ cell: QMChatCell!, didPerformAction action: Selector!, withSender sender: Any!) {
        print(cell)
    }
    
    func chatCell(_ cell: QMChatCell!, didTapOn result: NSTextCheckingResult) {
        
        switch result.resultType {
            
        case NSTextCheckingResult.CheckingType.link:
            
            let strUrl : String = (result.url?.absoluteString)!
            
            let hasPrefix = strUrl.lowercased().hasPrefix("https://") || strUrl.lowercased().hasPrefix("http://")
            
            if #available(iOS 9.0, *) {
                if hasPrefix {
                    
                    let controller = SFSafariViewController(url: URL(string: strUrl)!)
                    self.present(controller, animated: true, completion: nil)
                    
                    break
                }
                
            }
            // Fallback on earlier versions
            
            if UIApplication.shared.canOpenURL(URL(string: strUrl)!) {
                
                UIApplication.shared.openURL(URL(string: strUrl)!)
            }
            
            break
            
        case NSTextCheckingResult.CheckingType.phoneNumber:
            
//            if !self.canMakeACall() {
//
////                SVProgressHUD.showInfo(withStatus: "Your Device can't make a phone call", maskType: .none)
//                break
//            }
            
            let urlString = String(format: "tel:%@",result.phoneNumber!)
            let url = URL(string: urlString)
            
            self.view.endEditing(true)
            
            let alertController = UIAlertController(title: "",
                                                    message: result.phoneNumber,
                                                    preferredStyle: .alert)
            
            let cancelAction = UIAlertAction(title: "SA_STR_CANCEL", style: .cancel) { (action) in
                
            }
            
            alertController.addAction(cancelAction)
            
            let openAction = UIAlertAction(title: "SA_STR_CALL", style: .destructive) { (action) in
                UIApplication.shared.openURL(url!)
            }
            alertController.addAction(openAction)
            
            self.present(alertController, animated: true) {
            }
            
            break
            
        default:
            break
        }
    }
    
    func chatCellDidTapAvatar(_ cell: QMChatCell!) {
    }
    
    // MARK: QMDeferredQueueManager
    
    func deferredQueueManager(_ queueManager: QMDeferredQueueManager, didAddMessageLocally addedMessage: QBChatMessage) {
        
        if addedMessage.dialogID == self.dialog.id {
            self.chatDataSource.add(addedMessage)
        }
    }
    
    func deferredQueueManager(_ queueManager: QMDeferredQueueManager, didUpdateMessageLocally addedMessage: QBChatMessage) {
        
        if addedMessage.dialogID == self.dialog.id {
            self.chatDataSource.update(addedMessage)
        }
    }
    
    // MARK: QMChatServiceDelegate
    
    func chatService(_ chatService: QMChatService, didLoadMessagesFromCache messages: [QBChatMessage], forDialogID dialogID: String) {
        
        if self.dialog.id == dialogID {
            if !(self.progressView?.isHidden)! {
                self.stopSpinProgress()
            }
            self.chatDataSource.add(messages)
        }
    }
    
    func chatService(_ chatService: QMChatService, didAddMessageToMemoryStorage message: QBChatMessage, forDialogID dialogID: String) {
        
        if self.dialog.id == dialogID {
            // Insert message received from XMPP or self sent
            if self.chatDataSource.messageExists(message) {
                
                self.chatDataSource.update(message)
            }
            else {
                
                self.chatDataSource.add(message)
            }
        }
    }
    
    func chatService(_ chatService: QMChatService, didUpdateChatDialogInMemoryStorage chatDialog: QBChatDialog) {
        
        if self.dialog.type != QBChatDialogType.private && self.dialog.id == chatDialog.id {
            self.dialog = chatDialog
            self.title = self.dialog.name
        }
    }
    
    func chatService(_ chatService: QMChatService, didUpdate message: QBChatMessage, forDialogID dialogID: String) {
        
        if self.dialog.id == dialogID {
            self.chatDataSource.update(message)
        }
    }
    
    func chatService(_ chatService: QMChatService, didUpdate messages: [QBChatMessage], forDialogID dialogID: String) {
        
        if self.dialog.id == dialogID {
            self.chatDataSource.update(messages)
        }
    }
    
    // MARK: UITextViewDelegate
    
    @objc func didTapTextView(_ sender: UITapGestureRecognizer) {
        if self.emojiKeyboardView != nil {
           self.hideEmojiView()
        }
        self.inputToolbar?.contentView.textView.reloadInputViews()
        self.inputToolbar?.contentView.textView.becomeFirstResponder()
    }
    
    override func textViewDidBeginEditing(_ textView: UITextView) {
    }
    
    override func textViewDidChange(_ textView: UITextView) {
        super.textViewDidChange(textView)
    }
    
    override func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        
        // Prevent crashing undo bug
        let currentCharacterCount = textView.text?.length ?? 0
        
        if (range.length + range.location > currentCharacterCount) {
            return false
        }
        
        if !QBChat.instance.isConnected { return true }
        
        if let timer = self.typingTimer {
            timer.invalidate()
            self.typingTimer = nil
        } //else {
            self.sendBeginTyping()
//        }
        
//        self.textChangeLangauge(textView)
        
        self.typingTimer = Timer.scheduledTimer(timeInterval: 4.0, target: self, selector: #selector(ChatViewController.fireSendStopTypingIfNecessary), userInfo: nil, repeats: false)
        
        if maxCharactersNumber > 0 {
            
            if currentCharacterCount >= maxCharactersNumber && text.length > 0 {
                
                self.showCharactersNumberError()
                return false
            }
            
            let newLength = currentCharacterCount + text.length - range.length
            
            if  newLength <= maxCharactersNumber || text.length == 0 {
                return true
            }
            
            let oldString = textView.text ?? ""
            
            let numberOfSymbolsToCut = maxCharactersNumber - oldString.length
            
            var stringRange = NSMakeRange(0, min(text.length, numberOfSymbolsToCut))
            
            
            // adjust the range to include dependent chars
            stringRange = (text as NSString).rangeOfComposedCharacterSequences(for: stringRange)
            
            // Now you can create the short string
            let shortString = (text as NSString).substring(with: stringRange)
            
            let newText = NSMutableString()
            newText.append(oldString)
            newText.insert(shortString, at: range.location)
            textView.text = newText as String
            
            self.showCharactersNumberError()
            
            self.textViewDidChange(textView)
            
            return false
        }
        
        return true
    }
    
    override func textViewDidEndEditing(_ textView: UITextView) {
        
        super.textViewDidEndEditing(textView)
        
        self.fireSendStopTypingIfNecessary()
    }
    
//    func textChangeLangauge(_ textView: UITextView) {
//        let params = ROGoogleTranslateParams(source: "en",
//                                             target: "de",
//                                             text:   textView.text)
//
//        let translator = ROGoogleTranslate()//ROGoogleTranslate(with: GooglePlaces_APIKey)
//        translator.apiKey = GooglePlaces_APIKey
//
//        translator.translate(params: params) { (result) in
//            print("Translation: \(result)")
//            textView.text = result
//        }
//    }
    
    @objc func fireSendStopTypingIfNecessary() -> Void {
        
        if let timer = self.typingTimer {
            
            timer.invalidate()
        }
        
        self.typingTimer = nil
        self.sendStopTyping()
    }
    
    func sendBeginTyping() -> Void {
        self.dialog.sendUserIsTyping()
        print("Typing...")
    }
    
    func sendStopTyping() -> Void {
         print("Typing Stop")
        self.dialog.sendUserStoppedTyping()
    }
    
    // MARK: QMChatAttachmentServiceDelegate
    @objc func audioPlayButtonTapped(_ sender: UIButton) {
        
        let section = Int(sender.accessibilityHint!)!
        let row = sender.tag
        let indexPath = IndexPath(row: row, section: section)
        guard let chatItem = self.chatDataSource.message(for: indexPath) else {
            return
        }
        
        if self.player == nil {
            self.getDownloadedFilePath(chatItem) { (fileURL) in
                if fileURL == nil {
                    sender.loadingIndicator(true)
                    QBRequest.downloadFile(withUID: chatItem.attachments![0].id!, successBlock: {(response : QBResponse, audioData: Data) in
                        
                        do {
                            sender.setImage(#imageLiteral(resourceName: "play-button"), for: .normal)
                            self.saveAudio(with: "\(chatItem.id!).m4a", data: audioData)
                            sender.loadingIndicator(false)
//                            self.playAudio(fileURL!)
                        } catch {
                            print(error)
                        }
                    }, statusBlock: {(request: QBRequest?, status: QBRequestStatus?) in
                        sender.loadingIndicator(false)
                        
                    }, errorBlock: {(response :QBResponse) in
                        sender.loadingIndicator(false)
                        print(response.debugDescription)
                    })
                }else {
                    if self.audioPlayerTimer == nil {
                        sender.setImage(#imageLiteral(resourceName: "pause"), for: .normal)
                        self.playAudio(fileURL!, completion: { (status) in
                            if status == 0 {
                                sender.setImage(#imageLiteral(resourceName: "play-button"), for: .normal)
                            }
                        })
                    }
                }
            }
        }else {
            sender.setImage(#imageLiteral(resourceName: "play-button"), for: .normal)
            self.player?.stop()
            self.player = nil
            if self.audioPlayerTimer == nil {
                self.audioPlayerTimer = nil
                self.audioPlayerTimer?.invalidate()
            }
        }
    }
    
    func saveAudio(with name: String, data: Data) {
        let fileManager = FileManager.default
        do {
            let documentDirectory = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor:nil, create:false)
            let fileURL = documentDirectory.appendingPathComponent(name)
            
            try data.write(to: fileURL, options: .atomic)
        }catch {
            print(error)
        }
    }
    
    func playAudio(_ fileURL: URL, completion: @escaping(_ status: Int)-> Void) {
        DispatchQueue.main.async {
                do {
                    try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
                    try AVAudioSession.sharedInstance().setActive(true)
                    self.player = try AVAudioPlayer.init(contentsOf: fileURL)
                    self.player?.prepareToPlay()
                    self.player?.play()
//                    self.player?.duration
                   self.audioPlayerTimer = Timer.scheduledTimer(withTimeInterval: (self.player?.duration)!, repeats: false, block: { (timer) in
//                        print(timer)
                        self.player?.stop()
                        self.player = nil
                        self.audioPlayerTimer = nil
                        self.audioPlayerTimer?.invalidate()
                        completion(0)
                    })
                } catch {
                    print(error)
                    completion(0)
                    self.player?.stop()
                    self.player = nil
                }
        }
    }
    
    func getDownloadedFilePath(_ message: QBChatMessage, completion: @escaping(_ url: URL?)-> Void) {
        DispatchQueue.main.async {
                
                let fileManager = FileManager.default
                do {
                    let documentDirectory = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor:nil, create:false)
                    let fileURL = documentDirectory.appendingPathComponent("\(message.id!).m4a")
                    if let audioData = try Data(contentsOf: fileURL) as? Data{
                        completion(fileURL)
                    }else {
                        completion(nil)
                    }
                } catch {
                    print(error)
                    completion(nil)
                }
        }
    }
    
    
    
    func chatAttachmentService(_ chatAttachmentService: QMChatAttachmentService, didChange status: QMMessageAttachmentStatus, for message: QBChatMessage) {
        
        if status != QMMessageAttachmentStatus.notLoaded {
            if message.dialogID == self.dialog.id {
                self.chatDataSource.update(message)
            }
        }
    }
    
    func chatAttachmentService(_ chatAttachmentService: QMChatAttachmentService, didChangeLoadingProgress progress: CGFloat, for attachment: QBChatAttachment) {
        
        if let attachmentCell = self.attachmentCellsMap.object(forKey: attachment.id! as AnyObject?) {
            attachmentCell.updateLoadingProgress(progress)
        }
    }
    
    func chatAttachmentService(_ chatAttachmentService: QMChatAttachmentService, didChangeUploadingProgress progress: CGFloat, for message: QBChatMessage) {
        
        guard message.dialogID == self.dialog.id else {
            return
        }
        var cell = self.attachmentCellsMap.object(forKey: message.id as AnyObject?)
        
        if cell == nil && progress < 1.0 {
            
            if let indexPath = self.chatDataSource.indexPath(for: message) {
                cell = self.collectionView?.cellForItem(at: indexPath) as? QMChatAttachmentCell
                self.attachmentCellsMap.setObject(cell, forKey: message.id as AnyObject?)
            }
        }
        
        cell?.updateLoadingProgress(progress)
    }
    
    // MARK : QMChatConnectionDelegate
    
    func refreshAndReadMessages() {
        
//        SVProgressHUD.show(withStatus: "SA_STR_LOADING_MESSAGES", maskType: SVProgressHUDMaskType.clear)
        self.loadMessages()
        
        if let messagesToRead = self.unreadMessages {
            self.readMessages(messages: messagesToRead)
        }
        
        self.unreadMessages = nil
    }
    
    func chatServiceChatDidConnect(_ chatService: QMChatService) {
        
        self.refreshAndReadMessages()
    }
    
    func chatServiceChatDidReconnect(_ chatService: QMChatService) {
        
        self.refreshAndReadMessages()
    }
    
    func queueManager() -> QMDeferredQueueManager {
        return ServicesManager.instance().chatService.deferredQueueManager
    }
    
    func handleNotSentMessage(_ message: QBChatMessage,
                              forCell cell: QMChatCell!) {
        
        let alertController = UIAlertController(title: "", message: "SA_STR_MESSAGE_FAILED_TO_SEND", preferredStyle:.actionSheet)
        
        let resend = UIAlertAction(title: "SA_STR_TRY_AGAIN_MESSAGE", style: .default) { (action) in
            self.queueManager().perfromDefferedAction(for: message, withCompletion: nil)
        }
        alertController.addAction(resend)
        
        let delete = UIAlertAction(title: "SA_STR_DELETE_MESSAGE", style: .destructive) { (action) in
            self.queueManager().remove(message)
            self.chatDataSource.delete(message)
        }
        alertController.addAction(delete)
        
        let cancelAction = UIAlertAction(title: "SA_STR_CANCEL", style: .cancel) { (action) in
            
        }
        
        alertController.addAction(cancelAction)
        
        if alertController.popoverPresentationController != nil {
            self.view.endEditing(true)
            alertController.popoverPresentationController!.sourceView = cell.containerView
            alertController.popoverPresentationController!.sourceRect = cell.containerView.bounds
        }
        
        self.present(alertController, animated: true) {
        }
    }
}

