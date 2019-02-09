package com.project.ppmtool.service;

import com.project.ppmtool.entity.Backlog;
import com.project.ppmtool.entity.Project;
import com.project.ppmtool.entity.User;
import com.project.ppmtool.exception.ProjectIdentifierException;
import com.project.ppmtool.exception.ProjectNotFoundException;
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

    public Project saveOrUpdateProject(Project project, String userName){
        try {
            String getProjectId = project.getProjectIdentifier().toUpperCase();
            User user = userRepository.findByUserName(userName);
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

    public Project findProjectByIdentifier(String projectId, String userName){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null){
            throw new ProjectIdentifierException("Project ID '"+projectId.toUpperCase()+"' doesn't exists");
        }

        if (!project.getProjectLeader().equals(userName)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String userName){
        return projectRepository.findAllByProjectLeader(userName);
    }

    public void deleteProjectByIdentifier(String projectId, String userName){
        projectRepository.delete(findProjectByIdentifier(projectId, userName));
    }
}
