package net.achike.visa.client.auth;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;

public class BasicAuthHeaderGenerator {
    
    public static String getBasicAuthHeader() {
        String userId = "FIVMYH8X2TLHUIDCQC1J21M6zhIBZr0XVEAhxRD7WqXVMc_xg";
        String password = "0GdmhwMMTC0yz6wAZw4N";
        return "Basic " + base64Encode(userId + ":" + password);
    }

    private static String base64Encode(String token) {
        byte[] encodedBytes = Base64.encodeBase64(token.getBytes());
        return new String(encodedBytes, Charset.forName("UTF-8"));
    }

}
