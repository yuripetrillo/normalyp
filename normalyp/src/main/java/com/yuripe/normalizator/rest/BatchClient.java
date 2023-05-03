package com.yuripe.normalizator.rest;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BatchClient {
	
	  @Async
	  public CompletableFuture<ResponseEntity<String>> callBatch(String uri) {

	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> res = restTemplate.postForEntity(uri, null, null);
	    return CompletableFuture.completedFuture(res);
		  
	}

}
