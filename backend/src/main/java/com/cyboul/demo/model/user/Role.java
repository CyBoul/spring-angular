package com.cyboul.demo.model.user;

public enum Role {
    ROLE_USER, ROLE_ADMIN;
    public String toString(){
        return this.name();
    }
}