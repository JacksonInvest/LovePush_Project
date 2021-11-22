package com.lovepushapp.model.response.Profile_Model;

import com.google.gson.annotations.SerializedName;

public class PlanDetail{

	@SerializedName("plan")
	private Plan plan;

	@SerializedName("plan_id")
	private int planId;

	public void setPlan(Plan plan){
		this.plan = plan;
	}

	public Plan getPlan(){
		return plan;
	}

	public void setPlanId(int planId){
		this.planId = planId;
	}

	public int getPlanId(){
		return planId;
	}

	@Override
 	public String toString(){
		return 
			"PlanDetail{" + 
			"plan = '" + plan + '\'' + 
			",plan_id = '" + planId + '\'' + 
			"}";
		}
}