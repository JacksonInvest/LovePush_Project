package com.lovepushapp.core.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.adapters.OpenBottonDialogAdapter;
import com.lovepushapp.adapters.OpenBottonDialogMultiSelectAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.callbacks.OnItemClickListenerDialog;
import com.lovepushapp.model.local.MultiOptionModel;

import java.util.ArrayList;
import java.util.Calendar;


public class DialogUtils  {


    private int selectedType = 0;


    public void openBottomMultiOptionSelectDialog(Util util, final Activity activity, ArrayList<String> nameList, final int action, final OnItemClickListenerCutom listener) {
        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_list_botton_dialog, null);
        final RecyclerView list_view = (RecyclerView) child.findViewById(R.id.list_view);
        TextView doneTV = (TextView) child.findViewById(R.id.doneTV);
        mBottomSheetDialog.setContentView(child);

        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int listHeight = util.dpToPx(nameList.size() * 60);
        Log.e("openBottomDialog", "screenHeight" + screenHeight + " listHeight " + listHeight);

        if (listHeight > screenHeight) {
            Log.e("if", "-------------->");
            list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight / 2)));
        } else {
            Log.e("else", "==============>");
            list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }

        //   list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, listHeight < (screenHeight / 2) ? listHeight : screenHeight / 2));


        ArrayList<MultiOptionModel> nameListModel = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++) {
            MultiOptionModel model = new MultiOptionModel();
            model.name = nameList.get(i);
            model.isSelected = false;
            nameListModel.add(model);
        }
        final String[] finalSelected = {""};
        OpenBottonDialogMultiSelectAdapter adap = new OpenBottonDialogMultiSelectAdapter(util, activity, nameListModel, new OnItemClickListenerCutom() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                switch (type) {
                    case 1:
                        if (o instanceof String) {
                            String selectedValues = (String) o;
                            finalSelected[0] = selectedValues;
                        }

                        break;
                }
            }
        });
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setAdapter(adap);

        doneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    listener.onItemClick(list_view, 0, 0, finalSelected[0]);
                    mBottomSheetDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, activity.getString(R.string.please_select_item), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void openBottomDialog(Util util, final Activity activity, ArrayList<String> nameList, final int action, final OnItemClickListenerCutom listener) {
        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_list_botton_dialog, null);
        final RecyclerView list_view = (RecyclerView) child.findViewById(R.id.list_view);
        TextView doneTV = (TextView) child.findViewById(R.id.doneTV);
        mBottomSheetDialog.setContentView(child);

        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int listHeight = util.dpToPx(nameList.size() * 60);
        Log.e("openBottomDialog", "screenHeight" + screenHeight + " listHeight " + listHeight);

        if (listHeight > screenHeight) {
            Log.e("if", "-------------->");
            list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (screenHeight / 2)));
        } else {
            Log.e("else", "==============>");
            list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }

        //   list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, listHeight < (screenHeight / 2) ? listHeight : screenHeight / 2));
        final String[] selectedString = {""};
        final int[] pos = new int[1];
        OpenBottonDialogAdapter adap = new OpenBottonDialogAdapter(activity, nameList, new OnItemClickListenerCutom() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                switch (type) {
                    case 1:
                        selectedString[0] = (String) o;
                        pos[0] = position;

                        break;
                }
            }
        });
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setAdapter(adap);

        doneTV.setOnClickListener(v -> {
            try {
                if (TextUtils.isEmpty(selectedString[0])) {
                    Toast.makeText(activity, activity.getString(R.string.please_select_item), Toast.LENGTH_LONG).show();
                } else {
                    listener.onItemClick(list_view, pos[0], 0, selectedString[0]);
                    mBottomSheetDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, activity.getString(R.string.please_select_item), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void openFilterDialog(final Activity activity, int selectedFilterType, OnItemClickListenerDialog onItemClickListenerDialog) {
        selectedType = selectedFilterType;

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);

        View child = activity.getLayoutInflater().inflate(R.layout.view_filter_botton_dialog, null);
        AppCompatButton appCompatButton_cancel=child.findViewById(R.id.btnCancel);
        AppCompatButton appCompatButton_submit=child.findViewById(R.id.btnSubmit);
        ImageView noneIV = child.findViewById(R.id.noneIV);
        ImageView loveIV = child.findViewById(R.id.loveIV);
        ImageView connectIV = child.findViewById(R.id.connectIV);

        LinearLayout noneLL = child.findViewById(R.id.noneLL);
        noneLL.setVisibility(View.GONE);
        LinearLayout loveLL = child.findViewById(R.id.loveLL);
        LinearLayout connectLL = child.findViewById(R.id.connectLL);

        appCompatButton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        if (selectedType == 0){
            noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
            loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
        }else if (selectedType == 1){
            noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
            connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
        }
        else if (selectedType == 2){
            noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
        }

        noneLL.setOnClickListener(v -> {
            selectedType = 0;
            noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
            loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));

        });

        loveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*isNone = false;
                isLove = false;
                isSelected = true;*/
                selectedType = 1;

                noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
                loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
                connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
            }
        });

        connectLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*isNone = false;
                isLove = false;
                isSelected = true;*/
                selectedType = 2;

                noneIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
                loveIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_unfilled));
                connectIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.radio_filled));
            }
        });
        appCompatButton_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                onItemClickListenerDialog.onItemClick(appCompatButton_cancel,1,selectedType);
            }
        });

        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }




    public void openFilterExploreDialog(final Activity activity) {

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);

        View child = activity.getLayoutInflater().inflate(R.layout.view_filter_explore_botton_dialog, null);
        AppCompatButton appCompatButton_cancel=child.findViewById(R.id.cancel_B);
        AppCompatButton appCompatButton_submit=child.findViewById(R.id.submit_B);
        appCompatButton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }

    public static void showAlert(Context context, String message, Click click) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setTitle(context.getResources().getString(R.string.app_name))
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("OK", (dialog, id) -> {dialog.dismiss();
                    click.onclk();})
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showAlert1(Context context, String message, Click click) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setTitle(context.getResources().getString(R.string.app_name))
                    .setCancelable(false)
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("OK", (dialog, id) -> {dialog.dismiss();
                        click.onclk();});
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    public interface Click{
        void onclk();
    }




}
