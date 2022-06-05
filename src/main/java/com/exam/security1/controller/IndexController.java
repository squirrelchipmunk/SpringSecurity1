package com.exam.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // view >> mustache : 스프링이 권장하는 템플릿 엔진
public class IndexController {

	@GetMapping
	public String index() {
		// 머스태치 기본 폴더 src/main/resources/
		return "index"; //   src/main/resources/templates/index.mustache
	}
	
}
