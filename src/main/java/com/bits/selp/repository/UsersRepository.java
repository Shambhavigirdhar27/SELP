package com.bits.selp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bits.selp.model.UsersModel;

@Repository
public interface UsersRepository extends JpaRepository<UsersModel, Integer> {
	
	@Query("SELECT u FROM UsersModel u where u.role = :role")
	public List<UsersModel> getUserModelByRole(@Param(value = "role") String role);
	
	public UsersModel findByUserid(int userid);
	
	public UsersModel findByEmail(String email);
}
