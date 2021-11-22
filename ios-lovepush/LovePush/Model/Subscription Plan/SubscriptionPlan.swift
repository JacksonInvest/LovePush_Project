//
//  SubscriptionPlan.swift
//  LovePush
//
//  Created by lavi on 17/01/20.
//  Copyright © 2020 Jatinder Singh. All rights reserved.
//

import Foundation

struct SubscriptionPlan {
    
    static var shared: SubscriptionPlan!
    
    private var planData = [String:Any]()
    
    init(_ data: [String:Any]) {
        self.planData = data
        type(of: self).shared = self
    }
    
    var postAds : Int {
        get {
            return self.planData["ads"] as? Int ?? 0
        }
    }
    
    var sendingRequest : String {
        get {
            
            if let requestCount = self.planData["sending_requests"] as? Int {
               return "\(requestCount)"
            }
            return self.planData["sending_requests"] as? String ?? "0"
        }
    }
    
    var seeLoveRequest : String {
        get {
            
            if let requestCount = self.planData["see_love_requests"] as? Int {
                return "\(requestCount)"
            }
            return self.planData["see_love_requests"] as? String ?? "0"
        }
    }
    
    var recommendedProfile : String {
        get {
            
            if let requestCount = self.planData["recommended_profiles"] as? Int {
                return "\(requestCount)"
            }
            return self.planData["recommended_profiles"] as? String ?? "0"
        }
    }
    
    var newUserMessage : String {
        get {
            
            if let requestCount = self.planData["messages"] as? Int {
                return "\(requestCount)"
            }
            return self.planData["messages"] as? String ?? "0"
        }
    }
    
    var planType : String {
        get {
            
            return self.planData["type"] as? String ?? "FREE"
        }
    }
    
    var planID : Int {
        get {
            
            return self.planData["id"] as? Int ?? 0
        }
    }
}
