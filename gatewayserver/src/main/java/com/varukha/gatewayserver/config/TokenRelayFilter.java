//package com.varukha.gatewayserver.config;
//
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//
//@Configuration
//public class TokenRelayFilter {
//    private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
//    private static final String BEARER = "Bearer ";
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public GlobalFilter customFilter() {
//        return (exchange, chain) -> {
//            String authorizationHeader = exchange
//                    .getRequest()
//                    .getHeaders()
//                    .getFirst(AUTHORIZATION_HEADER);
//            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
//                String token = authorizationHeader.substring(BEARER.length());
//                exchange.getRequest()
//                        .mutate()
//                        .header(AUTHORIZATION_HEADER, BEARER + token);
//            }
//            return chain.filter(exchange);
//        };
//    }
//}
