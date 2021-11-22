//
//  MessageVC + CollectionView.swift
//  LovePush
//
//  Created by Lavi Kumar on 06/09/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation


extension MessagesVC : UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.matchedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "UsersCollectionCell", for: indexPath) as! UsersCollectionCell
        
        cell.newdata = self.matchedList[indexPath.row]
        if cell.user.eventType == "L" {
            cell.imgView.layer.borderColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0).cgColor
        }else if cell.user.eventType == "M" {
            cell.imgView.layer.borderColor = UIColor.darkGray.cgColor
        }else {
            cell.imgView.layer.borderColor = UIColor.blue.cgColor
        }
        cell.imgView.layer.borderWidth = 1.5
        
        return cell;
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        if SubscriptionPlan.shared.newUserMessage == "0" {
            self.showAlert(title: "Love push", message: "Your daily limit to start chat with new user is over.", completion: nil)
        }else {
            let cell = self.collectionView.cellForItem(at: indexPath) as! UsersCollectionCell
            
            //        let qbID = cell.quickBloxID
            
            let user = cell.user
            
            Helper.sharedInstance.ShowLoaderOnView(view: self.view)
            
            QuickbloxHandling.shared.createDialog(user!) { (dialog, error) in
                
                Helper.sharedInstance.HideLoaderFromView(view: self.view)
                
                if error == nil {
                    //                print(dialog)
                    privateRoomInfo = dialog
                    self.saveDialogWith((dialog?.id!)!, matchID: cell.matchID, completion: { (dialogData) in
                        
                        if dialogData != nil{
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                            vc.dialog        = dialog
                            vc.user          = user
                            vc.localDialogID = dialogData!["id"] as! Int
                            vc.chatStatus    = (dialogData as! [String:Any])["chat_status"] as! Int
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    })
                }else {
                    //                print(error)
                }
            }
        }
    }
    
    /**
     Scroll to load more newest matches
     - parameter scrollView: Collection View scrolling object
     - returns: nil
     **/
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollView == collectionView {
            let offsetX = scrollView.contentOffset.x
            let contentWidth = scrollView.contentSize.width
            if offsetX > contentWidth - scrollView.frame.size.width && !self.isAPIEnable {
                if self.pageNo < self.totalPage {
                    self.isAPIEnable = true
                    self.pageNo += 1
                    self.getMatchedUser()
                }
            }else if offsetX < 0 && !self.isAPIEnable {
                if self.pageNo != 1 {
                    self.isAPIEnable = true
                    self.pageNo -= 1
                    self.getMatchedUser()
                }
            }
        }
    }
}
