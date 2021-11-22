//
//  WatchLiveMainViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 12/08/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import MobileCoreServices
import Photos

class WatchLiveMainViewController: UIViewController, BambuserPlayerDelegate {
    
    //MARK:- Outlets
    @IBOutlet weak var playPauseButton : UIButton!
    @IBOutlet weak var stopButton      : UIButton!
    @IBOutlet weak var latencyLabel    : UILabel!
    @IBOutlet weak var slider          : UISlider!
    @IBOutlet weak var currentViewersLabel : UILabel!
    @IBOutlet weak var playerView          : UIView!
    
    var bambuserPlayer  : BambuserPlayer
    var seekerTimer     : Timer
    var latencyTimer    : Timer
    var broadcast       : Broadcast!
    
    required init?(coder aDecoder: NSCoder) {
        self.bambuserPlayer = BambuserPlayer()
        self.seekerTimer = Timer()
        self.latencyTimer = Timer()
        super.init(coder: aDecoder)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.contentSetup()
        
        bambuserPlayer.frame = CGRect(x: 0, y: 0, width: self.view.frame.size.width, height: self.view.frame.size.height)
        bambuserPlayer.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        bambuserPlayer.delegate = self
        bambuserPlayer.applicationId = Singleton.shared.liveStreamingAppID
        // This is a sample video; you can get a similarly signed resource URI for your broadcasts via the
        // Bambuser Metadata API.
        bambuserPlayer.playVideo(broadcast.resourceUri)
        self.playerView.addSubview(bambuserPlayer)
        
    }
    
    
    //MARK:- Button Actions
    
    @IBAction func playPauseButtonTapped(_ sender : UIButton) {
    
        if sender.currentImage == #imageLiteral(resourceName: "pause-white") {
            sender.setImage(#imageLiteral(resourceName: "play-white"), for: .normal)
            self.bambuserPlayer.pauseVideo()
        }else {
            sender.setImage(#imageLiteral(resourceName: "pause-white"), for: .normal)
            self.bambuserPlayer.playVideo()
        }
    }
    
    @IBAction func stopButtonTapped(_ sender : UIButton) {
        self.stopLiveVideo()
    }
    
    @IBAction func sliderSeekChanged(_ sender : UISlider) {
        self.seekTo(sender)
    }
    
    func durationKnown(_ duration: Double) {
        slider.minimumValue = 0
        slider.maximumValue = Float(duration)
        self.contentSetup()
    }
    
    func contentSetup() {
        if (broadcast.type!.uppercased() == "ARCHIVED") {
            self.slider.isEnabled         = true
            self.slider.isHidden          = false
            self.playPauseButton.isHidden = false
            self.latencyLabel.text        = ""
        }else {
            self.slider.isEnabled         = false
            self.slider.isHidden          = true
            self.playPauseButton.isHidden = true
            self.latencyLabel.text        = "Live"
        }
    }
    
    @objc func seekTo(_ sender: AnyObject) {
        let time = Double(slider.value)
        bambuserPlayer.seek(to: time)
    }
    
    @objc func updateSlider() {
        if (!slider.isTracking) {
            slider.value = Float(bambuserPlayer.playbackPosition)
        }
    }
    
    @objc func updateLatency() {
        let latency = bambuserPlayer.endToEndLatency
        if (latency.uncertainty >= 0) {
            latencyLabel.text = String(format: "Latency: %.2f s", latency.latency)
        } else {
//            latencyLabel.text = ""
        }
    }
    
    @objc func stopLiveVideo() {
        self.bambuserPlayer.stopVideo()
        self.dismiss(animated: false, completion: nil)
    }
    
    func videoLoadFail() {
        NSLog("videoLoadFail called")
    }
    
    func playbackStatusChanged(_ status: BambuserPlayerState) {
        switch status {
        case kBambuserPlayerStatePlaying:
            if (broadcast.type!.uppercased() == "LIVE") {
                self.playPauseButton.isHidden = true
                self.latencyLabel.text = "Live"
            }else {
                self.playPauseButton.isHidden = false
                self.latencyLabel.text = ""
            }
            self.seekerTimer = Timer.scheduledTimer(timeInterval: 0.1, target: self, selector: #selector(updateSlider), userInfo: nil, repeats: true)
            self.latencyTimer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(updateLatency), userInfo: nil, repeats: true)
            break
        case kBambuserPlayerStatePaused:
            self.seekerTimer.invalidate()
            self.latencyTimer.invalidate()
            self.currentViewersLabel.text = ""
//            self.latencyLabel.text = ""
            self.playPauseButton.setImage(#imageLiteral(resourceName: "play-white"), for: .normal)
            break
        case kBambuserPlayerStateStopped:
            self.seekerTimer.invalidate()
            self.latencyTimer.invalidate()
            self.currentViewersLabel.text = ""
            if (broadcast.type!.uppercased() == "LIVE") {
                Singleton.shared.isLiveVideoStopped = true
                self.dismiss(animated: false, completion: nil)
            }
            break
        default:
            break
        }
    }
    
    func currentViewerCountUpdated(_ viewers: Int32) {
        self.currentViewersLabel.text = "Viewers: \(viewers)"
    }
    
    func totalViewerCountUpdated(_ viewers: Int32) {
        print("Total viewers: \(viewers)")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
