package com.multidestination.restaurantbar_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableEurekaClient
@EnableResourceServer
@SpringBootApplication
public class RestaurantBarServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantBarServerApplication.class, args);
    }

}
