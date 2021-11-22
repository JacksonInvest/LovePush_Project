package com.lovepushapp.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.HomeFragment;
import com.lovepushapp.fragments.MenuFragment;

import com.lovepushapp.fragments.MessageFragment1;
import com.lovepushapp.fragments.NotificationFragment;
import com.lovepushapp.helper.GPSTracker;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.model.response.NotificationResponse;
import com.lovepushapp.model.response.updateUserLocationResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.utils.ErrorUtils;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.videoTestJava.services.CallService;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.utils.Consts;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.model.QBUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class DashboardActivity extends BaseActivity implements LoginMvp {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    @BindView(R.id.homeIV)
    ImageView homeIV;
    @BindView(R.id.notificataionIV)
    ImageView notificataionIV;
    @BindView(R.id.messagesIV)
    ImageView messagesIV;
    @BindView(R.id.profileIV)
    ImageView profileIV;
    @BindView(R.id.menuIV)
    ImageView menuIV;
    LocationManager locationManager = null;
    Context context = DashboardActivity.this;

    @BindView(R.id.total_noti_count)
    TextView total_noti_count;

    //removing this because this feature not needed any-more {find-total_msg_countTV}.
    /*static TextView total_msg_countTV;*/
    String activeFragment = "profile";
    LoginRegisterProfilePresenter loginRegisterProfilePresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_dashboard;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    public void manageToolBar() {

    }


    public static Intent createIntent(Context context) {
        return new Intent(context, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //removing this because this feature not needed any-more {find-total_msg_countTV}.
        // total_msg_countTV=findViewById(R.id.total_msg_count);


        if (savedInstanceState == null) {
            loadScreens(4);
        }

        if (!SharedPrefsHelper.getInstance().hasQbUser()) {
            firstTimeLogin();
        } else {
            if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
                loadDialogsFromQb(true);
            } else {
                loadDialogsFromQb(false);
            }
        }
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

        if (CheckGpsStatus()) {
            //progressDialog.show();
            getLocation();
        } else {
            /*show gps alert for on gps*/
            showGpsAlert();

        }

        if (getIntent().getExtras() != null) {
            Log.d(TAG, "onCreate: " + getIntent().getExtras());

            runOnUiThread(() -> new Handler().postDelayed(() -> {
                if (getIntent().hasExtra("notify_type")) {
                    String notify_type = getIntent().getStringExtra("notify_type");
                    if (notify_type.equals("chat")) {
                        loadScreens(3);
                        activeFragment = "messages";
                    }
                    if (notify_type.equals("notification")) {
                        // total_noti_count.setVisibility(View.GONE);
                        loadScreens(2);
                        activeFragment = "notifications";
                    }
                }
            }, 3000));
        }

        startLoginService();
        loginRegisterProfilePresenter.hitGetNotification(false);
    }


    //removing this because this feature not needed any-more {find-total_msg_countTV}.
  /*  public void getCount(Integer total) {
        Log.d( "totat messages: ","" + total);
       // context.total_msg_countTV.setText(String.valueOf(total > 99 ? "99+" : total));
        total_msg_countTV.setText(total+"");

        if (total>0){
            total_msg_countTV.setVisibility(View.VISIBLE);
        }else {
            total_msg_countTV.setVisibility(View.GONE);

        }
    }*/


    //removing this because this feature not needed any-more {find-total_msg_countTV}.
    /*public static void updateCount(int total)
    {

        Log.d( "totat messages: ","" + total);
        // context.total_msg_countTV.setText(String.valueOf(total > 99 ? "99+" : total));
        total_msg_countTV.setText(total+"");

        if (total>0){
            total_msg_countTV.setVisibility(View.VISIBLE);
        }else {
            total_msg_countTV.setVisibility(View.GONE);

        }

    }*/

    public void loadScreen(Integer screen) {

        if (!activeFragment.equalsIgnoreCase("notifications")) {
            // total_noti_count.setVisibility(View.GONE);

            loadScreens(screen);
        }
        activeFragment = "notifications";

    }

    private void startLoginService() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            LoginService.start(this, qbUser);

            /*Intent tempIntent = new Intent(this, CallService.class);
            PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
            */

            /*i added here*/
//            CallService.start(this);

        }
    }

    private void showGpsAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage("Please enable your gps location.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                    //turnGPSOn();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);
    }

    private void firstTimeLogin() {

        final QBUser user = new QBUser();
        String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));


        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        //user.setCustomData(profile_url);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));


        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                if (!QBChatService.getInstance().isLoggedIn()) {
                    QBUser currentUser = getUserFromSession();
                    ChatHelper.getInstance().loginToChat(currentUser, new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void unused, Bundle bundle) {
                            Log.e("DATA", "Success");
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("DATA", "ERROR " + e.getMessage());
                        }
                    });
                }
                QBSettings.getInstance().setEnablePushNotification(true);
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e("DATA", token);
                SubscribeService.subscribeToPushes(context, true);

                QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
                    @Override
                    public void onSubscriptionCreated() {
                        Log.e("DATA", "onSubscriptionCreated");
                    }

                    @Override
                    public void onSubscriptionError(Exception e, int i) {
                        Log.e("DATA", "onSubscriptionError " + e.toString());

                    }

                    @Override
                    public void onSubscriptionDeleted(boolean b) {
                        Log.e("DATA", "onSubscriptionDeleted");

                    }
                });

                // ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                //progressDialog.dismiss();
                //ProgressDialogFragment.hide(getFragmentManager());
                ErrorUtils.showSnackbar(homeIV, R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                firstTimeLogin();
                            }
                        });
            }
        });
    }


    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name", user.getLogin() + "  " + user.getPassword());
        return user;
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(context);
        // check if GPS location can get
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Address", "" + addresses + " " + gps.getLatitude() + " " + gps.getLongitude());

        String address = "";
        if (addresses != null && addresses.size() > 0) {
            if (addresses.get(0).getAddressLine(0) != null) {
                address = addresses.get(0).getAddressLine(0);
            }
        }

        new PreferenceManager().saveLocation(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()), address);

        loginRegisterProfilePresenter.hitUpdateLocation(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()), address);


        if (gps.canGetLocation()) {
            gps.getLocation();
        }
    }


    private boolean CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @OnClick({R.id.homeIV, R.id.notificataionIV, R.id.messagesIV, R.id.profileIV, R.id.menuIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.homeIV:
                if (!activeFragment.equalsIgnoreCase("Home")) {
                    loadScreens(1);
                }
                activeFragment = "Home";
                break;
            case R.id.notificataionIV:
                if (!activeFragment.equalsIgnoreCase("notifications")) {
                    // total_noti_count.setVisibility(View.GONE);

                    loadScreens(2);
                }
                activeFragment = "notifications";
                break;
            case R.id.messagesIV:
                if (!activeFragment.equalsIgnoreCase("messages")) {
                    loadScreens(3);
                }
                activeFragment = "messages";
                break;
            case R.id.profileIV:
                if (!activeFragment.equalsIgnoreCase("profile")) {
                    loadScreens(4);
                }
                activeFragment = "profile";
                break;
            case R.id.menuIV:
                if (!activeFragment.equalsIgnoreCase("menu")) {
                    loadScreens(5);
                }
                activeFragment = "menu";
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (!activeFragment.equals("Home")) {
            loadScreens(1);
            activeFragment = "Home";
        } else {
            super.onBackPressed();
        }


    }

    private void loadDialogsFromQb(final boolean silentUpdate) {
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();

        String my_quickblox_id = String.valueOf(sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));

        ChatHelper.getInstance().getDialogDashBoard(context, my_quickblox_id, requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                if (!QBChatService.getInstance().isLoggedIn()) {
                    QBUser currentUser = getUserFromSession();
                    ChatHelper.getInstance().loginToChat(currentUser, new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void unused, Bundle bundle) {
                            Log.e("DATA", "Success");
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("DATA", "ERROR " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onError(QBResponseException e) {
                //disableProgress();
                ToastUtils.shortToast(e.getMessage());
            }
        });


    }


    private void loadScreens(int i) {
        switch (i) {
            case 1:

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_hover));
                notificataionIV.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));
                messagesIV.setImageDrawable(getResources().getDrawable(R.drawable.msg_icon));
                profileIV.setImageDrawable(getResources().getDrawable(R.drawable.profile_normal));
                menuIV.setImageDrawable(getResources().getDrawable(R.drawable.menu));

                appUtils.addFragment(HomeFragment.newInstance(), getSupportFragmentManager(), R.id.frame, true);
                break;
            case 2:

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                notificataionIV.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon_hover));
                messagesIV.setImageDrawable(getResources().getDrawable(R.drawable.msg_icon));
                profileIV.setImageDrawable(getResources().getDrawable(R.drawable.profile_normal));
                menuIV.setImageDrawable(getResources().getDrawable(R.drawable.menu));

                appUtils.addFragment(NotificationFragment.newInstance(), getSupportFragmentManager(), R.id.frame, true);
                break;
            case 3:

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                notificataionIV.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));
                messagesIV.setImageDrawable(getResources().getDrawable(R.drawable.msg_icon_hover));
                profileIV.setImageDrawable(getResources().getDrawable(R.drawable.profile_normal));
                menuIV.setImageDrawable(getResources().getDrawable(R.drawable.menu));

                appUtils.addFragment(MessageFragment1.newInstance(), getSupportFragmentManager(), R.id.frame, true);
                break;
            case 4:

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                notificataionIV.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));
                messagesIV.setImageDrawable(getResources().getDrawable(R.drawable.msg_icon));
                profileIV.setImageDrawable(getResources().getDrawable(R.drawable.profle_active));
                menuIV.setImageDrawable(getResources().getDrawable(R.drawable.menu));


                appUtils.addFragment(ExploreFragment.newInstance(), getSupportFragmentManager(), R.id.frame, true);
                break;
            case 5:

                homeIV.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                notificataionIV.setImageDrawable(getResources().getDrawable(R.drawable.notification_icon));
                messagesIV.setImageDrawable(getResources().getDrawable(R.drawable.msg_icon));
                profileIV.setImageDrawable(getResources().getDrawable(R.drawable.profile_normal));
                menuIV.setImageDrawable(getResources().getDrawable(R.drawable.menu_hover));

                appUtils.addFragment(MenuFragment.newInstance(), getSupportFragmentManager(), R.id.frame, true);
                break;
        }

    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof NotificationResponse) {
            NotificationResponse notificationResponse = (NotificationResponse) test;

            if (notificationResponse.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                if (notificationResponse.getData() != null) {

                    /*int totalCount = notificationResponse.getData().size();
                    Log.d(TAG,"noti_count "+totalCount +" shared "+new PreferenceManager().getNotificationCount());
                    int processedCount = 0;
                    if (new PreferenceManager().getNotificationCount() < totalCount){
                        processedCount = totalCount - new PreferenceManager().getNotificationCount();
                        if (processedCount>0){
                            total_noti_count.setVisibility(View.VISIBLE);
                            total_noti_count.setText(String.valueOf(processedCount > 99 ? "99+" : processedCount));
                        }
                    }
                        new PreferenceManager().saveNotificationCount(totalCount);
*/


                    //removing this because this feature not needed any-more {find-total_noti_count}.
             /*       int totalCount = notificationResponse.getNoti_count();

                    if (totalCount>0){
                        total_noti_count.setVisibility(View.VISIBLE);
                        total_noti_count.setText(String.valueOf(totalCount > 99 ? "99+" : totalCount));
                    }

              */

                }

            } else {
                // total_noti_count.setVisibility(View.GONE);

            }


//            Toast.makeText(getContext(), ""+notificationResponse.getData().get(0).getEvent_info().getId(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), ""+notificationResponse.getMessage(), Toast.LENGTH_SHORT).show();

        }


        if (test instanceof updateUserLocationResponse) {
            updateUserLocationResponse response = (updateUserLocationResponse) test;

            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

            } else {
                //  Toast.makeText(context, "hello1", Toast.LENGTH_SHORT).show();


            }


        }
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }


}
