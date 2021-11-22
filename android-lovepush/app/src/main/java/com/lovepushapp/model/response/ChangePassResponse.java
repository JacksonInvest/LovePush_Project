package com.lovepushapp.model.response;

public class ChangePassResponse {


    /**
     * status : 200
     * message : Password has been changed successfully.
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
