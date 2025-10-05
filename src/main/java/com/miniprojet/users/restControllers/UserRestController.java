package com.miniprojet.users.restControllers;

import java.util.List;
import java.util.Map;

import com.miniprojet.users.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.miniprojet.users.entities.User;
import com.miniprojet.users.register.RegistationRequest;
import com.miniprojet.users.service.UserService;

@RestController

@CrossOrigin(origins = "*")
public class UserRestController {

    private final EmailService emailService;
	@Autowired
	UserService userService;

    UserRestController(EmailService emailService) {
        this.emailService = emailService;
    }

	@GetMapping("all")
	public List<User> getAllUsers() {
		return userService.findAllUsers();
	}

	@PostMapping("/register")
	public User register(@RequestBody RegistationRequest request) {
		return userService.registerUser(request);
	}

	@GetMapping("/verifyEmail/{token}")
	public User verifyEmail(@PathVariable("token") String token) {
		return userService.validateToken(token);
	}
	
	 @GetMapping("/sendTestEmail")
	    public String sendTestEmail() {
	        try {
	            emailService.sendEmail("smakhlouf278@gmail.com", "<h1>Email de test</h1><p>Ça marche 🎉</p>");
	            return "Email envoyé avec succès !";
	        } catch (Exception e) {
	            return "Erreur lors de l'envoi : " + e.getMessage();
	        }
	    }
	 @PostMapping("/send-email")
	 public String sendEmail(@RequestBody Map<String, String> request) {
	     try {
	         String to = request.get("to");
	         String subject = request.get("subject");
	         String body = request.get("body");
	         emailService.sendEmail(to, body); // pour l'instant, ignore le subject
	         return "Email envoyé avec succès !";
	     } catch (Exception e) {
	         return "Erreur lors de l'envoi : " + e.getMessage();
	     }
	 }

}
