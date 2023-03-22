package com.yuripe.normalizator.payload.request;

import javax.validation.constraints.*;

public class NewCarRequest {
	  
	  @NotBlank
	  private String carMake;
	  
	  @NotBlank
	  private String carModel;
	  
	  @NotBlank
	  private String carType;
	  
	  @NotBlank
	  private String carPlate;
	  
	  @NotBlank
	  private String customerCF;

	public String getCustomerCF() {
		return customerCF;
	}

	public void setCustomerCF(String customerCF) {
		this.customerCF = customerCF;
	}

	public String getCarMake() {
		return carMake;
	}

	public void setCarMake(String carMake) {
		this.carMake = carMake;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
}
