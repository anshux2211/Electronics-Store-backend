package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Autowired
	private UserRepositories user_repo;
	@Autowired
	private JwtHelper jwt_helper;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){
		User user=user_repo.findByEmail("akash@gmail.com").orElse(null);
		if(user!=null) {
			String token = jwt_helper.generateToken(user);
			System.out.println("Generated Token: " + token);
			System.out.println("Username: " + jwt_helper.getUsernameFromToken(token));
			System.out.println("Is Token Expired: " + jwt_helper.isTokenExpired(token));
		}
	}

}
