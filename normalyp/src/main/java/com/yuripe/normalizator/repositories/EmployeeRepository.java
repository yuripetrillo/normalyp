package com.yuripe.normalizator.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuripe.normalizator.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Optional<Employee> findByUsername(String username);
  List<Employee> findAll();

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  void deleteByUsername(String username);
  Optional<Employee> findByName(String employeeWorking);
}

