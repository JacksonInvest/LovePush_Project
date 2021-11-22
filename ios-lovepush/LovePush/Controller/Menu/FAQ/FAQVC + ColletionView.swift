//
//  FAQVC + ColletionView.swift
//  LovePush
//
//  Created by Lavi Kumar on 08/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

extension FAQViewController: UICollectionViewDelegate, UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.faqCategoryList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "FAQCollectionViewCell", for: indexPath) as! FAQCollectionViewCell
        
        cell.superView.layer.cornerRadius = 8
        cell.superView.layer.borderWidth  = 1
        cell.superView.layer.borderColor  = UIColor.lightGray.cgColor
        cell.categoryNameLabel.text = self.faqCategoryList[indexPath.row]["category_name"] as? String ?? ""
        
        if self.selectedCategoryIndex == indexPath.row{
            cell.superView.backgroundColor = UIColor(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0)
            cell.categoryNameLabel.textColor = UIColor.white
        }else {
            cell.superView.backgroundColor = UIColor.white
            cell.categoryNameLabel.textColor = UIColor.black
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedQsnArr.removeAllObjects()
        self.selectedCategoryIndex = indexPath.row
        self.questionArray = self.faqCategoryList[indexPath.row]["qsn_list"]  as? [[String:Any]] ?? []
        self.categoryCollectionView.reloadData()
        self.questionTableView.reloadData()
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let titleLabel = UILabel()
        
        titleLabel.text = self.faqCategoryList[indexPath.row]["category_name"] as? String ?? ""

        titleLabel.sizeToFit()

        return CGSize(width: titleLabel.frame.width + 20, height: 32)
    }
}



