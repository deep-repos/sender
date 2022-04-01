package Natwest.Sender.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import Natwest.Sender.Util.AES;
import Natwest.Sender.bean.Transaction;

@RestController
public class SenderController {

	@Value("${secretKey}")
	private  String secretKey;
	
	
	@PostMapping("/transaction")
	public ResponseEntity<Void> addTransaction(@RequestBody Transaction txn )  {
		
		try {
			if(txn == null ) {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			
			
	
			ObjectMapper mapper =new ObjectMapper();
		
			//1. converting transaction object into json
			System.out.println("in sender  converting to json");		
			System.out.println(mapper.writeValueAsString(txn));
			String txnJson =mapper.writeValueAsString(txn);
			
			
			//2. encrypting json string
			String encryptedString = AES.encrypt(txnJson, secretKey) ;
			
			System.out.println("in sender encrypting json String");
			System.out.println(encryptedString);
			
			
			
			
			//3. sending encrypted json string to receiver service
			RestTemplate restTemplate  = new RestTemplate();
			System.out.println("calling receiver api...");
			ResponseEntity<Void> res =  restTemplate.postForEntity("http://localhost:8081/v1/natwest/receiver/transaction",encryptedString , Void.class);
			return res;
		
		}catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}



}
