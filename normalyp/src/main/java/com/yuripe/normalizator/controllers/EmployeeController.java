package com.yuripe.normalizator.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.EmployeeRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	
	@Autowired
	PasswordEncoder encoder;
  
	@Autowired
	EmployeeRepository employessRepository;

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Employee> adminAccess() {
    return employessRepository.findAll();
  }
  
  @PostMapping("/addEmployee")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
	  employee.setPassword(encoder.encode("Pass123"));
	  //TODO: user must change PASSWORD after first ACCESS!
	  Map<Employee, String> response = new HashMap<>();
	  try {
		  response.put(employessRepository.save(employee),"Employee "+employee.getName()+" added!");
		  } catch(Exception e) {
			  if (!e.getCause().getCause().getMessage().isEmpty() && e.getCause().getCause().getMessage().startsWith("Duplicate"))
			  {
				  String errorMessage = e.getCause().getCause().getMessage();
				  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.substring(0, errorMessage.indexOf("for")));
			  }
			  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause().getMessage());
			  
		  }
	  
	  return ResponseEntity.ok(response);
  }
  
  @GetMapping("/getEmployee/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
	  Optional<Employee> actualEmployee = employessRepository.findById(id);
    return actualEmployee.isPresent() ? ResponseEntity.ok(employessRepository.findById(id)) : ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Employee not exists with id: " + id));
  }
  
  @PutMapping("/updateEmployee/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
    Employee actualEmployee = employessRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Employee not exists with id: " + id));
    
    actualEmployee.setName(employee.getName());
    actualEmployee.setSurname(employee.getSurname());
    actualEmployee.setEmail(employee.getEmail());
    actualEmployee.setUsername(employee.getUsername());
    
    return ResponseEntity.ok(employessRepository.save(actualEmployee));
  }
  
  @DeleteMapping("/removeEmployee/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteEmployeeById(@PathVariable Long id) {
	  Employee actualEmployee = employessRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Employee not exists with id: " + id));
	  
	employessRepository.delete(actualEmployee);
	Map<String, Boolean> response = new HashMap<>();
	response.put("Employee "+actualEmployee.getName()+" deleted", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
  }
}

