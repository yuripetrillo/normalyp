package com.yuripe.normalizator.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "customers", schema = "Application", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "telephoneNumber")})
public class Customer {
  @Id
  @Column(name = "customerId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long customerId;

  @NotBlank
  @Size(max = 40)
  private String name;
  
  @NotBlank
  @Size(max = 40)
  private String surname;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String telephoneNumber;
  
  @NotBlank
  @NaturalId
  @Column(name="customerCF")
  @Size(min = 16, max = 16)
  private String CF;


  public Customer() {
  }



  public Customer(Long id, String name, String surname, String email,String telephoneNumber, String CF) {
	super();
	this.customerId = id;
	this.name = name;
	this.surname = surname;
	this.email = email;
	this.telephoneNumber = telephoneNumber;
	this.CF = CF;
}



	public Long getId() {
	    return customerId;
	  }
	
	  public void setId(Long id) {
	    this.customerId = id;
	  }
	
	
	
	public String getName() {
		return name;
	}
	
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getSurname() {
		return surname;
	}
	
	
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
	
	public String getEmail() {
		return email;
	}
	
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	
	public Long getCustomerId() {
		return customerId;
	}



	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}



	public String getCF() {
		return CF;
	}



	public void setCF(String cF) {
		CF = cF;
	}

}
