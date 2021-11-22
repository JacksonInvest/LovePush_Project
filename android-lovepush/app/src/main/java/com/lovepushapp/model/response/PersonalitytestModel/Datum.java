
package com.lovepushapp.model.response.PersonalitytestModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("max_scores")
    @Expose
    private Integer maxScores;
    @SerializedName("max_answers")
    @Expose
    private Integer maxAnswers;
    @SerializedName("score_multiple")
    @Expose
    private double scoreMultiple;
    @SerializedName("get_question_desc")
    @Expose
    private GetQuestionDesc getQuestionDesc;
    @SerializedName("get_ques_options")
    @Expose
    private List<GetQuesOption> getQuesOptions = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxScores() {
        return maxScores;
    }

    public void setMaxScores(Integer maxScores) {
        this.maxScores = maxScores;
    }

    public Integer getMaxAnswers() {
        return maxAnswers;
    }

    public void setMaxAnswers(Integer maxAnswers) {
        this.maxAnswers = maxAnswers;
    }

    public double getScoreMultiple() {
        return scoreMultiple;
    }

    public void setScoreMultiple(Integer scoreMultiple) {
        this.scoreMultiple = scoreMultiple;
    }

    public GetQuestionDesc getGetQuestionDesc() {
        return getQuestionDesc;
    }

    public void setGetQuestionDesc(GetQuestionDesc getQuestionDesc) {
        this.getQuestionDesc = getQuestionDesc;
    }

    public List<GetQuesOption> getGetQuesOptions() {
        return getQuesOptions;
    }

    public void setGetQuesOptions(List<GetQuesOption> getQuesOptions) {
        this.getQuesOptions = getQuesOptions;
    }

}
