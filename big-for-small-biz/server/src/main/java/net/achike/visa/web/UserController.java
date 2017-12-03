package net.achike.visa.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.achike.visa.api.service.VisaApiService;
import net.achike.visa.dto.UserDto;
import net.achike.visa.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    VisaApiService visaApiService;
    
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @PostMapping("/login")
    public UserDto login(@RequestBody UserDto user) {
        return userService.login(user);
    }
    
    @PostMapping("/{username}/pay")
    public Boolean payment(@PathVariable String username) {

        if(visaApiService.payVisaDirect(username) != null) {
            return true;
        }
        
        
        return false;
    }
    
}
