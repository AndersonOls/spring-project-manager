package com.anderson.pmanager.dtos;

import com.anderson.pmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

public record TaskRecordDto (@NotBlank String description, @NotNull TaskStatus status, @Getter UUID responsavelId, @Getter UUID projectId){
}
