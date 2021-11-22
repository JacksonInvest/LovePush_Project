//
//  HomeVC + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import Quickblox
import UIKit


extension HomeVC : UITableViewDelegate, UITableViewDataSource {
    //Number items to show in post list
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        self.noPostLabel.isHidden = self.postList.count > 0
        return self.postList.count
    }
    
    //Cell configuration for post in indexpath
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableCell", for: indexPath) as! HomeTableCell

        cell.deleteButton.tag      = indexPath.row
        cell.deleteButton.addTarget(self, action: #selector(deleteButtonTapped), for: .touchUpInside)

        cell.profileButton.tag      = indexPath.row
        cell.profileButton.addTarget(self, action: #selector(profileButtonTapped), for: .touchUpInside)
        
        cell.likeButton.tag      = indexPath.row
        cell.likeButton.addTarget(self, action: #selector(likeButtonTapped), for: .touchUpInside)
        
        cell.chatButton.tag      = indexPath.row
        cell.chatButton.addTarget(self, action: #selector(chatButtonTapped), for: .touchUpInside)
        
        cell.reportButton.tag      = indexPath.row
        cell.reportButton.addTarget(self, action: #selector(reportButtonTapped), for: .touchUpInside)

        DispatchQueue.global().sync {
            cell.post = Post(self.postList[indexPath.row])
        }
        cell.localdata = self.postList[indexPath.row]
        
        return cell;
    }
    
    // Click on Post on particular index and show post details
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        var cell: HomeTableCell!
        DispatchQueue.global().sync {
            cell = (self.postTableView.cellForRow(at: IndexPath(row: indexPath.row, section: 0)) as! HomeTableCell)
        }
        
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "PostDetailsViewController") as! PostDetailsViewController
        let post    = Post(self.postList[indexPath.row])
        vc.post     = post
        vc.isMyPost = ("\(post.user!.id!)" == User.shared.id!)
        vc.isHome   = true
        vc.user     = cell.user
        vc.matchID  = (self.postList[indexPath.row])["match_id"] as? Int ?? 0
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    //Scroll action load more post added by me or my matched user
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (((scrollView.contentOffset.y + scrollView.frame.size.height) > scrollView.contentSize.height ) ){
            if (self.pageNo < self.lastPage) && !self.isScrollingEnabled {
                self.isScrollingEnabled = true
                self.pageNo += 1
                self.requestToGetPost(self.url)
            }
        }
    }
    
    /**
     Request to delete post added by me
     - parameter sender: Delete Button Object to get tag number for index or post
     **/
    @objc func deleteButtonTapped(_ sender:UIButton) {
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "Love Push", message: "Are you sure, you want to delete this post.") { (isYes) in
            if isYes {
                self.requestToDeletePost(for: sender.tag)
            }
        }
    }
    
    /**
     Report For Abusive content in photo
     - parameter sender: Report Button Object to get tag number for index or post
     **/
    @objc func reportButtonTapped(_ sender: UIButton) {
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "You have seen abusive or vulgor content in this photo!", message: "Do you want to report photo?") { (isYes) in
            if isYes {
                var cell: HomeTableCell!
                DispatchQueue.global().sync {
                    cell = (self.postTableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as! HomeTableCell)
                }
                self.reportVulgorPhoto(cell.post!.id!, imageType: "post", completion: { (isSuccess) in
                    print("Report send success")
                })
            }
        }
    }
    
    /**
     Show profile info user who added the post
     - parameter sender: Delete Button Object to get tag number for index or post
     **/
    @objc func profileButtonTapped(_ sender:UIButton) {
        let post = Post(self.postList[sender.tag])
        let cell = self.postTableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as! HomeTableCell
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(post.user!.id!)"
        vc.fromPage = "Home"
        vc.userInfo = cell.user
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    /**
     Request to like or unlike post added by users
     - parameter sender: Like Button Object to get tag number for index or post
     **/
    @objc func likeButtonTapped(_ sender:UIButton) {
        let post = Post(self.postList[sender.tag])
        
        if sender.currentImage == #imageLiteral(resourceName: "like_unactive") {//Like
            sender.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
            self.likeUnlike(post.id!, status: 1) { (isSuccess,data) in
                if isSuccess {
                    self.postList[sender.tag]["isLiked"] = 1
                    if post.user!.isNotificationEnable && ("\(post.user!.id!)" != User.shared.id!) {
                        self.createAlertPayload("\(User.shared.name!) like your post.", requestType: "Like", qbID: post.user!.qbID!, eventID: 0)
                    }
                }else {
                    sender.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
                }
            }
        }else {//Unlike
            sender.setImage(#imageLiteral(resourceName: "like_unactive"), for: .normal)
            self.likeUnlike(post.id!, status: 0) { (isSuccess,data) in
                if isSuccess {
                    self.postList[sender.tag]["isLiked"] = 0
                }else {
                    sender.setImage(#imageLiteral(resourceName: "like_active"), for: .normal)
                }
            }
        }
    }
    
    /**
     Start Chat with user who added post
     - parameter sender: Chat Button Object to get tag number for index or post
     **/
    @objc func chatButtonTapped(_ sender:UIButton) {
        let post = Post(self.postList[sender.tag])
        
        var cell: HomeTableCell!
        DispatchQueue.global().sync {
            cell = (self.postTableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as! HomeTableCell)
        }
        
        if let matchData = post.matchData {//Match QBDialog or Local Dialog already created needs to fetch or create dialog in Quickblox server
            QuickbloxHandling.shared.createDialog(cell.user!) { (dialog, error) in
                if error == nil {
                    //            QuickbloxHandling.shared.loadDialog(dialogData["qb_dialog_id"] as! String, complition: { (dialog) in
                    
                    privateRoomInfo = dialog
                    
                    if let loadedDialog = dialog {
                        if let user = cell.user {
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                            vc.dialog        = loadedDialog
                            vc.user          = user
                            vc.chatStatus    = user.chatStatus!
                            vc.deleteStatus  = (user.deletedBy != 0) ? 1 : 0
                            vc.matchID       = cell.matchID
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    }
                }
            }
        }else {//Local dialog not exist or first time chat start for this user need to create new dialog for local or Quickblox
            QuickbloxHandling.shared.createDialog(cell.user!) { (dialog, error) in
                if error == nil {
                    
                    privateRoomInfo = dialog
                    
                    self.saveDialogWith((dialog?.id!)!, matchID: (self.postList[sender.tag])["match_id"] as! Int, completion: { (localDialog) in
                        
                        if let matchData = localDialog {
                            if var data = self.postList[sender.tag]["user_info"] as? [String:Any] {
                                
                                    data["event_type"] = (matchData["match_info"] as! [String:Any])["event_type"] as? Int ?? 0
                                    data["chat_status"] = matchData["chat_status"] as? Int ?? 1
                                
                                let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                                vc.dialog        = dialog
                                vc.user          = cell.user
                                vc.chatStatus    = cell.user.chatStatus!
                                vc.deleteStatus  = (cell.user.deletedBy != 0) ? 1 : 0
                                vc.matchID       = cell.matchID
                                vc.localDialogID = (localDialog as! [String:Any])["id"] as! Int
                                self.navigationController?.pushViewController(vc, animated: true)
                            }
                        }
                    })
                }else {
                    //                print(error)
                }
            }
        }
    }
}
