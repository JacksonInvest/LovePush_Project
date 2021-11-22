package com.lovepushapp.model.response;

public class ForgotPassResponse {

    /**
     * status : 200
     * verification_code : 8690
     * message : Verification code has been send successfully.
     * id : 12
     * phone_code : +91
     * phone : 9457819996
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
