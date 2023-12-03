package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.ProjectRecordDto;
import com.anderson.pmanager.dtos.TaskRecordDto;
import com.anderson.pmanager.enums.UserRole;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRecordDto taskRecordDto, Authentication authentication) {
        // Verifica se o projeto existe
        Optional<ProjectModel> optionalProject = projectRepository.findById(taskRecordDto.projectId());
        if (optionalProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }

        // Acessa o usuário autenticado (autenticado pelo SecurityFilter)
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // Se a role do usuário for MEMBER, verifica se o responsavelId corresponde ao ID do usuário autenticado
        if (currentUser.getRole() == UserRole.MEMBER && !taskRecordDto.responsavelId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Members can only create tasks for themselves.");
        }

        // Verifica se o usuário responsável existe
        Optional<UserModel> optionalUser = userRepository.findById(taskRecordDto.responsavelId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Cria a task
        TaskModel taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel);

        // Define o projeto e o usuário responsável pela task
        taskModel.setProject(optionalProject.get());
        taskModel.setResponsavel(optionalUser.get());

        // Salva a task no repositório
        TaskModel savedTask = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(Authentication authentication) {
        UserModel currentUser = (UserModel) authentication.getPrincipal();
        List<TaskModel> tasks;

        // Se o usuário for MEMBER, retorna apenas as tarefas em que ele é responsável
        if (currentUser.getRole() == UserRole.MEMBER) {
            tasks = taskRepository.findAllByResponsavel(currentUser);
        } else {
            // Se não for MEMBER, retorna todas as tarefas
            tasks = taskRepository.findAll();
        }

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTask(@AuthenticationPrincipal UserModel currentUser,
                                             @PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid TaskRecordDto taskRecordDto) {
        Optional<TaskModel> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }

        TaskModel taskModel = optionalTask.get();

        // Se o usuário for MEMBER e não for o responsável pela tarefa, não permitir a atualização
        if (currentUser.getRole() == UserRole.MEMBER && !taskModel.getResponsavel().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Members can only update their own tasks.");
        }

        // Se o usuário for MEMBER e estiver tentando mudar o responsável da tarefa, não permitir
        if (currentUser.getRole() == UserRole.MEMBER && !taskRecordDto.responsavelId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Members cannot change the task's responsible.");
        }

        BeanUtils.copyProperties(taskRecordDto, taskModel, "responsavelId"); // Ignora o responsavelId no caso de MEMBER
        TaskModel updatedTask = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTask(@AuthenticationPrincipal UserModel currentUser,
                                             @PathVariable(value = "id") UUID id) {
        Optional<TaskModel> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }

        TaskModel task = optionalTask.get();

        // Verifica se o usuário é MEMBER e se é o responsável pela tarefa
        if (currentUser.getRole() == UserRole.MEMBER && !task.getResponsavel().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Members can only delete their own tasks.");
        }

        // Procede com a deleção da tarefa
        taskRepository.delete(task);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully.");
    }
}
