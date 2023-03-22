package com.yuripe.normalizator.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuripe.normalizator.models.Repair;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
  Optional<Repair> findById(Long id);
  //Optional<Repair> findByCarIdAndRepairTypeAndEmployeeIdAndworkTotalPrice(Long carId, String repairType, Long supervisorId, int workTotalPrice);
  Optional<Repair> findByRepairType(String repairType);

  List<Repair> findAll();
  Optional<List<Repair>> findByEmployee_employeeId(Long employeeId);
  Optional<List<Repair>> findByEmployee_employeeIdAndIsWorkingFalseAndIsDoneFalse(Long employeeId);
  Optional<List<Repair>> findByEmployee_employeeIdAndIsWorkingTrueAndIsDoneFalse(Long employeeId);
  Optional<List<Repair>> findAllByIsDoneFalse();
  Optional<List<Repair>> findAllByIsDoneTrue();
  }
