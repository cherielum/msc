package net.achike.visa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.achike.visa.entity.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Integer> {

    Card findByUserId(Integer userId);
    
}
