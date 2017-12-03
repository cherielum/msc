package net.achike.visa.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.achike.visa.client.auth.BasicAuthHeaderGenerator;
import net.achike.visa.client.auth.XPayTokenGenerator;

@Service
public class VisaApiClientImpl implements VisaApiClient {
    
    private static final Logger LOGGER = LogManager.getLogger(VisaApiClientImpl.class);
    
    private static CloseableHttpClient mutualAuthHttpClient;
    private static CloseableHttpClient XPayHttpClient;
    
    public enum MethodTypes {
        GET, POST, PUT, DELETE
    }
    
    private CloseableHttpClient fetchXPayHttpClient() {
        XPayHttpClient = HttpClients.createDefault();
        return XPayHttpClient;
    }
    
    private CloseableHttpClient fetchMutualAuthHttpClient() throws KeyManagementException, UnrecoverableKeyException,
    NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        if (mutualAuthHttpClient == null) {
            mutualAuthHttpClient = HttpClients.custom().setSSLSocketFactory(getSSLSocketFactory()).build();
        }
        return mutualAuthHttpClient;
    }
    
    private SSLContext loadClientCertificate() throws KeyManagementException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        
        /*
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(loadPfx("classpath:client.pfx", "!Tamena69".toCharArray()), password)
                .loadTrustMaterial(ResourceUtils.getFile("classpath:truststore.jks"), password)
                .build();
        */
        
        //InputStream in = this.getClass().getClassLoader()
        //        .getResourceAsStream("SomeTextFile.txt");
        
        //ResourceUtils.getFile("classpath:unity.jks")
        
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(new URL("https://s3.amazonaws.com/calendar.achike.net/unity.jks"), "!Tamena69".toCharArray(),
                        "!Tamena69".toCharArray())
                .build();
        
        
        //SSLContexts.custom()
        
        //InputStream is = this.getClass().getResourceAsStream("unity.jks");
        
        /*
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile("classpath:unity.jks");
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, "".toCharArray());
        }*/
        
        //new File("classpath:unity.jks")
        //ResourceUtils.getFile("classpath:unity.jks");
        return sslcontext;
    }
    
    private SSLConnectionSocketFactory getSSLSocketFactory() throws KeyManagementException, UnrecoverableKeyException,
    NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(loadClientCertificate(),
                        new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return sslSocketFactory;
    }
    
    private HttpRequest createHttpRequest(MethodTypes methodType, String url) throws Exception {
        HttpRequest request = null;
        switch (methodType) {
        case GET:
            request = new HttpGet(url);
            break;
        case POST:
            request = new HttpPost(url);
            break;
        case PUT:
            request = new HttpPut(url);
            break;
        case DELETE:
            request = new HttpDelete(url);
            break;
        default:
            LOGGER.error("Incompatible HTTP request method " + methodType);
        }
        return request;
   }

    @Override
    public CloseableHttpResponse doMutualAuthRequest(String path, String testInfo, String body, MethodTypes methodType,
            Map<String, String> headers) throws Exception {
       
        String url = "https://sandbox.api.visa.com" + path;
        logRequestBody(url, testInfo, body);
        HttpRequest request = createHttpRequest(methodType, url);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, BasicAuthHeaderGenerator.getBasicAuthHeader());
        request.setHeader("ex-correlation-id", getCorrelationId());
        
        if (headers != null && headers.isEmpty() == false) {
            for (Entry<String, String> header : headers.entrySet()) {
                request.setHeader(header.getKey(), header.getValue());
            }
        }
        
        if (request instanceof HttpPost) {
            ((HttpPost) request).setEntity(new StringEntity(body, "UTF-8"));
        } else if (request instanceof HttpPut) {
            ((HttpPut) request).setEntity(new StringEntity(body, "UTF-8"));
        }
        
        //HttpHost host = new HttpHost("VisaProperties.getProperty(Property.END_POINT)");
        CloseableHttpResponse response = fetchMutualAuthHttpClient().execute((HttpUriRequest) request);
        logResponse(response);
        return response;
    }
    
    @Override
    public CloseableHttpResponse doXPayTokenRequest(String baseUri, String resourcePath, String queryParams,
            String testInfo, String body, MethodTypes methodType, Map<String, String> headers) throws Exception {
        
        String url = "https://sandbox.api.visa.com" + baseUri + resourcePath + "?" + queryParams;
        logRequestBody(url, testInfo, body);
        
        String apikey = "N1FCIMRNUNEZKAVI703G21EiY0qevW2A1qFFUcZAmpdyis3XM";
        
        HttpRequest request = createHttpRequest(methodType, url);
        request.setHeader("content-type", "application/json");
        String xPayToken = XPayTokenGenerator.generateXpaytoken(resourcePath, "apikey=" + apikey, body);
        request.setHeader("x-pay-token", xPayToken);
        //request.setHeader("ex-correlation-id", getCorrelationId());
        
        if (headers != null && headers.isEmpty() == false) {
            for (Entry<String, String> header : headers.entrySet()) {
                request.setHeader(header.getKey(), header.getValue());
            }
        }
        
        if (request instanceof HttpPost) {
            ((HttpPost) request).setEntity(new StringEntity(body, "UTF-8"));
        } else if (request instanceof HttpPut) {
            ((HttpPut) request).setEntity(new StringEntity(body, "UTF-8"));
        }
        
        CloseableHttpResponse response = fetchXPayHttpClient().execute((HttpUriRequest) request);
        logResponse(response);
        return response;
    }
    
    private static void logResponse(CloseableHttpResponse response) throws IOException {
        Header[] h = response.getAllHeaders();
        
        // Get the response json object
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        // Print the response details
        HttpEntity entity = response.getEntity();
        LOGGER.info("Response status : " + response.getStatusLine() + "\n");
        
        LOGGER.info("Response Headers: \n");
        
        for (int i = 0; i < h.length; i++) {
            LOGGER.info(h[i].getName() + ":" + h[i].getValue());
        }
        
        LOGGER.info("\n Response Body:");
        
        if(!StringUtils.isEmpty(result.toString())) {
            ObjectMapper mapper = getObjectMapperInstance();
            Object tree;
            try {
                tree = mapper.readValue(result.toString(), Object.class);
                LOGGER.info("ResponseBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        
        EntityUtils.consume(entity);
    }
    
    private static void logRequestBody(String URI, String testInfo, String payload) {
        ObjectMapper mapper = getObjectMapperInstance();
        Object tree;
        LOGGER.info("URI: " + URI);
        LOGGER.info(testInfo);
        if(!StringUtils.isEmpty(payload)) {
            try {
                tree = mapper.readValue(payload,Object.class);
                LOGGER.info("RequestBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
    
    /**
     * Get Correlation Id for the API Call.
     * @return correlation Id  
     */
    private String getCorrelationId() {
        //Passing correlation Id header is optional while making an API call. 
        //return RandomStringUtils.random(10, true, true) + "_SC";
        
        Random rn = new Random();
        int min = 10000;
        int max = 10000000;
        int randomInt = rn.nextInt(max - min + 1) + min;
        
        return ""+randomInt;
    }
    /**
     * Get New Instance of ObjectMapper
     * @return
     */
    protected static ObjectMapper getObjectMapperInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // format json
        return mapper;
    }

}
