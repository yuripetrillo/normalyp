package com.yuripe.normalizator.security.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuripe.normalizator.exceptions.WorkException;
import com.yuripe.normalizator.models.Work;
import com.yuripe.normalizator.repositories.WorkRepository;

@Service
@Transactional
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    public List<Work> getAllWorks() {
        return (List<Work>) workRepository.findAll();
    }

    public Work getWork(Long id) throws WorkException {
        return workRepository.findById(id).orElseThrow(() -> new WorkException("Work Not FOUND!"));
    }
    
    public List<Work> getWorksByRepair(Long id) throws WorkException {
        return workRepository.findByRepair_repairId(id).orElseThrow(() -> new WorkException("Work of repair " + id + " Not FOUND!"));
    }

    public void addWork(Work work) {
        this.workRepository.save(work);
    }

    public void updateWork(String id, Work work) {
        this.workRepository.save(work);
    }

    public void deleteWork(Long id) {
        this.workRepository.deleteById(id);
    }

	public void deleteWorkByRepair(Long id) {
		this.workRepository.deleteByRepair_repairId(id);
		
	}
}
