package com.project.ppmtool.service;

import com.project.ppmtool.entity.Backlog;
import com.project.ppmtool.entity.Project;
import com.project.ppmtool.entity.User;
import com.project.ppmtool.exception.ProjectIdentifierException;
import com.project.ppmtool.repository.BacklogRepository;
import com.project.ppmtool.repository.ProjectRepository;
import com.project.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private BacklogRepository backlogRepository;
    private UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
        this.userRepository = userRepository;
    }

    public Project saveOrUpdateProject(Project project, String username){
        try {
            String getProjectId = project.getProjectIdentifier().toUpperCase();
            User user = userRepository.findByUserName(username);
            project.setUser(user);
            project.setProjectLeader(user.getUserName());
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
