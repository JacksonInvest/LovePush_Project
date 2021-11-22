//
//  FAQVC + API.swift
//  LovePush
//
//  Created by Lavi Kumar on 08/10/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import Foundation

//MARK:- Table View Delegate And Data Source

extension FAQViewController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.questionArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "FAQTableViewCell", for: indexPath) as! FAQTableViewCell
        
        cell.qsnLabel.text = self.questionArray[indexPath.row]["question"] as? String ?? ""
        
        if selectedQsnArr.contains(indexPath.row) {
            cell.answerLabel.text = self.questionArray[indexPath.row]["answer"] as? String ?? ""
            cell.arrowIcon.image  = UIImage(named: "downArrow")
        }else {
            cell.answerLabel.text = ""
            cell.arrowIcon.image  = UIImage(named: "rightArrow")
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if selectedQsnArr.contains(indexPath.row) {
            self.selectedQsnArr.remove(indexPath.row)
        }else {
            self.selectedQsnArr.add(indexPath.row)
        }
        self.questionTableView.reloadData()
    }
}
