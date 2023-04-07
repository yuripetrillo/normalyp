package com.yuripe.normalizator.repositories;

import com.yuripe.normalizator.models.SFTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SFTPRepository extends JpaRepository<SFTP, Long> {

    Optional<SFTP> findByCode(String code);

    List<SFTP> findAll();

    void deleteByCode(String code);
}
