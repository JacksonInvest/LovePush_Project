
package com.lovepushapp.model.response.Profile_Model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ProfileImage {

    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private int mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    private Uri imageUri;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

}
