package com.EmployeSystem.Management.repository;

import com.EmployeSystem.Management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	 User findByUsername(String username);
}

