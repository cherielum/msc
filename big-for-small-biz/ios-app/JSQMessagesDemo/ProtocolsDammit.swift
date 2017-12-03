//
//  CMGDataProtocols.swift
//  NewspaperBreakingNews
//
//  Created by Mickens, Maurice (CMG-Atlanta) on 8/3/17.
//  Copyright © 2017 Cox Media Group, Inc. All rights reserved.
//

import Foundation

@objc protocol CMGDataScraper {}
@objc protocol CMGDataManipulationProtocol {}

extension CMGDataScraper {
    
    func scrapeUUID(regex: NSRegularExpression, url: String) -> String {
        var uuid = ""
        let matches = regex.matches(in: url, options: [], range: NSRange(location: 0, length: url.characters.count))
        matches.forEach({ (result) in
            let urlString = url as NSString
            if result.rangeAt(0).location != NSNotFound {
                uuid = urlString.substring(with: result.rangeAt(0))
            }
        })
        
        return uuid
    }
}

extension CMGDataManipulationProtocol {
    
    func stringFromData(_ data: Data?) -> String? {
        guard let receivedData = data,
            let datastring = NSString(data:receivedData, encoding:String.Encoding.utf8.rawValue) as String? else { return nil }
        
        return datastring
    }
    
    func imageFromBase64String(_ string: String) -> UIImage? {
        guard let data: Data = Data(base64Encoded: string, options: NSData.Base64DecodingOptions.ignoreUnknownCharacters),
            let image = UIImage(data: data) else { return nil }
        
        return image
    }
    
    func parseJSONResponse(_ response: Data)->NSDictionary? {
        do {
            let rawObject = try JSONSerialization.jsonObject(with: response, options: JSONSerialization.ReadingOptions.allowFragments)
            if let dictionary =  rawObject as? NSDictionary {
                return dictionary
            }
        } catch let error as NSError {
            NSLog("error parsing JSON %@", error)
        }
        return nil
        
    }
    
}

protocol WebServiceProtocol {}

extension WebServiceProtocol {
    func imageForUrl(urlString: String, completion: @escaping (UIImage) -> Void) {
        let url = NSURL(string: urlString)
        let task = URLSession.shared.dataTask(with: url! as URL) { data, _, error in
            if error == nil {
                if let data = data, let image = UIImage(data: data) {
                    completion(image)
                }
            }
        }
        task.resume()
    }
    
    func handleError(_ response: URLResponse?, error: Error?, data: Data?) {
        var content: String = ""
        if let bytes = data {
            if let datastring = NSString(data:bytes, encoding:String.Encoding.utf8.rawValue) as String? {
                content = datastring
            }
        }
        
        if let err = error {
            print("\(err) \n \(content)")
        }
        
        if let  httpResponse: HTTPURLResponse = response as? HTTPURLResponse {
            let statusCode = httpResponse.statusCode
            if statusCode != 200 {
                print("status: \(statusCode) received with response")
                if let url = response?.url?.absoluteString {
                    print("fetch to: \(url) failed with message \(content)")
                }
            }
        }
    }
}
