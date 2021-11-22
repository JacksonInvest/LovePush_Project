package com.lovepushapp.model.response;

public class SendVerificationCodeResponse {


    /**
     * status : 200
     * verification_code : 8202
     * message : Verification code has been send successfully.
     */

    private int status;
    private int verification_code;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(int verification_code) {
        this.verification_code = verification_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
