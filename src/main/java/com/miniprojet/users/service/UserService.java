package com.miniprojet.users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.miniprojet.users.entities.Role;
import com.miniprojet.users.entities.User;

@Service
public interface UserService {
	
	User saveUser(User user);

	User findUserByUsername(String username);

	Role addRole(Role role);

	User addRoleToUser(String username, String rolename);
	
	List<User> findAllUsers();
}
