//
//  ExploreVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import Koloda

class ExploreVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var radarCollectionView : UICollectionView!
    @IBOutlet weak var kolodaView       : KolodaView!
    
    
    @IBOutlet weak var likeBtn          : UIButton!
    @IBOutlet weak var dislikeBtn       : UIButton!
    @IBOutlet weak var connectButton    : UIButton!
    @IBOutlet weak var chatButton       : UIButton!
    @IBOutlet weak var swipeButton      : UIButton!
    @IBOutlet weak var radarButton      : UIButton!
    @IBOutlet weak var indicatorView    : UIView!
    @IBOutlet weak var noDataLbl        : UILabel!
    @IBOutlet weak var noExploreUserLbl : UILabel!
    @IBOutlet weak var kolodaViewHeight : NSLayoutConstraint!
    
    //MARK:- Variables
    var swipeUserArray = [[String:Any]]()
    var pageNo = 1
    var totalCountAvailable = 0
    var totalUserCountFetch = 0
    
    var radarUserArray = [[String:Any]]()
    var radarCurrentPageNo = 1
    var radarLastPageNo    = 1
    var totalRadarCountAvailable = 0
    var totalRadarUserCountFetch = 0
    
    var isFilterEnable = false
    var currentQbID = "0"
    var isSwipeActive = true
    var isScrollingEnabled = false
    var minAge = 0
    var maxAge = 0
    var filterGender = 0
    var coordinate : CLLocationCoordinate2D?
    var currentUser : UserInfo!
    var isRequestButtonTapped = false
    var kolodaViewFrame : CGRect!
    var isAlreadyHitSwipeAPI = false
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.kolodaView.dataSource = self
        self.kolodaView.delegate = self
        self.unHideViews()
        
        self.noDataLbl.isHidden = true
        self.noExploreUserLbl.isHidden = true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if Singleton.shared.isSwipeProfileReloadable {
            Singleton.shared.isSwipeProfileReloadable = false
            self.viewDidAppear(false)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
//        self.kolodaViewHeight.constant = UIScreen.main.bounds.height/2
        
        self.kolodaViewFrame = self.kolodaView.frame
        
        if self.isLocationEnable() {
            
            if !Singleton.shared.isExploreFilter {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    
                    Singleton.shared.isExploreFilter = false
                    
                    if Singleton.shared.currentLocation != nil && Singleton.shared.isToReload {
                        
                        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
                        
                        if !self.isAlreadyHitSwipeAPI {
                            self.isAlreadyHitSwipeAPI = true
                            self.hideViews()
                            self.swipeUserArray.removeAll()
                            self.kolodaView.resetCurrentCardIndex()
                            self.kolodaView.reloadData()
                            self.kolodaView.resetCurrentCardIndex()
                            self.pageNo = 1
                            self.getExploreList()
                        }
                    
                        //                    if self.radarUserArray.count == 0 {
                        self.radarUserArray.removeAll()
                        self.radarCollectionView.reloadData()
                        self.radarCurrentPageNo = 1
                        self.getRadarList()
                        //                    }
                    }
                    
                    if !Singleton.shared.isToReload {
                        Singleton.shared.isToReload = true
                    }
                }
            }else {
                
                //                if self.isSwipeActive {
                //                    self.radarCollectionView.isHidden = true
                //                    self.noExploreUserLbl.isHidden = (self.swipeUserArray.count > 0)
                //                    self.noDataLbl.isHidden = true
                //                }else {
                //                    self.radarCollectionView.isHidden = false
                //                    self.noDataLbl.isHidden = self.radarUserArray.count > 0
                //                    self.noExploreUserLbl.isHidden = true
                //                }
            }
        }
    }
    
    func isLocationEnable()-> Bool {
        if CLLocationManager.locationServicesEnabled() {
            switch CLLocationManager.authorizationStatus() {
                
            case .notDetermined:
                // Request when-in-use authorization initially
                // This is the first and the ONLY time you will be able to ask the user for permission
                return false
                
            case .restricted, .denied:
                // Disable location features
                return false
                
            case .authorizedWhenInUse, .authorizedAlways:
                // Enable features that require location services here.
                print("Full Access")
                return true
            }
        }else {
            return false
        }
    }
    
    //MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func FilterAction(_ sender: UIButton) {
        Singleton.shared.isExploreFilter = true
        
        if Singleton.shared.findLocation == nil {
            if Singleton.shared.currentLocation != nil {
                Singleton.shared.findLocation = Singleton.shared.currentLocation!
                Singleton.shared.findAddress  = Singleton.shared.currentAddress
            }
        }
        
        if self.isSwipeActive {
            self.noExploreUserLbl.isHidden = (self.swipeUserArray.count != 0)
        }else {
            self.noDataLbl.isHidden = (self.radarUserArray.count > 0)
        }
       
        let obj = homeStoryboard.instantiateViewController(withIdentifier: "ExploreFilterID") as! ExploreFilterVC
        obj.filterDelegate = self
        obj.isFilterSwipe  = self.isSwipeActive
        self.navigationController?.pushViewController(obj, animated: true)
    }
    
    @IBAction func LikeAction(_ sender: UIButton) {
        
        if SubscriptionPlan.shared.sendingRequest == "" {
            self.showAlert(title: "Love Push", message: "Your daily limit to send new request is over.", completion: nil)
        }else {
            if self.swipeUserArray.count > 0 {
                self.isRequestButtonTapped = true
                self.kolodaView.swipe(.right)
            }
        }
    }
    
    @IBAction func chatAction(_ sender: UIButton) {
        if SubscriptionPlan.shared.sendingRequest == "" {
            self.showAlert(title: "Love Push", message: "Your daily limit to send new request is over.", completion: nil)
        }else {
            if self.swipeUserArray.count > 0{
                self.isRequestButtonTapped = true
                self.kolodaView.swipe(.right)
                self.currentUser = UserInfo(self.swipeUserArray[self.kolodaView.currentCardIndex - 1])
                let userID = self.swipeUserArray[self.kolodaView.currentCardIndex]["id"] as! Int
                self.currentQbID = "\(String(describing: self.swipeUserArray[self.kolodaView.currentCardIndex - 1]["quickblox_id"]!))"
                self.sendChatRequest(userID) { (isSuccess) in }
            }
        }
    }
    
    @IBAction func DislikeAction(_ sender: UIButton) {
        if self.swipeUserArray.count > 0 {
            self.isRequestButtonTapped = true
            self.kolodaView.swipe(.left)
        }
    }
    
    @IBAction func connectButton(_ sender: UIButton) {
        if SubscriptionPlan.shared.sendingRequest == "" {
            self.showAlert(title: "Love Push", message: "Your daily limit to send new request is over.", completion: nil)
        }else {
            if self.swipeUserArray.count > 0 {
                self.isRequestButtonTapped = true
                self.kolodaView.swipe(.down)
            }
        }
    }
    
    @IBAction func headerButtonTapped(_ sender : UIButton) {
        
        if Singleton.shared.currentLocation != nil {
            self.isSwipeActive = (sender.tag == 0)
          
            self.indicatorView.center.x = sender.center.x
            let redColor = UIColor(red: 184/255, green: 2/255, blue: 24/255, alpha: 1.0)
            self.radarButton.setTitleColor((sender.tag == 1) ? redColor : UIColor.lightGray, for: .normal)
            self.swipeButton.setTitleColor((sender.tag == 0) ? redColor : UIColor.lightGray, for: .normal)
            
            if (sender.tag == 0) {
                self.radarCollectionView.isHidden = true
                if (self.swipeUserArray.count > 0) {
                    self.unHideViews()
                }else {
                    self.hideViews()
                }
            }else {
                self.radarCollectionView.isHidden = false
                self.noDataLbl.isHidden = (self.radarUserArray.count > 0)
                self.noExploreUserLbl.isHidden = true
            }
        }
    }
    
    func hideViews() {
        DispatchQueue.main.async {
            self.noExploreUserLbl.isHidden = false;
            self.noDataLbl.isHidden     = true
            self.kolodaView.isHidden    = true;
            self.likeBtn.isHidden       = true;
            self.connectButton.isHidden = true
            self.dislikeBtn.isHidden    = true;
            self.chatButton.isHidden    = true
        }
    }
    
    func unHideViews() {
        DispatchQueue.main.async {
            self.noDataLbl.isHidden         = true
            self.noExploreUserLbl.isHidden  = true;
            self.kolodaView.isHidden        = false;
            self.likeBtn.isHidden           = false;
            self.connectButton.isHidden     = false
            self.dislikeBtn.isHidden        = false;
            self.chatButton.isHidden        = false
        }
    }
}
//MARK:- Explore Filter Delegate
extension ExploreVC: ExploreFilterDelegate {
    
    func didStartFilter(minAge: Int, maxAge: Int, gender: Int, lat: Double?, long: Double?) {
        
        self.maxAge             = maxAge
        self.minAge             = minAge
        self.filterGender       = gender
        self.coordinate         = CLLocationCoordinate2D(latitude: lat!, longitude: long!)
        
        if self.isSwipeActive {
            self.hideViews()
            self.noDataLbl.isHidden = true
            self.noExploreUserLbl.isHidden = (self.swipeUserArray.count > 0)
            self.kolodaView.isHidden = (self.swipeUserArray.count > 0)
        }else {
            self.noExploreUserLbl.isHidden = true
            self.noDataLbl.isHidden = self.radarUserArray.count > 0
        }
        
        DispatchQueue.global().sync {
            self.pageNo = 1
            self.swipeUserArray.removeAll()
            self.kolodaView.resetCurrentCardIndex()
            self.kolodaView.reloadData()
//            self.hideViews()
        }
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        self.getFilteredData(minAge: minAge, maxAge: maxAge, gender: gender, lat: lat!, long: long!)
        
        self.radarUserArray.removeAll()
        self.radarCollectionView.reloadData()
        self.radarCurrentPageNo = 1
        self.getRadarFilterList(gender, minAge: minAge, maxAge: maxAge, lat: lat!, long: long!)
    }
}
