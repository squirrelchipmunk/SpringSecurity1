package com.exam.security1.config.oauth;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.exam.security1.auth.PrincipalDetails;
import com.exam.security1.config.oauth.provider.FacebookUserInfo;
import com.exam.security1.config.oauth.provider.GoogleUserInfo;
import com.exam.security1.config.oauth.provider.NaverUserInfo;
import com.exam.security1.config.oauth.provider.OAuth2UserInfo;
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
		System.out.println("OAuth2.0 로그인 시도");
		System.out.println("userRequest : "+userRequest.getClientRegistration()); // 어떤 OAuth로 로그인 했는지..(구글, 페이스북)
		System.out.println("userRequest : "+userRequest.getAccessToken().getTokenValue());
		
		
		OAuth2User oauth2User= super.loadUser(userRequest);
		// 구글 로그인 클릭 -> 구글 로그인창 -> 로그인 -> code리턴 -> AccessToken 요청
		// userRequest -> loadUser() -> 회원 프로필
		System.out.println("userRequest : "+oauth2User.getAttributes());
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}
		else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}
		else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}
		else {
			System.out.println("구글과 페이스북과 네이버만 지원합니다.");
		}
		
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("비밀번호");
		String email = oAuth2UserInfo.getEmail();
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
