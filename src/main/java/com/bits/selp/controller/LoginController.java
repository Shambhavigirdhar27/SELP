package com.bits.selp.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bits.selp.dto.LoginRequest;
import com.bits.selp.dto.LoginResponse;
import com.bits.selp.dto.UserDto;
import com.bits.selp.model.UsersModel;
import com.bits.selp.service.UsersService;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	@Autowired
	UsersService userService;
	
	  @PostMapping("/login")
	  public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest req) {
	    // Simulate verification: accept any non-empty password
	    if (req.getPassword().isBlank()) {
	      return ResponseEntity.status(401).build();
	    }
	    String email = req.getEmail();
	    UsersModel userModel = userService.getUserModelByEmail(email); // get user model from db
	    String role = userModel.getRole(); 
	    String name = userModel.getUsername();
	    String token = Base64.getEncoder().encodeToString(userModel.getPassword().getBytes(StandardCharsets.UTF_8));

	    UserDto user = new UserDto(userModel.getUserid(), email, role, name);
	    return ResponseEntity.ok(new LoginResponse(token, user));
	  }
}