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
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 홈 경로 접근 허용
                        .anyRequest().authenticated() // 그 외 경로는 인증 필요
                );

        return http.build();
    }
}