package com.yuripe.normalizator.configurations;

import java.io.IOException;
import org.springframework.stereotype.Service;
import com.yuripe.core.library.services.FTPService;
import com.yuripe.core.library.utility.FtpClient;


@Service
public class FTPServiceCustom {
	
	private final FTPService ftp = new FTPService();

	public String hello() {
		return ftp.message();
	}
	
	public boolean checkFtpServerState(FtpClient ftpClient) throws IOException {
		return ftp.checkFTPServerStatus(ftpClient);
	}
	
	public boolean checkValidMandatoryInput(FtpClient ftpClient, String filePattern) throws IOException {
		return ftp.checkFTPServerTargetFile(ftpClient, filePattern);
	}
	
	public boolean getFileFromSFTP(String filePattern, String destination) throws IOException {
		return ftp.downloadFile(filePattern, destination);
	}
}
