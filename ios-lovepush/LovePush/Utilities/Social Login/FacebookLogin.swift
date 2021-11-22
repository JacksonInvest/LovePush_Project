//
//  FacebookLogin.swift
//  LovePush
//
//  Created by Lavi Kumar on 25/04/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import FBSDKCoreKit
import FBSDKLoginKit

protocol FBLoginDelegate {
    /**
       When Login with facebook we will have user profile data on facebook account
    - Parameters:
    - fbUser: Facebook user info
    - Return: Nil
    **/
    func didFBLoginFinished(_ fbUser: [String:Any])
}



class FacebookLogin {
    var  fbLoginManager : FBSDKLoginManager!
    
    var delegate : FBLoginDelegate?
    
    func startFacebook(_ target: UIViewController) {
        
        fbLoginManager = FBSDKLoginManager()
        
        fbLoginManager.logIn(withReadPermissions: ["email","public_profile"], from: target) { (result, error) -> Void in
            if (error == nil){
                let fbloginresult : FBSDKLoginManagerLoginResult = result!
                // if user cancel the login
                if (result?.isCancelled)!{
                    return
                }
                if(fbloginresult.grantedPermissions.contains("email"))
                {
                    self.getFBUserData()
                }
            }
        }
    }
    
    func getFBUserData(){
        
        
        if((FBSDKAccessToken.current()) != nil){
            FBSDKGraphRequest(graphPath: "me", parameters: ["fields": "id,name,email,first_name,last_name,gender,locale,timezone,picture,updated_time,verified"]).start(completionHandler: { (connection, result, error) -> Void in
                if (error == nil){
                    //everything works print the user data
                    print(result)
                    self.delegate?.didFBLoginFinished(result as! [String:Any])
                }else {
                    print(error?.localizedDescription)
                }
                
                self.fbLoginManager.logOut()
            })
        }
    }
}
