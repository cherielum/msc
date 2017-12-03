package net.achike.visa.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.achike.visa.dao.UserRepo;
import net.achike.visa.dto.UserDto;
import net.achike.visa.entity.User;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepo userRepo;

    @Override
    public List<UserDto> getAllUsers() {
        
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        
        for (User user : users) {
            userDtos.add(convertUserEntityToDto(user));
        }
        
        return userDtos;
    }

    @Override
    public UserDto login(UserDto userDto) {
        User user = userRepo.findOneByUsername(userDto.getUsername());
        return convertUserEntityToDto(user);
    }
    
    private UserDto convertUserEntityToDto(User user) {
        
        UserDto userDto = new UserDto();
        
        userDto.setEmail(user.getEmail());
        userDto.setFirst(user.getFirst());
        userDto.setLast(user.getLast());
        
        return userDto;
    }
}
