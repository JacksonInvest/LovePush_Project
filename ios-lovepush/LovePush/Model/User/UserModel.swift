
//
//  File.swift
//  LevelUp
//
//  Created by Lavi Kumar on 6/21/18.
//  Copyright © 2018 Lavi Kumar. All rights reserved.
//

import Foundation

//var user = User()

class User {
    
    static var shared: User!
    
    var data : [String:Any]!
    
    init(_ dataDetail : [String:Any]?) {
        data = dataDetail ?? [:]
        type(of: self).shared = self
    }
    
    var name: String! {
        get {
            return self.data["name"] as? String ?? ""
        }
    }
    
    var qBUserID : String! {
        get {
            return "\(self.data["quickblox_id"]!)"
        }
    }
    
    //We use education Key for Favorit Destination, so don't confuse, use it as favorit desitnation
    var education: String! {
        get {
            return self.data["education"] as? String ?? ""
        }
    }
    
    var referralCode : String! {
        get {
            return self.data["myReferalCode"] as? String ?? ""
        }
    }
    
    var country: String! {
        get {
            return self.data["country"] as? String ?? ""
        }
    }
    
    var currentPlan: String! {
        get {
            if let planDetails = self.data["plan_details"] as? [String:Any] {
                if let plan = planDetails["plan"] as? [String:Any] {
                    return plan["type"] as? String ?? "FREE"
                }else {
                    return "FREE"
                }
            }else {
                return "FREE"
            }
        }
    }
    
   var isProfileComplete: Int! {
        get {
            return self.data["is_profile_complete"] as? Int ?? 0
        }
    }
    
    var email: String! {
        get {
            return self.data["email"] as? String ?? ""
        }
    }
    
    var partnerPreference: Int! {
        get {
            return self.data["partner_preference"] as? Int ?? 0
        }
    }
    
    var profileURL: String! {
        get {
            return self.data["profile_image"] as? String ?? ""
        }
    }
    
    var address: String! {
        get {
            return UserDefaults.standard.value(forKey: "address") as? String ?? ""
        }
    }
    var username: String! {
        get {
            return self.data["username"] as? String ?? ""
        }
    }
    var phoneCode: String! {
        get {
            return self.data["phone_code"] as? String ?? ""
        }
    }
    var id: String! {
        get {
            if let id = self.data["id"] as? Int {
                return "\(id)"
            }else if let id = self.data["id"] as? String {
                return id
            }else {
                return "0"
            }
        }
    }
    
    var phoneNo: String! {
        get {
            return self.data["phone"] as? String ?? ""
        }
    }
    
    var eyecolor: String! {
        get {
            return self.data["eyecolor"] as? String ?? ""
        }
    }
    
    var aboutme: String! {
        get {
            return self.data["aboutme"] as? String ?? ""
        }
    }
    
    var employer: String! {
        get {
            return self.data["employer"] as? String ?? ""
        }
    }
    
    var sexualOrientation: Int! {
        get {
            return self.data["sexual_orientation"] as? Int ?? 0
        }
    }
    
    var verificationCode: String! {
        get {
            return self.data["verification_code"] as? String ?? ""
        }
    }
    var isRestrictFBFriends: Int! {
        get {
            return self.data["is_restrict_fb_friends"] as? Int ?? 0
        }
    }
    
    var emailVerifiedAt: String! {
        get {
            return self.data["email_verified_at"] as? String ?? ""
        }
    }
    
    var haircolor: String! {
        get {
            return self.data["haircolor"] as? String ?? ""
        }
    }
    
    var relationship: String! {
        get {
            return self.data["relationship"] as? String ?? ""
        }
    }
    
    var language: String! {
        get {
            return self.data["language"] as? String ?? ""
        }
    }
    
    var locale: String! {
        get {
            return self.data["locale"] as? String ?? ""
        }
    }
    
    var city: String! {
        get {
            return self.data["city"] as? String ?? ""
        }
    }
    
    var lovePushIntention: String! {
        get {
            return self.data["love_push_intention"] as? String ?? ""
        }
    }
    
    var hobbies: String! {
        get {
            return self.data["hobbies"] as? String ?? ""
        }
    }
    
    var gender: Int! {
        get {
            return self.data["gender"] as? Int ?? 0
        }
    }
    
    var loginType: Int! {
        get {
            return self.data["login_type"] as? Int ?? 0
        }
    }
    
    var size: Int! {
        get {
            return self.data["size"] as? Int ?? 0
        }
    }
    
    var socialId: Int! {
        get {
            return self.data["social_id"] as? Int ?? 0
        }
    }
    
    var isPhoneVerify: Int! {
        get {
            return self.data["is_phone_verify"] as? Int ?? 0
        }
    }
    
    var token: String!{
        get {
            return UserDefaults.standard.value(forKey: "token") as? String ?? ""
        }
    }
    
    var isEnableChat: Bool! {
        get {
            return UserDefaults.standard.value(forKey: "isEnableChat") as? Bool ?? false
        }
        
        set {
            UserDefaults.standard.set(newValue, forKey: "isEnableChat")
        }
    }
}

