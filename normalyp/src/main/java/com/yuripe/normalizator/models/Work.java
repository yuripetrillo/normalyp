package com.yuripe.normalizator.models;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "works")
public class Work {
  @Id
  @Column(name = "workId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long workId;
  
  @NotNull
  private int workingHours;
  
  @NotNull
  @Column(columnDefinition = "DATE")
  private LocalDate date;
  
  @NotBlank
  @Size(max = 50)
  private String note;
  
  @ManyToOne
  private Repair repair;
  
  @ManyToOne
  private Employee employee;

  public Work() {

  }

	public Work(Long workId, @NotBlank int workingHours, @NotNull LocalDate date, @NotBlank @Size(max = 50) @Email String note, Repair repair, Employee employee) {
		super();
		this.workId = workId;
		this.workingHours = workingHours;
		this.note = note;
		this.repair = repair;
		this.employee = employee;
		this.date = date;
	}
	
	public Long getWorkId() {
		return workId;
	}
	
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	
	public int getWorkingHours() {
		return workingHours;
	}
	
	public void setWorkingHours(int workingHours) {
		this.workingHours = workingHours;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public Repair getRepair() {
		return repair;
	}
	
	public void setRepair(Repair repair) {
		this.repair = repair;
	}
	public Repair getEmployee() {
		return repair;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}	  

	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}


}