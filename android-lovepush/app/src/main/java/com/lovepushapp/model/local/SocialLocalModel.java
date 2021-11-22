package com.lovepushapp.model.local;

public class SocialLocalModel {
    public String email, accessToken, lname, fname, id, image, socialType, gender;

    public interface TYPE {
        String FACEBOOK = "FACEBOOK";
        String GMAIL = "GMAIL";
    }
}
