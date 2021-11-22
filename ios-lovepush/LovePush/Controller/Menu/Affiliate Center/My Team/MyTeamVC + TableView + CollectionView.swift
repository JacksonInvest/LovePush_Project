//
//  MyTeamVC + TableView + CollectionView.swift
//  LovePush
//
//  Created by Lavi Kumar on 26/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import UIKit

//MARK:- Collection View Delegate And Data Source
extension MyTeamViewController : UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.levelCount
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "MyTeamCollectionViewCell", for: indexPath) as! MyTeamCollectionViewCell
        cell.levelLabel.lzText = "Level".localized() + " \(indexPath.row + 1)"
        cell.levelLabel.backgroundColor = (self.selectedLevelIndex == indexPath.row) ? UIColor(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0) : UIColor.clear
        cell.levelLabel.textColor = (self.selectedLevelIndex == indexPath.row) ? UIColor.white : UIColor.darkGray
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedLevelIndex = indexPath.row
        self.userArr = self.myTeamData["level\(indexPath.row + 1)"] as! [[String:Any]]
        self.teamMemberTableView.reloadData()
        self.levelCollectionView.reloadData()
    }
}

//MARK:- Table View Delegate And Data Source
extension MyTeamViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.userArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyTeamTableViewCell", for: indexPath) as! MyTeamTableViewCell
        cell.userData = self.userArr[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {

        let user = UserInfo(self.userArr[indexPath.row])
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID     = "\(user.id!)"
        vc.isOnlyShow = (user.matchInfo != nil)
        vc.userInfo   = user
        vc.fromPage   = "My Team"
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
