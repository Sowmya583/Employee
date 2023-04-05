package com.example.Employees.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Employees.Entity.AuthenticationRequest;
import com.example.Employees.Entity.AuthenticationResponse;
import com.example.Employees.Entity.Employee;
import com.example.Employees.Entity.EmployeeDTO;
import com.example.Employees.Exceptions.EmptyInputException;
import com.example.Employees.Exceptions.NoDataFoundException;
import com.example.Employees.Repository.EmployeeRepository;
import com.example.Employees.Service.UserService;
import com.example.Employees.Util.JwtUtils;



@RestController
//@CrossOrigin
public class EmployeeController {
	
	@Autowired
	private ModelMapper modelMapper;
	 
	@Autowired
	private EmployeeRepository employeerepository;
	
	@Autowired
	private UserService userservice;
	@Autowired 
	private JwtUtils jwtUtil; 
	@Autowired 
	private AuthenticationManager authenticationManager; 
	
			
	/*@PostMapping("/addEmployee")
	public Employee saveEmployee(@RequestBody Employee emp) {
		if(emp.getName().isEmpty() || emp.getDesignation().isEmpty()) {
			throw new EmptyInputException();
			
		}		
		return employeerepository.save(emp); 
    }*/
	
	@PostMapping("/addEmployee")
	public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO empdto) {
		if(empdto.getName().isEmpty() || empdto.getDesignation().isEmpty()) {
			throw new EmptyInputException();
			
		}	
		Employee empRequest = modelMapper.map(empdto, Employee.class);

		Employee emp = employeerepository.save(empRequest);
		
		EmployeeDTO empResponse = modelMapper.map(emp, EmployeeDTO.class);

		return new ResponseEntity<EmployeeDTO>(empResponse, HttpStatus.CREATED);
    }
	
	/*@GetMapping("/viewAllEmployees")
	public List<Employee> getEmployees(){
		
		return employeerepository.findAll();	
	}*/
	@GetMapping("/viewAllEmployees")
	public List<EmployeeDTO> getEmployees() {
		
		List<Employee> emp = employeerepository.findAll();	
		if(emp.size()>0) {
			return emp.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class))
					.collect(Collectors.toList());
			
		}
		throw new NoDataFoundException();
	}
	
	
	/*@PutMapping("/update/{id}")
	public Employee updateEmployee (@PathVariable("id") Long id,@RequestBody Employee emp) {
		Employee existingemp = employeerepository.findById(id).get();
		existingemp.setName(emp.getName());
		existingemp.setDesignation(emp.getDesignation());
		return employeerepository.save(existingemp);
	}
	*/
	@PutMapping("/update/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee (@PathVariable("id") Long id,@RequestBody EmployeeDTO empdto) {
		Employee existingemp = employeerepository.findById(id).get();
		existingemp.setName(empdto.getName());
		existingemp.setDesignation(empdto.getDesignation());
		
		Employee empRequest = modelMapper.map(existingemp, Employee.class);

		Employee emp = employeerepository.save(empRequest);
		
		EmployeeDTO empResponse = modelMapper.map(emp, EmployeeDTO.class);

		return ResponseEntity.ok().body(empResponse);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteEmployee (@PathVariable Long id) {
		employeerepository.deleteById(id);
		return "Employee deleted with id : "+id;
	}
	
	@GetMapping("/sortByField/{field}")
	public List<Employee> sortByField(@PathVariable String field){
	List<Employee> allEmployees=employeerepository.findAll(Sort.by(Sort.Direction.ASC,field));
	return allEmployees;
	}

	@GetMapping("/viewWithPagination/{offset}/{pageSize}")
	public Page<Employee> getEmployeesWithPagination(@PathVariable int offset,@PathVariable int pageSize){
	Page<Employee> employees= employeerepository.findAll(PageRequest.of(offset, pageSize));
	return employees;
	}
		
	
	@PostMapping("/auth")
	public ResponseEntity<?> authenticateClient(@RequestBody AuthenticationRequest authreq) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authreq.getUsername(), authreq.getPassword())
		 	);
				
		}
		catch(BadCredentialsException e) {
			throw new Exception("Invalid username or password",e);
		}
		
		final UserDetails userDetails= userservice.loadUserByUsername(authreq.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	
	
	


}
