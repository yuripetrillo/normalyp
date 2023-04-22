package com.yuripe.normalizator.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import com.yuripe.normalizator.models.*;
import com.yuripe.normalizator.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.yuripe.core.library.utility.FtpClient;
import com.yuripe.normalizator.configurations.FTPServiceCustom;
import com.yuripe.normalizator.exceptions.CarException;
import com.yuripe.normalizator.exceptions.CustomerException;
import com.yuripe.normalizator.exceptions.EmployeeException;
import com.yuripe.normalizator.exceptions.RepairException;
import com.yuripe.normalizator.payload.request.NewCarRequest;
import com.yuripe.normalizator.payload.request.NewCustomerRequest;
import com.yuripe.normalizator.payload.request.NewJobRequest;
import com.yuripe.normalizator.payload.response.MessageResponse;
import com.yuripe.normalizator.repositories.EmployeeRepository;
import com.yuripe.normalizator.repositories.RepairRepository;
import com.yuripe.normalizator.repositories.JobRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/batchProxy")
public class BatchProxyController {
  private static final Logger logger = LoggerFactory.getLogger(BatchProxyController.class);
  private static final String targetPath = new File(BatchProxyController.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getAbsolutePath();
  private static final String outputDir = targetPath.concat("/BATCH_IN_FILES/");
  private static final String backupDir = targetPath.concat("/BACKUP_IN_FILES/");
  private static final String batchInputFolder = Paths.get("C:\\Users\\yurip\\git\\INPUT_FILE_BATCH").normalize().toString();
  
  @Autowired
  EmployeeRepository employeeRepository;
  
  @Autowired
  JobRepository JobRepository;
  
  @Autowired
  RepairRepository repairRepository;
  
  @Autowired
  CarService carService;

  @Autowired
  SFTPService sftpService;
  
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
  
  
  @PostMapping("/launchJob/{scheduleName}/{filePattern}/{batchCode}")
  @PreAuthorize("hasRole('SUPERVISOR') or hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<String> launchJob(@PathVariable String scheduleName, @PathVariable String filePattern, @PathVariable String batchCode) throws IOException {
      if(Objects.isNull(scheduleName)) {
          return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                  .body("You must specify valid schedulation code.");
      }

      SFTP sftpServer = sftpService.getSftp(scheduleName);
	  FtpClient ftpClient = new FtpClient(sftpServer.getHost(), sftpServer.getPort(), sftpServer.getUsername(), sftpServer.getPassword());
      if(!ftp.checkFtpServerState(ftpClient)) {
    	  logger.error(HttpStatus.SERVICE_UNAVAILABLE.toString());
          return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
      }
      if(!ftp.checkValidMandatoryInput(ftpClient, filePattern)) {
    	  logger.error("Requested data ".concat(HttpStatus.NOT_FOUND.toString()));
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested data not available on server.");
      }
      
      //Move file from FTP instead of COPY
      if(!ftp.getFileFromSFTP(filePattern, outputDir.concat(filePattern))) {
    	  return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Cannot get file from server.");
      }
      backupFile(new File(outputDir.concat(filePattern)), new File(backupDir.concat(filePattern)));
      moveFile(outputDir.concat(filePattern), Paths.get(batchInputFolder + "\\".concat(batchCode)).normalize().toString().concat("\\" + filePattern));
      
      //CALL BATCH BY SCHEDULENAME AND FILEPATTERN
      
      //GET INFO FROM DB AND SEND JSON REQUEST
      
	  return ResponseEntity.status(HttpStatus.OK).body("Server status OK, input is valid, Job launched successfully!");
  }
  

  
  @SuppressWarnings("resource")
  private static void backupFile(File sourceFile, File destFile) throws IOException {
	    if (!sourceFile.exists()) {
	        throw new FileNotFoundException();
	    }
	    if (!destFile.exists()) {
	        destFile.createNewFile();
	    }
	    FileChannel source = null;
	    FileChannel destination = null;
	    source = new FileInputStream(sourceFile).getChannel();
	    destination = new FileOutputStream(destFile).getChannel();
	    if (destination != null && source != null) {
	        destination.transferFrom(source, 0, source.size());
	    }
	    
	    if (source != null) {
	        source.close();
	    }
	    if (destination != null) {
	        destination.close();
	    }

	}
  
  public boolean moveFile(String sourcePath, String targetPath) {
	    boolean fileMoved = false;

	    try {
	        Files.move(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
	        fileMoved = true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fileMoved;
	}


@PostMapping("/add/{employeeWorking}/{repairId}")
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
