package com.lovepushapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.lovepushapp.R;
import com.lovepushapp.activities.AddFeedListingActivity;
import com.lovepushapp.activities.LoginActivity;
import com.lovepushapp.activities.PersonalityTestActivity;
import com.lovepushapp.activities.PostAdsActivity;
import com.lovepushapp.activities.RecommendedProfileActivity;
import com.lovepushapp.activities.SettingsActivity;
import com.lovepushapp.activities.commisionsActivity;
import com.lovepushapp.activities.faqActivity;
import com.lovepushapp.activities.myAddFeedListingActivity;
import com.lovepushapp.activities.tipsMediaActivity;
import com.lovepushapp.activities.userTipsPdfFullPreview;
import com.lovepushapp.activities.withdrawalActivity;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.referral.referralCodeActivity;
import com.lovepushapp.activities.searchUsersActivity;
import com.lovepushapp.adapters.CustomExpandableListAdapter;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.ExpandableListDataPump;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.matchList.myMatchListActivity;
import com.lovepushapp.matchList.myMatchRequests;
import com.lovepushapp.model.response.LogoutResponse;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.referral.myTeamActivity;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.utils.UsersUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.QBUsers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuFragment extends BaseFragment implements LoginMvp {


    boolean isAdsLL, isPersonalMatchingLL;
    Unbinder unbinder;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;

    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    List<Integer> drawables = new ArrayList<>();
    @BindView(R.id.username_menu)
    AppCompatTextView usernameMenu;
    @BindView(R.id.email_menu)
    AppCompatTextView emailMenu;
    @BindView(R.id.settingIV)
    ImageView settingIV;
    @BindView(R.id.profile_menu)
    CircleImageView profileMenu;
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;

    private GetProfile profileResponse;
    private AlertDialogs progressDialog;

    int previousItem;

    /*    @BindView(R.id.menuAdsLL)
        LinearLayout menuAdsLL;
        @BindView(R.id.personalMatchingMenuLL)
        LinearLayout personalMatchingMenuLL;*/
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;


    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu_new;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(getActivity());
        loginRegisterProfilePresenter.attachView(this);
        profileResponse = getProfileModel();
        progressDialog = new AlertDialogs(getActivity());


        //drawables.add(R.drawable.live_stream);
        drawables.add(R.drawable.my_matches);
        drawables.add(R.drawable.ads);
        drawables.add(R.drawable.personal_matching);
        //drawables.add(R.drawable.around);
        drawables.add(R.drawable.earn_money);
        //drawables.add(R.drawable.shop);
        drawables.add(R.drawable.settings);
        drawables.add(R.drawable.user_tips);
        drawables.add(R.drawable.faq);
        drawables.add(R.drawable.logout);
        // menuAdsLL.setVisibility(View.GONE);
        //    personalMatchingMenuLL.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        profileResponse = getProfileModel();
        appUtils.hideKeyBoard(getActivity());
        String current_locale = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING);

       //Toast.makeText(getContext(), ""+current_locale, Toast.LENGTH_SHORT).show();
        if (current_locale.equalsIgnoreCase("de"))
        {
            setExpandableListViewGerman();
//            LocaleUtils.setLocale(getApplicationContext(),"de");
        }else
        {
            setExpandableListView();
        }
        setPreviousData();


    }

    private void setExpandableListViewGerman() {

        expandableListTitle = new ArrayList<>();
        //expandableListTitle.add("Live Stream");
        expandableListTitle.add("Meine Streichhölzer");
        expandableListTitle.add("Anzeigen");
        expandableListTitle.add("Persönliches Matching");
        // expandableListTitle.add("Around");
        expandableListTitle.add("Partnerzentrum");
        // expandableListTitle.add("Shop");
        expandableListTitle.add("Die Einstellungen");
        expandableListTitle.add("Benutzertipps hinzufügen");
        expandableListTitle.add("Häufig gestellte Frage");
        expandableListTitle.add("Ausloggen");
      /*  ViewTreeObserver vto = expandableListView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(() -> expandableListView.setIndicatorBounds(expandableListView.getRight()- 100, expandableListView.getWidth()));*/


        expandableListDetail = ExpandableListDataPump.getDataGerman();

        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail, drawables);
        expandableListView.setAdapter(expandableListAdapter);


//        expendable group listner


//          collapse expendable listner
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                for(int i=0;i<expandableListTitle.size();i++) {
                    if (i == groupPosition) {
                        //do nothing}
                    }
                    else{
                        expandableListView.collapseGroup(i);
                    }

                }


            }
        });


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.e("click: ",
                        expandableListTitle.get(groupPosition));




                switch (expandableListTitle.get(groupPosition)) {

                    case "Die Einstellungen":
                        appUtils.startActivity(getActivity(),
                                SettingsActivity.createIntent(getActivity()),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;

                    case "Häufig gestellte Frage":
                        appUtils.startActivity(getActivity(),
                                faqActivity.createIntent(getActivity()),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;

                    case "Benutzertipps hinzufügen":
                        appUtils.startActivity(getActivity(),
                                userTipsPdfFullPreview.createIntent(getActivity()).putExtra("from","user_tips").putExtra("url",""),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;
                    case "Ausloggen":

                        DialogUtils.showAlert(getActivity(), "Bist du sicher, Ausloggen", () -> loginRegisterProfilePresenter.hitLogout());
                        break;

                }

                return false;
            }
        });


        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Log.e("setExpandableListView: ",
                    expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition));


            switch (expandableListDetail.get(
                    expandableListTitle.get(groupPosition)).get(
                    childPosition))


            {
                case "Postanzeige":
                    appUtils.startActivity(getActivity(),
                            PostAdsActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );

                    break;
                case "Anzeigen-Feed":

                    appUtils.startActivity(getActivity(),
                            AddFeedListingActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Meine Anzeigen":

                    appUtils.startActivity(getActivity(),
                            myAddFeedListingActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Persönlichkeitstest":
                    appUtils.startActivity(getActivity(),
                            PersonalityTestActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Empfohlene Profile":
                    appUtils.startActivity(getActivity(),
                            RecommendedProfileActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Übereinstimmungsliste":
                    appUtils.startActivity(getActivity(),
                            myMatchListActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Match Anfrage":
                    appUtils.startActivity(getActivity(),
                            myMatchRequests.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Partnerübersicht":
                    appUtils.startActivity(getActivity(),
                            referralCodeActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Mein Team":
                    appUtils.startActivity(getActivity(),
                            myTeamActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Provisionen":
                    appUtils.startActivity(getActivity(),
                            commisionsActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Rückzug":
                    appUtils.startActivity(getActivity(),
                            withdrawalActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Tipps & Medien":
                    appUtils.startActivity(getActivity(),
                            tipsMediaActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

            }
            return false;
        });
    }


    public void setExpandableListView() {
        expandableListTitle = new ArrayList<>();
        //expandableListTitle.add("Live Stream");
        expandableListTitle.add("My Matches");
        expandableListTitle.add("Ads");
        expandableListTitle.add("Personal Matching");
       // expandableListTitle.add("Around");
        expandableListTitle.add("Affiliate Center");
       // expandableListTitle.add("Shop");
        expandableListTitle.add("Settings");
        expandableListTitle.add("User Tips");

        expandableListTitle.add("FAQ");
        expandableListTitle.add("Logout");
      /*  ViewTreeObserver vto = expandableListView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(() -> expandableListView.setIndicatorBounds(expandableListView.getRight()- 100, expandableListView.getWidth()));*/


        expandableListDetail = ExpandableListDataPump.getData();

        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail, drawables);
        expandableListView.setAdapter(expandableListAdapter);


//        expendable group listner


//          collapse expendable listner
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                for(int i=0;i<expandableListTitle.size();i++) {
                    if (i == groupPosition) {
                        //do nothing}
                    }
                    else{
                            expandableListView.collapseGroup(i);
                        }

                }


            }
        });


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.e("click: ",
                        expandableListTitle.get(groupPosition));




                switch (expandableListTitle.get(groupPosition)) {

                    case "Settings":
                        appUtils.startActivity(getActivity(),
                                SettingsActivity.createIntent(getActivity()),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;
                    case "User Tips":
                        appUtils.startActivity(getActivity(),
                                userTipsPdfFullPreview.createIntent(getActivity()),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;


                    case "FAQ":
                        appUtils.startActivity(getActivity(),
                                faqActivity.createIntent(getActivity()),
                                false,
                                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                false,
                                0
                        );
                        break;
                    case "Logout":

                        DialogUtils.showAlert(getActivity(), "Are you sure to logout", () -> loginRegisterProfilePresenter.hitLogout());
                      //  userLogout();


                        break;

                }

                return false;
            }
        });


        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Log.e("setExpandableListView: ",
                    expandableListDetail.get(
                            expandableListTitle.get(groupPosition)).get(
                            childPosition));


            switch (expandableListDetail.get(
                    expandableListTitle.get(groupPosition)).get(
                    childPosition))


            {
                case "Post Ad":
                    appUtils.startActivity(getActivity(),
                            PostAdsActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );

                    break;
                case "Ad Feed":

                    appUtils.startActivity(getActivity(),
                            AddFeedListingActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "My Ads":

                    appUtils.startActivity(getActivity(),
                            myAddFeedListingActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Personality Test":
                    appUtils.startActivity(getActivity(),
                            PersonalityTestActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Recommended Profiles":
                    appUtils.startActivity(getActivity(),
                            RecommendedProfileActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Match List":
                    appUtils.startActivity(getActivity(),
                            myMatchListActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Match Requests":
                    appUtils.startActivity(getActivity(),
                            myMatchRequests.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Affiliate Overview":
                    appUtils.startActivity(getActivity(),
                            referralCodeActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "My Team":
                    appUtils.startActivity(getActivity(),
                            myTeamActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Commissions":
                    appUtils.startActivity(getActivity(),
                            commisionsActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;

                case "Withdrawal":
                    appUtils.startActivity(getActivity(),
                            withdrawalActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;
                case "Tips & Media":
                    appUtils.startActivity(getActivity(),
                            tipsMediaActivity.createIntent(getActivity()),
                            false,
                            GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                            false,
                            0
                    );
                    break;


            }
            return false;
        });

    }



    private void userLogout() {
        progressDialog.show();

        ChatHelper.getInstance().destroy();
        logout();
        //finish();
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
        progressDialog.dismiss();
        appUtils.startActivity(getActivity(),
                LoginActivity.createIntent(getActivity()),
                true,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
    }
    private void logoutREST() {
        Log.d("MenuFragment", "SignOut");
        QBUsers.signOut().performAsync(null);

    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof LogoutResponse) {
            LogoutResponse response = (LogoutResponse) test;
            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                //logOutNow();
                userLogout();
            }

        }

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

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

    private void setPreviousData() {
        String name=profileResponse.getData().getName();
        if (name.isEmpty())
            name= SharedStorage.getString(GlobalsVariables.STORAGE.NAME,GlobalsVariables.STORAGE.TYPE_STRING);
        usernameMenu.setText(profileResponse.getData().getUsername());

        emailMenu.setText(name);
//        appUtils.loadGlideImage(getActivity(), profileResponse.getData().getProfileImage(), profileMenu, R.drawable.profile_bg);

       /* if (profileResponse.getData().getProfileImages().get(0).getImage()!=null)
        Picasso.get()
                .load(profileResponse.getData().getProfileImages().get(0).getImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(profileMenu);
        else*/
            Picasso.get()
                    .load(profileResponse.getData().getProfileImage())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(profileMenu);
    }

    @OnClick({R.id.profile_menu, R.id.settingIV,R.id.passwordET})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_menu:

                appUtils.startActivity(getActivity(),
                        ProfileActivity.createIntent(getActivity()),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
            case R.id.settingIV:
                appUtils.startActivity(getActivity(),
                        SettingsActivity.createIntent(getActivity()),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
            case R.id.passwordET:
                appUtils.startActivity(getActivity(),
                        searchUsersActivity.createIntent(getActivity()),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;
        }
    }

}
