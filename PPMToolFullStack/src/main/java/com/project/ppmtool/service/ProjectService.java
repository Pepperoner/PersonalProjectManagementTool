package com.project.ppmtool.service;

import com.project.ppmtool.entity.Backlog;
import com.project.ppmtool.entity.Project;
import com.project.ppmtool.exception.ProjectIdentifierException;
import com.project.ppmtool.repository.BacklogRepository;
import com.project.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private BacklogRepository backlogRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
    }

    public Project saveOrUpdateProject(Project project){

        try {
            String getProjectId = project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(getProjectId);
            if (project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(getProjectId);
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(getProjectId));
            }
            return projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdentifierException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null){
            throw new ProjectIdentifierException("Project ID '"+projectId.toUpperCase()+"' doesn't exists");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null){
            throw new ProjectIdentifierException("Cannot delete Project with ID '"+projectId.toUpperCase()+"'. This Project doesn't exist");
        }
        projectRepository.delete(project);
    }
}
