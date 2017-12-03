package net.achike.visa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import net.achike.visa.entity.Chat;

public interface ChatRepo extends JpaRepository<Chat,Integer> {
    
    Chat findOneByRequest(String request);
}
