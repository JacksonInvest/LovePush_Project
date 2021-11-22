package com.lovepushapp.model.response;

public class SaveForgotPassWordResponse {


    /**
     * status : 200
     * message : Your password has been changed successfully.
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
