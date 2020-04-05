package com.sc.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;

@Configuration
public class MyLimitConfig {

    @Bean
    KeyResolver limitKeyResolver() {
        return exchange -> Mono.just(
                Optional.ofNullable(exchange.getRequest().getRemoteAddress()).
                        orElse(InetSocketAddress.createUnresolved("127.0.0.1", 80)).
                        getHostName()
        );
    }

}
