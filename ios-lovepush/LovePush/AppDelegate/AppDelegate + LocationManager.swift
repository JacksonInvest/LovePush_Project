//
//  AppDelegate + LocationManager.swift
//  LovePush
//
//  Created by Lavi Kumar on 17/07/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import Foundation

extension AppDelegate: CLLocationManagerDelegate {
    func locationManagerSetup() {
        
        // Ask for Authorisation from the User.
        locationManager.requestAlwaysAuthorization()
        //
        //        // For use in foreground
        locationManager.requestWhenInUseAuthorization()
        
        locationManager.requestAlwaysAuthorization()
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
            locationManager.startUpdatingLocation()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let locValue: CLLocationCoordinate2D = manager.location?.coordinate else { return }
//        print("locations = \(locValue.latitude) \(locValue.longitude)")
        if Singleton.shared.currentAddress == "" && locValue != nil {
            Singleton.shared.currentLocation = locValue
            self.getAddress(coordinate: Singleton.shared.currentLocation!) { (address) in
                //            print(address)
                if User.shared != nil && Singleton.shared.currentLocation != nil {
                    self.updateLocation(address)
                }
            }
        }
    }
    
    /**
     Get selected loaction element from location coordinates
     - parameter coordinate: Selected Location Lat long coordinates
     - parameter completion: Final Address from Selected location coordinate
     **/
    func getAddress(coordinate: CLLocationCoordinate2D,completion: @escaping(_ address: String)-> Void) {
        let geoCoder = CLGeocoder()
        let location = CLLocation(latitude: coordinate.latitude, longitude: coordinate.longitude)
        var finalAddress = ""
        geoCoder.reverseGeocodeLocation(location, completionHandler: { placemarks, error in
            guard let addressDict = placemarks?[0].addressDictionary else {
                return
            }
            
            // Print each key-value pair in a new row
            addressDict.forEach { print($0) }
            
            Singleton.shared.placemark = placemarks?[0]
            
            // Print fully formatted address
            if let formattedAddress = addressDict["FormattedAddressLines"] as? [String] {
//                print(formattedAddress.joined(separator: ", "))
                finalAddress = formattedAddress.joined(separator: ", ")
                
                completion(finalAddress)
            }
            
            if finalAddress == "" {
                var addressArr = [String]()
                // Access each element manually
                if let locationName = addressDict["Name"] as? String {
                    //                print(locationName)
                    addressArr.append(locationName)
                }
                if let street = addressDict["Thoroughfare"] as? String {
                    //                print(street)
                    addressArr.append(street)
                }
                if let city = addressDict["City"] as? String {
                    //                print(city)
                    addressArr.append(city)
                }
                if let zip = addressDict["ZIP"] as? String {
                    //                print(zip)
                    addressArr.append(zip)
                }
                if let country = addressDict["Country"] as? String {
                    //                print(country)
                    addressArr.append(country)
                }
                
                finalAddress = addressArr.joined(separator: ",")
                
                completion(finalAddress)
            }
        })
    }
    
    /**
     Request to update current location to share with friends
     - parameter address: Address Got from current location manager coordinates
     **/
    func updateLocation(_ address: String) {
        
        let param = [
            "user_id" : User.shared.id!,
            "latitude" : Singleton.shared.currentLocation!.latitude,
            "longitude": Singleton.shared.currentLocation!.longitude,
            "address" : address
            ] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        print(param)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.updateLocation, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    Singleton.shared.currentAddress = address
                }else {
//                     self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
//                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
