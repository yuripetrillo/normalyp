package com.yuripe.normalizator.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuripe.normalizator.models.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
  Optional<Work> findById(Long id);
  List<Work> findAll();
  Optional<List<Work>> findByRepair_repairId(Long repairId);
  void deleteByRepair_repairId(Long id);
  }
