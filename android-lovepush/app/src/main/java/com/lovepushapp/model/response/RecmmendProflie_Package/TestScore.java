
package com.lovepushapp.model.response.RecmmendProflie_Package;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TestScore {

    @SerializedName("id")
    private Long mId;
    @SerializedName("scores")
    private String mScores;
    @SerializedName("test_id_1")
    private Long mTestId1;
    @SerializedName("test_id_2")
    private Long mTestId2;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getScores() {
        return mScores;
    }

    public void setScores(String scores) {
        mScores = scores;
    }

    public Long getTestId1() {
        return mTestId1;
    }

    public void setTestId1(Long testId1) {
        mTestId1 = testId1;
    }

    public Long getTestId2() {
        return mTestId2;
    }

    public void setTestId2(Long testId2) {
        mTestId2 = testId2;
    }

}
