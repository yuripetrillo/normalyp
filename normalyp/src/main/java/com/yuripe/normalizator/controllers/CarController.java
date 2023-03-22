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

import com.yuripe.normalizator.models.Car;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.CarRepository;
import com.yuripe.normalizator.repositories.CustomerRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/car")
public class CarController {
	
	@Autowired
	PasswordEncoder encoder;
  
	@Autowired
	CarRepository carRepository;
	
	@Autowired
	CustomerRepository customerRepository;

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Car> adminAccess() {
    return carRepository.findAll();
  }
  
  @PostMapping("/addCar/{customerCF}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addCar(@PathVariable String customerCF, @RequestBody Car car) {	  
	  Map<Car, String> response = new HashMap<>();
	  try {
		  car.setCustomer(customerRepository.findByCF(customerCF).orElseThrow(() -> new NoSuchElementException("Customer not exists with CF: " + customerCF)));
		  response.put(carRepository.save(car),"Car with plate: "+car.getPlate()+" added!");
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
  
  @GetMapping("/getCar/{plate}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getCarById(@PathVariable String plate) {
	  Optional<Car> actualCar = carRepository.findByPlate(plate);
	  
    return actualCar.isPresent() ? ResponseEntity.ok(actualCar): ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Car not exists with plate: " + plate));
  }
  
  @PutMapping("/updateCar/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
    Car actualCar = carRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Car not exists with id: " + id));
    
    return ResponseEntity.ok(carRepository.save(actualCar));
  }
  
  @DeleteMapping("/removeCar/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteCarById(@PathVariable Long id) {
	  Car actualCar = carRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Car not exists with id: " + id));
	  
	carRepository.delete(actualCar);
	Map<String, Boolean> response = new HashMap<>();
	response.put("Car with plate: "+actualCar.getPlate()+" deleted", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
  }
}

