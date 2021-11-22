package com.lovepushapp.model.quick;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by varinder on 14/2/19.
 */

public class MessageTemplateData {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}
