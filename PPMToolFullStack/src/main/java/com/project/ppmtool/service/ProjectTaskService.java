package com.project.ppmtool.service;

import com.project.ppmtool.entity.Backlog;
import com.project.ppmtool.entity.Project;
import com.project.ppmtool.entity.ProjectTask;
import com.project.ppmtool.exception.ProjectNotFoundException;
import com.project.ppmtool.repository.BacklogRepository;
import com.project.ppmtool.repository.ProjectRepository;
import com.project.ppmtool.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    private BacklogRepository backlogRepository;
    private ProjectTaskRepository projectTaskRepository;
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String userName){

            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, userName).getBacklog();
            projectTask.setBacklog(backlog);

            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }

//        if (projectTask.getStatus().equals("") || projectTask.getStatus() == null){
//            projectTask.setStatus("TO-DO");
//       }
            return projectTaskRepository.save(projectTask);

    }


    public Iterable<ProjectTask> findBacklogById(String id, String userName) {

        projectService.findProjectByIdentifier(id, userName);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
        projectTaskRepository.delete(projectTask);
    }
}
