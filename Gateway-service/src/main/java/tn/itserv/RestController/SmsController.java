package tn.itserv.RestController;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@RestController
@RequestMapping("/cms-sms")
public class SmsController {
	private static final String SMS_SERVICE="cms-sms";
	@Autowired 
	private RestTemplate restTemplate;

	
	@GetMapping("/startTask")
	@CircuitBreaker(name=SMS_SERVICE,fallbackMethod="smsFallback")
	public ResponseEntity<String> smsService(){
		String response =restTemplate.getForObject("http://localhost:8082/startTask", String.class);
				return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	@PostMapping("/count")
	@CircuitBreaker(name=SMS_SERVICE,fallbackMethod="smsFallback")
	public ResponseEntity<String> countFiles(@RequestBody String directoryName){
		String response =restTemplate.postForObject("http://localhost:8082/count",directoryName ,String.class);
				return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	public ResponseEntity<String> smsFallback(Exception t){
		System.out.println("Exception called : " +t.getMessage());
		
		return new ResponseEntity<String>("Service is Down " ,HttpStatus.OK);
	}
}
