//
//  BambuserLiveViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 12/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import MobileCoreServices
import Photos

class BambuserLiveViewController: UIViewController, BambuserViewDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var statusLabel : UILabel!
    @IBOutlet weak var timeLabel   : UILabel!
    @IBOutlet weak var swapButton  : UIButton!
    @IBOutlet weak var trackButton : UIButton!
    @IBOutlet weak var bambuserMainView : UIView!
    
    
    //MARK:- Variables
    var bambuserView : BambuserView!
    var pinchRecognizer: UIPinchGestureRecognizer!
    var initialZoom: Float = 0.0
//    var broadcastButton : UIButton
//    var swapButton: UIButton
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.bambuserView = BambuserView(preparePreset: kSessionPresetAuto)
//        self.broadcastButton = UIButton(type: UIButton.ButtonType.system)
        self.bambuserView.delegate = self
        self.bambuserView.applicationId = Singleton.shared.liveStreamingAppID
        
        // Do any additional setup after loading the view, typically from a nib.
        self.bambuserView.orientation = UIApplication.shared.statusBarOrientation
        self.bambuserMainView.addSubview(bambuserView.view)
        self.bambuserView.startCapture()
        
        self.broadcast()
        
        pinchRecognizer = UIPinchGestureRecognizer()
        initialZoom = 0.0
        
        pinchRecognizer = UIPinchGestureRecognizer(target: self, action: #selector(handlePinchGesture(_:)))
        
        self.view.addGestureRecognizer(pinchRecognizer);
    }
    
    override func viewWillLayoutSubviews() {
        var statusBarOffset : CGFloat = 0.0
        statusBarOffset = CGFloat(self.topLayoutGuide.length)
        self.bambuserView.previewFrame = CGRect(x: 0.0, y: 0.0 + statusBarOffset, width: self.view.bounds.size.width, height: self.view.bounds.size.height - statusBarOffset)
        
        if (self.bambuserView.hasFrontCamera) {
            swapButton.isHidden = false
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender :UIButton) {
        Singleton.shared.floatingButtonController.window.isHidden = true
        self.bambuserView.stopBroadcasting()
        self.dismiss(animated: false, completion: nil)
    }
    
    @IBAction func swapButtonTapped(_ sender :UIButton) {
        self.bambuserView.swapCamera()
    }
    
    @IBAction func goTrackButtonTapped(_ sender :UIButton) {
        
        _ = self.dismiss(animated: false, completion: nil)
        
        Singleton.shared.floatingButtonController.window.isHidden = false
        
    }
    
    @objc func handlePinchGesture(_ sender : UIPinchGestureRecognizer) {
        if (sender.state == UIGestureRecognizerState.began) {
            initialZoom = bambuserView.zoom
        }
        bambuserView.zoom = initialZoom * Float(sender.scale)
    }
    
    @objc func broadcast() {
        self.statusLabel.text = "Connecting"
        NSLog("Starting broadcast")
//        broadcastButton.setTitle("Connecting", for: UIControl.State.normal)
//        broadcastButton.removeTarget(nil, action: nil, for: UIControl.Event.touchUpInside)
//        broadcastButton.addTarget(bambuserView, action: #selector(bambuserView.stopBroadcasting), for: UIControl.Event.touchUpInside)
        self.bambuserView.startBroadcasting()
        
        Singleton.shared.floatingButtonController.bambuserView = self.bambuserView
        self.sendTracks()
    }
    
    func broadcastStarted() {
        self.statusLabel.text = "Live"
        
        NSLog("Received broadcastStarted signal")
//        broadcastButton.setTitle("Stop", for: UIControl.State.normal)
//        broadcastButton.removeTarget(nil, action: nil, for: UIControl.Event.touchUpInside)
//        broadcastButton.addTarget(bambuserView, action: #selector(bambuserView.stopBroadcasting), for: UIControl.Event.touchUpInside)
    }
    
    func broadcastStopped() {
        NSLog("Received broadcastStopped signal")
//        broadcastButton.setTitle("Broadcast", for: UIControl.State.normal)
//        broadcastButton.removeTarget(nil, action: nil, for: UIControl.Event.touchUpInside)
//        broadcastButton.addTarget(self, action: #selector(broadcast), for: UIControl.Event.touchUpInside)
    }
    
    func sendTracks(){
        
        Singleton.shared.floatingButtonController.mainView.frame.size = CGSize(width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.y = 0
        
        Singleton.shared.floatingButtonController.mainView.frame.origin.x = self.view.frame.size.width - 100
        
        Singleton.shared.floatingButtonController.playerButton.frame = CGRect(x: 0, y: 0, width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.timerLabel.frame = CGRect(x: 0, y: 0, width: 100, height: 40)
        
        Singleton.shared.floatingButtonController.bambuserView?.previewFrame = CGRect(x: 0, y: 40, width: 100, height: 120)
        
        Singleton.shared.floatingButtonController.window.isHidden = true
        
        Singleton.shared.floatingButtonController.remoteVideoView.isHidden = true
        
        Singleton.shared.floatingButtonController.timerLabel.isHidden = true
        
        Singleton.shared.floatingButtonController.logoImage.isHidden = false
        
    }
}
