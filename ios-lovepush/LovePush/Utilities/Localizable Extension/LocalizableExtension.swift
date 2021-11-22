//
//  LocalizableExtension.swift
//  Camel
//
//  Created by CallSoft on 21/02/19.
//  Copyright © 2019 mobulous. All rights reserved.
//

import Foundation

extension UIButton {
    @IBInspectable var title: String? {
        get {
            self.setTitle(self.title?.localized(), for: .normal)
            return self.currentTitle
        }
        set {
            self.setTitle(newValue?.localized(), for: .normal)
        }
    }
    
    @IBInspectable var imageAr: UIImage? {
        get {
            if Localize.currentLanguage() == "ar" {
                if self.currentImage == #imageLiteral(resourceName: "back") {
                    self.setImage(#imageLiteral(resourceName: "back_ar"), for: .normal)
                }else if self.currentImage == #imageLiteral(resourceName: "send") {
                    self.setImage(#imageLiteral(resourceName: "send_ar"), for: .normal)
                }
            }else if Localize.currentLanguage() == "en" {
                if self.currentImage == #imageLiteral(resourceName: "back_ar") {
                    self.setImage(#imageLiteral(resourceName: "back"), for: .normal)
                }else if self.currentImage == #imageLiteral(resourceName: "send_ar") {
                    self.setImage(#imageLiteral(resourceName: "send"), for: .normal)
                }
            }
            return self.currentImage
        }
        set {
            if Localize.currentLanguage() == "ar" {
                if self.currentImage == #imageLiteral(resourceName: "back") {
                    self.setImage(#imageLiteral(resourceName: "back_ar"), for: .normal)
                }else if self.currentImage == #imageLiteral(resourceName: "send") {
                    self.setImage(#imageLiteral(resourceName: "send_ar"), for: .normal)
                }
            }else if Localize.currentLanguage() == "en" {
                if self.currentImage == #imageLiteral(resourceName: "back_ar") {
                    self.setImage(#imageLiteral(resourceName: "back"), for: .normal)
                }else if self.currentImage == #imageLiteral(resourceName: "send_ar") {
                    self.setImage(#imageLiteral(resourceName: "send"), for: .normal)
                }
            }
        }
        
    }
}

extension UITabBarItem {
    @IBInspectable var lztitle: String? {
        get {
            self.title = self.lztitle?.localized()
            return self.title
        }
        set {
            self.title = newValue?.localized()
        }
    }
}

extension UIImageView {
    @IBInspectable var lzImage : UIImage? {
        get {
            if Localize.currentLanguage() == "ar" {
                if self.image == #imageLiteral(resourceName: "forword-arrow") {
                    self.image = #imageLiteral(resourceName: "forword-ar")
                }
            }else if Localize.currentLanguage() == "en" {
                if self.image == #imageLiteral(resourceName: "forword-ar") {
                    self.image = #imageLiteral(resourceName: "forword-arrow")
                }
            }
            return self.image
        }
        set {
            if Localize.currentLanguage() == "ar" {
                 if self.image == #imageLiteral(resourceName: "forword-arrow") {
                    self.image = #imageLiteral(resourceName: "forword-ar")
                 }
            }else if Localize.currentLanguage() == "en" {
                if self.image == #imageLiteral(resourceName: "forword-ar") {
                    self.image = #imageLiteral(resourceName: "forword-arrow")
                }
            }
        }
    }
}

extension UISearchBar {
    @IBInspectable var lzPlaceholder: String? {
        get {
            self.placeholder = self.lzPlaceholder?.localized()
            return self.placeholder
        }
        set {
            self.placeholder = newValue?.localized()
        }
    }
}

extension UITextView : UITextViewDelegate {
    @IBInspectable var isLangEn: Bool {
        get {
            if Localize.currentLanguage() == "en"{
                self.textAlignment = NSTextAlignment.left
                self.semanticContentAttribute = UISemanticContentAttribute.forceLeftToRight
            }else{
                self.textAlignment = NSTextAlignment.right
                self.semanticContentAttribute = UISemanticContentAttribute.forceRightToLeft
            }
            
            return self.isLangEn
        }
        set {
            if Localize.currentLanguage() == "en"{
                self.textAlignment = NSTextAlignment.left
                self.semanticContentAttribute = UISemanticContentAttribute.forceLeftToRight
            }else{
                self.textAlignment = NSTextAlignment.right
                self.semanticContentAttribute = UISemanticContentAttribute.forceRightToLeft
            }
        }
    }
    
    @IBInspectable var lzText: String? {
        get {
            self.text = self.lzText?.localized()
            return self.text
        }
        set {
            self.text = newValue?.localized()
        }
    }
}



extension UILabel {
    @IBInspectable var lzText: String? {
        get {
            self.text = self.lzText?.localized()
            return self.text
        }
        set {
            self.text = newValue?.localized()
        }
    }
}

extension UITextField {
    @IBInspectable var LzPlaceholder: String? {
        get {
            self.placeholder = self.LzPlaceholder?.localized()
            self.fixAlignment()
            return self.placeholder
        }
        set {
            self.placeholder = newValue?.localized()
            self.fixAlignment()
        }
    }
    
    func fixAlignment() {
        if Localize.currentLanguage() == "ar" {
            if textAlignment == .natural {
                self.textAlignment = .right
            }
        }else if Localize.currentLanguage() == "en" {
            self.textAlignment = .left
        }
    }
}
