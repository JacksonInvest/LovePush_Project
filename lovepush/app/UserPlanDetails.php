<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserPlanDetails extends Model
{

    protected $fillable = [
        "user_id",
        "plan_id",
        "type",
        "name",
        "price",
        "messages",
        "sending_requests",
        "see_love_requests",
        "recommended_profiles",
        "recommended_profiles_started_at",
        "ads",
        "ads_started_at",
    ];

	protected $table = "user_plan_details";

	public function userInfo() {
		return $this->belongsTo('App\User','user_id','id');
	}
}
