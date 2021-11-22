//
//  SettingsVC.swift
//  LovePush
//
//  Created by Jatinder Singh on 17/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import FTPopOverMenu_Swift

class SettingsVC: UIViewController {
    
    //MARK:- Outlet
    
    @IBOutlet weak var countryLbl       : UILabel!
    @IBOutlet weak var fbSwitch         : UISwitch!
    @IBOutlet weak var menIcon          : UIImageView!
    @IBOutlet weak var womenIcon        : UIImageView!
    @IBOutlet weak var everyoneIcon     : UIImageView!
    @IBOutlet weak var settingTableView : UITableView!
    
    //MARK:- Variables
    var settingInfo: Setting!
    var settingArr = [String]()
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.getSettingsData { (response) in
            if let settingsData = response {
                DispatchQueue.global().sync {
                    self.settingInfo = Setting(settingsData)
                }
                self.settingArr = settings//Global Vairable
                self.settingTableView.reloadData()
            }else {
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
       self.getMyRemainingPlanDetails()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    //MARK:- Button Action
    @IBAction func BackAction(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func SetCountryAction(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "SRCountryPickerControllerID") as! SRCountryPickerController
        vc.countryDelegate = self;
        vc.isCountry = true;
        self.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func ChangePasswordAction(_ sender: UIButton) {
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "ChangePasswordID") as! ChangePasswordVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func MenAction(_ sender: UIButton) {
        if(self.menIcon.image != UIImage(named: "checkedOrange")) {
            self.menIcon.image = UIImage(named: "checkedOrange")
            self.womenIcon.image = nil
            self.everyoneIcon.image = nil
        }
    }
    
    @IBAction func WomenAction(_ sender: UIButton) {
        if(self.womenIcon.image != UIImage(named: "checkedOrange")) {
            self.womenIcon.image = UIImage(named: "checkedOrange")
            self.menIcon.image = nil
            self.everyoneIcon.image = nil
        }
    }
    
    @IBAction func EveryoneAction(_ sender: UIButton) {
        if(self.everyoneIcon.image != UIImage(named: "checkedOrange")) {
            self.everyoneIcon.image = UIImage(named: "checkedOrange")
            self.menIcon.image = nil
            self.womenIcon.image = nil
        }
    }
    
    
    //MARK:- Switch Changed Action
    @IBAction func switchButtonChaged(_ sender: UISwitch) {
        
        var url = ""
        switch sender.tag {
        case 1:
            url = URLConstant.setNotification
            break;
//        case 2:
//            url = URLConstant.setChatRequest
//            break;
        case 2:
            url = URLConstant.setLikeRequest
            break;
        case 3:
            url = URLConstant.hideFromSearch
            break;
        default:
            break;
        }
        
        self.enableDisableReuqest(url, status: sender.isOn, index: sender.tag) { (isSuccess) in
            
            if !isSuccess { // Updatating Failed
                if sender.isOn {
                    sender.isOn = isSuccess
                }else {
                    sender.isOn = !isSuccess
                }
            }
        }
    }
}


extension SettingsVC: CountrySelectedDelegate {
    func SRcountrySelected(countrySelected country: Country) {
        self.countryLbl.text = country.country_name
    }
}

//MARK:- Table View Delegate And Data Source
extension SettingsVC : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.settingArr.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
//        if(indexPath.row == 0) {
//
//            let cell = tableView.dequeueReusableCell(withIdentifier: "cellList2", for: indexPath) as! SettingsCellTwo
//            cell.titleLbl.lzText = "Current Language"
//
//            return cell;
//        }else
         if ((indexPath.row > 3) && (indexPath.row <= 8)){
            let cell = tableView.dequeueReusableCell(withIdentifier: "cellList1", for: indexPath) as! SettingsCellOne
            
            cell.titleLbl.text = settingArr[indexPath.row].localized()
            
            return cell;
        }else if ((indexPath.row >= 1) && (indexPath.row <= 3)) {
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "cellList3", for: indexPath) as! SettingsCellThree
            cell.index            = indexPath.row
            cell.setting          = self.settingInfo
            cell.titleLbl.text    = self.settingArr[indexPath.row].localized()
            cell.switchButton.tag = indexPath.row
//
            return cell;
            
        }else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "cellList1", for: indexPath) as! SettingsCellOne
            cell.titleLbl.text = self.settingArr[indexPath.row].localized()
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
//            let cell = tableView.cellForRow(at: indexPath) as! SettingsCellTwo
//            self.changeLangauge(cell.titleLbl)
//        }else if indexPath.row == 1 {
//            let vc = homeStoryboard.instantiateViewController(withIdentifier: "CommisionViewController") as! CommisionViewController
//            vc.titleStr = "MEMBERSHIP"
//            self.navigationController?.pushViewController(vc, animated: true)
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "SubscriptionPageViewController") as! SubscriptionPageViewController
            self.navigationController?.pushViewController(vc, animated: true)
        }else if indexPath.row == 4 {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "ChangePasswordID") as! ChangePasswordVC
            self.navigationController?.pushViewController(vc, animated: true)
        }else if indexPath.row == 5 {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "GuidlinesViewController") as! GuidlinesViewController
            self.navigationController?.pushViewController(vc, animated: true)
        }else if indexPath.row == 6 {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "LegacyViewController") as! LegacyViewController
            vc.isFromSetting = true
            self.navigationController?.pushViewController(vc, animated: true)
        }else if indexPath.row == 7 {
            let vc = mainStoryboard.instantiateViewController(withIdentifier: "Help_SupportViewController") as! Help_SupportViewController
            self.navigationController?.pushViewController(vc, animated: true)
        }else if indexPath.row == 8 {
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "DeleteAccountViewController") as! DeleteAccountViewController
            self.present(vc, animated: true, completion: nil)
        }
    }
    
    func changeLangauge(_ sender: UILabel) {
        
//        let cellConfi                   = FTConfiguration()
//        cellConfi.textColor             = UIColor.darkGray
//        cellConfi.textFont              = UIFont.systemFont(ofSize: 14)
//        cellConfi.textAlignment         = .left
//        cellConfi.backgoundTintColor    = .white
//        cellConfi.menuWidth             = 250
//        cellConfi.menuSeparatorColor    = .lightGray
//        
//        var cellConfis                  = Array(repeating: cellConfi, count: 4)
//        
//        let cellConfi1                  = FTConfiguration()
//        cellConfi1.textColor            = UIColor.darkGray
//        cellConfis[1]                   = cellConfi1
//        cellConfi1.backgoundTintColor   = .white
//        
//        FTPopOverMenu.showForSender(sender: sender, with: ["English", "German"], done: { (selectedIndex) in
//            print(selectedIndex)
//            switch selectedIndex {
//            case 0:
//                    if Localize.currentLanguage() == "de" {
//                        Localize.setCurrentLanguage("en")
//                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
//                        self.navigationController?.setViewControllers([vc], animated: false)
//                    }
//                
//                    sender.text = "English"
//                    break;
//            case 1:
//                if Localize.currentLanguage() == "en" {
//                    Localize.setCurrentLanguage("de")
//                    let vc = homeStoryboard.instantiateViewController(withIdentifier: "mainTabBarID")
//                    self.navigationController?.setViewControllers([vc], animated: false)
//                }
//                sender.text = "German"
//                    break;
//            default:
//                print("Default")
//            }
//        }) {
//            print("cancel")
//        }
    }
}


class SettingsCellOne: UITableViewCell {
    
    @IBOutlet weak var titleLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}

class SettingsCellTwo: UITableViewCell {
    
    @IBOutlet weak var titleLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
}



