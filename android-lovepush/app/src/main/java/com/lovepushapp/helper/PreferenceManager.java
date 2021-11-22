package com.lovepushapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.lovepushapp.core.MyApplication;


/**
 * Created by Subhash on 13/5/19.
 */
public class PreferenceManager {


    private static final String USER_SETTINGS = "userSettings";
    private final String PREFERENCE_NAME = "User_PREFERENCES";
    private final String TUTORIAL_STATUS = "TutorialStatus";
    private final String DEVICE_ID = "device_id";
    private final String USER_DATA = "user_data";
    private static Context context;
    private static PreferenceManager ourInstance;
    private final String USER_LOCATION = "user_location";
    private String DEVICE_TOKEN="deviceToken";
    private String REQUEST_DATA = "request_data";
    private String MESSAGE_COUNT = "message_count";
    private String language = "language";
    private String PENDING_RATING = "pending_rating";
    private String NOTIFICATION_COUNT= "noti_count";


    public static PreferenceManager getInstance(Context context1) {
        PreferenceManager.context = context1;
        if (ourInstance == null) {
            ourInstance = new PreferenceManager();
        }
        return ourInstance;
    }

    private SharedPreferences getPreferences() {
        if (context != null)
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        else
            return MyApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

   /* private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }*/

    /**
     * SharedStorage
     * SAVE STRING VALUES
     */
    public String getPrefrencesString(String key) {
        try {
            return getPreferences().getString(key, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPrefrencesString(String key, String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    /**
     * SAVE boolean VALUES
     */
    public boolean getPrefrencesBoolean(String key) {
        try {
            return getPreferences().getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPrefrencesBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    //    public void setUserData(UserDataModel userData) {
//        getPreferences().edit().putString(GlobalsVariables.USER_DATA, new Gson().toJson(userData)).commit();
//    }
//
//    public UserDataModel getUserData() {
//        return new Gson().fromJson(getPreferences().getString(GlobalsVariables.USER_DATA, ""), UserDataModel.class);
//    }
    public Integer getMessageCount() {
        return getPreferences().getInt(MESSAGE_COUNT, 0);
    }

    public void setMessageCount(Integer count) {
        getPreferences().edit().putInt(MESSAGE_COUNT, count).commit();
    }

    public void clearAllData() {
        getPreferences().edit().clear().commit();
    }



    public void setDeviceId(String deviceId) {
        getPreferences().edit().putString(DEVICE_ID, deviceId).apply();
    }

    public String getDeviceId() {
        return getPreferences().getString(DEVICE_ID, "");
    }


    public void clearUserData() {
        getPreferences().edit().remove(USER_DATA).apply();
        getPreferences().edit().remove(DEVICE_ID).apply();
    }


    public void setUserLocation(String location) {
        getPreferences().edit().putString(USER_LOCATION, location).apply();
    }

    public String getLanguage() {
        return getPreferences().getString(language, "mn");
    }

    public void setLanguage(String language) {
        getPreferences().edit().putString(this.language, language).commit();
    }

    public int getNotificationCount() {
        return getPreferences().getInt(NOTIFICATION_COUNT, 0);
    }

    public void saveNotificationCount(int count) {
        getPreferences().edit().putInt(NOTIFICATION_COUNT, count).apply();
    }




    public double[] getUserLocation() {
        double[] location = new double[2];
        String userLocation = getPreferences().getString(USER_LOCATION, null);
        if (userLocation != null) {
            location[0] = Double.parseDouble(userLocation.split(",")[0]);
            location[1] = Double.parseDouble(userLocation.split(",")[1]);
        } else {
            location[0] = 0.0;
            location[1] = 0.0;
        }
        return location;
    }

//    public void setRequestData(RequestData requestData) {
//        getPreferences().edit().putString(REQUEST_DATA, new Gson().toJson(requestData)).commit();
//    }
//
//    public RequestData getRequestData() {
//        return new Gson().fromJson(getPreferences().getString(REQUEST_DATA, ""), RequestData.class);
//    }
//
//    public void setPendingRatingData(RequestData requestData) {
//        getPreferences().edit().putString(PENDING_RATING, new Gson().toJson(requestData)).commit();
//    }
//
//    public RequestData getPendingRatingData() {
//        return new Gson().fromJson(getPreferences().getString(PENDING_RATING, ""), RequestData.class);
//    }


    public void save(String key, Object value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-supported preference");
        }

        editor.commit();
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) getPreferences().getAll().get(key);
    }

    public String getDeviceToken() {
        return getPreferences().getString(DEVICE_TOKEN, "");
    }

   /* public void saveFileId(QBUser qbUser) {
        save(QB_FILE_ID, qbUser.getFileId());
    }*/

    public void saveLocation(String latitude, String longitude, String address) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.putString("address",address);

        editor.apply();
    }

    public String getSharedlatitude() {
        return getPreferences().getString("latitude", "0.0");
    }

    public String getSharedlongitude() {
        return getPreferences().getString("longitude", "0.0");
    }

    public String getSharedAddress() {
        return getPreferences().getString("address", "");
    }



}
