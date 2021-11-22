//
//  RegisterVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 15/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import GoogleSignIn

class RegisterVC: UIViewController {

    //MARK:- Outlets
    @IBOutlet weak var topView  : UIView!
    
    
    @IBOutlet weak var codeBtn  : UIButton!
    @IBOutlet weak var maleBtn  : UIButton!
    @IBOutlet weak var femaleBtn: UIButton!
    @IBOutlet weak var transBtn : UIButton!
    
    @IBOutlet weak var userNameTxt  : UITextFieldCustomClass!
    @IBOutlet weak var emailTxt     : UITextFieldCustomClass!
    @IBOutlet weak var phoneTxt     : UITextFieldCustomClass!
    @IBOutlet weak var passwordTxt  : UITextFieldCustomClass!
    @IBOutlet weak var referralTxt  : UITextFieldCustomClass!
    
    @IBOutlet weak var confirmPasswordTxt : UITextFieldCustomClass!
    @IBOutlet weak var referralCodeView   : UIView!
    
    @IBOutlet weak var termPolicyLabel    : UILabel!
    
    
    //MARK:- Variables
    var completeWithSocialView: CompleteSocialView!
    var loginType = 0
    
    var selectedPlaceCoordinates = CLLocationCoordinate2D()
    var cityName = "city"
    var countryName = "country"
    var isAgreeTerms = false
    var instagramLogin: InstagramViewController!
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        self.referralCodeView.addDashedBorder()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.topView.addBottomShadow()
        
        UIApplication.shared.statusBarStyle = .lightContent
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func CountryCodeAction(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "SRCountryPickerControllerID") as! SRCountryPickerController
        vc.countryDelegate = self;
        self.present(vc, animated: true, completion: nil)
        self.completeWithSocialView = nil
    }
    
//    @IBAction func AddressAction(_ sender: UIButton) {
//        self.presentPlacePicker()
//    }
    
    @IBAction func MaleAction(_ sender: UIButton) {
//        if(sender.currentImage == UIImage(named: "radioOff")) {
            self.maleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.femaleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transBtn.setImage(UIImage(named: "radioOff"), for: .normal)
//        }
    }
    
    @IBAction func FemaleAction(_ sender: UIButton) {
            self.femaleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.maleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transBtn.setImage(UIImage(named: "radioOff"), for: .normal)
    }
    
    @IBAction func transgenderAction(_ sender: UIButton) {
            self.transBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.femaleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.maleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
    }
    
    @IBAction func NextAction(_ sender: UIButton) {
        
//        if self.isValidAllField() {
//            if (self.referralTxt.text!.checkIsEmpty()) {
//                self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "Not Sure", title: "Love Push", message: "Are you sure you don't have any referral code.") { (isYes) in
//                    if isYes {
//                        self.registerWithEmail()
//                    }
//                }
//            }else {
//                self.registerWithEmail()
//            }
//        }
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "CreateProfileID") as! CreateProfileVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func termsConditionButtonTapped(_ sender: UIButton) {
        if sender.currentImage == #imageLiteral(resourceName: "radioOff") {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "LegacyViewController") as! LegacyViewController
            vc.isFromSetting = false
            vc.completion = { isAgree in
                if isAgree! {
                    self.isAgreeTerms = true
                    sender.setImage(#imageLiteral(resourceName: "radioOn"), for: .normal)
                }
            }
            self.navigationController?.pushViewController(vc, animated: true)
        }else {
            self.isAgreeTerms = false
            sender.setImage(#imageLiteral(resourceName: "radioOff"), for: .normal)
        }
    }
    
    @IBAction func facebookButtonTapped(_ sender: UIButton) {
        let facebookLogin = FacebookLogin()
        facebookLogin.delegate = self
        facebookLogin.startFacebook(self)
    }
    
    @IBAction func instagramButtonTapped(_ sender: UIButton) {
        self.loginWithInstagram()
    }
    
    @IBAction func googleButtonTapped(_ sender: UIButton) {
        GIDSignIn.sharedInstance().delegate=self
        GIDSignIn.sharedInstance().uiDelegate=self
        GIDSignIn.sharedInstance()?.signOut()
        GIDSignIn.sharedInstance().signIn()
    }
    
//    func presentPlacePicker() {
//        let config = GMSPlacePickerConfig(viewport: nil)
//        let placePicker = GMSPlacePickerViewController(config: config)
//        placePicker.delegate = self;
//        present(placePicker, animated: true, completion: nil)
//    }
    
//    func fetchCityAndCountry(from location: CLLocation, completion: @escaping (_ city: String?, _ country:  String?, _ error: Error?) -> ()) {
//        CLGeocoder().reverseGeocodeLocation(location) { placemarks, error in
//            completion(placemarks?.first?.locality,
//                       placemarks?.first?.country,
//                       error)
//        }
//    }
    
//    @objc func tapLabel(tap: UITapGestureRecognizer) {
//        guard let range1 = self.termPolicyLabel.text?.range(of: "Terms and Conditions".localized())?.nsRange else {
//            return
//        }
//        if tap.didTapAttributedTextInLabel(label: self.termPolicyLabel, inRange: range1) {
//            // Substring tapped
//        }
//
//        guard let range2 = self.termPolicyLabel.text?.range(of: "Privacy Policy".localized())?.nsRange else {
//            return
//        }
//        if tap.didTapAttributedTextInLabel(label: self.termPolicyLabel, inRange: range2) {
//            // Substring tapped
//        }
//    }
    
}


//MARK:- Country Picker
extension RegisterVC: CountrySelectedDelegate {
    
    func SRcountrySelected(countrySelected country: Country) {
        if self.completeWithSocialView == nil {
            self.codeBtn.setTitle(country.dial_code, for: .normal)
        }else {
            self.completeWithSocialView.phoneCodeLabel.setTitle(country.dial_code, for: .normal)
        }
    }
}

//MARK:- Facebook Delegate
extension RegisterVC : FBLoginDelegate {
    func didFBLoginFinished(_ fbUser: [String : Any]) {
        self.loginType = 2
        self.loginWithSocialMedial(fbUser)
    }
}

//MARK:- Google Login
extension RegisterVC : GIDSignInUIDelegate,GIDSignInDelegate {
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!,
              withError error: Error!) {
        if let error = error {
            print("\(error.localizedDescription)")
        } else {
            // Perform any operations on signed in user here.
            let userId = user.userID                  // For client-side use only!
//            let idToken = user.authentication.idToken // Safe to send to the server
            let fullName = user.profile.name
//            let givenName = user.profile.givenName
//            let familyName = user.profile.familyName
            let email = user.profile.email
            
            let dict = ["email": email, "name": fullName, "id": String(describing: userId!)]
            self.loginType = 3
            self.loginWithSocialMedial(dict)
        }
    }
    
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
        self.showAlert(title: "Love Push", message: String(describing: error.localizedDescription), completion: nil)
    }
}

//MARK:- Instagram Delegate
extension RegisterVC : InstagramLoginViewControllerDelegate {
    
    /**
     Register Insta User info
     - parameter instagramUser: Insta User info to login in local server
     **/
    func loginWithInstagram() {
        
        // 2. Initialize your 'InstagramLoginViewController' and set your 'ViewController' to delegate it
        instagramLogin = self.storyboard?.instantiateViewController(withIdentifier: "InstagramViewController") as? InstagramViewController
        instagramLogin.delegate = self
        
        
        // If you want a .stop (or other) UIBarButtonItem on the left of the view controller
        instagramLogin.navigationItem.leftBarButtonItem = UIBarButtonItem(barButtonSystemItem: .stop, target: self, action: #selector(dismissLoginViewController))
        
        // You could also add a refresh UIBarButtonItem on the right
        instagramLogin.navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .refresh, target: self, action: #selector(refreshPage))
        
        // 4. Present it inside a UINavigationController (for example)
        present(instagramLogin, animated: true)
    }
    
    @objc func dismissLoginViewController() {
        instagramLogin.dismiss(animated: true)
    }
    
    @objc func refreshPage() {
        //        instagramLogin.reloadPage()
    }
    
    func instagramLoginDidFinish(accessToken: String?, error: String?) {
        
        // Whatever you want to do ...
        
        // And don't forget to dismiss the 'InstagramLoginViewController'
        if error == nil {
            instagramLogin.dismiss(animated: true)
            self.fetchInstagramLoginDetails(accessToken!) { (response) in
                if let result = response {
                    self.didInstagramLoginFinished(result)
                }else {
                    self.showAlert(title: "Love Push", message: "A server with the specified hostname could not be found.", completion: nil)
                }
            }
        }
    }
    
    /**
     Login Insta Finished with user info
     - parameter instagramUser: Insta User info to login in local server
     **/
    func didInstagramLoginFinished(_ instagramUser: [String : Any]) {
        self.loginType = 4
        
        let userId = instagramUser["id"] as! String
        let fullName = instagramUser["full_name"] as! String
        
        let dict = ["name": fullName, "id": userId]
        
        self.loginWithInstagram(dict as [String : Any])
    }
}

extension UITapGestureRecognizer {
    
    func didTapAttributedTextInLabel(label: UILabel, inRange targetRange: NSRange) -> Bool {
        // Create instances of NSLayoutManager, NSTextContainer and NSTextStorage
        let layoutManager = NSLayoutManager()
        let textContainer = NSTextContainer(size: CGSize.zero)
        let textStorage = NSTextStorage(attributedString: label.attributedText!)
        
        // Configure layoutManager and textStorage
        layoutManager.addTextContainer(textContainer)
        textStorage.addLayoutManager(layoutManager)
        
        // Configure textContainer
        textContainer.lineFragmentPadding = 0.0
        textContainer.lineBreakMode = label.lineBreakMode
        textContainer.maximumNumberOfLines = label.numberOfLines
        let labelSize = label.bounds.size
        textContainer.size = labelSize
        
        // Find the tapped character location and compare it to the specified range
        let locationOfTouchInLabel = self.location(in: label)
        let textBoundingBox = layoutManager.usedRect(for: textContainer)
        
        let textContainerOffset = CGPoint(x: (labelSize.width - textBoundingBox.size.width) * 0.5 - textBoundingBox.origin.x, y: (labelSize.height - textBoundingBox.size.height) * 0.5 - textBoundingBox.origin.y)
        
        let locationOfTouchInTextContainer = CGPoint(x: locationOfTouchInLabel.x - textContainerOffset.x, y: locationOfTouchInLabel.y - textContainerOffset.y)
        let indexOfCharacter = layoutManager.characterIndex(for: locationOfTouchInTextContainer, in: textContainer, fractionOfDistanceBetweenInsertionPoints: nil)
        return NSLocationInRange(indexOfCharacter, targetRange)
    }
    
}

extension Range where Bound == String.Index {
    var nsRange:NSRange {
        return NSRange(location: self.lowerBound.encodedOffset,
                       length: self.upperBound.encodedOffset -
                        self.lowerBound.encodedOffset)
    }
}
