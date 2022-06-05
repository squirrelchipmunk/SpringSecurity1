package com.exam.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록 >> 최신버전에서는 사용하지 않는 방법
public class SecurityConfig{

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
        	.antMatchers("/user/**").authenticated()
        	.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN' OR hasRole('ROLE_MANAGER')")
        	.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')")
        	.anyRequest().permitAll()
        	.and()
        	.formLogin()
        	.loginPage("/login");
        	
        return http.build();
    }
}
