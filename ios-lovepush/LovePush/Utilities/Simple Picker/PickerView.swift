//
//  PickerView.swift
//  Quick Fix
//
//  Created by Apple on 10/10/18.
//  Copyright © 2018 Apple. All rights reserved.
//

import Foundation
import UIKit

protocol PickerViewProtocol {
    func didPickerSelected(_ itemIndex: Int)
}

open class PickerView: NSObject {
    
    var pickerView   = UIPickerView()
    var toolBar      = UIToolbar()
    var pickerArr    = [String]()
    var selectedItemIndex = -1
    var delegate: PickerViewProtocol?
    
    open class var shared : PickerView {
        struct Static {
            static let instance: PickerView = PickerView()
        }
        return Static.instance
    }
    
    func addpickerView(_ loadArr: [String],_ target: UIViewController){
        
        self.delegate  = target as? PickerViewProtocol
        self.pickerArr = loadArr
        
        // UIPickerView
        self.pickerView = UIPickerView(frame:CGRect(x: 0, y: target.view.frame.size.height - 216, width: target.view.frame.size.width, height: 216))
        self.pickerView.delegate        = self
        self.pickerView.dataSource      = self
        self.pickerView.backgroundColor = UIColor.lightGray
        
        target.view.addSubview(self.pickerView)
        
        // ToolBar
        self.toolBar = UIToolbar(frame:CGRect(x: 0, y: self.pickerView.frame.origin.y - 45, width: target.view.frame.size.width, height: 50))
        self.toolBar.barStyle        = .default
        self.toolBar.isTranslucent   = true
        self.toolBar.tintColor       = UIColor.black
        self.toolBar.backgroundColor = UIColor.darkGray
        self.toolBar.sizeToFit()
        
        // Adding Button ToolBar
        let doneButton = UIBarButtonItem(title: "Done".localized(), style: .plain, target: self, action: #selector(doneClick))
        let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel".localized(), style: .plain, target: self, action: #selector(cancelClick))
        self.toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        self.toolBar.isUserInteractionEnabled = true
        
        target.view.addSubview(self.toolBar)
    }
    
    @objc func doneClick(_ sender: UIPickerView) {
        if self.selectedItemIndex != -1 {
            self.delegate?.didPickerSelected(self.selectedItemIndex)
            self.removeFromSuperView()
        }else {
            //print("Please Pick a value Properly")
            self.delegate?.didPickerSelected(0)
            self.removeFromSuperView()
        }
    }
    
    @objc func cancelClick() {
        self.removeFromSuperView()
    }
    
    func removeFromSuperView() {
        self.pickerView.removeFromSuperview()
        self.toolBar.removeFromSuperview()
    }
}


extension PickerView: UIPickerViewDelegate, UIPickerViewDataSource {
    
    public func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    public func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return self.pickerArr.count
    }
    
    public func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return self.pickerArr[row].localized()
    }
    
    public func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        let label: UILabel
        
        if let view = view {
            label = view as! UILabel
        }
        else {
            label = UILabel(frame: CGRect(x: 4, y: 4, width: pickerView.frame.width, height: 400))
        }
        
        label.text = self.pickerArr[row].localized()
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        label.sizeToFit()
        
        return label
    }
    
    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        self.selectedItemIndex = row
    }
}
