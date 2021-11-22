package com.lovepushapp.core.utils;

import android.app.Activity;
import android.app.Dialog;

import com.lovepushapp.R;




public class AlertDialogs {

    Dialog dialog;
    Activity activity;

    public AlertDialogs(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.TransparentProgressDialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.view_progress_dialog);
    }

    public void show() {
        try {
            if (dialog != null) {
                dialog.show();
            } else {
                new AlertDialogs(activity);
                show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (dialog != null&& !activity.isFinishing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
