package net.achike.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.achike.visa.dao.ChatRepo;
import net.achike.visa.entity.Chat;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    ChatRepo chatRepo;
    
    @Override
    public Chat getChat(String request) {
        
        return chatRepo.findOneByRequest(request);
    }

}
