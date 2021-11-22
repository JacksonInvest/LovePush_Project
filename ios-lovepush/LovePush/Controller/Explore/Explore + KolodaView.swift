//
//  Explore + KolodaView.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/07/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import Koloda
import UIKit

//MARK:- Koloda View Delegate
extension ExploreVC: KolodaViewDelegate {
    
    func kolodaDidRunOutOfCards(_ koloda: KolodaView) {
        self.hideViews()
        self.swipeUserArray.removeAll()
        self.kolodaView.resetCurrentCardIndex()
        if self.totalUserCountFetch < self.totalCountAvailable {
            self.pageNo += 1
            self.kolodaView.resetCurrentCardIndex()
            Helper.sharedInstance.ShowLoaderOnView(view: self.view)
            if Singleton.shared.isExploreFilter {
                self.getFilteredData(minAge: self.minAge, maxAge: self.maxAge, gender: self.filterGender, lat: self.coordinate!.latitude, long: self.coordinate!.longitude)
            }else {
                self.getExploreList()
            }
        }
    }
    
    func koloda(_ koloda: KolodaView, didSelectCardAt index: Int) {
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(self.swipeUserArray[index]["id"] as! Int)"
        vc.fromPage = "Explore"
        vc.isFromSwipe = true
        Singleton.shared.isToReload = false
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func koloda(_ koloda: KolodaView, shouldSwipeCardAt index: Int, in direction: SwipeResultDirection) -> Bool {
        if self.isRequestButtonTapped {
            self.isRequestButtonTapped = false
            return true
        }else {
            return false
        }
    }
    
    func koloda(_ koloda: KolodaView, didSwipeCardAt index: Int, in direction: SwipeResultDirection) {
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        if (direction == .right) && (self.swipeUserArray.count > 0) && (self.kolodaView.currentCardIndex > 0){
            self.currentUser = UserInfo(self.swipeUserArray[self.kolodaView.currentCardIndex - 1])
            let userID = self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["id"] as! Int
            self.currentQbID = "\(String(describing: self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["quickblox_id"]!))"
            self.likeOrDislikUserRequest(1, userID: userID)
        }else if (direction == .left) && (self.swipeUserArray.count > 0) && (self.kolodaView.currentCardIndex > 0){
            let userID = self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["id"] as! Int
            self.currentUser = UserInfo(self.swipeUserArray[self.kolodaView.currentCardIndex - 1])
            self.currentQbID = "\(String(describing: self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["quickblox_id"]!))"
            
            self.likeOrDislikUserRequest(0, userID: userID)
            
        }else if (direction == .down) && (self.swipeUserArray.count > 0) {
            self.currentUser = UserInfo(self.swipeUserArray[self.kolodaView.currentCardIndex - 1])
            let userID = self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["id"] as! Int
            self.currentQbID = "\(String(describing: self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["quickblox_id"]!))"
            self.connectUserRequest(userID)
        }
    }
    
    func koloda(_ koloda: KolodaView, allowedDirectionsForIndex index: Int) -> [SwipeResultDirection] {
        return [.left, .right, .down]
    }
}

//MARK:- Koloda View Data Source
extension ExploreVC: KolodaViewDataSource {
    
    func kolodaNumberOfCards(_ koloda:KolodaView) -> Int {
//        self.noExploreUserLbl.isHidden = (self.swipeUserArray.count != 0) && self.isSwipeActive
        return self.swipeUserArray.count;
    }
    
    func kolodaSpeedThatCardShouldDrag(_ koloda: KolodaView) -> DragSpeed {
        return .fast
    }
    
    func koloda(_ koloda: KolodaView, viewForCardAt index: Int) -> UIView {
        
        let view = Bundle.main.loadNibNamed("KolodaCustomView", owner: self, options: nil)![0] as? KolodaViewCustom
        view!.frame = self.kolodaViewFrame
        let dict = self.swipeUserArray[index]
        if let imageURL = dict["profile_image"] as? String {
            view?.backImgView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        }
        view?.nameLbl.text = dict["name"] as? String ?? ""
        
        let formattedString = NSMutableAttributedString()
        let age = dict["age"] as? Int ?? 0
        
        formattedString
            .bold("Age:")
            .normal(" \(age)")
        view?.ageLabel.attributedText = formattedString
//        view?.ageLabel.text = "Age: \(dict["age"]!)"
        
        DispatchQueue.global().sync {
            if let images = dict["profile_images"] as? [[String:Any]] {
                view?.images = images
            }
        }
        return view!
    }
}
