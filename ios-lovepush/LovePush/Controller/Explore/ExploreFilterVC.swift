//
//  ExploreFilterVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 20/03/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
import LocationPicker
import RangeSeekSlider

protocol ExploreFilterDelegate {
    func didStartFilter(minAge: Int, maxAge: Int, gender: Int, lat: Double?, long: Double?)
}

class ExploreFilterVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var slider         : RangeSeekSlider!
    @IBOutlet weak var maleButton     : UIButton!
    @IBOutlet weak var femalButton    : UIButton!
    @IBOutlet weak var transButton    : UIButton!
    @IBOutlet weak var locationView   : UIView!
    @IBOutlet weak var locationLabel  : UILabel!
    @IBOutlet weak var ageTopSpace    : NSLayoutConstraint!
    @IBOutlet weak var locationHeight : NSLayoutConstraint!
    
    
    //MARK:- Variables
    var filterDelegate: ExploreFilterDelegate?
    var selectedGenderID = 0
    var isFilterSwipe    = true
    var filterCoordinate : CLLocationCoordinate2D?
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        DispatchQueue.global().sync {
            if Singleton.shared.findLocation != nil {
                self.locationLabel.text      = Singleton.shared.findAddress
                self.filterCoordinate        = Singleton.shared.findLocation
                self.slider.selectedMinValue = Singleton.shared.minAge
                self.slider.selectedMaxValue = Singleton.shared.maxAge
                if Singleton.shared.filterGender != 0 {
                    let button = UIButton()
                    button.tag = Singleton.shared.filterGender
                    self.genderButtonTapped(button)
                }
            }
        }
        self.locationLabel.textColor = (self.locationLabel.text!.isEmpty || self.locationLabel.text == "Search Location") ? UIColor.lightGray : UIColor.black
//       self.manageTopSpace()
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
    }
    
//    func manageTopSpace() {
//        self.locationView.isHidden   = self.isFilterSwipe
//        self.locationHeight.constant = self.locationLabel.frame.size.height + 25
//        self.ageTopSpace.constant    = self.isFilterSwipe ? 30 : self.locationLabel.frame.size.height + 100
//    }
    
    //MARK:- Button Action
    @IBAction func BackAction(_ sender: UIButton) {
//        Singleton.shared.isExploreFilter = false
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func pickLocationButtonTapped(_ sender:UIButton) {
        DispatchQueue.global().sync {
            self.navigationController?.navigationBar.isHidden = false
        }
        self.pickLocation { (location) in
            if location != nil {
                self.filterCoordinate = location?.coordinate
                (UIApplication.shared.delegate as! AppDelegate).getAddress(coordinate: location!.coordinate, completion: { (address) in
                    Singleton.shared.findAddress    = address
                    Singleton.shared.findLocation   = location!.coordinate
                    
                    self.locationLabel.text         = address
                    self.locationLabel.textColor    = UIColor.black
                    
//                    self.manageTopSpace()
                })
            }
        }
    }
    
    @IBAction func genderButtonTapped(_ sender: UIButton ) {
        Singleton.shared.filterGender = sender.tag
        if sender.tag == 1 {
            self.maleButton.setImage(#imageLiteral(resourceName: "radioOn"), for: .normal)
            self.femalButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
            self.transButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
        }else if sender.tag == 2 {
            self.maleButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
            self.femalButton.setImage(#imageLiteral(resourceName: "radioOn"), for: .normal)
            self.transButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
        }else if sender.tag == 3 {
            self.maleButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
            self.femalButton.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
            self.transButton.setImage(#imageLiteral(resourceName: "radioOn"), for: .normal)
        }
        self.selectedGenderID = sender.tag
    }
    
    @IBAction func submitButtonTapped(_ sender: UIButton) {
        if self.selectedGenderID == 0 {
            self.showAlert(title: "Love Push", message: "Please Select Gender", completion: nil)
        }else {
            
            if self.filterCoordinate == nil {
                self.showAlert(title: "Love Push", message: "Please Select Location", completion: nil)
            }else {
                print("Love PushÖÖÖ \(self.slider.selectedMinValue)")
                
                Singleton.shared.minAge = self.slider.selectedMinValue
                Singleton.shared.maxAge = self.slider.selectedMaxValue
                
                let minAge = Int(round(self.slider.selectedMinValue))
                self.filterDelegate?.didStartFilter(minAge: minAge, maxAge:Int(self.slider.selectedMaxValue), gender: self.selectedGenderID, lat: self.filterCoordinate!.latitude, long: self.filterCoordinate!.longitude)
            }
            
            self.navigationController?.popViewController(animated: false)
        }
    }
    
    
}
