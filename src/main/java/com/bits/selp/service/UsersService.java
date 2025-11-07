package com.bits.selp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bits.selp.model.UsersModel;
import com.bits.selp.repository.EquipmentRepository;
import com.bits.selp.repository.OrdersRepository;
import com.bits.selp.repository.UsersRepository;

@Service
public class UsersService {
	
    @Autowired
    UsersRepository usersRepository;
    
    @Autowired
    OrdersRepository ordersRepository;
    
    @Autowired
    EquipmentRepository equipmentRepository;
    
    public List<UsersModel> getUserModelByRole(String role) {
    	return usersRepository.getUserModelByRole(role);
    }
    
    public UsersModel saveUser(UsersModel userModel) {
    	return usersRepository.save(userModel);
    }
    
    public UsersModel getUserModelByUserId(int userid) {
    	return usersRepository.findByUserid(userid);
    }
    
    public UsersModel getUserModelByEmail(String email) {
    	return usersRepository.findByEmail(email);
    }
    
    public void deleteUser(UsersModel userModel) {
    	usersRepository.delete(userModel);
    }
}
