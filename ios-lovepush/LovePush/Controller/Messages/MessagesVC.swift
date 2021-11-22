//
//  MessagesVC.swift
//  LovePush
//
//  Created by Lavi Kumar on 16/01/19.
//  Copyright © 2019 Lavi Kumar. All rights reserved.
//

import UIKit
import Quickblox

class MessagesVC: UIViewController {
    
    //MARK:- Outlets
    @IBOutlet weak var newMatchedHeight : NSLayoutConstraint!
    @IBOutlet weak var requestBtnHeight : NSLayoutConstraint!
    @IBOutlet weak var collectionView   : UICollectionView!
    @IBOutlet weak var tableView        : UITableView!
    @IBOutlet weak var noUserLbl        : UILabel!
    @IBOutlet weak var noUSerView       : UIView!
    @IBOutlet weak var newMatchesView   : UIView!
    @IBOutlet weak var requestButton    : UIButton!
    
    
    //MARK:- Variables
    var matchedList  = [[String:Any]]()
    var chatList     = [[String:Any]]()
    var dialogList   = [QBChatDialog]()
    var pageNo       = 1 // Current Page
    var totalPage    = 0
    var perPageCount = 15
    var isAPIEnable  = false
    var reqeustCount = 0
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.pageNo = 1
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        
        //
        self.getMatchedUser()
        //        self.getDialogList()
    }
    
    
    //MARK:- Button Action
    @IBAction func requestButtonTapped(_ sender: UIButton) {
        
        self.tabBarController?.selectedIndex = 1
    }
    
    //MARK:- Load User Data
    
    /**
     Get All Quickblox dialog list
     **/
    func getDialogList() {
        
        QuickbloxHandling.shared.getDialogList { (dialogs) in
            
            self.removeNonExistDialogFromQB(dialogs, completion: { (finalDialogs) in
                
                let filteredEmptyMessageDialogs = finalDialogs.filter({$0.lastMessageText == nil})
                
                if filteredEmptyMessageDialogs.count > 0 {
                    
                    let dialogIDs = filteredEmptyMessageDialogs.map({"\($0.id!)"})
                    
                    if dialogIDs.count > 0 {
                        let idsStr = dialogIDs.joined(separator: ",")
                        
                        QBRequest.deleteDialogs(withIDs: Set(dialogIDs), forAllUsers: true, successBlock: nil, errorBlock: nil)
                        
                        if let ids = self.filterDialogIDsFromLocalDialogList(idsStr) {
                            self.deleteDialogFromLocalDB(ids, completion: { (isSuccess) in
                                self.getMatchedUser()
                            })
                        }else {
                            self.loadData(finalDialogs)
                        }
                    }
                }else {
                    self.loadData(finalDialogs)
                }
            })
        }
    }
    
    /**
     Remove dialog if existing in quickblox but not in local server or delete , blocked by other user
     - parameter dialogs: Quickblox dialog list
     - returns: Completion handler with remaining QB Dialogs list
     **/
    func removeNonExistDialogFromQB(_ dialogs: [QBChatDialog], completion: @escaping (_ dialogs: [QBChatDialog])->Void) {
        
        let deletedUserMatch    = self.chatList.filter({($0["isDeleted"] as! Int == 1) || ($0["isBlocked"]! as! Int == 1)})
        //        Get Local Match List containing isDeleted or isBlocked status 1
        
        let deleteDialogIDs = deletedUserMatch.map({"\(($0["qb_chat_dialog"] as! [String:Any])["qb_dialog_id"]!)"})
        //        Get Dialog Ids From Local Match List containing isDeleted or isBlocked status 1
        
        var finalDialogs = dialogs.filter({ !deleteDialogIDs.contains($0.id!)})// Filter non deleted or blocked dialogs from QB Dialog list
        
        //        if finalDialogs.count > self.chatList.count {
        
        ///////////////
        let localDialogIds = self.chatList.map({"\(($0["qb_chat_dialog"] as! [String:Any])["qb_dialog_id"]!)"})// Get Dialog Ids From Local Match List are containing QB Dailog
        
        let filterQBDialogIds = finalDialogs.map({$0.id!}) // Get All QB Dialog Ids
        
        let removableIds = Set(filterQBDialogIds).subtracting(Set(localDialogIds))//Subtracting Local Dialogs ids From QB Dialog ids, then will have removable dialog ids
        
        
        let removableDialogs = finalDialogs.filter({removableIds.contains($0.id!)})//Get Removable Dialogs from All QB dailogs by removable Ids
        
        //            let myOwnRemovableDialogs = removableDialogs.filter({"\($0.userID)" == User.shared.qBUserID!})// Get my own created dialogs to delete or deleted from other user
        
        if removableDialogs.count > 0 {
            //Remove Dialogs if created by me
            let myRemovableDialogIds = removableDialogs.map({$0.id!})
            
            QBRequest.deleteDialogs(withIDs: Set(myRemovableDialogIds), forAllUsers: true, successBlock: { (response, dataSet1, dataSet2, dataSet3) in
                print(response)
            }) { (response) in
                print(response)
            }
        }
        
        //////////////////////
        finalDialogs = finalDialogs.filter({ !removableIds.contains($0.id!)}) //Get Final Dialogs List Not Contains Removable Ids
        
        completion(finalDialogs)
        //        }else {
        //           completion(finalDialogs)
        //        }
    }
    
    /**
     Get All dialog ids from local server
     - parameter dialogIDsStr: Quickblox dialog ids joined by ','
     - returns: Local server dialog ids
     **/
    func filterDialogIDsFromLocalDialogList(_ dialogIDsStr: String)-> String? {
        let localDBDialogs = self.chatList.filter { (userData) -> Bool in
            if let dialog = userData["qb_chat_dialog"] as? [String:Any] {
                if dialogIDsStr.contains(dialog["qb_dialog_id"] as! String) {
                    return true
                }else {
                    return false
                }
            }else {
                return false
            }
        }
        let localDialogIds = localDBDialogs.map({"\(($0["qb_chat_dialog"] as! [String:Any])["id"]!)"})
        //        print(localDialogIds)
        return (localDialogIds.count > 0) ? localDialogIds.joined(separator: ",") : nil
    }
    
    /**
     Load User List on table or Collection view
     - parameter dialogs: Final Quickblox Dialogs
     - returns: nil
     **/
    func loadData(_ dialogs: [QBChatDialog]) {
        var filteredNonEmptyMessageDialogs = [QBChatDialog]()
        DispatchQueue.global().sync {
            filteredNonEmptyMessageDialogs = self.filterNonEmptyMessageDialog(dialogs)
        }
        
        DispatchQueue.global().sync {
            if filteredNonEmptyMessageDialogs.count > 0 {
                self.noUSerView.isHidden = true
                self.dialogList = filteredNonEmptyMessageDialogs.sorted(by: { (dialog1, dialog2) -> Bool in
                    dialog1.join(completionBlock: nil)
                    if let date1 = dialog1.lastMessageDate,let date2 = dialog2.lastMessageDate {
                        return date1 > date2
                    }else {
                        return false
                    }
                    
                })
            }else if filteredNonEmptyMessageDialogs.count == 0 && (self.chatList.count == 0 && self.matchedList .count == 0) {
                self.dialogList.removeAll()
                self.noUSerView.isHidden = false
                //                self.showAlert(title: "No messages yet.", message: "Go to Explore & find your first match.", completion: nil)
            }else {
                self.noUSerView.isHidden = true
            }
        }
        self.newMatchesView.isHidden = (self.matchedList.count == 0)
        self.newMatchedHeight.constant = (self.matchedList.count == 0) ? 0 : 142
        
        self.noUserLbl.isHidden = (self.dialogList.count > 0) && (self.matchedList.count > 0)
        
        self.collectionView.reloadData()
        self.tableView.reloadData()
    }
    
    /**
     Empty message dialog needs to delete if created and will display in newest match list
     - parameter dialogList: Final Quickblox Dialogs
     - returns: nil
     **/
    func filterNonEmptyMessageDialog(_ dialogList: [QBChatDialog])-> [QBChatDialog] {
        let finalDailogList = dialogList.filter { (dialog) -> Bool in
            if dialog.lastMessageText == nil {
                //                QBRequest.deleteDialogs(withIDs: [dialog.id!], forAllUsers: true, successBlock: nil, errorBlock: nil)
                return false
            }else {
                return true
            }
        }
        return finalDailogList
    }
}



