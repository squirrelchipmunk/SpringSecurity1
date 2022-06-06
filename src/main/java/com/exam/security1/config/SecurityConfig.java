package com.exam.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.exam.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록 >> 최신버전에서는 사용하지 않는 방법
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig{

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
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
        	.and()
        	.oauth2Login()
        	.loginPage("/loginForm")  // 구글 로그인 후처리 필요 (카카오 로그인과 차이점 -> 엑세스토큰+사용자프로필 )
        	.userInfoEndpoint()
        	.userService(principalOauth2UserService);
        	;
        	
        return http.build();
    }
}

// ouath2 : 1. 코드 받기(인증 완료)     2. 엑세스 토큰(사용자 정보 접근 권한 획득)      3. 사용자 프로필 정보      4. 정보를 토대로 자동 회원가입(or 추가 정보를 입력하여 수동 회원가입) 및 로그인
