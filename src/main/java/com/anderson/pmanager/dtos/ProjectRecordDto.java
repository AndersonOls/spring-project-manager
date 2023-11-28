package com.anderson.pmanager.dtos;


import com.anderson.pmanager.model.TaskModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectRecordDto(@NotBlank  String name, @NotNull String description) {
}
