package com.yuripe.normalizator.controllers;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yuripe.normalizator.exceptions.CarException;
import com.yuripe.normalizator.exceptions.CustomerException;
import com.yuripe.normalizator.exceptions.EmployeeException;
import com.yuripe.normalizator.exceptions.RepairException;
import com.yuripe.normalizator.models.Car;
import com.yuripe.normalizator.models.Customer;
import com.yuripe.normalizator.models.EVehicleType;
import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.models.Repair;
import com.yuripe.normalizator.models.Work;
import com.yuripe.normalizator.payload.request.NewCarRequest;
import com.yuripe.normalizator.payload.request.NewCustomerRequest;
import com.yuripe.normalizator.payload.request.NewWorkRequest;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.EmployeeRepository;
import com.yuripe.normalizator.repositories.RepairRepository;
import com.yuripe.normalizator.repositories.WorkRepository;
import com.yuripe.normalizator.security.services.CarService;
import com.yuripe.normalizator.security.services.CustomerService;
import com.yuripe.normalizator.security.services.EmployeeService;
import com.yuripe.normalizator.security.services.RepairService;
import com.yuripe.normalizator.security.services.WorkService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/work")
public class WorkController {
  @Autowired
  EmployeeRepository employeeRepository;
  
  @Autowired
  WorkRepository workRepository;
  
  @Autowired
  RepairRepository repairRepository;
  
  
  @Autowired
  CarService carService;
  
  @Autowired
  CustomerService customerService;
  
  @Autowired
  EmployeeService employeeService;
  
  @Autowired
  RepairService repairService;
  
  @Autowired
  WorkService workService;

  @GetMapping("/all")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public List<Work> getAllWorks() {
    return workRepository.findAll();
  }
  
  @PostMapping("/add/{employeeWorking}/{repairId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> addNewWork(@PathVariable Long employeeWorking, @PathVariable Long repairId, @RequestBody NewWorkRequest workRequest) throws CarException, EmployeeException, CustomerException, RepairException {
	  Repair rep = repairService.getRepair(repairId);
	  Employee emp = employeeService.getEmployee(employeeWorking);
	   
	  Work work = new Work();
	  work.setEmployee(emp);
	  work.setRepair(rep);
	  if (!validateInput(workRequest.getNote(), workRequest.getWorkingHours())) {
		  throw new InputMismatchException(
				  );
	  }
	  work.setNote(workRequest.getNote());
	  work.setWorkingHours(workRequest.getWorkingHours());
	  work.setDate(LocalDate.now());

	  workService.addWork(work);
	  return ResponseEntity.ok(new MessageResponse("Work added successfully!"));
  }
  
  
  private boolean validateInput(String note, int workingHours) {
	  return (note.length() > 50 || note.length() < 1 || Objects.isNull(note) || workingHours > 8 || workingHours < 1 || Objects.isNull(workingHours) ) ? false : true;
}

@GetMapping("/getWork/{id}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getWorkById(@PathVariable Long id) {
	  Optional<Work> actualWork = workRepository.findById(id);
    return actualWork.isPresent() ? ResponseEntity.ok(workRepository.findById(id)) : ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Work not exists with id: " + id));
  }
  
  @GetMapping("/getWorkRepair/{repairId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getWorkByRepairId(@PathVariable Long repairId) {
	  Optional<List<Work>> actualWork = workRepository.findByRepair_repairId(repairId);
    return actualWork.isPresent() ? ResponseEntity.ok(actualWork) : ResponseEntity
            .badRequest()
            .body(new MessageResponse(String.format("Error: No associated works with this repair [id: %d]", repairId)));
  }
  
  
  @PutMapping("/updateWork/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Work> updateWork(@PathVariable Long id, @RequestBody Work work) {
	  Work actualWork = workRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Work not exists with id: " + id));


    return ResponseEntity.ok(workRepository.save(actualWork));
  }
  
  @DeleteMapping("/removeWork/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteWorkById(@PathVariable Long id) {
	  Work actualWork = workRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Work not exists with id: " + id));
	  
	  workRepository.delete(actualWork);
	
    return ResponseEntity.ok(null);
  }
  
  
  
  
  
  
  
  
  
  
  @GetMapping("/checkCustomer")
  public ResponseEntity<?> verifyGetCustomer(@RequestParam String customerCF) throws CustomerException {
	  Customer customer = customerService.getCustomer(customerCF);
	  return ResponseEntity.ok(new MessageResponse(MessageFormat.format("Customer {0} {1} is already present.", customer.getName(), customer.getSurname())));
  }

  @PostMapping("/newCustomer")
  public ResponseEntity<?> createCustomer(@Valid @RequestBody NewCustomerRequest customerRequest) {
	  Customer newCustomer = new Customer();
	  
	  newCustomer.setCF(customerRequest.getCustomerCF());
	  newCustomer.setEmail(customerRequest.getCustomerEmail());
	  newCustomer.setName(customerRequest.getCustomerName());
	  newCustomer.setSurname(customerRequest.getCustomerSurname());
	  newCustomer.setTelephoneNumber(customerRequest.getCustomerTelephoneNumber());

	  customerService.addCustomer(newCustomer);
	  return ResponseEntity.ok(new MessageResponse("Customer added successfully!"));
  }
  
  @GetMapping("/checkCar")
  public ResponseEntity<?> verifyGetCar(@RequestParam String carPlate) throws CarException {
	  Car car = carService.getCar(carPlate);
	  return ResponseEntity.ok(new MessageResponse(MessageFormat.format("Car {0} {1} is already present.", car.getMake(), car.getPlate())));
  }
  
  @PostMapping("/newCar")
  public ResponseEntity<?> createCar(@Valid @RequestBody NewCarRequest carRequest) throws CustomerException {
	  Car newCar = new Car();
	  
	  newCar.setMake(carRequest.getCarMake());
	  newCar.setModel(carRequest.getCarModel());
	  newCar.setType(EVehicleType.BERLINA);
	  newCar.setPlate(carRequest.getCarPlate());
	  newCar.setCustomer(customerService.getCustomer(carRequest.getCustomerCF()));

	  carService.addCar(newCar);
	  return ResponseEntity.ok(new MessageResponse("Car added successfully!"));
  }
  
  @GetMapping("/todo")
  @PreAuthorize("hasRole('SUPERVISOR')")
  public String getWorksToDo() {
    return "Moderator Board.";
  }
}
