package net.achike.visa.service;

import java.util.List;
import net.achike.visa.dto.UserDto;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto login(UserDto user);
}
