package com.lovepushapp.activities;

import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.helper.TimeAgo;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.homePosts.postAdDeleteResponse;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.quickBloxTest.ui.activity.ChatActivity;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.quickBloxTest.utils.qb.callback.QbEntityCallbackImpl;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class singleHomePostView extends BaseActivity  implements PostAdsMvp {
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.postIV)
    ImageView postIV;
    @BindView(R.id.ivReport)
    ImageView ivReport;
    @BindView(R.id.postedByImg)
    CircleImageView postedByImg;
    @BindView(R.id.postedByName) AppCompatTextView postedByName;
    @BindView(R.id.descriptionTV) AppCompatTextView descriptionTV;
    @BindView(R.id.postedTimeTV) AppCompatTextView postedTimeTV;
    @BindView(R.id.titleTV) AppCompatTextView titleTV;
    @BindView(R.id.chatIV) ImageView chatIV;
    @BindView(R.id.likeIV) ImageView likeIV;
    getHomePostResponse.Data.Datum data;
    private String check="new";
    private AlertDialogs progressDialog;
    private static QBChatDialog qbChatDialog = null;
    @BindView(R.id.editIV) ImageView editIV;
    @BindView(R.id.deleteIV) ImageView deleteIV;
    private boolean isChatDialogCreated = false;
    PostAdsPresenter adsPresenter;
    private Activity context = singleHomePostView.this;
    Boolean meBlocked=false;

    @BindView(R.id.userProfileLL)
    LinearLayout userProfileLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adsPresenter = new PostAdsPresenter(context);
        adsPresenter.attachView(this);
        data = new Gson().fromJson(getIntent().getExtras().getString("data"), getHomePostResponse.Data.Datum.class);
        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        progressDialog = new AlertDialogs(context);

        String posted_user_id = String.valueOf(data.getUserInfo().getId());

        Boolean isMatchedUser=getIntent().getBooleanExtra("isMatchedUser",false);
        if (!isMatchedUser)
            chatIV.setVisibility(View.VISIBLE);
        else
            chatIV.setVisibility(View.GONE);
        if (data.getImage()==null || data.getImage().isEmpty())
            postIV.setVisibility(View.GONE);
        else
        Picasso.get()
                .load(data.getImage())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(postIV);


        titleTV.setText(data.getTitle());
        descriptionTV.setText(data.getDescription());

        if (posted_user_id.equals(my_user_id)){
            deleteIV.setVisibility(View.VISIBLE);
            editIV.setVisibility(View.VISIBLE);
            ivReport.setVisibility(View.GONE);
        }else {
            deleteIV.setVisibility(View.GONE);
            editIV.setVisibility(View.GONE);
            ivReport.setVisibility(View.VISIBLE);
        }

        if (data.getIsPostAnonymously()==1){
            Picasso.get()
                    .load(R.drawable.placeholder_male_square)
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(postedByImg);
            postedByName.setText("Anonymous");
            chatIV.setVisibility(View.GONE);
        }else {
            Picasso.get()
                    .load(data.getUserInfo().getProfileImage())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(postedByImg);
            postedByName.setText(data.getUserInfo().getName());

            if (posted_user_id.equals(my_user_id)){
                chatIV.setVisibility(View.GONE);
            }else
            {
                Boolean isBlocked = String.valueOf(data.getBlockedBy()).equalsIgnoreCase(my_user_id);
                if (isBlocked)
                    chatIV.setVisibility(View.GONE);
                else if (!isBlocked && !data.getBlockedBy().equalsIgnoreCase("0"))
                    meBlocked=true;
            }

        }

        if (data.getIsLiked()==1){
            likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_active));
        }
        if (data.getIsLiked()==0){
            likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_unactive));

        }

        ivReport.setOnClickListener(v -> {
            alertDialog();
        });
        userProfileLL.setOnClickListener(v -> {
            if (data.getIsPostAnonymously()!=1) {
                if (posted_user_id.equals(my_user_id)) {
                    appUtils.startActivity(context,
                            ProfileActivity.createIntent(context),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                } else {
                    context.startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id", posted_user_id).putExtra("from", "explore"));
                }
            }

        });


postIV.setOnClickListener(v -> {
    Intent i = new Intent(context, FullImage.class);
    i.putExtra("ImagePath",data.getImage());
    startActivity(i);
});
        chatIV.setOnClickListener(v -> {
            //if (dataList.get(pos).)
          //  listener.onItemClick(holder.chatIV, pos, 1, dataList);
            if (data.getMatchQbID()!=null){
                if (!data.getMatchQbID().getQbDialogId().equals("")){
                    String dialog_id = data.getMatchQbID().getQbDialogId();
                    String receiverName = data.getUserInfo().getName();
                    String receiverImage = data.getUserInfo().getProfileImage();
                    String receiverId = String.valueOf(data.getUserInfo().getId());
                    String event_type = data.getMatchQbID().getMatchInfo().getEventType();
                    /*if (ChatHelper.getInstance().isLogged()) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // SharedPrefsHelper.getInstance().saveChatUser(dialog_id,"false",finalReceiverImage,finalReceiverName,finalEvent_type,"");
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("dialog_id", dialog_id);
                                intent.putExtra("newDialogCreate","false");
                                intent.putExtra("receiverImage", receiverImage);
                                intent.putExtra("receiverName",receiverName);
                                intent.putExtra("receiverId",receiverId);
                                intent.putExtra("event_type",event_type);
                                intent.putExtra("match_id","");
                                intent.putExtra("from","home");
                                intent.putExtra("restore_match_id",data.getMatchId()+"");
                                intent.putExtra("deletedBy",data.getDeletedBy());
                                intent.putExtra("blockedBy",data.getBlockedBy());
                                intent.putExtra("isReceiveNotification",data.getReceive_notification());
                                startActivity(intent);
                            }
                        }, 1000);



                        //ChatActivity.startForResult(intent,getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
                    } else {
                        //showProgressDialog(R.string.dlg_login);*/
                        progressDialog.show();
                        QBUser currentUser = getUserFromSession();

                        ChatHelper.getInstance().loginToChat(currentUser, new QBEntityCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid, Bundle bundle) {
                                ChatHelper.getInstance().getDialogById(dialog_id, new QbEntityCallbackImpl<QBChatDialog>() {
                                    @Override
                                    public void onSuccess(QBChatDialog result, Bundle bundle) {
                                        progressDialog.dismiss();
                                        QbDialogHolder.getInstance().addDialog(result);
                                        Intent intent = new Intent(context, ChatActivity.class);
                                        intent.putExtra("dialog_id", dialog_id);
                                        intent.putExtra("newDialogCreate","false");
                                        intent.putExtra("receiverImage", receiverImage);
                                        intent.putExtra("receiverName",receiverName);
                                        intent.putExtra("receiverId",receiverId);
                                        intent.putExtra("event_type",event_type);
                                        intent.putExtra("isBlocked",meBlocked);
                                        intent.putExtra("match_id","");
                                        intent.putExtra("from","home");
                                        intent.putExtra("restore_match_id",data.getMatchId()+"");
                                        intent.putExtra("deletedBy",data.getDeletedBy());
                                        intent.putExtra("blockedBy",data.getBlockedBy());
                                        intent.putExtra("isReceiveNotification",data.getUserInfo().getReceive_notification());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(QBResponseException e) {
                                        // isProcessingResultInProgress = false;
                                        progressDialog.dismiss();
                                    }
                                });


                                // ChatActivity.startForResult(intent,getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                progressDialog.dismiss();
                                ToastUtils.shortToast(R.string.login_chat_login_error);
                                //showErrorSnackbar(R.string.login_chat_login_error, e, null);
                            }
                        });
                }
            }else {
                ArrayList<QBUser> qbUsers = new ArrayList<>();

                QBUser user1 = new QBUser();
                String chatName = "";

                user1.setId(Integer.parseInt(data.getUserInfo().getQuickbloxId()));
                chatName = data.getUserInfo().getName();

                qbUsers.add(user1);
                check = "start";
                createDialog(qbUsers, data,chatName);
            }
        });

        likeIV.setOnClickListener(v -> {
            //listener.onItemClick(holder.likeIV, pos, 2, dataList);
            if (data.getIsLiked()==1){
                adsPresenter.hitHomePostLikeDislike("0", String.valueOf(data.getId()));
                data.setIsLiked(0);
                likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_unactive));
            }else if (data.getIsLiked()==0){
                adsPresenter.hitHomePostLikeDislike("1", String.valueOf(data.getId()));
                data.setIsLiked(1);
                likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_active));

            }
        });

        String MyFinalValue =  new TimeAgo().covertTimeStringToAgo(data.getCreatedAt(), context);

        postedTimeTV.setText(MyFinalValue);
    }
    void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You have seen abusive or vulgar content in this photo! Do you want to report photo?");
        builder.setPositiveButton("OK", (dialog, id) -> {
            report();
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void report() {
        AlertDialogs dialogs = new AlertDialogs(context);
        //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
        dialogs.show();
        new PostAdsPresenter(context).reportImage(data.getId(), "post", new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.body() != null) {
                    CommonResponse response1 = (CommonResponse) response.body();
                    if (response1.getStatus())
                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private QBUser getUserFromSession(){
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name",user.getLogin()+"  "+user.getPassword());
        return user;
    }


    private void createDialog(final ArrayList<QBUser> selectedUsers, getHomePostResponse.Data.Datum dataList, String chatName) {
//        Log.e("SELECTED_USER",new Gson().toJson(selectedUsers));
        progressDialog.show();
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog=dialog;
                        Log.e("QBCHATDI24", String.valueOf(dialog.getOccupants()));
                        Log.e("dialogId", "//"+qbChatDialog.getDialogId() + "//" + qbChatDialog.getRoomJid());

                        progressDialog.dismiss();
                        try {
                            // ProgressDialogFragment.hide(getFragmentManager());
                        }
                        catch (Exception e)
                        {
                            Log.e("ProgressExcep", String.valueOf(e));
                        }

                        isChatDialogCreated = true;
                        if (check.equalsIgnoreCase("start"))
                        {
                            chatStart(dataList);
                        }

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

    private void chatStart(getHomePostResponse.Data.Datum data1) {
        if(data1.getMatchInfo()!= null){
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("dialog_id", qbChatDialog.getDialogId());
            intent.putExtra("newDialogCreate","true");
            intent.putExtra("match_id",data1.getId());
            intent.putExtra("receiverName", data1.getUserInfo().getName());
            intent.putExtra("receiverImage",data1.getUserInfo().getProfileImage());
            intent.putExtra("receiverId",data1.getUserInfo().getId()+"");
            intent.putExtra("event_type",data1.getMatchInfo().getEventType());
            intent.putExtra("from","home");
            intent.putExtra("isBlocked",meBlocked);
            intent.putExtra("restore_match_id",data1.getMatchId()+"");
            intent.putExtra("deletedBy",data1.getDeletedBy());
            intent.putExtra("blockedBy",data1.getBlockedBy());
            intent.putExtra("isReceiveNotification",data.getUserInfo().getReceive_notification());

            startActivity(intent);
        }else {

            alertPopup(getString(R.string.record_not_found));
        }
    }

    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
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
    @Override
    public int getLayoutId() {
        return R.layout.activity_single_home_post_view;

    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.post));

    }

    @OnClick({R.id.backIV,R.id.deleteIV,R.id.editIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.deleteIV:
              deletePost();
                break;
            case R.id.editIV:

                Intent intent = new Intent(context, updateHomePostActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                startActivity(intent);
                finish();
                break;

        }
    }

    private void deletePost() {
        AlertDialogs dialogs=new AlertDialogs(this);
        //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
        dialogs.show();
        adsPresenter.hitDeleteMyHomePost(data.getId(), new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                if (response.body() instanceof postAdDeleteResponse) {
                    dialogs.dismiss();
                    postAdDeleteResponse res = (postAdDeleteResponse) response.body();
//            appUtils.showToast(response.getMessage());
                    if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                        appUtils.showToast(res.getMessage());
                        goback();
                    }
                    else
                    {

                        appUtils.showToast(res.getMessage());

                    }

                }
            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
