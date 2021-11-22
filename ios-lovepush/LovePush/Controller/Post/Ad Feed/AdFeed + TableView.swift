//
//  AdFeed + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 02/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit

extension ADFeedVC : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        self.noPostLabel.isHidden = self.postList.count > 0
        return self.postList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "AdFeedTableViewCell", for: indexPath) as! AdFeedTableViewCell
        
        let post = Post(self.postList[indexPath.row])
        
        DispatchQueue.global().sync {
            cell.post = post
        }
        
        cell.profileButtoon.isHidden = !self.isAdFeed
        cell.deleteButton.isHidden   = self.isAdFeed
        cell.reportButton.isHidden   = !self.isAdFeed || ("\(post.user!.id!)" == User.shared.id!)
        
        cell.deleteButton.tag        = indexPath.row
        cell.deleteButton.addTarget(self, action: #selector(deleteButtonTapped), for: .touchUpInside)
        
        cell.reportButton.tag        = indexPath.row
        cell.reportButton.addTarget(self, action: #selector(reportButtonTapped), for: .touchUpInside)
        
        cell.profileButtoon.isHidden = (("\(post.user!.id!)" == User.shared.id!) || (post.anonymouslStatus == 1))
        
        if (post.anonymouslStatus != 1) && ("\(post.user!.id!)" != User.shared.id!){
            //Show profile who posted ads if its not anonymous or not current(me) user ads
            cell.profileButtoon.tag      = indexPath.row
            cell.profileButtoon.addTarget(self, action: #selector(profileButtonTapped), for: .touchUpInside)
        }
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if self.postList.count > 0 {
            let post = Post(self.postList[indexPath.row])
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "PostDetailsViewController") as! PostDetailsViewController
            vc.post = post
            vc.isMyPost = ("\(post.user!.id!)" == User.shared.id!)// ? true : true
            self.navigationController?.pushViewController(vc, animated: true)
        }else {

        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if (((scrollView.contentOffset.y + scrollView.frame.size.height) > scrollView.contentSize.height ) ){
            if (self.pageNo < self.lastPage) && !self.isScrollingEnabled {
                self.isScrollingEnabled = true
                self.pageNo += 1
                self.requestToGetPost(self.url, latitude: self.latitude,longitude: self.logitutde,isFilter: self.isFilter)
            }
        }
    }
    
    @objc func deleteButtonTapped(_ sender:UIButton) {
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "Love Push", message: "Are you sure, you want to delete this post.") { (isYes) in
            if isYes {
                self.requestToDeletePost(for: sender.tag)
            }
        }
    }
    
    @objc func profileButtonTapped(_ sender:UIButton) {
       let post = Post(self.postList[sender.tag])
        
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(post.user!.id!)"
        vc.fromPage = self.isAdFeed ? "Ad Feed" : "My Feed"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func reportButtonTapped(_ sender: UIButton) {
       
        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "You have seen abusive or vulgor content in this photo!", message: "Do you want to report photo?") { (isYes) in
            if isYes {
                var cell: AdFeedTableViewCell!
                DispatchQueue.global().sync {
                    cell = (self.adTableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as! AdFeedTableViewCell)
                }
                self.reportVulgorPhoto(cell.post!.id!, imageType: "post_ad", completion: { (isSuccess) in
                    print("Report send success")
                })
            }
        }
    }
}
