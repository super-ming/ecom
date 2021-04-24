package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
        log.debug("UserController.findById for id {}", id);
	    return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
        log.debug("UserController.findByUserName for username {}");
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.debug("UserController.createUser for username {}", createUserRequest.getUsername());

	    if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
            log.error("Cannot create user {} because the password doesn't match confirmPassword.", createUserRequest.getUsername());
		    String message = String.format("Password doesn't match confirmPassword.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
		if(createUserRequest.getPassword().length() < 4){
            log.error("Cannot create user {} because the password is too short.", createUserRequest.getUsername());
            String message = String.format("Password is too short.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
	    User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		userRepository.save(user);
        log.info("New user {} created", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
	
}
