//
//  SubscriptionPageViewController.swift
//  LovePush
//
//  Created by lavi on 20/12/19.
//  Copyright © 2019 Jatinder Singh. All rights reserved.
//

import UIKit

class SubscriptionPageViewController: UIPageViewController {
    
    //MARK:- Variables
    
    private(set) lazy var orderedViewControllers: [UIViewController] = {
        return [self.newViewController("SubscriptionDetailsViewController"),
                self.newViewController("SubscriptionDetailsViewController")]
    }()
    
    private func newViewController(_ identifier: String) -> UIViewController {
        return mainStoryboard.instantiateViewController(withIdentifier: identifier)
    }
    
    
    var plans = [[String:Any]]()
    
    //MARK:- Controller Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if let firstViewController = self.orderedViewControllers.first as? SubscriptionDetailsViewController{

            self.setViewControllers([firstViewController],
                                    direction: .forward,
                                    animated: true,
                                    completion: nil)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.requestToGetPlans { (dataPlans) in
            if let plans = dataPlans {
                
                self.plans = plans
                
                self.dataSource = self
                
                if let firstViewController = self.orderedViewControllers.first as? SubscriptionDetailsViewController{
                    
                    firstViewController.planDetails = plans.first
                    firstViewController.index = 0
                    firstViewController.viewWillAppear(true)
                    self.setViewControllers([firstViewController],
                                            direction: .forward,
                                            animated: true,
                                            completion: nil)
                }
            }
        }
    }
}

// MARK: UIPageViewControllerDataSource

extension SubscriptionPageViewController: UIPageViewControllerDataSource {

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        let orderedViewControllersCount = orderedViewControllers.count
        
       
        
        guard orderedViewControllersCount != nextIndex else {
            return nil
        }
        
        guard orderedViewControllersCount > nextIndex else {
            return nil
        }
        
        DispatchQueue.global().sync {
            Singleton.shared.planIndex = nextIndex
        }
        
        let vc = orderedViewControllers[nextIndex] as! SubscriptionDetailsViewController
        
        vc.planDetails = self.plans[nextIndex]
        vc.index = nextIndex
        return vc
    }

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        
        guard let viewControllerIndex = orderedViewControllers.index(of: viewController) else {
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        
        
        guard previousIndex >= 0 else {
            return nil
        }
        
        guard orderedViewControllers.count > previousIndex else {
            return nil
        }
        
        DispatchQueue.global().sync {
            Singleton.shared.planIndex = previousIndex
        }
        
        let vc = orderedViewControllers[previousIndex] as! SubscriptionDetailsViewController
        vc.planDetails = self.plans[previousIndex]
        vc.index = previousIndex
        return vc
    }
}

extension SubscriptionPageViewController {
    
    func requestToGetPlans(completion: @escaping (_ plans: [[String:Any]]?)-> Void) {
        
        Helper.sharedInstance.ShowLoaderOnView(view: self.view)
        
        WebAPI.requestToGetType(URLConstant.getPlan) { (response, isSuccess) in
            Helper.sharedInstance.HideLoaderFromView(view: self.view)
            
            print(response)
            
            if isSuccess {
                if (response as! [String:Any])["status"] as! Int == 200 {
                    
                    if let dataPlans = (response as! [String:Any])["data"] as? [[String:Any]] {
                        completion(dataPlans)
                    }else {
                        completion(nil)
                        self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (ok) in
                            self.navigationController?.popViewController(animated: true)
                        })
                    }
                }else {
                    completion(nil)
                    self.showAlert(title: "Love Push", message: (response as! [String:Any])["message"] as! String, completion: { (ok) in
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            }else {
                completion(nil)
                self.showAlert(title: "Love Push", message: (response as! [String:Any])["Error"] as! String, completion: { (ok) in
                    self.navigationController?.popViewController(animated: true)
                })
            }
        }
    }
}
