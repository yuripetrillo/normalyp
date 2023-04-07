package com.yuripe.normalizator.services;

import com.yuripe.normalizator.models.SFTP;
import com.yuripe.normalizator.repositories.SFTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SFTPService {

    @Autowired
    SFTPRepository sftpRepository;

    public List<SFTP> getAllSftps() {
        return (List<SFTP>) sftpRepository.findAll();
    }

    public SFTP getSftp(String code) throws RuntimeException {
        return sftpRepository.findByCode(code).orElseThrow(() -> new RuntimeException("SERVER Not FOUND!"));
    }

    public void addSFTP(SFTP sftp) {
        this.sftpRepository.save(sftp);
    }

    public void updateCar(String id, SFTP sftp) {
        this.sftpRepository.save(sftp);
    }

    public void deleteSftp(String code) {
        this.sftpRepository.deleteByCode(code);
    }
}
