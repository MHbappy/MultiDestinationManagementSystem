//package com.microservice.gateway;
//
//import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Duration;
//
//@Configuration
//public class NettyServerCustomizerConfig {
//    @Bean
//    public NettyServerCustomizer nettyServerCustomizer() {
//        return httpServer -> httpServer.idleTimeout(Duration.ofMinutes(30));
//    }
//}
//
