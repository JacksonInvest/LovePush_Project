package com.lovepushapp.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lovepushapp.R;
import com.lovepushapp.activities.LoginActivity;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class RestProcess<T> implements Callback<T> {

    AlertDialogs dialogs;
    RestCallback callback;
    String serviceCode;
    Activity activity;
    boolean isDialogShow;

    public RestProcess(String serviceCode, RestCallback restCallback, Activity activity, boolean isDialogShow) {
        this.callback = restCallback;
        this.serviceCode = serviceCode;
        this.activity = activity;
        this.isDialogShow = isDialogShow;
       try{
           dialogs = new AlertDialogs(activity);
           if (isDialogShow) {
               dialogs.show();
           }
       }catch (Exception e)
       {
           Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (call != null) {


          /*if (response.code()==400 && serviceCode.equalsIgnoreCase(API_GLOBALS.API_NAME.deleteMatchRecord))
          {
              if (isDialogShow) {
                  dialogs.dismiss();
              }

//              Log.e("RESPONSE",new Gson().toJson(response.errorBody()))
              try {
                  String error=response.errorBody().string();
                  JSONObject jsonObject=new JSONObject(error);

                  Log.e("RESPONSE",jsonObject.getString("message"));
                  String error_message=jsonObject.getString("message");
                  Toast.makeText(activity, ""+error_message, Toast.LENGTH_SHORT).show();

              } catch (Exception e) {
                  e.printStackTrace();
              }




          }*/



            if (serviceCode.equalsIgnoreCase(API_GLOBALS.API_NAME.hitHomePostLikeDislike))
                try {
                    String error=response.errorBody().string();
                    JSONObject jsonObject=new JSONObject(error);

                    String error_message=jsonObject.getString("message");
                    String status =jsonObject.getString("status");

                    Log.e("RESPONSE",error_message+" "+response.toString());
                    //Toast.makeText(activity, ""+error_message, Toast.LENGTH_SHORT).show();
                    //alertPopup2(error_message);
                    callback.onSuccess(call, response, serviceCode);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (response.code()==400){
                if (isDialogShow) {
                    dialogs.dismiss();
                   // callback.onSuccess(call, response, serviceCode);
                   /* try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        alertPopup2(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
                switch (serviceCode){
                    case API_GLOBALS.API_NAME.deleteMatchRecord:
                    case API_GLOBALS.API_NAME.chatRequest:
                    case API_GLOBALS.API_NAME.accept_reject_ConnectRequest:
                    case API_GLOBALS.API_NAME.accept_reject_ChatRequest:

                        try {
                            String error=response.errorBody().string();
                            JSONObject jsonObject=new JSONObject(error);

                            String error_message=jsonObject.getString("message");
                            Log.e("RESPONSE",error_message);
                            alertPopup2(error_message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;


                     case API_GLOBALS.API_NAME.like:

                         if(TextUtils.isEmpty(SharedStorage.getString("showDialogs",GlobalsVariables.STORAGE.TYPE_STRING))){

                         }else if(SharedStorage.getString("showDialogs",GlobalsVariables.STORAGE.TYPE_STRING).equals("yes")){
                             try {
                                 String error=response.errorBody().string();
                                 JSONObject jsonObject=new JSONObject(error);

                                 String error_message=jsonObject.getString("message");
                                 Log.e("RESPONSE",error_message);
                                 alertPopup2(error_message);

                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }else {

                         }

                         break;
                    case API_GLOBALS.API_NAME.connect:

                        if(TextUtils.isEmpty(SharedStorage.getString("showDialogs",GlobalsVariables.STORAGE.TYPE_STRING))){

                        }else if(SharedStorage.getString("showDialogs",GlobalsVariables.STORAGE.TYPE_STRING).equals("yes")){
                            try {
                                String error=response.errorBody().string();
                                JSONObject jsonObject=new JSONObject(error);

                                String error_message=jsonObject.getString("message");
                                Log.e("RESPONSE",error_message);
                                alertPopup2(error_message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {

                        }

                        break;


                }

                //              Log.e("RESPONSE",new Gson().toJson(response.errorBody()))

            }

            if (response.code() == 500) {
                if (isDialogShow) {
                    dialogs.dismiss();
                }
                new Util().showToast(MyApplication.getInstance().getString(R.string.server_error));
            } else {
                if (isDialogShow) {
                    dialogs.dismiss();
                }
                boolean isLogout = false;
                try {
//                    Log.e("Test-----____>", new Gson().toJson(response.body()).toString());
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()).toString());

                    try {
                        if ((jsonObject.getInt("status") == API_GLOBALS.RESPONSE_CODE.SESSION_EXPIRE)) {
                            logoutNow();
                            isLogout = true;
                        }
                    } catch (Exception e) {
                        if ((jsonObject.getInt("status")==API_GLOBALS.RESPONSE_CODE.SESSION_EXPIRE)) {
                            logoutNow();
                            isLogout = true;
                        }
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!isLogout) {

                    callback.onSuccess(call, response, serviceCode);
                }
            }

            if (response.code() == 401){
                //Toast.makeText(activity, "Unauth", Toast.LENGTH_SHORT).show();
                logoutNow();
            }


    }
    private void alertPopup2(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
//        builder1.setTitle("Message");
        builder1.setTitle("Love Push");
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void logoutNow() {
        new SharedStorage().clearLocalStorage();
        new Util().startActivity(activity, LoginActivity.createIntent(activity),
                true, GlobalsVariables.ANIM_TYPE.ONLY_TOP, false, 0);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("getFailure", t.getMessage());
        if (isDialogShow) {
            dialogs.dismiss();
        }
        if (t instanceof SocketTimeoutException) {
            Log.e("SocketTimeoutException ", t.getMessage());
            callback.onTimeOut(call, t, serviceCode);
        } else if (t instanceof UnknownHostException) {
            Log.e("UnknownHostException ", t.getMessage());
        } else if (callback != null) {
            callback.onFailure(call, t, serviceCode);
        }


    }

}