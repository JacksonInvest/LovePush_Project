//
//  PostAdsVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import LocationPicker
import CoreLocation
import MapKit

class PostAdsVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var headerTitleLabel     : UILabel!
    @IBOutlet weak var maxDistanceLabel     : UILabel!
    @IBOutlet weak var titleTxt             : UITextField!
    @IBOutlet weak var descriptionTxtView   : UITextView!
    @IBOutlet weak var loveAdBtn            : UIButton!
    @IBOutlet weak var connectAdBtn         : UIButton!
    @IBOutlet weak var imgView              : UIImageView!
    @IBOutlet weak var locationBtn          : UIButton!
    @IBOutlet weak var submitButton         : UIButton!
    @IBOutlet weak var anonymouslySwitch    : UISwitch!
    @IBOutlet weak var locationLabel        : UILabel!
    @IBOutlet weak var anonymousLabel       : UILabel!
    @IBOutlet weak var radiusSlider         : UISlider!
    @IBOutlet weak var contentView          : UIView!
    @IBOutlet weak var postButton           : UIView!
    @IBOutlet weak var loveConnectView      : UIView!
    @IBOutlet weak var locetionView         : UIView!
    @IBOutlet weak var radiusView           : UIView!
    @IBOutlet weak var descriptionViewHeight: NSLayoutConstraint!
    @IBOutlet weak var loveConnectHeight    : NSLayoutConstraint!
    @IBOutlet weak var locationViewHeight   : NSLayoutConstraint!
    @IBOutlet weak var radiusViewHeight     : NSLayoutConstraint!
    @IBOutlet weak var radiusTopSpace       : NSLayoutConstraint!
    
    //MARK:- Variables
    var myPickerController = UIImagePickerController()
    var selectedLocation   = CLLocationCoordinate2D()
    var isHome             = false
    var isForUpdate        = false
    var isImageSelected    = false
    
    var post: Post?
    
    // #MARK:- View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.isHome {
            self.normalPostSetup()
        }
        self.headerTitleLabel.text = self.isHome ? ((self.post == nil) ? "POST".localized() : "UPDATE".localized()) : ((self.post != nil) ? "UPDATE ADS".localized() : "POST AD".localized())
        
        self.titleTxt.placeholder = self.isHome ? "Title" : "What are you looking for?"
        self.descriptionTxtView.text = self.isHome ? "Description" : "Let our community know more about your ambitions!"
        
//        self.radiusTopSpace.constant = self.isHome ? 8 : 70
        
        self.anonymousLabel.isHidden = self.isHome
        self.anonymouslySwitch.isHidden = self.isHome
        
        self.submitButton.setTitle(self.headerTitleLabel.text, for: .normal)
        
        if self.post != nil {
            
            defaultPostSetup(self.post!)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
 
    func normalPostSetup() {//Post For Home Page
        self.loveConnectView.isHidden    = true
        self.locetionView.isHidden       = true
        self.radiusView.isHidden         = true
        self.loveConnectHeight.constant  = 0
        self.locationViewHeight.constant = 0
        self.radiusViewHeight.constant   = 0
    }
    
    func defaultPostSetup(_ post: Post) {
        
        self.titleTxt.text                = post.title!
        self.selectedLocation             = post.coordinate!
        self.locationLabel.text           = post.address!
        self.radiusSlider.value           = post.radius!
        self.anonymouslySwitch.isOn       = (post.anonymouslStatus! == 1)
        self.locationLabel.textColor      = UIColor.black
        self.descriptionTxtView.text      = post.description!
        self.descriptionTxtView.textColor = UIColor.black
        
        self.loveAdBtn.setImage((post.type == 1) ? #imageLiteral(resourceName: "radioOn") : #imageLiteral(resourceName: "radioOff") , for: .normal)
        self.connectAdBtn.setImage((post.type == 2) ? #imageLiteral(resourceName: "radioOn") : #imageLiteral(resourceName: "radioOff"), for: .normal)
    
        if post.url != nil {
            self.imgView.setImageWithoutBaseURL(post.url!, PlaceholderImage: #imageLiteral(resourceName: "default_image"))
            self.isImageSelected = true
        }else {
            self.isImageSelected = false
        }
    }
    
    //MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func LoveAdAction(_ sender: UIButton) {
//        if(sender.imageView?.image == UIImage(named: "radioOff")) {
            self.loveAdBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.connectAdBtn.setImage(UIImage(named: "radioOff"), for: .normal)
//        }
    }
    
    @IBAction func ConnectAdAction(_ sender: UIButton) {
//        if(sender.imageView?.image == UIImage(named: "radioOff")) {
            self.connectAdBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.loveAdBtn.setImage(UIImage(named: "radioOff"), for: .normal)
//        }
    }
    
    @IBAction func AddImageAction(_ sender: UIButton) {
        
        let alert = UIAlertController(title: "Choose Image", message: "", preferredStyle: .actionSheet)
        
        alert.addAction(UIAlertAction(title: "Camera", style: .default, handler: { action in
            
            self.camera()
        }))
        
        alert.addAction(UIAlertAction(title: "All Photos", style: .default, handler: { action in
            
            self.photoLibrary()
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { action in
            
        }))
        
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad ){
            
            if let currentPopoverpresentioncontroller = alert.popoverPresentationController{
                currentPopoverpresentioncontroller.barButtonItem = UIBarButtonItem(customView: sender)
                currentPopoverpresentioncontroller.permittedArrowDirections = UIPopoverArrowDirection.down;
                self.present(alert, animated: true, completion: nil)
            }
        }else{
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    @IBAction func radiusRangeChanged(_ sender: Any) {
        self.maxDistanceLabel.text = "\(self.radiusSlider.value)KM"
    }
    
    @IBAction func SetLocationAction(_ sender: UIButton) {
        DispatchQueue.global().sync {
            self.navigationController?.navigationBar.isHidden = false
        }
        self.pickLocation { (location) in
            if location != nil {
                self.selectedLocation = location!.coordinate
                (UIApplication.shared.delegate as! AppDelegate).getAddress(coordinate: location!.coordinate, completion: { (address) in
                    self.navigationController?.navigationBar.isHidden = true
                    self.locationLabel.text = address
                    self.locationLabel.textColor = UIColor.black
                })
            }
        }
    }
    
    @IBAction func PostAction(_ sender: UIButton) {
        
        if self.isValid() {
            
            self.view.endEditing(true)
            
            self.requestToAddOrUpdatePost((self.post == nil) ? nil : self.post!.id!)
        }
    }
    
    // #MARK:- Functions
    func camera()
    {
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            myPickerController = UIImagePickerController()
            myPickerController.delegate = self;
            myPickerController.sourceType = .camera
            myPickerController.allowsEditing = true;
            self.present(myPickerController, animated: true, completion: nil)
        }
        
    }
    
    func photoLibrary(){
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            myPickerController = UIImagePickerController()
            myPickerController.delegate = self;
            myPickerController.sourceType = .photoLibrary
            myPickerController.allowsEditing = true;
            self.present(myPickerController, animated: true, completion: nil)
        }
    }
}

extension PostAdsVC: UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        if let image = info[UIImagePickerControllerEditedImage] as? UIImage {
            
            DispatchQueue.main.async {
                
                if let imageData = image.jpeg(.lowest) {
                    
                    self.imgView.image = UIImage.init(data: imageData)
                    self.isImageSelected = true
                }else {
                    self.imgView.image = image
                    self.isImageSelected = true
                }
            }
        }
        
        picker.dismiss(animated: true, completion: nil)
    }
}

extension PostAdsVC : UITextViewDelegate {
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.text == "Description".localized() || textView.text == "Let our community know more about your ambitions!"{
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = self.isHome ? "Description" : "Let our community know more about your ambitions!"
            textView.textColor = UIColor.lightGray
        }
    }
}
