//
//  LegacyViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 21/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import WebKit

class LegacyViewController: UIViewController,WKNavigationDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var webView : WKWebView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var agreeButton: UIButton!
    @IBOutlet weak var notAgreeButton: UIButton!
    @IBOutlet weak var bottomHeight: NSLayoutConstraint!
    
    public var completion: ((Bool?) -> ())?
    
    var isFromSetting = false
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
//        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
//        self.webView.navigationDelegate = self
//        self.webView.load(URLRequest(url: URL(string: URLConstant.legacy)!))
        
        
        self.agreeButton.isHidden    = self.isFromSetting
        self.notAgreeButton.isHidden = self.isFromSetting
        self.backButton.isHidden     = !self.isFromSetting
        
        self.bottomHeight.constant = self.isFromSetting ? 20 : 70
    }
    
    //MARK:- Button Action
    
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func agreeButtonTapped(_ sender: UIButton) {
        self.completion!(true)
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func notAgreeButtonTapped(_ sender: UIButton) {
        self.completion!(false)
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK:- WKNavigation Delegate
    
//    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
//        Helper.sharedInstance.HideLoaderFromView(view: self.view)
//    }
//    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
//        Helper.sharedInstance.HideLoaderFromView(view: self.view)
//    }
//    
//    func webViewWebContentProcessDidTerminate(_ webView: WKWebView) {
//        Helper.sharedInstance.HideLoaderFromView(view: self.view)
//    }
}
