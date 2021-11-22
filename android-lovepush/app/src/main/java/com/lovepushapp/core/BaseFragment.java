package com.lovepushapp.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private static final int RESTART_DELAY = 200;

    public static String CURRENT_FRAGMENT="";
    @Inject
    public Util appUtils;
    private ProgressDialog progressDialog = null;

    @Inject
    public DialogUtils dialogUtils;

    @Inject
    public SharedStorage sharedStorage;
    protected QBResRequestExecutor requestExecutor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getAppComponent(getActivity()).inject(this);
        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public GetProfile getProfileModel() {
        GetProfile response = null;
        try {
            Log.e( "manageLoginResponse: ",(String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE,
                    GlobalsVariables.STORAGE.TYPE_STRING));
            Gson gson = new GsonBuilder().create();
            response = gson.fromJson((String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    GetProfile.class);

            Log.e( "getProfileModel: ",response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void showProgressDialog(@StringRes Integer messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }
        progressDialog.setMessage(getString(messageId));
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void restartApp(Context context) {
        // Application needs to restart when user declined some permissions at runtime
        Intent restartIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(context, 0, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + RESTART_DELAY, intent);
        System.exit(0);
    }

}
