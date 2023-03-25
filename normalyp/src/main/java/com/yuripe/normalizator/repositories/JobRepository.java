package com.yuripe.normalizator.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuripe.normalizator.models.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
  Optional<Job> findById(Long id);
  List<Job> findAll();
  Optional<List<Job>> findByRepair_repairId(Long repairId);
  void deleteByRepair_repairId(Long id);
  }
