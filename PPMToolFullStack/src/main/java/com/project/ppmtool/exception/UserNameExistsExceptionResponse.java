package com.project.ppmtool.exception;

public class UserNameExistsExceptionResponse {

    private String userName;

    public UserNameExistsExceptionResponse(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
