package com.yuripe.normalizator.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.repositories.EmployeeRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/verify")
public class UserController {
  
	@Autowired
	EmployeeRepository employessRepository;
	
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/employee")
  @PreAuthorize("hasRole('USER') or hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public String userAccess(@RequestHeader("Username") String Username) {
    return Username;
  }

  @GetMapping("/supervisor")
  @PreAuthorize("hasRole('SUPERVISOR')")
  public String moderatorAccess() {
    return "EDD Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Employee> adminAccess() {
    return employessRepository.findAll();
  }
}

