package com.anderson.pmanager.dtos;

import com.anderson.pmanager.enums.UserRole;
import com.anderson.pmanager.model.TaskModel;

public record RegisterDto(String name, String login, String password, UserRole role) {
}
