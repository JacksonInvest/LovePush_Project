//
//  Singleton.swift
//  LovePush
//
//  Created by Lavi Kumar on 24/04/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation
import CoreLocation
import UIKit

class Singleton {
    
    static var shared : Singleton = Singleton()
    
    var language : String {
        get {
            return "en"//Localize.currentLanguage()
        }
    }
    
    var floatingNavigationController : UINavigationController!
    
    var floatingButtonController = FloatingButtonController()
    
    var isCallInitiated = false
    
    var callReceiverID = 0
    
    var callReceiveForDialogID = ""
    
    var session: QBRTCSession?
    
    var quickbloxUserdata: QBUUser!
    
    var deviceToken: Data?
    
    var unreadMessageCount = 0
    
    var navigationController : UINavigationController?
    
    var notificationArr = [[String:Any]]()
    
    var currentLocation: CLLocationCoordinate2D?
    var placemark:CLPlacemark?
    var currentAddress : String = ""
    
    var findLocation: CLLocationCoordinate2D?
    var findPlacemark: CLPlacemark?
    var findAddress : String  = "Search Location"
    
    var filterGender = 0
    var minAge:CGFloat   = 18
    var maxAge:CGFloat   = 100
    
    var liveStreamingAppID = "AK7Vkq5eLYkeqU0hzi6I1Q"
    var isLiveVideoStopped = false
    
    var isMessageRead = false
    
    var isToReload = true
    var isExploreFilter = false
    var isSwipeProfileReloadable = false
    
    var planIndex = 0
    var planArr : [[String:Any]]! = [
        [
            "plan_type" : "",
            "plan_price": "Free"
        ],
        [
            "plan_type" : "Premium",
            "plan_price" : "$9.90"
        ],
        [
            "plan_type" : "V.I.P",
            "plan_price": "$9.90"
        ]
    ]
    
}
