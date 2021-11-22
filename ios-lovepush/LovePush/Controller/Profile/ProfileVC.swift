//
//  ProfileVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 16/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import FRStretchImageView
import SDWebImage

class ProfileVC: UIViewController {
    
    //MARK:- Button Action
    @IBOutlet weak var postCollectionView : UICollectionView!
    @IBOutlet weak var imageCollectionView: UICollectionView!
    
    @IBOutlet weak var scrollView   : UIScrollView!
    @IBOutlet weak var defaultImage : UIImageView!
    
    @IBOutlet weak var leftArrow    : UIButton!
    @IBOutlet weak var rightArrow   : UIButton!
    @IBOutlet weak var backButton   : UIButton!
    @IBOutlet weak var editButton   : UIButton!
    @IBOutlet weak var chatButton   : UIButton!
    
    @IBOutlet weak var ageLabel          : UILabel!
    @IBOutlet weak var nameLbl           : UILabel!
    @IBOutlet weak var bioLbl            : UILabel!
    @IBOutlet weak var userNameLabel     : UILabel!
    @IBOutlet weak var lookingLabel      : UILabel!
    @IBOutlet weak var favDestLabel      : UILabel!
    @IBOutlet weak var langaugeLabel     : UILabel!
    @IBOutlet weak var postLabel         : UILabel!
    @IBOutlet weak var loveIntentLabel   : UILabel!
    @IBOutlet weak var hobbiesLabel      : UILabel!
    @IBOutlet weak var relationshipLabel : UILabel!
    
    @IBOutlet weak var userNameView      : UIView!
    @IBOutlet weak var userAgeView       : UIView!
    @IBOutlet weak var lookingView       : UIView!
    @IBOutlet weak var languageView      : UIView!
    @IBOutlet weak var favDestView       : UIView!
    @IBOutlet weak var loveIntentView    : UIView!
    @IBOutlet weak var hobbiesView       : UIView!
    @IBOutlet weak var relationshipView  : UIView!
    @IBOutlet weak var bioView           : UIView!
    @IBOutlet weak var likeView          : UIView!
    @IBOutlet weak var acceptRejctView   : UIView!
    
    @IBOutlet weak var uNameViewHeight    : NSLayoutConstraint!
    @IBOutlet weak var uAgeViewHeight     : NSLayoutConstraint!
    @IBOutlet weak var lookinViewHeight   : NSLayoutConstraint!
    @IBOutlet weak var languageViewHeight : NSLayoutConstraint!
    @IBOutlet weak var favDestViewHeight  : NSLayoutConstraint!
    @IBOutlet weak var bioViewHeight      : NSLayoutConstraint!
    @IBOutlet weak var bioTitleHeight     : NSLayoutConstraint!
    @IBOutlet weak var postBottomHeight   : NSLayoutConstraint!
    @IBOutlet weak var loveIntentHeight   : NSLayoutConstraint!
    @IBOutlet weak var hobbiesHeight      : NSLayoutConstraint!
    @IBOutlet weak var relationshipHeight : NSLayoutConstraint!
    
    //MARK:- Variables
    
    var isMyProfile     = false
    var isOnlyShow      = false
    var userID: String  = "0"
    var imagesArray     = [[String:Any]]()
    var postArr         = [[String:Any]]()
    var detailDict      = NSDictionary()
    var eventType       = ""
    var eventID         = 0
    var eventStatus     = ""
    var msgInfoStatus   = ""
    var qbID            = "0"
    var pageNo          = 1
    var lastPage        = 1
    var isAPIEnable     = false
    var isBlocked       = false
    var deletedBy       = 0
    var userInfo        : UserInfo?
    var fromPage        = ""
    var matchId         = 0
    var userData : UserInfo!
    var scrollIndex = 1
    var isLikeAcceptBtn = false
    var isFromSwipe = false
    
    let lovaPushIntentions = ["Relationship","Friendship","Daily Activities", "No Expectation"]
    let relationshipStatus = ["Single","In Relationship","Married","Recently Separated"]
    
    //MARK:- View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        UIApplication.shared.statusBarStyle = .lightContent
        
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
        
        if self.isMyProfile || self.isOnlyShow {//Current user profile
            self.userID = !self.isOnlyShow ? User.shared.id! : self.userID
//            self.likeView.isHidden = true
            self.editButton.isHidden = !self.isMyProfile
            self.postBottomHeight.constant = 30
        }else {//Other User profile
            self.postBottomHeight.constant = 90
            self.editButton.isHidden = true
        }
        
        if self.userID != "0" {//Page open to show my own profile
            
            self.GetProfile()
            
            self.pageNo    = 1
            self.lastPage  = 1
            self.postArr.removeAll()
            self.postCollectionView.reloadData()
        }
    }
    
    func manageHideShowData() {
        
        if self.fromPage == "Home" || self.fromPage  == "Match List" {
            self.chatButton.isHidden      = false
            self.likeView.isHidden        = true
            self.acceptRejctView.isHidden = true
        }else if self.fromPage == "Explore" || self.fromPage == "Recommend" {
            self.chatButton.isHidden      = true
            self.likeView.isHidden        = false
            self.acceptRejctView.isHidden = true
//             return
        }else if self.fromPage == "Search" || self.fromPage  == "My Team"{
            self.chatButton.isHidden      = (self.userInfo?.matchInfo == nil)
            self.likeView.isHidden        = (self.userInfo?.matchInfo != nil)
            self.acceptRejctView.isHidden = true
             return
        }else if self.fromPage == "Notification" {
            
            self.chatButton.isHidden = true
                
            self.likeView.isHidden   = true
            self.acceptRejctView.isHidden = ((self.eventType == "A_C_R") || (self.eventType == "A_M_R") || (self.eventType == "A_L_R") || (self.eventType == "P_L_R"))//P_L_R Post Like Request
        }else if self.fromPage == "Ad Feed" {
            self.chatButton.isHidden      = (self.userInfo!.matchInfo == nil)
            self.likeView.isHidden        = (self.userInfo!.matchInfo != nil)
            self.acceptRejctView.isHidden = true
             return
        }
        if self.isMyProfile || self.fromPage == "Chat" || self.userInfo!.isBlocked {
            self.chatButton.isHidden       = true
            self.acceptRejctView.isHidden  = true
            self.likeView.isHidden         = self.isMyProfile ? true : (self.userInfo!.matchInfo != nil)
            
        }else {
            self.chatButton.isHidden = self.userInfo!.isBlocked || (self.userInfo!.matchInfo == nil)
            self.likeView.isHidden   = (self.userInfo!.matchInfo != nil) || (self.fromPage == "Notification")
             return
        }
        
    }
    
    func SetData(details dict: NSDictionary) {
        
        self.detailDict = dict
        
        self.imagesArray.removeAll()
        self.nameLbl.text       = dict.value(forKey: "name") as? String ?? ""
        self.userNameLabel.text = dict.value(forKey: "username") as? String ?? ""
        self.langaugeLabel.text = dict.value(forKey: "language") as? String ?? ""
        self.favDestLabel.text  = dict.value(forKey: "education") as? String ?? ""
        self.bioLbl.text        = dict.value(forKey: "aboutme") as? String ?? ""
        self.hobbiesLabel.text  = dict.value(forKey: "hobbies") as? String ?? ""
        self.qbID               = "\(dict.value(forKey: "quickblox_id")!)"
        
        if let age = dict.value(forKey: "age") as? Int  {
            self.ageLabel.text = "\(age)"
        }else if let age = dict.value(forKey: "age") as? String  {
            self.ageLabel.text = "\(age)"
        }
        
        if let currentPrifleImageURL = dict.value(forKey: "profile_image") as? String {
            let dict = ["image" : currentPrifleImageURL, "id": 0, "updated_at" : "\(Date())","user_id": User.shared.id!,"created_at": "\(Date())"] as [String : Any]
            self.imagesArray.append(dict)
        }
        
        if let newArr = dict.value(forKey: "profile_images") as? [[String:Any]] {
            if newArr.count > 0 {
                self.imagesArray.removeAll()
                self.imagesArray  = newArr
                if self.imagesArray.count > 0 {
                    self.setTimer()
                }
            }
        }
        
        if let lovePushIntestion = dict.value(forKey: "love_push_intention") as? String {
            let loveIntensionArr = lovePushIntestion.components(separatedBy: ",")
            let localizedArr = loveIntensionArr.map({$0.localized()})
            self.loveIntentLabel.text = (localizedArr.count > 0) ? (localizedArr.joined(separator: ",")).replacingOccurrences(of: " ", with: "") : ""
        }else {
            self.loveIntentLabel.text = ""
        }
        
        if let relationID = dict.value(forKey: "relationship") as? Int {
            if relationID <= self.relationshipStatus.count {
                self.relationshipLabel.text = (relationID == 0) ? "" : self.relationshipStatus[relationID - 1].localized()
            }
        }
        
        self.manageHeight()
        
        self.imageCollectionView.isHidden = self.imagesArray.count > 0 ? false : true
        self.leftArrow.isHidden           = self.imagesArray.count > 1 ? false : true
        self.rightArrow.isHidden          = self.imagesArray.count > 1 ? false : true
        self.imageCollectionView.isHidden = self.imagesArray.count > 0 ? false : true
        self.imageCollectionView.reloadData()
        
        self.manageHideShowData()
    }
    
    func manageHeight() {
        
        self.userNameView.isHidden       = (self.userNameLabel.text! == "") ? true : false
        self.uNameViewHeight.constant    = (self.userNameLabel.text! == "") ? 0 : 52
      
        self.userAgeView.isHidden        = self.ageLabel.text! == "" ? true : false
        self.uAgeViewHeight.constant     = self.ageLabel.text! == "" ? 0 : 52
       
        self.lookingView.isHidden        = self.lookingLabel.text! == "" ? true : false
        self.lookinViewHeight.constant   = self.lookingLabel.text! == "" ? 0 : 52
     
        self.languageView.isHidden       = self.langaugeLabel.text! == "" ? true : false
        self.languageViewHeight.constant = self.langaugeLabel.text! == "" ? 0 : 52
       
        self.favDestView.isHidden        = self.favDestLabel.text! == "" ? true : false
        self.favDestViewHeight.constant  = self.favDestLabel.text! == "" ? 0 : 52
        
        self.loveIntentView.isHidden     = self.loveIntentLabel.text! == "" ? true : false
        self.loveIntentHeight.constant   = self.loveIntentLabel.text! == "" ? 0 : 20
        
        self.hobbiesView.isHidden        = self.hobbiesLabel.text! == "" ? true : false
        self.hobbiesHeight.constant      = self.hobbiesLabel.text! == "" ? 0 : 20
        
        self.relationshipView.isHidden   = self.relationshipLabel.text! == "" ? true : false
        self.relationshipHeight.constant = self.relationshipLabel.text! == "" ? 0 : 52
        
        self.bioView.isHidden            = ((self.bioLbl.text! == "") || (self.bioLbl.text! == "About me".localized())) ? true : false
        
        if ((self.bioLbl.text! == "") || (self.bioLbl.text! == "About me".localized())) {
            self.bioViewHeight.constant = 0
            self.bioTitleHeight.constant = 0
        }else {
            self.bioTitleHeight.constant = 25
        }
    }
    
    //MARK:- Button Actions
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func EditAction(_ sender: UIButton) {
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        if (self.detailDict.count != 0) {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "UpdateProfileID") as! UpdateProfileVC
            vc.detailDict.addEntries(from: self.detailDict as! [AnyHashable : Any])
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    @IBAction func leftScrollButtonTapped(_ sender: UIButton) {
        let collectionBounds = self.imageCollectionView.frame
        let contentOffset = CGFloat(floor(self.imageCollectionView.contentOffset.x - collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)

    }
    @IBAction func rightScrollButtonTapped(_ sender: UIButton) {
        let collectionBounds = self.imageCollectionView.frame
        let contentOffset = CGFloat(floor(self.imageCollectionView.contentOffset.x + collectionBounds.size.width))
        self.moveCollectionToFrame(contentOffset: contentOffset)
    }
    
    @IBAction func LikeAction(_ sender: UIButton) {
        if self.userID != "0" {
            self.likeOrDislikUserRequest(1)
        }
    }
    
    @IBAction func DislikeAction(_ sender: UIButton) {
        if self.userID != "0" {
            self.likeOrDislikUserRequest(0)
        }
    }
    
    @IBAction func connectMatchButtonTapped(_ sender: UIButton) {
        if self.userID != "0" {
            self.connectUserRequest()
        }
    }
    
    @IBAction func acceptButtonTapped(_ sender : UIButton) {
        if self.eventType == "S_L_R" {
            self.likeOrDislikUserRequest(1)
        }else {
            self.acceptOrRejectRequest("A")
        }
    }
    
    @IBAction func rejectButtonTapped(_ sender: UIButton) {
        if self.eventType == "S_L_R" {
            self.isLikeAcceptBtn = true
            self.likeOrDislikUserRequest(0)
        }else {
            self.acceptOrRejectRequest("R")
        }
    }
    @IBAction func chatButtonTapped(_ sender: UIButton) {
        self.sendChatRequest()
    }
    
    @IBAction func messageChatButtonTapped(_ sender : UIButton) {
        if self.userInfo != nil {
            if self.userInfo!.matchInfo != nil {
                if let dialogData = self.userInfo!.matchInfo!["qb_chat_dialog"] as? [String:Any] {
                    QuickbloxHandling.shared.createDialog(self.userInfo!) { (dialog, error) in
                        if error == nil {
//                          5e217cd0a0eb473367e01762   ,1007 QuickbloxHandling.shared.loadDialog(dialogData["qb_dialog_id"] as! String, complition: { (dialog) in
                            if let loadedDialog = dialog {
                                if var user = self.userInfo {
                                    let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                                    
                                    vc.dialog       = loadedDialog
                                    vc.user         = user
                                    vc.deleteStatus = (user.deletedBy! != 0) ? 1 : 0
                                    vc.matchID      = user.matchInfo!["id"] as! Int
                                    self.navigationController?.pushViewController(vc, animated: true)
                                }
                                
                            }
                            }
//                        })
                    }
                }else {
                    QuickbloxHandling.shared.createDialog(self.userInfo!) { (dialog, error) in
                        if error == nil {
                            self.saveDialogWith(dialog!.id!, matchID: self.userInfo!.matchInfo!["id"] as! Int, completion: { (response) in
                                
                                if dialog != nil {
                                    let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                                    
                                    vc.dialog        = dialog
                                    vc.user          = self.userInfo
                                    vc.localDialogID = (response as! [String:Any])["id"] as! Int
                                    vc.chatStatus    = (response as! [String:Any])["chat_status"] as! Int
                                    vc.deleteStatus  = (self.userInfo!.deletedBy! != 0) ? 1 : 0
                                    vc.matchID       = self.userInfo!.matchInfo!["id"] as! Int
                                    self.navigationController?.pushViewController(vc, animated: true)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

extension ProfileVC : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if  collectionView == self.imageCollectionView {
            return self.imagesArray.count
        }else {
            self.postLabel.isHidden = (self.postArr.count == 0)
            return self.postArr.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == self.imageCollectionView {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ProfileImageCollectionViewCell", for: indexPath) as! ProfileImageCollectionViewCell
            
            cell.reportButton.isHidden = self.isMyProfile
            
            cell.reportButton.tag      = indexPath.row
            cell.reportButton.addTarget(self, action: #selector(reportButtonTapped), for: .touchUpInside)
            
            if let imageURL = self.imagesArray[indexPath.row]["image"] as? String {
                cell.imageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            }
            return cell;
        }else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PostCollectionViewCell", for: indexPath) as! ProfileImageCollectionViewCell
            if self.postArr.count != 0 {
               let post = Post(self.postArr[indexPath.row])
                DispatchQueue.global().sync {
                    cell.post = post
                }
                cell.localdata = self.postArr[indexPath.row]
                if post.url != nil {
                    cell.postImage.setImageWithoutBaseURL(post.url!, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                }
            }
            return cell;
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == self.imageCollectionView {
            return CGSize(width: self.view.frame.size.width, height: 232)
        }else {
            return CGSize(width: 170, height: 110)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == self.postCollectionView {
//            if self.postArr.count == 0 && self.isMyProfile {
//                let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
//                self.navigationController?.pushViewController(vc, animated: false)
//            }
            
            
            var cell: ProfileImageCollectionViewCell!
            DispatchQueue.global().sync {
                cell = (self.postCollectionView.cellForItem(at: IndexPath(row: indexPath.row, section: 0)) as! ProfileImageCollectionViewCell)
            }
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "PostDetailsViewController") as! PostDetailsViewController
            let post    = Post(self.postArr[indexPath.row])
            vc.post     = post
            vc.isMyPost = ("\(post.user!.id!)" == User.shared.id!)
            vc.isHome   = true
            vc.user     = cell.user
            vc.matchID  = (self.postArr[indexPath.row])["match_id"] as? Int ?? 0
            //        vc.localdata = self.postList[indexPath.row]
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    /**
     Report For Abusive content in photo
     - parameter sender: Report Button Object to get tag number for index or post
     **/
    @objc func reportButtonTapped(_ sender: UIButton) {
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "You have seen abusive or vulgor content in this photo!", message: "Do you want to report photo?") { (isYes) in
            if isYes {
               let imageID = self.imagesArray[sender.tag]["id"] as! Int
                self.reportVulgorPhoto(imageID, imageType: "post_ad", completion: { (isSuccess) in
                    print("Report send success")
                })
            }
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollView == self.postCollectionView {
            let offsetX = scrollView.contentOffset.x
            let contentWidth = scrollView.contentSize.width
            if offsetX > contentWidth - scrollView.frame.size.width && !self.isAPIEnable {
                if self.pageNo < self.lastPage {
                    self.isAPIEnable = true
                    self.pageNo += 1
                    self.getUserPost()
                }
            }//else if offsetX < 0 && !self.isAPIEnable {
//                if self.pageNo != 1 {
//                    self.isAPIEnable = true
//                    self.pageNo -= 1
//                    self.getUserPost()
//                }
//            }
        }
    }
    
    func setTimer() {
        let _ = Timer.scheduledTimer(timeInterval: 3.0, target: self, selector: #selector(autoScroll), userInfo: nil, repeats: true)
    }
    
    
    
    @objc func autoScroll() {
        if self.scrollIndex < self.imagesArray.count {
            let indexPath = IndexPath(item: scrollIndex, section: 0)
            self.imageCollectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
            self.scrollIndex = self.scrollIndex + 1
        } else {
            self.scrollIndex = 0
            self.imageCollectionView.scrollToItem(at: IndexPath(item: 0, section: 0), at: .centeredHorizontally, animated: true)
        }
    }
    
    func moveCollectionToFrame(contentOffset : CGFloat) {
        let frame: CGRect = CGRect(x : contentOffset ,y : self.imageCollectionView.contentOffset.y ,width : self.imageCollectionView.frame.width,height : self.imageCollectionView.frame.height)
        self.imageCollectionView.scrollRectToVisible(frame, animated: true)
    }
}
