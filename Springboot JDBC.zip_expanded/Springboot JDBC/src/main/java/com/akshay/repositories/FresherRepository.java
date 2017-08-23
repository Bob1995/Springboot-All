package com.akshay.repositories;





import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.akshay.model.Fresher;



@Component
public interface FresherRepository  extends CrudRepository<Fresher, Integer> {
	
	

	
}


