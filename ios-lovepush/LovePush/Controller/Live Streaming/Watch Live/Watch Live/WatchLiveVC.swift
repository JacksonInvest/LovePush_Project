//
//  WatchLiveVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 29/03/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class WatchLiveVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var broadcastTableView: UITableView!
    
    //MARK:- Variables
    var broadcastArr = [[String:Any]]()
    var bambuserPlayerCompletion: ((Bool?) -> ())?
    var liveCellArr = [LiveBroadcastTableViewCell]()
    var bambuserPlayer  : BambuserPlayer!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        self.getLiveBroadcastList()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if Singleton.shared.isLiveVideoStopped {        // When you watch live video and Live User stopped then
            Singleton.shared.isLiveVideoStopped = false // need to refresh video list
            self.getLiveBroadcastList()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        if self.bambuserPlayer != nil {
            self.bambuserPlayer.stopVideo()
            self.bambuserPlayer = nil
        }
    }
    
    @IBAction func BackAction(_ sender: UIButton) {
        
        self.navigationController?.popViewController(animated: true)
    }
}

extension WatchLiveVC : UITableViewDelegate, UITableViewDataSource, BambuserPlayerDelegate  {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.broadcastArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "LiveBroadcastTableViewCell", for: indexPath) as! LiveBroadcastTableViewCell
        
        cell.broadcast = Broadcast(self.broadcastArr[indexPath.row])
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "WatchLiveMainViewController") as! WatchLiveMainViewController
        vc.broadcast = Broadcast(self.broadcastArr[indexPath.row])
        self.present(vc, animated: true, completion: nil)
    }
}
