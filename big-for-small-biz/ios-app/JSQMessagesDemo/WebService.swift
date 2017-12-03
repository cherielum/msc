//
//  CMGWebService.swift
//  NewspaperBreakingNews
//
//  Created by Mickens, Maurice (CMG-Atlanta) on 10/8/17.
//  Copyright © 2017 Cox Media Group, Inc. All rights reserved.
//

import Foundation
import UIKit

// swiftlint:disable force_try
// swiftlint:disable variable_name
typealias JSONDictionary = [String: AnyObject]

public enum Result<T> {
    case success(T)
}

public typealias SessionId = String

enum HttpMethod<Body> {
    case get
    case post(Body)
}

/* A Resource bundles all the information we neeed to make a request*/
public struct Resource<A> {
    var url: NSURL
    let method: HttpMethod<Data>
    let parse: (Data) -> A?
    let parameter: String!
}

/* Handle all initialization requirements of our Resource properties*/
extension Resource {
    init(url: NSURL?, method: HttpMethod<Any> = .get, parseJSON: @escaping (Any) -> A?) {
        if let url = url {
            self.url = url
        } else {
            self.url = NSURL(string: "")!
        }
        self.method = method.map { json in
            try! JSONSerialization.data(withJSONObject: json, options: [])
        }
        self.parse = { data in
            let json = try? JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions())
            return json.flatMap(parseJSON)
        }
        self.parameter = ""
    }
}

extension HttpMethod {
    var method: String {
        switch self {
        case .get: return "GET"
        case .post: return "POST"
        }
    }
}

extension HttpMethod {
    func map<B>(f: (Body) -> B) -> HttpMethod<B> {
        switch self {
        case .get: return .get
        case .post(let body):
            return .post(f(body))
        }
    }
}

extension NSMutableURLRequest {
    convenience init<A>(resource: Resource<A>) {
        self.init()
        timeoutInterval = 30.0
        if let resourceUrl = resource.url as URL? {
            url = resourceUrl
        }
        httpMethod = resource.method.method
        addValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        addValue("application/json", forHTTPHeaderField: "Accept")
        if case let .post(data) = resource.method {
            httpBody = data
        }
    }
}

final class CMGWebService: NSObject, CMGDataManipulationProtocol, WebServiceProtocol {
    public static let shared = CMGWebService()
    var simulatedStartDate: Date?
    
    override init() {
    }
    
    /// This is a Generic method used to load a resource from the network
    ///
    /// - Parameters:
    ///   - resource: Bundles all the information we need to make a request to the server
    ///   - completion: A Generic is used here to return data, an error, or nil value
    func load<A>(resource: Resource<A>, completion: @escaping (A?, HTTPURLResponse?, Error?) -> Void) {
        let request = NSMutableURLRequest(resource: resource)
        URLSession.shared.dataTask(with: request as URLRequest) { data, response, error in
            completion(data.flatMap(resource.parse), response as? HTTPURLResponse, error)
            }.resume()
    }
}
