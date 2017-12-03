//
//  DemoMessagesViewController+Swift.swift
//  JSQMessages
//
//  Created by Mickens, Maurice (CMG-Atlanta) on 12/2/17.
//  Copyright © 2017 Hexed Bits. All rights reserved.
//

import Foundation
import ApiAI

enum EndPoint:String{
    case getBotMessage = "http://big-hack.us-east-1.elasticbeanstalk.com?chat"
}

extension DemoMessagesViewController {
    
    func initChatBotAI() {
        let configuration = AIDefaultConfiguration()
        configuration.clientAccessToken = "a0bf2f0015344e34a6bc78cafbfe15f7"
        
        let apiai = ApiAI.shared()
        apiai?.configuration = configuration
    }
    
    func postMessage(message: JSQMessage, completion: @escaping ((BotMessage) -> Void)){
        guard let text = message.text else { return }
        
        let request = ApiAI.shared().textRequest()
        request?.query = text
        
        request?.setMappedCompletionBlockSuccess({ (request, response) in
            let response = response as! AIResponse
            if let message = response.result.fulfillment.speech {
                let botMessage = BotMessage()
                botMessage.response = message
                completion(botMessage)
            }
        }, failure: { (request, error) in
            
        })
        ApiAI.shared().enqueue(request)
        
//        single(param: text) { (botMessage) in
//            completion(botMessage)
//        }
//        completion(single(param: text, completion: <#((BotMessage) -> Void)#>))
//        CMGWebService.shared.load(resource: single(param: text)) { [weak self]  (data, response, error) in
//            guard let strongSelf = self else { return }
//            if error != nil {
//                guard let botMessage = data else { return }
//                completion(botMessage)
//            }
//        }
    }
    func toJSONHelper(_ dictionary:NSMutableDictionary, key:String, value:AnyObject?) {
        if let val = value {
            dictionary[key] = val
        }
    }
    
    func single(param:String, completion: @escaping ((BotMessage) -> Void)){
        var json = NSMutableDictionary()
        toJSONHelper(json, key: "request", value: "hey" as AnyObject?)
        
        let jsonData = try? JSONSerialization.data(withJSONObject: json, options: .prettyPrinted)
        // create post request
        let url = URL(string: EndPoint.getBotMessage.rawValue)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        
//         insert json data to the request
        request.httpBody = jsonData
        
//        self.synchronousUploadTaskWithRequest(request as URLRequest, data: jsonData!, completionHandler: { (data, response, error) in
//            guard let data = data, error == nil else {
//                print(error?.localizedDescription ?? "No data")
//                return
//            }
//            let responseJSON = try? JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions())
//            if let responseJSON = responseJSON as? [String: Any] {
//                print(responseJSON)
//                let botMessage = BotMessage()
//                botMessage.parse(dictionary: responseJSON)
//                completion(botMessage)
//            }
//        })
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {
                print(error?.localizedDescription ?? "No data")
                return
            }
            let responseJSON = try? JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions())
            if let responseJSON = responseJSON as? [String: Any] {
                print(responseJSON)
                let botMessage = BotMessage()
                botMessage.parse(dictionary: responseJSON)
                completion(botMessage)
            }
        }

        task.resume()
        
        
//        return Resource(url: NSURL(string:EndPoint.getBotMessage.rawValue)!, method: .post(jsonData), parseJSON: { json in
//            let botMessage = BotMessage()
//            guard let dictionary = json as? JSONDictionary else { return nil }
//            botMessage.parse(dictionary: dictionary)
//
//            return botMessage
//        })
    }
    
    func synchronousUploadTaskWithRequest(_ request:URLRequest,
                                          data:Data,
                                          completionHandler:@escaping (_ data:Data?, _ response:URLResponse?, _ error:NSError?)->Void) {
        
        // this extention causes the upload task to behave SYNCHRONOSLY so we can add it a an NSOperationQueue
        // without this step nsOperation queue will be tricked to think that the operation finished right way and will start
        // executing other blocks right away.
        let semaphore = DispatchSemaphore(value: 0)
        let task = URLSession.shared.uploadTask(with: request, from: data) { data, response, error in
            
            completionHandler(data, response, error as NSError?)
            semaphore.signal()
        }
        
        task.resume()
        semaphore.wait(timeout: DispatchTime.distantFuture)
    }
}
