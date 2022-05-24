package com.lovepushapp.core;

import static com.lovepushapp.core.utils.API_GLOBALS.ACCOUNT_KEY;
import static com.lovepushapp.core.utils.API_GLOBALS.AUTH_KEY;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.lovepushapp.core.Component.DaggerDiComponent;
import com.lovepushapp.core.Component.DiComponent;
import com.lovepushapp.core.Component.DiModule;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.utils.LocaleUtils;
import com.lovepushapp.videoTestJava.db.DbHelper;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.SubscribePushStrategy;



public class MyApplication extends Application {

    private static MyApplication instance;
    private DiComponent myComponent;




    public static final String USER_DEFAULT_PASSWORD = "quickblox";
    public static final int CHAT_PORT = 5223;
    public static final int SOCKET_TIMEOUT = 300;
    public static final boolean KEEP_ALIVE = true;
    public static final boolean USE_TLS = true;
    public static final boolean AUTO_JOIN = false;
    public static final boolean AUTO_MARK_DELIVERED = true;
    public static final boolean RECONNECTION_ALLOWED = true;
    public static final boolean ALLOW_LISTEN_NETWORK = true;



    private QBResRequestExecutor qbResRequestExecutor;

    /*private QBResRequestExecutor qbResRequestExecutor;*/


    private static DbHelper dbHelper;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        QBSettings.getInstance().init(this, API_GLOBALS.APP_ID, AUTH_KEY, API_GLOBALS.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
       // QBSettings.getInstance().setEndpoints(API_DOMAIN, CHAT_DOMAIN), ServiceZone.PRODUCTION);
        QBChatService.setDebugEnabled(true);
        QBChatService.setDefaultPacketReplyTimeout(10000);
        QBSettings.getInstance().setEnablePushNotification(true);
        QBSettings.getInstance().setSubscribePushStrategy(SubscribePushStrategy.ALWAYS);

        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);

        dbHelper = new DbHelper(this);


//        Fabric.with(this, new Crashlytics());


        instance = this;

        //Initialize Dagger
        myComponent = DaggerDiComponent.builder().diModule(new DiModule()).build();
    }


    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public DiComponent getMyComponent() {
        return myComponent;
    }

    public static DiComponent getAppComponent(Context applicationContext) {
        return ((MyApplication) applicationContext.getApplicationContext()).getMyComponent();
    }
/*
    public void initNotificationToken() {
        if (checkPlayServices()) {
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }
    }*/


    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }


  public synchronized DbHelper getDbHelper(){
      return dbHelper;
  }


    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(LocaleUtils.onAttach(base, "en"));
    }
}
