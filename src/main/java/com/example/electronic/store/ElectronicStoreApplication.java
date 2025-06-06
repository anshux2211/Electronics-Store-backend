package com.example.electronic.store;

import com.example.electronic.store.entities.Role;
import com.example.electronic.store.entities.User;
import com.example.electronic.store.repositories.RoleRepository;
import com.example.electronic.store.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepositories user_repo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Role role=new Role();
		role.setId(UUID.randomUUID().toString());
		role.setName("ROLE_ADMIN");

		Role role1=new Role();
		role1.setId(UUID.randomUUID().toString());
		role1.setName("ROLE_NORMAL");

		Role tmprole=roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if(tmprole==null){
			roleRepository.save(role);
		}

		tmprole=roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if(tmprole==null){
			roleRepository.save(role1);
		}


		User user=user_repo.findByEmail("virat@gmail.com").orElse(null);
		if(user==null) {
			user=new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setImageName("user1.jpg");
			user.setAbout("Cricketer");
			user.setGender("Male");
			user.setName("Virat Kohli");
			user.setEmail("virat@gmail.com");
			user.setPhone("9190221356");
			user.setPassword(passwordEncoder.encode("virat"));
			user.setRoleList(List.of(role));
			user_repo.save(user);
		}
	}
}
