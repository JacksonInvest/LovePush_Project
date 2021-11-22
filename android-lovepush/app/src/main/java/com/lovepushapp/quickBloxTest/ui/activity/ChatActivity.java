package com.lovepushapp.quickBloxTest.ui.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.model.response.Chat.SaveDialogResponse;
import com.lovepushapp.model.response.deleteChatResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.managers.DialogsManager;
import com.lovepushapp.quickBloxTest.ui.adapter.AttachmentPreviewAdapter;
import com.lovepushapp.quickBloxTest.ui.adapter.ChatAdapter;
import com.lovepushapp.quickBloxTest.ui.adapter.listeners.AttachClickListener;
import com.lovepushapp.quickBloxTest.ui.dialog.ProgressDialogFragment;
import com.lovepushapp.quickBloxTest.ui.widget.AttachmentPreviewAdapterView;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SystemPermissionHelper;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.imagepick.ImagePickHelper;
import com.lovepushapp.quickBloxTest.utils.imagepick.OnImagePickedListener;
import com.lovepushapp.quickBloxTest.utils.qb.PaginationHistoryListener;
import com.lovepushapp.quickBloxTest.utils.qb.QbChatDialogMessageListenerImp;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogUtils;
import com.lovepushapp.quickBloxTest.utils.qb.VerboseQbChatConnectionListener;
import com.lovepushapp.quickBloxTest.utils.recorder.AudioRecorder;
import com.lovepushapp.quickBloxTest.utils.recorder.exceptions.MediaRecorderException;
import com.lovepushapp.quickBloxTest.utils.recorder.listeners.QBMediaRecordListener;
import com.lovepushapp.quickBloxTest.utils.recorder.view.QBRecordAudioButton;
import com.lovepushapp.videoTestJava.activities.CallActivity;
import com.lovepushapp.videoTestJava.activities.PermissionsActivity;
import com.lovepushapp.videoTestJava.services.CallService;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.utils.Consts;
import com.lovepushapp.videoTestJava.utils.PermissionsChecker;
import com.lovepushapp.videoTestJava.utils.PushNotificationSender;
import com.lovepushapp.videoTestJava.utils.WebRtcSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBEventType;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends BaseActivity implements OnImagePickedListener, QBMessageStatusListener, DialogsManager.ManagingDialogsCallbacks, LoginMvp {
    //public static final String EXTRA_DIALOG_ID;
    public static final String EXTRA_IS_NEW_DIALOG = "isNewDialog";
    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int MAX_UPLOAD_FILES = 2;
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;
    private static final String ORDER_RULE = "order";
    private static final String ORDER_VALUE = "desc string created_at";
    private static final int LIMIT_PER_PAGE = 100;
    private static final int REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS = 200;
    protected List<QBChatMessage> messagesList;
    View rootView;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    ImageView emojiButton, callIV, videoIV;
    EmojIconActions emojIcon;
    String intentNewDialogCreate, intentReceiverName, intentReceiverImage, intentReceiverId, intentMatchId, intentDialogId, intentEventType, intent;
    String my_shared_name, my_shared_image, my_qb_id, my_shared_id;
    String intentFrom, intentDeletedBy = "0", intentBlockedBy = "0", intentRestoreMatchId, isReceiveNofication = "1";
    boolean isDeleteRestored = false;
    boolean isBlocked = false;
    TextView txtBlocked;
    private ProgressBar progressBar;
    private EmojiconEditText messageEditText;
    private CircleImageView ReceiveruserIV;
    private LinearLayout attachmentPreviewContainerLayout;
    private ChatAdapter chatAdapter;
    private RecyclerView chatMessagesRecyclerView;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;
    private ImageAttachClickListener imageAttachClickListener;
    private QBMessageStatusesManager qbMessageStatusesManager;
    private DialogsManager dialogsManager;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    private boolean checkAdapterInit;
    private LinearLayout audioLayout;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private AudioRecorder audioRecorder;
    private QBRecordAudioButton recordButton;
    private TextView audioRecordTextView;
    private Chronometer recordChronometer;
    private Vibrator vibro;
    private ImageView bucketView, backIV;
    private TextView receiverName;
    private PermissionsChecker checker;

   /* public static void startForResult(Intent intent1, Activity activity, int code, QBChatDialog dialogId) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra("newDialogCreate",intent1.getStringExtra("newDialogCreate"));
        intent.putExtra("to_user_name", intent1.getStringExtra("to_user_name"));
        intent.putExtra("match_id",intent1.getStringExtra("match_id"));

        activity.startActivityForResult(intent, code);
    }*/

   /* public static void startForResult(Activity activity, int code, QBChatDialog dialogId, boolean isNewDialog) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatActivity.EXTRA_IS_NEW_DIALOG, isNewDialog);
        activity.startActivityForResult(intent, code);
    }*/

    public static void start(Context context) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        Intent intent = new Intent(context, ChatActivity.class);

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("dialog_id", sharedPrefsHelper.getSharedDialogId());
        intent.putExtra("newDialogCreate", sharedPrefsHelper.getSharedNewDialogCreate());
        intent.putExtra("receiverImage", sharedPrefsHelper.getSharedReceiverImage());
        intent.putExtra("receiverName", sharedPrefsHelper.getSharedReceiverName());
        intent.putExtra("event_type", sharedPrefsHelper.getSharedevent_type());
        intent.putExtra("match_id", sharedPrefsHelper.getSharedmatch_id());

        context.startActivity(intent);
        //Toast.makeText(context, "New Session", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat1);
        Log.v(TAG, "onCreate ChatActivity on Thread ID = " + Thread.currentThread().getId());

        /*if (!ChatHelper.getInstance().isLogged()) {
            Log.w(TAG, "Restarting App...");
            restartApp(this);
        }*/

        backIV = findViewById(R.id.backIV);
        audioLayout = findViewById(R.id.layout_chat_audio_container);
        recordButton = findViewById(R.id.button_chat_record_audio);
        recordChronometer = findViewById(R.id.chat_audio_record_chronometer);
        bucketView = findViewById(R.id.chat_audio_record_bucket_imageview);
        audioRecordTextView = findViewById(R.id.chat_audio_record_textview);
        rootView = findViewById(R.id.root_view);
        emojiButton = findViewById(R.id.emoji_btn);
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        receiverName = findViewById(R.id.screenTitleTV);
        messageEditText = findViewById(R.id.edit_chat_message);
        progressBar = findViewById(R.id.progress_chat);
        callIV = findViewById(R.id.callIV);
        videoIV = findViewById(R.id.videoIV);
        ReceiveruserIV = findViewById(R.id.userIV);
        txtBlocked = findViewById(R.id.txtBlocked);
        attachmentPreviewContainerLayout = findViewById(R.id.layout_attachment_preview_container);
        checker = new PermissionsChecker(getApplicationContext());
        dialogsManager = new DialogsManager();
        dialogsManager.addManagingDialogsCallbackListener(this);
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
        systemMessagesListener = new SystemMessagesListener();
        intentDialogId = getIntent().getStringExtra("dialog_id");
        qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(getIntent().getStringExtra("dialog_id"));
        intentNewDialogCreate = getIntent().getStringExtra("newDialogCreate");
        intentReceiverImage = getIntent().getStringExtra("receiverImage");
        intentReceiverName = getIntent().getStringExtra("receiverName");
        intentReceiverId = getIntent().getStringExtra("receiverId");
        intentEventType = getIntent().getStringExtra("event_type");
        intentMatchId = getIntent().getStringExtra("match_id");
        intentFrom = getIntent().getStringExtra("from");
        isBlocked = getIntent().getBooleanExtra("isBlocked", false);


        if (!intentFrom.equalsIgnoreCase("push_notification")) {
            // intentIsDeleted= getIntent().getStringExtra("isDeleted");
            intentDeletedBy = getIntent().getStringExtra("deletedBy");
            // intentIsBlocked= getIntent().getStringExtra("isBlocked");
            intentBlockedBy = getIntent().getStringExtra("blockedBy");
            intentRestoreMatchId = getIntent().getStringExtra("restore_match_id");
            isReceiveNofication = getIntent().getStringExtra("isReceiveNotification");
        }
        Log.v(TAG, "Deserialized dialog = " + qbChatDialog + "  " + intentDialogId);

        qbChatDialog.initForChat(QBChatService.getInstance());
        chatMessageListener = new ChatMessageListener();
        qbChatDialog.addMessageListener(chatMessageListener);
        my_shared_name = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        my_shared_image = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        my_qb_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));
        my_shared_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        initEmoji();
        setActionBar();
        initViews();
        initMessagesRecyclerView();
        initChatConnectionListener();
        initChat();


        requestPermission();

        initAudioRecorder();
        recordButton.setRecordTouchListener(new RecordTouchListenerImpl());

        backIV.setOnClickListener(v -> {
            onBackPressed();
        });

        callIV.setOnClickListener(v -> {
            //Toast.makeText(ChatActivity.this, "Call", Toast.LENGTH_SHORT).show();
            /*if (isLoggedInChat()) {
                startCall(false);
            }
            if (checkPermission1(PERMISSIONS[1])) {
                startPermissionsActivity(true);
            }*/

            if (checkIsLoggedInChat()) {
                startCall(false);
            }
            if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                startPermissionsActivity(true);
            }
        });
        videoIV.setOnClickListener(v -> {
            //Toast.makeText(ChatActivity.this, "video", Toast.LENGTH_SHORT).show();
            if (checkIsLoggedInChat()) {
                startCall(true);
            }
            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                startPermissionsActivity(false);
            }
        });

        ReceiveruserIV.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(ChatActivity.this, ProfileActivity.class)).putExtra("user_id", intentReceiverId).putExtra("from", "chat"));

        });
        receiverName.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(ChatActivity.this, ProfileActivity.class)).putExtra("user_id", intentReceiverId).putExtra("from", "chat"));

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat1;

    }

    @Override
    public void manageToolBar() {

    }


    private boolean checkIsLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            startLoginService();
            ToastUtils.shortToast(R.string.dlg_relogin_wait);
            return false;
        }
        return true;
    }

    private void startLoginService() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            LoginService.start(this, qbUser);
        }
    }

    private void startCall(boolean isVideoCall) {
        String callType = "";
        String call_type = "";
        if (isVideoCall) {
            callType = "video call";
            call_type = "video";
        } else {
            callType = "audio call";
            call_type = "audio";

        }
        String notification_title = my_shared_name + " calling you";
//        sendPushMessage(opponentsList, qbChatDialog.getName());
        sendCallPushNotification(notification_title, callType, call_type, isVideoCall);


//        sendCallPushNotificationIOS(notification_title,callType,call_type,isVideoCall);

    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);

    }

    public boolean checkPermission1(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(ChatActivity.this, permission) == PackageManager.PERMISSION_DENIED;
    }

    private boolean isLoggedInChat() {

        if (!QBChatService.getInstance().isLoggedIn()) {
            ToastUtils.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();

        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            // LoginService.start(this, SharedPrefsHelper.getQbUser())

        }
    }

// computer is electronic device

    /*private void startCall(boolean isVideoCall) {

        Log.e(TAG, "startCall()");
        //ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        *//*List<Integer> opponentsList = qbChatDialog.getOccupants();
        opponentsList.remove(SharedPrefsHelper.getInstance().getQbUser().getId());*//*
        List<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(qbChatDialog.getRecipientId());

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.INSTANCE.setCurrentSession(newQbRtcSession);
        String notification_title = my_shared_name+" Calling you";

        String callType = "";
        if (isVideoCall){
            callType = "video call";
        }else {
            callType = "audio call";

        }

        //sendPushMessage(opponentsList, qbChatDialog.getName());
        sendCallPushNotification(notification_title,callType);
        CallActivity.Companion.start(this, false);
        Log.e(TAG, "conferenceType = " + conferenceType+" "+opponentsList.get(0).toString());
    }*/
    private void initEmoji() {
        emojIcon = new EmojIconActions(this, rootView, messageEditText, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (qbChatDialog != null) {
            outState.putString(intentDialogId, qbChatDialog.getDialogId());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(intentDialogId));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        chatAdapter.setAttachImageClickListener(imageAttachClickListener);
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
        qbMessageStatusesManager = QBChatService.getInstance().getMessageStatusesManager();
        qbMessageStatusesManager.addMessageStatusListener(this);


        boolean isIncomingCall = SharedPrefsHelper.getInstance().get(Consts.EXTRA_IS_INCOMING_CALL, false);
        if (isCallServiceRunning(CallService.class)) {
            Log.d(TAG, "CallService is running now");
            CallActivity.start(this, isIncomingCall);
        }
        clearAppNotifications();

    }

    private boolean isCallServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void clearAppNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatAdapter.removeAttachImageClickListener();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
        qbMessageStatusesManager.removeMessageStatusListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }
        qbChatDialog.removeMessageListrener(chatMessageListener);
        dialogsManager.removeManagingDialogsCallbackListener(this);
//        sharedPrefsHelper.save("dialog_id", "cdfkfctouter");
        sharedPrefsHelper.save("dialog_id", ""+qbChatDialog.getDialogId());
        SharedPrefsHelper.getInstance().removeSharedDialogId();
    }

    @Override
    public void onBackPressed() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        sendDialogId();
        super.onBackPressed();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);

        MenuItem menuItemLeave = menu.findItem(R.id.menu_chat_action_leave);
        MenuItem menuItemAdd = menu.findItem(R.id.menu_chat_action_add);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_chat_action_delete);

        if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
            menuItemLeave.setVisible(false);
            menuItemAdd.setVisible(false);
        } else {
            menuItemDelete.setVisible(false);
        }

        if (qbChatDialog.getType() != QBDialogType.GROUP) {
            menu.findItem(R.id.menu_chat_action_add).setVisible(false);
        }

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDialog() {
        ProgressDialogFragment.show(getSupportFragmentManager());
        Log.d(TAG, "Update Dialog");
        ChatHelper.getInstance().getDialogById(qbChatDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog updatedChatDialog, Bundle bundle) {
                Log.d(TAG, "Update Dialog Successful: " + updatedChatDialog.getDialogId());
                qbChatDialog = updatedChatDialog;
                loadUsersFromQb(updatedChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Update Dialog Error: " + e.getMessage());
                ProgressDialogFragment.hide(getSupportFragmentManager());
                showErrorSnackbar(R.string.select_users_get_dialog_error, e, null);
            }
        });
    }

    private void loadUsersFromQb(final QBChatDialog qbChatDialog) {
        ArrayList<GenericQueryRule> rules = new ArrayList<>();
        rules.add(new GenericQueryRule(ORDER_RULE, ORDER_VALUE));

        QBPagedRequestBuilder qbPagedRequestBuilder = new QBPagedRequestBuilder();
        qbPagedRequestBuilder.setRules(rules);
        qbPagedRequestBuilder.setPerPage(LIMIT_PER_PAGE);

        Log.d(TAG, "Loading Users");
        QBUsers.getUsers(qbPagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                Log.d(TAG, "Loading Users Successful");
                ProgressDialogFragment.hide(getSupportFragmentManager());
                if (qbChatDialog.getOccupants().size() >= users.size()) {
                    ToastUtils.shortToast(R.string.added_users);
                } else {
                    //SelectUsersActivity.startForResult(ChatActivity.this, REQUEST_CODE_SELECT_PEOPLE, qbChatDialog);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Loading Users Error: " + e.getMessage());
                ProgressDialogFragment.hide(getSupportFragmentManager());
                showErrorSnackbar(R.string.select_users_get_users_error, e, null);
            }
        });
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(intentDialogId, qbChatDialog.getDialogId());
        setResult(RESULT_OK, result);
    }

    private void leaveGroupChat() {
        ProgressDialogFragment.show(getSupportFragmentManager());
        dialogsManager.sendMessageLeftUser(qbChatDialog);
        dialogsManager.sendSystemMessageLeftUser(systemMessagesManager, qbChatDialog);
        Log.d(TAG, "Leaving Dialog");
        ChatHelper.getInstance().exitFromDialog(qbChatDialog, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbDialog, Bundle bundle) {
                Log.d(TAG, "Leaving Dialog Successful: " + qbDialog.getDialogId());
                ProgressDialogFragment.hide(getSupportFragmentManager());
                QbDialogHolder.getInstance().deleteDialog(qbDialog);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Leaving Dialog Error: " + e.getMessage());
                ProgressDialogFragment.hide(getSupportFragmentManager());
                showErrorSnackbar(R.string.error_leave_chat, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaveGroupChat();
                    }
                });
            }
        });
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with resultCode: " + resultCode + " requestCode: " + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PEOPLE) {
                progressBar.setVisibility(View.VISIBLE);
               /* final ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(
                        SelectUsersActivity.EXTRA_QB_USERS);
                List<Integer> existingOccupantsIds = qbChatDialog.getOccupants();
                final List<Integer> newUsersIds = new ArrayList<>();

                for (QBUser user : selectedUsers) {
                    if (!existingOccupantsIds.contains(user.getId())) {
                        newUsersIds.add(user.getId());
                    }
                }

                ChatHelper.getInstance().getDialogById(qbChatDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        progressBar.setVisibility(View.GONE);
                        dialogsManager.sendMessageAddedUsers(qbChatDialog, newUsersIds);
                        dialogsManager.sendSystemMessageAddedUser(systemMessagesManager, qbChatDialog, newUsersIds);
                        ChatActivity.this.qbChatDialog = qbChatDialog;
                        updateDialog(selectedUsers);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        progressBar.setVisibility(View.GONE);
                        showErrorSnackbar(R.string.update_dialog_error, e, null);
                    }
                });*/
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SystemPermissionHelper.PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST && grantResults[0] != -1) {
            openImagePicker();
        }
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                Uri contentURI = Uri.fromFile(file);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ChatActivity.this.getContentResolver(), contentURI);
                    File pathFile = saveAndCompress(bitmap);
                    attachmentPreviewAdapter.add(new File(contentURI.getPath()));
                    //attachmentPreviewAdapter.add(pathFile);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public File saveAndCompress(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       // myBitmap.compress(Bitmap.CompressFormat.JPEG, 87, bytes);

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        File f = null;
        try {
            f = new File(wallpaperDirectory, "img_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return new File(f.getAbsolutePath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return f;
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        showErrorSnackbar(0, e, null);
    }

    @Override
    public void onImagePickClosed(int ignored) {

    }

    public void onSendChatClick(View view) {
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment, "image");
                }
            } else {
                ToastUtils.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null, "text");
        }


    }

    public void onAttachmentsClick(View view) {
        if (attachmentPreviewAdapter.getCount() >= MAX_UPLOAD_FILES) {
            ToastUtils.shortToast(getString(R.string.restriction_upload_files, MAX_UPLOAD_FILES));
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        SystemPermissionHelper permissionHelper = new SystemPermissionHelper(this);
        if (permissionHelper.isSaveImagePermissionGranted()) {
            new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
        } else {
            permissionHelper.requestPermissionsForSaveFileImage();
        }
    }

    public void showMessage(QBChatMessage message) {
        if (isAdapterConnected()) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            delayShowMessage(message);
        }
    }

    private boolean isAdapterConnected() {
        return checkAdapterInit;
    }

    private void delayShowMessage(QBChatMessage message) {
        if (unShownMessages == null) {
            unShownMessages = new ArrayList<>();
        }
        unShownMessages.add(message);
    }

    private void initViews() {
        // actionBar.setDisplayHomeAsUpEnabled(true);

        attachmentPreviewAdapter = new AttachmentPreviewAdapter(this,
                count -> attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE),
                e -> showErrorSnackbar(0, e, v -> onAttachmentsClick(v)));
        AttachmentPreviewAdapterView previewAdapterView = findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);
        if (isBlocked)
            txtBlocked.setVisibility(View.VISIBLE);
        else
            txtBlocked.setVisibility(View.GONE);
    }

    private void initMessagesRecyclerView() {
        chatMessagesRecyclerView = findViewById(R.id.list_chat_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatMessagesRecyclerView.setLayoutManager(layoutManager);

        messagesList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, qbChatDialog, messagesList);
        chatAdapter.setPaginationHistoryListener(new PaginationListener());
        chatMessagesRecyclerView.addItemDecoration(
                new StickyRecyclerHeadersDecoration(chatAdapter));

        chatMessagesRecyclerView.setAdapter(chatAdapter);
        imageAttachClickListener = new ImageAttachClickListener();
    }

    private void sendChatMessage(final String text, final QBAttachment attachment, String type) {
        if (ChatHelper.getInstance().isLogged()) {
            QBChatMessage chatMessage = new QBChatMessage();
            if (attachment != null) {
                chatMessage.addAttachment(attachment);
                chatMessage.setBody("Media");
            } else {
                chatMessage.setBody(text);
            }
            chatMessage.setSaveToHistory(true);
            chatMessage.setDateSent(System.currentTimeMillis() / 1000);
            chatMessage.setMarkable(true);

            if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
                ToastUtils.shortToast(R.string.chat_still_joining);
                return;
            }

            try {
                if (qbChatDialog.getLastMessage() == null) {
                    CreateDialogInOurServer();
                }
                Log.d(TAG, "Sending Message with ID: " + chatMessage.getId());
                qbChatDialog.sendMessage(chatMessage);

                if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                    showMessage(chatMessage);
                    String notification_title = my_shared_name + " sent message";

                    // sendAndroidPushNotification(notification_title);
                    /*if (!intentDeletedBy.equalsIgnoreCase("0")){

                        String deletedUserID = (intentDeletedBy.equals(my_shared_id)) ? intentDeletedBy : my_shared_id;

                        if (!isDeleteRestored){
                            LoginRegisterProfilePresenter loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(ChatActivity.this);
                            loginRegisterProfilePresenter.attachView(this);

                            loginRegisterProfilePresenter.hit_delete_chat(intentRestoreMatchId,"0", deletedUserID);//1 - delete, 0 - restore
                            if (intentBlockedBy.equalsIgnoreCase("0")){
                                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                                sendMsgPushNotification(notification_title);
                            }

                        }

                    }else if (intentBlockedBy.equalsIgnoreCase("0") || intentDeletedBy.equalsIgnoreCase("0")){
                        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                        sendMsgPushNotification(notification_title);
                    }*/

                    if (!intentFrom.equalsIgnoreCase("push_notification")) {

                        if (!intentDeletedBy.equalsIgnoreCase("0")) {

                            String deletedUserID = (intentDeletedBy.equals(my_shared_id)) ? intentReceiverId : my_shared_id;

                            if (!isDeleteRestored) {
                                LoginRegisterProfilePresenter loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(ChatActivity.this);
                                loginRegisterProfilePresenter.attachView(this);

                                loginRegisterProfilePresenter.hit_delete_chat(intentRestoreMatchId, "0", deletedUserID);//1 - delete, 0 - restore
                                if (intentBlockedBy.equalsIgnoreCase("0")) {
                                    //  Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                                    sendMsgPushNotification(notification_title);
                                }

                            }

                        } else if (intentBlockedBy.equalsIgnoreCase("0") && intentDeletedBy.equalsIgnoreCase("0")) {
                            // Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            sendMsgPushNotification(notification_title);
                        }

                        /*Subhash added here*/
                        else
                        {
                            sendMsgPushNotification(notification_title);
                        }
                    }

                    else {
                        sendMsgPushNotification(notification_title);
                    }
                }

                if (attachment != null) {
                    if (type.equals("image")) {
                        attachmentPreviewAdapter.remove(attachment);
                    }
                } else {
                    messageEditText.setText("");
                }
            } catch (SmackException.NotConnectedException e) {
                Log.w(TAG, e);
                ToastUtils.shortToast(R.string.chat_error_send_message);
            }


        } else {
            showProgressDialog(R.string.dlg_login);
            Log.d(TAG, "Relogin to Chat");
            ChatHelper.getInstance().loginToChat(ChatHelper.getCurrentUser(), new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Log.d(TAG, "Relogin Successfull");
                    sendChatMessage(text, attachment, "");
                    hideProgressDialog();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "Relogin Error: " + e.getMessage());
                    hideProgressDialog();
                    ToastUtils.shortToast(R.string.chat_error_send_message);
                }
            });
        }
    }


    private void sendMsgPushNotification(String message) {


        if (isReceiveNofication.equals("1")) {
            StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
            userIds.add(qbChatDialog.getRecipientId());

            QBEvent event = new QBEvent();
            event.setUserIds(userIds);
            event.setEnvironment(QBEnvironment.PRODUCTION);
//            event.setEnvironment(QBEnvironment.DEVELOPMENT);
            event.setNotificationType(QBNotificationType.PUSH);
//            event.setPushType(QBPushType.APNS);
            event.setPushType(QBPushType.GCM);
            event.setType(QBEventType.ONE_SHOT);

            JSONObject data = new JSONObject();
            try {
                data.put("message", message);
                data.put("alert", "1");
                data.put("ios_alert", "1");
                data.put("user_qb_id", my_qb_id);
                data.put("dialog_id", qbChatDialog.getDialogId());
                data.put("user_id", my_shared_id);
                data.put("user_name", my_shared_name);
                data.put("notify_type", "chat");
                data.put("chat_status", "1");
                data.put("event_type", intentEventType);
                data.put("user_image", my_shared_image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.setMessage(data.toString());
            QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
                @Override
                public void onSuccess(QBEvent qbEvent, Bundle args) {
                    // sent
                    Log.e(TAG, "PushNotification " + " Success");

                }

                @Override
                public void onError(QBResponseException errors) {
                    Log.e(TAG, "PushNotification " + " error");
                    Log.e("NOTIFICATION_ERROR", String.valueOf(errors));
                    //Toast.makeText(ChatActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void sendCallPushNotification(String message, String callType, String call_type, boolean isVideoCall) {

        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(qbChatDialog.getRecipientId());

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.PRODUCTION);
//        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);
        event.setPushType(QBPushType.GCM);
//        event.setPushType(QBPushType.APNS_VOIP);
        event.setType(QBEventType.ONE_SHOT);
        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
            data.put("ios_voip", "1");
            data.put("VOIPCall", "1");
            data.put("notify_type", "call");
            data.put("call_type",callType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setMessage(data.toString());

        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle args) {
                // sent
                Log.e(TAG, "PushNotificationAndroid " + " Success");
                new Handler().postDelayed(() -> makecall(call_type, isVideoCall), 2000);
                // sent
            }

            @Override
            public void onError(QBResponseException errors) {

                Log.e(TAG, "PushNotificationAndroid " + " Failed"+"  ");
                Log.e(TAG,"CALL_NOTIFICATION_ERROR",errors);
                //  Toast.makeText(ChatActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> makecall(call_type, isVideoCall), 2000);
            }
        });

    }
    private void sendCallPushNotificationIOS(String message, String callType, String call_type, boolean isVideoCall) {

        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(qbChatDialog.getRecipientId());

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
        event.setEnvironment(QBEnvironment.PRODUCTION);
//        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);
//        event.setPushType(QBPushType.GCM);
        event.setPushType(QBPushType.APNS_VOIP);
        event.setType(QBEventType.ONE_SHOT);
        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
            data.put("ios_voip", "1");
            data.put("VOIPCall", "1");
            data.put("notify_type", "call");
            data.put("call_type",callType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setMessage(data.toString());

        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle args) {
                // sent
                Log.e(TAG, "PushNotificationIOS " + " Success");
                new Handler().postDelayed(() -> makecall(call_type, isVideoCall), 2000);
                // sent
            }

            @Override
            public void onError(QBResponseException errors) {

                Log.e(TAG, "PushNotificationIOS " + " Failed"+"  ");
                Log.e(TAG,"CALL_NOTIFICATION_ERROR",errors);
                //  Toast.makeText(ChatActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> makecall(call_type, isVideoCall), 2000);
            }
        });

    }


    private void makecall(String call_type, boolean isVideoCall) {

        List<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(qbChatDialog.getRecipientId());

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
        Log.d(TAG, "conferenceType = " + conferenceType);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", my_qb_id);
        userInfo.put("call_type", call_type);
        userInfo.put("caller_name", my_shared_name);
        userInfo.put("user_name", intentReceiverName);
        userInfo.put("caller_image", intentReceiverImage);
        Log.e(TAG, "session " + userInfo.values().toString());
        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
        qbrtcClient.prepareToProcessCalls();
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);


//        subhash added here
//        PushNotificationSender.sendPushMessage((ArrayList<Integer>) opponentsList, "testing");

        newQbRtcSession.startCall(userInfo);


        CallActivity.start(this, false);

                PushNotificationSender.sendPushMessage((ArrayList<Integer>) opponentsList, "testing");


    }

    private void CreateDialogInOurServer() {
        //Toast.makeText(this, "blank", Toast.LENGTH_SHORT).show();
        if (intentMatchId != null && !intentMatchId.equalsIgnoreCase("")) {
            LoginRegisterProfilePresenter loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(ChatActivity.this);
            loginRegisterProfilePresenter.attachView(ChatActivity.this);
            loginRegisterProfilePresenter.hit_create_dialog_chat(intentMatchId, qbChatDialog.getDialogId());
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:

                ToastUtils.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));

                finish();

                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                Log.d(TAG, "Joined to Dialog Successful");
                notifyUsersAboutCreatingDialog();
                hideProgressDialog();
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Joining Dialog Error:" + e.getMessage());
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void notifyUsersAboutCreatingDialog() {
        if (getIntent().getBooleanExtra(EXTRA_IS_NEW_DIALOG, false)) {
            dialogsManager.sendMessageCreatedDialog(qbChatDialog);
            getIntent().removeExtra(EXTRA_IS_NEW_DIALOG);
        }
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void updateDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbChatDialog, selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        loadDialogUsers();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        showErrorSnackbar(R.string.chat_info_add_people_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialog(selectedUsers);
                                    }
                                });
                    }
                }
        );
    }

    private void loadDialogUsers() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                //setActionBar();
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                showErrorSnackbar(R.string.chat_load_users_error, e,
                        v -> loadDialogUsers());
            }
        });
    }

    private void setActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        if (intentReceiverName.equalsIgnoreCase("")) {
            receiverName.setText(chatName);
        } else {
            receiverName.setText(intentReceiverName);
        }

        if (!intentReceiverImage.equalsIgnoreCase("")) {

            Picasso.get()
                    .load(intentReceiverImage)
                    .placeholder(R.drawable.profile_bg)
                    .error(R.drawable.profile_bg)
                    .into(ReceiveruserIV);

            if (intentEventType.equalsIgnoreCase("L")) {
                ReceiveruserIV.setBorderColor(getResources().getColor(R.color.colorAccent));
//            Blue
            } else if (intentEventType.equalsIgnoreCase("C")) {
//                red
                ReceiveruserIV.setBorderColor(getResources().getColor(R.color.blue));
            } else if (intentEventType.equalsIgnoreCase("")) {
//                    grey
                ReceiveruserIV.setBorderColor(getResources().getColor(R.color.grey));
            }

        }
    }

    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (!checkAdapterInit) {
                    checkAdapterInit = true;
                    chatAdapter.addList(messages);
                    addDelayedMessagesToAdapter();
                } else {
                    chatAdapter.addToList(messages);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Loading Dialog History Error: " + e.getMessage());
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void addDelayedMessagesToAdapter() {
        if (unShownMessages != null && !unShownMessages.isEmpty()) {
            List<QBChatMessage> chatList = chatAdapter.getList();
            for (QBChatMessage message : unShownMessages) {
                if (!chatList.contains(message)) {
                    chatAdapter.add(message);
                }
            }
        }
    }

    private void scrollMessageListDown() {
        chatMessagesRecyclerView.scrollToPosition(messagesList.size() - 1);
    }



    private void initChatConnectionListener() {
        View rootView = findViewById(R.id.list_chat_messages);
        chatConnectionListener = new VerboseQbChatConnectionListener(rootView) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                Log.d(TAG, "Reconnection Successful");
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        checkAdapterInit = false;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    @Override
    public void processMessageDelivered(String messageID, String dialogID, Integer userID) {
        if (qbChatDialog.getDialogId().equals(dialogID)) {
            chatAdapter.updateStatusDelivered(messageID, userID);
        }
    }

    @Override
    public void processMessageRead(String messageID, String dialogID, Integer userID) {
        if (qbChatDialog.getDialogId().equals(dialogID)) {
            chatAdapter.updateStatusRead(messageID, userID);
        }
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {

    }

    @Override
    public void onDialogUpdated(String chatDialog) {

    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {

    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof SaveDialogResponse) {
            SaveDialogResponse res = (SaveDialogResponse) test;

            if (res.getStatus()) {

                //Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(this, "not save", Toast.LENGTH_SHORT).show();

            }

        }

        if (test instanceof deleteChatResponse) {
            deleteChatResponse res = (deleteChatResponse) test;
            if (res.getStatus()) {
                isDeleteRestored = true;
            }
        }
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    public boolean requestPermission() {


        if (ContextCompat.checkSelfPermission(this,permissions[2])==PackageManager.PERMISSION_GRANTED)
        {

        } else{

            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }

        if(ContextCompat.checkSelfPermission(this,permissions[0])==PackageManager.PERMISSION_GRANTED){
            return true;

        }else{
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }
        if(ContextCompat.checkSelfPermission(this,permissions[1])==PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }

        return false;
    }

    public void initAudioRecorder() {
        audioRecorder = AudioRecorder.newBuilder()
                // Required
                .useInBuildFilePathGenerator(this)
                .setDuration(1500)
                .build();
        // Optional
//                .setDuration(10)
//                .setAudioSource(MediaRecorder.AudioSource.MIC)
//                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                .setAudioSamplingRate(44100)
//                .setAudioChannels(CHANNEL_STEREO)
//                .setAudioEncodingBitRate(96000)
        audioRecorder.setMediaRecordListener(new QBMediaRecordListenerImpl());
    }

    public void startRecord() {

        Log.d(TAG, "startRecord");
        recordChronometer.setBase(SystemClock.elapsedRealtime());
        recordChronometer.start();
        recordChronometer.setVisibility(View.VISIBLE);
        audioRecordTextView.setVisibility(View.VISIBLE);
        vibro.vibrate(100);
        audioLayout.setVisibility(View.VISIBLE);
        audioRecorder.startRecord();
    }

    public void stopRecord() {
        Log.d(TAG, "stopRecord");
        recordChronometer.stop();
        vibro.vibrate(100);
        audioLayout.setVisibility(View.INVISIBLE);
        audioRecorder.stopRecord();
    }

    public void processSendMessage(File file) {
        //Toast.makeText(this, "Audio recorded! " + file.getPath(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "processSendMessage file= " + file);
        // get file int fileId = R.raw.sample_file;

        Uri uri = Uri.parse(String.valueOf(file));
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(getApplicationContext(), uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int durationMillSecond = Integer.parseInt(durationStr);


        boolean isPublic = false;
        ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        QBContent.uploadFileTask(file, isPublic, null, new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgress(progress);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait... (" + progress + "%)");
                Log.e("getProgress", "" + progress);


            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {

                progressDialog.dismiss();
                QBAttachment attachment = new QBAttachment(QBAttachment.AUDIO_TYPE);
                attachment.setId(qbFile.getUid());
                attachment.setSize(qbFile.getSize());
                attachment.setName(qbFile.getName());
                attachment.setDuration(durationMillSecond);
                attachment.setContentType(qbFile.getContentType());

                //fileQBAttachmentMap.put(qbFile, result);

                sendChatMessage(null, attachment, "audio");


            }

            @Override
            public void onError(QBResponseException error) {
                Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void cancelRecord() {
        Log.d(TAG, "cancelRecord");
        hideRecordView();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        bucketView.startAnimation(shake);
        vibro.vibrate(100);
        audioLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                audioLayout.setVisibility(View.INVISIBLE);
            }
        }, 1500);

        audioRecorder.cancelRecord();
    }

    /*@OnClick(R.id.backIV)
    void onClickBack() {
        onBackPressed();
    }*/

    public void clearRecorder() {
        hideRecordView();
        audioRecorder.releaseMediaRecorder();
    }

    private void hideRecordView() {
        recordChronometer.stop();
        recordChronometer.setVisibility(View.INVISIBLE);
        audioRecordTextView.setVisibility(View.INVISIBLE);
    }

    private class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.d(TAG, "Processing Received Message: " + qbChatMessage.getBody());
            showMessage(qbChatMessage);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Message Received: " + qbChatMessage.getId());
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Messages Error: " + e.getMessage() + "With MessageID: " + qbChatMessage.getId());
        }
    }

    private class ImageAttachClickListener implements AttachClickListener {

        @Override
        public void onLinkClicked(QBAttachment qbAttachment, int position) {
            if (qbAttachment != null) {
                String url = QBFile.getPrivateUrlForUID(qbAttachment.getId());
                AttachmentImageActivity.start(ChatActivity.this, url);
            }
        }
    }

    private class PaginationListener implements PaginationHistoryListener {

        @Override
        public void downloadMore() {
            Log.w(TAG, "Download More");
            loadChatHistory();
        }
    }

    private class QBMediaRecordListenerImpl implements QBMediaRecordListener {

        @Override
        public void onMediaRecorded(File file) {
            audioLayout.setVisibility(View.INVISIBLE);
            processSendMessage(file);
        }

        @Override
        public void onMediaRecordError(MediaRecorderException e) {
            Log.d(TAG, "onMediaRecordError e= " + e.getMessage());
            clearRecorder();
        }

        @Override
        public void onMediaRecordClosed() {
            Toast.makeText(ChatActivity.this, "Audio is not recorded", Toast.LENGTH_LONG).show();
        }
    }

    private class RecordTouchListenerImpl implements QBRecordAudioButton.RecordTouchEventListener {

        @Override
        public void onStartClick(View view) {

            if(requestPermission()){
                startRecord();
            }
            else {
                requestPermission();
            }
        }

        @Override
        public void onCancelClick(View view) {
            cancelRecord();
        }

        @Override
        public void onStopClick(View view) {
            stopRecord();
        }
    }
}