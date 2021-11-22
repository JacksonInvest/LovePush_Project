
package com.lovepushapp.model.response.ExploreRadar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class userDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("quickblox_id")
    @Expose
    private String quickbloxId;
    @SerializedName("distance")
    @Expose
    private Double distance;

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
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
        return quickbloxId;
    }

    public void setQuickbloxId(String quickbloxId) {
        this.quickbloxId = quickbloxId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
