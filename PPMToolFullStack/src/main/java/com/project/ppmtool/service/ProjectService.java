package com.project.ppmtool.service;

import com.project.ppmtool.entity.Project;
import com.project.ppmtool.exception.ProjectIdentifierException;
import com.project.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveOrUpdateProject(Project project){

        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdentifierException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }
}
