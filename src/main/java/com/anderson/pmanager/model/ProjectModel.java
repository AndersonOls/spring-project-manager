package com.anderson.pmanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_project")
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    @OneToMany
    @JoinColumn(name="project_id")
    private List<TaskModel> tasks;

}
