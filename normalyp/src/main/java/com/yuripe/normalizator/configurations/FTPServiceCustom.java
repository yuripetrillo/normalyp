package com.yuripe.normalizator.configurations;

import org.springframework.stereotype.Service;
import com.yuripe.core.library.services.FTPService;


@Service
public class FTPServiceCustom {

	public String call() {
		FTPService ftp = new FTPService();
		return ftp.message();
	}
	
}
