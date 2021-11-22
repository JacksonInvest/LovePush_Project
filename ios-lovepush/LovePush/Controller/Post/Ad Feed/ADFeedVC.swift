//
//  ADFeedVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/01/19.
//  Copyright © 2019 Lavi Kumar.. All rights reserved.
//

import UIKit

class ADFeedVC: UIViewController {

    //MARK:- Outlets
    @IBOutlet weak var adTableView : UITableView!
    @IBOutlet weak var noPostLabel : UILabel!
    @IBOutlet weak var titleLabel  : UILabel!
    
    //MARK:- Variables
    var pageNo   = 1
    var lastPage = 1
    var isAdFeed = true
    var postList = [[String:Any]]()
    var tempArr  = [[String:Any]]()
    let selectedPostType   = AdType(rawValue: 1)
    var isScrollingEnabled = false
    
    var latitude  = 0.0
    var logitutde = 0.0
    
    var filterPostType = 0
    var isFilter = false
    
    var url: String!{
        get {
            return self.isAdFeed ? URLConstant.getAllPostsAds : URLConstant.getMyPostAds
        }
    }
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if Singleton.shared.currentLocation != nil {
            self.latitude = Singleton.shared.currentLocation!.latitude
            self.logitutde = Singleton.shared.currentLocation!.longitude
        }

        self.titleLabel.text = self.isAdFeed ? "AD FEED".localized() : "MY FEED".localized()
        self.requestToGetPost(self.url, latitude: self.latitude, longitude: self.logitutde, isFilter: false)
        self.adTableView.rowHeight = UITableViewAutomaticDimension
        
        //Add observer to reload list after update own post or filter parameter
        NotificationCenter.default.addObserver(forName: Notification.Name(rawValue: "Reload Post"), object: nil, queue: nil) { (notification) in
            
            if notification.userInfo == nil {//Get ads list for default location
                self.postList.removeAll()
                self.requestToGetPost(self.url, latitude: 0.0, longitude: 0.0, isFilter: false)
            }else {
                //Get ads list for added location or filter type
                let adType = (notification.userInfo as! [String:Any])["Ad_type"] as? Int ?? 0
                
                self.filterPostType = adType
                
                self.latitude  = Singleton.shared.findLocation!.latitude
                self.logitutde = Singleton.shared.findLocation!.longitude
                self.isFilter  = true
                
                self.tempArr.removeAll()
                self.postList.removeAll()
                
                self.requestToGetPost(self.url, latitude: Singleton.shared.findLocation!.latitude, longitude: Singleton.shared.findLocation!.longitude, isFilter: true)
            }
            
            NotificationCenter.default.removeObserver(self, name: Notification.Name(rawValue: "Reload Post"), object: nil)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
   //MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func FilterAction(_ sender: UIButton) {
        if self.tempArr.count > 0 {
            self.postList = self.tempArr
            self.tempArr.removeAll()
        }
        let obj = homeStoryboard.instantiateViewController(withIdentifier: "FilterID") as! FilterVC
        obj.modalPresentationStyle = .overCurrentContext
        obj.modalTransitionStyle = .crossDissolve
        obj.isAd = true;
        
        self.navigationController?.pushViewController(obj, animated: true)
    }
}
