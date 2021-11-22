

import UIKit
import MBProgressHUD

extension Date {
    var ticks: UInt64 {
        return UInt64((self.timeIntervalSince1970 + 62_135_596_800) * 10_000_000)
    }
}

class Helper: NSObject {
    
    static let sharedInstance = Helper()
    private override init() {}
    
    func ShowAlert(message: String, viewController: UIViewController, finished: @escaping () -> Void)
    {
        let warningAlert = UIAlertController(title: "Alert".localized(), message: message.localized(), preferredStyle: UIAlertControllerStyle.alert)
        
        warningAlert.addAction(UIAlertAction(title: "OK".localized(), style: .default, handler: { (action: UIAlertAction!) in
            finished()
        }))
        
        viewController.present(warningAlert, animated: true, completion: nil)
    }
    
    func ShowLoaderOnView(view: UIView)
    {
        DispatchQueue.main.async {
            view.isUserInteractionEnabled = false;
            MBProgressHUD.showAdded(to: view, animated: true)
        }
    }
    
    func HideLoaderFromView(view: UIView)
    {
        DispatchQueue.main.async {
            view.isUserInteractionEnabled = true;
            MBProgressHUD.hide(for: view, animated: false)
        }
    }
    
    func checkNullValue(_ value: Any?) -> String {
        
        if(value is NSNull)
        {
            return ""
        }
        else if(value == nil) {
            return ""
        }
        else if(value is NSNumber) {
            
            return "number";
        }
        else if(value is Float) {
            
            return "double";
        }
        else
        {
            let string = value as! String
            if ((string == "null")) || ((string == "(null)")) || ((string == "<null>")) || ((string == "nil")) || ((string == "")) || ((string == "<nil>")) || (string.characters.count == 0) {
                
                return ""
            }
            
            return string;
        }
    }
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    func userId() -> Int {
        
        let data = UserDefaults.standard.object(forKey: "userData") as? Data
        if let aData = data {
            let getdDict = NSKeyedUnarchiver.unarchiveObject(with: aData) as! NSDictionary
            
            return getdDict.value(forKey: "id") as! Int
        }
        
        return 0
    }
    
    func token() -> String {
        
        return "Bearer \(UserDefaults.standard.string(forKey: "token")!)"
    }
}
