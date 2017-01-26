package com.radioteria.web.security;

import com.radioteria.db.entities.User;
import com.radioteria.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EntityUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public EntityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User loadedUser = userService.findByEmail(s);
        if (loadedUser == null) {
            throw new UsernameNotFoundException(String.format("User with email \"%s\" does not exist.", s));
        }
        return new TheUserDetails(loadedUser);
    }

}
