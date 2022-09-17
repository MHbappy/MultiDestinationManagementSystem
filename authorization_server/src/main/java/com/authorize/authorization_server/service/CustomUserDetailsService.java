package com.authorize.authorization_server.service;

import com.authorize.authorization_server.domain.Destination;
import com.authorize.authorization_server.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service(value = "userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersService usersService;

    public CustomUserDetailsService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Users> optionalUser = usersService.findByUsername(s);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        log.info("additional_param: " + request.getParameter("city"));

        String manager = request.getParameter("manager");

       // String roleName = optionalUser.get().getRoles().stream().findFirst().get().getName();

        if (optionalUser.get().getRoles().stream().findFirst().get().getName().toString().equals("USER") && manager.equals("y")){
            throw new UsernameNotFoundException("Your are not permited!");
        }
//        if (manager.equals("y")){
//            throw new UsernameNotFoundException("Your are not permited!");
//        }

//        Set<Destination> cities = optionalUser.get() != null ?  optionalUser.get().getCities() : new HashSet<>();
//        boolean isCityexits = false;
//        for (Destination cit: cities) {
//            log.info(cit.getName());
//            if (cit.getName().equals(city)){
//                isCityexits = true;
//                //throw new UsernameNotFoundException("City is not found");
//                break;
//            }
//        }
//
//        if (!isCityexits){
//            throw new UsernameNotFoundException("City is not found");
//        }

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(optionalUser.get());
    }
}
