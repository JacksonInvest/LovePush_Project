package com.lovepushapp.matchList;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.matchList.adapter.matchRequestsAdapter;
import com.lovepushapp.model.response.LikeResponse;
import com.lovepushapp.model.response.NotificationResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class myMatchRequests extends BaseActivity implements LoginMvp {

    @BindView(R.id.noDataFoundTV) AppCompatTextView noDataFoundTV;

    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    List<NotificationResponse.Data> dataList = new ArrayList<>();
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    boolean is_send_notification = false;
    String notiEventType,acceptEventType,clickedUserQBID= "";
    String likeAcceptType;
    private String TAG = "myMatchRequests";


    public static Intent createIntent(Context context) {
        return new Intent(context, myMatchRequests.class);
    }
    private Activity context = myMatchRequests.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_match_requests;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.match_requests));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);
       // loginRegisterProfilePresenter.hitGetNotification();

        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataList.clear();
        loginRegisterProfilePresenter.hitGetNotification(true);
    }

    private void setAdapter() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            matchRequestsAdapter adap = new matchRequestsAdapter(dataList,context, new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    List<NotificationResponse.Data> data;
                    data= (List<NotificationResponse.Data>) o;
                    String user_id= String.valueOf(data.get(position).getUser_info().getId());
                    String event_type=data.get(position).getEvent_type();
                    String connect_id =  data.get(position).getEvent_id();
                    String noti_id = data.get(position).getId();

                    switch (type)
                    {
                        case 1:

//                            Toast.makeText(getContext(), ""+event_type, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), ""+data.get(position).getEvent_info().getStatus(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, ProfileActivity.class).putExtra("from", "notification").putExtra("user_id",user_id).putExtra("event_type",event_type)
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
            recyclerView.setAdapter(adap);
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

                dataList.clear();
                List<NotificationResponse.Data> data = notificationResponse.getData();
                for (NotificationResponse.Data item : data) {
                    if (item.getEvent_type().equals("S_L_R") || item.getEvent_type().equals("S_C_R")){
                        dataList.add(item);
                    }
                }

                if (dataList.size()>0){
                    noDataFoundTV.setVisibility(View.GONE);

                    setAdapter();
                }else {
                    noDataFoundTV.setText(getString(R.string.noDataFound));
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
                Log.d("GetExploreResponse",""+dataList.toString());
            }
            else
            {
                if (notificationResponse.getData() == null) {
                    noDataFoundTV.setText(notificationResponse.getMessage());
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
            }
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
                    alertPopup(response.getMessage());
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
                    dataList.clear();
                    loginRegisterProfilePresenter.hitGetNotification(true);
                   // alertPopup(response.getMessage());
                }

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(context, "Api error", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }
    }

}
