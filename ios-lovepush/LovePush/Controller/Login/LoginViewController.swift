//
//  ViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 15/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import GoogleSignIn

class LoginViewController: UIViewController {
    
    //MARK:- Outlet Variables
    @IBOutlet weak var userNameTxt : UITextFieldCustomClass!
    @IBOutlet weak var passwordTxt : UITextFieldCustomClass!
    @IBOutlet weak var signUpButton: UIButton!
    
    //MARK:- Variable
    var completeWithSocialView: CompleteSocialView!
    var loginType = 0
    var instagramLogin: InstagramViewController!
    
    //MARK:- Controller LifeCycle Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setupAttributedString()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        UIApplication.shared.statusBarStyle = .default
    }
    
    //Set Attributed string with sign up button
    func setupAttributedString() {
        
        let firstAttributes: [NSAttributedString.Key: Any] = [.foregroundColor: UIColor.lightGray]
        let secondAttributes = [NSAttributedString.Key.foregroundColor: UIColor(red: 34/255, green: 122/255, blue: 207/255, alpha: 1.0)]
        
        let firstString = NSMutableAttributedString(string: "Don't have a account? ".localized(), attributes: firstAttributes)
        let secondString = NSAttributedString(string: "Sign up".localized(), attributes: secondAttributes)
        
        firstString.append(secondString)
        
        self.signUpButton.setAttributedTitle(firstString, for: .normal)
    }
    
    // #MARK:- Button Actions
    @IBAction func SigninAction(_ sender: UIButton) {
        
        if (self.userNameTxt.text!.checkIsEmpty()) {//Check User Name Empty or not
            Helper.sharedInstance.ShowAlert(message: "Please enter your Username.", viewController: self, finished: {})
        }else if(passwordTxt.text!.checkIsEmpty()) {// Check Password Empty or not
            Helper.sharedInstance.ShowAlert(message: "Please enter your Password.", viewController: self, finished: {})
        }else {
           self.view.endEditing(true)
           self.requestToLogin()
        }
    }
    
    @IBAction func ForgotAction(_ sender: UIButton) {
        if (userNameTxt.text!.checkIsEmpty()) {// For forgot password needs to enter email or user name, then a mail sent to that email to reset password
            Helper.sharedInstance.ShowAlert(message: "Please enter your Username.", viewController: self, finished: {})
        }else {
            self.requestForgotPassword()
        }
    }
    
    @IBAction func SignupAction(_ sender: UIButton) {// For new user needs to register in app
        let vc = mainStoryboard.instantiateViewController(withIdentifier: "RegisterID") as! RegisterVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    //Login with facebook
    @IBAction func facebookButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        let facebookLogin = FacebookLogin()
        facebookLogin.delegate = self
        facebookLogin.startFacebook(self)
    }
    
    //Login with instagram
    @IBAction func instagramButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        self.loginWithInstagram()
    }
    
    //Login with goole account
    @IBAction func googleButtonTapped(_ sender: UIButton) {
        self.view.endEditing(true)
        GIDSignIn.sharedInstance().delegate=self
        GIDSignIn.sharedInstance().uiDelegate=self
        GIDSignIn.sharedInstance()?.signOut()
        GIDSignIn.sharedInstance().signIn()
    }
}

//MARK:- Facebook Delegate
extension LoginViewController : FBLoginDelegate {
    //Get user info from facebook Account
    func didFBLoginFinished(_ fbUser: [String : Any]) {
        self.loginType = 2
        self.loginWithSocialMedial(fbUser)
    }
}

//MARK:- Google Login
extension LoginViewController : GIDSignInUIDelegate,GIDSignInDelegate {
    //Get user info from Google Account
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!,
              withError error: Error!) {
        if let error = error {
            print("\(error.localizedDescription)")
        } else {
            // Perform any operations on signed in user here.
            let userId = user.userID
            let idToken = user.authentication.idToken
            let fullName = user.profile.name
            let givenName = user.profile.givenName
            let familyName = user.profile.familyName
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
extension LoginViewController : InstagramLoginViewControllerDelegate {
    
    func loginWithInstagram() {
        
        // 2. Initialize your 'InstagramLoginViewController' and set your 'ViewController' to delegate it
        self.instagramLogin = self.storyboard?.instantiateViewController(withIdentifier: "InstagramViewController") as? InstagramViewController
        self.instagramLogin.delegate = self
        
  
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
    
    //Get user info from instagram Account
    func instagramLoginDidFinish(accessToken: String?, error: String?) {
        
        // Whatever you want to do ...
        
        // And don't forget to dismiss the 'InstagramLoginViewController'x
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
    
        func didInstagramLoginFinished(_ instagramUser: [String : Any]) {
            self.loginType = 4

            let userId = instagramUser["id"] as! String
            let fullName = instagramUser["full_name"] as! String
            var dict = ["name": fullName, "id": userId]
            
            self.loginWithInstagram(dict as [String : Any])
        }
}




