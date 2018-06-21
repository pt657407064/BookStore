package com.qa.restful.controllers;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qa.models.Address;
import com.qa.models.Customer;
import com.qa.services.AddressService;


@RestController
public class AddressBookRESTfulController {
	
	@Autowired
	private AddressService addressService;
	
	@RequestMapping(value="/changeAddress", method=RequestMethod.POST)
	
	// Need to use spring security to get current logged in user's information
	public ResponseEntity<Map<String, Address>> updateAddress(@ModelAttribute("logged_in_customer") Customer loggedInCustomer, @RequestBody Address address){
		
		Map<String, Address> addresses = new HashMap<String, Address>();
		
		Address billingAddress = null;
		Address shippingAddress = null;
		System.out.println("Before update ");

		System.out.println("ID "+loggedInCustomer.getCustomerId());
		System.out.println("Name"+loggedInCustomer.getFirstName());
		System.out.println("Email"+loggedInCustomer.getEmail());
		
		Address bAddress = addressService.findAddressByType(loggedInCustomer.getCustomerId(), "billing");
		
		Address sAddress = addressService.findAddressByType(loggedInCustomer.getCustomerId(), "shipping");
		
		if(bAddress!=null || sAddress!=null)
		{
		
		        int recordsUpdated = addressService.updateBillingAddress(address.getAddressLine1(),
				address.getAddressLine2(), 
				address.getCity(), 
				address.getPostcode(),
				address.getState(),
				address.getCountry(), 
				address.getPhoneNumber(), 
				loggedInCustomer.getCustomerId(), 
				address.getAddressType());
		
		if(recordsUpdated>0)
		{
			billingAddress  = addressService.findAddressByType(loggedInCustomer.getCustomerId(),"billing");
			shippingAddress  = addressService.findAddressByType(loggedInCustomer.getCustomerId(),"shipping");
			
			System.out.println("After update ");
			addresses.put("billing_address", billingAddress);
			addresses.put("shipping_address", shippingAddress);
		}
		else
		{
			addresses.put("billing_address", billingAddress);
			addresses.put("shipping_address", shippingAddress);
			
		}
		
		}
		else
		{
			Address savedAddress = addressService.saveAddress(address);
			addresses.put("billing_address", savedAddress);
			
		}
		return new ResponseEntity<>(addresses,HttpStatus.OK);
	}
	
	
}
