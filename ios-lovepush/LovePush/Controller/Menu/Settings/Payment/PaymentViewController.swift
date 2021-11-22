//
//  PaymentViewController.swift
//  LovePush
//
//  Created by lavi on 06/01/20.
//  Copyright © 2020 Jatinder Singh. All rights reserved.
//

import UIKit
import WebKit

class PaymentViewController: UIViewController,WKNavigationDelegate  {
    
//    sb-nla43y840737@personal.example.com
//
//    s^&<g4K4

    
    //MARK:- Outlets
    @IBOutlet weak var webView : WKWebView!
    
    
    //MARK:- Variables
    var paypalURL = ""
    var selectedPlan = [String:Any]()
    public var completion: ((Bool?) -> ())?
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        self.webView.navigationDelegate = self
        self.webView.load(URLRequest(url: URL(string: paypalURL)!))
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
        print(webView.url)
        
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
        
        if "\(webView.url!)".contains("return?token=") {
            
            var userData = User.shared.data
            
            userData!["plan_details"] = ["plan": self.selectedPlan]
            
            User(userData)
            
            let dataSave = NSKeyedArchiver.archivedData(withRootObject: userData)
            UserDefaults.standard.set(dataSave, forKey: "userData")
            UserDefaults.standard.synchronize()
            
            self.showAlert(title: "Love push", message: "Plan has been subscribed successfully.") { (ok) in
                let viewControllers = self.navigationController!.viewControllers as [UIViewController];
                for aViewController:UIViewController in viewControllers {
                    if aViewController.isKind(of: SettingsVC.self) {
                        _ = self.navigationController?.popToViewController(aViewController, animated: true)
                    }
                }
            }
        }
       
    }
    
    func webViewWebContentProcessDidTerminate(_ webView: WKWebView) {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
}
