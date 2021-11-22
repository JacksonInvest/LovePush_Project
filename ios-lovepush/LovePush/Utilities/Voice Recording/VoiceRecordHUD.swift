//
//  VoiceRecordHUD.swift
//  Audio Recorder
//
//  Created by Venkat Kukunuru on 30/12/16.
//  Copyright © 2016 Venkat Kukunuru. All rights reserved.
//

import UIKit

@IBDesignable
class VoiceRecordHUD: UIView {
    @IBInspectable var rate: CGFloat = 0.0
    
    @IBInspectable var fillColor: UIColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0) {
        didSet {
            setNeedsDisplay()
        }
    }
    var image: UIImage! {
        didSet {
            setNeedsDisplay()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        image = UIImage(named: "mikeIcon")
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        image = UIImage(named: "mikeIcon")
    }
    
    func update(_ rate: CGFloat) {
        self.rate = rate
        setNeedsDisplay()
    }
    
    override func draw(_ rect: CGRect) {
        let context = UIGraphicsGetCurrentContext()
        context?.translateBy(x: 0, y: bounds.size.height)
        context?.scaleBy(x: 1, y: -1)
        
        context?.draw(image.cgImage!, in: bounds)
        context?.clip(to: bounds, mask: image.cgImage!)
        
        context?.setFillColor(fillColor.cgColor.components!)
        context?.fill(CGRect(x: 0, y: 0, width: bounds.width, height: bounds.height * rate))
    }
    
    override func prepareForInterfaceBuilder() {
        let bundle = Bundle(for: type(of: self))
        image = UIImage(named: "mikeIcon", in: bundle, compatibleWith: self.traitCollection)
    }
}
