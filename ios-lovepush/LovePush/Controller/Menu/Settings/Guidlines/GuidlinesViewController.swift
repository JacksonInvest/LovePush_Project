//
//  GuidlinesViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 21/08/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import WebKit

class GuidlinesViewController: UIViewController,WKNavigationDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var webView : WKWebView!
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.webView.navigationDelegate = self
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        self.webView.load(URLRequest(url: URL(string: URLConstant.communityGuidlines)!))
    }
    
    //MARK:- Button Action
    
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK:- WKNavigation Delegate
   
    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
    
    func webViewWebContentProcessDidTerminate(_ webView: WKWebView) {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
}
