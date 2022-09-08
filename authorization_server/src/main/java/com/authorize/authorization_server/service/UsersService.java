package com.authorize.authorization_server.service;

import com.authorize.authorization_server.domain.Users;
import com.authorize.authorization_server.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public Optional<Users> findByUsername(String email) {
        return usersRepository.findByUsername(email);
    }
}
