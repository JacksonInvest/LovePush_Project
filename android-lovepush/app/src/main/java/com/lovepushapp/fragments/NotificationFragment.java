package com.lovepushapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.activities.LoginActivity;
import com.lovepushapp.adapters.NotificationAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.model.response.LikeResponse;
import com.lovepushapp.model.response.NotificationResponse;
import com.lovepushapp.model.response.clearNotificationResponse;
import com.lovepushapp.model.response.readNotificationResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.utils.UsersUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.services.SubscribeService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NotificationFragment extends BaseFragment implements LoginMvp {

    @BindView(R.id.notificationRV)
    RecyclerView notificationRV;
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.noDataFoundTV) AppCompatTextView noDataFoundTV;

    @BindView(R.id.clearNotification)
    ImageView clearNotification;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    private List<NotificationResponse.Data> dataList = new ArrayList<>();

    private boolean is_send_notification = false;
    private String notiEventType,acceptEventType,clickedUserQBID= "";
    private String likeAcceptType;
    private String TAG = "myMatchRequests";

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }

    private void logout() {
        SubscribeService.unSubscribeFromPushes(getContext());
        LoginService.logout(getContext());
        UsersUtils.removeUserData(getContext());
        requestExecutor.signOut();
        SharedPrefsHelper.getInstance().removeQbUser();
        SharedPrefsHelper.getInstance().clearAllData();
        sharedStorage.clearLocalStorage();
        QbDialogHolder.getInstance().clear();
        appUtils.startActivity(getActivity(),
                LoginActivity.createIntent(getActivity()),
                true,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenTitleTV.setText(getString(R.string.notification));
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(getActivity());
        loginRegisterProfilePresenter.attachView(this);
        loginRegisterProfilePresenter.hitGetNotification(true);
//        setAdapter();

        clearNotification.setVisibility(View.GONE);
        clearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.clear_notification_alert))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                loginRegisterProfilePresenter.hit_clear_notifications();

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        loginRegisterProfilePresenter.hitReadNotifications(false);



    }

    @Override
    public void onResume() {
        super.onResume();
        loginRegisterProfilePresenter.hitGetNotification(true);
    }

    private void setAdapter() {
        try {
            notificationRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            NotificationAdapter adap = new NotificationAdapter(dataList,getActivity(), new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    List<NotificationResponse.Data> data;
                    data= (List<NotificationResponse.Data>) o;
                    String user_id= String.valueOf(data.get(position).getUser_info().getId());
                    String event_type = data.get(position).getEvent_type();
                    String connect_id =  data.get(position).getEvent_id();
                    String noti_id = data.get(position).getId();
                   /* if (data.get(position).getEvent_info()!=null){
                        connect_id = data.get(position).getEvent_info().getId();
                        event_status = data .get(position).getEvent_info().getStatus();

                    }*/
                    switch (type)
                    {
                        case 1:


                          startActivity(new Intent(getContext(),ProfileActivity.class).putExtra("from", "notification").putExtra("user_id",user_id).putExtra("event_type",event_type)
                                    .putExtra("noti_id",noti_id).putExtra("connect_id",connect_id));
//                            Toast.makeText(getContext(), ""+data.get(position).getNotification_message().getMessage(), Toast.LENGTH_SHORT).show();
//                            if (o instanceof )
                            break;

                        case 2:
                            clickedUserQBID = data.get(position).getUser_info().getQuickbloxId();
                            is_send_notification =true;
                            notiEventType = "Accept";
                            //notiReceiverQbId = data.get(position).

                            if (event_type.equalsIgnoreCase("S_C_R"))
                            {
                                String status="A";
                                acceptEventType = "S_C_R";
                                sendAcceptRejectConnectRequest(connect_id,status);

                            }
                            if (event_type.equalsIgnoreCase("S_M_R"))
                            {
                                String status="A";
                                acceptEventType = "S_M_R";

//                    Toast.makeText(context, "Accept chat request on testing", Toast.LENGTH_SHORT).show();
                                sendChatAcceptRejectRequest(status,connect_id);
                            }

                            if (event_type.equalsIgnoreCase("S_L_R")){
                                String like="1";

                                is_send_notification = true;

                                notiEventType = "Like";
                                loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(user_id),like);
                            }

                            break;
                        case 3:
                            is_send_notification =false;
                            if (event_type.equalsIgnoreCase("S_C_R")) {
                                String status2 = "R";
                                sendAcceptRejectConnectRequest(connect_id, status2);
                            }
                            if (event_type.equalsIgnoreCase("S_M_R"))
                            {
                                String status2 = "R";
                                sendChatAcceptRejectRequest(status2,connect_id);
//                    Toast.makeText(context, "Reject chat request on testing", Toast.LENGTH_SHORT).show();
                            }
                            if (event_type.equalsIgnoreCase("S_L_R")){
                                is_send_notification = false;

                                String dislike="0";

                                loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(user_id),dislike);
                            }
                            break;
                    }


                }
            });
            notificationRV.setAdapter(adap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendChatAcceptRejectRequest(String status,String connect_id) {
        loginRegisterProfilePresenter.hitChatAccept_RejectRequest(connect_id,status);
    }

    private void sendAcceptRejectConnectRequest(String connect_id, String status) {
        loginRegisterProfilePresenter.hitAcceptConnectRequest(connect_id,status);
    }




    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof NotificationResponse) {
            NotificationResponse notificationResponse = (NotificationResponse) test;

            if (notificationResponse.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                dataList=notificationResponse.getData();
                if (dataList.size()>0){
                    noDataFoundTV.setVisibility(View.GONE);

                    setAdapter();
                    clearNotification.setVisibility(View.VISIBLE);
                    int totalCount = notificationResponse.getData().size();
                    Log.d(TAG,"noti_count "+totalCount +" shared "+new PreferenceManager().getNotificationCount());
                    new PreferenceManager().saveNotificationCount(totalCount);
                }else {
                    noDataFoundTV.setText(notificationResponse.getMessage());
                    noDataFoundTV.setVisibility(View.VISIBLE);
                    clearNotification.setVisibility(View.GONE);
                }
                Log.d("GetExploreResponse",""+dataList.toString());
            }
            else
            {
                if (notificationResponse.getData() == null) {
                    noDataFoundTV.setText(notificationResponse.getMessage());
                    noDataFoundTV.setVisibility(View.VISIBLE);
                    clearNotification.setVisibility(View.GONE);
                }

            }



//            Toast.makeText(getContext(), ""+notificationResponse.getData().get(0).getEvent_info().getId(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), ""+notificationResponse.getMessage(), Toast.LENGTH_SHORT).show();

        }

        if (test instanceof LikeResponse)
        {
            LikeResponse response= (LikeResponse) test;

            if (response.getStatus()) {
                if (response.getData() == null){
                    likeAcceptType = "Send";
                    if (is_send_notification){
                        sendMsgPushNotification(notiEventType);
                    }
                    //alertPopup(response.getMessage());
                }else {
                    likeAcceptType = "Accept";
                    if (is_send_notification){
                        sendMsgPushNotification(notiEventType);
                       /* Intent intent = new Intent(context, matchedSuccessActivity.class);
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName",receiverName);
                        intent.putExtra("receiverId",receiverId);
                        intent.putExtra("event_type",response.getData().getMatchDetails().getEventType()+"");
                        intent.putExtra("receiverQb_id",notiReceiverQbId);
                        intent.putExtra("restore_match_id",response.getData().getMatchDetails().getId()+"");
                        intent.putExtra("deletedBy",chat_deleted_by);
                        intent.putExtra("blockedBy",blocked_by);
                        startActivity(intent);*/
                    }

                    // alertPopup(response.getMessage());
                }
                dataList.clear();
                loginRegisterProfilePresenter.hitGetNotification(true);
            }
        }

        if (test instanceof clearNotificationResponse)
        {

            clearNotificationResponse response= (clearNotificationResponse) test;

            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                dataList.clear();
                loginRegisterProfilePresenter.hitGetNotification(true);
            }else {
                alertPopup(response.getMessage());
            }
        }

        if (test instanceof readNotificationResponse)
        {

            readNotificationResponse response= (readNotificationResponse) test;

            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                //dataList.clear();
               // loginRegisterProfilePresenter.hitGetNotification(true);
            }else {
               // alertPopup(response.getMessage());
            }
        }

    }

    private void sendMsgPushNotification(String notiEventType) {
        String my_shared_name =  String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_image =  String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_qb_id =  String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        String message = "Notification";

        if (notiEventType.equalsIgnoreCase("Accept")){
            if (acceptEventType.equalsIgnoreCase("S_C_R")) {
                message = my_shared_name+" has accepted connect request.";
            }
            if (acceptEventType.equalsIgnoreCase("S_M_R")) {
                message = my_shared_name+" has accepted chat request.";
            }


        }

        if (notiEventType.equalsIgnoreCase("Like")){
            if (likeAcceptType.equalsIgnoreCase("Send")){
                message = my_shared_name+" has sent you a date request.";
            }
            if (likeAcceptType.equalsIgnoreCase("Accept")){
                message = my_shared_name+" has accepted date request.";

            }
        }


        if (clickedUserQBID.equalsIgnoreCase("")){
            clickedUserQBID = "0";
        }
        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(Integer.valueOf(clickedUserQBID));

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.PRODUCTION);
//        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);
        //event.setPushType(QBPushType.APNS);

        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
            data.put("alert", "1");
            data.put("ios_alert", "1");
            data.put("user_qb_id", my_qb_id);
            data.put("user_id",my_shared_id);
            data.put("user_name", my_shared_name);
            data.put("notify_type","notification");
            data.put("event_type", notiEventType);
            data.put("event_id", "0");
            data.put("user_image", my_shared_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setMessage(data.toString());
        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle args) {
                // sent
                Log.e(TAG,"PushNotification "+" Success "+data+" qbId "+clickedUserQBID);

            }
            @Override
            public void onError(QBResponseException errors) {
            }
        });

    }
    public void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dataList.clear();
                        loginRegisterProfilePresenter.hitGetNotification(true);

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public <T> void onApiError(T test) {
        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(getContext(), "Api error", Toast.LENGTH_SHORT).show();
    }
}
