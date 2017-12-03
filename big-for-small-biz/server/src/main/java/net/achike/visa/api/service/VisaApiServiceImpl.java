package net.achike.visa.api.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.achike.visa.api.dto.AddressDto;
import net.achike.visa.api.dto.CardAcceptorDto;
import net.achike.visa.api.dto.CardDto;
import net.achike.visa.api.dto.PaymentDto;
import net.achike.visa.api.dto.PushFundsDto;
import net.achike.visa.client.VisaApiClient;
import net.achike.visa.client.VisaApiClientImpl;
import net.achike.visa.client.VisaApiClientImpl.MethodTypes;
import net.achike.visa.dao.CardRepo;
import net.achike.visa.dao.UserRepo;
import net.achike.visa.entity.Card;
import net.achike.visa.entity.User;

@Service
public class VisaApiServiceImpl implements VisaApiService {

    private static final Logger LOGGER = LogManager.getLogger(VisaApiServiceImpl.class);
    
    @Autowired
    VisaApiClient visaApiClient;
    
    @Autowired
    UserRepo userRepo;
    
    @Autowired
    CardRepo cardRepo;

    @Override
    public String getVisaHelloWorld() {
        
        CloseableHttpResponse response = null;
        
        try {
            response = visaApiClient.doMutualAuthRequest("/vdp/helloworld", null, null, VisaApiClientImpl.MethodTypes.GET, null);
            
            LOGGER.info("The result of Hello World -> {}" + response);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return response.toString();
    }
    
    @Override
    public Boolean pay(String username) {
        
        String queryString = "apikey=" + "N1FCIMRNUNEZKAVI703G21EiY0qevW2A1qFFUcZAmpdyis3XM";
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        User user = userRepo.findOneByUsername(username);
        
        Card card = user.getCards().iterator().next();
        CardDto cardDto = cardEntityToDto(card);
        //Card card = cardRepo.findByUserId(user.getId());
        
        PaymentDto pay = new PaymentDto();
        
        pay.setAmount("0");
        pay.setCurrency("USD");
        pay.setPayment(cardDto);
        
        CloseableHttpResponse response = null;
        try {
            String payJson = objectMapper.writeValueAsString(pay);
            LOGGER.info("Achike's DEBUG: Payment JSON -> " + payJson);
            response = visaApiClient.doXPayTokenRequest("/cybersource/", "payments/v1/authorizations", queryString,
                    "Payment Authorization Test", payJson, MethodTypes.POST, new HashMap<String, String>());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public Boolean payVisaDirect(String username) {
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        User user = userRepo.findOneByUsername(username);
        
        PushFundsDto pushFundsDto = new PushFundsDto();
        pushFundsDto.setAmount("124.05");
        pushFundsDto.setRecipientPrimaryAccountNumber("4123640062698797");
        pushFundsDto.setRetrievalReferenceNumber("430000367618");
        
        pushFundsDto.setMerchantCategoryCode("6012");
        
        pushFundsDto.setSenderName(user.getFirst() + " " + user.getLast());
        pushFundsDto.setSenderReference("1234");
        
        pushFundsDto.setSenderAccountNumber("4541237895236");
        pushFundsDto.setSystemsTraceAuditNumber("313042");
        pushFundsDto.setTransactionIdentifier("381228649430015");
        
        pushFundsDto.setLocalTransactionDateTime(getStartDate());
        
        CardAcceptorDto cardAcceptor = new CardAcceptorDto();
        cardAcceptor.setIdCode("ID-Code123");
        cardAcceptor.setName("Card Accpector ABC");
        cardAcceptor.setTerminalId("365529");
        
        AddressDto address = new AddressDto();
        
        cardAcceptor.setAddress(address);
        
        pushFundsDto.setCardAcceptor(cardAcceptor);
        
        try {
            String pushFundsJson = objectMapper.writeValueAsString(pushFundsDto);
            
            String baseUri = "/visadirect/";
            String resourcePath = "mvisa/v1/cashinpushpayments";

            CloseableHttpResponse response = visaApiClient.doMutualAuthRequest(baseUri + resourcePath, "M Visa Transaction Test", pushFundsJson, MethodTypes.POST, new HashMap<String, String>());
            response.close();
            
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private CardDto cardEntityToDto(Card card) {
        
        CardDto cardDto = new CardDto();
        
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setCardExpirationMonth(card.getCardExpirationMonth());
        cardDto.setCardExpirationYear(card.getCardExpirationYear());
        
        return cardDto;
    }
    
    private String getStartDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        sdfDate.setTimeZone(utc);
        Date now = new Date();
        String strDate = sdfDate.format(now);
        
        return strDate;
    }
    
}
