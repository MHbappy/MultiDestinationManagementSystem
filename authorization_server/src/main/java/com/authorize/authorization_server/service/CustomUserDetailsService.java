package com.authorize.authorization_server.service;

import com.authorize.authorization_server.domain.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersService usersService;

    public CustomUserDetailsService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Users> optionalUser = usersService.findByUsername(s);

        System.out.println("");
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(optionalUser.get());
    }
}
