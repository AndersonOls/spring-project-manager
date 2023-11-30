package com.anderson.pmanager.enums;

public enum UserRole {
    ADMIN("admin"),
    MANAGER("manager"),
    MEMBER("member");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
