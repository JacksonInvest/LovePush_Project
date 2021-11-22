package com.lovepushapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.DashboardActivity;
import com.lovepushapp.activities.ExploreFragment;
import com.lovepushapp.activities.ProfileEditActivity;
import com.lovepushapp.activities.matchedSuccessActivity;
import com.lovepushapp.activities.singleHomePostView;
import com.lovepushapp.adapters.ProfileAdapter;
import com.lovepushapp.adapters.ProfilePostsAdapter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.response.DeleteProfile.DeleteProfile;
import com.lovepushapp.model.response.LikeResponse;
import com.lovepushapp.model.response.Profile_Model.Data;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.quickBloxTest.ui.activity.ChatActivity;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.QbDialogHolder;
import com.lovepushapp.quickBloxTest.utils.qb.callback.QbEntityCallbackImpl;
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
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import hari.bounceview.BounceView;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.nameTV)
    AppCompatTextView nameTV;
    @BindView(R.id.emailTV)
    AppCompatTextView emailTV;
    @BindView(R.id.phoneTV)
    AppCompatTextView phoneTV;
    @BindView(R.id.bioTV)
    AppCompatTextView bioTV;
    @BindView(R.id.backIV)
    ImageView backIV;
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    ProfileImage profileImage;
    ProfileAdapter adapter;
    @BindView(R.id.profileVp)
    ViewPager profileVp;
    @BindView(R.id.left)
    ImageView left;

    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.likell)
    LinearLayout likell;
    @BindView(R.id.editIV)
    ImageView editIV;

    @BindView(R.id.usernameTV)
    AppCompatTextView usernameTV;
    @BindView(R.id.ageTV)
    AppCompatTextView ageTV;
    @BindView(R.id.lookingforTV)
    AppCompatTextView lookingforTV;
    @BindView(R.id.fav_travelTV)
    AppCompatTextView fav_travelTV;
    @BindView(R.id.languageTV)
    AppCompatTextView languageTV;
    @BindView(R.id.hobiesTV)
    AppCompatTextView hobiesTV;

    @BindView(R.id.lookingforLL)
    LinearLayout lookingforLL;
    @BindView(R.id.languageLL)
    LinearLayout languageLL;
    @BindView(R.id.hobiesLL)
    LinearLayout hobiesLL;
    @BindView(R.id.fav_travelLL)
    LinearLayout fav_travelLL;

    @BindView(R.id.bioLL)
    LinearLayout bioLL;

    //    likeLL
    @BindView(R.id.dislikeIV)
    ImageView dislikeIV;


    @BindView(R.id.likeIV)
    ImageView likeIV;

    @BindView(R.id.drivkIV)
    ImageView drivkIV;

    @BindView(R.id.accept_rejectLL)
    LinearLayout accept_rejectLL;
    @BindView(R.id.nestedScrollPosts)
    NestedScrollView nestedScrollPost;
    @BindView(R.id.postTV)
    TextView postTV;

    @BindView(R.id.intensionTV)
    TextView intensionTV;

    @BindView(R.id.intensionLL)
    LinearLayout intensionLL;

    @BindView(R.id.relationshipStatusTV)
    TextView relationshipStatusTV;

    @BindView(R.id.relationshipStatusLL)
    LinearLayout relationshipStatusLL;


    @BindView(R.id.progressbarPosts)
    ProgressBar progressbarPosts;

    @BindView(R.id.chatIV)
    ImageView chatIV;
    boolean isProfile = true;
    boolean isAccpectLikeClick = false;

    int currentPage = 0;
    List<ProfileImage> images = new ArrayList<>();
    String userId = "";
    String from = "";
    RelativeLayout slider;
    String connect_id;
    boolean is_send_notification = false;
    @BindView(R.id.postsRV)
    RecyclerView postsRV;
    Integer postCurrentPage = 1, last_page_post = 1;
    List<getHomePostResponse.Data.Datum> postsList = new ArrayList<>();
    boolean match_1_fill = false, match_2_fill = false;
    Data userProfileData;
    @BindView(R.id.layoutData)
    LinearLayout layoutData;
    boolean isMyProfile = false;
    String event_type = "", isReceiveNotification = "1";
    String receiverName = "", receiverImage = "", receiverId = "0", receiver_event_type = "", chat_match_id = "0", chat_deleted_by = "0", blocked_by = "0";
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    private String imagePath = "";
    private String notiReceiverQbId = "0", notiEventType = "", acceptEventType = "";
    private Activity context = ProfileActivity.this;
    private String TAG = "ProfileActivity";
    private AlertDialogs progressDialog;
    Boolean meBlocked=false;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.profile));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);
        progressDialog = new AlertDialogs(context);
        slider = findViewById(R.id.slider);
        chatIV.setVisibility(View.GONE);

        slider.setLayoutParams(new LinearLayout.LayoutParams(Util.display(this).widthPixels, Util.display(this).widthPixels));
        if (getIntent().getExtras() != null) {
            // likell.setVisibility(View.VISIBLE);
            isMyProfile = false;
            editIV.setVisibility(View.GONE);
            isProfile = false;
            userId = getIntent().getStringExtra("user_id");
            from = getIntent().getStringExtra("from");
            if (from.equalsIgnoreCase("explore")) {
//                Toast.makeText(context, "Explore", Toast.LENGTH_SHORT).show();
            }
            if (from.equalsIgnoreCase("notification")) {
//                Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();

                event_type = getIntent().getStringExtra("event_type");

                if (event_type == null) {
                    event_type = "";
                }
                connect_id = getIntent().getStringExtra("connect_id");
                String noti_id = getIntent().getStringExtra("noti_id");
                Log.e("EVENT_TYPE", event_type);
                if (event_type.equals("S_L_R") || event_type.equals("S_C_R") || event_type.equals("S_M_R")) {
                    accept_rejectLL.setVisibility(View.VISIBLE);

                } else {
                    // Toast.makeText(context, ""+noti_id, Toast.LENGTH_SHORT).show();
                    accept_rejectLL.setVisibility(View.GONE);
                }


                if (!event_type.equals("S_L_R") && !event_type.equals("S_C_R") && !event_type.equals("S_M_R")) {
                    loginRegisterProfilePresenter.hitDeleteNotification(noti_id);
                }

                likell.setVisibility(View.GONE);
            }

        } else {
            isMyProfile = true;
            userId = (String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
            likell.setVisibility(View.GONE);
            editIV.setVisibility(View.VISIBLE);
            chatIV.setVisibility(View.GONE);
        }

        BounceView.addAnimTo(editIV);

    }


    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }

    private void getProfile() {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();
        loginRegisterProfilePresenter.hitGetProfile(userId, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                manageProfile(response.body());
            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        layoutData.setVisibility(View.GONE);
        // String myuserId= (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);


        match_1_fill = false;
        match_2_fill = false;
        if (isMyProfile) {
        }
        postsList.clear();
        loginRegisterProfilePresenter.hit_get_my_posts(postCurrentPage, userId);
    }

    private <T> void manageProfile(T body) {
        if (body instanceof GetProfile) {
            GetProfile res = (GetProfile) body;
            Log.d(TAG, "onApiResponse: " + new Gson().toJson(res));
            images.clear();
            currentPage = 0;
            if (isProfile)
                sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res));
            nameTV.setText(res.getData().getName());
            emailTV.setText(res.getData().getEmail());
            phoneTV.setText(res.getData().getPhoneCode() + res.getData().getPhone());
            bioTV.setText(res.getData().getAboutme());
            intensionTV.setText(res.getData().getLovePushIntention());
            usernameTV.setText(res.getData().getUsername());
            fav_travelTV.setText(res.getData().getEducation());
            ageTV.setText(String.valueOf(res.getData().getAge()));
            languageTV.setText(res.getData().getLanguage());
            hobiesTV.setText(res.getData().getHobbies());
            isReceiveNotification = res.getData().getReceive_notification();
            notiReceiverQbId = String.valueOf(res.getData().getQuickbloxId());
            layoutData.setVisibility(View.VISIBLE);

            StringBuilder stringBuilder = new StringBuilder();

            String man = "";
            String women = "";
            String trans = "";
            try {
                for (int i = 0; i < res.getF_que_ans().size(); i++) {
                    if (res.getF_que_ans().get(i).equals(1)) {
                        man = "Man";
                    }
                    if (res.getF_que_ans().get(i).equals(2)) {
                        women = "Women";
                    }
                    if (res.getF_que_ans().get(i).equals(3)) {
                        trans = "Transgender";
                    }
                }

            } catch (Exception e) {
                Log.e("EXPROFILE", String.valueOf(e));
            }
            if (man.equalsIgnoreCase("Man")) {
                stringBuilder.append("Man");
            }
            if (women.equalsIgnoreCase("Women")) {
                stringBuilder.append("Women");
                if (man.equalsIgnoreCase("Man")) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Man, Women");
                }

            }
            if (trans.equalsIgnoreCase("Transgender")) {
                stringBuilder.append("Transgender");
                if (man.equalsIgnoreCase("Man")) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Man, Transgender");
                    if (women.equalsIgnoreCase("women")) {

                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Man, Women, Transgender");

                    }
                }
                /*if (women.equalsIgnoreCase("women"))
                {
                    stringBuilder.append("Women, Transgender");
                }*/


            }

            if (res.getData().getRelationship() == null) {
                relationshipStatusLL.setVisibility(View.GONE);

            } else if (res.getData().getRelationship() == 0) {
                relationshipStatusLL.setVisibility(View.GONE);

            } else {
                if (res.getData().getRelationship() == 1) {
                    relationshipStatusTV.setText(getString(R.string.single));
                }
                if (res.getData().getRelationship() == 2) {
                    relationshipStatusTV.setText(getString(R.string.in_relationship));
                }
                if (res.getData().getRelationship() == 3) {
                    relationshipStatusTV.setText(getString(R.string.married));
                }
                if (res.getData().getRelationship() == 4) {
                    relationshipStatusTV.setText(getString(R.string.recently_separated));
                }

                relationshipStatusLL.setVisibility(View.VISIBLE);
            }

//            String looking_for= String.valueOf(res.getF_que_ans());

//            Toast.makeText(context, "hello "+res.getF_que_ans(), Toast.LENGTH_SHORT).show();
//            "f_que_ans": 1 = (Man)
//                    2 = (Women)
//                    3 = (Transgender)
            lookingforTV.setText(stringBuilder);
            /*if (looking_for!=null) {

                if (looking_for.equalsIgnoreCase("1")) {
                    lookingforTV.setText("Man");
                } else if (looking_for.equalsIgnoreCase("2")) {
                    lookingforTV.setText("Women");
                }
                if (looking_for.equalsIgnoreCase("3")) {
                    lookingforTV.setText("Transgender");
                }
            }*/
            if (!lookingforTV.getText().toString().isEmpty()) {
                lookingforLL.setVisibility(View.VISIBLE);
            }
            if (res.getData().getLanguage() != null) {
                languageLL.setVisibility(View.VISIBLE);
            }
            if (res.getData().getHobbies() != null) {
                hobiesLL.setVisibility(View.VISIBLE);
            }
            if (res.getData().getEducation() != null) {
                fav_travelLL.setVisibility(View.VISIBLE);
            }
            if (res.getData().getAboutme() != null && !res.getData().getAboutme().equalsIgnoreCase("About me")) {
                bioLL.setVisibility(View.VISIBLE);
            }
            if (res.getData().getLovePushIntention() != null) {
                intensionLL.setVisibility(View.VISIBLE);
            }


//            "f_que_ans": 1 = (Man)
//2 = (Women)
//3 = (Transgender)
//            langauge

            profileImage = new ProfileImage();
            profileImage.setImage(res.getData().getProfileImage());
            profileImage.setId(-10);
            profileImage.setUserId((long) 55);

            images.addAll(res.getData().getProfileImages());
            if (res.getData().getProfileImages().isEmpty()) {
                images.add(profileImage);
            }


            String profileImage=res.getData().getProfileImage() == null ? "": res.getData().getProfileImage();
            for(int i = 0 ; i < images.size(); i++ ){
                if (profileImage.equalsIgnoreCase(images.get(i).getImage())){
                    ProfileImage profileImage1 = images.get(i);
                    images.remove(i);
                    images.add(0, profileImage1);
                    break;
                }
            }




            adapter = new ProfileAdapter(images, context, isMyProfile);
            profileVp.setAdapter(adapter);
            profileVp.setCurrentItem(0, true);
            if (images.size() >= 1) {
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
            }
            if (images.size() == 0 || images.size() == 1) {
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
            }
//            Toast.makeText(context, ""+images.size(), Toast.LENGTH_SHORT).show();
            if (res.getData().getProfileImages() == null) {

            }


            if (getIntent().getExtras() != null) {
                receiverName = res.getData().getName();
                receiverImage = res.getData().getProfileImage();
                receiverId = String.valueOf(res.getData().getId());
                blocked_by = String.valueOf(res.getData().getBlockedBy());
                chat_deleted_by = String.valueOf(res.getData().getDeletedBy());
                //if (from.equalsIgnoreCase("explore")){

                if (res.getData().getMatchInfo1().size() == 0 && res.getData().getMatchInfo2().size() > 0) {
                    // Toast.makeText(context, "match 2 fill", Toast.LENGTH_SHORT).show();

                    if (from.equalsIgnoreCase("chat")) {
                        chatIV.setVisibility(View.GONE);
                    } else {
                        chatIV.setVisibility(View.VISIBLE);
                    }
                    likell.setVisibility(View.GONE);
                    match_2_fill = true;
                    userProfileData = res.getData();
                    receiver_event_type = res.getData().getMatchInfo2().get(0).getEventType();
                    chat_match_id = String.valueOf(res.getData().getMatchInfo2().get(0).getId());


                }

                if (res.getData().getMatchInfo2().size() == 0 && res.getData().getMatchInfo1().size() > 0) {
                    //Toast.makeText(context, "match 1 fill", Toast.LENGTH_SHORT).show();
                    if (from.equalsIgnoreCase("chat")) {
                        chatIV.setVisibility(View.GONE);
                    } else {
                        chatIV.setVisibility(View.VISIBLE);
                    }
                    likell.setVisibility(View.GONE);
                    match_1_fill = true;
                    userProfileData = res.getData();
                    receiver_event_type = res.getData().getMatchInfo1().get(0).getEventType();
                    chat_match_id = String.valueOf(res.getData().getMatchInfo1().get(0).getId());
                }

                if (res.getData().getMatchInfo1().size() == 0 && res.getData().getMatchInfo2().size() == 0) {
                    chatIV.setVisibility(View.GONE);
                    if (event_type.equals("S_L_R") || event_type.equals("S_C_R") || event_type.equals("S_M_R")) {
                        likell.setVisibility(View.GONE);
                    } else {
                        likell.setVisibility(View.VISIBLE);
                    }

                }

                if (res.getData().getMatchInfo1().size() != 0 && res.getData().getMatchInfo2().size() != 0) {
                    if (isMyProfile) {
                        postsList.clear();
                        loginRegisterProfilePresenter.hit_get_my_posts(postCurrentPage, userId);
                    }
                }
                if (userProfileData!=null)
                {
                    String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
                    Boolean isBlocked = String.valueOf(userProfileData.getBlockedBy()).equalsIgnoreCase(my_user_id);
                    if (isBlocked)
                        chatIV.setVisibility(View.GONE);
                    else if (!isBlocked && userProfileData.getBlockedBy()!=0)
                        meBlocked=true;
                }
                //}
            }


            //removing this because this feature not needed any-more {find-profileVp}.
/*            Handler handler = new Handler();
            Runnable update = () -> {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                profileVp.setCurrentItem(currentPage++, true);
            };


            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 3000, 5000);*/

//                Toast.makeText(context, "profile images "+res.getData().getProfileImages().get(0).getImage(), Toast.LENGTH_SHORT).show();
            Log.e("POST", new Gson().toJson(res));

        }
    }

    private void setAdapterRecent(List<getHomePostResponse.Data.Datum> pathlist) {
//        Log.e("PATHLIST", pathlist.get(0).getImage());
        try {
            postsRV.setNestedScrollingEnabled(false);

            postsRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ProfilePostsAdapter adap = new ProfilePostsAdapter(context, pathlist, (view, position, type, o) -> {
                List<getHomePostResponse.Data.Datum> list = (List<getHomePostResponse.Data.Datum>) o;
                Log.e("onItemClick: ", String.valueOf(type));
                Intent intent = new Intent(context, singleHomePostView.class);
                intent.putExtra("data", new Gson().toJson(list.get(position)));
                intent.putExtra("isMatchedUser", likell.getVisibility() == View.VISIBLE);
                startActivity(intent);
                /*if (type==150)
                {

                    goToDashboardScreen();

                }
                else
                {
                    loginRegisterProfilePresenter.deleteprofile(String.valueOf(type));
                    adapter.removeView(type);

                }

*/


            });
            postsRV.setAdapter(adap);
            if (nestedScrollPost != null) {

                nestedScrollPost.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (postCurrentPage < last_page_post) {
                                        progressbarPosts.setVisibility(View.VISIBLE);
                                        postCurrentPage++;

                                        loginRegisterProfilePresenter.hit_get_my_posts(postCurrentPage, userId);

                                    }


                                }
                            }, 500);

                            // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof DeleteProfile) {

            DeleteProfile res = (DeleteProfile) test;

            Log.e("onApiResponse: ", "" + res.getMessage());
            appUtils.setSnackbar(postsRV, res.getMessage());

        }

        if (test instanceof LikeResponse) {
            LikeResponse response = (LikeResponse) test;

            if (response.getStatus()) {

                if (is_send_notification) {
                    sendMsgPushNotification(notiEventType);
                    if (isAccpectLikeClick) {
                        Intent intent = new Intent(context, matchedSuccessActivity.class);
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("event_type", receiver_event_type);
                        intent.putExtra("receiverQb_id", notiReceiverQbId);
                        intent.putExtra("restore_match_id", response.getData().getMatchDetails().getId() + "");
                        intent.putExtra("deletedBy", chat_deleted_by);
                        intent.putExtra("blockedBy", blocked_by);
                        intent.putExtra("isReceiveNotification", isReceiveNotification);

                        startActivity(intent);
                        finish();
                    } else {
                        alertPopup(response.getMessage());
                    }
                }
            }
        }

        if (test instanceof getHomePostResponse) {
            //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            getHomePostResponse res = (getHomePostResponse) test;

            if (res.getStatus() == 200) {
                last_page_post = res.getData().getLastPage();
                List<getHomePostResponse.Data.Datum> data = res.getData().getData();
                postsList.addAll(data);

                if (postsList.size() > 0) {
                    List<getHomePostResponse.Data.Datum> newList = new ArrayList();
                    for (int i = 0; i < postsList.size(); i++) {
                        if (postsList.get(i).getImage() != null)
                            newList.add(postsList.get(i));
                    }
                    setAdapterRecent(newList);
                    postTV.setVisibility(View.VISIBLE);
                } else {
                    postTV.setVisibility(View.GONE);

                }
            } else {
                postTV.setVisibility(View.GONE);

            }

        }

    }


    private void sendMsgPushNotification(String notiEventType) {
        String my_shared_name = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_image = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_qb_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.QuickbloxId, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_shared_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        String message = "Notification";

        if (notiEventType.equalsIgnoreCase("Connect")) {
            message = my_shared_name + " has sent you a connect request.";
        }

        if (notiEventType.equalsIgnoreCase("Like")) {
            message = my_shared_name + " has sent you a date request.";
        }

        if (notiEventType.equalsIgnoreCase("Chat")) {
            message = my_shared_name + " has sent you a chat request.";
        }

        if (notiEventType.equalsIgnoreCase("Accept")) {
            if (acceptEventType.equalsIgnoreCase("S_C_R")) {
                message = my_shared_name + " has accepted connect request.";


            }
            if (acceptEventType.equalsIgnoreCase("S_M_R")) {
                message = my_shared_name + " has accepted chat request.";

            }
            if (acceptEventType.equalsIgnoreCase("S_L_R")) {
                message = my_shared_name + " has accepted date request.";

            }
        }

        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(Integer.valueOf(notiReceiverQbId));

        QBEvent event = new QBEvent();
        event.setUserIds(userIds);
//        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setEnvironment(QBEnvironment.PRODUCTION);
        event.setNotificationType(QBNotificationType.PUSH);
        event.setType(QBEventType.ONE_SHOT);
        //event.setPushType(QBPushType.APNS);

        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
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
                Log.e(TAG, "PushNotification " + " Success " + data + " qbId " + notiReceiverQbId);

            }

            @Override
            public void onError(QBResponseException errors) {
            }
        });

    }

    public void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = getIntent();
                    intent.putExtra("explore", true);
                    setResult(ExploreFragment.EXPLORE_TASK_CODE, intent);
                    finish();

                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }


    @OnClick({R.id.backIV, R.id.editIV, R.id.left, R.id.right, R.id.likeIV, R.id.drivkIV, R.id.dislikeIV, R.id.acceptTV, R.id.rejectTV, R.id.chat_RequestIV, R.id.chatIV})
    public void onViewClicked(View view) {
        isAccpectLikeClick = false;
        switch (view.getId()) {
            case R.id.editIV:
                appUtils.startActivity(context,
                        ProfileEditActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
                break;

            case R.id.backIV:

                onBackPressed();
                break;
            case R.id.left:
                profileVp.setCurrentItem(getItem(-1), true);
                currentPage = getItem(-1);

                break;
            case R.id.right:


                profileVp.setCurrentItem(getItem(+1), true);
                currentPage = getItem(+1);

                break;

            case R.id.dislikeIV:
                is_send_notification = false;
                sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "yes");

                String dislike = "0";

                loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(userId), dislike);
                break;
            case R.id.likeIV:
                String like = "1";
                sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "yes");

                is_send_notification = true;
                notiEventType = "Like";
                loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(userId), like);
                break;
            case R.id.drivkIV:
                sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "yes");

                is_send_notification = true;
                notiEventType = "Connect";
                loginRegisterProfilePresenter.hitConnect(Integer.valueOf(userId));
                break;
            case R.id.chat_RequestIV:
                is_send_notification = true;
                notiEventType = "Chat";
                loginRegisterProfilePresenter.hitChatRequest(userId);
            case R.id.acceptTV:
//                Toast.makeText(context, "Accept Testing", Toast.LENGTH_SHORT).show();
                is_send_notification = true;
                notiEventType = "Accept";

                if (event_type.equalsIgnoreCase("S_C_R")) {
                    String status = "A";
                    acceptEventType = "S_C_R";
                    sendAcceptRejectConnectRequest(userId, status);

                }
                if (event_type.equalsIgnoreCase("S_M_R")) {
                    String status = "A";
                    acceptEventType = "S_M_R";

//                    Toast.makeText(context, "Accept chat request on testing", Toast.LENGTH_SHORT).show();
                    sendChatAcceptRejectRequest(status);
                }
                if (event_type.equalsIgnoreCase("S_L_R")) {
                    String like1 = "1";
                    acceptEventType = "S_L_R";
                    loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(userId), like1);
                    isAccpectLikeClick = true;

                }
                break;
            case R.id.rejectTV:
                is_send_notification = false;
                if (event_type.equalsIgnoreCase("S_C_R")) {
                    String status2 = "R";
                    sendAcceptRejectConnectRequest(userId, status2);
                    alertPopup("Request Rejected");
                }
                if (event_type.equalsIgnoreCase("S_M_R")) {
                    String status2 = "R";
                    sendChatAcceptRejectRequest(status2);
//                    Toast.makeText(context, "Reject chat request on testing", Toast.LENGTH_SHORT).show();
                }

                if (event_type.equalsIgnoreCase("S_L_R")) {
                    String dislike1 = "0";

                    loginRegisterProfilePresenter.hitLike_Dislike(Integer.valueOf(userId), dislike1);

                }

//                Toast.makeText(context, "Reject Testing", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatIV:
                if (from.equalsIgnoreCase("chat")) {
                    onBackPressed();
                } else {
                    startChat();
                }
                break;

        }
    }

    private void startChat() {
        String dialog_id = "", receiverName = "", receiverImage = "", receiverId = "", event_type = "";

        if (match_1_fill) {

            if (userProfileData.getMatchInfo1().get(0).getQbChatDialog() != null) {
                dialog_id = userProfileData.getMatchInfo1().get(0).getQbChatDialog().getQbDialogId();
                startOldChat(dialog_id);

            } else {
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser user1 = new QBUser();
                user1.setId(Integer.parseInt(notiReceiverQbId));
                qbUsers.add(user1);
                createDialog(qbUsers, receiverName);
            }

        } else if (match_2_fill) {
            if (userProfileData.getMatchInfo2().get(0).getQbChatDialog() != null) {
                dialog_id = userProfileData.getMatchInfo2().get(0).getQbChatDialog().getQbDialogId();
                startOldChat(dialog_id);
            } else {
                ArrayList<QBUser> qbUsers = new ArrayList<>();
                QBUser user1 = new QBUser();
                user1.setId(Integer.parseInt(notiReceiverQbId));
                qbUsers.add(user1);
                createDialog(qbUsers, receiverName);
            }

        }
    }

    private void createDialog(ArrayList<QBUser> selectedUsers, String chatName) {
        progressDialog.show();
        Log.e("SELECTEDUSERS", String.valueOf(selectedUsers));
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers, chatName,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("dialog_id", dialog.getDialogId());
                        intent.putExtra("newDialogCreate", "true");
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("event_type", receiver_event_type);
                        intent.putExtra("isBlocked", meBlocked);

                        if (chat_match_id == null)
                            intent.putExtra("match_id", "");
                        else
                            intent.putExtra("match_id", chat_match_id);
                        intent.putExtra("from", "profile");
                        intent.putExtra("restore_match_id", chat_match_id);
                        intent.putExtra("deletedBy", chat_deleted_by);
                        intent.putExtra("blockedBy", blocked_by);
                        intent.putExtra("isReceiveNotification", isReceiveNotification);
                        startActivity(intent);

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

    private void startOldChat(String dialog_id) {

       /* if (ChatHelper.getInstance().isLogged()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // SharedPrefsHelper.getInstance().saveChatUser(dialog_id,"false",finalReceiverImage,finalReceiverName,finalEvent_type,"");
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("dialog_id", dialog_id);
                    intent.putExtra("newDialogCreate","false");
                    intent.putExtra("receiverImage", receiverImage);
                    intent.putExtra("receiverName",receiverName);
                    intent.putExtra("receiverId",receiverId);
                    intent.putExtra("event_type",receiver_event_type);
                    intent.putExtra("match_id","");
                    intent.putExtra("from","profile");
                    intent.putExtra("restore_match_id",chat_match_id);
                    intent.putExtra("deletedBy",chat_deleted_by);
                    intent.putExtra("blockedBy",blocked_by);
                    startActivity(intent);

                   // Log.d(TAG, "ongoing: "+dialog_id+"  "+receiverName+"  "+receiverId+"  "+receiver_event_type+"  "+chat_match_id+"  "+chat_deleted_by+"  "+blocked_by);
                }
            }, 500);



            //ChatActivity.startForResult(intent,getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
        } else {
            //showProgressDialog(R.string.dlg_login);*/
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
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("dialog_id", dialog_id);
                        intent.putExtra("newDialogCreate", "false");
                        intent.putExtra("receiverImage", receiverImage);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("event_type", receiver_event_type);
                        intent.putExtra("match_id", "");
                        intent.putExtra("isBlocked", meBlocked);
                        intent.putExtra("from", "profile");
                        intent.putExtra("restore_match_id", chat_match_id);
                        intent.putExtra("deletedBy", chat_deleted_by);
                        intent.putExtra("blockedBy", blocked_by);
                        intent.putExtra("isReceiveNotification", isReceiveNotification);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        // isProcessingResultInProgress = false;
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        Log.e("saved user name", user.getLogin() + "  " + user.getPassword());
        return user;
    }

    private void sendChatAcceptRejectRequest(String status) {
        loginRegisterProfilePresenter.hitChatAccept_RejectRequest(connect_id, status);
    }

    private void sendAcceptRejectConnectRequest(String userId, String status) {
        loginRegisterProfilePresenter.hitAcceptConnectRequest(connect_id, status);
    }

    private int getItem(int i) {
        return profileVp.getCurrentItem() + i;
    }


    private void goToDashboardScreen() {

        appUtils.startActivity(context,
                DashboardActivity.createIntent(context),
                true,
                GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                false,
                0
        );
    }


}
