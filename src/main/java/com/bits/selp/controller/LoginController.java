package com.bits.selp.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	@Autowired
	UsersService userService;
	
	/** 
	  * User login endpoint
	  * 
	  * @param req LoginRequest containing email and password
	  * @return LoginResponse with authentication token and user details
	*/
	
	@PostMapping("/login")
	@Operation(
		summary = "Authenticate user login",
		description = "Validates the user’s credentials (email and password) and returns an authentication token along with user details.",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "User credentials for login (email and password)",
		    required = true,
		    content = @io.swagger.v3.oas.annotations.media.Content(
		    	schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequest.class)
		    )
		),
		responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
		        description = "Login successful. Returns authentication token and user info.",
		        content = @io.swagger.v3.oas.annotations.media.Content(
		        	schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginResponse.class)
		        )
			),
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		    	responseCode = "401",
		        description = "Unauthorized – invalid or empty password"
		    ),
		    @io.swagger.v3.oas.annotations.responses.ApiResponse(
		    	responseCode = "500",
		        description = "Internal server error during authentication"
		    )
		}
	)
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