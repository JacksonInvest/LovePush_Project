//
//  CreateProfileVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import PhotosUI

class CreateProfileVC: UIViewController, UITextViewDelegate {
        
    //MARK:- Outlet
    @IBOutlet weak var userName : TextFieldWithPadding!
    @IBOutlet weak var nameTextField: TextFieldWithPadding!
    @IBOutlet weak var titleTxt: TextFieldWithPadding!
    @IBOutlet weak var ageTextField: TextFieldWithPadding!
    @IBOutlet weak var relationshipTxt: TextFieldWithPadding!
    @IBOutlet weak var addnewuserimg: UIImageView!
    @IBOutlet weak var languagesTxt: TextFieldWithPadding!
    @IBOutlet weak var physique: TextFieldWithPadding!
    //    @IBOutlet weak var eyeColor: TextFieldWithPadding!
    //    @IBOutlet weak var hairColor: TextFieldWithPadding!
    @IBOutlet weak var usrimgcollection: UICollectionView!
    @IBOutlet weak var religionTextField: TextFieldWithPadding!
    @IBOutlet weak var jobEmpTextField: TextFieldWithPadding!
    @IBOutlet weak var sizeTxt: TextFieldWithPadding!
    //    @IBOutlet weak var educationTxt: UITextField!
    @IBOutlet weak var hobbiesTxt: TextFieldWithPadding!
    @IBOutlet weak var heterosexualBtn: UIButton!
    @IBOutlet weak var bisexualBtn: UIButton!
    @IBOutlet weak var homosexualBtn: UIButton!
    @IBOutlet weak var aboutMeTxt: UITextViewExtension!
    @IBOutlet weak var lovePushLabel: UILabel!
    @IBOutlet weak var favTravelDestTxt: UITextField!
    @IBOutlet weak var loveIntenionTableView: UITableView!
    @IBOutlet weak var profileImageView: UIImageView!
    var datePicker = UIDatePicker()
    var userimages = [Data]()
    

    
    //MARK:- Variables
    var pickerView : PickerView!
    let lovaPushIntentions = ["Relationship","Friendship","Daily Activities", "No Expectation"]
    let relationshipStatus = ["Single","In Relationship","Married","Recently Separated"]
    let physiques          = ["Plump","Thin","Athletic","Muscular","Normal"]
    let ageList            = ["18","19","20",
                              "21","22","23","24","25","26","27","28","29","30",
                              "31","32","33","34","35","36","37","38","39","40",
                              "41","42","43","44","45","46","47","48","49","50",
                              "51","52","53","54","55","56","57","58","59","60",
                              "61","62","63","64","65","66","67","68","69","70",
                              "71","72","73","74","75","76","77","78","79","80"]
    var lovePushID = 0
    var relationshipID = 0
    var physiqueID = 0
    var selectType = ""
    var loveIntensionArr = [String]()
    var myPickerController = UIImagePickerController()
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.pickerView = PickerView()
        self.pickerView.delegate = self
//        self.nameTextField.text = User.shared.name
//        self.userName.text = User.shared.username
        aboutMeTxt.text = "About me".localized()
        aboutMeTxt.textColor = UIColor.lightGray
        self.addShadowToTableView()
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.keyboardNotification(notification:)),
                                               name: NSNotification.Name.UIKeyboardWillChangeFrame,
                                               object: nil)
        self.showDatePicker()

    }
    
    override func viewWillAppear(_ animated: Bool) {
        UIApplication.shared.statusBarStyle = .lightContent
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func keyboardNotification(notification: NSNotification) {
        self.pickerView.cancelClick()
    }
    
    //MARK:- Button Action
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func profileImageButtonTapped(_ sender: UIButton) {
        self.showImagePickerOption(true, sender: sender)
    }
    
    @IBAction func SubmitAction(_ sender: UIButton) {
        if (self.profileImageView.image == #imageLiteral(resourceName: "userPlaceholder")){
            Helper.sharedInstance.ShowAlert(message: "Please add profile image.", viewController: self, finished: {})
        }else if self.nameTextField.text!.checkIsEmpty() {
            Helper.sharedInstance.ShowAlert(message: "Please enter name.", viewController: self, finished: {})
        }else if nameTextField.text!.count < 3 || nameTextField.text!.count > 20 {
            Helper.sharedInstance.ShowAlert(message: "Name length should be minimum 3 and maximum 20 characters.", viewController: self, finished: {})
        } else if self.ageTextField.text!.checkIsEmpty() {
            Helper.sharedInstance.ShowAlert(message: "Please set age.", viewController: self, finished: {})
        } else {
            self.view.endEditing(true)
            self.createProfileRequest()
        }
        
    }
    
    @IBAction func HeterosexualAction(_ sender: UIButton){
        self.heterosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
        self.bisexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        self.homosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
    }
    

    @IBAction func BisexualAction(_ sender: UIButton) {
        self.bisexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
        self.heterosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        self.homosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
         
    }
    
    @IBAction func HomosexualAction(_ sender: UIButton) {
        self.homosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
        self.bisexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        self.heterosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        
    }
    
    @IBAction func selectLoveIntentionButtonTapped(_ sender : UIButton) {
        self.view.endEditing(true)
        self.loveIntenionTableView.isHidden = !self.loveIntenionTableView.isHidden
    }
    
    @IBAction func selectRelationStatus(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Relation Status"
        self.pickerView.removeFromSuperView()
        self.pickerView.addpickerView(self.relationshipStatus, self)
    }
    
//    @IBAction func selectAgeButtonTapped(_ sender: UIButton) {
//        self.view.endEditing(true)
//        self.selectType = "Age"
//
////        self.pickerView.removeFromSuperView()
////        self.pickerView.addpickerView(self.ageList, self)
//    }
    
    @IBAction func selectPhysiqueButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Physique"
        self.pickerView.removeFromSuperView()
        self.pickerView.addpickerView(self.physiques, self)
    }
  
    func showDatePicker(){
      //Formate Date
       datePicker.datePickerMode = .date

       //ToolBar
      let toolbar = UIToolbar();
      toolbar.sizeToFit()

      //done button & cancel button
        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(self.onClickDoneButton))
       let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel", style: .plain, target: self, action: #selector(self.onClickcancelButton))
      toolbar.setItems([cancelButton,spaceButton,doneButton], animated: false)

        datePicker.date = Date()
        datePicker.set18YearValidation()
      //        datePicker.locale = .current
        if #available(iOS 13.4, *) {
            datePicker.preferredDatePickerStyle = .wheels
        } else {
            // Fallback on earlier versions
        }
   // add toolbar to textField
        ageTextField.inputAccessoryView = toolbar
    // add datepicker to textField
        ageTextField.inputView = datePicker

      }
    
//    func showDatePicker() {
//        datePicker = UIDatePicker()
//        datePicker.date = Date()
//        datePicker.locale = .current
//        if #available(iOS 13.4, *) {
//            datePicker.preferredDatePickerStyle = .wheels
//        } else {
//            // Fallback on earlier versions
//        }
//        ageTextField.inputView = datePicker
//        //Write toolbar code for done button
//        let toolBar = UIToolbar()
//        toolBar.barStyle = .default
//        toolBar.isTranslucent = true
//        let space = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
//        let doneButton = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(onClickDoneButton))
//        toolBar.setItems([space, doneButton], animated: false)
//        toolBar.isUserInteractionEnabled = true
//        toolBar.sizeToFit()
//        datePicker.inputAccessoryView = toolBar //Change your TextField name here
//
//
//    }
    
    @objc func onClickcancelButton() {
        self.view.endEditing(true)
    }
    
    
    @objc func onClickDoneButton() {
        let dateselected = datePicker.date
        let dateformatter = DateFormatter()
        dateformatter.dateFormat = "yyyy-MM-dd"
        let str = dateformatter.string(from: dateselected)
        print(str)
        let agestr = self.calculateAge(dob: str).year
        print(agestr)
        self.ageTextField.text = "\(agestr)"
        self.view.endEditing(true)
    }
    
    @IBAction func lovePushDoneButtonTapped(_ sender: UIButton) {
        self.loveIntenionTableView.isHidden = true
    }
    
    func calculateAge(dob : String, format:String = "yyyy-MM-dd") -> (year :Int, month : Int, day : Int){
           let df = DateFormatter()
           df.dateFormat = format
           let date = df.date(from: dob)
           guard let val = date else{
               return (0, 0, 0)
           }
           var years = 0
           var months = 0
           var days = 0
           
           let cal = Calendar.current
           years = cal.component(.year, from: Date()) -  cal.component(.year, from: val)
           
           let currMonth = cal.component(.month, from: Date())
           let birthMonth = cal.component(.month, from: val)
           
           //get difference between current month and birthMonth
           months = currMonth - birthMonth
           //if month difference is in negative then reduce years by one and calculate the number of months.
           if months < 0
           {
               years = years - 1
               months = 12 - birthMonth + currMonth
               if cal.component(.day, from: Date()) < cal.component(.day, from: val){
                   months = months - 1
               }
           } else if months == 0 && cal.component(.day, from: Date()) < cal.component(.day, from: val)
           {
               years = years - 1
               months = 11
           }
           
           //Calculate the days
           if cal.component(.day, from: Date()) > cal.component(.day, from: val){
               days = cal.component(.day, from: Date()) - cal.component(.day, from: val)
           }
           else if cal.component(.day, from: Date()) < cal.component(.day, from: val)
           {
               let today = cal.component(.day, from: Date())
               let date = cal.date(byAdding: .month, value: -1, to: Date())
               
               days = date!.daysInMonth - cal.component(.day, from: val) + today
           } else
           {
               days = 0
               if months == 12
               {
                   years = years + 1
                   months = 0
               }
           }
           
           return (years, months, days)
       }
    
    //MARK:- Text View Delegate
    func textViewDidBeginEditing(_ textView: UITextView) {
        self.pickerView.cancelClick()
        if textView.textColor == UIColor.lightGray {
            textView.text = nil
            textView.textColor = UIColor.black
        }
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "About me".localized()
            textView.textColor = UIColor.lightGray
        }
    }
    
    func addShadowToTableView() {
        self.loveIntenionTableView.layer.shadowColor    = UIColor.black.cgColor
        self.loveIntenionTableView.layer.shadowOpacity  = 5
        self.loveIntenionTableView.layer.shadowOffset   = CGSize(width: 1, height: 1)
        self.loveIntenionTableView.layer.shadowRadius   = 10
        self.loveIntenionTableView.layer.borderWidth    = 1
        self.loveIntenionTableView.layer.borderColor    = UIColor.lightGray.cgColor
    }
    
}

//MARK:- Picker View Delegate
extension CreateProfileVC : PickerViewProtocol {
    func didPickerSelected(_ itemIndex: Int) {
        if self.selectType == "Love Intention" {
            //            self.lovePushID = itemIndex + 1
            //            self.titleTxt.text = self.lovaPushIntentions[itemIndex]
        }else if self.selectType == "Relation Status" {
            self.relationshipID = itemIndex + 1
            self.relationshipTxt.text = self.relationshipStatus[itemIndex].localized()
        }else if self.selectType == "Age" {
            self.ageTextField.text = self.ageList[itemIndex]
        }else if self.selectType == "Physique" {
            self.physiqueID = itemIndex + 1
            self.physique.text = self.physiques[itemIndex].localized()
        }
    }
}

//A2ZH9I

//MARK:- API

extension CreateProfileVC {
    /**
     Create Parameder to reguest to create user profile
     **/
    func createParam()-> [String:Any] {
        
        var sexualOrientation = 0
        if(self.heterosexualBtn.currentImage == UIImage(named: "radioOn")) {
            sexualOrientation = SexualOrientation.Hetero.rawValue
        }
        else if(self.bisexualBtn.currentImage == UIImage(named: "radioOn")) {
            sexualOrientation = SexualOrientation.Bi.rawValue
        }
        else {
            sexualOrientation = SexualOrientation.Homo.rawValue
        }
        
        let parameters = [
            "love_push_intention"   :self.loveIntensionArr.joined(separator: ","),
            "age"                   :self.ageTextField.text!,
            "name"                  :self.nameTextField.text!,
            "physique"              :(self.physiqueID == 0) ? "" : "\(self.physiqueID)",
            "religion"              :self.religionTextField.text!,
            "relationship"          :(self.relationshipID == 0) ? "" : "\(self.relationshipID)",
            "language"              :self.languagesTxt.text!,
            "size"                  :self.sizeTxt.text!,
            "haircolor"             :"",
            "eyecolor"              :"",
            "education"             :self.favTravelDestTxt.text!,
            "job"                   :self.jobEmpTextField.text!,
            "hobbies"               :self.hobbiesTxt.text!,
            "sexual_orientation"    :"\(sexualOrientation)",
            "aboutme"               :self.aboutMeTxt.text!,
            "user_id"               :"\(Helper.sharedInstance.userId())",
            "device_type"           :"2",
            "device_token"          :(Singleton.shared.deviceToken != nil) ?  Singleton.shared.deviceToken!.hexString : ""
            ] as [String : Any]
        
        return parameters
    }
    
    func getLovePushIds()-> String {
        let stringArray:[String] = self.loveIntensionArr.map { String($0) }
        return stringArray.joined(separator: ",")
    }
    
    /**
     Reguest to create profile of user
     **/
    func createProfileRequest() {
        
        let param = self.createParam()
        let header = ["locale": Singleton.shared.language]
        
        print(param)
        
        let imgData = self.profileImageView.image?.jpeg(.lowest)
       // let imgData = (self.profileImageView.image == #imageLiteral(resourceName: "userPlaceholder")) ? nil : UIImagePNGRepresentation(self.profileImageView.image!)
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostWithData(URLConstant.createProfile, params: param as! [String : String], header: header, fileData: imgData, fileKey: "profile_image") { (response, isSuccess) in
            
            //        WebAPI.requestToPOSTWithHeader(URLConstant.createProfile, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        UserDefaults.standard.setValue((response as! [String: Any])["token"] as! String, forKey: "token")
                        User(data)
                        
                        let dataSave = NSKeyedArchiver.archivedData(withRootObject: data)
                        UserDefaults.standard.set(dataSave, forKey: "userData")
                        UserDefaults.standard.synchronize()
                        
                        UserDefaults.standard.set(User.shared.qBUserID, forKey: "userQuickbloxId")
                      
                        //Login to quickblox server by User quickblox ID generated on backend with registration process
                        QuickbloxHandling.shared.quickBloxLogin(UInt(User.shared.qBUserID)!)
                        
                        UserDefaults.standard.set(true, forKey: "isLogin")
                        
                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                    
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}

//MARK:- Delegate

//Show List of Love Push intention
extension CreateProfileVC : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return lovaPushIntentions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LoveIntentionCreateProfileCell", for: indexPath) as! LoveIntentionTableViewCell
        cell.cPLovePushTitleLabel.text = self.lovaPushIntentions[indexPath.row].localized()
      
        if self.loveIntensionArr.lastIndex(of: self.lovaPushIntentions[indexPath.row].localized()) != nil {
            cell.cPCheckIcon.image = #imageLiteral(resourceName: "checked_blue")
        }else {
            cell.cPCheckIcon.image = #imageLiteral(resourceName: "uncheck")
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let index = self.loveIntensionArr.lastIndex(of: self.lovaPushIntentions[indexPath.row].localized()) {
            self.loveIntensionArr.remove(at: index)
        }else { self.loveIntensionArr.append(self.lovaPushIntentions[indexPath.row].localized())
        }
        self.lovePushLabel.text = (self.loveIntensionArr.count == 0) ? "Select LovePush Intention".localized(): self.loveIntensionArr.joined(separator: ",")
        self.lovePushLabel.textColor = (self.loveIntensionArr.count == 0) ? UIColor.lightGray : UIColor.black
        tableView.reloadData()
    }
}

//MARK:- Image Picker Handling

extension CreateProfileVC: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    /**
     Show image picker to add image to user profile
     **/
    func showImagePickerOption(_ isMore : Bool, sender: UIButton) {
        
        let alert = UIAlertController(title: "Choose Image".localized(), message: nil, preferredStyle: .actionSheet)
        alert.addAction(UIAlertAction(title: "Camera".localized(), style: .default, handler: { _ in
            self.camera()
        }))
        alert.addAction(UIAlertAction(title: "Gallery".localized(), style: .default, handler: { _ in
            self.photoLibrary()
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel".localized(), style: .cancel, handler: { action in
            
        }))
        
        
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad ){
            //Show popover for iPad Device
            if let currentPopoverpresentioncontroller = alert.popoverPresentationController{
                currentPopoverpresentioncontroller.barButtonItem = UIBarButtonItem(customView: sender)
                currentPopoverpresentioncontroller.permittedArrowDirections = UIPopoverArrowDirection.down;
                self.present(alert, animated: true, completion: nil)
            }
        }else{
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    func camera() {
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            myPickerController = UIImagePickerController()
            myPickerController.delegate = self;
            myPickerController.sourceType = .camera
            myPickerController.allowsEditing = true;
            self.present(myPickerController, animated: true, completion: nil)
        }
        
    }
    
    func photoLibrary() {
        if #available(iOS 14, *) {
            var config = PHPickerConfiguration()
            config.selectionLimit = 3
            config.filter = PHPickerFilter.images

            let pickerViewController = PHPickerViewController(configuration: config)
            pickerViewController.delegate = self
            self.present(pickerViewController, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
            
            if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
    //            myPickerController = UIImagePickerController()
                myPickerController.delegate = self;
                myPickerController.sourceType = .photoLibrary
                myPickerController.allowsEditing = true;
                self.present(myPickerController, animated: true, completion: nil)
            }
            
        }

       
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
       
      if let image = info[UIImagePickerControllerEditedImage] as? UIImage {
            
            DispatchQueue.main.async {
                
                if let imageData = image.jpeg(.lowest) {
                    self.profileImageView.image = UIImage.init(data: imageData)!
                    self.userimages.append(imageData)
                    self.usrimgcollection.reloadData()

                }else {
                    let data = UIImagePNGRepresentation(image)
                    self.profileImageView.image = image
                    self.userimages.append(data ?? Data())
                    self.usrimgcollection.reloadData()
                }
            }
        }
        
        picker.dismiss(animated: true, completion: nil)
    }
  
   
    
}

extension CreateProfileVC: UICollectionViewDelegate, UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        self.userimages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "imgaddCollectionViewCell", for: indexPath) as! imgaddCollectionViewCell
        
        cell.baseview.aroundShadow()
        let imgdata = self.userimages[indexPath.row]
        cell.imgadd.image = UIImage(data: imgdata)
        
        return cell
    }
    
    
    
    
}


extension CreateProfileVC: PHPickerViewControllerDelegate {
    @available(iOS 14, *)
    // Swift
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
      
       
       for result in results {
          result.itemProvider.loadObject(ofClass: UIImage.self, completionHandler: { (object, error) in
             if let image = object as? UIImage {
                DispatchQueue.main.async {
                   // Use UIImage
                   print("Selected image: \(image)")
                    self.profileImageView.image = image
                    
                   
                }
               
             }
           
          })
        
       }
        picker.dismiss(animated: true, completion: nil)
    }
}
