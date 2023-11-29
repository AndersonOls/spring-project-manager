package com.anderson.pmanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

public record TaskRecordDto (@NotBlank String description, @NotNull String status, String responsavel, @Getter UUID projectId){
}
