package com.lovepushapp.model.homePosts.addsmodel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("image")
	private String image;

	@SerializedName("approval")
	private String approval;

	@SerializedName("matchQbID")
	private MatchQbID matchQbID;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("match_id")
	private int matchId;

	public int getBlockedBy() {
		return blockedBy;
	}

	public void setBlockedBy(int blockedBy) {
		this.blockedBy = blockedBy;
	}

	@SerializedName("blockedBy")
	private int blockedBy;

	@SerializedName("matchInfo")
	private MatchInfo matchInfo;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("title")
	private String title;

	@SerializedName("is_post_anonymously")
	private int isPostAnonymously;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("user_info")
	private UserInfo userInfo;

	@SerializedName("post_type")
	private int postType;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("radius")
	private String radius;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("status")
	private int status;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setApproval(String approval){
		this.approval = approval;
	}

	public String getApproval(){
		return approval;
	}

	public void setMatchQbID(MatchQbID matchQbID){
		this.matchQbID = matchQbID;
	}

	public MatchQbID getMatchQbID(){
		return matchQbID;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setMatchId(int matchId){
		this.matchId = matchId;
	}

	public int getMatchId(){
		return matchId;
	}

	public void setMatchInfo(MatchInfo matchInfo){
		this.matchInfo = matchInfo;
	}

	public MatchInfo getMatchInfo(){
		return matchInfo;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsPostAnonymously(int isPostAnonymously){
		this.isPostAnonymously = isPostAnonymously;
	}

	public int getIsPostAnonymously(){
		return isPostAnonymously;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo(){
		return userInfo;
	}

	public void setPostType(int postType){
		this.postType = postType;
	}

	public int getPostType(){
		return postType;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRadius(String radius){
		this.radius = radius;
	}

	public String getRadius(){
		return radius;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"image = '" + image + '\'' + 
			",approval = '" + approval + '\'' + 
			",matchQbID = '" + matchQbID + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",match_id = '" + matchId + '\'' + 
			",matchInfo = '" + matchInfo + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",title = '" + title + '\'' + 
			",is_post_anonymously = '" + isPostAnonymously + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",user_info = '" + userInfo + '\'' + 
			",post_type = '" + postType + '\'' + 
			",location = '" + location + '\'' + 
			",id = '" + id + '\'' + 
			",radius = '" + radius + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}