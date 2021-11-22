
package com.lovepushapp.referral;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteLink {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("app_links")
    @Expose
    private List<AppLink> appLinks = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AppLink> getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(List<AppLink> appLinks) {
        this.appLinks = appLinks;
    }

}
