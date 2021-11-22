package com.lovepushapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.helper.TimeAgo;
import com.lovepushapp.model.homePosts.addsmodel.DataItem;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.homePosts.postAdDeleteResponse;
import com.lovepushapp.model.response.Profile_Model.Data;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class singlePostAdView extends BaseActivity  implements PostAdsMvp , LoginMvp {
    private static final String TAG = "singlePostAdView";
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.postIV) ImageView postIV;
    @BindView(R.id.postedByImg) CircleImageView postedByImg;
    @BindView(R.id.postedByName) AppCompatTextView postedByName;
    @BindView(R.id.postedDesTV) AppCompatTextView postedDesTV;
    @BindView(R.id.postedTimeTV) AppCompatTextView postedTimeTV;
    @BindView(R.id.postTitleTV) AppCompatTextView postTitleTV;
    @BindView(R.id.postLocationTV) AppCompatTextView postLocationTV;
    @BindView(R.id.root_view)
    LinearLayout root_view;
    @BindView(R.id.postTypeTV) AppCompatTextView postTypeTV;
    @BindView(R.id.editIV) ImageView editIV;
    @BindView(R.id.deleteIV) ImageView deleteIV;
    @BindView(R.id.chatIV) ImageView chatIV;
    String userId = "";

    boolean match_1_fill = false, match_2_fill = false;
    Data userProfileData;
    Boolean meBlocked=false;
    private String notiReceiverQbId = "0", notiEventType = "", acceptEventType = "";
    String event_type = "", isReceiveNotification = "1";
    String receiverName = "", receiverImage = "", receiverId = "0", receiver_event_type = "", chat_match_id = "0", chat_deleted_by = "0", blocked_by = "0";
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;




    DataItem data;
    private AlertDialogs progressDialog;
    PostAdsPresenter adsPresenter;

    //new chat variable
    boolean isMyProfile = false;

    getHomePostResponse.Data.Datum chatData;
    private boolean isChatDialogCreated = false;
    private String check="new";
    private static QBChatDialog qbChatDialog = null;


    private Activity context = singlePostAdView.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        chatIV.setVisibility(View.GONE);
            // likell.setVisibility(View.VISIBLE);


        if (getIntent().getExtras() != null) {
            isMyProfile = false;            editIV.setVisibility(View.GONE);
            userId = getIntent().getStringExtra("user_id");

        }else {
            chatIV.setVisibility(View.GONE);
        }



        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);
        //userId = (String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

        chatData = new Gson().fromJson(getIntent().getExtras().getString("data"), getHomePostResponse.Data.Datum.class);

        //String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));


        adsPresenter = new PostAdsPresenter(context);
        adsPresenter.attachView(this);

        data = new Gson().fromJson(getIntent().getExtras().getString("data"), DataItem.class);
        String myProfileImage=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        String MyName=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));


        progressDialog = new AlertDialogs(context);

        if (data.getImage() == null || data.getImage().equals("")){
            postIV.setVisibility(View.GONE);
        }else {
            Picasso.get()
                    .load(data.getImage())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(postIV);
            postIV.setVisibility(View.VISIBLE);
        }



        postTitleTV.setText(data.getTitle());
        postedDesTV.setText(data.getDescription());
        postLocationTV.setText(data.getLocation());

        String posted_user_id = String.valueOf(data.getUserId());


        if (posted_user_id.equals(my_user_id)){
            deleteIV.setVisibility(View.VISIBLE);
            editIV.setVisibility(View.VISIBLE);
            chatIV.setVisibility(View.GONE);
        }else {
            deleteIV.setVisibility(View.GONE);
            editIV.setVisibility(View.GONE);
        }

        if (data.getIsPostAnonymously()==1){
            Picasso.get()
                    .load(R.drawable.placeholder_male_square)
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(postedByImg);
            postedByName.setText("Anonymous");
        }else {
            if (posted_user_id.equals(my_user_id)){
                Picasso.get()
                        .load(myProfileImage)
                        .placeholder(R.drawable.placeholder_male_square)
                        .error(R.drawable.placeholder_male_square)
                        .into(postedByImg);
                postedByName.setText(MyName);
            }else {
                Picasso.get()
                        .load(data.getUserInfo().getProfileImage())
                        .placeholder(R.drawable.placeholder_male_square)
                        .error(R.drawable.placeholder_male_square)
                        .into(postedByImg);
                postedByName.setText(data.getUserInfo().getName());
            }
        }


        if (data.getPostType()==1){
            postTypeTV.setText(getString(R.string.love_ad));
            postTypeTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_button_small_blue));
        }else {
            postTypeTV.setText(getString(R.string.connect_ad));
            postTypeTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_button_small));
        }


        String MyFinalValue =  new TimeAgo().covertTimeStringToAgo(data.getCreatedAt(), context);

        postedTimeTV.setText(MyFinalValue);

        postedByImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        context.startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id", data.getUserInfo().getId()+"").putExtra("from", "explore"));
                    }
                }

            }
        });

        postLocationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = "ABC Label";
                String uriBegin = "geo:" + data.getLatitude() + "," + data.getLongitude();
                String query = data.getLatitude() + "," + data.getLongitude() + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        postIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullImage.class);
                i.putExtra("ImagePath",data.getImage());
                startActivity(i);
            }
        });


        chatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat();
            }
        });



    }

    private void startChat() {
        String dialog_id = "", receiverName = "", receiverImage = "", receiverId = "", event_type = "";

        if (match_1_fill) {

            if (userProfileData.getMatchInfo1().get(0).getQbChatDialog() != null) {
                dialog_id = userProfileData.getMatchInfo1().get(0).getQbChatDialog().getQbDialogId();
                startOldChat(dialog_id);

            } else {
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser user1 = new QBUser();
                user1.setId(Integer.parseInt(notiReceiverQbId));
                qbUsers.add(user1);
                createDialog(qbUsers, receiverName);
            }

        } else if (match_2_fill) {
            if (userProfileData.getMatchInfo2().get(0).getQbChatDialog() != null) {
                dialog_id = userProfileData.getMatchInfo2().get(0).getQbChatDialog().getQbDialogId();
                startOldChat(dialog_id);
            } else {
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser user1 = new QBUser();
                user1.setId(Integer.parseInt(notiReceiverQbId));
                qbUsers.add(user1);
                createDialog(qbUsers, receiverName);
            }

        }
    }

    private void createDialog(ArrayList<QBUser> selectedUsers, String chatName) {
        progressDialog.show();
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers, chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("dialog_id", dialog.getDialogId());
                        intent.putExtra("newDialogCreate", "true");
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("event_type", receiver_event_type);
                        intent.putExtra("isBlocked", meBlocked);

                        if (chat_match_id == null)
                            intent.putExtra("match_id", "");
                        else
                            intent.putExtra("match_id", chat_match_id);
                        intent.putExtra("from", "profile");
                        intent.putExtra("restore_match_id", chat_match_id);
                        intent.putExtra("deletedBy", chat_deleted_by);
                        intent.putExtra("blockedBy", blocked_by);
                        intent.putExtra("isReceiveNotification", isReceiveNotification);
                        startActivity(intent);

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


    private void startOldChat(String dialog_id) {
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
                        intent.putExtra("newDialogCreate", "false");
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("event_type", receiver_event_type);
                        intent.putExtra("match_id", "");
                        intent.putExtra("isBlocked", meBlocked);
                        intent.putExtra("from", "profile");
                        intent.putExtra("restore_match_id", chat_match_id);
                        intent.putExtra("deletedBy", chat_deleted_by);
                        intent.putExtra("blockedBy", blocked_by);
                        intent.putExtra("isReceiveNotification", isReceiveNotification);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        // isProcessingResultInProgress = false;
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_single_post_ad_view;

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
                adsPresenter.hitDeleteMyAdPost(data.getId());
                break;
            case R.id.editIV:

                Intent intent = new Intent(context, updatePostAdsActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof postAdDeleteResponse) {
            Log.e("RESPONSEGetUSer",new Gson().toJson(test));
            postAdDeleteResponse response = (postAdDeleteResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                appUtils.showToast(response.getMessage());
                goback();
            }
            else
            {
                appUtils.showToast(response.getMessage());

            }

        }
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }



// new chat

    private QBUser getUserFromSession(){
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name",user.getLogin()+"  "+user.getPassword());
        return user;
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





    private void getProfile() {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();
        Log.d("getProfile", "getProfile: userid"+String.valueOf(chatData.getUserId()));
        loginRegisterProfilePresenter.hitGetProfile(String.valueOf(chatData.getUserId()), new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();


                if (response.body() instanceof GetProfile) {
                    GetProfile res = (GetProfile) response.body();
                    userProfileData = res.getData();
                    notiReceiverQbId = String.valueOf(res.getData().getQuickbloxId());
                    Log.d("onComplete", "onComplete: notiReceiverQbId  "+notiReceiverQbId);
                    chat_deleted_by = String.valueOf(res.getData().getDeletedBy());
                    blocked_by=String.valueOf(res.getData().getBlockedBy());
                    isReceiveNotification = res.getData().getReceive_notification() !=null ? res.getData().getReceive_notification() : isReceiveNotification;
                    receiverId = String.valueOf(res.getData().getId());
                    receiverName = res.getData().getName();
                    receiverImage = res.getData().getProfileImage();


                    if (res.getData().getMatchInfo1().size() == 0 && res.getData().getMatchInfo2().size() > 0) {
                        // Toast.makeText(context, "match 2 fill", Toast.LENGTH_SHORT).show();
                        match_2_fill = true;
                        receiver_event_type = res.getData().getMatchInfo2().get(0).getEventType();
                        chat_match_id = String.valueOf(res.getData().getMatchInfo2().get(0).getId());
                        chatIV.setVisibility(View.VISIBLE);
                    }

                    if (res.getData().getMatchInfo2().size() == 0 && res.getData().getMatchInfo1().size() > 0) {
                        //Toast.makeText(context, "match 1 fill", Toast.LENGTH_SHORT).show();
                        receiver_event_type = res.getData().getMatchInfo1().get(0).getEventType();
                        chat_match_id = String.valueOf(res.getData().getMatchInfo1().get(0).getId());
                        match_1_fill = true;
                        chatIV.setVisibility(View.VISIBLE);
                    }

                        if (userProfileData != null && userProfileData.getBlockedBy() != null) {
                            String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
                            Boolean isBlocked = String.valueOf(userProfileData.getBlockedBy()).equalsIgnoreCase(my_user_id);
                            if (isBlocked)
                                chatIV.setVisibility(View.GONE);
                            else if (!isBlocked && userProfileData.getBlockedBy() != 0)
                                meBlocked = true;
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
    protected void onResume() {
        super.onResume();
        getProfile();
    }
}
