/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.spring.config.SecurityConfig
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.security.config.annotation.web.builders.HttpSecurity
 *  org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer$AuthorizedUrl
 *  org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.security.web.SecurityFilterChain
 */
package com.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/admin/**").authenticated()
	            .requestMatchers("/", "/home", "/loginPage", "/profileList", "/profileDetail/**", "/client/**", "/api/check-email", "/api/business/**").permitAll()
	            .anyRequest().permitAll()
	        )
	        .csrf(csrf -> csrf
	            .ignoringRequestMatchers("/api/login") // 이 라인 추가: /api/login 에 대해서만 CSRF 체크 안 함
	        )
	        .formLogin(login -> login
	            .loginPage("/loginPage")
	            .loginProcessingUrl("/login")
	            .defaultSuccessUrl("/admin/profileList", true)
	            .permitAll()
	        )
	        .httpBasic(basic -> basic.disable());

	    return http.build();
	}


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

