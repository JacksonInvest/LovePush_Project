package com.lovepushapp.referral.model;

import java.util.List;

public class Level {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String profile_image;
    private Integer gender;
    private Integer age;
    private String quickblox_id;
    private String myReferalCode;
    private String referalBy;
    private List<MatchInfo1> match_info1 = null;
    private List<MatchInfo2> match_info2 = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profile_image;
    }

    public void setProfileImage(String profileImage) {
        this.profile_image = profileImage;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getQuickbloxId() {
        return quickblox_id;
    }

    public void setQuickbloxId(String quickbloxId) {
        this.quickblox_id = quickbloxId;
    }

    public String getMyReferalCode() {
        return myReferalCode;
    }

    public void setMyReferalCode(String myReferalCode) {
        this.myReferalCode = myReferalCode;
    }

    public String getReferalBy() {
        return referalBy;
    }

    public void setReferalBy(String referalBy) {
        this.referalBy = referalBy;
    }

    public List<MatchInfo1> getMatchInfo1() {
        return match_info1;
    }

    public void setMatchInfo1(List<MatchInfo1> matchInfo1) {
        this.match_info1 = matchInfo1;
    }

    public List<MatchInfo2> getMatchInfo2() {
        return match_info2;
    }

    public void setMatchInfo2(List<MatchInfo2> matchInfo2) {
        this.match_info2 = matchInfo2;
    }

}