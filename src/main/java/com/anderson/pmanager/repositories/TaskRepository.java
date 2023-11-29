package com.anderson.pmanager.repositories;

import com.anderson.pmanager.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    @Query("SELECT t FROM TaskModel t WHERE t.project.id = :projectId")
    List<TaskModel> findByProjectId(UUID projectId);
}
