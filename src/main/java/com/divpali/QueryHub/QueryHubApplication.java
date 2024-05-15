package com.divpali.QueryHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class QueryHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryHubApplication.class, args);
	}

	//We will call the methods on this bean when we need to hash a password.
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
