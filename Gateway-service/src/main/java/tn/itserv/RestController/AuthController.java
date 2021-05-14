package tn.itserv.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@RestController
@RequestMapping("/cms-auth")

public class AuthController {
	private static final String Authentication_SERVICE="cms-auth";
	@Autowired 
	private RestTemplate restTemplate;
	
	
	@PostMapping("/signin")
	@CircuitBreaker(name=Authentication_SERVICE,fallbackMethod="authFallback")
	public ResponseEntity<String> authService(@RequestBody Object o){
		String response =restTemplate.postForObject("http://localhost:8050/signin", o,String.class );
				return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	public ResponseEntity<String> authFallback(Exception t){
		System.out.println("Exeption called : " +t.getMessage());
		
		return new ResponseEntity<String>("Service is Down " ,HttpStatus.OK);
	}
}
