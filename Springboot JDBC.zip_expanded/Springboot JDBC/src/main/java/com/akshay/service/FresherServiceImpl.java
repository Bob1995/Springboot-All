package com.akshay.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akshay.dao.FresherDao;
import com.akshay.model.Fresher;


@Component
public class FresherServiceImpl implements FresherService {



	@Autowired
	FresherDao fresherdao;
	@Override
	public Iterable<Fresher> getAllFreshers() {

		return fresherdao.getAllFreshers();
	}

	@Override
	public void insertFresher(Fresher fresher) {


		fresherdao.insertFresher(fresher);
	}

	@Override
	public void updateFresher(Fresher fresher) {

		fresherdao.updateFresher(fresher);

	}

	@Override
	public void deleteFresher(int id) {

		fresherdao.deleteFresher(id);

	}



}





