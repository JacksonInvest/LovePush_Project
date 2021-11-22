//
//  LoveMatchViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 30/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class LoveMatchViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var myImageView      : UIImageView!
    @IBOutlet weak var otherImageView   : UIImageView!
    
    //MARK:- Variables
    var userInfo :UserInfo!
    var isChatStarted = false

    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.myImageView.setImageWithoutBaseURL(User.shared.profileURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        self.otherImageView.setImageWithoutBaseURL(userInfo.imageURL!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if self.isChatStarted {
            self.navigationController?.popViewController(animated: false)
        }
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    //MARK:- Button Action
    @IBAction func chatNowButtonTapped(_ sender: UIButton) {
//        NotificationCenter.default.post(name: Notification.Name("Chat Now"), object: nil, userInfo: self.userInfo.matchInfo)
        //Go To Chat
        QuickbloxHandling.shared.createDialog(self.userInfo) { (dialog, error) in
            if error == nil {
                //                print(dialog)
                privateRoomInfo = dialog
                self.saveDialogWith((dialog?.id!)!, matchID: self.userInfo.matchInfo!["id"] as! Int, completion: { (dialogData) in
                    self.isChatStarted = true
                    
                    if dialogData != nil{
                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                        vc.dialog = dialog
                        vc.user = self.userInfo
                        vc.localDialogID = dialogData!["id"] as! Int
                        vc.chatStatus = (dialogData as! [String:Any])["chat_status"] as! Int
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                })
            }else {
                //                print(error)
            }
        }
    }
    
    @IBAction func chatLaterButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
}
