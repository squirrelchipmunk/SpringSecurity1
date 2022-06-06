package com.exam.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록 >> 최신버전에서는 사용하지 않는 방법
public class SecurityConfig{

	@Bean // 리턴 메서드를 IoC로 등록
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
        	.antMatchers("/user/**").authenticated() // 인증만 되면(로그인 하면)
        	.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
        	.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
        	.anyRequest().permitAll()
        	.and()
        	.formLogin()
        	.loginPage("/loginForm")
        	.loginProcessingUrl("/login") // /login 호출되면 시큐리티가 대신 로그인 진행
        	.defaultSuccessUrl("/") // 로그인페이지에서 로그인을 하면 /로 이동하지만 다른 요청을 통해 로그인을 하면 해당 요청으로 이동
        	;
        	
        return http.build();
    }
}
