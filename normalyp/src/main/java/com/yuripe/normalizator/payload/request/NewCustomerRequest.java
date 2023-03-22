package com.yuripe.normalizator.payload.request;

import javax.validation.constraints.*;

public class NewCustomerRequest {
	  @NotBlank
	  @Size(min = 16, max = 16)
	  private String customerCF;

	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String customerEmail;
	  
	  @NotBlank
	  private String customerName;
	  
	  @NotBlank
	  private String customerSurname;
	  
	  @NotBlank
	  private String customerTelephoneNumber;
	  

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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerSurname() {
		return customerSurname;
	}

	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}

	public String getCustomerTelephoneNumber() {
		return customerTelephoneNumber;
	}

	public void setCustomerTelephoneNumber(String customerTelephoneNumber) {
		this.customerTelephoneNumber = customerTelephoneNumber;
	}
}
