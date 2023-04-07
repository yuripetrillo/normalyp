package com.yuripe.normalizator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuripe.normalizator.models.Employee;
import com.yuripe.normalizator.repositories.EmployeeRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  EmployeeRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Employee Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
