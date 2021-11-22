package com.lovepushapp.model.response.Profile_Model;

import com.google.gson.annotations.SerializedName;

public class Plan{

	@SerializedName("view_storys")
	private String viewStorys;

	@SerializedName("voice_call")
	private String voiceCall;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("type")
	private String type;

	@SerializedName("live_video_streaming")
	private String liveVideoStreaming;

	@SerializedName("ads")
	private String ads;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("see_live_video")
	private String seeLiveVideo;

	@SerializedName("price")
	private double price;

	@SerializedName("swipe")
	private String swipe;

	@SerializedName("name")
	private String name;

	@SerializedName("messages")
	private String messages;

	@SerializedName("id")
	private int id;

	@SerializedName("network_marketing")
	private String networkMarketing;

	@SerializedName("plan_id")
	private String planId;

	@SerializedName("likes")
	private String likes;

	@SerializedName("status")
	private String status;

	public void setViewStorys(String viewStorys){
		this.viewStorys = viewStorys;
	}

	public String getViewStorys(){
		return viewStorys;
	}

	public void setVoiceCall(String voiceCall){
		this.voiceCall = voiceCall;
	}

	public String getVoiceCall(){
		return voiceCall;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setLiveVideoStreaming(String liveVideoStreaming){
		this.liveVideoStreaming = liveVideoStreaming;
	}

	public String getLiveVideoStreaming(){
		return liveVideoStreaming;
	}

	public void setAds(String ads){
		this.ads = ads;
	}

	public String getAds(){
		return ads;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setSeeLiveVideo(String seeLiveVideo){
		this.seeLiveVideo = seeLiveVideo;
	}

	public String getSeeLiveVideo(){
		return seeLiveVideo;
	}

	public void setPrice(double price){
		this.price = price;
	}

	public double getPrice(){
		return price;
	}

	public void setSwipe(String swipe){
		this.swipe = swipe;
	}

	public String getSwipe(){
		return swipe;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMessages(String messages){
		this.messages = messages;
	}

	public String getMessages(){
		return messages;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setNetworkMarketing(String networkMarketing){
		this.networkMarketing = networkMarketing;
	}

	public String getNetworkMarketing(){
		return networkMarketing;
	}

	public void setPlanId(String planId){
		this.planId = planId;
	}

	public String getPlanId(){
		return planId;
	}

	public void setLikes(String likes){
		this.likes = likes;
	}

	public String getLikes(){
		return likes;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Plan{" + 
			"view_storys = '" + viewStorys + '\'' + 
			",voice_call = '" + voiceCall + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",type = '" + type + '\'' + 
			",live_video_streaming = '" + liveVideoStreaming + '\'' + 
			",ads = '" + ads + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",see_live_video = '" + seeLiveVideo + '\'' + 
			",price = '" + price + '\'' + 
			",swipe = '" + swipe + '\'' + 
			",name = '" + name + '\'' + 
			",messages = '" + messages + '\'' + 
			",id = '" + id + '\'' + 
			",network_marketing = '" + networkMarketing + '\'' + 
			",plan_id = '" + planId + '\'' + 
			",likes = '" + likes + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}