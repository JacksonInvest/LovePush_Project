
package com.lovepushapp.model.response.PersonalitytestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuesOptionDesc {

    @SerializedName("ques_option_id")
    @Expose
    private Integer quesOptionId;
    @SerializedName("language_id")
    @Expose
    private Integer languageId;
    @SerializedName("option")
    @Expose
    private String option;

    public Integer getQuesOptionId() {
        return quesOptionId;
    }

    public void setQuesOptionId(Integer quesOptionId) {
        this.quesOptionId = quesOptionId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

}
