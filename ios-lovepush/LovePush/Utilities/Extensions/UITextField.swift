

import Foundation
import UIKit


class dropDownTextField: UITextField {
    
    let padding = UIEdgeInsets(top: 0, left: 7, bottom: 0, right: 20);
    
    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return UIEdgeInsetsInsetRect(bounds, padding)
    }
    
    override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
        return UIEdgeInsetsInsetRect(bounds, padding)
    }
    
    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return UIEdgeInsetsInsetRect(bounds, padding)
    }
}


extension UITextField {
    
    var isEmpty: Bool {
        if self.text == nil || self.text == "" || self.text!.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
            return true
        }
        return false
    }
    
    func setPlaceholder(color: UIColor) {
        let attributedString = NSAttributedString(string: self.placeholder!, attributes:[NSAttributedStringKey.foregroundColor: color])
        self.attributedPlaceholder = attributedString
    }
    
    var isEmailValid: Bool {
        let stricterFilterString = "\\A[a-z0-9]+([-._][a-z0-9]+)*@([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,4}\\z"
        let laxString = "^(?=.{1,64}@.{4,64}$)(?=.{6,100}$).*"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", stricterFilterString)
        let emailTest2 = NSPredicate(format:"SELF MATCHES %@", laxString)
        return emailTest.evaluate(with: self.text!) && emailTest2.evaluate(with: self.text!)
        
    }
    
    var isPasswordValid:Bool{
        let stricterFilterString = "^(?=.*\\d)[A-Za-z\\d$@$!%*?&]{8,}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", stricterFilterString)
        return emailTest.evaluate(with: self.text!)
    }
    
    var isValidPhoneNo: Bool{
        let phoneRegex = "^((\\+)|(00))[0-9]{6,14}$"
        let phoneNoTest = NSPredicate(format:"SELF MATCHES %@", phoneRegex)
        return phoneNoTest.evaluate(with: self.text!)
    }
    
//    //MARK: Name validation method
//    func validateNameTextField() -> Bool {
//        let set = NSCharacterSet(charactersIn: ACCEPTABLE_CHARACTERS)
//        let inverted = set.inverted
//        let filtered = self.text!.components(separatedBy:inverted).joined(separator: "")
//        return filtered == self.text!
//    }
}

