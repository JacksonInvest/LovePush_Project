//
//  MessageVC + TableView.swift
//  LovePush
//
//  Created by Lavi Kumar on 06/09/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

//MARK:- Table View Delegate and Datasource
extension MessagesVC : UITableViewDelegate, UITableViewDataSource, ReportDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.dialogList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "UserTableViewCell", for: indexPath) as! UserTableViewCell
        
        cell.dialog = self.dialogList[indexPath.row]
        if let userData = self.getUser("\(cell.userOccupantID)") {
            cell.localdata = userData
        }
        
        if cell.user != nil {
            if cell.user.eventType == "L" {
                cell.imgView.layer.borderColor = UIColor(red: 184/255, green: 2/255, blue: 25/255, alpha: 1.0).cgColor
            }else if cell.user.eventType == "M" {
                cell.imgView.layer.borderColor = UIColor.darkGray.cgColor
            }else {
                cell.imgView.layer.borderColor = UIColor.blue.cgColor
            }
            cell.imgView.layer.borderWidth = 1.5
        }
        
        cell.profileImageButton.tag = indexPath.row
        cell.profileImageButton.addTarget(self, action: #selector(showProfileButtonTapped), for: .touchUpInside)
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        privateRoomInfo = self.dialogList[indexPath.row]
        var cell: UserTableViewCell!
        DispatchQueue.global().sync {
            cell = self.tableView.cellForRow(at: indexPath) as! UserTableViewCell
        }
        
        if cell != nil {
            QuickbloxHandling.shared.loadDialog(self.dialogList[indexPath.row].id!, complition: { (dialog) in
                if let loadedDialog = dialog {
                    if let user = cell.user {
                        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                        vc.dialog        = loadedDialog
                        vc.user          = user
                        vc.chatStatus    = user.chatStatus!
                        vc.deleteStatus  = (user.deletedBy != 0) ? 1 : 0
                        vc.matchID       = cell.matchID
                        //                        vc.isBlocked     =
                        self.navigationController?.pushViewController(vc, animated: true)
                    }
                }
            })
        }
    }
    
    func tableView(_ tableView: UITableView, titleForDeleteConfirmationButtonForRowAt indexPath: IndexPath) -> String? {
        return "Remove".localized()
    }
    
    func tableView(_ tableView: UITableView,
                   leadingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration?{
        
        let reportAction = UIContextualAction(style: .normal, title:  "Report".localized(), handler: { (ac:UIContextualAction, view:UIView, success:(Bool) -> Void) in
            print("Report Action ...")
            let cell = self.tableView.cellForRow(at: indexPath) as! UserTableViewCell
            self.showReportView(cell.user, index: indexPath.row)
            success(true)
        })
        reportAction.backgroundColor = UIColor(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0)
        
        let blockAction = UIContextualAction(style: .normal, title:  "Block".localized(), handler: { (ac:UIContextualAction, view:UIView, success:(Bool) -> Void) in
            print("Block Action ...")
            
            self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "If you want to block this user, your chat will be disabled and you can unblock user from Match List.", message: "Are you sure you want to block?") { (isYes) in
                if isYes {
                    var cell: UserTableViewCell!
                    DispatchQueue.global().sync {
                        cell = self.tableView.cellForRow(at: indexPath) as! UserTableViewCell
                    }
                    self.blockUnblockUser("\(cell.user.id!)", status: "block", completion: { (isSuccess) in
                        
                        //                        if let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID}) {
                        //                            self.chatList.remove(at: index)
                        //                            self.dialogList.remove(at: indexPath.row)
                        //                            self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                        //                        }
                        self.removeUserFromList(indexPath)
                    })
                }
            }
            
            success(true)
        })
        blockAction.backgroundColor = UIColor.darkGray
        
        return UISwipeActionsConfiguration(actions: [reportAction,blockAction])
        
    }
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        let deleteAction = UIContextualAction(style: .normal, title:  "Delete".localized(), handler: { (ac:UIContextualAction, view:UIView, success:(Bool) -> Void) in
            
            print("Delete Action ...")
            
            self.showAlertWith(yesBtnTitle: "Yes".localized(), noBtnTitle: "No".localized(), title: "Love Push".localized(), message: "Are you sure, you want to delete the chat?") { (isYes) in
                if isYes {
                    var cell: UserTableViewCell!
                    DispatchQueue.global().sync {
                        cell = (self.tableView.cellForRow(at: indexPath) as! UserTableViewCell)
                    }
                    
                    self.deleteRestoreChatWithUser("\(cell.user.id!)", match_id: "\(cell.matchID)", status: "1", completion: { (isSuccess) in
                        if isSuccess {
                            
                            //                            if let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID}) {
                            //                                self.chatList.remove(at: index)
                            //                                self.dialogList.remove(at: indexPath.row)
                            //                                self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                            //                            }
                            self.removeUserFromList(indexPath)
                        }
                    })
                }
            }
            success(true)
        })
        
        deleteAction.backgroundColor = UIColor(red: 255/255, green: 60/255, blue: 0/255, alpha: 1.0)
        
        let unmatchAction = UIContextualAction(style: .normal, title:  "Unmatch".localized(), handler: { (ac:UIContextualAction, view:UIView, success:(Bool) -> Void) in
            print("Block Action ...")
            self.showAlertWith(yesBtnTitle: "Yes".localized(), noBtnTitle: "No".localized(), title: "If you want to unmatch this user, you can't see this user in app.".localized(), message: "Are you sure, you want to unmatch?") { (isYes) in
                if isYes {
                    
                    self.unmatchRequest(indexPath.row)
                }
            }
            success(true)
        })
        unmatchAction.backgroundColor = UIColor.lightGray//(red: 52/255, green: 117/255, blue: 185/255, alpha: 1.0)
        return UISwipeActionsConfiguration(actions: [deleteAction, unmatchAction])
    }
    
    @objc func showProfileButtonTapped(_ sender: UIButton) {
        let cell = self.tableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as! UserTableViewCell
        let vc = homeStoryboard.instantiateViewController(withIdentifier: "ProfileVC") as! ProfileVC
        vc.userID = "\(cell.user.id!)"
        vc.isOnlyShow = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func showReportView(_ user: UserInfo, index: Int) {
        let reportView    = Bundle.main.loadNibNamed("ReportView", owner: self, options: nil)?.first as! ReportView
        reportView.frame  = self.view.frame
        reportView.user   = user
        reportView.target = self
        reportView.delegate = self
        reportView.index = index
        self.view.addSubview(reportView)
    }
    
    //    occupantID    String    "93283876"
    func getUser(_ occupantID: String)-> [String:Any]? {
        let user = self.chatList.filter { (dataDict) -> Bool in
            return (((dataDict["user_info1"] as! [String:Any])["quickblox_id"] as! String == occupantID) || ((dataDict["user_info2"] as! [String:Any])["quickblox_id"] as! String == occupantID))
        }
        
        return user.first
    }
    
    //MARK:- Report Delegate
    func didSentChatReport(_ index: Int, isSuccess: Bool) {
        if isSuccess {
            //        self.showAlertWith(yesBtnTitle: "Yes".localized(), noBtnTitle: "No".localized(), title: "Love Push".localized(), message: "Are you sure, you want to unmatch this user?") { (isYes) in
            //            if isYes {
//            self.unmatchRequest(index)
            //            }
            //        }
            
            let indexPath = IndexPath(row: index, section: 0)
            var cell: UserTableViewCell!
            DispatchQueue.global().sync {
                cell = (self.tableView.cellForRow(at: indexPath) as! UserTableViewCell)
            }
            
            QBRequest.deleteDialogs(withIDs: Set(arrayLiteral: self.dialogList[indexPath.row].id!), forAllUsers: true, successBlock: { (response, data1, data2, data3) in
                
                let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID})
                self.chatList.remove(at: index!)
                self.dialogList.remove(at: indexPath.row)
                self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                
            }, errorBlock: { (error) in
                print(error)
                let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID})
                self.chatList.remove(at: index!)
                self.dialogList.remove(at: indexPath.row)
                self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
            })
        }
    }
    
    //MARK:- Remove User from list
    func removeUserFromList(_ indexPath: IndexPath) {
        
        var cell: UserTableViewCell!
        DispatchQueue.global().sync {
            cell = (self.tableView.cellForRow(at: indexPath) as! UserTableViewCell)
        }
        
        let resPage = QBResponsePage(limit:20, skip: 0)
        
        QBRequest.messages(withDialogID: self.dialogList[indexPath.row].id!, extendedRequest: nil, for: resPage, successBlock: {(response: QBResponse, messages: [QBChatMessage]?, responcePage: QBResponsePage?) in
            //                                print(messages)
            let messagesIDs = messages!.map({$0.id!})
            //                                print(messagesIDs)
            QBRequest.deleteMessages(withIDs: Set(messagesIDs), forAllUsers: false, successBlock: { (response) in
                
                ServicesManager.instance().chatService.deleteMessagesLocally(messages!, forDialogID: self.dialogList[indexPath.row].id!)
                
                ServicesManager.instance().chatService.messagesMemoryStorage.delete(messages!, forDialogID: self.dialogList[indexPath.row].id!)
                
                if let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID}) {
                    self.chatList.remove(at: index)
                    self.dialogList.remove(at: indexPath.row)
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                    //                    self.noUSerView.isHidden = (self.dialogList.count > 0) && (self.matchedList.count > 0)
                }
            }, errorBlock: { (error) in
                print(error)
                
                ServicesManager.instance().chatService.deleteMessagesLocally(messages!, forDialogID: self.dialogList[indexPath.row].id!)
                ServicesManager.instance().chatService.messagesMemoryStorage.delete(messages!, forDialogID: self.dialogList[indexPath.row].id!)
                
                if let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID}) {
                    self.chatList.remove(at: index)
                    self.dialogList.remove(at: indexPath.row)
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                    //                    self.noUSerView.isHidden = (self.dialogList.count > 0) && (self.matchedList.count > 0)
                }
            })
        }, errorBlock: {(response: QBResponse!) in
            print(response)
        })
    }
    
    func unmatchRequest(_ index: Int) {
        
        let indexPath = IndexPath(row: index, section: 0)
        var cell: UserTableViewCell!
        DispatchQueue.global().sync {
            cell = (self.tableView.cellForRow(at: indexPath) as! UserTableViewCell)
        }
        
        self.removeMatchWithUser("\(cell.matchID)", completion: { (isSuccess) in
            
            if isSuccess {
                QBRequest.deleteDialogs(withIDs: Set(arrayLiteral: self.dialogList[indexPath.row].id!), forAllUsers: true, successBlock: { (response, data1, data2, data3) in
                    
                    let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID})
                    self.chatList.remove(at: index!)
                    self.dialogList.remove(at: indexPath.row)
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                    
                }, errorBlock: { (error) in
                    print(error)
                    let index = self.chatList.lastIndex(where: {$0["id"] as! Int == cell.matchID})
                    self.chatList.remove(at: index!)
                    self.dialogList.remove(at: indexPath.row)
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.automatic)
                })
            }
        })
    }
}
