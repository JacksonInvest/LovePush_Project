
package com.lovepushapp.model.response.Profile_Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class GetProfile {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Integer mStatus;
    @SerializedName("token")
    private String mToken;
    @SerializedName("isSocialUser")
    private boolean isSocialUser;


    @SerializedName("f_que_ans")
    private List<Integer> f_que_ans;



    public void setIsSocialUser(boolean isSocialUser){
        this.isSocialUser = isSocialUser;
    }


    public boolean isIsSocialUser(){
        return isSocialUser;
    }

    public List<Integer> getF_que_ans() {
        return f_que_ans;
    }

    public void setF_que_ans(List<Integer> f_que_ans) {
        this.f_que_ans = f_que_ans;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

}
