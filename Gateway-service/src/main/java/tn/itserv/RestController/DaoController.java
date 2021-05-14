package tn.itserv.RestController;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import tn.itserv.model.SasCampaign;
@RestController

public class DaoController {
	private static final String DAO_SERVICE="cms-dao";
	@Autowired 
	private RestTemplate restTemplate;
	

	
	@GetMapping("/cms-dao/")
	@CircuitBreaker(name=DAO_SERVICE,fallbackMethod="daoFallback")
	public ResponseEntity<String> daoService(){
		String response =restTemplate.getForObject("http://localhost:8100/sas_campaign", String.class);
				return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	@PutMapping("/cms-dao/updatecampaigns/{code}")
	@CircuitBreaker(name=DAO_SERVICE,fallbackMethod="daoFallback")
	public ResponseEntity<String> updatecampaigns(@PathVariable ("code")String code,@RequestBody Map<String,Integer> updates){
		restTemplate.put("http://localhost:8100/updatecampaigns/"+code, updates);
				return new ResponseEntity<String> (HttpStatus.OK);
	}
	@PostMapping("/cms-dao/sas_campaign")
	@CircuitBreaker(name=DAO_SERVICE,fallbackMethod="daoFallback")
	public ResponseEntity<String> addcampaign(@RequestBody SasCampaign o){
		if ((o.getSasCampaignCode() =="" ))
		{	throw new NullPointerException( );}
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8100/sas_campaign", o, String.class);
		String response =responseEntity.getBody();
			return responseEntity;
	}
	
	public ResponseEntity<String> daoFallback(Exception t){
		System.out.println("Exeption called : " +t);
		if (t.getClass().equals(NullPointerException.class))
			{return new ResponseEntity<String>("null values inaccepted" ,HttpStatus.OK);}
		return new ResponseEntity<String>("Service is Down " ,HttpStatus.OK);
	}
}
