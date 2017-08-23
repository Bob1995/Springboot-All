package com.akshay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.akshay.dao.ProjectDao;
import com.akshay.model.Project;
@Service
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	ProjectDao projectdao;

	@Override
	public List<Project> getAllProjects() {
		// TODO Auto-generated method stub
		return projectdao.getAllProjects();
	}

	@Override
	public void insertProject(Project project) {
		
		 projectdao.insertProject(project);
	}

	@Override
	public void updateProject(Project project) {
		projectdao.updateProject(project);
		
	}

	@Override
	public void deleteProject(String id) {
		projectdao.deleteProject(id);
		
	}

	

}
