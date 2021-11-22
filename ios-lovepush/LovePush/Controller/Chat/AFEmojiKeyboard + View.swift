//
//  AFEmojiKeyboard + View.swift
//  LovePush
//
//  Created by Lavi Kumar on 10/06/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation
import UIKit
import AGEmojiKeyboard

extension ChatViewController: AGEmojiKeyboardViewDataSource, AGEmojiKeyboardViewDelegate {
    
    func setupEmojiView() {
        self.emojiKeyboardView = AGEmojiKeyboardView(frame: CGRect(x: 0, y: self.emojViewY, width: Double(view.frame.size.width), height: self.keyboardHeight), dataSource: self)
        self.emojiKeyboardView.autoresizingMask = UIView.AutoresizingMask.flexibleHeight
        self.emojiKeyboardView.delegate = self
        self.emojiKeyboardView.segmentsBar.tintColor = UIColor.lightGray
        self.inputToolbar?.contentView.textView.inputView = emojiKeyboardView
        self.inputToolbar?.contentView.textView.reloadInputViews()
    }
    
    func emojiKeyBoardView(_ emojiKeyBoardView: AGEmojiKeyboardView?, didUseEmoji emoji: String?) {
        self.inputToolbar?.contentView?.textView.text = (self.inputToolbar?.contentView?.textView.text)! + (emoji ?? "")
    }
    
    func emojiKeyBoardViewDidPressBackSpace(_ emojiKeyBoardView: AGEmojiKeyboardView?) {
        if !(self.inputToolbar?.contentView?.textView.text.isEmpty)! {
            self.inputToolbar?.contentView?.textView.text.removeLast()
        }
    }

    func randomColor() -> UIColor? {
        return UIColor(red: CGFloat(drand48()), green: CGFloat(drand48()), blue: CGFloat(drand48()), alpha: CGFloat(drand48()))
    }
    
    func randomImage() -> UIImage? {
        let size = CGSize(width: 30, height: 10)
        UIGraphicsBeginImageContextWithOptions(size, _: false, _: 0)
        
        let context = UIGraphicsGetCurrentContext()
        var fillColor: UIColor? = randomColor()
        context?.setFillColor((fillColor?.cgColor)!)
        var rect = CGRect(x: 0, y: 0, width: size.width, height: size.height)
        context?.fill(rect)
        
        fillColor = randomColor()
        context?.setFillColor((fillColor?.cgColor)!)
        let xxx: CGFloat = 3
        rect = CGRect(x: xxx, y: xxx, width: size.width - 2 * xxx, height: size.height - 2 * xxx)
        context?.fill(rect)
        
        let img: UIImage? = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return img
    }

    func emojiKeyboardView(_ emojiKeyboardView: AGEmojiKeyboardView?, imageForSelectedCategory category: AGEmojiKeyboardViewCategoryImage) -> UIImage? {
        let imageArr = [#imageLiteral(resourceName: "clock"),#imageLiteral(resourceName: "happy"),#imageLiteral(resourceName: "giftbox"),#imageLiteral(resourceName: "frog"),#imageLiteral(resourceName: "home"),#imageLiteral(resourceName: "different-currencies")]
        let img: UIImage? = imageArr[category.rawValue]//randomImage()
        img?.withRenderingMode(.alwaysOriginal)
        return img//img
    }
    
    func emojiKeyboardView(_ emojiKeyboardView: AGEmojiKeyboardView?, imageForNonSelectedCategory category: AGEmojiKeyboardViewCategoryImage) -> UIImage? {
        let imageArr = [#imageLiteral(resourceName: "clock"),#imageLiteral(resourceName: "happy"),#imageLiteral(resourceName: "giftbox"),#imageLiteral(resourceName: "frog"),#imageLiteral(resourceName: "home"),#imageLiteral(resourceName: "different-currencies")]
        let img: UIImage? = imageArr[category.rawValue]//randomImage()
        img?.withRenderingMode(.alwaysOriginal)
        return img//#imageLiteral(resourceName: "checkedOrange")
    }
    
    func backSpaceButtonImage(for emojiKeyboardView: AGEmojiKeyboardView?) -> UIImage? {
        let img: UIImage? = randomImage()
        img?.withRenderingMode(.alwaysOriginal)
        return #imageLiteral(resourceName: "backBlackIcon")//img
    }
}
