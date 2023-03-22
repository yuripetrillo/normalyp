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

import com.yuripe.normalizator.models.Customer;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.CustomerRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	PasswordEncoder encoder;
  
	@Autowired
	CustomerRepository customerRepository;

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Customer> adminAccess() {
    return customerRepository.findAll();
  }
  
  @PostMapping("/addCustomer")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {	  
	  Map<Customer, String> response = new HashMap<>();
	  try {
	  response.put(customerRepository.save(customer),"Customer "+customer.getName()+" added!");
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
  
  @GetMapping("/getCustomer/{CF}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getCustomerById(@PathVariable String CF) {
	  Optional<Customer> actualCustomer = customerRepository.findByCF(CF);
	  
    return actualCustomer.isPresent() ? ResponseEntity.ok(actualCustomer): ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Customer not exists with CF: " + CF));
  }
  
  @PutMapping("/updateCustomer/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
    Customer actualCustomer = customerRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Customer not exists with id: " + id));
    
    actualCustomer.setName(customer.getName());
    actualCustomer.setSurname(customer.getSurname());
    actualCustomer.setEmail(customer.getEmail());
    actualCustomer.setCF(customer.getCF());
    
    return ResponseEntity.ok(customerRepository.save(actualCustomer));
  }
  
  @DeleteMapping("/removeCustomer/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteCustomerById(@PathVariable Long id) {
	  Customer actualCustomer = customerRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Customer not exists with id: " + id));
	  
	customerRepository.delete(actualCustomer);
	Map<String, Boolean> response = new HashMap<>();
	response.put("Customer "+actualCustomer.getName()+" deleted", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
  }
}

