package com.miniprojet.users.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniprojet.users.entities.Role;
import com.miniprojet.users.entities.User;
import com.miniprojet.users.exception.EmailAlreadyExistsException;
import com.miniprojet.users.exception.ExpiredTokenException;
import com.miniprojet.users.register.RegistationRequest;
import com.miniprojet.users.register.VerificationToken;
import com.miniprojet.users.register.VerificationTokenRepository;
import com.miniprojet.users.repos.RoleRepository;
import com.miniprojet.users.repos.UserRepository;
import com.miniprojet.users.util.EmailService;
import com.miniprojet.users.exception.InvalidTokenException;

@Transactional
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRep;
	@Autowired
	RoleRepository roleRep;
	@Autowired
	private EmailService emailService;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private VerificationTokenRepository verificationTokenRepo;

	@Override
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);
		if (usr != null && r != null && !usr.getRoles().contains(r)) {
	        usr.getRoles().add(r);
	        usr = userRep.save(usr); 
	    }
		return usr;
	}

	@Override
	public Role addRole(Role role) {
		Role existing = roleRep.findByRole(role.getRole());
	    if (existing != null) {
	        return existing; 
	    }
		return roleRep.save(role);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

	@Override
	public User registerUser(RegistationRequest request) {
		Optional<User> optionaluser = userRep.findByEmail(request.getEmail());
		if (optionaluser.isPresent())
			throw new EmailAlreadyExistsException("email déjà existant!");
		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setEmail(request.getEmail());
		newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
		newUser.setEnabled(false);
		
		Role r = roleRep.findByRole("USER"); 
		if (r == null) {
	        r = new Role();
	        r.setRole("USER");
	        roleRep.save(r);
	    }
		
		List<Role> roles = new ArrayList<>();
		roles.add(r);
		newUser.setRoles(roles);
		
		userRep.save(newUser);

		// génére le code secret
		String code = this.generateCode();
		VerificationToken token = new VerificationToken(code, newUser);
		verificationTokenRepo.save(token);

		String emailContent = "<h1>Confirme ton email</h1>"
	            + "<p>Voici ton code de confirmation : " + code + "</p>";

	    emailService.sendEmail(newUser.getEmail(), emailContent);
	    
		return newUser;
	}

	public String generateCode() {
		Random random = new Random();
		Integer code = 100000 + random.nextInt(900000);

		return code.toString();
	}

	@Override
	public User validateToken(String code) {
		VerificationToken token = verificationTokenRepo.findByToken(code);
		if (token == null) {
			throw new InvalidTokenException("Invalid Token");
		}

		User user = token.getUser();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			verificationTokenRepo.delete(token);
			throw new ExpiredTokenException("expired Token");
		}
		user.setEnabled(true);
		userRep.save(user);
		return user;
	}

}
