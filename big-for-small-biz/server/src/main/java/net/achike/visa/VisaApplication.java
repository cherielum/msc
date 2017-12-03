package net.achike.visa;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisaApplication {

    private static final Logger LOGGER = LogManager.getLogger(VisaApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(VisaApplication.class, args);
    }
    
    /*
    @Bean
    public RestTemplate restTemplate() {
        return null;
        //return new RestTemplate(new HttpComponentsClientHttpRequestFactory(createHttpClient(applicationKeyAlias)));
    }
    
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        char[] password = "password".toCharArray();

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore("classpath:cert.jks", password), password)
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        return builder
                .requestFactory(new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }

     private KeyStore keyStore(String file, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile(file);
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, password);
        }
        return keyStore;
    }
    
    
    private HttpClient createHttpClient(final String keyAlias) {
    
        LOGGER.info("Creating HTTP client using keystore={} and alias={}", keystorePath, keyAlias);
        
        final KeyStore trustStore = new KeyStoreFactoryBean(
            makeResource("classpath:/truststore.jks"), "JKS", "changeit").newInstance();
            
        final KeyStore keyStore = new KeyStoreFactoryBean(
            makeResource(keystorePath), keystoreType, keystorePassword).newInstance();
            
        final SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> keyAlias)
                .loadTrustMaterial(trustStore, (x509Certificates, s) -> false)
                .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            throw new IllegalStateException("Error loading key or trust material");
        }
        
        final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
            sslContext,
            new String[] { "TLSv1.2" },
            null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslSocketFactory)
            .build();
        
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpClientPoolSize);
        connectionManager.setDefaultMaxPerRoute(httpClientPoolSize);
        
        return HttpClients.custom()
            .setSSLSocketFactory(sslSocketFactory)
            .setConnectionManager(connectionManager)
            .build();
    }*/
    
}
