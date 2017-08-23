package com.akshay.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SharedSessionContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akshay.model.Project;

@Repository
@Transactional
public class ProjectDaoImpl implements ProjectDao{

	@Autowired
	EntityManager entitymanager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAllProjects() {
		Session session = (Session) entitymanager.unwrap(Project.class);

		return ((SharedSessionContract) session).createCriteria(Project.class).list();
	}

	@Override
	public void deleteProject(String id) {
	
		entitymanager.remove(id);
	}

	@Override
	public void updateProject(Project project) {
		System.out.println("hello1");

	}

	@Override
	public void insertProject(Project project) {
		 entitymanager.persist(project);
		 entitymanager.flush();
	}

	

}
