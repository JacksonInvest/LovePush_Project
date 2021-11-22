//
//  UserInfo.swift
//  LovePush
//
//  Created by Lavi Kumar on 10/06/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation


struct UserInfo {
    var name         : String?
    var imageURL     : String?
    var id           : Int?
    var qbID         : String?
    var eventType    : String?
    var chatStatus   : Int?
    var email        : String?
    var userName     : String?
    var referralBy   : String?
    var referralCode : String? //My Referral Code
    var matchInfo    : [String:Any]?

    
    
    var data = [String:Any]()
    var isNotificationEnable: Bool! {
        get {
            if let notificationStatus = self.data["receive_notification"] as? String {
                if notificationStatus == "0" {
                    return false
                }else {
                    return true
                }
            }else {
                return true
            }
        }
    }
    
    init(_ data: [String:Any]) {
        self.data           = data
        self.imageURL       = data["profile_image"] as? String ?? ""
        self.name           = data["name"] as? String ?? ""
        self.email          = data["email"] as? String ?? ""
        self.qbID           = data["quickblox_id"] as? String ?? "0"
        self.id             = data["id"] as? Int ?? 0
        self.eventType      = data["event_type"] as? String ?? ""
        self.chatStatus     = data["chat_status"] as? Int ?? 1
        self.userName       = data["username"] as? String ?? ""
        self.referralBy     = data["referalBy"] as? String ?? ""
        self.referralCode   = data["myReferalCode"] as? String ?? ""
//        self.isDeleted      = data["isDeleted"] as? String ?? "0"
//        self.deletedBy      = data["deletedBy"] as? String ?? "0"
        
        if let matchInfo1 = data["match_info1"] as? [[String:Any]] {
            if matchInfo1.count > 0 {
                self.eventType = matchInfo1.first!["event_type"] as? String ?? ""
                self.matchInfo = matchInfo1.first
            }
        }
        if let matchInfo2 = data["match_info2"] as? [[String:Any]] {
            if matchInfo2.count > 0 {
                self.eventType = matchInfo2.first!["event_type"] as? String ?? ""
                self.matchInfo = matchInfo2.first
            }
        }
        
        if let matchDetails = data["matchDetails"] as? [String:Any] {
                self.eventType = matchDetails["event_type"] as? String ?? ""
                self.matchInfo = matchDetails
        }
    }
    
    var isDeleted : Int? {
        get {
            return self.data["isDeleted"] as? Int ?? 0
        }
    }
    var deletedBy : Int? {
        get {
                return self.data["deletedBy"] as? Int ?? 0
            }
    }
    
    var isBlocked : Bool {
        get {
            return (self.data["isBlocked"] as? Bool ?? false) || (self.blockedBy == self.id)
        }
    }
    
    var blockedBy : Int {
        get {
            return self.data["blockedBy"] as? Int ?? 0
        }
    }
}
