package com.yuripe.normalizator.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuripe.normalizator.exceptions.RepairException;
import com.yuripe.normalizator.models.Repair;
import com.yuripe.normalizator.repositories.RepairRepository;

@Service
public class RepairService {

    @Autowired
    private RepairRepository repairRepository;

    public List<Repair> getAllRepairs() {
        return (List<Repair>) repairRepository.findAll();
    }

    public Repair getRepair(Long id) throws RepairException {
        return repairRepository.findById(id).orElseThrow(() -> new RepairException("Repair Not FOUND!"));
    }
    
    public List<Repair> getRepairsNotDone() throws RepairException {
        return repairRepository.findAllByIsDoneFalse().orElseThrow(() -> new RepairException("Repairs uncomplete Not FOUND!"));
    }
    
    public List<Repair> getRepairsDone() throws RepairException {
        return repairRepository.findAllByIsDoneTrue().orElseThrow(() -> new RepairException("Repairs completed Not FOUND!"));
    }

    public void addRepair(Repair repair) {
        this.repairRepository.save(repair);
    }

    public void updateRepair(Repair repair) {
        this.repairRepository.save(repair);
    }

    public void deleteRepair(Long id) {
        this.repairRepository.deleteById(id);;
    }

	public List<Repair> getRepairsByEmployeeId(Long employeeId) throws RepairException {
		return repairRepository.findByEmployee_employeeIdAndIsWorkingTrueAndIsDoneFalse(employeeId).orElseThrow(() -> new RepairException("Repairs of employee " +employeeId+" Not FOUND!"));
	}

	public List<Repair> getRepairsByEmployeeIdAndFilterWorkingDone(Long employeeId) throws RepairException {
		return repairRepository.findByEmployee_employeeIdAndIsWorkingFalseAndIsDoneFalse(employeeId).orElseThrow(() -> new RepairException("Repairs not done of employee " +employeeId+" Not FOUND!"));
	}
}
