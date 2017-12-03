package net.achike.visa.client;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;

public class HttpPayResult {

    private CloseableHttpResponse closeableHttpResponse;
    private SuccessfulTransaction transaction;
    
    public CloseableHttpResponse getCloseableHttpResponse() {
        return closeableHttpResponse;
    }
    public void setCloseableHttpResponse(CloseableHttpResponse closeableHttpResponse) {
        this.closeableHttpResponse = closeableHttpResponse;
    }
    public SuccessfulTransaction getTransaction() {
        return transaction;
    }
    public void setTransaction(SuccessfulTransaction transaction) {
        this.transaction = transaction;
    }
    
    public void close() throws IOException {
        if (closeableHttpResponse != null) {
            closeableHttpResponse.close();
        }
    }
    
}
