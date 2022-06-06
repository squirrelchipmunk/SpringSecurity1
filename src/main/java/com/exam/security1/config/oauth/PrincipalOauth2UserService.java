package com.exam.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	//구글로부터 받은 userRequest 데이터에 대한 후처리 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest : "+userRequest.getClientRegistration()); // 어떤 OAuth로 로그인 했는지..(구글, 페이스북)
		System.out.println("userRequest : "+userRequest.getAccessToken().getTokenValue());
		
		// 구글 로그인 클릭 -> 구글 로그인창 -> 로그인 -> code리턴 -> AccessToken 요청
		// userRequest -> loadUser() -> 회원 프로필
		System.out.println("userRequest : "+super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User= super.loadUser(userRequest);
		
		
		return super.loadUser(userRequest);
	}
}
