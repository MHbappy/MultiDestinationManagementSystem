package com.authorize.authorization_server.controller;
import com.authorize.authorization_server.domain.ERole;
import com.authorize.authorization_server.domain.Roles;
import com.authorize.authorization_server.domain.Users;
import com.authorize.authorization_server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RegistrationResource {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public Users userRegistration(@RequestBody Users users){
        Optional<Users> findByUsername = usersRepository.findByUsername(users.getUsername());
        if (findByUsername.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate User!");
        }
        if (users.getRoles().size() == 1){
            Optional<Roles> roles = users.getRoles().stream().findFirst();
            if (roles.isPresent() && roles.get().getName().equals(ERole.USER) && users.getCities().size() > 1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "With user registration, you can not registration with multi cities");
            }
//            if (roles.equals())
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);
        return savedUser;
    }

    @GetMapping("/user-list")
    public List<Users> usersList(){
        return usersRepository.findAll();
    }


}