package com.akshay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.akshay.model.Address;
import com.akshay.repositories.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	AddressRepository addressrepository;

	@Override
	public List<Address> getAllAddress() {

		return addressrepository.findAll();
	}

	@Override
	public Address insertAddress(Address address) {

		return addressrepository.save(address);

	}

	@Override
	public void updateAddress(Address address) {
		addressrepository.save(address);
	}

	@Override
	public void deleteAddress() {

		addressrepository.deleteAll();
	}

	@Override
	public Address FindOne(long id) {

		return addressrepository.findOne(id);
	}

}
