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

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);

            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }

//        if (projectTask.getStatus().equals("") || projectTask.getStatus() == null){
//            projectTask.setStatus("TO-DO");
//       }
            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
        }
    }


    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if (project==null){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }

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
