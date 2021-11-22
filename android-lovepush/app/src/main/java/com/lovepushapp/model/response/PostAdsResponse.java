package com.lovepushapp.model.response;

public class PostAdsResponse {

    /**
     * message : The image may not be greater than 2048 kilobytes.
     * status : 400
     */

    private String message;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
