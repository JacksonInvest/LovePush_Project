package com.lovepushapp.quickBloxTest.fcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;

/**
 * Created by Manjinder Singh on 27,October,2021
 */
public class NotificationClassChecker extends QBFcmPushListenerService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("DATA", new Gson().toJson(remoteMessage));
    }
}
