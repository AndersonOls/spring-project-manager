package com.anderson.pmanager.controller;

import com.anderson.pmanager.dtos.ProjectRecordDto;
import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.repositories.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping
    public ResponseEntity<ProjectModel> createProject(@RequestBody @Valid ProjectRecordDto projectRecordDto){
        var projectModel = new ProjectModel();
        BeanUtils.copyProperties(projectRecordDto, projectModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectRepository.save(projectModel));
    }

}
