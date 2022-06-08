package com.exam.security1.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@SequenceGenerator(
		name= "USER_SEQ"						// 생성기 이름
		,sequenceName = "USER_SEQ"	    // 시퀀스 이름
		,initialValue = 1							// 초기값
		,allocationSize = 1						// 증가값
	)

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;
	
	@CreationTimestamp
	private Timestamp createDate;

	private String provider; // google
	private String providerId; // sub
}
