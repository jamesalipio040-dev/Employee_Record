package com.eprs.model;

public record ActivityLog (
    int id,
    int user_id,
    String username,
    String action,
    String timestamp
){
    @Override
    public String toString(){
        return String.format("[%s] User '%s' (ID: %d) -> Action: %s ", timestamp, username, user_id, action);
    }
}
