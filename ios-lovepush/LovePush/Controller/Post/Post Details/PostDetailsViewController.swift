//
//  PostDetailsViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 05/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class PostDetailsViewController: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var headerTitleLabel : UILabel!
    @IBOutlet weak var userNameLabel    : UILabel!
    @IBOutlet weak var postTypeLabel    : UILabel!
    @IBOutlet weak var postTitleLabel   : UILabel!
    @IBOutlet weak var locationLabel    : UILabel!
    @IBOutlet weak var descLabel        : UILabel!
    
    @IBOutlet weak var editButton       : UIButton!
    @IBOutlet weak var deleteButton     : UIButton!
    @IBOutlet weak var likeButton       : UIButton!
    @IBOutlet weak var chatButton       : UIButton!
    @IBOutlet weak var profileButton    : UIButton!
    @IBOutlet weak var reportButton     : UIButton!
    
    @IBOutlet weak var userImageView    : UIImageView!
    @IBOutlet weak var postImageView    : UIImageView!
    
    @IBOutlet weak var imageTopSpace    : NSLayoutConstraint!
    @IBOutlet weak var postTypeTopSpace : NSLayoutConstraint!
    @IBOutlet weak var postImageHeight  : NSLayoutConstraint!
    
    //MARK:- Variables
    var post:Post?
    var isMyPost: Bool!
    var isHome  = false
    var user : UserInfo!
    var matchID = 0
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(addressTapped))
        self.locationLabel.addGestureRecognizer(tapGesture)
        
        self.dataSetup()
    }
    
    func dataSetup() {
        self.editButton.isHidden    = !self.isMyPost
        self.deleteButton.isHidden  = !self.isMyPost
        self.reportButton.isHidden  = self.isMyPost
        self.chatButton.isHidden    = self.isMyPost || (self.post?.anonymouslStatus! == 1) || !self.isHome
        self.likeButton.isHidden    = !self.isHome
//        self.userNameLabel.isHidden = self.isMyPost || (self.post?.anonymouslStatus! == 1) || !self.isHome
//        self.userImageView.isHidden = self.isMyPost || (self.post?.anonymouslStatus! == 1) || !self.isHome
        self.profileButton.isHidden = (self.isMyPost || (self.post?.anonymouslStatus! == 1)) && !self.isHome
//        self.imageTopSpace.constant = self.isMyPost || (self.post?.anonymouslStatus! == 1) || !self.isHome ? 0 : 100
//        self.postTypeTopSpace.constant = self.isMyPost || (self.post?.anonymouslStatus! == 1) || !self.isHome ? 20 : 45
        
        self.postTypeLabel.isHidden = self.isHome
        self.locationLabel.isHidden = self.isHome
        
        //Post of unmatched user
        self.chatButton.isHidden    = (self.post!.matchData == nil) ? true : self.chatButton.isHidden
        
        
        //Post without photo show profile image
//        self.imageTopSpace.constant = (self.post!.url == nil) ? 100 : self.imageTopSpace.constant
//        self.userImageView.isHidden = (self.post!.url == nil) ? false : self.userImageView.isHidden
        
        if self.isMyPost || (self.post?.anonymouslStatus! != 1) {
            self.userNameLabel.text = self.post?.user?.userName!
            self.userImageView.setImageWithoutBaseURL((self.post?.user!.imageURL!)!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        }else {
            self.userNameLabel.text  = ""
            self.userImageView.image = #imageLiteral(resourceName: "userPlaceholder")
        }
        
        let postLikeArr = self.post!.likesArr
        
        let filterMe = postLikeArr!.filter({"\($0["user_id"] as! Int)" == User.shared.id})
        
        if filterMe.count != 0 {
            self.likeButton.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
        }else {
            self.likeButton.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
        }
        
        self.headerTitleLabel.text  = self.isHome ? "POST".localized() : self.isMyPost ? "MY FEED".localized() : "AD FEED".localized()
        self.postTypeLabel.lzText   = (self.post?.type! == 1) ? "Love Ad" : "Connect Ad"
        self.postTitleLabel.text    = self.post?.title!
        self.locationLabel.text     = self.post?.address!
        self.descLabel.text         = self.post?.description!
        
        if self.post!.url == nil {
            self.postImageHeight.constant = self.isHome ? 65 : 0
            self.postImageView.isHidden = true
        }else {
            self.postImageView.setImageWithoutBaseURL(self.post!.url!, PlaceholderImage: #imageLiteral(resourceName: "default_image"))
        }
    }

    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func editButtonTapped(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "PostAdsID") as! PostAdsVC
        vc.post        = self.post
        vc.isHome      = self.isHome
        vc.isForUpdate = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func deleteButtonTapped(_ sender: UIButton) {
        self.requestToDeletePost()
    }
    
    @IBAction func profileButtonTapped(_ sender: UIButton) {
        let post = self.post
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(post!.user!.id!)"
        vc.fromPage = "Home"
        vc.userInfo = self.user
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    
    @IBAction func reportButtonTapped(_ sender: UIButton) {
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "You have seen abusive or vulgor content in this photo!", message: "Do you want to report photo?") { (isYes) in
            if isYes {
                self.reportVulgorPhoto(self.post!.id!, imageType: "post", completion: { (isSuccess) in
                    print("Report send success")
                })
            }
        }
    }
    
    @IBAction func likeButtonTapped(_ sender: UIButton) {
        
        if sender.currentImage == #imageLiteral(resourceName: "like_unactive") {
            sender.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
            self.likeUnlike(self.post!.id!, status: 1) { (isSuccess,data) in
                if isSuccess {
                    if self.post!.user!.isNotificationEnable && !self.isMyPost {
                        self.createAlertPayload("\(User.shared.name!) like your post.", requestType: "Like", qbID: self.post!.user!.qbID!, eventID: 0)
                    }
                }else {
                    sender.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
                }
            }
        }else {
            sender.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
            self.likeUnlike(self.post!.id!, status: 0) { (isSuccess,data) in
                if !isSuccess {
                    sender.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
                }
            }
        }
    }
    
    @IBAction func chatButtonTapped(_ sender: UIButton) {
        if self.post!.matchData != nil {
            QuickbloxHandling.shared.createDialog(self.user!) { (dialog, error) in
                if error == nil {
                    //            QuickbloxHandling.shared.loadDialog(dialogData["qb_dialog_id"] as! String, complition: { (dialog) in
                    if let loadedDialog = dialog {
                        if self.user != nil {
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                            vc.dialog        = loadedDialog
                            vc.user          = self.user
                            vc.chatStatus    = self.user.chatStatus!
                            vc.deleteStatus  = (self.user.deletedBy != 0) ? 1 : 0
                            vc.matchID       = self.matchID
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    }
                }
            }
        }else {
            QuickbloxHandling.shared.createDialog(self.user!) { (dialog, error) in
                if error == nil {
                    self.saveDialogWith(dialog!.id!, matchID: self.matchID, completion: { (localDialog) in
                        
                        if localDialog != nil{
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                            vc.dialog = dialog
                            vc.user = self.user
                            vc.localDialogID = (localDialog as! [String:Any])["id"] as! Int
                            vc.chatStatus    = self.user.chatStatus!
                            vc.deleteStatus  = (self.user.deletedBy != 0) ? 1 : 0
                            vc.matchID       = self.matchID
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    })
                }
            }
        }
    }
    
    @objc func addressTapped(_ sender: UITapGestureRecognizer) {
        
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad ){
            UIApplication.shared.open(URL(string:
                "https://maps.google.com/?q=@\(self.post!.coordinate!.latitude),\(self.post!.coordinate!.longitude)")!, options: [:], completionHandler: nil)
        }else{
            if (UIApplication.shared.canOpenURL(URL(string:"comgooglemaps://")!)) {
                
                let url = URL(string:
                    "comgooglemaps://?q=\(self.post!.coordinate!.latitude),\(self.post!.coordinate!.longitude)&zoom=14&views=traffic")!
                
                UIApplication.shared.open(url, options: [:]) { (isSuccess) in
                    print(isSuccess)
                    if !isSuccess {//Failed
                        UIApplication.shared.open(URL(string:
                            "https://maps.google.com/?q=@\(self.post!.coordinate!.latitude),\(self.post!.coordinate!.longitude)")!, options: [:], completionHandler: nil)
                    }
                }
            } else {
                UIApplication.shared.open(URL(string:
                    "https://maps.google.com/?q=@\(self.post!.coordinate!.latitude),\(self.post!.coordinate!.longitude)")!, options: [:], completionHandler: nil)
            }
        }
    }
    
    func requestToDeletePost() {
        
        let post = self.post!
        
        let param = ["id" : post.id!]
        
        let url = self.isHome ? URLConstant.deletePost : URLConstant.deletePostAd
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        print(param)
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(url, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                self.navigationController?.popViewController(animated: true)
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
