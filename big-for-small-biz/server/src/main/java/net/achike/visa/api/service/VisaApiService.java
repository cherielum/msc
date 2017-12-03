package net.achike.visa.api.service;

import net.achike.visa.client.HttpPayResult;

public interface VisaApiService {
    
    String getVisaHelloWorld();
    Boolean pay(String username);
    
    HttpPayResult payVisaDirect(String username);
}
