package com.bits.selp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.selp.model.UsersModel;
import com.bits.selp.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
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
        description = "Fetches a list of users filtered by their role (Admin or Student)."
    )
    public List<UsersModel> getUserModelByRole(@RequestParam(value = "role") String role) {
    	logger.info("getUserModelByRole(): begin");
    	List<UsersModel> userModelList = new ArrayList<>();
    	try {
    		userModelList = usersService.getUserModelByRole(role);
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
        description = "Creates a new user record in the system. Accepts name, email, phone, and role as parameters."
    )
    public String createNewUser(HttpServletRequest request) {
    	logger.info("createNewUser(): begin");
    	String response = "";
    	try {
    		UsersModel usersModel = new UsersModel();
    		usersModel.setUsername(request.getParameter("name"));
        	usersModel.setEmail(request.getParameter("email"));
        	usersModel.setPhonenumber(request.getParameter("phone"));
        	if ("admin".equalsIgnoreCase(request.getParameter("role"))) {
        		usersModel.setRole(ResponseConstants.ROLE_ADMIN);
        	} else {
        		usersModel.setRole(ResponseConstants.ROLE_STUDENT);
        	}

        	usersService.saveUser(usersModel);
        	response = ResponseConstants.RESPONSE_SUCCESS;
    		
    	} catch (Exception e) {
    		logger.error("Error while saving user details.", e);
    		response = ResponseConstants.RESPONSE_ERROR;
    	}
    	return response;
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
        description = "Deletes a user from the system using their unique user ID."
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
