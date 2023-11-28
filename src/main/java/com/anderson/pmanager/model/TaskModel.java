package com.anderson.pmanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String description;
    private String status;
    private String responsavel;
    @ManyToOne
    private ProjectModel project;

}
