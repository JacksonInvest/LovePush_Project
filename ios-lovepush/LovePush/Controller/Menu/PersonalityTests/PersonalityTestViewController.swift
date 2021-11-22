//
//  PersonalityTestViewController.swift
//  LovePush
//
//  Created by Lavi Kumar on 07/05/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class PersonalityTestViewController: UIViewController {
    
    //MARK:- Outlet
    @IBOutlet weak var questionLabel: UILabel!
    @IBOutlet weak var maximumAnserLabel: UILabel!
    @IBOutlet weak var personalityTestTableView: UITableView!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var progressView: UIProgressView!
    
    //MARK:- Variables
    var selectOptionArr = [Int]()
    var answerArr   = [[String:Any]]()
    var questionArr = [[String:Any]]()
    var maxAnswer = 0
    var currentQuestionID = 0
    var currentQuestionIndex = 0
    var whatAreYouSearchingForIndex = -1
    var totalQuestionToAttempt = 10
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        self.getQuestions()
        self.progressViewSetup()
    }
    
    func progressViewSetup() {
        self.progressView.layer.cornerRadius = 5
        self.progressView.layer.masksToBounds = true
    }
    
    //MARK:- Button Action
    @IBAction func backButtonTapped(_ sender: UIButton) {
        if self.currentQuestionIndex != 0 {
            self.showAlert(title: "Love Push", message: "Kindly complete the test to exit this section", completion: nil)
        }else {
            self.navigationController?.popViewController(animated: true)
        }
    }
    @IBAction func nextButtonTapped(_ sender: UIButton) {
        
        if (self.selectOptionArr.count > 0 && self.selectOptionArr.count == self.maxAnswer) || (self.currentQuestionIndex == 0 && self.selectOptionArr.count > 0) {
            self.saveAnswerAndGotToNext()
        }else {
            self.showAlert(title: "Love Push", message: "Please give all answer of the question", completion: nil)
        }
    }
    
    //MARK:- Functions
    func setCurrentQuestion(_ question: [String:Any]) {
        
        self.progressView.progress = Float(self.currentQuestionIndex) / Float(self.totalQuestionToAttempt)
        
        self.maxAnswer = question["max_answers"] as! Int
        self.currentQuestionID = question["id"] as! Int
        self.selectOptionArr.removeAll()
        if currentQuestionIndex == 0 {
            self.maximumAnserLabel.text = "Mark".localized() + " " + "Max".localized()  + " " +  "3"  + " " +  "Answers".localized()
        }else {
            self.maximumAnserLabel.text = ("Mark".localized() + " " + String(describing: self.maxAnswer) + " Answers".localized()).replacingOccurrences(of: "Mark".localized()  + " " +  "1"  + " " + "Answers".localized(), with: "Mark".localized() + " " +  "1" + " " +  "Answer".localized())
        }
        if let questionDescription = question["get_question_desc"] as? [String:Any] {
            self.questionLabel.text = questionDescription["question"] as? String ?? ""
        }
        if let answerArr = question["get_ques_options"] as? [[String:Any]] {
            self.answerArr = answerArr
            self.personalityTestTableView.reloadData()
        }
    }
}

//MARK:- Table View Delegate
extension PersonalityTestViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.answerArr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PersonalityTestTableViewCell", for: indexPath) as! PersonalityTestTableViewCell
//            cell.checkButton.tag = indexPath.row
//            cell.checkButton.addTarget(self, action: #selector(checkButtonTapped), for: .touchUpInside)
        
        let answer = self.answerArr[indexPath.row]
        
        cell.answerTitleLabel.text = (answer["get_ques_option_desc"] as! [String:Any])["option"] as? String ?? ""
        
        let answerID = answer["id"] as? Int ?? 0
        
        if self.selectOptionArr.contains(answerID) {
            cell.answerTitleLabel.textColor = UIColor.white
            cell.superView.backgroundColor = UIColor(red: 32/255, green: 121/255, blue: 206/255, alpha: 1.0)
            cell.checkButton.setImage(#imageLiteral(resourceName: "checked_blue"), for: .normal)
        }else {
            cell.answerTitleLabel.textColor = UIColor.black
            cell.superView.backgroundColor = UIColor.white
            cell.superView.layer.borderColor = UIColor.lightGray.cgColor
            cell.superView.layer.borderWidth = 1
            cell.checkButton.setImage(#imageLiteral(resourceName: "uncheck"), for: .normal)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let answer = self.answerArr[indexPath.row]
        let answerID = answer["id"] as? Int ?? 0
        if self.maxAnswer == 1 {
            self.selectOptionArr.removeAll()
            self.selectOptionArr.append(answerID)
            if self.currentQuestionIndex == 1 || self.currentQuestionIndex == 4 {
                self.whatAreYouSearchingForIndex = indexPath.row
            }
        }else if self.selectOptionArr.count < self.maxAnswer {
            
            if let index = self.selectOptionArr.lastIndex(of: answerID) {
                self.selectOptionArr.remove(at: index)
            }else {
                self.selectOptionArr.append(answerID)
            }
        }else if self.selectOptionArr.count == self.maxAnswer {
            if let index = self.selectOptionArr.lastIndex(of: answerID) {
                self.selectOptionArr.remove(at: index)
            }else {
                self.showAlert(title: "Love Push", message: "Maximum answer limit of this question is".localized()
                    + " " + "\(self.maxAnswer)", completion: nil)
            }
        }
        self.personalityTestTableView.reloadData()
    }
}

//MARK:- API
extension PersonalityTestViewController {
    
    /**
     Get All question list to give personality test
    **/
    func getQuestions() {
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetWithHeader(URLConstant.personalityTest, header: header as [String : AnyObject]) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let test_id = (response as! [String:Any])["test_id"] as? Int {
                        self.showAlertWith(yesBtnTitle: "Yes", noBtnTitle: "No", title: "You have to attempt the personality test completely", message: "Do you want to edit your personality test?", completion: { (isYes) in
                            if isYes {
                                
                                self.deleteUserTestRecordBeforeAttemptingAgain()
                                
                                let array = (response as! [String:Any])["data"] as! [[String:Any]]
                                
                                if(array.count != 0) {
                                    self.questionArr = array
                                    self.setCurrentQuestion(self.questionArr.first!)
                                }
                            }else {
                                self.navigationController?.popViewController(animated: true)
                            }
                        })
                    }else {
                        let array = (response as! [String:Any])["data"] as! [[String:Any]]
                        
                        if(array.count != 0) {
                            self.questionArr = array
                            self.setCurrentQuestion(self.questionArr.first!)
                        }
                    }
                   
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
    
    /**
     Delete previous given test record to start test again
    **/
    func deleteUserTestRecordBeforeAttemptingAgain() {
        
        let param = [
            "user_id": User.shared.id!
            ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.deleteUserTestRecord, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                }else {
                    self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["message"] as! String, actionTitle: "Ok", completion: { (isYes) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                self.showAlertWithAction(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, actionTitle: "Ok", completion: { (isYes) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
    
    /**
     Save Current Question answer and to next question
    **/
    func saveAnswerAndGotToNext() {
        
        let stringArray = self.selectOptionArr.map { String($0) }
        let string = stringArray.joined(separator: ",")
        
        let param = [
                        "user_id": User.shared.id!,
                        "question_id": self.currentQuestionID,
                        "option_ids": string
                    ] as [String : Any]
        
        let header = ["locale": Singleton.shared.language, "Authorization" : "Bearer \(String(describing: User.shared.token!))"]
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToPostBodyWithHeader(URLConstant.userAnswerSave, param, header: header) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if self.totalQuestionToAttempt == 0 {
                         self.totalQuestionToAttempt = self.questionArr.count
                    }
                    
                    if (self.currentQuestionIndex == 1) && (self.whatAreYouSearchingForIndex != 0)  {
                        self.totalQuestionToAttempt = 8 //If you select love answer option of Ques. 2. Then you have to attemt only 10 questions
                    }
                    
                    if self.currentQuestionIndex == self.totalQuestionToAttempt - 1 {
                        
                        self.showAlertWithAction(title: "Congratulations, you have done it!", message: "Thank you for taking the time, here are your results.", actionTitle: "Continue", completion: { isOK in
                            let vc = homeStoryboard.instantiateViewController(withIdentifier: "RecommendedProfilesID") as! RecommendedProfilesVC
                            self.navigationController?.pushViewController(vc, animated: true)
                        })
                    }else {
                        if self.currentQuestionIndex == self.totalQuestionToAttempt - 2 {
                            self.nextButton.setTitle("DONE".localized(), for: .normal)
                        }
                        self.currentQuestionIndex += 1
                        self.setCurrentQuestion(self.questionArr[self.currentQuestionIndex])
                    }
                }else {
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: nil)
                }
            }else {
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: nil)
            }
        }
    }
}
