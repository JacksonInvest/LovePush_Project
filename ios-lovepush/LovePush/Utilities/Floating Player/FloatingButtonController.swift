import UIKit

import AVFoundation

class FloatingButtonController: UIViewController {
    
    var mainView: UIView!
    
    var playerButton : UIButton!
    
    var logoImage: UIImageView!
    
    var timerLabel : UILabel!
    
    var currentCallingVC : UIViewController!
    
    var remoteVideoView: QBRTCRemoteVideoView!
    
    var bambuserView : BambuserView?
    
    let window = FloatingButtonWindow()
    
    var lastControllerRotationStatus: Bool?
    
    required init?(coder aDecoder: NSCoder) {
        
        fatalError("init(coder:) has not been implemented")
    }
    
    init() {
        
        super.init(nibName: nil, bundle: nil)
        
        window.windowLevel = CGFloat.greatestFiniteMagnitude
        
        window.isHidden = false
        
        window.rootViewController = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(FloatingButtonController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardDidShow, object: nil)
    }
    
    override func loadView() {
        
        if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
            lastControllerRotationStatus = appDelegate.shouldRotate
            appDelegate.shouldRotate = false
        }
        
        let view = UIView()
        
        let mainView = UIView()
        
        mainView.backgroundColor = UIColor.black
        
        mainView.layer.shadowColor = UIColor.black.cgColor
        
        mainView.layer.shadowRadius = 3
        
        mainView.layer.shadowOpacity = 0.8
        
        mainView.layer.shadowOffset = CGSize.zero
        
        mainView.layer.borderWidth = 1.0
        
        mainView.layer.borderColor = UIColor.gray.cgColor
        
        mainView.frame = CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: 100, height: 120))
        
        let logoImage = UIImageView()
        
        logoImage.frame = CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: 100, height: 60))
        
        logoImage.image = #imageLiteral(resourceName: "AppIconSquare")
        
        mainView.addSubview(logoImage)
        
        if bambuserView == nil {
            let timerLabel = UILabel()
            
            timerLabel.frame = CGRect(origin: CGPoint(x: 0, y: 30), size: CGSize(width: 100, height: 40))
            
            timerLabel.textColor = UIColor(red: 169/255, green: 16/255, blue: 36/255, alpha: 1.0)
            
            timerLabel.text = "00:00:00"
            
            timerLabel.textAlignment = .center
            
            mainView.addSubview(timerLabel)
            
            let remoteVideoView = QBRTCRemoteVideoView()
            
            remoteVideoView.frame = CGRect(origin: CGPoint(x: 0, y: 40), size: CGSize(width: 100, height: 120))
            
            mainView.addSubview(remoteVideoView)
            
            self.remoteVideoView = remoteVideoView
            
            self.timerLabel = timerLabel
            
        }else {
            self.bambuserView!.previewFrame = CGRect(origin: CGPoint(x: 0, y: 40), size: CGSize(width: 100, height: 120))
            mainView.addSubview(self.bambuserView!.view)
        }
        
        let playerButton = UIButton()
        
        playerButton.frame = CGRect(origin: CGPoint(x: 5, y: 40), size: CGSize(width: 100, height: 120))
        
        playerButton.backgroundColor = UIColor.clear
        
        self.playerButton = playerButton
        
        self.playerButton.addTarget(self, action: #selector(representCall), for: .touchUpInside)
        
        mainView.addSubview(playerButton)
        
        view.addSubview(mainView)
        
        self.view = view
        
        self.logoImage = logoImage
        
        self.mainView = mainView
        
        window.mainView = mainView
        
        window.mainView?.backgroundColor = UIColor.white
        
        let panner = UIPanGestureRecognizer(target: self, action: #selector(FloatingButtonController.panDidFire(_:)))
        
        mainView.addGestureRecognizer(panner)
        
        self.window.isHidden = true
        
    }
    
    override func viewDidLayoutSubviews() {
        
        super.viewDidLayoutSubviews()
        
        snapButtonToSocket()
    }
    
    @objc func representCall() {
        
        var nav = UINavigationController()
        
        if Singleton.shared.floatingNavigationController == nil {
            let rootViewController = (UIApplication.shared.delegate as! AppDelegate).window?.rootViewController as! LoginViewController
            
            nav = rootViewController.parent as! UINavigationController
        }else {
            nav = Singleton.shared.floatingNavigationController
        }
        let vc = Singleton.shared.floatingButtonController.currentCallingVC

        nav.present(vc!, animated: false, completion: nil)

        Singleton.shared.floatingButtonController.window.isHidden = true
    }
    
    @objc func panDidFire(_ panner: UIPanGestureRecognizer) {
        
        let offset = panner.translation(in: view)
        
        panner.setTranslation(CGPoint.zero, in: view)
        
        var center = mainView.center
        
        center.x += offset.x
        
        center.y += offset.y
        
        mainView.center = center
        
        if panner.state == .ended || panner.state == .cancelled {
            
            UIView.animate(withDuration: 0.3, animations: {
                
                self.snapButtonToSocket()
                
            })
        }
    }
    
    @objc func keyboardDidShow(_ note: Notification) {
        
        window.windowLevel = 0
        
        window.windowLevel = CGFloat.greatestFiniteMagnitude
    }
    
    fileprivate func snapButtonToSocket() {
        
        var bestSocket = CGPoint.zero
        
        var distanceToBestSocket = CGFloat.infinity
        
        let center = mainView.center
        
        for socket in sockets {
            
            let distance = hypot(center.x - socket.x, center.y - socket.y)
            
            if distance < distanceToBestSocket {
                
                distanceToBestSocket = distance
                
                bestSocket = socket
                
            }
        }
        
        mainView.center = bestSocket
    }
    
    fileprivate var sockets: [CGPoint] {
        
        let buttonSize = mainView.bounds.size
        
        let rect = view.bounds.insetBy(dx: 4 + buttonSize.width / 2, dy: 4 + buttonSize.height / 2)
        
        let sockets: [CGPoint] = [
            
            CGPoint(x: rect.minX, y: rect.minY), CGPoint(x: rect.minX, y: rect.maxY), CGPoint(x: rect.maxX, y: rect.minY), CGPoint(x: rect.maxX, y: rect.maxY), CGPoint(x: rect.midX, y: rect.midY)
            
        ]
        
        return sockets
    }
}

class FloatingButtonWindow: UIWindow {
    
    var mainView: UIView?
    
    init() {
        
        super.init(frame: UIScreen.main.bounds)
        
        backgroundColor = nil
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        
        fatalError("init(coder:) has not been implemented")
        
    }
    
    
    internal override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
        
        guard let mainView = mainView else { return false }
        
        let mainViewPoint = convert(point, to: mainView)
        
        return mainView.point(inside: mainViewPoint, with: event)
    }
}
