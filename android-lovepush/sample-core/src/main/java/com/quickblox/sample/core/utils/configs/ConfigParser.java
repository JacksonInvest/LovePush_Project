package com.quickblox.sample.core.utils.configs;

import android.content.Context;

import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.AssetsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ConfigParser {

    private Context con;

    public ConfigParser(Context context)
    {
       // this.con=context;
        con = CoreApp.getContext();
    }

    public String getConfigsAsJsonString(String fileName,Context context) throws IOException {
        return AssetsUtils.getJsonAsString(fileName, context);
    }

    public JSONObject getConfigsAsJson(String fileName,Context context) throws IOException, JSONException {
        return new JSONObject(getConfigsAsJsonString(fileName,context));
    }

    public String getConfigByName(JSONObject jsonObject, String fieldName) throws JSONException {
        return jsonObject.getString(fieldName);
    }
}
