package com.lovepushapp.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lovepushapp.core.MyApplication;




public class SharedStorage {

    private static SharedPreferences getInstance() {
        return MyApplication.getInstance().getSharedPreferences(GlobalsVariables.STORAGE.NAME, Context.MODE_PRIVATE);
    }

    public void setSharedData(String keyName, String type, Object value) {
        if (type.equals(GlobalsVariables.STORAGE.TYPE_STRING)) {
            getInstance().edit().putString(keyName, (String) value).commit();
        }/* else if (type.equals(GlobalsVariables.STORAGE.TYPE_INTEGER)) {
            getInstance().edit().putInt(keyName, (Integer) value).commit();
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_BOOLEAN)) {
            getInstance().edit().putBoolean(keyName, (Boolean) value).commit();
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_LONG)) {
            getInstance().edit().putLong(keyName, (Long) value).commit();
        }*/

    }

    public static Object getSharedData(String keyName, String type) {
        if (type.equals(GlobalsVariables.STORAGE.TYPE_STRING)) {
            return getInstance().getString(keyName, "");
        } /*else if (type.equals(GlobalsVariables.STORAGE.TYPE_INTEGER)) {
            return getInstance().getInt(keyName, -1);
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_BOOLEAN)) {
            return getInstance().getBoolean(keyName, false);
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_LONG)) {
            return getInstance().getLong(keyName, 0);
        }*/
        return null;
    }


    public static String getString(String keyName, String type) {
        if (type.equals(GlobalsVariables.STORAGE.TYPE_STRING)) {
            return getInstance().getString(keyName, "");
        } /*else if (type.equals(GlobalsVariables.STORAGE.TYPE_INTEGER)) {
            return getInstance().getInt(keyName, -1);
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_BOOLEAN)) {
            return getInstance().getBoolean(keyName, false);
        } else if (type.equals(GlobalsVariables.STORAGE.TYPE_LONG)) {
            return getInstance().getLong(keyName, 0);
        }*/
        return null;
    }

    public void clearLocalStorage() {
        getInstance().edit().clear().commit();


    }

}
