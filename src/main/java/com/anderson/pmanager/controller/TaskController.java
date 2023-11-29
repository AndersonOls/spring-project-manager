package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.ProjectRecordDto;
import com.anderson.pmanager.dtos.TaskRecordDto;
import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.model.TaskModel;
import com.anderson.pmanager.repositories.ProjectRepository;
import com.anderson.pmanager.repositories.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;

    @PostMapping
    public ResponseEntity<TaskModel> createTask(@RequestBody @Valid TaskRecordDto taskRecordDto){
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel);

        UUID projectId = taskRecordDto.getProjectId();
        Optional<ProjectModel> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            ProjectModel project = optionalProject.get();

            taskModel.setProject(project);

            if (project.getTasks() == null) {
                project.setTasks(new ArrayList<>());
            }
            project.getTasks().add(taskModel);

            projectRepository.save(project);

            System.out.println("Task created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(taskModel));
        } else {
            System.out.println("Project not found.");

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAll());
    }

    @GetMapping("{projectId}")
    public ResponseEntity<List<TaskModel>> getTasksByProjectId(@PathVariable UUID projectId) {
        List<TaskModel> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(tasks);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTask(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid TaskRecordDto taskRecordDto){
        Optional<TaskModel> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        var taskModel = optionalTask.get();
        BeanUtils.copyProperties(taskRecordDto, taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskModel));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "id") UUID id){
        Optional<TaskModel> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        taskRepository.delete(optionalTask.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted sucessfully.");
    }
}
