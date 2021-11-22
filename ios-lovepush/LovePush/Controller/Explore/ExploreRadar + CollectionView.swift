//
//  ExploreRadar + CollectionView.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/07/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit

extension ExploreVC: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        self.noDataLbl.isHidden = (self.radarUserArray.count > 0) && !isSwipeActive
        return self.radarUserArray.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "RecommendedProfilesCell2", for: indexPath) as! RecommendedProfilesCell
        
        let dict = self.radarUserArray[indexPath.row]
        if let imageURL = dict["profile_image"] as? String {
            cell.profileImg.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        }
        
        cell.nameLbl.text = dict["name"] as? String ?? ""
        
        var distance = dict["distance"] as? Double ?? 0
        distance = distance.rounded(.up)
        
        cell.percentageLbl.text = ("\(distance)" != "") ? "\(distance) KM".replacingOccurrences(of: ".0", with: "") : ""
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(self.radarUserArray[indexPath.row]["id"] as! Int)"
        vc.fromPage = "Explore"
        Singleton.shared.isToReload = false
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let width = (collectionView.frame.size.width/2)-5
        return CGSize(width: width, height: width)
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        DispatchQueue.global().sync {
            if (((scrollView.contentOffset.y + scrollView.frame.size.height) > scrollView.contentSize.height ) ){
                if (self.radarCurrentPageNo < self.radarLastPageNo) && !self.isScrollingEnabled {
                    self.isScrollingEnabled = true
                    self.radarCurrentPageNo += 1
                     Helper.sharedInstance.ShowLoaderOnView(view: self.view)
                    if Singleton.shared.isExploreFilter && self.coordinate != nil {
                        self.getRadarFilterList(self.filterGender, minAge: self.minAge, maxAge: self.maxAge, lat: self.coordinate!.latitude, long: self.coordinate!.longitude)
                    }else {
                        self.getRadarList()
                    }
                }
            }
        }
    }
}
