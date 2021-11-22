//
//  RecorderViewController.swift
//  Audio Recorder
//
//  Created by Venkat Kukunuru on 01/01/17.
//  Copyright © 2017 Venkat Kukunuru. All rights reserved.
//

import Foundation
import UIKit

protocol RecorderViewDelegate : class {
    func didFinishRecording(_ recorderViewController: RecorderViewController, filePath: URL, duration: Int)
}

class RecorderViewController: UIViewController , RecorderDelegate {
   
    //MARK:- Outlets
    @IBOutlet weak var tapToFinishBtn: UIButton!
    @IBOutlet weak var durationLabel: UILabel!
    @IBOutlet weak var voiceRecordHUD: VoiceRecordHUD!
    
    //MARK:- Variables
    open weak var delegate: RecorderViewDelegate?
    var recording: Recording!
    var recordDuration = 0

    // MARK: View Life Cycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        createRecorder()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        voiceRecordHUD.update(0.0)
        voiceRecordHUD.fillColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
        durationLabel.text = ""

    }
    
    open func createRecorder() {
        recording = Recording(to: "audio.m4a")
        recording.delegate = self
        
        // Optionally, you can prepare the recording in the background to
        // make it start recording faster when you hit `record()`.
        
        DispatchQueue.global().async {
            // Background thread
            do {
                try self.recording.prepare()
            } catch {
                print(error)
            }
        }
    }

    open func startRecording() {
        recordDuration = 0
        do {
            try recording.record()
        } catch {
            print(error)
        }
    }
    
    @IBAction func cancel() {
        dismiss(animated: true, completion: nil)
        
        recordDuration = 0
        recording.stop()
        voiceRecordHUD.update(0.0)
    }

    @IBAction func stop() {
        
        delegate?.didFinishRecording(self, filePath: recording!.url, duration: self.recordDuration)
        dismiss(animated: true, completion: nil)
        
        recordDuration = 0
        recording.stop()
        voiceRecordHUD.update(0.0)
        
    }
    
    func audioMeterDidUpdate(_ db: Float) {
//        print("db level: %f", db)
        
        self.recording.recorder?.updateMeters()
        let ALPHA = 1.0
        let peakPower = pow(10, (ALPHA * Double((self.recording.recorder?.peakPower(forChannel: 0))!)))
        var rate: Double = 0.0
        if (peakPower <= 0.2) {
            rate = 0.2
        } else if (peakPower > 0.9) {
            rate = 1.0
        } else {
            rate = peakPower
        }
        
        voiceRecordHUD.update(CGFloat(rate))
        voiceRecordHUD.fillColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0)
        recordDuration += 1
//        durationLabel.text = recordDuration.timeStamp()//String(recordDuration)
        
        self.hmsFrom(duration: recordDuration) { hours, minutes, seconds in
            
            let hours = self.getStringFrom(hours)
            
            let minutes = self.getStringFrom(minutes)
            
            let seconds = self.getStringFrom(seconds)
            
            let recordTimeStr = "\(hours):\(minutes):\(seconds)"
            
            self.durationLabel.text = recordTimeStr
        }
    }
}
