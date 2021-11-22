//
//  InstagramLogin.swift
//  LovePush
//
//  Created by Lavi Kumar on 06/05/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit
import WebKit
//http://www.peerknowledge.net/instagram-login-shareios/

public protocol InstagramLoginViewControllerDelegate: class {
    /**
     When Login with instagram we will get user profile data on instagram account
     - Parameters:
     - accessToken: Token to get redirect URL
     - error: Something wrong in login process
     - Return: Nil
     **/
    func instagramLoginDidFinish(accessToken: String?, error: String?)
}

struct INSTAGRAM_IDS {
    
    static let INSTAGRAM_AUTHURL = "https://api.instagram.com/oauth/authorize/"
    
    static let INSTAGRAM_APIURl  = "https://api.instagram.com/v1/users/"
    
    static let INSTAGRAM_CLIENT_ID  = "a7a17573890441b4bd47def27abfe6e2"
    
    static let INSTAGRAM_CLIENTSERCRET = "5029786dd9c4459ca1eada968af3341b"
    
    static let INSTAGRAM_REDIRECT_URI = "https://www.love-push.com/"
    
    static let INSTAGRAM_ACCESS_TOKEN =  "access_token"
    
    static let INSTAGRAM_SCOPE = "basic"
    
}


class InstagramViewController: UIViewController {
    
    // MARK: - Properties
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var webView2: UIWebView!
    
//    private var webView: UIWebView!
    private var progressView: UIProgressView?
    private var webViewObservation: NSKeyValueObservation?
    
    // MARK: - Public Properties
    
    public weak var delegate: InstagramLoginViewControllerDelegate?
    
    // MARK: - View Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Initializes web view
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        setupWebView()
       
    }
    
    @IBAction func backButtonTapped(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func signOut() {
        if let cookies = HTTPCookieStorage.shared.cookies {
            for cookie in cookies {
                if cookie.domain.contains(".instagram.com") {
                    HTTPCookieStorage.shared.deleteCookie(cookie)
                }else {
                    print("Not Available")
                }
            }
        }
    }
    
    private func setupWebView() {
        
//        webView.navigationDelegate = self
//        webView.uiDelegate = self
        
        webView2.delegate = self
        
        self.signOut()
        self.unSignedRequest()
    }
    
    //MARK: - unSignedRequest
    func unSignedRequest () {
        let authURL = String(format: "%@?client_id=%@&redirect_uri=%@&response_type=token&scope=%@&DEBUG=True", arguments: [INSTAGRAM_IDS.INSTAGRAM_AUTHURL,INSTAGRAM_IDS.INSTAGRAM_CLIENT_ID,INSTAGRAM_IDS.INSTAGRAM_REDIRECT_URI      , INSTAGRAM_IDS.INSTAGRAM_SCOPE ])
        let urlRequest =  URLRequest.init(url: URL.init(string: authURL)!)
//        self.webView.load(urlRequest)
        self.webView2.loadRequest(urlRequest)
    }
    
    func checkRequestForCallbackURL(request: URLRequest) -> Bool {
        
        let requestURLString = (request.url?.absoluteString)! as String
        print(requestURLString)
        if requestURLString.hasPrefix(INSTAGRAM_IDS.INSTAGRAM_REDIRECT_URI) {
            let range: Range<String.Index> = requestURLString.range(of: "#access_token=")!
            handleAuth(authToken: requestURLString.substring(from: range.upperBound))
            return false;
        }else if requestURLString.hasPrefix("https://www.instagram.com/#reactivated") || "https://www.instagram.com/" == requestURLString {
            self.unSignedRequest()
        }
        return true
    }
    
    func handleAuth(authToken: String)  {
        print("Instagram authentication token ==", authToken)
        self.delegate?.instagramLoginDidFinish(accessToken: authToken, error: nil)
        self.dismiss(animated: true, completion: nil)
    }
}

//MARK:- WKNavigation Delegate

extension InstagramViewController: UIWebViewDelegate {//WKNavigationDelegate, WKUIDelegate{
    
    //Web view will start load with New URL
    internal func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        return checkRequestForCallbackURL(request: request)
    }

    //Loading Did started
    func webViewDidStartLoad(_ webView: UIWebView) {

    }

    //URL Loading did Finised
    func webViewDidFinishLoad(_ webView: UIWebView) {
//        APPDELEGATE.hideIndicator()
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }

    //Some Error occured in loading URL
    func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        webViewDidFinishLoad(webView)
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
    }
    
//    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
////        print(webView.url)
//        if let url = webView.url{
//            checkRequestForCallbackURL(request: URLRequest.init(url: url))
//        }
//    }
//
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
