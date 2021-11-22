//
//  Broadcast.swift
//  LovePush
//
//  Created by Lavi Kumar on 12/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

struct Broadcast {
    var id            : String?
    var author        : String?
    var height        : Int?
    var ingestChannel : String?
    var preview       : String?
    var length        : Int?
    var resourceUri   : String?
    var tags          : [String]?
    var title         : String?
    var type          : String?
    var createdAt     : Double?
    
    init(_ data: [String:Any]) {
        self.id              = data["id"] as? String ?? ""
        self.author          = data["author"] as? String ?? ""
        self.tags            = data["tags"] as? [String] ?? []
        self.height          = data["author"] as? Int ?? 0
        self.ingestChannel   = data["ingestChannel"] as? String ?? ""
        self.preview         = data["preview"] as? String ?? ""
        self.resourceUri     = data["resourceUri"] as? String ?? ""
        self.title           = data["title"] as? String ?? ""
        self.type            = data["type"] as? String ?? ""
        self.length          = data["length"] as? Int ?? 0
        self.createdAt       = data["created"] as? Double ?? 0.0
    }
}
