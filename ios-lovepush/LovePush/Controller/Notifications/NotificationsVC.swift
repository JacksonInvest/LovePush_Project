//
//  NotificationsVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 16/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit

class NotificationsVC: UIViewController {

    //MARK:- Outlets
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var noDataLbl: UILabel!
    
    //MARK:- Variables
    var notificationArr = [[String:Any]]()
    var currentUser : UserInfo!
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let tabBarVC = (self.tabBarController as! TabBarController)
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        //Request to update status for seen new notifications
        tabBarVC.readNewNotifications { (isReadSuccess) in
            //Request to update status for seen new notifications
            tabBarVC.getNotifications { (newArr) in
                if newArr.count == 0 {
                    self.noDataLbl.isHidden = false;
                    self.tableView.isHidden = true;
                    self.tableView.reloadData()
                }else {
                    self.notificationArr = self.sortNewestNotification(newArr)
                    //                self.notificationArr = newArr
                    self.tableView.isHidden = false;
                    self.noDataLbl.isHidden = true;
                    self.tableView.reloadData()
                }
            }
        }
    }
    
    //MARK:- Button Action
    @IBAction func clearButtonTapped(_ sender: UIButton) {
        self.clearNotifications()
    }
}

extension NotificationsVC : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        Helper.sharedInstance.HideLoaderFromView(view: self.view)
        return self.notificationArr.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "listCell", for: indexPath) as! NotificationsCell
        
        cell.data = self.notificationArr[indexPath.row]
        
        cell.acceptButton.tag = indexPath.row
        cell.acceptButton.addTarget(self, action: #selector(acceptButtonTapped), for: .touchUpInside)
        
        cell.rejectButton.tag = indexPath.row
        cell.rejectButton.addTarget(self, action: #selector(rejectButtonTapped), for: .touchUpInside)
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        
        
        let eventType = self.notificationArr[indexPath.row]["event_type"] as! String
        
        // Event Type indicates request type:-
        //   "S_C_R":- Sent Connect Request
        //   "S_M_R":- Sent Message Request
        //   "S_L_R":- Sent Love Request
        
        if (SubscriptionPlan.shared.seeLoveRequest == "0") && (eventType == "S_L_R") {
            self.showAlert(title: "Love Push", message: "Your daily limit to see Date Request is over.", completion: nil)
        }else {
            if ((eventType != "S_C_R") && (eventType != "S_M_R") && (eventType != "S_L_R")) {
                let notificationID = self.notificationArr[indexPath.row]["id"] as! Int
                self.deleteAcceptedNotifications(notificationID) { (isSuccess) in
                    print("Is Deleted: \(isSuccess)")
                }
            }
            
            let vc         = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
            vc.userID      = "\(self.notificationArr[indexPath.row]["user_id"] as! Int)"
            vc.eventType   = eventType
            vc.eventID     = self.notificationArr[indexPath.row]["event_id"] as! Int
            
            vc.fromPage    = "Notification"
            if let messageEventInfo = self.notificationArr[indexPath.row]["mess_event_info"] as? [String:Any] {
                vc.msgInfoStatus = messageEventInfo["status"] as? String ?? ""
            }
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    //MARK:- Cell Button Action
    @objc func acceptButtonTapped(_ sender: UIButton) {
        self.currentUser  = UserInfo(self.notificationArr[sender.tag]["user_info"] as! [String:Any])
        self.acceptOrRejectReceivedRequest(sender.tag, status: "A")
    }
    
    @objc func rejectButtonTapped(_ sender: UIButton) {
        let eventType = self.notificationArr[sender.tag]["event_type"] as! String
        if (SubscriptionPlan.shared.seeLoveRequest == "0") && (eventType == "S_L_R") {
            self.showAlert(title: "Love Push", message: "Your daily limit to see Date Request is over.", completion: nil)
        }else {
            let eventID = self.notificationArr[sender.tag]["event_id"] as! Int
            let qbID    = "\((self.notificationArr[sender.tag]["user_info"] as! [String:Any])["quickblox_id"]!)"
            let userID  = self.notificationArr[sender.tag]["user_id"] as! Int
            
            if eventType == "S_L_R" {
                self.likeOrDislikUserRequest(0, eventType: eventType, eventID: eventID, qbID: qbID, userID: userID)
            }else {
                self.currentUser  = UserInfo(self.notificationArr[sender.tag]["user_info"] as! [String:Any])
                self.acceptOrRejectReceivedRequest(sender.tag, status: "R")
            }
        }
    }
    
    /**
     Accept or Reject received request sent to me by other users
     - parameter index: index number of request listed in table
     - parameter status: Status 0 for reject or 1 for accept request
     **/
    func acceptOrRejectReceivedRequest(_ index: Int, status: String) {
        let eventType = self.notificationArr[index]["event_type"] as! String
        
        if (SubscriptionPlan.shared.seeLoveRequest == "0") && (eventType == "S_L_R") {
            self.showAlert(title: "Love Push", message: "Your daily limit to see Date Request is over.", completion: nil)
        }else {
            let eventID   = self.notificationArr[index]["event_id"] as! Int
            let qbID      = "\((self.notificationArr[index]["user_info"] as! [String:Any])["quickblox_id"]!)"
            let userID    = self.notificationArr[index]["user_id"] as! Int
            
            if eventType == "S_L_R" {//
                self.likeOrDislikUserRequest((status == "A") ? 1 : 0, eventType: eventType, eventID: eventID, qbID: qbID, userID: userID)
            }else {
                self.acceptOrRejectRequest(status, eventType: eventType, eventID: eventID, qbID: qbID)
            }
        }
        
        
    }
}


