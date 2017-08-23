package com.akshay.service;



import java.util.List;

import org.springframework.stereotype.Component;

import com.akshay.model.Address;




@Component
public interface AddressService {
	
	public List<Address> getAllAddress();	
	public Address FindOne(long id);
	public  Address  insertAddress(Address address) ;
	public  void updateAddress(Address address);
	public  void deleteAddress();
}
