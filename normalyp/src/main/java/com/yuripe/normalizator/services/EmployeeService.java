package com.yuripe.normalizator.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuripe.normalizator.exceptions.EmployeeException;
import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
 	EmployeeRepository employeeRepository;

    public List<Employee> getAllCars() {
        return (List<Employee>) employeeRepository.findAll();
    }

    public Employee getEmployeeByUsername(String username) throws EmployeeException {
        return employeeRepository.findByUsername(username).orElseThrow(() -> new EmployeeException("Employee Not FOUND!"));
    }
    
    public Employee getEmployee(Long id) throws EmployeeException {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeException("Employee Not FOUND!"));
    }

    public void addEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    public void updateEmployee(String id, Employee employee) {
        this.employeeRepository.save(employee);
    }

    public void deleteEmployee(String username) {
        this.employeeRepository.deleteByUsername(username);;
    }

	public Employee getEmployeeByName(String name) throws EmployeeException {
		return employeeRepository.findByName(name).orElseThrow(() -> new EmployeeException("Employee Not FOUND!"));
	}
}
