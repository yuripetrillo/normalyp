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

import com.yuripe.normalizator.configurations.FTPServiceCustom;
import com.yuripe.normalizator.exceptions.CarException;
import com.yuripe.normalizator.exceptions.CustomerException;
import com.yuripe.normalizator.exceptions.EmployeeException;
import com.yuripe.normalizator.exceptions.RepairException;
import com.yuripe.normalizator.models.Car;
import com.yuripe.normalizator.models.Customer;
import com.yuripe.normalizator.models.EVehicleType;
import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.models.Repair;
import com.yuripe.normalizator.models.Job;
import com.yuripe.normalizator.payload.request.NewCarRequest;
import com.yuripe.normalizator.payload.request.NewCustomerRequest;
import com.yuripe.normalizator.payload.request.NewJobRequest;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.EmployeeRepository;
import com.yuripe.normalizator.repositories.RepairRepository;
import com.yuripe.normalizator.repositories.JobRepository;
import com.yuripe.normalizator.security.services.CarService;
import com.yuripe.normalizator.security.services.CustomerService;
import com.yuripe.normalizator.security.services.EmployeeService;
import com.yuripe.normalizator.security.services.RepairService;
import com.yuripe.normalizator.security.services.JobService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/batchProxy")
public class BatchProxyController {
  @Autowired
  EmployeeRepository employeeRepository;
  
  @Autowired
  JobRepository JobRepository;
  
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
  JobService JobService;  
  
  @Autowired
  FTPServiceCustom ftp;

  @GetMapping("/all")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public List<Job> getAllJobs() {
    return JobRepository.findAll();
  }
  
  
  @PostMapping("/launchJob")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<String> launchJob() {
	  
	  
	  return ResponseEntity.ok(ftp.call() + "Job launched successfully!");
  }
  
  
  @PostMapping("/add/{employeeJobing}/{repairId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> addNewJob(@PathVariable Long employeeJobing, @PathVariable Long repairId, @RequestBody NewJobRequest JobRequest) throws CarException, EmployeeException, CustomerException, RepairException {
	  Repair rep = repairService.getRepair(repairId);
	  Employee emp = employeeService.getEmployee(employeeJobing);
	   
	  Job job = new Job();
	  job.setEmployee(emp);
	  job.setRepair(rep);
	  if (!validateInput(JobRequest.getNote(), JobRequest.getWorkingHours())) {
		  throw new InputMismatchException(
				  );
	  }
	  job.setNote(JobRequest.getNote());
	  job.setJobingHours(JobRequest.getWorkingHours());
	  job.setDate(LocalDate.now());

	  JobService.addJob(job);
	  return ResponseEntity.ok(new MessageResponse("Job added successfully!"));
  }
  
  
  private boolean validateInput(String note, int JobingHours) {
	  return (note.length() > 50 || note.length() < 1 || Objects.isNull(note) || JobingHours > 8 || JobingHours < 1 || Objects.isNull(JobingHours) ) ? false : true;
}

@GetMapping("/getJob/{id}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getJobById(@PathVariable Long id) {
	  Optional<Job> actualJob = JobRepository.findById(id);
    return actualJob.isPresent() ? ResponseEntity.ok(JobRepository.findById(id)) : ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Job not exists with id: " + id));
  }
  
  @GetMapping("/getJobRepair/{repairId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getJobByRepairId(@PathVariable Long repairId) {
	  Optional<List<Job>> actualJob = JobRepository.findByRepair_repairId(repairId);
    return actualJob.isPresent() ? ResponseEntity.ok(actualJob) : ResponseEntity
            .badRequest()
            .body(new MessageResponse(String.format("Error: No associated Jobs with this repair [id: %d]", repairId)));
  }
  
  
  @PutMapping("/updateJob/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
	  Job actualJob = JobRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Job not exists with id: " + id));


    return ResponseEntity.ok(JobRepository.save(actualJob));
  }
  
  @DeleteMapping("/removeJob/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteJobById(@PathVariable Long id) {
	  Job actualJob = JobRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Job not exists with id: " + id));
	  
	  JobRepository.delete(actualJob);
	
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
  public String getJobsToDo() {
    return "Moderator Board.";
  }
}
