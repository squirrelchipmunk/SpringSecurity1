package com.exam.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.security1.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	// findByㅡ
	// select * from user where username = 1?
	public User findByUsername(String username);
	
}
