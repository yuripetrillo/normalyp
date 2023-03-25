package com.yuripe.normalizator.payload.request;

import javax.validation.constraints.*;

public class NewJobRequest {
	  @NotBlank
	  private int workingHours;
	  
	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String note;

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
}
