package com.example.Employees.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Employees.Entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
