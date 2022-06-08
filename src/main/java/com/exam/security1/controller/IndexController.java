package com.exam.security1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exam.security1.auth.PrincipalDetails;
import com.exam.security1.model.User;
import com.exam.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // view >> mustache : 스프링이 권장하는 템플릿 엔진
public class IndexController {

	private final UserRepository userRepository;
	private final  BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/*
	 * 스프링 시큐리티 세션의 Authentication 객체의 타입
	 * 1. UserDetails( == PrincipalDetails) : 일반 로그인
	 * 2. OAuth2User : OAuth2 로그인
	 */
	
	@GetMapping("/test/login")
	public @ResponseBody String loginTest( Authentication authentication, 
															@AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("/test/login");
		PrincipalDetails principalDetails =  (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : "+principalDetails.getUser()); 
		
		System.out.println("userDetails : "+userDetails.getUser());
		return "세션 정보 확인";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String oauthLoginTest( Authentication authentication,
																	@AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/login");
		OAuth2User oauth2User =  (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : "+oauth2User.getAttributes()); 
		
		System.out.println(oauth.getAttributes());
		return "세션 정보 확인";
	}
	
	@GetMapping({"","/"})
	public String index() {
		// 머스태치 기본 폴더 src/main/resources/
		return "index"; //   src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : "+principalDetails);
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}	
	
	
	@GetMapping("/loginForm") // SecurityConfig 파일 생성 후 /login 작동 안 함
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm"; 
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER"); // 원래는 service에서 해야 함
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") // 특정 메서드에 권한 부여
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') OR hasRole('ROLE_ADMIN')") // data() 전에 실행. 2개 이상의 권한 부여에 사용
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data";
	}	
	
}
