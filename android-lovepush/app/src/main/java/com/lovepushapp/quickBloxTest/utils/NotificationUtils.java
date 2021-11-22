package com.lovepushapp.quickBloxTest.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.collection.ArrayMap;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.DashboardActivity;
import com.lovepushapp.activities.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationUtils {
    private static final String CHANNEL_ONE_ID = "com.quickblox.samples.ONE";// The id of the channel.
    private static final String CHANNEL_ONE_NAME = "Channel One";


    private static String logData="";

    public static void showNotification(Context context,
                                        String title, String message, Map data, @DrawableRes int icon,
                                        int notificationId) {
           SharedPrefsHelper sharedPrefsHelper =  SharedPrefsHelper.getInstance();
        String currentDialogID=sharedPrefsHelper.getSharedDialogId();

        Log.e("CurrentDialogId",sharedPrefsHelper.getSharedDialogId());


        try
        {

//            if (currentDialogID.isEmpty() || currentDialogID.equalsIgnoreCase((String) ((ArrayMap) data).valueAt(7)))
            if (currentDialogID.equalsIgnoreCase((String) ((ArrayMap) data).valueAt(7)))
            {
                Log.e("push","user opend");

            }
            else {
                Log.e("push","building");
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannelIfNotExist(notificationManager);
                }
                Notification notification = buildNotification(context, title, message, data, icon);

                notificationManager.notify(notificationId, notification);
            }
        }catch (Exception e)
        {
            showNotification2(context,title,message,data,icon,notificationId);
            Log.e("push","exception"+e);
        }
    }

    private static void showNotification2(Context context, String title, String message, Map data, int icon, int notificationId) {
        try {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannelIfNotExist(notificationManager);
            }
            JSONObject jsonObject=new JSONObject(message);
           String newMsg= jsonObject.getString("message");
            Notification notification = buildNotification(context, title, newMsg, data, icon);

            notificationManager.notify(notificationId, notification);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SHOW_NOTIFICATION_ISSUE",""+e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannelIfNotExist(NotificationManager notificationManager) {
        if (notificationManager.getNotificationChannel(CHANNEL_ONE_ID) == null) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    //.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableVibration(true);
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationChannel.setSound(uri,audioAttributes);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private static Notification buildNotification(Context context,
                                                  String title, String message, Map data, @DrawableRes int icon) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(context, CHANNEL_ONE_ID)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[] { 600, 600, 600, 600 })
                .setLights(Color.YELLOW, 4000, 4000)
                .setSound(defaultSoundUri)
                .setContentIntent(buildContentIntent(context, message,data))
                .build();
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_img : R.mipmap.ic_launcher;
    }


    private static PendingIntent buildContentIntent(Context context, String message, Map data) {

        try {


            /*Notify user by notify type*/

            JSONObject json = new JSONObject(data);

            logData=new Gson().toJson(data);

            String notify_type;

            if(logData.contains("{")) {

                logData = data.toString().replaceAll("\\\\", "");
                logData = data.toString().replaceAll("=", ":");

                JSONObject json2 = new JSONObject(logData);


                 notify_type = (String) json2.getJSONObject("message").get("notify_type");
            }
            else{

                notify_type = (String) json.get("notify_type");

            }


//            Log.e("NOTIFYDATA", String.valueOf(json.get(notify_type)));
           /* if (notify_type.equalsIgnoreCase("chat")){
                Log.d("NotificationData", "buildContentIntent: success");
               *//* Intent intent = new Intent(context, DashboardActivity.class);
                intent.putExtra("dialog_id", String.valueOf(json.get("dialog_id")));
                intent.putExtra("newDialogCreate","false");
                intent.putExtra("receiverImage", String.valueOf(json.get("user_image")));
                intent.putExtra("receiverName",String.valueOf(json.get("user_name")));
                intent.putExtra("event_type",String.valueOf(json.get("event_type")));
                intent.putExtra("match_id","");
                intent.putExtra("receiverId",String.valueOf(json.get("user_id")));
                intent.putExtra("from","push_notification");
                intent.putExtra(FcmConsts.EXTRA_FCM_MESSAGE, message);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*

                //context.startActivity(intent);
                // intent.putExtra(FcmConsts.EXTRA_FCM_MESSAGE, message);
                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            }*/


//            Intent intent = new Intent(context, DashboardActivity.class);
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.putExtra("notify_type", notify_type);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("NotificationData", "buildContentIntent: "+e.toString());
            Log.d("NotificationData", "buildContentIntent: "+logData);

           /* Intent intent1 = new Intent(context, SplashActivity.class);
            intent1.putExtra(FcmConsts.EXTRA_FCM_MESSAGE, message);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);*/
        }

/*
        if (data != null){
            Intent intent = new Intent(context, DashboardActivity.class);

            intent.putExtra("data",data+"");
            intent.putExtra(FcmConsts.EXTRA_FCM_MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
*/


        Intent intent1 = new Intent(context, SplashActivity.class);
        intent1.putExtra(FcmConsts.EXTRA_FCM_MESSAGE, message);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}