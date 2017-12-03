package net.achike.visa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.achike.visa.entity.User;

@Repository
public interface UserRepo  extends JpaRepository<User, Integer> {

    User findOneByUsername(String name);
    
}
