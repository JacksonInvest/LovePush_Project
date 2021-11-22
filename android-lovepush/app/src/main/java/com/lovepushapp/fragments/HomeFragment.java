package com.lovepushapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.LoginActivity;
import com.lovepushapp.activities.createPostActivity;
import com.lovepushapp.activities.singleHomePostView;
import com.lovepushapp.adapters.PostAdapter;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.quickBloxTest.ui.activity.ChatActivity;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.quickBloxTest.utils.qb.callback.QbEntityCallbackImpl;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.utils.UsersUtils;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBEventType;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

public class HomeFragment extends BaseFragment implements PostAdsMvp {


    private static QBChatDialog qbChatDialog = null;
    Unbinder unbinder;
    PostAdsPresenter adsPresenter;
    @BindView(R.id.radarRV)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    String searchString = "";
    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;
    PostAdapter adap;
    private int last_page_pager = 1;
    private int current_page = 1;
    private List<getHomePostResponse.Data.Datum> dataList = new ArrayList<>();
    private String check = "new";
    private AlertDialogs progressDialog;
    private boolean isChatDialogCreated = false;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void sendMsgPushNotification(String notiEventType, String quikboxId) {
        String my_shared_name = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_image = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_qb_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        if (!quikboxId.equalsIgnoreCase(my_qb_id)) {

            StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
            userIds.add(Integer.valueOf(quikboxId));

            QBEvent event = new QBEvent();
            event.setUserIds(userIds);
            event.setEnvironment(QBEnvironment.PRODUCTION);
//            event.setEnvironment(QBEnvironment.DEVELOPMENT);
            event.setNotificationType(QBNotificationType.PUSH);
            event.setType(QBEventType.ONE_SHOT);
            //event.setPushType(QBPushType.APNS);

            JSONObject data = new JSONObject();
            try {
                data.put("message", my_shared_name + " likes your post.");
                data.put("alert", "1");
                data.put("ios_alert", "1");
                data.put("user_qb_id", my_qb_id);
                data.put("user_id", my_shared_id);
                data.put("user_name", my_shared_name);
                data.put("notify_type", "notification");
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
                    Log.e(TAG, "PushNotification " + " Success " + data + " qbId " + quikboxId);

                }

                @Override
                public void onError(QBResponseException errors) {
                   // Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adsPresenter = new PostAdsPresenter(getActivity());
        adsPresenter.attachView(this);

        recyclerView.setNestedScrollingEnabled(false);
        progressDialog = new AlertDialogs(getActivity());

        if (nestedScroll != null) {

            nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (current_page < last_page_pager) {
                                progressBar.setVisibility(View.VISIBLE);
                                current_page++;

                                adsPresenter.hitGetHomePosts(current_page, false);

                            }


                        }
                    }, 500);

                    // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }


    private void setAdapter(List<getHomePostResponse.Data.Datum> dataList) {
        String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        Boolean isBlocked=false;
        try {
            ArrayList<getHomePostResponse.Data.Datum> filteredList = new ArrayList<>();

            for(getHomePostResponse.Data.Datum s: dataList){
                if(s.getBlockedBy().equalsIgnoreCase("0")){
                    filteredList.add(s);
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            adap = new PostAdapter(my_user_id, getActivity(), filteredList, (view, position, type, o) -> {
                List<getHomePostResponse.Data.Datum> list = (List<getHomePostResponse.Data.Datum>) o;
                if (type == 1) {
                    // Toast.makeText(getContext(), ""+"chat", Toast.LENGTH_SHORT).show();
                    if (list.get(position).getMatchQbID() != null) {
                        if (!list.get(position).getMatchQbID().getQbDialogId().equals("")) {
                            String dialog_id = list.get(position).getMatchQbID().getQbDialogId();
                            String receiverName = list.get(position).getUserInfo().getName();
                            String receiverImage = list.get(position).getUserInfo().getProfileImage();
                            String receiverId = String.valueOf(list.get(position).getUserInfo().getId());
                            String event_type = list.get(position).getMatchQbID().getMatchInfo().getEventType();
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
                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                            intent.putExtra("dialog_id", dialog_id);
                                            intent.putExtra("newDialogCreate", "false");
                                            intent.putExtra("receiverImage", receiverImage);
                                            intent.putExtra("receiverName", receiverName);
                                            intent.putExtra("receiverId", receiverId);
                                            intent.putExtra("event_type", event_type);
                                            intent.putExtra("match_id", "");
                                            intent.putExtra("from", "home");
                                            intent.putExtra("restore_match_id", list.get(position).getMatchId() + "");
                                            intent.putExtra("deletedBy", list.get(position).getDeletedBy());
                                            intent.putExtra("blockedBy", list.get(position).getBlockedBy());
                                            intent.putExtra("isReceiveNotification", list.get(position).getUserInfo().getReceive_notification());


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
                    } else {
                        ArrayList<QBUser> qbUsers = new ArrayList<>();

                        QBUser user1 = new QBUser();
                        String chatName = "";

                        user1.setId(Integer.parseInt(list.get(position).getUserInfo().getQuickbloxId()));
                        chatName = list.get(position).getUserInfo().getName();

                        qbUsers.add(user1);
                        check = "start";
                        createDialog(qbUsers, list, position, chatName);
                    }

                } else if (type == 2) {

                    if (list.get(position).getIsLiked() == 1) {
                        adsPresenter.hitHomePostLikeDislike("0", String.valueOf(list.get(position).getId()));
                        dataList.get(position).setIsLiked(0);
                        adap.notifyDataSetChanged();
                    } else if (list.get(position).getIsLiked() == 0) {
                        sendMsgPushNotification("Like", list.get(position).getUserInfo().getQuickbloxId());
                        adsPresenter.hitHomePostLikeDislike("1", String.valueOf(list.get(position).getId()));
                        dataList.get(position).setIsLiked(1);
                        adap.notifyDataSetChanged();
                    }
                } else if (type == 3) {
                    Intent intent = new Intent(getContext(), singleHomePostView.class);
                    intent.putExtra("data", new Gson().toJson(list.get(position)));
                    startActivity(intent);
                }


            });
            recyclerView.setAdapter(adap);


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name", user.getLogin() + "  " + user.getPassword());
        return user;
    }


    private void createDialog(final ArrayList<QBUser> selectedUsers, List<getHomePostResponse.Data.Datum> dataList, int position, String chatName) {
//        Log.e("SELECTED_USER",new Gson().toJson(selectedUsers));
        progressDialog.show();
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers, chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        Log.e("QBCHATDI24", String.valueOf(dialog.getOccupants()));
                        Log.e("dialogId", "//" + qbChatDialog.getDialogId() + "//" + qbChatDialog.getRoomJid());

                        progressDialog.dismiss();
                        try {
                            // ProgressDialogFragment.hide(getFragmentManager());
                        } catch (Exception e) {
                            Log.e("ProgressExcep", String.valueOf(e));
                        }

                        isChatDialogCreated = true;
                        if (check.equalsIgnoreCase("start")) {
                            chatStart(dataList, position);
                        }

//                        Toast.makeText(getContext(), "create dialog", Toast.LENGTH_SHORT).show();


                        //getActivity().finish();

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

    private void chatStart(List<getHomePostResponse.Data.Datum> data1, int position) {
        if (data1.get(position).getMatchInfo() != null) {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("dialog_id", qbChatDialog.getDialogId());
            intent.putExtra("newDialogCreate", "true");
            intent.putExtra("match_id", data1.get(position).getId());
            intent.putExtra("receiverName", data1.get(position).getUserInfo().getName());
            intent.putExtra("receiverImage", data1.get(position).getUserInfo().getProfileImage());
            intent.putExtra("receiverId", data1.get(position).getUserInfo().getId() + "");
            intent.putExtra("event_type", data1.get(position).getMatchInfo().getEventType());
            intent.putExtra("from", "home");
            intent.putExtra("restore_match_id", data1.get(position).getMatchId() + "");
            intent.putExtra("deletedBy", data1.get(position).getDeletedBy());
            intent.putExtra("blockedBy", data1.get(position).getBlockedBy());
            intent.putExtra("isReceiveNotification", data1.get(position).getUserInfo().getReceive_notification());

            startActivity(intent);
        } else {

            alertPopup(getString(R.string.record_not_found));
        }

    }

    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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
    public void onResume() {
        super.onResume();
        noDataFoundTV.setVisibility(View.GONE);
        current_page = 1;
        dataList.clear();
        adsPresenter.hitGetHomePosts(current_page, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.createRL})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.createRL:
                appUtils.startActivity(getActivity(),
                        createPostActivity.createIntent(getActivity()),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof getHomePostResponse) {
//            Log.e("RESPONSEGetUSer", new Gson().toJson(test));
            getHomePostResponse response = (getHomePostResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                progressBar.setVisibility(View.GONE);
                last_page_pager = response.getData().getLastPage();
                List<getHomePostResponse.Data.Datum> data = response.getData().getData();
                dataList.addAll(data);

                if (dataList.size() > 0) {
                    setAdapter(dataList);
                } else {
                    noDataFoundTV.setText(getString(R.string.noDataFound));
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
                Log.d("GetExploreResponse", "" + dataList.toString());
            } else {
                progressBar.setVisibility(View.GONE);
                noDataFoundTV.setText(response.getMessage());
                noDataFoundTV.setVisibility(View.VISIBLE);
                if (response.getMessage().equalsIgnoreCase("User does not exist."))
                 {
                     logout();
                }

            }

        }
       /* Toast.makeText(getContext(), ""+serviceMode, Toast.LENGTH_SHORT).show();
        if (test instanceof GetHomePostLikeDislike){
            Log.e("RESPONSELikeDislike",new Gson().toJson(test));
            GetHomePostLikeDislike response = (GetHomePostLikeDislike) test;
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();


            }
        }*/
       /* if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.hitHomePostLikeDislike)){
            Log.e("RESPONSELikeDislike",new Gson().toJson(test));
            GetHomePostLikeDislike response = (GetHomePostLikeDislike) test;
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();


            }
        }*/
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
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
