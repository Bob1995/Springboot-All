package com.akshay.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akshay.model.Fresher;
import com.akshay.repositories.FresherRepository;



@Component
public class FresherDaoImpl implements FresherDao{

	

	@Autowired
	FresherRepository fresherrepository;
	
	
	@Override
	public Iterable<Fresher> getAllFreshers() {
		
		return  fresherrepository.findAll() ;
	}

	@Override
	public void insertFresher(Fresher fresher) {
		
		fresherrepository.save(fresher);
		
	}

	@Override
	public void updateFresher(Fresher fresher) {
	
		fresherrepository.save(fresher);
		
	}

	@Override
	public void deleteFresher(int id) {
				
		fresherrepository.delete(id);;
	}

}
