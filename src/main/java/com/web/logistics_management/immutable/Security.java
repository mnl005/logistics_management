package com.web.logistics_management.immutable;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 인증 없이 접근 가능하도록 설정
                );


        return http.build();
    }
}