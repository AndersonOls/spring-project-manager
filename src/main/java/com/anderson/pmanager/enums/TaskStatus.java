package com.anderson.pmanager.enums;

public enum TaskStatus {
    ABERTA("aberta"),
    DESENVOLVIMENTO ("desenvolvimento"),
    CONCLUIDA ("concluida");

    private String status;

    TaskStatus(String status){this.status = status;}

    public String getStatus(){ return status;}
}
