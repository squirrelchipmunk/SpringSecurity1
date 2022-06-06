package com.exam.security1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exam.security1.model.User;
import com.exam.security1.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수 실행

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // input 태그 name = username으로 하거나 시큐리티 설정에 .usernameParameter() 추가
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity); // --> 시큐리티 session ( Authentication ( Principal Details ) )
		}
		return null;
	}
}
