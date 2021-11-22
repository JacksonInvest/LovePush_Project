package com.lovepushapp.quickBloxTest.fcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.lovepushapp.R;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.quickBloxTest.utils.NotificationUtils;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;
import com.quickblox.users.model.QBUser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PushListenerService extends QBFcmPushListenerService {
    private static final String TAG = "push";
    private static final int NOTIFICATION_ID = 1;

    protected void showNotification(String message, Map data) {



         Log.v("QucikBloxFCM","---------------------RECIEVED2");

        NotificationUtils.showNotification(MyApplication.getInstance(),
                MyApplication.getInstance().getString(R.string.notification_title), message,data,
                R.drawable.logo_img, NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.v("push","---------------------RECIEVED");

        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            Log.d(TAG, "App has logged user" + qbUser.getId());
            LoginService.start(this, qbUser);
        }
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, ": " + data);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);
        if (SharedPrefsHelper.getInstance().getNotificationEnableDisable().equals("1")){

            {

                showNotification(message,data);

            }
        }
        /*if (ActivityLifecycle.getInstance().isBackground()) {
        }*/
    }
}
