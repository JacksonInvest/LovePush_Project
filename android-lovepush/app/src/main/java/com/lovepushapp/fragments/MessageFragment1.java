package com.lovepushapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.DashboardActivity;
import com.lovepushapp.adapters.ChatDialogAdapter;
import com.lovepushapp.adapters.ChatRecentAdapter;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.helper.SwipeController;
import com.lovepushapp.model.response.Chat.DeleteMatchResponse;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.blockUserResponse;
import com.lovepushapp.model.response.deleteChatResponse;
import com.lovepushapp.model.response.unmatchUserResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.quickBloxTest.async.BaseAsyncTask;
import com.lovepushapp.quickBloxTest.managers.DialogsManager;
import com.lovepushapp.quickBloxTest.ui.activity.ChatActivity;
import com.lovepushapp.quickBloxTest.utils.ErrorUtils;
import com.lovepushapp.quickBloxTest.utils.FcmConsts;
import com.lovepushapp.quickBloxTest.utils.ProgressDialogFragment;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbChatDialogMessageListenerImp;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.quickBloxTest.utils.qb.callback.QbEntityCallbackImpl;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class MessageFragment1 extends BaseFragment implements LoginMvp, DialogsManager.ManagingDialogsCallbacks
        /*, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener*/ {

    private static final String TAG = ProgressDialogFragment.class.getSimpleName();
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;
    //code for chat list test
    private static final int REQUEST_SELECT_PEOPLE = 174;
    private static final int PLAY_SERVICES_REQUEST_CODE = 9000;
    private static QBChatDialog qbChatDialog = null;
    @BindView(R.id.recentChatRV)
    RecyclerView recentChatRV;
    /*@BindView(R.id.lastChatRV)
    RecyclerView lastChatRV;*/
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.dialogsRV)
    RecyclerView dialogsRV;
    @BindView(R.id.progress_dialogs)
    ProgressBar progressBar;
    @BindView(R.id.swipy_refresh_layout)
    SwipyRefreshLayout setOnRefreshListener;
    @BindView(R.id.rootLAyout)
    LinearLayout rootLAyout;
    @BindView(R.id.requestCountTV)
    AppCompatTextView requestCountTV;
    Unbinder unbinder;
    SwipeController swipeController = null;
    int countc = 1;
    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;
    @BindView(R.id.newestTV)
    AppCompatTextView newestTV;
    boolean isUser2 = false;
    boolean isUser1 = false;
    private String check = "new";
    private boolean isChatDialogCreated = false;
    private ChatMessageListener chatMessageListener;
    private String my_user_id;
    private Integer my_qb_id;
    private List<UserChatList.Data> newestList = new ArrayList<>();
    private List<UserChatList.Data> filteredChatList = new ArrayList<>();
    private List<UserChatList.Data> data2List = new ArrayList<>();
    private List<String> filteredQbDialogList = new ArrayList<>();
    private QBRequestGetBuilder requestBuilder;
    private int skipRecords = 0;
    private boolean isProcessingResultInProgress;
    private BroadcastReceiver pushBroadcastReceiver;
    private ChatDialogAdapter dialogsAdapter;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private QBUser currentUser;
    private List<QBChatDialog> dialogsList;
    private AlertDialogs progressDialog;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    private String deleteNeededMAtchId;
    //private int deleteNeededPostion;
    private QBChatDialog qbDialogDelete;

    public static MessageFragment1 newInstance() {
        MessageFragment1 fragment = new MessageFragment1();
        return fragment;
    }

    public static void start(Context context, boolean isRunForCall) {
        /*Intent intent = new Intent(context, OpponentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);*/
        //Toast.makeText(context, "New Session", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenTitleTV.setText(getString(R.string.messages));
        //setAdapterChatList();
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(getActivity());
        loginRegisterProfilePresenter.attachView(this);
        progressDialog = new AlertDialogs(getActivity());
        newestTV.setVisibility(View.GONE);
        my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
//        setAdapterRecent();
        my_qb_id = Integer.valueOf(String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING)));
        dialogsList = new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());
        //Toast.makeText(getActivity(), "hello guys i am in message frament 1 "+profile_url, Toast.LENGTH_SHORT).show();

        if (!ChatHelper.getInstance().isLogged()) {
            Log.w(TAG, "Restarting App...");
            // restartApp(getContext());
        }

        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();
        currentUser = ChatHelper.getCurrentUser();

        initUi();


       /* if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
            loadDialogsFromQb(true);
        } else {
            loadDialogsFromQb(false);
        }*/
    }

    private void setAdapterRecent() {
        newestList.clear();
        filteredChatList.clear();
        filteredQbDialogList.clear();
        for (UserChatList.Data item : data2List) {
            if (item.getQbChatDialog() == null) {
                newestList.add(item);
                Collections.sort(newestList, (lhs, rhs) -> rhs.getCreated_at().compareTo(lhs.getCreated_at()));

            }
            if (item.getQbChatDialog() != null) {


                filteredChatList.add(item);

                Log.d(TAG, "setAdapterRecent: " + item.getIsDeleted());

                if (item.getIsDeleted() == 1 || item.getIsBlocked() == 1) {
                } else {
                    filteredQbDialogList.add(item.getQbChatDialog().getQb_dialog_id());

                }

            } else {
                Log.e("dde", "jde");
            }

        }
        if (SharedPrefsHelper.getInstance().hasQbUser()) {
            if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
                loadDialogsFromQb(true);
            } else {
                loadDialogsFromQb(false);
            }

        } else {
            firstTimeLogin();
        }

        if (newestList.size() > 0) {
            newestTV.setVisibility(View.VISIBLE);
        } else {
            newestTV.setVisibility(View.GONE);

        }


        try {
            recentChatRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            ChatRecentAdapter adap = new ChatRecentAdapter(my_user_id, newestList, getActivity(), (view, position, type, o) -> {
                List<UserChatList.Data> list;
                if (type == 11) {
                    list = (List<UserChatList.Data>) o;

                    //                            Toast.makeText(getContext(), ""+list.get(position).getUser_info1().getName(), Toast.LENGTH_SHORT).show();
                       /* appUtils.startActivity(getActivity(),
                                ChatWindowActivity.createIntent(getActivity()).putExtra("from","message_f").putExtra("data",new Gson().toJson(list.get(position).getUser_info1())),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );*/
                    ArrayList<QBUser> qbUsers = new ArrayList<>();


//                           from_user = getIntent().getExtras().getInt("from_user_qb_id");
//            to_user = getIntent().getExtras().getInt("to_user_qb_id");
                    QBUser user1 = new QBUser();
                    String chatName = "";
                    if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info1().getId())) {
                        user1.setId(Integer.parseInt(list.get(position).getUser_info2().getQuickblox_id()));
                        chatName = list.get(position).getUser_info2().getName();

                        isUser2 = true;
                    } else if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info2().getId())) {
                        if (!list.get(position).getUser_info1().getQuickblox_id().isEmpty()) {
                            isUser1 = true;
                            user1.setId(Integer.parseInt(list.get(position).getUser_info1().getQuickblox_id()));
                            chatName = list.get(position).getUser_info1().getName();
                        }
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
                    check = "start";
                    createDialog(qbUsers, list, position, chatName);


//                            Toast.makeText(getContext(), "chat activity", Toast.LENGTH_SHORT).show();
                }

            });
            recentChatRV.setAdapter(adap);

//            if (countc==2) {
//                countc = countc + 1;
            if (isChatDialogCreated == false)
                createChat(0);
//            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    void alertDialog(int pos, List<UserChatList.Data> list) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage("If you want to block this user, your chat will be disabled and you can unblock user from Match List. Are you sure you want to block?");
        builder.setPositiveButton("OK", (dialog, id) -> {
            block(pos,list);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void block(int pos, List<UserChatList.Data> list) {
        String finalReceiverId=my_user_id.equalsIgnoreCase(list.get(pos).getUser_info1().getId()) ? list.get(pos).getUser_info2().getId() : list.get(pos).getUser_info1().getId();
        AlertDialogs dialogs = new AlertDialogs(getActivity());
        //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
        dialogs.show();
        new LoginRegisterProfilePresenter(getActivity()).hit_block_user("block", finalReceiverId, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.body() != null) {
                    blockUserResponse response1 = (blockUserResponse) response.body();
                    if (response1.getStatus()) {
                        Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                        loginRegisterProfilePresenter.hitGetMyMatches();
                        updateDialogsAdapter();
                    }
                    else
                        Toast.makeText(getActivity(), "Something went wrong ?", Toast.LENGTH_SHORT).show();

                }
                else if(response.errorBody()!=null)
                {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(), jObjError.getString("blocked"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }
    private void firstTimeLogin() {

        final QBUser user = new QBUser();


        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        //user.setCustomData(profile_url);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));


        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {


            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);

                SubscribeToNotification.getInstance().setSubscribeToNotification(getContext());

                if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
                    loadDialogsFromQb(true);
                } else {
                    loadDialogsFromQb(false);
                }


                // ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                //progressDialog.dismiss();
                //ProgressDialogFragment.hide(getFragmentManager());
               /* ErrorUtils.showSnackbar(getView(), R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                firstTimeLogin();
                            }
                        });*/
                firstTimeLogin();
            }
        });
    }


    @OnClick({R.id.screenTitleTV, R.id.requestCountTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.screenTitleTV:

                break;
            case R.id.requestCountTV:
                /*appUtils.startActivity(getActivity(),
                        myMatchRequests.createIntent(getActivity()),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );*/
                ((DashboardActivity) getContext()).loadScreen(2);

                break;

        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        UserChatList userChatList;
        Log.e("RESPONSEMESSAGEF", serviceMode);
        if (test instanceof UserChatList) {
            userChatList = (UserChatList) test;

            if (userChatList.getRequests() == 0 || userChatList.getRequests() == null) {
                requestCountTV.setVisibility(View.GONE);
            } else {
                processRequestTV(userChatList.getRequests());
            }
            data2List = userChatList.getData();
            if (data2List.size() > 0) {
                setAdapterRecent();
                noDataFoundTV.setVisibility(View.GONE);
            } else {
                noDataFoundTV.setText(getString(R.string.noMsgYet));
                noDataFoundTV.setVisibility(View.VISIBLE);

            }

            Log.d("GetMessageResponse", "" + data2List.toString());

//            Toast.makeText(getContext(), ""+userChatList.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (test instanceof deleteChatResponse) {
            deleteChatResponse res = (deleteChatResponse) test;
            if (res.getStatus()) {
                loginRegisterProfilePresenter.hitGetMyMatches();
                updateDialogsAdapter();
                appUtils.showToast(res.getMessage());
            }
        }

        if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.deleteMatchRecord)) {
            Log.e("RESPONSEMESSAGEF", "hi i am in");

            DeleteMatchResponse res = (DeleteMatchResponse) test;

            if (res != null) {
                // Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                //appUtils.showToast(res.getMessage());

                progressDialog.show();

                ArrayList<QBChatDialog> currentDialog = new ArrayList<>();
                currentDialog.add(qbDialogDelete);
                ChatHelper.getInstance().deleteDialogs(currentDialog, new QBEntityCallback<ArrayList<String>>() {
                    @Override
                    public void onSuccess(ArrayList<String> dialogsIds, Bundle bundle) {
                        rootLAyout.setEnabled(true);
                        Log.d(TAG, "Dialogs Deleting Successful");
                        progressDialog.dismiss();

                        QbDialogHolder.getInstance().deleteDialogs(dialogsIds);
                        loginRegisterProfilePresenter.hitGetMyMatches();
                        updateDialogsAdapter();


                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Deleting Dialogs Error: " + e.getMessage());
                        rootLAyout.setEnabled(true);
                        progressDialog.dismiss();
                    }
                });
            }

            Log.e("RESPONSEMESSAGEF", new Gson().toJson(res));


        }
        if (test instanceof CommonResponse) {
            CommonResponse res = (CommonResponse) test;
            if (res.getStatus()) {
                loginRegisterProfilePresenter.hit_unmatch_user(deleteNeededMAtchId);
            } else {

                appUtils.showToast(res.getMessage());
            }
        }

        if (test instanceof blockUserResponse) {
            blockUserResponse res = (blockUserResponse) test;
            loginRegisterProfilePresenter.hitGetMyMatches();
            updateDialogsAdapter();
            appUtils.showToast(res.getMessage());
        }

        if (test instanceof unmatchUserResponse) {
            unmatchUserResponse res = (unmatchUserResponse) test;
            loginRegisterProfilePresenter.hitGetMyMatches();
            updateDialogsAdapter();
            appUtils.showToast(res.getMessage());
            loginRegisterProfilePresenter.hitGetNotification(false);
        }


    }

    private void processRequestTV(Integer requestCount) {

        if (requestCount > 0) {
            if (requestCount == 1) {
                requestCountTV.setText(requestCount + " " + getString(R.string.request));
            } else {
                requestCountTV.setText(requestCount + " " + getString(R.string.requests));

            }
            requestCountTV.setVisibility(View.VISIBLE);
        } else {
            requestCountTV.setVisibility(View.GONE);

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

    private void createDialog(final ArrayList<QBUser> selectedUsers, List<UserChatList.Data> dataList, int position, String chatName) {
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

//                        Toast.makeText(getContext(), "id"+dialog, Toast.LENGTH_SHORT).show();
                        qbChatDialog.addMessageListener(chatMessageListener);
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

        /*Log.d(TAG, "Creating Dialog");
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers, chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        Log.d(TAG, "Creating Dialog Successful");
                        isProcessingResultInProgress = false;
                        dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        ArrayList<QBChatDialog> dialogs = new ArrayList<>();
                        dialogs.add(dialog);
                        new DialogJoinerAsyncTask(DialogsActivity.this, dialogs, false).execute();

                        ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, dialog, true);
                        ProgressDialogFragment.hide(getFragmentManager());
                    }

                    @Override
                    public void onError(QBResponseException error) {
                        Log.d(TAG, "Creating Dialog Error: " + error.getMessage());
                        isProcessingResultInProgress = false;
                        ProgressDialogFragment.hide(getFragmentManager());
                        showSnackbarError(getView(), R.string.something,null,null);

                    }
                }
        );*/
    }

   /* @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ChatDialogAdapter.CommonViewHolder) {
            // get the removed item name to display it in snack bar


            qbDialogDelete = dialogsList.get(position);
            QBChatDialog qbChatDialog = dialogsList.get(position);
//        Log.d("DialogAdapter", "getDataList: "+dataList.get(pos).getQbChatDialog().getQb_dialog_id() +" dialogid "+ dialog.getDialogId());

            for (UserChatList.Data item: filteredChatList) {
                if(item.getQbChatDialog().getQb_dialog_id().equals(qbDialogDelete.getDialogId())){
                    deleteNeededMAtchId = item.getId();
                    break;
                }
            }
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Chat")
                    .setMessage("Are you sure, you want to delete the chat?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getContext(), ""+deleteNeededDialogId+"  pos "+deleteNeededPostion, Toast.LENGTH_SHORT).show();
                            rootLAyout.setEnabled(false);
                            loginRegisterProfilePresenter.hit_delete_match(deleteNeededMAtchId);

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }*/

    private void setMessageCount() {
        Integer count = getPref().getMessageCount();

        if (count > 0) {
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

    }

    private PreferenceManager getPref() {
        return PreferenceManager.getInstance(getContext());
    }

    private void showSnackbarError(View rootLayout, @StringRes int resId, QBResponseException e, View.OnClickListener clickListener) {
        ErrorUtils.showSnackbar(rootLayout, resId, e, R.string.dlg_retry, clickListener);
    }

    private void login(int pos) {
        String my_quickblox_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));

//        Toast.makeText(getContext(), "login"+my_quickblox_id, Toast.LENGTH_SHORT).show();
        String profile_url = "";
        profile_url = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));

        Log.d("getProfileData", "profile_url " + profile_url);
        String my_id = data2List.get(0).getUser_info2().getQuickblox_id();
        final QBUser user = new QBUser();

        Log.v("MYQB_ID", my_quickblox_id);

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));


        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {


            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);

                SubscribeToNotification.getInstance().setSubscribeToNotification(getContext());


                ArrayList<QBUser> qbUsers2 = new ArrayList<>();
                QBUser user2 = new QBUser();
                QBUser user1 = new QBUser();
//                user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));

                try {
                    user2.setPassword(QBSessionManager.getInstance().getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    // means you have not created a session before
                }

//                qbUsers2.add(user1);
//                Toast.makeText(getContext(), "login", Toast.LENGTH_SHORT).show();

//                QBUser user1=new QBUser();
                String chatName = "";
                if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId())) {
                    user1.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                    chatName = data2List.get(0).getUser_info2().getName();
                    isUser2 = true;
                } else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId())) {
                    isUser1 = true;
                    user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                    chatName = data2List.get(0).getUser_info1().getName();
                }


                qbUsers2.add(user1);
//              now comment
                createDialog(qbUsers2, null, pos, chatName);


                progressDialog.dismiss();
                // ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
                //ProgressDialogFragment.hide(getFragmentManager());
               /* ErrorUtils.showSnackbar(getView(), R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login(pos);
                            }
                        });*/
                login(pos);

            }
        });
    }

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name", user.getLogin() + "  " + user.getPassword());
        return user;
    }

    private void restoreChatSession(int position) {

//        else
        if (ChatHelper.getInstance().isLogged()) {

//            Toast.makeText(getContext(), "restore chat session", Toast.LENGTH_SHORT).show();
            QBUser currentUser = getUserFromSession();
            SharedPrefsHelper.getInstance().saveQbUser(currentUser);

            ArrayList<QBUser> qbUsers = new ArrayList<>();
            QBUser qbUser = new QBUser();
//            qbUser.setId(Integer.parseInt(userChatList.get(0).getUser_info2().getQuickblox_id()));

            QBUser user1 = new QBUser();
//            user1.setId(Integer.parseInt(data2List.get(position).getUser_info1().getQuickblox_id()));

//            Toast.makeText(getContext(), "id "+userChatList.get(position).getUser_info1().getQuickblox_id(), Toast.LENGTH_SHORT).show();
            String chatName = "";
            if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId())) {
                user1.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                chatName = data2List.get(0).getUser_info2().getName();
                isUser2 = true;
            } else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId())) {
                isUser1 = true;
                user1.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                chatName = data2List.get(0).getUser_info1().getName();
            }
            qbUsers.add(user1);
//            qbUsers.add(1,user1.setId(userChatList.get(0).getUser_info1().getQuickblox_id()));
//            qbUsers.add(qbUser);
            createDialog(qbUsers, null, position, chatName);

            //ProgressDialogFragment.hide(getFragmentManager());
        } else {
//            Toast.makeText(getContext(), "not restore", Toast.LENGTH_SHORT).show();
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                login(position);
            } else {
                loginToChat(position, currentUser);
            }
        }


    }

    private void loginToChat(int pos, final QBUser user) {
        //ProgressDialogFragment.show(getFragmentManager(), R.string.dlg_restoring_chat_session);
        progressDialog.show();
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()");

                //ProgressDialogFragment.hide(getFragmentManager());
                SharedPrefsHelper.getInstance().saveQbUser(user);

                if (SubscribeToNotification.getInstance().isSubscribeToNotification(getContext()))
                    SubscribeToNotification.getInstance().setSubscribeToNotification(getContext());
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser qbUser = new QBUser();

                if (check.equalsIgnoreCase("start")) {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                } else {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                }

                Log.e("getSecondUser", "one " + data2List.get(0).getUser_info1().getQuickblox_id() + " two " + data2List.get(0).getUser_info2().getQuickblox_id());


                String chatName = "";
                if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info1().getId())) {
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info2().getQuickblox_id()));
                    chatName = data2List.get(0).getUser_info2().getName();
                    isUser2 = true;
                } else if (my_user_id.equalsIgnoreCase(data2List.get(0).getUser_info2().getId())) {
                    isUser1 = true;
                    qbUser.setId(Integer.parseInt(data2List.get(0).getUser_info1().getQuickblox_id()));
                    chatName = data2List.get(0).getUser_info1().getName();
                }


                qbUsers.add(qbUser);
//                qbUsers.add(user1);
                createDialog(qbUsers, null, pos, chatName);

            }

            @Override
            public void onError(QBResponseException e) {
                //ProgressDialogFragment.hide(getFragmentManager());
                progressDialog.dismiss();
                Log.w(TAG, "Chat login onError(): " + e);
                /*showSnackbarError(getView(), R.string.error_recreate_session, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginToChat(pos,user);
                            }
                        });*/
                loginToChat(pos, user);
            }
        });
    }

    private void chatStart(List<UserChatList.Data> data1, int position) {


        String receiverName = "", receiverImage = "", receiverId = "", receiverReceiveNotification = "";
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("dialog_id", qbChatDialog.getDialogId());
        intent.putExtra("newDialogCreate", "true");
        intent.putExtra("match_id", data1.get(position).getId());

        String finalDeleteBy="";

        if (my_user_id.equalsIgnoreCase(data1.get(position).getUser_info2().getId())) {
            receiverName = data1.get(position).getUser_info1().getName();
            receiverImage = data1.get(position).getUser_info1().getProfile_image();
            receiverId = data1.get(position).getUser_info1().getId();
            receiverReceiveNotification = data1.get(position).getUser_info1().getReceive_notification();
            finalDeleteBy=data1.get(position).getDeletedBy();
        } else if (my_user_id.equalsIgnoreCase(data1.get(position).getUser_info1().getId())) {
            receiverName = data1.get(position).getUser_info2().getName();
            receiverImage = data1.get(position).getUser_info2().getProfile_image();
            receiverId = data1.get(position).getUser_info2().getId();
            receiverReceiveNotification = data1.get(position).getUser_info2().getReceive_notification();
            finalDeleteBy=data1.get(position).getDeletedBy();
        }
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("receiverImage", receiverImage);
        intent.putExtra("receiverId", receiverId);

        intent.putExtra("from", "chat_list");
        intent.putExtra("restore_match_id", data1.get(position).getId());
        //intent.putExtra("isDeleted",data1.get(position).getIsDeleted()+"");
//        intent.putExtra("deletedBy", data1.get(position).getDeletedBy());
        intent.putExtra("deletedBy", finalDeleteBy);
        //intent.putExtra("isBlocked",data1.get(position).getIsBlocked()+"");
        intent.putExtra("blockedBy", data1.get(position).getBlockedBy());

        intent.putExtra("isReceiveNotification", receiverReceiveNotification);

        intent.putExtra("event_type", data1.get(position).getEvent_type());
        SharedPrefsHelper.getInstance().saveChatUser(qbChatDialog.getDialogId(), "true", receiverImage, receiverName, data1.get(position).getEvent_type(), data1.get(position).getId());
        startActivity(intent);

        //ChatActivity.startForResult(intent, getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE,qbChatDialog);
        Log.e("MY_USER_ID", my_user_id);

    }

    private void createChat(int position) {
        progressDialog.show();
        if (SharedPrefsHelper.getInstance().hasQbUser()) {
            restoreChatSession(position);

        } else {
            login(position);
//            Toast.makeText(getContext(), "login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        noDataFoundTV.setVisibility(View.GONE);

        registerQbChatListeners();
        pushBroadcastReceiver = new PushBroadcastReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(FcmConsts.ACTION_NEW_FCM_EVENT));
        loginRegisterProfilePresenter.hitGetMyMatches();
//        SharedPrefsHelper.getInstance().save("dialog_id", "cdfkfctouter");
        if (qbChatDialog!=null) {
            SharedPrefsHelper.getInstance().save("dialog_id", "" + qbChatDialog.getDialogId());
        }
        //loadDialogsFromQb(true);

    }

    @Override
    public void onPause() {
        super.onPause();

//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterQbChatListeners();
    }

    private void registerQbChatListeners() {

        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : (allDialogsMessagesListener = new AllDialogsMessageListener()));
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : (systemMessagesListener = new SystemMessagesListener()));
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    private void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with ResultCode: " + resultCode + " RequestCode: " + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra("dialog_id");

                    loadUpdatedDialog(dialogId);
                } else {
                    isProcessingResultInProgress = false;
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void loadUpdatedDialog(String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                QbDialogHolder.getInstance().addDialog(result);
                updateDialogsAdapter();
                isProcessingResultInProgress = false;
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
            }
        });
    }

    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true);
    }

    private void initUi() {

       /* ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(dialogsRV);*/

        /*swipeController = new SwipeController(false,getContext(),new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                qbDialogDelete = dialogsList.get(position);
                QBChatDialog qbChatDialog = dialogsList.get(position);
//        Log.d("DialogAdapter", "getDataList: "+dataList.get(pos).getQbChatDialog().getQb_dialog_id() +" dialogid "+ dialog.getDialogId());
                String reciver_user_id = "";

                for (UserChatList.Data item: filteredChatList) {
                    if(item.getQbChatDialog().getQb_dialog_id().equals(qbDialogDelete.getDialogId())){
                        deleteNeededMAtchId = item.getId();
                        if (!my_user_id.equalsIgnoreCase(item.getUser_info1().getId())){
                            reciver_user_id = item.getUser_info1().getId();
                        }else{
                            reciver_user_id = item.getUser_info2().getId();

                        }break;
                    }
                }

                String finalReciver_user_id = reciver_user_id;
                new AlertDialog.Builder(getContext())
                        .setTitle("Love Push")
                        .setMessage("Are you sure, you want to remove the chat?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), ""+deleteNeededDialogId+"  pos "+deleteNeededPostion, Toast.LENGTH_SHORT).show();
                                rootLAyout.setEnabled(false);
                                //loginRegisterProfilePresenter.hit_delete_match(deleteNeededMAtchId);
                                loginRegisterProfilePresenter.hit_delete_chat(deleteNeededMAtchId,"1", finalReciver_user_id);//1 - delete, 0 - restore

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onLeftClicked(int position) {
//
                showReportDialog(getActivity(),position);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(dialogsRV);

        dialogsRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
*/

        my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        my_qb_id = Integer.valueOf(String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING)));


        dialogsAdapter = new ChatDialogAdapter(my_user_id, my_qb_id, getActivity(), dialogsList, filteredChatList, (view, position, type, o) -> {
            List<UserChatList.Data> list = (List<UserChatList.Data>) o;
            if (type == 101) {
                boolean isBlocked=false;
                QBChatDialog selectedDialog = dialogsList.get(position);

                String receiverName = "", receiverImage = "", event_type = "", receiverId = "", receiverReceiveNotification = "",deleteBy2="",restoreMatchId="";
                for (UserChatList.Data item : filteredChatList) {
                    if (item.getQbChatDialog().getQb_dialog_id().equals(selectedDialog.getDialogId())) {

                        event_type = item.getEvent_type();
                        if (!my_user_id.equalsIgnoreCase(item.getUser_info1().getId())) {
                            receiverImage = item.getUser_info1().getProfile_image();
                            receiverName = item.getUser_info1().getName();
                            receiverId = item.getUser_info1().getId();
                            receiverReceiveNotification = item.getUser_info1().getReceive_notification();
                            isBlocked=item.getBlockedBy().equalsIgnoreCase(item.getUser_info1().getId());
                            deleteBy2=item.getDeletedBy();
                            restoreMatchId=item.getQbChatDialog().getMatch_id();

                        } else {
                            receiverImage = item.getUser_info2().getProfile_image();
                            receiverName = item.getUser_info2().getName();
                            receiverId = item.getUser_info2().getId();
                            receiverReceiveNotification = item.getUser_info2().getReceive_notification();
                            isBlocked=item.getBlockedBy().equalsIgnoreCase(item.getUser_info2().getId());
                            deleteBy2=item.getDeletedBy();
                            restoreMatchId=item.getQbChatDialog().getMatch_id();
                        }
                        break;
                    }
                }

                String finalReceiverImage = receiverImage;
                String finalReceiverName = receiverName;
                String finalEvent_type = event_type;
                String finalReceiverId = receiverId;
                String finalReceiverReceiveNotification = receiverReceiveNotification;
                String finalDeleteBy= ""+deleteBy2;
                String finalRestoreMatchId=""+restoreMatchId;



                if (ChatHelper.getInstance().isLogged()) {

                    boolean finalIsBlocked = isBlocked;
                    new Handler().postDelayed(() -> {
                        SharedPrefsHelper.getInstance().saveChatUser(selectedDialog.getDialogId(), "false", finalReceiverImage, finalReceiverName, finalEvent_type, "");
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("dialog_id", selectedDialog.getDialogId());
                        intent.putExtra("newDialogCreate", "false");
                        intent.putExtra("receiverImage", finalReceiverImage);
                        intent.putExtra("receiverName", finalReceiverName);
                        intent.putExtra("receiverId", finalReceiverId);
                        intent.putExtra("event_type", finalEvent_type);
                        intent.putExtra("match_id", "");
                        intent.putExtra("isBlocked", finalIsBlocked);
//                        intent.putExtra("restore_match_id", list.get(position).getId());
                        intent.putExtra("restore_match_id", finalRestoreMatchId);
                        intent.putExtra("from", "chat_list");
                        //intent.putExtra("isDeleted",list.get(position).getIsDeleted()+"");
//                        intent.putExtra("deletedBy", list.get(position).getDeletedBy());
                        intent.putExtra("deletedBy", finalDeleteBy);
                        //intent.putExtra("isBlocked",list.get(position).getIsBlocked()+"");
                        intent.putExtra("blockedBy", list.get(position).getBlockedBy());
                        intent.putExtra("isReceiveNotification", finalReceiverReceiveNotification);
                        startActivity(intent);
                    }, 1000);



                    //ChatActivity.startForResult(intent,getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
                } else {
                    //showProgressDialog(R.string.dlg_login);
                    progressDialog.show();
                    boolean finalIsBlocked1 = isBlocked;
                    ChatHelper.getInstance().loginToChat(ChatHelper.getCurrentUser(), new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid, Bundle bundle) {
                            hideProgressDialog();
                            progressDialog.dismiss();
                            SharedPrefsHelper.getInstance().saveChatUser(selectedDialog.getDialogId(), "false", finalReceiverImage, finalReceiverName, finalEvent_type, "");

                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("dialog_id", selectedDialog.getDialogId());
                            intent.putExtra("newDialogCreate", "false");
                            intent.putExtra("receiverImage", finalReceiverImage);
                            intent.putExtra("receiverName", finalReceiverName);
                            intent.putExtra("receiverId", finalReceiverId);
                            intent.putExtra("event_type", finalEvent_type);
                            intent.putExtra("match_id", "");
                            intent.putExtra("isBlocked", finalIsBlocked1);
//                            intent.putExtra("restore_match_id", list.get(position).getId());
                            intent.putExtra("restore_match_id", finalRestoreMatchId);
                            intent.putExtra("from", "chat_list");
                            // intent.putExtra("isDeleted",list.get(position).getIsDeleted()+"");
//                            intent.putExtra("deletedBy", list.get(position).getDeletedBy());
                            intent.putExtra("deletedBy", finalDeleteBy);
                            //intent.putExtra("isBlocked",list.get(position).getIsBlocked()+"");
                            intent.putExtra("blockedBy", list.get(position).getBlockedBy());
                            intent.putExtra("isReceiveNotification", finalReceiverReceiveNotification);
                            startActivity(intent);

                            // ChatActivity.startForResult(intent,getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            progressDialog.dismiss();
                            // ToastUtils.shortToast(R.string.login_chat_login_error);
                            //showErrorSnackbar(R.string.login_chat_login_error, e, null);
                        }
                    });
                }
            }

            else if(type==112)
            {
                alertDialog(position,list);
            }
            else if (type == 102) {
                showReportDialog(getActivity(), position);

            }
            else if (type == 103) {

                qbDialogDelete = dialogsList.get(position);
                String reciver_user_id = "";

                for (UserChatList.Data item : filteredChatList) {
                    if (item.getQbChatDialog().getQb_dialog_id().equals(qbDialogDelete.getDialogId())) {
                        deleteNeededMAtchId = item.getId();
                        if (!my_user_id.equalsIgnoreCase(item.getUser_info1().getId())) {
                            reciver_user_id = item.getUser_info1().getId();
                        } else {
                            reciver_user_id = item.getUser_info2().getId();

                        }
                        break;
                    }
                }

                //Toast.makeText(getContext(), ""+deleteNeededMAtchId, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.unmatch_sure_alert))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), ""+deleteNeededDialogId+"  pos "+deleteNeededPostion, Toast.LENGTH_SHORT).show();
                                //loginRegisterProfilePresenter.hit_delete_match(deleteNeededMAtchId);
                                dialogsList.remove(position);
                                dialogsAdapter.notifyDataSetChanged();
                                //for block user
                                //loginRegisterProfilePresenter.hit_block_user("block", finalReciver_user_id);//block, unblock
                                //for unmatch
                                loginRegisterProfilePresenter.hit_unmatch_user(deleteNeededMAtchId);//block, unblock

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
            else if (type == 104) {

                qbDialogDelete = dialogsList.get(position);
                String reciver_user_id = "";

                for (UserChatList.Data item : filteredChatList) {
                    if (item.getQbChatDialog().getQb_dialog_id().equals(qbDialogDelete.getDialogId())) {
                        deleteNeededMAtchId = item.getId();
                        if (!my_user_id.equalsIgnoreCase(item.getUser_info1().getId())) {
                            reciver_user_id = item.getUser_info1().getId();
                        } else {
                            reciver_user_id = item.getUser_info2().getId();

                        }
                        break;
                    }
                }

                String finalReciver_user_id = reciver_user_id;
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.delete_sure_alert))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), ""+deleteNeededDialogId+"  pos "+deleteNeededPostion, Toast.LENGTH_SHORT).show();
                                /*dialogsList.remove(position);
                                dialogsAdapter.notifyDataSetChanged();*/
                                rootLAyout.setEnabled(false);
                                //loginRegisterProfilePresenter.hit_delete_match(deleteNeededMAtchId);
                                loginRegisterProfilePresenter.hit_delete_chat(deleteNeededMAtchId, "1", finalReciver_user_id);//1 - delete, 0 - restore

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        dialogsRV.setLayoutManager(linearLayoutManager);
        dialogsRV.setAdapter(dialogsAdapter);



        requestBuilder = new QBRequestGetBuilder();

        setOnRefreshListener.setOnRefreshListener(direction -> {
            //Pagination
            //requestBuilder.setSkip(skipRecords += ChatHelper.DIALOG_ITEMS_PER_PAGE);

            loadDialogsFromQb(true);
        });
    }

    void showReportDialog(Activity activity, int position) {

        qbDialogDelete = dialogsList.get(position);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.report_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        AppCompatTextView nameTV = dialog.findViewById(R.id.nameTV);
        AppCompatEditText descriptionTV = dialog.findViewById(R.id.descriptionTV);
        CircleImageView img_profile = dialog.findViewById(R.id.img_profile);

        ImageView closeDialogImg = dialog.findViewById(R.id.closeDialog);
        String report_user_id = "";
        //QBChatDialog qbChatDialog = dialogsList.get(position);
//        Log.d("DialogAdapter", "getDataList: "+dataList.get(pos).getQbChatDialog().getQb_dialog_id() +" dialogid "+ dialog.getDialogId());

        for (UserChatList.Data item : filteredChatList) {
            if (item.getQbChatDialog().getQb_dialog_id().equals(qbDialogDelete.getDialogId())) {
                deleteNeededMAtchId = item.getId();
                if (!my_user_id.equalsIgnoreCase(item.getUser_info1().getId())) {
                    nameTV.setText(item.getUser_info1().getName());
                    report_user_id = item.getUser_info1().getId();

                    Picasso.get()
                            .load(item.getUser_info1().getProfile_image())
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(img_profile);

                    if (item.getEvent_type().equalsIgnoreCase("L")) {
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.colorAccent));
//            Blue
                    } else if (item.getEvent_type().equalsIgnoreCase("C")) {
//                red
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.blue));
                    } else {
//                    grey
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.grey));
                    }


                } else {
                    nameTV.setText(item.getUser_info2().getName());
                    report_user_id = item.getUser_info2().getId();
                    Picasso.get()
                            .load(item.getUser_info2().getProfile_image())
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(img_profile);

                    if (item.getEvent_type().equalsIgnoreCase("L")) {
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.colorAccent));
//            Blue
                    } else if (item.getEvent_type().equalsIgnoreCase("C")) {
//                red
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.blue));
                    } else {
//                    grey
                        img_profile.setBorderColor(getContext().getResources().getColor(R.color.grey));
                    }
                }
                break;
            }

        }


        closeDialogImg.setOnClickListener(v -> dialog.cancel());

        LinearLayout btnDialogSubmit = dialog.findViewById(R.id.btnDialogSubmit);
        String finalReport_user_id = report_user_id;
        btnDialogSubmit.setOnClickListener(v -> {

            if (descriptionTV.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Please enter some description", Toast.LENGTH_SHORT).show();
            } else {
                dialog.cancel();

                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.report_sure_alert))
                        .setPositiveButton(getString(R.string.yes), (dialog1, which) -> {
                            AlertDialogs dialogs=new AlertDialogs(getActivity());

                            dialogs.show();
                            loginRegisterProfilePresenter.hit_report_chat(finalReport_user_id, descriptionTV.getText().toString().trim(), new ResponseListner() {
                                @Override
                                public <T> void onComplete(Response<T> response) {
                                    dialogs.dismiss();
                                    if (response.isSuccessful())
                                    {
                                        CommonResponse res=(CommonResponse) response.body();
                                        if (res.getStatus()) {
                                            /*Change here*/
                                            deleteChat(res.getMessage());
//                                            dialogsList.remove(position);
                                            dialogsAdapter.notifyDataSetChanged();

                                            /*For message counter update*/


                                        } else {

                                            appUtils.showToast(res.getMessage());
                                        }
                                    }

                                }

                                @Override
                                public Void onError(String message) {
                                    dialogs.dismiss();
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    return null;
                                }
                            });

                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }

        });

        dialog.show();
    }
    private void deleteChat( String message) {
        ChatHelper.getInstance().deleteDialog(qbDialogDelete, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                loadDialogsFromQb(true);

//                loginRegisterProfilePresenter.hit_unmatch_user(deleteNeededMAtchId);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDialogsFromQb(final boolean silentUpdate) {
        isProcessingResultInProgress = true;
        if (!silentUpdate) {
            progressDialog.show();
            progressBar.setVisibility(View.VISIBLE);
        }
        String my_quickblox_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));

        ChatHelper.getInstance().getDialogs(getContext(), my_quickblox_id, filteredQbDialogList, requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                Log.e(TAG, "getAllDialogs " + dialogs);
                DialogJoinerAsyncTask dialogJoinerAsyncTask = new DialogJoinerAsyncTask(getContext(), dialogs, true);
                dialogJoinerAsyncTask.execute();
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                //ToastUtils.shortToast(e.getMessage());
            }
        });


    }

    private void disableProgress() {
        isProcessingResultInProgress = false;
        progressBar.setVisibility(View.GONE);
        progressDialog.dismiss();
        setOnRefreshListener.setRefreshing(false);
    }

    private void updateDialogsAdapter() {
        dialogsList = new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());
        dialogsAdapter.updateList(dialogsList, filteredChatList);

    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
        //updateDialogsAdapter();
        loginRegisterProfilePresenter.hitGetMyMatches();
    }

    private class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.e(TAG, "processMessage");
            boolean isRequestAccepted = false;
            if (isRequestAccepted) {
                Integer count = getPref().getMessageCount();
                count++;
                getPref().setMessageCount(count);

                setMessageCount();
            }
        }
    }

    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(FcmConsts.EXTRA_FCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            e.getCondition();
        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.d(TAG, "Processing received Message: " + qbChatMessage.getBody());
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
                String currentDialogID = sharedPrefsHelper.getSharedDialogId();
                if (!currentDialogID.equalsIgnoreCase(dialogId)) {
                    dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
                } else
                    Log.e("message issue", "solved");
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DialogJoinerAsyncTask extends BaseAsyncTask<Void, Void, Void> {

        private ArrayList<QBChatDialog> dialogs;
        private boolean clearDialogHolder;

        DialogJoinerAsyncTask(Context dialogsActivity, ArrayList<QBChatDialog> dialogs, boolean clearDialogHolder) {

            this.dialogs = dialogs;
            this.clearDialogHolder = clearDialogHolder;
        }

        @Override
        public Void performInBackground(Void... voids) throws Exception {
            ChatHelper.getInstance().join(dialogs);
            return null;
        }

        @Override
        public void onResult(Void aVoid) {
            disableProgress();
            if (clearDialogHolder) {
                QbDialogHolder.getInstance().clear();
            }
            QbDialogHolder.getInstance().addDialogs(dialogs);
            updateDialogsAdapter();
        }

        @Override
        public void onException(Exception e) {
            super.onException(e);
            Log.d(TAG, "Error: " + e);
        }
    }
}
