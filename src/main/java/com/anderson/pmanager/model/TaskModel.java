package com.anderson.pmanager.model;

import com.anderson.pmanager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    private TaskStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserModel responsavel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private ProjectModel project;

}
