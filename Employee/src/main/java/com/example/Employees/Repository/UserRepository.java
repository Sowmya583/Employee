package com.example.Employees.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Employees.Entity.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String>{

		UserModel findByusername(String username);
	
}