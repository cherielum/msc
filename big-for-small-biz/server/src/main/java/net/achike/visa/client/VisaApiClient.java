package net.achike.visa.client;

import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import net.achike.visa.client.VisaApiClientImpl.MethodTypes;

public interface VisaApiClient {

    CloseableHttpResponse doMutualAuthRequest(String path, String testInfo, String body, MethodTypes methodType,
            Map<String, String> headers) throws Exception;
    
    CloseableHttpResponse doXPayTokenRequest(String baseUri, String resourcePath, String queryParams,
            String testInfo, String body, MethodTypes methodType, Map<String, String> headers) throws Exception;
    
}
