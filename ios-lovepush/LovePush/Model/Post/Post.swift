//
//  Post.swift
//  LovePush
//
//  Created by Lavi Kumar on 01/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import CoreLocation

struct Post {
    
    init(_ postData: [String:Any]?) {
        self.postData = postData
    }
    
    var postData : [String:Any]?
    
    var id : Int? {
        get {
            return self.postData!["id"] as? Int ?? 0
        }
    }
    
    var url : String? {
        get {
            return self.postData!["image"] as? String ?? nil
        }
    }
    
    var date : String? {
        get {
            return self.postData!["updated_at"] as? String ?? ""
        }
    }
    
    var title : String? {
        get {
            return self.postData!["title"] as? String ?? ""
        }
    }
    
    var isLiked : Bool? {
        get {
            return self.postData!["isLiked"] as? Bool ?? false
        }
    }
    
    var type : Int? {
        get {
            return self.postData!["post_type"] as? Int ?? 0
        }
    }
    
    
    var anonymouslStatus : Int? {
        get {
            return self.postData!["is_post_anonymously"] as? Int ?? 0
        }
    }
    
    var description : String? {
        get {
            return self.postData!["description"] as? String ?? ""
        }
    }
    
   var radius: Float? {
        get {
            let radiusStr = self.postData!["radius"] as? String ?? "0.0"
            return Float(radiusStr)!
        }
    }
    
    var address : String? {
        get {
            return self.postData!["location"] as? String ?? ""
        }
    }
    
    var likesArr : [[String:Any]]? {
        get {
            return self.postData!["post_like"] as? [[String:Any]] ?? []
        }
    }
    
   private var latitude: Double? {
        get {
            let latitudeStr = self.postData!["latitude"] as? String ?? "0.0"
            return Double(latitudeStr)!
        }
    }
    
   private var longitude: Double? {
        get {
            let longitudeStr = self.postData!["longitude"] as? String ?? "0.0"
            return Double(longitudeStr)!
        }
    }
    
    var coordinate : CLLocationCoordinate2D? {
        get {
            return CLLocationCoordinate2D(latitude: self.latitude!, longitude: self.longitude!)
        }
    }
    
    var matchData : [String:Any]? {
        get {
            return self.postData!["matchQbID"] as? [String:Any] ?? nil
        }
    }
    
    private var userInfo : [String:Any]? {
        get {
            return self.postData!["user_info"] as? [String:Any] ?? nil
        }
    }
    
    var user : UserInfo? {
        get {
            if self.userInfo != nil {
                return UserInfo(self.userInfo!)
            }
            return nil
        }
    }
}
