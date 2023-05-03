package com.yuripe.normalizator.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "file_configuration", schema = "BATCH",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "configurationCode")})
public class FileConfiguration {

	@Id
    @Column(name = "configurationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long configurationId;
	
	@NotBlank
    @Size(max = 50)
	private String configurationCode;
	
	@NotBlank
    @Size(max = 50)
	private String filePattern;
	
	@NotBlank
    @Size(max = 50)
	private String scheduleCode;
	
	//@ManyToOne
    //@JoinColumn(name = "sftp_code")
	@NotBlank
    @Size(max = 50)
	private SFTP ftpCode;
	
	@NotBlank
    @Size(max = 50)
	private String isActive;
	
	
	
	public FileConfiguration() {
		
	}



	public Long getConfigurationId() {
		return configurationId;
	}



	public void setConfigurationId(Long configurationId) {
		this.configurationId = configurationId;
	}



	public String getConfigurationCode() {
		return configurationCode;
	}



	public void setConfigurationCode(String configurationCode) {
		this.configurationCode = configurationCode;
	}



	public String getFilePattern() {
		return filePattern;
	}



	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}



	public String getScheduleCode() {
		return scheduleCode;
	}



	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}



	public SFTP getFtpCode() {
		return ftpCode;
	}



	public void setFtpCode(SFTP ftpCode) {
		this.ftpCode = ftpCode;
	}



	public String getIsActive() {
		return isActive;
	}



	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	
}
