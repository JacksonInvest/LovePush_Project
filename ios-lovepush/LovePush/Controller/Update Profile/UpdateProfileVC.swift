//
//  UpdateProfileVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 25/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class UpdateProfileVC: UIViewController {
    
    //MARK:- Outlet
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var userNameTextField: UITextField!
    @IBOutlet weak var nameTxt: UITextField!
    @IBOutlet weak var extraImagesView: UIView!
    @IBOutlet weak var extraImageViewHeight: NSLayoutConstraint!
    @IBOutlet weak var ageTextField: UITextField!
    @IBOutlet weak var titleTxt: UITextField!
    @IBOutlet weak var relationshipTxt: UITextField!
    @IBOutlet weak var languagesTxt: UITextField!
    @IBOutlet weak var sizeTxt: UITextField!
    @IBOutlet weak var physiqueTextField: UITextField!
    @IBOutlet weak var educationTxt: UITextField!
    @IBOutlet weak var jobTxt: UITextField!
    @IBOutlet weak var hobbiesTxt: UITextField!
    @IBOutlet weak var heterosexualBtn: UIButton!
    @IBOutlet weak var bisexualBtn: UIButton!
    @IBOutlet weak var homosexualBtn: UIButton!
    @IBOutlet weak var aboutMeTxt: UITextView!
    @IBOutlet weak var lovePushLabel: UILabel!
    @IBOutlet weak var maleBtn: UIButton!
    @IBOutlet weak var femaleBtn: UIButton!
    @IBOutlet weak var transgenderButton: UIButton!
    @IBOutlet weak var religionTextField: UITextField!
    @IBOutlet weak var imageCollectionView: UICollectionView!
    @IBOutlet weak var collectionViewHeight: NSLayoutConstraint!
    @IBOutlet weak var selectionTableTopSpace: NSLayoutConstraint!
    @IBOutlet weak var lovePushIntentionTableView: UITableView!
    
    //MARK:- Variables
    var selectedPlaceCoordinates = CLLocationCoordinate2D()
    var detailDict = NSMutableDictionary()
    var cityName = "city"
    var countryName = "country"
    var myPickerController = UIImagePickerController()
    var imagesArr = [[String:Any]]()
    var imageDataArr = [UIImage]()
    var newAddedImages = [UIImage]()
    var isPickProfileImage = false
    var isPickToReplaceFirstImage = false
    
    var pickerView : PickerView!
    let lovaPushIntentions = ["Relationship","Friendship","Daily Activities", "No Expectation"]
    let relationshipStatus = ["Single","In Relationship","Married","Recently Separated"]
    let physiques          = ["Plump","Thin","Athletic","Muscular","Normal"]//["Electro","Reggae","Rock","HipHop","Pop","Classic","All mixed up"]
    let ageList            = ["18","19","20","21","22","23","24",
                             "25","26","27","28","29","30","31","32","33","34","35","36"
        ,"37","38","39","40","42","43","44","45","46","47","48","49","50"]
    var lovePushID = 0
    var relationshipID = 0
    var physiqueID = 0
    var selectType = ""
    var loveIntensionArr = [String]()
    var selectdMusicArr = [String]()
    var activeIndex = -1
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        print(detailDict)
        
        self.pickerView = PickerView()
        self.pickerView.delegate = self
        
        UIApplication.shared.statusBarStyle = .lightContent
        
        self.setUpData()
        self.addShadowToTableView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    //Setup user profile info
    func setUpData() {
        
        if(Helper.sharedInstance.checkNullValue(self.detailDict.value(forKey: "profile_image")) != "") {
            self.profileImg.sd_addActivityIndicator()
            self.profileImg.sd_setImage(with: URL.init(string: "\(self.detailDict.value(forKey: "profile_image") as! String)"), placeholderImage: #imageLiteral(resourceName: "userPlaceholder"), options: .refreshCached, completed: nil)
        }
        else {
            self.profileImg.image = #imageLiteral(resourceName: "userPlaceholder")
        }
        
        self.userNameTextField.text = self.detailDict.value(forKey: "username") as? String
        self.nameTxt.text = self.detailDict.value(forKey: "name") as? String
        
        self.languagesTxt.text = self.detailDict.value(forKey: "language") as? String
        self.sizeTxt.text = self.detailDict.value(forKey: "size") as? String
        
        self.educationTxt.text = self.detailDict.value(forKey: "education") as? String// Fav Dectination
        self.jobTxt.text = self.detailDict.value(forKey: "job") as? String
        
        self.hobbiesTxt.text = self.detailDict.value(forKey: "hobbies") as? String
        self.religionTextField.text = self.detailDict.value(forKey: "religion") as? String ?? ""
        
        self.aboutMeTxt.text = self.detailDict.value(forKey: "aboutme") as? String ?? "About me".localized()
        self.aboutMeTxt.textColor = self.aboutMeTxt.text == "About me".localized() ? UIColor.lightGray : UIColor.black
        self.aboutMeTxt.delegate = self
        
        //Set user gender info for Gender type code
        if(self.detailDict.value(forKey: "gender") as! Int == Gender.Male.rawValue) {
            self.maleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
        }else if(self.detailDict.value(forKey: "gender") as! Int == Gender.Female.rawValue) {
            self.femaleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
        }else if(self.detailDict.value(forKey: "gender") as! Int == Gender.Trans.rawValue) {
            self.transgenderButton.setImage(UIImage(named: "radioOn"), for: .normal)
        }
        
        //Set sexual orientation details for type code
        if let sexualOrientation = self.detailDict.value(forKey: "sexual_orientation") as? Int {
            if(sexualOrientation == SexualOrientation.Hetero.rawValue) {
                self.heterosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            }
            else if(sexualOrientation == SexualOrientation.Bi.rawValue) {
                self.bisexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            }else {
                self.homosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            }
        }
        
        if let age = self.detailDict.value(forKey: "age") as? Int {
            self.ageTextField.text = "\(age)"
        }
        
        //Set User Love push intension details added by user
        if let lovePushIntestion = self.detailDict.value(forKey: "love_push_intention") as? String {
            self.loveIntensionArr = lovePushIntestion.components(separatedBy: ",")
            let localizedArr = self.loveIntensionArr.map({$0.localized()})
            self.lovePushLabel.text = (localizedArr.joined(separator: ",")).replacingOccurrences(of: " ", with: "")
            
            if (self.lovePushLabel.text?.isEmpty)! {//Set as placeholder in intension not set
                self.lovePushLabel.text = "Select LovePush Intention".localized()
                self.lovePushLabel.textColor = UIColor.lightGray
            }else {
                self.lovePushLabel.textColor = UIColor.black
            }
        }
        
        //Set Realtionship status of user
        if let relationID = self.detailDict.value(forKey: "relationship") as? Int {
            self.relationshipID = relationID
            self.relationshipTxt.text = (relationID == 0) ? "Select Relationship".localized() : self.relationshipStatus[relationID - 1].localized()
        }
        
        //Set Phsyque
        if let physique = self.detailDict.value(forKey: "physique") as? Int {
            self.physiqueID = physique
            self.physiqueTextField.text = (physique == 0) ? "Physique".localized() : self.physiques[physique - 1].localized()
        }
        
        //Set Bio of user described by user
        if(self.aboutMeTxt.text == "") {
            aboutMeTxt.text = "About me".localized()
            aboutMeTxt.textColor = UIColor.lightGray
        }
        
        self.imageDataSetup()
    }
    
    /**
     Set images array list in collection view added by user
    **/
    func imageDataSetup() {
        
        self.newAddedImages.removeAll()
        self.imageDataArr.removeAll()
        self.imagesArr = self.detailDict.value(forKey: "profile_images") as! [[String:Any]]
        
        if self.imagesArr.count > 0 {
            for newmageData in self.imagesArr {
                if let image = self.getImageFromURL(newmageData["image"] as! String) {
                    self.imageDataArr.append(image)
                }
            }
            if self.imageDataArr.count > 0 {
                self.collectionViewHeight.constant = 80
                self.extraImageViewHeight.constant = 110
                self.imageCollectionView.reloadData()
            }else {
                self.collectionViewHeight.constant = 0
                self.extraImageViewHeight.constant = 30
            }
        }else {
            self.collectionViewHeight.constant = 0
            self.extraImageViewHeight.constant = 30
        }
        self.manageImageCollection()
    }
    
    func getLovePushIdsIntArr(_ strArr: [String])-> [String] {
        let intArray:[String] = strArr.map { String($0) }
        return intArray
    }
    
    func getImageFromURL(_ urlStr: String)-> UIImage? {
        
        let imageUrl = URL(string: urlStr)!
        
        let imageData = try! Data(contentsOf: imageUrl)
        
        let image = UIImage(data: imageData)
        
        return image
    }
    
    //MARK:- Button Actions
    
    @IBAction func addImageButtonTapped(_ sender: UIButton) {
        self.isPickProfileImage = false
        self.isPickToReplaceFirstImage = false
        self.showImagePickerOption(true, sender: sender)
    }
    @IBAction func BackAction(_ sender: UIButton) {
        if(self.imageDataArr.count == 0) {
            Helper.sharedInstance.ShowAlert(message: "Profile image should not be empty.", viewController: self, finished: {})
        }else {
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func selectAgeButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Age"
        self.pickerView.removeFromSuperView()
        self.pickerView.addpickerView(self.ageList, self)
    }
    @IBAction func selectLovePuchButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Love Intention"
//        self.selectionTableTopSpace.constant = 0
        self.lovePushIntentionTableView.reloadData()
        self.lovePushIntentionTableView.isHidden = !self.lovePushIntentionTableView.isHidden
    }
    
    @IBAction func selectReletionshipButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Relation Status"
        self.pickerView.removeFromSuperView()
        self.pickerView.addpickerView(self.relationshipStatus, self)
    }
    
    @IBAction func selectPhysiqueButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.selectType = "Physique"
//        self.selectionTableTopSpace.constant = 400
//        self.lovePushIntentionTableView.reloadData()
//        self.lovePushIntentionTableView.isHidden = !self.lovePushIntentionTableView.isHidden
        self.pickerView.removeFromSuperView()
        self.pickerView.addpickerView(self.physiques, self)
    }
    
    @IBAction func MaleAction(_ sender: UIButton) {
            self.maleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.femaleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOff"), for: .normal)
    }
    
    @IBAction func FemaleAction(_ sender: UIButton) {
            self.femaleBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.maleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOff"), for: .normal)
    }
    
    @IBAction func transgenderButtonTapped(_ sender: UIButton) {
            self.femaleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.maleBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.transgenderButton.setImage(UIImage(named: "radioOn"), for: .normal)
    }
    
    @IBAction func HeterosexualAction(_ sender: UIButton) {
        
        if(sender.currentImage == UIImage(named: "radioOff")) {
            self.heterosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.bisexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.homosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        }
    }
    
    @IBAction func BisexualAction(_ sender: UIButton) {
        
        if(sender.currentImage == UIImage(named: "radioOff")) {
            self.bisexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.heterosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.homosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        }
    }
    
    @IBAction func HomosexualAction(_ sender: UIButton) {
        
        if(sender.currentImage == UIImage(named: "radioOff")) {
            self.homosexualBtn.setImage(UIImage(named: "radioOn"), for: .normal)
            self.bisexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
            self.heterosexualBtn.setImage(UIImage(named: "radioOff"), for: .normal)
        }
    }
    
    @IBAction func lovePushDoneButtonTapped(_ sender: UIButton) {
        self.lovePushIntentionTableView.isHidden = true
    }
    
    @IBAction func SubmitAction(_ sender: UIButton) {
        
        if(self.imageDataArr.count == 0) {//Images List is empy in collection view please add an image to set in profile
            Helper.sharedInstance.ShowAlert(message: "Profile image should not be empty.", viewController: self, finished: {})
        }else if(self.userNameTextField.text!.checkIsEmpty()) {
            Helper.sharedInstance.ShowAlert(message: "Please enter your Username.", viewController: self, finished: {})
        }else if(self.nameTxt.text!.checkIsEmpty()) {
            
            Helper.sharedInstance.ShowAlert(message: "Please enter name.", viewController: self, finished: {})
        }else if(self.ageTextField.text!.checkIsEmpty()) {
            
            Helper.sharedInstance.ShowAlert(message: "Please set age.", viewController: self, finished: {})
        }else {
            
            self.view.endEditing(true)
            self.updateProfile()
        }
    }
    
    @objc func handleTap(sender: UITapGestureRecognizer? = nil) {
        self.isPickProfileImage = true
//        self.showImagePickerOption(false)
    }
    
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
        
//        if !isMore {// Main Profile image updating
//            alert.addAction(UIAlertAction(title: "Remove Image", style: .default, handler: { action in
//
//                self.profileImg.image = #imageLiteral(resourceName: "userPlaceholder")
//            }))
//        }
//
        alert.addAction(UIAlertAction(title: "Cancel".localized(), style: .cancel, handler: { action in
            
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
    
//        self.present(alert, animated: true, completion: nil)
        
    }
    
    //MARK:- Functions
    func addShadowToTableView() {
        self.lovePushIntentionTableView.layer.shadowColor = UIColor.black.cgColor
        self.lovePushIntentionTableView.layer.shadowOpacity = 5
        self.lovePushIntentionTableView.layer.shadowOffset = CGSize(width: 1, height: 1)
        self.lovePushIntentionTableView.layer.shadowRadius = 10
        self.lovePushIntentionTableView.layer.borderWidth = 1
        self.lovePushIntentionTableView.layer.borderColor = UIColor.lightGray.cgColor
    }
    
    func fetchCityAndCountry(from location: CLLocation, completion: @escaping (_ city: String?, _ country:  String?, _ error: Error?) -> ()) {
        CLGeocoder().reverseGeocodeLocation(location) { placemarks, error in
            completion(placemarks?.first?.locality,
                       placemarks?.first?.country,
                       error)
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
        
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            myPickerController = UIImagePickerController()
            myPickerController.delegate = self;
            myPickerController.sourceType = .photoLibrary
            myPickerController.allowsEditing = true;
            self.present(myPickerController, animated: true, completion: nil)
        }
    }
}

//MARK:- Picker View Delegate
extension UpdateProfileVC : PickerViewProtocol {
    func didPickerSelected(_ itemIndex: Int) {
        if self.selectType == "Love Intention" {
//            self.lovePushID = itemIndex
//            self.titleTxt.text = self.lovaPushIntentions[itemIndex]
        }else if self.selectType == "Relation Status" {
            self.relationshipID = itemIndex + 1
            self.relationshipTxt.text = self.relationshipStatus[itemIndex].localized()
        }else if self.selectType == "Age" {
            self.ageTextField.text = self.ageList[itemIndex]
        }else if self.selectType == "Physique" {
            self.physiqueID = itemIndex + 1
            self.physiqueTextField.text = self.physiques[itemIndex].localized()
        }
    }
}

extension UpdateProfileVC : UITextViewDelegate {
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if textView.text == "About me".localized() {
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
    
}

extension UpdateProfileVC: UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        if let image = info[UIImagePickerControllerEditedImage] as? UIImage {
            
            DispatchQueue.main.async {
                
                    if let imageData = image.jpeg(.lowest) {
                        self.imageDataArr.append(UIImage.init(data: imageData)!)
                        self.newAddedImages.append(UIImage.init(data: imageData)!)
                    }else {
                        self.imageDataArr.append(image)
                        self.newAddedImages.append(image)
                    }
                
                
                if self.imageDataArr.count == 1 {//First Image
                    Helper.sharedInstance.ShowLoaderOnView(view: self.view)
                }
                
                if self.newAddedImages.count > 0 {
//                    print(self.activeIndex)
//                    print(self.isPickToReplaceFirstImage)
                    if self.activeIndex == 0 && self.isPickToReplaceFirstImage{
                        self.removeImage(0)
                    }else {
                        self.activeIndex = self.imageDataArr.count - 1
                        self.addImages()
                    }
                }
            }
        }
        
        picker.dismiss(animated: true, completion: nil)
    }
}

extension UpdateProfileVC: UICollectionViewDelegate, UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.imageDataArr.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "UpdateProfileCollectionViewCell", for: indexPath) as! UpdateProfileCollectionViewCell
        
        cell.imageView.image = self.imageDataArr[indexPath.row]
        
        cell.imageView.layer.borderColor = (indexPath.row == 0) ? UIColor.red.cgColor : UIColor.clear.cgColor
        cell.imageView.layer.borderWidth = (indexPath.row == 0) ? 2 : 0
        
        cell.deleteButton.tag = indexPath.row
        cell.deleteButton.addTarget(self, action: #selector(deleteImageButtonTapped), for: .touchUpInside)
        
        if indexPath.row == 0 {
            self.profileImg.image = cell.imageView.image
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        return CGSize(width: 81, height: 80)
    }
    
    @objc func deleteImageButtonTapped(_ sender: UIButton) {
        
        if (self.imageDataArr.count == 1) {
            self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "Profile image should not be empty.", message: "Do you want to update profile image with new image?") { (isYes) in
                if isYes {
                    self.activeIndex = sender.tag
                    print(sender.tag)
                    self.isPickToReplaceFirstImage = true
                    self.isPickProfileImage = false
                    self.showImagePickerOption(true, sender: sender)
                }
            }
//            Helper.sharedInstance.ShowAlert(message: "Profile image should not be empty.", viewController: self, finished: {})
        }else  {
            self.activeIndex = sender.tag
            if sender.tag >= self.imagesArr.count {
                let newImage = self.imageDataArr[sender.tag]
                
                let index  = self.imageDataArr.lastIndex(of: newImage)
                self.newAddedImages.remove(at: index!)
                
                self.imageDataArr.remove(at: sender.tag)
                self.imageCollectionView.reloadData()
                self.manageImageCollection()
            }else {
                self.removeImage(sender.tag)
            }
        }
    }
    
    //Manage images collection view height
    func manageImageCollection() {
        self.extraImagesView.isHidden      = false
        self.extraImageViewHeight.constant = self.imageDataArr.count > 0 ? 110 : 30
        self.collectionViewHeight.constant = self.imageDataArr.count > 0 ? 80 : 0
        
//        if self.activeIndex == 0 {
            if self.imageDataArr.count == 0 {
//                self.profileImg.image = self.imageDataArr.first
////                self.updateProfile()
//            }else {
                self.profileImg.image = #imageLiteral(resourceName: "userPlaceholder")
//                self.updateProfile()
            }
//        }
    }
}

//MARK:- Delegate
extension UpdateProfileVC : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return  lovaPushIntentions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LoveIntentionUpdateProfileCell", for: indexPath) as! LoveIntentionTableViewCell
        
//        if (self.selectType == "Love Intention") {
            cell.uPLovePushTitleLabel.text = self.lovaPushIntentions[indexPath.row].localized()
            if self.loveIntensionArr.lastIndex(of: self.lovaPushIntentions[indexPath.row]) != nil {
                cell.uPCheckIcon.image = #imageLiteral(resourceName: "checked_blue")
            }else {
                cell.uPCheckIcon.image = #imageLiteral(resourceName: "uncheck")
            }
//        }else {
//            cell.uPLovePushTitleLabel.text = self.physiques[indexPath.row].localized()
//            if self.selectdMusicArr.lastIndex(of: self.physiques[indexPath.row]) != nil {
//                cell.uPCheckIcon.image = #imageLiteral(resourceName: "checked_blue")
//            }else {
//                cell.uPCheckIcon.image = #imageLiteral(resourceName: "uncheck")
//            }
//        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
//        if (self.selectType == "Love Intention") {
            if let index = self.loveIntensionArr.lastIndex(of: self.lovaPushIntentions[indexPath.row]) {
                self.loveIntensionArr.remove(at: index)
            }else {
                self.loveIntensionArr.append(self.lovaPushIntentions[indexPath.row])
            }
            let selectedArr         = self.loveIntensionArr.map({$0.localized()}) // Convert original Arr to localized to display on label
            self.lovePushLabel.text = (selectedArr.count == 0) ? "Select LovePush Intention".localized(): selectedArr.joined(separator: ",")
            self.lovePushLabel.textColor = (self.loveIntensionArr.count == 0) ? UIColor.lightGray : UIColor.black
//        }else {
//            if let index = self.selectdMusicArr.lastIndex(of: self.physiques[indexPath.row]) {
//                self.selectdMusicArr.remove(at: index)
//            }else {
//                self.selectdMusicArr.append(self.physiques[indexPath.row])
//            }
//            let selectedArr         = self.selectdMusicArr.map({$0.localized()}) // Convert original Arr to localized to display on label
//            self.physiqueTextField.text = (selectedArr.count == 0) ? "Music Style".localized(): selectedArr.joined(separator: ",")
//            self.physiqueTextField.textColor = (self.selectdMusicArr.count == 0) ? UIColor.lightGray : UIColor.black
//        }
        tableView.reloadData()
    }
    
}
