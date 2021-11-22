
package com.lovepushapp.model.response.PersonalitytestModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQuestionDesc {

    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @SerializedName("language_id")
    @Expose
    private Integer languageId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
