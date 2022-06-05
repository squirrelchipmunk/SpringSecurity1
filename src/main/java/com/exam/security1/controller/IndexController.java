package com.exam.security1.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exam.security1.model.User;
import com.exam.security1.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // view >> mustache : 스프링이 권장하는 템플릿 엔진
public class IndexController {

	private final UserRepository userRepository;
	private final  BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({"","/"})
	public String index() {
		// 머스태치 기본 폴더 src/main/resources/
		return "index"; //   src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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
	
	
	
}
