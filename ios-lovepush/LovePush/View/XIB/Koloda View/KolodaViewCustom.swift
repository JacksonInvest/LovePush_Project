//
//  KolodaViewCustom.swift
//  Rum-A
//
//  Created by Lavi Kumar on 24/10/18.
//  Copyright © 2018 Lavi Kumar. All rights reserved.
//

import UIKit

class KolodaViewCustom: UIView {

    //MARK:- Outlets
    @IBOutlet weak var backgroundVew        : UIViewCustomClass!
    @IBOutlet weak var scrollView           : UIScrollView!
    @IBOutlet weak var backImgView          : UIImageView!
    
    @IBOutlet weak var nameLbl              : UILabel!
    @IBOutlet weak var ageLabel             : UILabel!
    
    @IBOutlet weak var leftArrowButton      : UIButton!
    @IBOutlet weak var rightArrorButton     : UIButton!
    
    @IBOutlet weak var imageCollectionView  : UICollectionView!
    
    //MARK:- Variables
    var scrollIndex = 1
    
    var images:[[String:Any]]? {
        didSet {
            self.leftArrowButton.isHidden = (images!.count == 0) || (images!.count == 1)
            self.rightArrorButton.isHidden = (images!.count == 0) || (images!.count == 1)
            
            
            if self.images!.count > 0 {
                
                self.imageCollectionView.register(UICollectionViewCell.self, forCellWithReuseIdentifier: "ExploreImageCell")
                
                self.imageCollectionView.isHidden = false
                self.imageCollectionView.delegate = self
                self.imageCollectionView.dataSource = self
                self.imageCollectionView.reloadData()
                
                self.setTimer()
            }else {
                self.imageCollectionView.isHidden = true
            }
//            self.scrollView.isHidden = false
//            self.addImageInScroll()
        }
    }
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    
    override func draw(_ rect: CGRect) {
        // Drawing code
        
        self.backgroundVew.shadowAllSides()
        
    }
    
    func addImageInScroll() {
        for i in 0..<images!.count {
            let imageView = UIImageView()
            let x = self.backImgView.frame.size.width * CGFloat(i)
            imageView.frame = CGRect(x: x, y: 0, width: self.backImgView.frame.width, height: self.backImgView.frame.height)
            
            if let imageURL = images![i]["image"] as? String {
                imageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
                imageView.clipsToBounds = true
                imageView.contentMode = .scaleToFill
                self.backImgView.isHidden = true
//                self.imageCollectionView.isHidden = (self.images!.count == 0)
//                imageView.center.y = self.backImgView.center.y
//                imageView.frame.size.height = self.backImgView.frame.height
            }
            
            self.scrollView.contentSize.width = scrollView.frame.size.width * CGFloat(i + 1)
            self.scrollView.addSubview(imageView)
        }
    }
    
    //MARK:- Button Action
    @IBAction func leftScrollButtonTapped(_ sender: UIButton) {
//        if scrollView.contentOffset.x != 0 {
        
            let collectionBounds = self.imageCollectionView.frame
            let contentOffset = CGFloat(floor(self.imageCollectionView.contentOffset.x - collectionBounds.size.width))
            self.moveCollectionToFrame(contentOffset: contentOffset)
        
//            let xOffset = scrollView.contentOffset.x
//            let yOffset = scrollView.contentOffset.y
//            let customOffset = CGPoint(x: xOffset - self.frame.size.width, y: yOffset)
//            scrollView.setContentOffset(customOffset, animated: true)
//
//            self.leftArrowButton.isHidden = (scrollView.contentOffset.x == self.frame.size.width)
//            self.rightArrorButton.isHidden = false
//        }
    }
    @IBAction func rightScrollButtonTapped(_ sender: UIButton) {
        
//        if scrollView.contentOffset.x != self.frame.size.width * CGFloat((images!.count - 1)) {
        
            let collectionBounds = self.imageCollectionView.frame
            let contentOffset = CGFloat(floor(self.imageCollectionView.contentOffset.x + collectionBounds.size.width))
            self.moveCollectionToFrame(contentOffset: contentOffset)
        
//            let xOffset = scrollView.contentOffset.x
//            let yOffset = scrollView.contentOffset.y
//            let customOffset = CGPoint(x: xOffset + self.frame.size.width, y: yOffset)
//            scrollView.setContentOffset(customOffset, animated: true)
//
//            self.rightArrorButton.isHidden = (scrollView.contentOffset.x == self.frame.size.width * CGFloat((images!.count - 2)))
//            self.leftArrowButton.isHidden = false
//        }
    }
}

extension KolodaViewCustom: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        return self.images!.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        
        
        let cell = self.imageCollectionView.dequeueReusableCell(withReuseIdentifier: "ExploreImageCell", for: indexPath)
        
        if let imageURL = images![indexPath.row]["image"] as? String {
            let imageView = UIImageView()
            imageView.frame = CGRect(x: 0, y: 0, width: self.frame.width, height: cell.frame.height)
            imageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            imageView.contentMode = .scaleToFill
            cell.addSubview(imageView)
//            cell.profileImageView.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
        }
        
        return cell
    }
    
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        return CGSize(width: collectionView.frame.size.width, height: collectionView.frame.size.height)
    }
    
    func setTimer() {
        let _ = Timer.scheduledTimer(timeInterval: 3.0, target: self, selector: #selector(autoScroll), userInfo: nil, repeats: true)
    }
    
    @objc func autoScroll() {
        if self.scrollIndex < self.images!.count {
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


