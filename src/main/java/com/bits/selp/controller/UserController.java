package com.bits.selp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.selp.model.UsersModel;
import com.bits.selp.service.LoginService;
import com.bits.selp.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import utilities.ResponseConstants;


@RestController
@RequestMapping("/user")
@Tag(name = "User Management", description = "APIs for managing users in the School Equipment Lending Portal")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    UsersService usersService;

    @GetMapping("/list")
    @Operation(
    	summary = "Get user list by role",
        description = "Fetches a list of users filtered by their role (Admin or Student).",
        parameters = {
        	@Parameter(
        		name = "role",
                description = "Role of the users to fetch (e.g., Admin, Student).",
                required = true,
                example = "Admin"
        	)
    	},
        responses = {
        	@ApiResponse(
        		responseCode = "200",
                description = "List of users retrieved successfully.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsersModel.class))
        	),
            @ApiResponse(responseCode = "400", description = "Invalid role parameter."),
            @ApiResponse(responseCode = "500", description = "Server error while fetching users.")
    	}
    )
	public List<UsersModel> getUserModelByRole(@RequestHeader(value = "Authorization") String authorization,
			@RequestParam(value = "role") String role) {
		logger.info("getUserModelByRole(): begin");
    	List<UsersModel> userModelList = new ArrayList<>();
    	try {
    		LoginService.validateUserToken(authorization);
    		userModelList = usersService.getUserModelByRole(role);
    	} catch(IllegalAccessException e) {
    		logger.error(e.getMessage(), e);
    		return new ArrayList<>();
    	} catch (Exception e) {
    		logger.error("Exception while fetching user list based on role.", e);
    	}
        return userModelList;
    }
    
    /** 
     * Create a new user
     * 
     * @param request
     * @param userModel
     * @return
     */
    @PostMapping("/add")
    @Operation(
    	summary = "Create a new user",
        description = "Creates a new user record in the system. Accepts name, email, phone, and role as JSON input.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        	description = "User details to be added",
            required = true,
            content = @Content(schema = @Schema(implementation = UsersModel.class))
        ),
        responses = {
        	@ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Error while creating the user.")
    	}
    )
    public ResponseEntity<Map<String, Object>> createNewUser(@RequestHeader(value = "Authorization") String authorization, @RequestBody UsersModel userRequest) {
        logger.info("createNewUser(): begin");
        Map<String, Object> response = new HashMap<>();

        try {
        	LoginService.validateUserToken(authorization);
            // Build the model
            UsersModel usersModel = new UsersModel();
            usersModel.setUsername(userRequest.getUsername());
            usersModel.setEmail(userRequest.getEmail());
            usersModel.setPhonenumber(userRequest.getPhonenumber());

            // Role check
            if ("admin".equalsIgnoreCase(userRequest.getRole())) {
                usersModel.setRole(ResponseConstants.ROLE_ADMIN);
            } else {
                usersModel.setRole(ResponseConstants.ROLE_STUDENT);
            }

            // Save user
            usersService.saveUser(usersModel);

            response.put("status", "success");
            response.put("message", "User created successfully");
            response.put("user", usersModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch(IllegalAccessException e) {
    		logger.error(e.getMessage(), e);
    		response.put("status", "unauthorized");
    		response.put("message", e);
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    	} catch (Exception e) {
            logger.error("Error while saving user details.", e);
            response.put("status", "error");
            response.put("message", "Failed to create user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    /** 
     * Delete a user
     * 
     * @param userId
     * @return
     */
    @PostMapping("/delete")
    @Operation(
    	summary = "Delete a user",
        description = "Deletes a user from the system using their unique user ID.",
        parameters = {
        	@Parameter(
        		name = "userid",
                description = "ID of the user to delete",
                required = true,
                example = "101"
        	)
    	},
        responses = {
        	@ApiResponse(responseCode = "200", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Error while deleting user.")
    	}
    )
    public String deleteUser(@RequestParam(value = "userid") int userId) {
    	logger.info("deleteUser(): begin");
    	String response = "";
    	try {
    		UsersModel usersModel = usersService.getUserModelByUserId(userId);
    		usersService.deleteUser(usersModel);
    		response = ResponseConstants.RESPONSE_SUCCESS;
    	} catch(Exception e) {
    		logger.error("Exception while deleting user.", e);
    		response = ResponseConstants.RESPONSE_ERROR;
    	}
		return response;
    }
 
}
