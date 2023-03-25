package com.yuripe.normalizator.models;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "jobs")
public class Job {
  @Id
  @Column(name = "jobId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobId;
  
  @NotNull
  private int jobingHours;
  
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

  public Job() {

  }

	public Job(Long jobId, @NotBlank int jobingHours, @NotNull LocalDate date, @NotBlank @Size(max = 50) @Email String note, Repair repair, Employee employee) {
		super();
		this.jobId = jobId;
		this.jobingHours = jobingHours;
		this.note = note;
		this.repair = repair;
		this.employee = employee;
		this.date = date;
	}
	
	public Long getJobId() {
		return jobId;
	}
	
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	public int getJobingHours() {
		return jobingHours;
	}
	
	public void setJobingHours(int jobingHours) {
		this.jobingHours = jobingHours;
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