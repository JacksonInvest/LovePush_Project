package com.lovepushapp.model.homePosts.addsmodel;

import com.google.gson.annotations.SerializedName;

public class MatchQbID{

	@SerializedName("match_info")
	private MatchInfo matchInfo;

	@SerializedName("chat_status")
	private int chatStatus;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("qb_dialog_id")
	private String qbDialogId;

	@SerializedName("match_id")
	private int matchId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setMatchInfo(MatchInfo matchInfo){
		this.matchInfo = matchInfo;
	}

	public MatchInfo getMatchInfo(){
		return matchInfo;
	}

	public void setChatStatus(int chatStatus){
		this.chatStatus = chatStatus;
	}

	public int getChatStatus(){
		return chatStatus;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setQbDialogId(String qbDialogId){
		this.qbDialogId = qbDialogId;
	}

	public String getQbDialogId(){
		return qbDialogId;
	}

	public void setMatchId(int matchId){
		this.matchId = matchId;
	}

	public int getMatchId(){
		return matchId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"MatchQbID{" + 
			"match_info = '" + matchInfo + '\'' + 
			",chat_status = '" + chatStatus + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",qb_dialog_id = '" + qbDialogId + '\'' + 
			",match_id = '" + matchId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}