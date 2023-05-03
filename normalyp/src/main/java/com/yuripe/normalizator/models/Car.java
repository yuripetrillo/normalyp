package com.yuripe.normalizator.models;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cars", schema = "Application",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "plate")})
public class Car {
  @Id
  @Column(name = "carId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long carId;

  @NotBlank
  @Size(max = 50)
  private String make;

  @NotBlank
  @Size(max = 50)
  private String model;

  @NotBlank
  @Size(max = 15)
  private String plate;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private EVehicleType type;
  
  @ManyToOne
  @JoinColumn(name="customerId", nullable=false)
  private Customer customer;
 
  public Car() {
  }

  public Long getId() {
    return carId;
  }

  public void setId(Long id) {
    this.carId = id;
  }


	
	public String getMake() {
		return make;
	}
	
	
	
	public void setMake(String make) {
		this.make = make;
	}
	
	
	
	public String getModel() {
		return model;
	}
	
	
	
	public void setModel(String model) {
		this.model = model;
	}
	
	
	
	public String getPlate() {
		return plate;
	}
	
	
	
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	
	
	public EVehicleType getType() {
		return type;
	}
	
	
	
	public void setType(EVehicleType type) {
		this.type = type;
	}
	
	public Customer getCustomer() {
		return customer;
	}



	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

  
}
