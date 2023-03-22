package com.yuripe.normalizator.payload.request;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.validation.constraints.*;

public class NewRepairRequest {
	  @NotBlank
	  @Size(min = 16, max = 16)
	  private String customerCF;

	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String customerEmail;
	  
	  @NotBlank
	  @Column(columnDefinition = "DATE")
	  private LocalDate startDate;
	  
	  @NotBlank
	  @Column(columnDefinition = "DATE")
	  private LocalDate endDate;
	  
	  @NotBlank
	  private int workTotalPrice;
	  
	  @NotBlank
	  private int partsTotalPrice;
	  
	  @NotBlank
	  private int externalTotalPrice;
	  
	  @NotBlank
	  private int repairHours;
	  
	  @NotBlank
	  private String repairType;
	  
	  @NotBlank
	  private String carMake;
	  
	  @NotBlank
	  private String carModel;
	  
	  @NotBlank
	  private String carType;
	  
	  @NotBlank
	  private boolean isDone;

	  @NotBlank
	  private String carPlate;

	  @NotBlank
	  private String supervisorUsername;

	public String getCustomerCF() {
		return customerCF;
	}

	public void setCustomerCF(String customerCF) {
		this.customerCF = customerCF;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public String getSupervisorUsername() {
		return supervisorUsername;
	}

	public void setSupervisorUsername(String supervisorUsername) {
		this.supervisorUsername = supervisorUsername;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
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

	public int getRepairHours() {
		return repairHours;
	}

	public void setRepairHours(int repairHours) {
		this.repairHours = repairHours;
	}

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

}
