package com.akshay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akshay.model.Project;
import com.akshay.service.ProjectService;

@Controller
public class ProjectController {
	

	@Autowired
	ProjectService projectservice;

	
	
    @RequestMapping(value = "/getAllProjects",method=RequestMethod.GET)
    public @ResponseBody List<Project> getAllProjects() {
        
    	System.out.println("hello1");
    	return  projectservice.getAllProjects();
    	
    }
	
	  
    @RequestMapping(value="/insertProject",method=RequestMethod.POST)
    public @ResponseBody void insertProject(@RequestBody Project project){
    	 projectservice.insertProject(project);
		   	
    }

}
