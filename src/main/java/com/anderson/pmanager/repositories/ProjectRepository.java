package com.anderson.pmanager.repositories;

import com.anderson.pmanager.model.ProjectModel;
import com.anderson.pmanager.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectModel, UUID> {
}
