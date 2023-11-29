package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.ProjectRecordDto;
import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.repositories.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectRecordDto projectRecordDto) {
        // Verifica se o nome do projeto já existe
        Optional<ProjectModel> existingProject = projectRepository.findByName(projectRecordDto.name());
        if (existingProject.isPresent()) {
            // Se o projeto com esse nome já existe, retorna um erro de conflito
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: O nome do projeto já existe.");
        }

        // Se o projeto com esse nome não existe, cria um novo projeto
        ProjectModel projectModel = new ProjectModel();
        BeanUtils.copyProperties(projectRecordDto, projectModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectRepository.save(projectModel));
    }

    @GetMapping
    public ResponseEntity<List<ProjectModel>> getAllProjects(){
        return ResponseEntity.status(HttpStatus.OK).body(projectRepository.findAll());
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateProject(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProjectRecordDto projectRecordDto){
        Optional<ProjectModel> projectO = projectRepository.findById(id);
        if(projectO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
        var projectModel = projectO.get();
        BeanUtils.copyProperties(projectRecordDto, projectModel);
        return ResponseEntity.status(HttpStatus.OK).body(projectRepository.save(projectModel));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable(value = "id") UUID id){
        Optional<ProjectModel> projectO = projectRepository.findById(id);
        if(projectO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
        projectRepository.delete(projectO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Project deleted sucessfully.");
    }

}
