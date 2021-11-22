//
//  RecommendedProfilesVC.swift
//  LovePush
//
//  Created by Jatinder Singh on 17/01/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class RecommendedProfilesVC: UIViewController {

    //MARK:- Outlets
    @IBOutlet weak var collectionView : UICollectionView!
    @IBOutlet weak var noDataLbl      : UILabel!
    
    //MARK:- Variables
    var usersArray = [[String:Any]]()
    
    //MARK:- View Did Load
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.4) {
            self.getUsers()
        }
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent // .default
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK:- Button Actions
    @IBAction func BackAction(_ sender: UIButton) {
       self.gotToMenu()
    }
    
    func gotToMenu() {
        let controllers = self.navigationController!.viewControllers as Array
        
        if controllers[controllers.count - 2].isKind(of: PersonalityTestViewController.self) {
            self.backTwo()
        }else {
            self.navigationController!.popViewController(animated: true)
        }
    }
    
    func backTwo() {
        let viewControllers: [UIViewController] = self.navigationController!.viewControllers as [UIViewController]
        self.navigationController!.popToViewController(viewControllers[viewControllers.count - 3], animated: false)
    }
    
    //MARK:- API
    func getUsers() {
        
        let  param = ["user_id": User.shared.id!]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))","X-Requested-With":"XMLHttpRequest"]

        Helper.sharedInstance.ShowLoaderOnView(view: self.view)

        WebAPI.requestToPostBodyWithHeader(URLConstant.recommendProfile, param as [String : Any], header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)

            print(response)

            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {

                    if let userData = (response as! [String: Any])["data"] as? [[String:Any]] {
                        
                        self.usersArray = userData
                        
                        if(self.usersArray.count != 0) {
                            self.collectionView.isHidden = false;
                            self.noDataLbl.isHidden = true;
                        }else {
                            self.showAlertWithAction(title: "Love Push", message: "Seems like you have to come back later.", actionTitle: "Ok", completion: { (isOk) in
                                self.gotToMenu()
                            })
                            self.collectionView.isHidden = true;
                            self.noDataLbl.isHidden = false;
                        }
                        
                        self.collectionView.reloadData()
                    }else {
                        self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isOk) in
                            self.gotToMenu()
                        })
                    }
                }else {
                    self.showAlertWithAction(title: "Love Push", message: "No Data Found", actionTitle: "Ok", completion: { (isOk) in
                        self.gotToMenu()
                    })
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}



extension RecommendedProfilesVC : UICollectionViewDelegate, UICollectionViewDataSource , UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.usersArray.count;
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "listCell", for: indexPath) as! RecommendedProfilesCell
        
        if let dict = self.usersArray[indexPath.row] as? [String:Any] {
            if let imageURL = dict["profile_image"] as? String {
                cell.profileImg.setImageWithoutBaseURL(imageURL, PlaceholderImage: #imageLiteral(resourceName: "userPlaceholder"))
            }
            cell.nameLbl.text = dict["username"] as? String ?? ""
            
            
            if let userTest = dict["user_test"] as? [String:Any] {
                if let userTest = userTest["test_score"] as? [String:Any] {
                    if let score = userTest["scores"] as? String {
                        var percentage = ((Double(score)! * 100.0)) / 340.0
                        percentage = percentage.rounded(FloatingPointRoundingRule.toNearestOrEven)
                        cell.percentageLbl.text = "\(Int(percentage))%" // Rate of matching Personality to yours
                    }
                }
            }
            
        }
        return cell;
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        if SubscriptionPlan.shared.recommendedProfile == "0" {
            self.showAlert(title: "Love Push", message: "Your weekly limit is over to see Recommended User Profile.", completion: nil)
        }else {
            self.getUpdatePlanDetails(.recommended_profiles)
            let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
            vc.userID = "\((self.usersArray[indexPath.row])["id"] as! Int)"
            vc.fromPage = "Recommend"
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let width = (collectionView.frame.size.width/2)-5
        return CGSize(width: width, height: width)
    }
}

