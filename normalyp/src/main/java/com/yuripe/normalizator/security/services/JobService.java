package com.yuripe.normalizator.security.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuripe.normalizator.exceptions.JobException;
import com.yuripe.normalizator.models.Job;
import com.yuripe.normalizator.repositories.JobRepository;

@Service
@Transactional
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return (List<Job>) jobRepository.findAll();
    }

    public Job getJob(Long id) throws JobException {
        return jobRepository.findById(id).orElseThrow(() -> new JobException("Job Not FOUND!"));
    }
    
    public List<Job> getJobsByRepair(Long id) throws JobException {
        return jobRepository.findByRepair_repairId(id).orElseThrow(() -> new JobException("Job of repair " + id + " Not FOUND!"));
    }

    public void addJob(Job job) {
        this.jobRepository.save(job);
    }

    public void updateJob(String id, Job job) {
        this.jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }

	public void deleteJobByRepair(Long id) {
		this.jobRepository.deleteByRepair_repairId(id);
		
	}
}
