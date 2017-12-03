package net.achike.visa.api.service;

public interface VisaApiService {
    
    String getVisaHelloWorld();    
    Boolean pay(String username);
    
    Boolean payVisaDirect(String username);
}
