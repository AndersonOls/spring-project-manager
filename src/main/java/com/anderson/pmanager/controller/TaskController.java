package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.ProjectRecordDto;
import com.anderson.pmanager.dtos.TaskRecordDto;
import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.model.TaskModel;
import com.anderson.pmanager.model.UserModel;
import com.anderson.pmanager.repositories.ProjectRepository;
import com.anderson.pmanager.repositories.TaskRepository;
import com.anderson.pmanager.repositories.UserRepository;
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
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRecordDto taskRecordDto) {
        Optional<ProjectModel> optionalProject = projectRepository.findById(taskRecordDto.projectId());
        if (optionalProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }

        Optional<UserModel> optionalUser = userRepository.findById(taskRecordDto.responsavelId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        TaskModel taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel);

        taskModel.setProject(optionalProject.get());
        taskModel.setResponsavel(optionalUser.get());

        TaskModel savedTask = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findAll());
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
