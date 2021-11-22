

import Foundation
import UIKit
import LocationPicker
//import CommonCryptoModule

extension String {
    
    public func isStringEmpty() -> Bool
    {
        let testingString = self
        if testingString.characters.count == 0 || checkIsEmpty()
        {
            return true
        }
        return false
    }
    
    func checkIsEmpty()-> Bool{
        let trimmedString = self.trimmingCharacters(in:  CharacterSet.whitespaces)
        if trimmedString.characters.count == 0 {
            return true
        }else{
            return false
        }
    }
    
    func removeSpaces()-> String{
        let trimmedString = self.trimmingCharacters(in:  CharacterSet.whitespaces)
        if trimmedString.characters.count == 0 {
            return ""
        }else{
            return trimmedString
        }
    }
    
    func isValidEmail() -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: self)
    }
    
    func isValidPassword()-> Bool{
        
        if self.count < 6 || self.count > 20 {
            return false
        }else{
            return true
        }
    }
}

//MARK: -
extension Date {
    /// Returns the amount of years from another date
    func years(from date: Date) -> Int {
        return Calendar.current.dateComponents([.year], from: date, to: self).year ?? 0
    }
    /// Returns the amount of months from another date
    func months(from date: Date) -> Int {
        return Calendar.current.dateComponents([.month], from: date, to: self).month ?? 0
    }
    /// Returns the amount of weeks from another date
    func weeks(from date: Date) -> Int {
        return Calendar.current.dateComponents([.weekOfMonth], from: date, to: self).weekOfMonth ?? 0
    }
    /// Returns the amount of days from another date
    func days(from date: Date) -> Int {
        return Calendar.current.dateComponents([.day], from: date, to: self).day ?? 0
    }
    /// Returns the amount of hours from another date
    func hours(from date: Date) -> Int {
        return Calendar.current.dateComponents([.hour], from: date, to: self).hour ?? 0
    }
    /// Returns the amount of minutes from another date
    func minutes(from date: Date) -> Int {
        return Calendar.current.dateComponents([.minute], from: date, to: self).minute ?? 0
    }
    /// Returns the amount of seconds from another date
    func seconds(from date: Date) -> Int {
        return Calendar.current.dateComponents([.second], from: date, to: self).second ?? 0
    }
    /// Returns the a custom time interval description from another date
    func offset(from date: Date) -> String {
        if years(from: date)   > 0 {
            return ("\(years(from: date)) " + "years ago".localized()).replacingOccurrences(of: "1 years ago", with: "1 year ago")
        }
        if months(from: date)  > 0 {
            return ("\(months(from: date)) " + "months ago".localized()).replacingOccurrences(of: "1 months ago", with: "1 month ago")
        }
        if weeks(from: date)   > 0 {
            return ("\(weeks(from: date)) " + "weeks ago".localized()).replacingOccurrences(of: "1 weeks ago", with: "1 week ago")
        }
        if days(from: date)    > 0 {
            return ("\(days(from: date)) " + "days ago".localized()).replacingOccurrences(of: "1 days ago", with: "1 day ago")
        }
        if hours(from: date)   > 0 {
            return ("\(hours(from: date)) " + "hours ago".localized()).replacingOccurrences(of: "1 hours ago", with: "1 hour ago")
        }
        if minutes(from: date) > 0 {
            return ("\(minutes(from: date)) " + "minutes ago".localized()).replacingOccurrences(of: "1 minutes ago", with: "1 minute ago")
        }
        if seconds(from: date) > 0 {
            return ("\(seconds(from: date)) " + "seconds ago".localized()).replacingOccurrences(of: "1 seconds ago", with: "1 second ago")
        }
        else {return "now"}
    }
}


extension UIView{
    
    func aroundShadow(shadowColor color: UIColor = UIColor.darkGray, shadowRadius radius: CGFloat = 3) {
        self.layer.shadowColor = color.cgColor
        self.layer.shadowOpacity = 0.3
        self.layer.shadowRadius = radius
        self.layer.masksToBounds = false
        self.layer.shadowOffset = CGSize(width: 0.3, height: 0.3)
        self.backgroundColor = .white
    }
    
}


extension UIView {
    func showAnimations(_ completion: ((Bool) -> Swift.Void)? = nil) {
        UIView.animate(withDuration: 0.3, delay: 0.0, options: UIViewAnimationOptions.transitionCrossDissolve, animations: {
            self.layoutIfNeeded()
            self.layoutSubviews()
        }, completion: completion)
    }
    
    func addBottomShadow() {
        
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 6.0)
        self.layer.shadowOpacity = 0.1
        self.layer.shadowRadius = 0.0
        self.layer.masksToBounds = false
    }
    
    func topViewShadow() {
        
        let shadowSize1 : CGFloat = 1.5
        let shadowPath1 = UIBezierPath(rect: CGRect(x: 3.0,
                                                    y: 2.0,
                                                    width: self.frame.size.width + shadowSize1,
                                                    height: self.frame.size.height + shadowSize1))
        self.layer.masksToBounds = false
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 0.0)
        self.layer.shadowOpacity = 0.3
        self.layer.shadowPath = shadowPath1.cgPath
    }
    
    func shadowAllSides() {
        
        let shadowSize2 : CGFloat = 3.5
        let shadowPath2 = UIBezierPath(rect: CGRect(x: -shadowSize2 / 2,
                                                    y: -shadowSize2 / 2,
                                                    width: self.frame.size.width + shadowSize2,
                                                    height: self.frame.size.height + shadowSize2))
        self.layer.masksToBounds = false
        self.layer.shadowColor = UIColor.lightGray.cgColor
        self.layer.shadowOffset = CGSize(width: 1, height: 1)
        self.layer.shadowOpacity = 0.5
        self.layer.shadowPath = shadowPath2.cgPath
    }
    
    func setRadialProgressView(_ show: Bool, progress: CGFloat) {
        let tag = 80840402
        if show {
//            self.isEnabled = false
//            self.alpha = 0.5
            if let radialView = self.viewWithTag(tag) as? RadialProgressView {
                radialView.ringProgress = progress * 100
            }else {
                let radialView = RadialProgressView()
                let buttonHeight = self.bounds.size.height
                let buttonWidth = self.bounds.size.width
                radialView.center = CGPoint(x: 60, y: 60)
                radialView.tag = tag
                radialView.tintColor = UIColor.red
                radialView.ringProgress = progress * 100
                radialView.createChart()
                self.addSubview(radialView)
            }
        } else {
//            self.isEnabled = true
//            self.alpha = 1.0
            if let radialView = self.viewWithTag(tag) as? UIActivityIndicatorView {
                radialView.removeFromSuperview()
            }
        }
    }
}

extension UIButton {
    func addBottomShadow1() {
        
        self.layer.shadowColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.25).cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 6.0)
        self.layer.shadowOpacity = 0.3
        self.layer.shadowRadius = 0.0
        self.layer.masksToBounds = false
    }
    
    func loadingIndicator(_ show: Bool) {
        let tag = 808404
        if show {
            self.isEnabled = false
            self.alpha = 0.5
            let indicator = UIActivityIndicatorView()
            let buttonHeight = self.bounds.size.height
            let buttonWidth = self.bounds.size.width
            indicator.center = CGPoint(x: buttonWidth/2, y: buttonHeight/2)
            indicator.tag = tag
            indicator.tintColor = UIColor.red
            indicator.color = UIColor.red
            self.addSubview(indicator)
            indicator.startAnimating()
        } else {
            self.isEnabled = true
            self.alpha = 1.0
            if let indicator = self.viewWithTag(tag) as? UIActivityIndicatorView {
                indicator.stopAnimating()
                indicator.removeFromSuperview()
            }
        }
    }
}

extension UIImage {
    public func imageRotatedByDegrees(degrees: CGFloat) -> UIImage {
        //Calculate the size of the rotated view's containing box for our drawing space
        let rotatedViewBox: UIView = UIView(frame: CGRect(x: 0, y: 0, width: self.size.width, height: self.size.height))
        let t: CGAffineTransform = CGAffineTransform(rotationAngle: degrees * CGFloat.pi / 180)
        rotatedViewBox.transform = t
        let rotatedSize: CGSize = rotatedViewBox.frame.size
        //Create the bitmap context
        UIGraphicsBeginImageContext(rotatedSize)
        let bitmap: CGContext = UIGraphicsGetCurrentContext()!
        //Move the origin to the middle of the image so we will rotate and scale around the center.
        bitmap.translateBy(x: rotatedSize.width / 2, y: rotatedSize.height / 2)
        //Rotate the image context
        bitmap.rotate(by: (degrees * CGFloat.pi / 180))
        //Now, draw the rotated/scaled image into the context
        bitmap.scaleBy(x: 1.0, y: -1.0)
        bitmap.draw(self.cgImage!, in: CGRect(x: -self.size.width / 2, y: -self.size.height / 2, width: self.size.width, height: self.size.height))
        let newImage: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return newImage
    }
   
    
    public func fixedOrientation() -> UIImage {
        if imageOrientation == UIImageOrientation.up {
            return self
        }
        
        var transform: CGAffineTransform = CGAffineTransform.identity
        
        switch imageOrientation {
        case UIImageOrientation.down, UIImageOrientation.downMirrored:
            transform = transform.translatedBy(x: size.width, y: size.height)
            transform = transform.rotated(by: CGFloat.pi)
            break
        case UIImageOrientation.left, UIImageOrientation.leftMirrored:
            transform = transform.translatedBy(x: size.width, y: 0)
            transform = transform.rotated(by: CGFloat.pi/2)
            break
        case UIImageOrientation.right, UIImageOrientation.rightMirrored:
            transform = transform.translatedBy(x: 0, y: size.height)
            transform = transform.rotated(by: -CGFloat.pi/2)
            break
        case UIImageOrientation.up, UIImageOrientation.upMirrored:
            break
        }
        
        switch imageOrientation {
        case UIImageOrientation.upMirrored, UIImageOrientation.downMirrored:
            transform.translatedBy(x: size.width, y: 0)
            transform.scaledBy(x: -1, y: 1)
            break
        case UIImageOrientation.leftMirrored, UIImageOrientation.rightMirrored:
            transform.translatedBy(x: size.height, y: 0)
            transform.scaledBy(x: -1, y: 1)
        case UIImageOrientation.up, UIImageOrientation.down, UIImageOrientation.left, UIImageOrientation.right:
            break
        }
        
        let ctx: CGContext = CGContext(data: nil,
                                       width: Int(size.width),
                                       height: Int(size.height),
                                       bitsPerComponent: self.cgImage!.bitsPerComponent,
                                       bytesPerRow: 0,
                                       space: self.cgImage!.colorSpace!,
                                       bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue)!
        
        ctx.concatenate(transform)
        
        switch imageOrientation {
        case UIImageOrientation.left, UIImageOrientation.leftMirrored, UIImageOrientation.right, UIImageOrientation.rightMirrored:
            ctx.draw(self.cgImage!, in: CGRect(x: 0, y: 0, width: size.height, height: size.width))
        default:
            ctx.draw(self.cgImage!, in: CGRect(x: 0, y: 0, width: size.width, height: size.height))
            break
        }
        
        let cgImage: CGImage = ctx.makeImage()!
        
        return UIImage(cgImage: cgImage)
    }
}

class ShadowView: UIView {
    override var bounds: CGRect {
        didSet {
            setupShadow()
        }
    }
    
    private func setupShadow() {
        self.layer.cornerRadius = 8
        self.layer.shadowOffset = CGSize(width: 0, height: 3)
        self.layer.shadowRadius = 3
        self.layer.shadowOpacity = 0.3
        self.layer.shadowPath = UIBezierPath(roundedRect: self.bounds, byRoundingCorners: .allCorners, cornerRadii: CGSize(width: 8, height: 8)).cgPath
        self.layer.shouldRasterize = true
        self.layer.rasterizationScale = UIScreen.main.scale
    }
}

extension NSMutableAttributedString {
    @discardableResult func bold(_ text:String) -> NSMutableAttributedString {
        
        let attrs : [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font : UIFont.boldSystemFont(ofSize: 15),
            NSAttributedString.Key.foregroundColor : UIColor.black
        ]
        let boldString = NSMutableAttributedString(string: text, attributes: attrs)
        self.append(boldString)
        return self
    }
    
    @discardableResult func normal(_ text:String)->NSMutableAttributedString {
        let attrs : [NSAttributedString.Key : Any] = [
            NSAttributedString.Key.font : UIFont.systemFont(ofSize: 15),
            NSAttributedString.Key.foregroundColor : UIColor.darkGray
        ]
        let normal =  NSAttributedString(string: text,  attributes:attrs)
        self.append(normal)
        return self
    }
}


//#MARK: - UIViewController

extension UIViewController {
    
    func setStatusBarStyle(_ style: UIStatusBarStyle) {
        if let statusBar = UIApplication.shared.value(forKey: "statusBar") as? UIView {
            statusBar.backgroundColor = style == .lightContent ? UIColor.black : .white
            statusBar.setValue(style == .lightContent ? UIColor.white : .black, forKey: "foregroundColor")
        }
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func getBase64File(_ filePath: URL)-> String {
        do {
            let fileData = try Data.init(contentsOf: filePath)
            let base64File = fileData.base64EncodedString(options: NSData.Base64EncodingOptions.init(rawValue: 0))
            return base64File
        } catch {
            return ""
        }
    }
    
    func showAlert(title: String, message: String, completion: ((_ isOkAction: Bool)-> Void)?) {
        let alert = UIAlertController(title: title.localized(), message: message.localized(), preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "Ok".localized(), style: .default, handler: { (okAction) in
            //            self.logout()
            if completion != nil {
                completion!(true)
            }
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    func showAlertWithAction(title: String, message: String,actionTitle: String, completion: @escaping(_ isOkAction: Bool)-> Void) {
        let alert = UIAlertController(title: title.localized(), message: message.localized(), preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: actionTitle.localized(), style: .default, handler: { (okAction) in
            //            self.logout()
               completion(true)
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    func showAlertWith(yesBtnTitle: String, noBtnTitle: String, title: String, message: String, completion: @escaping(_ isTrue: Bool)-> Void) {
        
        let alert = UIAlertController(title: title.localized(), message: message.localized(), preferredStyle: UIAlertController.Style.alert)
        
        alert.addAction(UIAlertAction(title: yesBtnTitle.localized(), style: .default, handler: { (action: UIAlertAction!) in
         
            completion(true)
        }))
        
        alert.addAction(UIAlertAction(title: noBtnTitle.localized(), style: .cancel, handler: { (action: UIAlertAction!) in
            
            completion(false)
        }))
        
        present(alert, animated: true, completion: nil)
    }
    
    /**
     Create Payload to send push notification alert when send request,
     Notification handle from quickblox Push Notification methods
     - parameter message: Message text to display in notification alert
     - parameter requestType: Type of request like Connect, Love, Chat etc
     - parameter qbID: User Quickblox ID
     - parameter eventID: Request id will genrate with request sent
     **/
    func createAlertPayload(_ message : String, requestType: String, qbID: String, eventID: Int) {
        
        let payload = [
            "message"    : message,
            "ios_alert"  : "1",
            UsersConstant.alert: "1",
            "user_qb_id" : "\(User.shared.qBUserID!)",
            "user_id"    : "\(User.shared.id!)",
            "user_name"  : User.shared.name,
            "event_type" : requestType,
            "event_id"   : "\(eventID)",
            "user_image" : User.shared.profileURL!,
            "notify_type": "request"
            ] 
        let data = try? JSONSerialization.data(withJSONObject: payload,
                                               options: .prettyPrinted)
        var message = ""
        if let data = data {
            message = String(data: data, encoding: .utf8) ?? ""
        }
        let event = QBMEvent()
        event.notificationType = QBMNotificationType.push
        event.usersIDs = qbID
        event.type = QBMEventType.oneShot
        event.message = message
        QBRequest.createEvent(event, successBlock: { response, events in
            print("Send push - Success")
        }, errorBlock: { response in
            print("Send push - Error")
        })
    }
    
    func hmsFrom(duration: Int, completion: @escaping (_ hours: Int, _ minutes: Int, _ seconds: Int)->()) {
        let finalDuration = duration/60
        completion(finalDuration / 3600, (finalDuration % 3600) / 60, (finalDuration % 3600) % 60)
    }
    func getStringFrom(_ duration: Int) -> String {
        
        return duration < 10 ? "0\(duration)" : "\(duration)"
    }
    
    
    
    /**
     Request to Delete or restore chat with user
     - parameter user_id: User ID
     - parameter math_id: Match id with user
     - parameter status: 1 for delete, 0 for restore or restart chat with user
     **/
    func deleteRestoreChatWithUser(_ user_id: String, match_id: String, status: String,completion: @escaping(_ isSuccess: Bool)-> Void) {
        
        let param = [
            "user_id" : user_id,
            "match_id": match_id,
            "status"  : status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.deleteChat, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    //                    self.getMatchedUser()
                    completion(true)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(false)
                }
            }else {
                completion(false)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Request to block or unblock user
     - parameter user_id: User ID
     - parameter status: 1 for block, 0 for unblock
     **/
    
    func blockUnblockUser(_ user_id: String,status: String,completion: @escaping(_ isSuccess: Bool)-> Void) {
        
        let param = [
            "user_id"   : User.shared.id!,
            "blocked_user_id" : user_id,
            "status"  : status
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.blockUser, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    //                    self.getMatchedUser()
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(true)
                }else {
                    
                    let message = (response as! [String:Any])["blocked"] as? String ?? (response as! [String:Any])["message"] as! String
                    
                    self.showAlert(title: "Love Push", message: message, completion: nil)
                    completion(false)
                }
            }else {
                completion(false)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Get other user data on bases of user_info1 or user_info2
     - parameter data: Match Data in myMatches List
     - returns: Other User Data
     **/
    func getOtherUserData(from data: [String:Any])-> [String:Any]? {
        var finalData = [String:Any]()
        DispatchQueue.global().sync {
            if let userInfo1 = data["user_info1"] as? [String:Any], let userInfo2 = data["user_info2"] as? [String:Any] {
                if let userID1 = userInfo1["id"] as? Int {
                    if "\(userID1)" == User.shared.id! {
                        finalData = userInfo2
                    }else {
                        finalData = userInfo1
                    }
                }
            }
        }
        return finalData
    }
    
    /**
     Request to delete dialog from local server
     - parameter dialogID: Local dialog to delete from local server
     - returns: Completion Handler with succes response
     **/
    func deleteDialogFromLocalDB(_ dialogIDs: String, completion: @escaping(_ isSuccess : Bool)-> Void) {
        let param = ["ids": dialogIDs] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.deleteDialog, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    completion(true)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(false)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                completion(false)
            }
        }
    }
    
    /**
     Request to save new quickblox dialog to local server
     - parameter id: New Created Quickblox Dialog id+
     - parameter matchID: Match ID with current user in list
     - returns: Completion Handler with succes local dialog created
     **/
    func saveDialogWith(_ id: String, matchID: Int, completion: @escaping (_ dialogData: [String:Any]?)-> Void) {
        
        let param = ["match_id" : matchID, "qb_dialog_id": id] as [String : Any]
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.saveDialog, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    if let outerData = (response as! [String:Any])["data"] as? [String:Any] {
                        completion(outerData)
                    }
                }else {
                    //                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(nil)
                }
            }else {
                //                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                completion(nil)
            }
        }
    }
    
    /**
     Get date format to execute sorting of newest matches
     - parameter dateStr: default date format
     - returns: New date Format
     **/
    func getDateFormate(_ dateStr: String)-> Date {
        
        let dateFormatter        = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone   = TimeZone(identifier: "UTC")
        dateFormatter.locale     = Locale.current
        let date = dateFormatter.date(from: dateStr)
        
        return date!
    }
    
    
    /**
     Request to like or unlike post added by my matched users
     - parameter postID: ID of post added by other user
     - parameter status: Status 1 for like and 0 for unlike the post
     **/
    func likeUnlike(_ postID: Int, status: Int, completion: @escaping (_ isSuccess: Bool,_ data: [String:Any]?)-> Void) {
        
        let param = ["post_id" : postID, "status": status, "user_id": User.shared.id!] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        print(param)
        //        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.likeUnlikePost, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    self.getMyRemainingPlanDetails()
                    completion(true, (response as! [String:Any])["data"] as! [String:Any])
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(false, nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
                completion(false, nil)
            }
        }
    }
    
    /**
     Request to Report photo for vulgor or abusive content
     - parameter user_id: User ID
     - parameter math_id: Match id with user
     - parameter status: 1 for delete, 0 for restore or restart chat with user
     **/
    func reportVulgorPhoto(_ imageID: Int, imageType: String,completion: @escaping(_ isSuccess: Bool)-> Void) {
        
        let param = [
            "image_id" : imageID,
            "type": imageType,
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.reportImage, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 1 {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(true)
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                    completion(false)
                }
            }else {
                completion(false)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Pick Location from Google map to Filter users post or ads
     - parameter completion: Return selected location data
     **/
    func pickLocation(completion: @escaping ((Location?) -> ())) {
        let locationPicker = LocationPickerViewController()
        
        // you can optionally set initial location
        
        if Singleton.shared.placemark != nil {
            let location = CLLocation(latitude: Singleton.shared.currentLocation!.latitude, longitude: Singleton.shared.currentLocation!.longitude)
            let initialLocation = Location(name: Singleton.shared.currentAddress, location: location, placemark: Singleton.shared.placemark!)
            locationPicker.location = initialLocation
        }else if Singleton.shared.findPlacemark != nil {
            let location = CLLocation(latitude: Singleton.shared.findLocation!.latitude, longitude: Singleton.shared.findLocation!.longitude)
            let initialLocation = Location(name: Singleton.shared.findAddress, location: location, placemark: Singleton.shared.findPlacemark!)
            locationPicker.location = initialLocation
        }
        
        // button placed on right bottom corner
        locationPicker.showCurrentLocationButton = true // default: true
        
        // default: navigation bar's `barTintColor` or `.whiteColor()`
        locationPicker.currentLocationButtonBackground = .blue
        
        // ignored if initial location is given, shows that location instead
        locationPicker.showCurrentLocationInitially = true // default: true
        
        locationPicker.mapType = .standard // default: .Hybrid
        
        // for searching, see `MKLocalSearchRequest`'s `region` property
        locationPicker.useCurrentLocationAsHint = true // default: false
        
        locationPicker.searchBarPlaceholder = "Search places".localized() // default: "Search or enter an address"
        
        locationPicker.searchHistoryLabel = "Previously searched".localized() // default: "Search History"
        
        locationPicker.selectButtonTitle = "Select".localized()
        
        // optional region distance to be used for creation region when user selects place from search results
        locationPicker.resultRegionDistance = 500
        
        locationPicker.navigationController?.isNavigationBarHidden = false
        locationPicker.navigationController?.setNavigationBarHidden(false, animated: false)
        
        locationPicker.completion = { location in
            // do some awesome stuff with location
            locationPicker.navigationController?.navigationBar.isHidden = true
            completion(location)
        }
        
        self.navigationController?.pushViewController(locationPicker, animated: true)
    }
    
    func getMyRemainingPlanDetails() {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.myPlanDetails, header: header as [String : AnyObject]) { (response, isSuccess) in
//            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
           print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                      let planData =  SubscriptionPlan(data)
                    }
                }else {
                    
                }
            }else {
                
            }
        }
    }
    
    func getUpdatePlanDetails(_ planKey: PlanKey) {
        
        let param = ["plan_key": "\(planKey)"]
        
        print(param)
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPOSTWithHeader(URLConstant.updatePlanData, params: param as [String : AnyObject], headers: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    if let data = (response as! [String:Any])["data"] as? [String:Any] {
                        let planData =  SubscriptionPlan(data)
                    }
                }else {
                    
                }
            }else {
                
            }
        }
    }
}

enum PlanKey {
    case messages
    case see_love_requests
    case recommended_profiles
}




extension UIDatePicker {
    func set18YearValidation() {
        let currentDate: Date = Date()
        var calendar: Calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        calendar.timeZone = TimeZone(identifier: "UTC")!
        var components: DateComponents = DateComponents()
        components.calendar = calendar
        components.year = -18
        let maxDate: Date = calendar.date(byAdding: components, to: currentDate)!
        components.year = -80
        let minDate: Date = calendar.date(byAdding: components, to: currentDate)!
        self.minimumDate = minDate
        self.maximumDate = maxDate
    } }

extension Date{
     var daysInMonth:Int{
        let calendar = Calendar.current
        
        let dateComponents = DateComponents(year: calendar.component(.year, from: self), month: calendar.component(.month, from: self))
        let date = calendar.date(from: dateComponents)!
        
        let range = calendar.range(of: .day, in: .month, for: date)!
        let numDays = range.count
        
        return numDays
    }
}
