package net.achike.visa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import net.achike.visa.api.service.VisaApiService;
import net.achike.visa.entity.Chat;
import net.achike.visa.service.ChatService;

@CrossOrigin(origins={"*"})
@RestController(value="/chat")
public class ChatController {
    
    @Autowired
    VisaApiService visaApiService;
    
    @Autowired
    ChatService chatService;

    @GetMapping(path="/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public String getHelloWorld() {
        return visaApiService.getVisaHelloWorld();
    }
    
    /*
    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Chat chat(@RequestParam(required = true) String request) {
        return chatService.getChat(request);
    }*/
    

    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public Chat chat(@RequestBody Chat chat) {

        return chatService.getChat(chat.getRequest());
    }
    
}
