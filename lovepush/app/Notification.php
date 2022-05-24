<?php

namespace App;

use App\NotificationDesc;
use Illuminate\Support\Facades\Log;
use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{

	public function userInfo() {
		return $this->belongsTo('App\User','user_id','id')
					->select('id','name','email','profile_image','quickblox_id','receive_notification');
	}

	public function eventInfo() {
		return $this->belongsTo('App\ConnectRequest','event_id','id')
					->select('id','from_user_id','to_user_id','status');
	}

	public function messEventInfo() {
		return $this->belongsTo('App\ChatRequest','event_id','id')
					->select('id','from_user_id','to_user_id','status');
	}

	public function notificationMessage() {
		return $this->hasOne('App\NotificationDesc','notification_id','id')
					->select('notification_id','message');
	}

	public static function saveNotification($event_type, $event_id, $user_id, $to_user_id) {

		$exist = Notification::where('event_type', $event_type)
							->where('event_id', $event_id)
							->where('user_id', $user_id)
							->where('to_user_id', $to_user_id)
							->first();

		if(!$exist) {
			$noti = new Notification;
			$noti->event_type = $event_type;
			$noti->event_id   = $event_id;
			$noti->user_id    = $user_id;
			$noti->to_user_id = $to_user_id;
			if($noti->save()) {

				//Get to_user_id name
				$name = User::where('id', $user_id)->value('name');

				$en_mesg = '';
				$de_mesg = '';
				if($event_type == 'L') { //Like
					// $en_mesg = $name.' has like your profile.';
					// $de_mesg = $name.' hat dein Profil gefallen.';
					$en_mesg = $name.' has sent you Date Request.';
					$de_mesg = $name.' hat dein Profil gefallen.';
				} else if($event_type == 'S_C_R') { //Send Connect request
					$en_mesg = $name.' has sent you Connect Request';
					$de_mesg = $name.' hat Ihnen eine Verbindungsanforderung gesendet.';
				} else if($event_type == 'A_C_R') { //Send Connect request
					$en_mesg = $name.' accepted a connect request.';
					$de_mesg = $name.' hat eine Verbindungsanforderung angenommen.';
				} else if($event_type == 'S_M_R') { //Send chat messaging request
					$en_mesg = $name.' has sent you Chat Request.';
					$de_mesg = $name.' hat dir eine Chatanfrage gesendet.';
				} else if($event_type == 'A_M_R') { //Send chat messaging request
					$en_mesg = $name.' accepted a chat request.';
					$de_mesg = $name.' hat eine Chatanfrage gesendet.';
				}
				else if($event_type == 'S_L_R') { //Send chat messaging request
					$en_mesg = $name.' has sent you Date Request.';
					$de_mesg = $name.' hat Ihnen eine Terminanfrage gesendet.';
				}
				else if($event_type == 'A_L_R') { //Send chat messaging request
					$en_mesg = $name.' has accepted your Date Request.';
					$de_mesg = $name.' hat Ihre Terminanfrage angenommen.';
				}
				else if($event_type == 'P_L_R'){
					$en_mesg = $name.' has liked your post.';
					$de_mesg = $name.' hat Ihren Beitrag gefallen.';
				}

				$messages = [$en_mesg, $de_mesg];

				for ($i=0; $i <= 1; $i++) {

                    if ($i==0) {
                        $to_user = User::where('id', $to_user_id)->first();
                        $device_token = $to_user->device_token;
                        (new Notification)->firebase_sender([$device_token], PROJECT_NAME, $messages[$i]);
                    }

					$desc = new NotificationDesc;
					$desc->notification_id = $noti->id;
					$desc->language_id     = $i+1;
					$desc->message         = $messages[$i];
					$desc->save();
				}
			}
		}
	}

	public static function makeNotificationInactive($event_type, $event_id) {

		Notification::where('event_type', $event_type)
					->where('event_id', $event_id)
					->update(
						['status' => 0]
					);
	}

    function firebase_sender(?array $device_token, $title, $body){
        $data = array(
            "title" => $title,
            "body" => $body,
            "sound" => 'Default',
        );
        try{
            $url = 'https://fcm.googleapis.com/fcm/send';
            $fields = array(
                "registration_ids" => $device_token,
                "notification" => $data,
                "data" => $data,
            );
            $fields = json_encode($fields);
            $headers = array(
                'Authorization: key=AAAAmYS6Yfg:APA91bFmDFBq0KTlGjJiLPeanDk_Qc4a_YgYrPj6wDueDGm48os5gmau3fOk-vRYntKa0bBuRaY55JJKIanNiCblIuf9KTpHkssOSzdbkO3oI2n0Jm-tsKkhCpXV5wTDRTUdnBXMJRg1',
                'Content-Type: application/json'
            );
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
            $result = curl_exec($ch);
            Log::info('testing push : '. $result);
            curl_close($ch);
        }
        catch(\Exception $e){
            Log::info($e->getMessage());
        }

    }

}
