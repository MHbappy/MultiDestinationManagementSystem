package com.microservice.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Collections;

@Configuration
public class GatewayConfig {
//    @Bean
//    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("rastarant-bar", r -> r.path("/api/**")
//                        .filters(f -> f
//                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
//                        .uri("http://localhost:8084"))
//                .build();
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*"));
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}
