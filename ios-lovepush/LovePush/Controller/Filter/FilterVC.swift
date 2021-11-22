//
//  FilterVC.swift
//  LovePush
//
//  Created by Jatinder Singh on 17/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
import LocationPicker

class FilterVC: UIViewController {

    //MARK:- Outlets
    @IBOutlet weak var titleLbl        : UILabel!
    @IBOutlet weak var locationLabel   : UILabel!
    @IBOutlet weak var adView          : UIView!
    @IBOutlet weak var loveAdButton    : UIButton!
    @IBOutlet weak var connectAdButton : UIButton!
    
    //MARK:- Variables
    var isAd = Bool()
    var filterCoordinate : CLLocationCoordinate2D?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if(isAd) {
//            self.titleLbl.text = "FILTER BY AD"
            self.adView.isHidden = false;
        }
        
        if Singleton.shared.findLocation != nil {
            self.locationLabel.text = Singleton.shared.findAddress
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK:- Button Action
    @IBAction func loveConnectButtonTapped(_ sender: UIButton) {
        if sender.tag == 0 {
//            if(sender.currentImage == UIImage(named: "radioOff")) {
                self.loveAdButton.setImage(UIImage(named: "radioOn"), for: .normal)
                self.connectAdButton.setImage(UIImage(named: "radioOff"), for: .normal)
//            }
        }else {
//            if(sender.currentImage == UIImage(named: "radioOff")) {
                self.connectAdButton.setImage(UIImage(named: "radioOn"), for: .normal)
                self.loveAdButton.setImage(UIImage(named: "radioOff"), for: .normal)
//            }
        }
    }

    @IBAction func SubmitAction(_ sender: UIButton) {
        
        if self.locationLabel.text == "Location".localized() {
            self.showAlert(title: "Love Push", message: "Please Select Location", completion: nil)
        }else {
            var adType = AdType.Love.rawValue
            if(self.connectAdButton.currentImage == UIImage(named: "radioOn")) {
                adType = AdType.Connect.rawValue
            }
            
            NotificationCenter.default.post(name: Notification.Name(rawValue: "Reload Post"), object: nil, userInfo: ["Ad_type": adType])
            
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func CancelAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func selectLocationButtonTapped(_ sender: UIButton) {
        DispatchQueue.global().sync {
            self.navigationController?.navigationBar.isHidden = false
        }
        self.pickLocation { (location) in // Return Selected location data to filter List 
            if location != nil {
                self.filterCoordinate = location?.coordinate
                (UIApplication.shared.delegate as! AppDelegate).getAddress(coordinate: location!.coordinate, completion: { (address) in
                    Singleton.shared.findAddress    = address
                    Singleton.shared.findLocation   = location!.coordinate
                    
                    self.locationLabel.text         = address
                    self.locationLabel.textColor    = UIColor.black
                })
            }
        }
    }
}
