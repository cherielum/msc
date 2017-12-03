//
//  BotMessage.swift
//  JSQMessages
//
//  Created by Mickens, Maurice (CMG-Atlanta) on 12/2/17.
//  Copyright © 2017 Hexed Bits. All rights reserved.
//

import Foundation

@objc class BotMessage:NSObject {
    
    var response:String!
    
    func parse(dictionary: [String :Any]){
        // TODO: Throw an error if invalid key is passed in to the dictionary from the server
        guard let responseMessage = dictionary["response"] as? String else { return }
        self.response = responseMessage
    }
}
