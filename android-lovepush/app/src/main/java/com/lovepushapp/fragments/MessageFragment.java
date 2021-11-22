/*
package com.lovepush.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepush.R;
import com.lovepush.activities.ChatWindowActivity;
import com.lovepush.adapters.ChatListAdapter;
import com.lovepush.adapters.ChatRecentAdapter;
import com.lovepush.callbacks.OnItemClickListenerCutom;
import com.lovepush.core.BaseFragment;
import com.lovepush.core.utils.GlobalsVariables;
import com.lovepush.helper.PreferenceManager;
import com.lovepush.model.response.Chat.getHomePostResponse;
import com.lovepush.modules.Login.LoginMvp;
import com.lovepush.modules.Login.LoginRegisterProfilePresenter;
import com.lovepush.quickblox.ChatActivity;
import com.lovepush.quickblox.ChatActivity2;
import com.lovepush.quickblox.utils.ErrorUtils;
import com.lovepush.quickblox.utils.ProgressDialogFragment;
import com.lovepush.quickblox.utils.SharedPrefsHelper;
import com.lovepush.quickblox.utils.SubscribeToNotification;
import com.lovepush.quickblox.utils.chat.ChatHelper;
import com.lovepush.quickblox.utils.qb.QbChatDialogMessageListenerImp;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.lovepush.quickblox.ChatActivity.EXTRA_DIALOG_ID;


public class MessageFragment extends BaseFragment implements LoginMvp {

    @BindView(R.id.recentChatRV)
    RecyclerView recentChatRV;
    @BindView(R.id.lastChatRV)
    RecyclerView lastChatRV;
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    Unbinder unbinder;
    private static final String TAG = ProgressDialogFragment.class.getSimpleName();

    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;

    static QBChatDialog qbChatDialog = null;

    String check="new";


    int countc=1;

    private boolean isChatDialogCreated = false;
    private ChatMessageListener chatMessageListener;
    private boolean isRequestAccepted =false;


    String my_user_id;








    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;

    List<getHomePostResponse.Data> data2List;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }


    boolean isUser2=false;
    boolean isUser1=false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenTitleTV.setText(getString(R.string.messages));
        setAdapterChatList();
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(getActivity());
        loginRegisterProfilePresenter.attachView(this);
        loginRegisterProfilePresenter.hitGetMyMatches();

        my_user_id=String.valueOf(sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
//        setAdapterRecent();
    }

    private void setAdapterRecent() {
        try {
            recentChatRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            ChatRecentAdapter adap = new ChatRecentAdapter(my_user_id,data2List,getActivity(), new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    List<getHomePostResponse.Data> list;
                    switch (type)
                    {
                        case 11:
                            list= (List<getHomePostResponse.Data>) o;
//                            Toast.makeText(getContext(), ""+list.get(position).getUser_info1().getName(), Toast.LENGTH_SHORT).show();
                           */
/* appUtils.startActivity(getActivity(),
                                    ChatWindowActivity.createIntent(getActivity()).putExtra("from","message_f").putExtra("data",new Gson().toJson(list.get(position).getUser_info1())),
                                    false,
                                    GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                    false,
                                    0
                            );*//*

                            ArrayList<QBUser> qbUsers=new ArrayList<>();



//                           from_user = getIntent().getExtras().getInt("from_user_qb_id");
//            to_user = getIntent().getExtras().getInt("to_user_qb_id");
                            QBUser user1=new QBUser();
                            if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info1().getId()))
                            {
                                user1.setId(Integer.parseInt(list.get(position).getUser_info2().getQuickblox_id()));
                                isUser2=true;
                            }
                            else if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info2().getId()))
                            {
                                isUser1=true;
                                user1.setId(Integer.parseInt(list.get(position).getUser_info1().getQuickblox_id()));
                            }

//                            appUtils.startActivity(getActivity(),
//                                    ChatActivity.createIntent(getActivity()).putExtra("from","message").putExtra("data",new Gson().toJson(list.get(position).getUser_info1()))
//                                            .putExtra("from_user_qb_id",list.get(position).getUser_info2().getQuickblox_id()).putExtra("to_user_qb_id",list.get(position).getUser_info1().getQuickblox_id()),
//                                    false,
//                                    GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
//                                    false,
//                                    0
//                            );

                            qbUsers.add(user1);
                            check="start";
                            createDialog(qbUsers,list,position);





//                            Toast.makeText(getContext(), "chat activity", Toast.LENGTH_SHORT).show();

                            break;
                    }

                }
            });
            recentChatRV.setAdapter(adap);

//            if (countc==2) {
//                countc = countc + 1;
            if (isChatDialogCreated==false)
                createChat(0);
//            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }



    private void setAdapterChatList() {
        try {
            lastChatRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            ChatListAdapter adap = new ChatListAdapter(getActivity(), new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    appUtils.startActivity(getActivity(),
                            ChatWindowActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                }
            });
            lastChatRV.setAdapter(adap);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    @OnClick({R.id.screenTitleTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.screenTitleTV:

                break;

        }
    }

    @Override
    public <T> void onApiResponse(T test) {
        getHomePostResponse userChatList;
        if (test instanceof getHomePostResponse)
        {
            userChatList=(getHomePostResponse) test;
            data2List=userChatList.getData();
            if (data2List.size()>0) {
                setAdapterRecent();
            }
//            Toast.makeText(getContext(), ""+userChatList.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public <T> void onApiError(T test) {
        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

    }






//    quickblox

    private void createDialog(final ArrayList<QBUser> selectedUsers, List<getHomePostResponse.Data> data1, int position) {

//        Log.e("SELECTED_USER",new Gson().toJson(selectedUsers));
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog=dialog;
                        Log.e("QBCHATDI24", String.valueOf(dialog.getOccupants()));
                        Log.e("dialogId", "//"+qbChatDialog.getDialogId() + "//" + qbChatDialog.getRoomJid());

//                        Toast.makeText(getContext(), "id"+dialog, Toast.LENGTH_SHORT).show();
                        qbChatDialog.addMessageListener(chatMessageListener);
                        try {
                            ProgressDialogFragment.hide(getFragmentManager());
                        }
                        catch (Exception e)
                        {
                            Log.e("ProgressExcep", String.valueOf(e));
                        }

                        isChatDialogCreated = true;
                        if (check.equalsIgnoreCase("start"))
                        {
                            chatStart(data1,position);
                        }

//                        Toast.makeText(getContext(), "create dialog", Toast.LENGTH_SHORT).show();







                        //getActivity().finish();

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        ProgressDialogFragment.hide(getFragmentManager());
                        Log.e("QUICKBLOX", String.valueOf(e));
                        showSnackbarError(getView(), R.string.something,null,null);
                    }
                }
        );
    }

    private class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.e(TAG, "processMessage");
            if (isRequestAccepted) {
                Integer count = getPref().getMessageCount();
                count++;
                getPref().setMessageCount(count);

                setMessageCount();
            }
        }
    }

    private void setMessageCount() {
        Integer count = getPref().getMessageCount();

        if (count>0) {
//            Drawable buttonDrawable = txtMessageCount.getBackground();
//            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
//            DrawableCompat.setTint(buttonDrawable, Color.RED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                txtMessageCount.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.red));
            } else {
//                txtMessageCount.setBackgroundResource(R.drawable.msg_notification_circle);
            }
        } else {
//            txtMessageCount.setBackgroundResource(R.drawable.msg_notification_circle);
        }

//        if (count>9)
//            txtMessageCount.setText("9+");
//        else
//            txtMessageCount.setText(""+count);
    }

    public PreferenceManager getPref() {
        return PreferenceManager.getInstance(getContext());
    }

    protected void showSnackbarError(View rootLayout, @StringRes int resId, QBResponseException e, View.OnClickListener clickListener) {
        ErrorUtils.showSnackbar(rootLayout, resId, e, R.string.dlg_retry, clickListener);
    }



    private void login(int pos) {




//
//        UserDataModel userDataModel=new UserDataModel();
//        userDataModel.quickbloxEmail="CardApp";
//        userDataModel.usertype=2;
//        requestUserData.quickBloxId="93227556";
//        userDataModel.id=93227556;
//        getPref().setUserData(userDataModel);

//        my_user_id=String.valueOf(sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));


        String my_quickblox_id=  String.valueOf(sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));


//        Toast.makeText(getContext(), "login"+my_quickblox_id, Toast.LENGTH_SHORT).show();

        String my_id=data2List.get(0).getUser_info2().getQuickblox_id();
        final QBUser user = new QBUser();

        Log.v("MYQB_ID",my_quickblox_id);

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));

//        Log.e("USER", String.valueOf(new Gson().toJson(QBUsers.getUser(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id())))));
//        user.setLogin(my_id);
//        user.setPassword(GlobalsVariables.quickblox_password);

//        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
//        pagedRequestBuilder.setPage(0);
//        pagedRequestBuilder.setPerPage(50);

//        Toast.makeText(getContext(), ""+my_id, Toast.LENGTH_SHORT).show();

//        QBUsers.getUser(Integer.parseInt(my_id)).performAsync(new QBEntityCallback<QBUser>() {
//
//            @Override
//            public void onSuccess(QBUser qbUser, Bundle bundle) {
//                Log.e("SUCEESS", String.valueOf(qbUser));
//
//                user[0] =qbUser;
//                user[0].setPassword(GlobalsVariables.quickblox_password);
//
//            }
//
//            @Override
//            public void onError(QBResponseException errors) {
//                Log.e("error" , errors.toString());
//                Log.e("ERROR", String.valueOf(errors));
//
//            }
//        });




        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);

                SubscribeToNotification.getInstance().setSubscribeToNotification(getContext());


                ArrayList<QBUser> qbUsers2=new ArrayList<>();
                QBUser user2 = new QBUser();
                QBUser user1=new QBUser();
//                user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));

     try {
                    user2.setPassword(QBSessionManager.getInstance().getToken());
                }catch(Exception e) {
                    e.printStackTrace();
                    // means you have not created a session before
                }

//                qbUsers2.add(user1);
//                Toast.makeText(getContext(), "login", Toast.LENGTH_SHORT).show();

//                QBUser user1=new QBUser();
                if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId()))
                {
                    user1.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                    isUser2=true;
                }
                else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId()))
                {
                    isUser1=true;
                    user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                }
                qbUsers2.add(user1);
//              now comment
                createDialog(qbUsers2, null, pos);



                ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getFragmentManager());
                ErrorUtils.showSnackbar(getView(), R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login(pos);
                            }
                        });
            }
        });
    }

    private QBUser getUserFromSession(){
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name",user.getLogin()+"  "+user.getPassword());
        return user;
    }

    private void restoreChatSession(int position) {
//        if (check.equalsIgnoreCase("start"))
//        {
//            Toast.makeText(getContext(), "not ", Toast.LENGTH_SHORT).show();
//            QBUser currentUser = getUserFromSession();
//            if (currentUser == null) {
//                login(position);
//            } else {
////                login(position);
//                loginToChat(position,currentUser);
//            }
//        }
//        else
        if (ChatHelper.getInstance().isLogged()) {

//            Toast.makeText(getContext(), "restore chat session", Toast.LENGTH_SHORT).show();
            QBUser currentUser = getUserFromSession();
            SharedPrefsHelper.getInstance().saveQbUser(currentUser);

            ArrayList<QBUser> qbUsers=new ArrayList<>();
            QBUser qbUser=new QBUser();
//            qbUser.setId(Integer.parseInt(userChatList.get(0).getUser_info2().getQuickblox_id()));

            QBUser user1=new QBUser();
//            user1.setId(Integer.parseInt(data2List.get(position).getUser_info1().getQuickblox_id()));

//            Toast.makeText(getContext(), "id "+userChatList.get(position).getUser_info1().getQuickblox_id(), Toast.LENGTH_SHORT).show();
            if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId()))
            {
                user1.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                isUser2=true;
            }
            else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId()))
            {
                isUser1=true;
                user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
            }

            qbUsers.add(user1);
//            qbUsers.add(1,user1.setId(userChatList.get(0).getUser_info1().getQuickblox_id()));
//            qbUsers.add(qbUser);
            createDialog(qbUsers, null, position);

            //ProgressDialogFragment.hide(getFragmentManager());
        } else {
//            Toast.makeText(getContext(), "not restore", Toast.LENGTH_SHORT).show();
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                login(position);
            } else {
                loginToChat(position,currentUser);
            }
        }
    }

    private void loginToChat(int pos,final QBUser user) {
        //ProgressDialogFragment.show(getFragmentManager(), R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()");

                //ProgressDialogFragment.hide(getFragmentManager());
                SharedPrefsHelper.getInstance().saveQbUser(user);

                if (SubscribeToNotification.getInstance().isSubscribeToNotification(getContext()))
                    SubscribeToNotification.getInstance().setSubscribeToNotification(getContext());
                ArrayList<QBUser> qbUsers=new ArrayList<>();
                QBUser qbUser=new QBUser();
//                int id=userChatList.get(pos).getUser_info1().getQuickblox_id();
//                List<Integer> userID=new ArrayList<>();
//
//
//                QBUser qbUser12 = null;
//
//                 List<QBUser> objectsList=new ArrayList<>();


//                userID.add(userChatList.get(0).getUser_info2().getQuickblox_id());
//                userID.add(userChatList.get(0).getUser_info1().getQuickblox_id());


                if (check.equalsIgnoreCase("start"))
                {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                }
                else
                {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                }


                   */
/* for (Integer id2 : userID) {
                        qbUser.setId(id2);
                        Log.e("QBUSER", String.valueOf(id2));
                        Log.e("QBUSER2", String.valueOf(qbUser.getId()));
//                        if (qbUser.getId().equals(id2)) {
//                            selectedUsers.add(user);
//                            initiallySelectedUsers.add(user.getId());
                            break;
//                        }
                    }*//*

//                   qbUser.setId(userID.get(0));
//                qbUser.setId(userID.get(0));

//                qbUser.setId(158156);
//
//                QBUser user1=new QBUser();
//                user1.setId(userChatList.get(0).getUser_info2().getQuickblox_id());
//
//
//                qbUsers.add(user1);
                if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId()))
                {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                    isUser2=true;
                }
                else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId()))
                {
                    isUser1=true;
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                }


                qbUsers.add(qbUser);
//                qbUsers.add(user1);
                createDialog(qbUsers, null, pos);
//                Toast.makeText(getContext(), "login to chat", Toast.LENGTH_SHORT).show();
                */
/*Intent intent1=new Intent(context, ChatActivity.class);
                intent1.putExtra(EXTRA_DIALOG_ID, qbChatDialog);
                context.startActivity(intent1);
                getActivity().finish();*//*

                */
/*DialogsActivity.start(SplashActivity.this);
                finish();*//*

            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getFragmentManager());
                Log.w(TAG, "Chat login onError(): " + e);
                showSnackbarError(getView(), R.string.error_recreate_session, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginToChat(pos,user);
                            }
                        });
            }
        });
    }

    private void chatStart(List<getHomePostResponse.Data> data1, int position) {

        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(EXTRA_DIALOG_ID, qbChatDialog);
        intent.putExtra("from","message");
        intent.putExtra("user", new Gson().toJson(""));
        if (isUser1) {
            intent.putExtra("type", "u1");
            intent.putExtra("from_user_qb_id", data1.get(position).getUser_info2().getQuickblox_id());
            intent.putExtra("to_user_qb_id", data1.get(position).getUser_info1().getQuickblox_id());
        }
        if (isUser2) {
            intent.putExtra("type", "u2");
            intent.putExtra("from_user_qb_id", data1.get(position).getUser_info1().getQuickblox_id());
            intent.putExtra("to_user_qb_id", data1.get(position).getUser_info2().getQuickblox_id());
        }
        intent.putExtra("data", new Gson().toJson(data1.get(position)));
//
        intent.putExtra("user2", new Gson().toJson(data1.get(position).getUser_info2()));
//
//
        //startActivity(intent);
        ChatActivity2.startForResult(getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE,qbChatDialog);
        Log.e("MY_USER_ID",my_user_id);
    }

    private void createChat(int position) {
//        Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "create chat", Toast.LENGTH_SHORT).show();
        ProgressDialogFragment.show(getFragmentManager(), R.string.dlg_loading);

        if (SharedPrefsHelper.getInstance().hasQbUser()) {
            restoreChatSession(position);

        } else {
            login(position);
//            Toast.makeText(getContext(), "login", Toast.LENGTH_SHORT).show();
        }
    }














}
*/
