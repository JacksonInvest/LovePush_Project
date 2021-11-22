package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.response.SettingResponse;
import com.lovepushapp.model.response.deleteAccountResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements LoginMvp {
    private String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;



    @BindView(R.id.notification_toggleIV)
    ImageView notification_toggleIV;

    @BindView(R.id.receove_chat_req_ll_toggleIV)
            ImageView receove_chat_req_ll_toggleIV;

    @BindView(R.id.receive_love_req_tog_IV)
            ImageView receive_love_req_tog_IV;

    @BindView(R.id.hidehome_toggleIV)
            ImageView hidehome_toggleIV;

    @BindView(R.id.change_languageTV)
            AppCompatTextView change_languageTV;

    private AlertDialogs progressDialog;

    protected QBResRequestExecutor requestExecutor;
    boolean notificatin_tg,receive_chat_req_tg,rec_love_req_tg,hide_home_search_tg;


    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;


    String my_qb_id ;
    @Override
    public int getLayoutId() {
        return R.layout.activity_settings_updated;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.setting));
    }

    private Activity context = SettingsActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterProfilePresenter=new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);

        loginRegisterProfilePresenter.hitGetSettings();
        progressDialog = new AlertDialogs(context);

        String current_locale = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING);
        String english=getString(R.string.english);
        String german=getString(R.string.german);
        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();
        my_qb_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));


//        Toast.makeText(context, ""+LocaleUtils.getLanguage(getApplicationContext()), Toast.LENGTH_SHORT).show();
        if (current_locale.equalsIgnoreCase("en"))
        {
//            LocaleUtils.setLocale(getApplicationContext(),"en");
//            English
            change_languageTV.setText(getString(R.string.english));


        }
        if (current_locale.equalsIgnoreCase("de"))
        {
            change_languageTV.setText(getString(R.string.german));

//            LocaleUtils.setLocale(getApplicationContext(),"de");
        }


    }

    @OnClick({R.id.backIV, R.id.filterLL,R.id.changePassLL,R.id.notification_toggleIV,R.id.receove_chat_req_ll_toggleIV,R.id.receive_love_req_tog_IV,R.id.hidehome_toggleIV,R.id.lovePushGuideLL,R.id.privacyPolicyLL,R.id.helpSupportLL,R.id.change_languageTV,R.id.deleteAccountLL,R.id.membershiptLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.changePassLL:
                appUtils.startActivity(context,
                        ChangePassActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
            case R.id.filterLL:
                appUtils.startActivity(context,
                        FilterSettingsActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;

            case R.id.notification_toggleIV:
                if (!notificatin_tg) {
                    notificatin_tg = true;
                    loginRegisterProfilePresenter.hitSetNotification( 1);
                }
                else {
                    notificatin_tg = false;
                    loginRegisterProfilePresenter.hitSetNotification( 0);
                }
                manageToggleClick();
                break;
            case R.id.receove_chat_req_ll_toggleIV:
                if (!receive_chat_req_tg) {
                    receive_chat_req_tg = true;
                    loginRegisterProfilePresenter.hitSetChatRequest( 1);

                }
                else {
                    receive_chat_req_tg = false;
                    loginRegisterProfilePresenter.hitSetChatRequest( 0);

                }
                manageToggleClick();
                break;
            case R.id.receive_love_req_tog_IV:
                if (!rec_love_req_tg) {
                    rec_love_req_tg = true;
                    loginRegisterProfilePresenter.hitsetLikeRequest( 1);

                }
                else {
                    rec_love_req_tg = false;
                    loginRegisterProfilePresenter.hitsetLikeRequest( 0);

                }
                manageToggleClick();
                break;

            case R.id.hidehome_toggleIV:
                if (!hide_home_search_tg) {
                    hide_home_search_tg = true;
                    loginRegisterProfilePresenter.hitsetHideFromSearch( 1);

                }
                else {
                    hide_home_search_tg = false;
                    loginRegisterProfilePresenter.hitsetHideFromSearch( 0);

                }
                manageToggleClick();
                break;

            case R.id.lovePushGuideLL:
                appUtils.startActivity(context,
                        PreviewActivity.createIntent(context).putExtra("from","guidelines").putExtra("url",""),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
//                finish();
                break;
            case R.id.privacyPolicyLL:

                appUtils.startActivity(context,
                        PreviewActivity.createIntent(context).putExtra("from","privacy_policy").putExtra("url",TermsCondition.LEGACY),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );

//                finish();
                break;
            case R.id.helpSupportLL:
                appUtils.startActivity(context,
                        SupportActivity.createIntent(context).putExtra("from","setting"),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;

            case R.id.change_languageTV:
                showLanguageMenu();
                break;

            case R.id.deleteAccountLL:

                startActivity(new Intent(this,DeleteAccount.class));
                /*new AlertDialog.Builder(context)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.delete_account_alert))

                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                loginRegisterProfilePresenter.hit_delete_account();//block, unblock



                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/

                break;
            case R.id.membershiptLL:
                appUtils.startActivity(context,
                        memberShipActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;

        }
    }

    private void showLanguageMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(SettingsActivity.this, change_languageTV);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.language, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                change_languageTV.setText(item.getTitle());

                setLanguage(item.getTitle().toString());

//                Toast.makeText(SettingsActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu


}

    private void setLanguage(String value) {
    /*    switch (value) {
            case "English":
//                hit api
                LocaleUtils.setLocale(context, "en");
                setLang("en",value);
                break;
            case "German":
//                hit api
                LocaleUtils.setLocale(context, "de");
                setLang("de",value);
                break;
            case "Englisch":
                LocaleUtils.setLocale(context,"en");
                setLang("en",value);
                break;
            case "Deutsche":
                LocaleUtils.setLocale(context,"de");
                setLang("de",value);
                break;
            default:
                LocaleUtils.setLocale(context, "en");
                setLang("en",value);
                break;

        }
*/

    }

    private void setLang(String en, String value) {
        sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING,""+en);
        String your_new_lang=getString(R.string.your_new_lang);
        Toast.makeText(context, your_new_lang+" "+value, Toast.LENGTH_SHORT).show();
        goback();
    }

    private void manageToggleClick() {
        if (notificatin_tg)
        {
            notification_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_active));

//            /api/setNotification
//            "id:7
//            status:1"



        }
        if (!notificatin_tg)
        {

            notification_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_unactive));
        }



        if (receive_chat_req_tg)
        {
            receove_chat_req_ll_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_active));
        }
        if (!receive_chat_req_tg)
        {

            receove_chat_req_ll_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_unactive));
        }


        if (rec_love_req_tg)
        {
            receive_love_req_tog_IV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_active));
        }
        if (!rec_love_req_tg)
        {

            receive_love_req_tog_IV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_unactive));
        }



        if (hide_home_search_tg)
        {
            hidehome_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_active));
        }
        if (!hide_home_search_tg)
        {

            hidehome_toggleIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_unactive));
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.getSetting))
        {
            try{

                SettingResponse settingResponse=(SettingResponse) test;
                if (settingResponse.getSettings().getHide_from_search()==1)
                {

                    hide_home_search_tg=true;

                }
                if (settingResponse.getSettings().getReceive_chat_request()==1)
                {

                    receive_chat_req_tg=true;

                }
                if (settingResponse.getSettings().getReceive_like_request()==1)
                {
                    rec_love_req_tg=true;

                }
                if (settingResponse.getSettings().getReceive_notification()==1)
                {
                    notificatin_tg=true;
                    SharedPrefsHelper.getInstance().saveNotificationEnableDisable("1");

                }


                if (settingResponse.getSettings().getHide_from_search()==0)
                {

                    hide_home_search_tg=false;

                }
                if (settingResponse.getSettings().getReceive_chat_request()==0)
                {

                    receive_chat_req_tg=false;

                }
                if (settingResponse.getSettings().getReceive_like_request()==0)
                {
                    rec_love_req_tg=false;

                }
                if (settingResponse.getSettings().getReceive_notification()==0)
                {
                    notificatin_tg=false;
                    SharedPrefsHelper.getInstance().saveNotificationEnableDisable("0");
                }
                manageToggleClick();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.setNotification))
        {
            loginRegisterProfilePresenter.hitGetSettings();
        }
        else if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.setChatRequest))
        {
            loginRegisterProfilePresenter.hitGetSettings();
        }
        else if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.setLikeRequest))
        {
            loginRegisterProfilePresenter.hitGetSettings();
        }
        else if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.setHideFromSearch))
        {
            loginRegisterProfilePresenter.hitGetSettings();
        }


        if (serviceMode.equalsIgnoreCase(API_GLOBALS.API_NAME.deleteAccount)){
            deleteAccountResponse deleteAccountRes=(deleteAccountResponse) test;

            if (deleteAccountRes.getStatus()){

                int my_id = Integer.parseInt(my_qb_id);

                requestExecutor.deleteCurrentUser(my_id, new QBEntityCallbackImpl<Void>() {
                    @Override
                    public void onSuccess(Void user, Bundle params) {

                        Log.d(TAG, "Delete Account Successful");

                        appUtils.startActivity(context,
                                LoginActivity.createIntent(context),
                                true,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );

                    }

                    @Override
                    public void onError(QBResponseException responseException) {
                        Log.d(TAG, "Error SignIn" + responseException.getMessage());
                        progressDialog.dismiss();
                        appUtils.startActivity(context,
                                LoginActivity.createIntent(context),
                                true,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );

                        //hideProgressDialog();
                       // ToastUtils.longToast(R.string.sign_in_error_with_error);
                    }
                });
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
