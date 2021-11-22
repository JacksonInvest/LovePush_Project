package com.quickblox.sample.core.utils.configs;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.quickblox.sample.core.models.QbConfigs;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CoreConfigUtils {

    public static final String USER_LOGIN_FIELD_NAME = "user_login";
    public static final String USER_PASSWORD_FIELD_NAME = "user_password";

    public static QbConfigs getCoreConfigs(String fileName, Context context) throws IOException {
        ConfigParser configParser = new ConfigParser(context);
        Gson gson = new Gson();
        return gson.fromJson(configParser.getConfigsAsJsonString(fileName,context), QbConfigs.class);
    }

    public static QbConfigs getCoreConfigsOrNull(String fileName,Context context){
        QbConfigs qbConfigs = null;

        try {
            qbConfigs = getCoreConfigs(fileName,context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return qbConfigs;
    }

    public static String getStringConfigFromFile(String fileName, String fieldName,Context context) throws IOException, JSONException {
        JSONObject appConfigs = new ConfigParser(context).getConfigsAsJson(fileName,context);
        return appConfigs.getString(fieldName);
    }

    public static String getStringConfigFromFileOrNull(String fileName, String fieldName,Context context) {
        String fieldValue = null;

        try {
            fieldValue = getStringConfigFromFile(fileName, fieldName,context);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return fieldValue;
    }

    public static boolean isStringConfigFromFileNotEmpty(String fileName, String fieldName,Context context){
        return !TextUtils.isEmpty(getStringConfigFromFileOrNull(fileName, fieldName,context));
    }

    public static QBUser getUserFromConfig(String fileName,Context context){
        QBUser qbUser = null;

        String userLogin;
        String userPassword;

        try {
            JSONObject configs = new ConfigParser(context).getConfigsAsJson(fileName,context);
            userLogin = configs.getString(USER_LOGIN_FIELD_NAME);
            userPassword = configs.getString(USER_PASSWORD_FIELD_NAME);
            qbUser = new QBUser(userLogin, userPassword);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return qbUser;
    }
}
