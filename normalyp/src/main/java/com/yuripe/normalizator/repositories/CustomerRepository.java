package com.yuripe.normalizator.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuripe.normalizator.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByName(String name);
  List<Customer> findAll();

  Boolean existsByName(String name);

  Boolean existsByEmail(String email);
  Optional<Customer> findByCF(String customerCF);
  void deleteByCF(String customerCF);
}
