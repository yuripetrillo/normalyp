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
		return ftp.checkFTPServerTargetFile(ftpClient);
	}
	
	public boolean checkValidMandatoryInput(FtpClient ftpClient) throws IOException {
		ftp.checkFTPServerTargetFile(ftpClient);
		return false;
	}
	
}
