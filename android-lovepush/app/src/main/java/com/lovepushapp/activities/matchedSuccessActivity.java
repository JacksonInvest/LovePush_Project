package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.quickBloxTest.ui.activity.ChatActivity;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class matchedSuccessActivity extends BaseActivity {

    private AlertDialogs progressDialog;
    @BindView(R.id.userIV1) CircleImageView userIV1;
    @BindView(R.id.userIV2) CircleImageView userIV2;
    String receiverName = "",receiverImage = "",receiverId = "0",receiver_event_type = "",chat_match_id = "0",chat_deleted_by = "0",blocked_by = "0",receiverQb_id = "0";

    String isReceiveNotification;

    @Override
    public int getLayoutId() {
        return R.layout.activity_matched_success;
    }

    @Override
    public void manageToolBar() {

    }

    private Activity context = matchedSuccessActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, matchedSuccessActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new AlertDialogs(context);


        receiverName = getIntent().getStringExtra("receiverName");
        receiverImage =  getIntent().getStringExtra("receiverImage");
        receiverId =  getIntent().getStringExtra("receiverId");
        blocked_by =  getIntent().getStringExtra("blockedBy");
        chat_deleted_by =  getIntent().getStringExtra("deletedBy");
        receiver_event_type = getIntent().getStringExtra("event_type");
        receiverQb_id = getIntent().getStringExtra("receiverQb_id");
        chat_match_id = getIntent().getStringExtra("restore_match_id");
        isReceiveNotification = getIntent().getStringExtra("isReceiveNotification");

       String current_user_image = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING);

        Picasso.get()
                .load(current_user_image)
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(userIV1);

        Picasso.get()
                .load(receiverImage)
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(userIV2);

    }


    @OnClick({R.id.chatNowBtn, R.id.laterBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chatNowBtn:



                try {
                    if(TextUtils.isEmpty(receiverQb_id)){

                        Toast.makeText(matchedSuccessActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        finish();

                    }else if(TextUtils.isEmpty(receiverName)){
                        Toast.makeText(matchedSuccessActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        finish();


                    }else {
                        ArrayList<QBUser> qbUsers = new ArrayList<>();
                        QBUser user1 = new QBUser();
                        user1.setId(Integer.parseInt(receiverQb_id));
                        qbUsers.add(user1);
                        createDialog(qbUsers, receiverName);

                    }
                }catch (Exception e){

                }



                break;
            case R.id.laterBtn:

                finish();
                break;

        }
    }

    private void createDialog(ArrayList<QBUser> selectedUsers, String chatName) {
        progressDialog.show();
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("dialog_id", dialog.getDialogId());
                        intent.putExtra("newDialogCreate","true");
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName",receiverName);
                        intent.putExtra("receiverId",receiverId);
                        intent.putExtra("event_type",receiver_event_type);
                        intent.putExtra("match_id",chat_match_id);
                        intent.putExtra("from","profile");
                        intent.putExtra("restore_match_id",chat_match_id);
                        intent.putExtra("deletedBy",chat_deleted_by);
                        intent.putExtra("blockedBy",blocked_by);
                        intent.putExtra("isReceiveNotification",isReceiveNotification);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        //ProgressDialogFragment.hide(getFragmentManager());
                        progressDialog.dismiss();
                        Log.e("QUICKBLOX", String.valueOf(e));
                        //showSnackbarError(getView(), R.string.something,null,null);
                    }
                }
        );
    }



}
