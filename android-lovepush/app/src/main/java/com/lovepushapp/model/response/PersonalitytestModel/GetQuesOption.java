
package com.lovepushapp.model.response.PersonalitytestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuesOption {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("get_ques_option_desc")
    @Expose
    private GetQuesOptionDesc getQuesOptionDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public GetQuesOptionDesc getGetQuesOptionDesc() {
        return getQuesOptionDesc;
    }

    public void setGetQuesOptionDesc(GetQuesOptionDesc getQuesOptionDesc) {
        this.getQuesOptionDesc = getQuesOptionDesc;
    }

}
