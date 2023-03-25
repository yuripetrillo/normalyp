package com.yuripe.normalizator.controllers;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import com.yuripe.normalizator.exceptions.JobException;
import com.yuripe.normalizator.models.Car;
import com.yuripe.normalizator.models.Customer;
import com.yuripe.normalizator.models.EVehicleType;
import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.models.Repair;
import com.yuripe.normalizator.payload.request.NewCarRequest;
import com.yuripe.normalizator.payload.request.NewCustomerRequest;
import com.yuripe.normalizator.payload.request.NewRepairRequest;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.EmployeeRepository;
import com.yuripe.normalizator.repositories.RepairRepository;
import com.yuripe.normalizator.security.services.CarService;
import com.yuripe.normalizator.security.services.CustomerService;
import com.yuripe.normalizator.security.services.EmployeeService;
import com.yuripe.normalizator.security.services.RepairService;
import com.yuripe.normalizator.security.services.JobService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/repair")
public class RepairController {
  @Autowired
  EmployeeRepository employeeRepository;
  
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
  JobService jobService;

  @GetMapping("/all")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public List<Repair> getAllRepairs() {
    return repairRepository.findAll();
  }
  
  @GetMapping("/all/notDone")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public List<Repair> getAllRepairsNotDone() throws RepairException {
    return repairService.getRepairsNotDone();
  }
  
  @GetMapping("/all/done")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public List<Repair> getAllRepairsDone() throws RepairException {
    return repairService.getRepairsDone();
  }
  
  @PostMapping("/add/{employeeWorking}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN')")
  public ResponseEntity<?> addNewRepair(@PathVariable String employeeWorking, @RequestBody NewRepairRequest repairRequest) throws CarException, EmployeeException, CustomerException {
	  
	  Car car = carService.getCar(repairRequest.getCarPlate());
	  Employee emp = employeeService.getEmployeeByName(employeeWorking);
	  Customer customer = customerService.getCustomer(repairRequest.getCustomerCF());
	  //if (repairRepository.existsByCarIdAndRepairTypeAndEmployeeIdAndworkTotalPrice(car.getId(), repairRequest.getRepairType(), supervisor.get().getId(), repairRequest.getWorkTotalPrice()).isPresent()) {
	  /*if (repairRepository.findByRepairType(repairRequest.getRepairType()).isPresent()){
  			return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: this repair is already present!"));
	    }*/
	  
	  if(!car.getCustomer().getCF().equals(customer.getCF())) {
		  return ResponseEntity
		          .badRequest()
		          .body(new MessageResponse("Error: customer "+customer.getCF()+" and car registered customer "+car.getCustomer().getCF()+" doesn't match!"));
	  }
	  
	  Repair newRepair = new Repair();
	  newRepair.setCar(car);
	  newRepair.setCustomerCF(car.getCustomer().getCF());
	  newRepair.setEmployee(emp);
	  newRepair.setStartDate(repairRequest.getStartDate());
	  newRepair.setEndDate(repairRequest.getEndDate());
	  newRepair.setExternalTotalPrice(repairRequest.getExternalTotalPrice());
	  newRepair.setWorkTotalPrice(repairRequest.getWorkTotalPrice());
	  newRepair.setExternalTotalPrice(repairRequest.getExternalTotalPrice());
	  newRepair.setRepairHours(repairRequest.getRepairHours());
	  newRepair.setRepairType(repairRequest.getRepairType());
	  
	  repairService.addRepair(newRepair);
	  return ResponseEntity.ok(new MessageResponse("Repair added successfully!"));
  }
  
  
  @GetMapping("/getRepair/{id}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getRepairById(@PathVariable Long id) {
	  Optional<Repair> actualRepair = repairRepository.findById(id);
    return actualRepair.isPresent() ? ResponseEntity.ok(repairRepository.findById(id)) : ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Repair not exists with id: " + id));
  }
  
  @GetMapping("/getRepairEmployee/{employeeId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getRepairsByEmployeeId(@PathVariable Long employeeId) throws RepairException {
	  List<Repair> actualRepairs = repairService.getRepairsByEmployeeId(employeeId);
    return ResponseEntity.ok(actualRepairs);
  }
  
  @GetMapping("/getRepairToDoEmployee/{employeeId}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<?> getRepairToDoByEmployeeId(@PathVariable Long employeeId) throws RepairException {
	  List<Repair> actualRepairs = repairService.getRepairsByEmployeeIdAndFilterWorkingDone(employeeId);
    return ResponseEntity.ok(actualRepairs);
  }
  
  @PutMapping("/updateRepair/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateRepair(@PathVariable Long id, @RequestBody Repair repair) throws EmployeeException {
	  Repair actualRepair = repairRepository.findById(id)
    		.orElseThrow(() -> new NoSuchElementException("Repair not exists with id: " + id));
	  
	  Employee emp = employeeService.getEmployeeByName(repair.getEmployee().getName());
      
    actualRepair.setDone(repair.isDone());
    actualRepair.setEmployee(emp);
    actualRepair.setStartDate(repair.getStartDate());
    actualRepair.setEndDate(repair.getEndDate());
    actualRepair.setExternalTotalPrice(repair.getExternalTotalPrice());
    actualRepair.setPartsTotalPrice(repair.getPartsTotalPrice());
    actualRepair.setWorkTotalPrice(repair.getWorkTotalPrice());


    repairService.updateRepair(actualRepair);
    return ResponseEntity.ok("Repair updated successfully");
  }
  
  @DeleteMapping("/removeRepair/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Boolean>> deleteRepairById(@PathVariable Long id) throws RepairException, JobException {
	  Repair actualRepair = repairService.getRepair(id);	  
	  jobService.deleteJobByRepair(id);
	  repairService.deleteRepair(id);
	  
	Map<String, Boolean> response = new HashMap<>();
	response.put("Repair of Car Plate: "+actualRepair.getCar().getPlate()+" deleted", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/closeRepair/{id}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<Map<String, Boolean>> closeRepairById(@PathVariable Long id) throws RepairException {
	  Repair actualRepair = repairService.getRepair(id);
	  actualRepair.setDone(true);
	  
	repairService.updateRepair(actualRepair);
	Map<String, Boolean> response = new HashMap<>();
	response.put("Repair of Car Plate: "+actualRepair.getCar().getPlate()+" closed", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/takeRepair/{id}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<Map<String, Boolean>> takeRepairById(@PathVariable Long id) throws RepairException {
	  Repair actualRepair = repairService.getRepair(id);
	  actualRepair.setWorking(true);
	  
    repairService.updateRepair(actualRepair);
	Map<String, Boolean> response = new HashMap<>();
	response.put("Repair of Car Plate: "+actualRepair.getCar().getPlate()+" taken", Boolean.TRUE);
	
    return ResponseEntity.ok(response);
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
  public String getRepairsToDo() {
    return "Moderator Board.";
  }
}
