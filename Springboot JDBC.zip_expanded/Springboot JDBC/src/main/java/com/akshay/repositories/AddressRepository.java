package com.akshay.repositories;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.akshay.model.Address;

@Component
public interface AddressRepository  extends JpaRepository<Address, Long>{

	
	
	

}
