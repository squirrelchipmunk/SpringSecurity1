package com.exam.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view >> mustache : 스프링이 권장하는 템플릿 엔진
public class IndexController {

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
	
	
	@GetMapping("/login") // SecurityConfig 파일 생성 후 /login 작동 안 함
	public @ResponseBody String login() {
		return "login";
	}
	
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료";
	}
	
	
}
