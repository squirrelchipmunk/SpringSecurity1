package com.exam.security1.config.oauth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.exam.security1.auth.PrincipalDetails;
import com.exam.security1.model.User;
import com.exam.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
	//구글로부터 받은 userRequest 데이터에 대한 후처리 함수
	//함수 종료 시 @AuthenticationPrincipal 어노테이션 만들어진다
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest : "+userRequest.getClientRegistration()); // 어떤 OAuth로 로그인 했는지..(구글, 페이스북)
		System.out.println("userRequest : "+userRequest.getAccessToken().getTokenValue());
		
		
		OAuth2User oauth2User= super.loadUser(userRequest);
		// 구글 로그인 클릭 -> 구글 로그인창 -> 로그인 -> code리턴 -> AccessToken 요청
		// userRequest -> loadUser() -> 회원 프로필
		System.out.println("userRequest : "+oauth2User.getAttributes());
		
		String provider = userRequest.getClientRegistration().getClientId(); // google, facebook 등
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("비밀번호");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) { // 기존회원이 아니면
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
