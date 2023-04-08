package com.yuripe.normalizator.models;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "repairs", schema = "Application")
public class Repair {
  @Id
  @Column(name = "repairId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long repairId;

  @NotBlank
  @Size(max = 50)
  private String repairType;

  @NotNull
  @Column(columnDefinition = "DATE")
  private LocalDate startDate;
  
  @NotNull
  @Column(columnDefinition = "DATE")
  private LocalDate endDate;
  
  private int repairHours;
  
  private boolean isDone = false;
  
  private boolean isWorking = false;
  
  @NotNull
  private int workTotalPrice;
  
  @NotNull
  private int partsTotalPrice;
  
  @NotNull
  private int externalTotalPrice;

  @ManyToOne
  private Car car;
  
  @NotBlank
  private String customerCF;
  
  @ManyToOne
  private Employee employee;



  public Repair() {
  }

  public Repair(Long repairId, String repairType, LocalDate startDate, LocalDate endDate,
		int repairHours, boolean isDone, boolean isWorking, int workTotalPrice, int partsTotalPrice,
		int externalTotalPrice, Car car, Employee employee) {
	super();
	this.repairId = repairId;
	this.repairType = repairType;
	this.startDate = startDate;
	this.endDate = endDate;
	this.repairHours = repairHours;
	this.isDone = isDone;
	this.isWorking = isWorking;
	this.workTotalPrice = workTotalPrice;
	this.partsTotalPrice = partsTotalPrice;
	this.externalTotalPrice = externalTotalPrice;
	this.car = car;
	this.employee = employee;
  }

	public Long getRepairId() {
		return repairId;
	}
	
	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}
	
	public Long getId() {
		return repairId;
	}
	
	public void setId(Long repairId) {
		this.repairId = repairId;
	}
	
	public String getRepairType() {
		return repairType;
	}
	
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public int getRepairHours() {
		return repairHours;
	}
	
	public void setRepairHours(int repairHours) {
		this.repairHours = repairHours;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public boolean isWorking() {
		return isWorking;
	}
	
	public void setWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
	
	public int getWorkTotalPrice() {
		return workTotalPrice;
	}
	
	public void setWorkTotalPrice(int workTotalPrice) {
		this.workTotalPrice = workTotalPrice;
	}
	
	public int getPartsTotalPrice() {
		return partsTotalPrice;
	}
	
	public void setPartsTotalPrice(int partsTotalPrice) {
		this.partsTotalPrice = partsTotalPrice;
	}
	
	public int getExternalTotalPrice() {
		return externalTotalPrice;
	}
	
	public void setExternalTotalPrice(int externalTotalPrice) {
		this.externalTotalPrice = externalTotalPrice;
	}
	
	public Car getCar() {
		return car;
	}
	
	public void setCar(Car car) {
		this.car = car;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getCustomerCF() {
		return customerCF;
	}

	public void setCustomerCF(String customerCF) {
		this.customerCF = customerCF;
	}
  
}
