package com.miniprojet.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.miniprojet.users.entities.Role;
import com.miniprojet.users.entities.User;
import com.miniprojet.users.service.UserService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			// ajouter les rôles
			//userService.addRole(new Role(null, "ADMIN"));
			//userService.addRole(new Role(null, "USER"));
			// ajouter les utilisateurs
			/*userService.saveUser(new User(null, "admin", "123", true, null));
			userService.saveUser(new User(null, "sarra", "123", true, null));
			userService.saveUser(new User(null, "fatma", "123", true, null));
			// affecter les rôles
			userService.addRoleToUser("admin", "ADMIN");
			userService.addRoleToUser("admin", "USER");
			userService.addRoleToUser("sarra", "USER");
			userService.addRoleToUser("fatma", "USER");*/
		};
	}
	
	/*@Bean
	BCryptPasswordEncoder getBCE() {
		return new BCryptPasswordEncoder();
	}*/

}
