package com.akshay.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akshay.model.Address;
import com.akshay.service.AddressService;


@Controller
public class AddressController {
	
	@Autowired
	AddressService addressservice;

	

	@RequestMapping(value = "/getAllAddress",method=RequestMethod.GET)
	public @ResponseBody List<Address> getAllAddress() {

		System.out.println("hello1");
		
		return addressservice.getAllAddress();

	}

	
	@RequestMapping(value="/insertAddress",method=RequestMethod.POST)
	public @ResponseBody Address  insertAddress(@RequestBody  Address address){

			System.out.println(address);
			return addressservice.insertAddress(address);
		

	}
	
	

	@RequestMapping(value="/updateAddress",method=RequestMethod.PUT)
	public @ResponseBody void updateAddress(@RequestBody  Address address){

		addressservice.updateAddress(address);
			
	}

	

	@RequestMapping(value="/deleteAddress",method=RequestMethod.DELETE)
	public @ResponseBody void deleteAddress(){

		addressservice.deleteAddress();
	

	}


	

}
