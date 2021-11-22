package com.lovepushapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.adapters.ExploreAdapter;
import com.lovepushapp.adapters.ExploreRadarAdapter;
import com.lovepushapp.adapters.SwipeDeckAdapter;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.helper.GPSTracker;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.model.response.Explore.Explore;
import com.lovepushapp.model.response.Explore.ExploreRadar;
import com.lovepushapp.model.response.ExploreRadar.GetExploreRadarResponse;
import com.lovepushapp.model.response.ExploreRadar.userDatum;
import com.lovepushapp.model.response.LikeResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExploreFragment extends BaseFragment implements LoginMvp, CardStackListener {


    private static final String RIGHT = "Right";
    private static final String LEFT =  "Left";
    private static boolean isDisLikeFromBtn=false;
    private static boolean isCardSwapSourceFromLikeBtn=false;
    private static boolean isCardSwapSourceFromDeleteBtn=false;


    private String source="";

    private static  boolean is_send_right_swap_notification = false;
    private static  boolean is_send_left_swap_notification = false;


    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    int page = 1;
    private final int EXPLORE_FILTER_CODE = 11;
//
/*    @BindView(R.id.cardStack)
    SwipeDeck cardStack;*/

    SwipeDeckAdapter adapter;
    boolean isDislike=false;
    @BindView(R.id.backIV)
    ImageView backIV;
    final public static int EXPLORE_TASK_CODE = 51;
    Unbinder unbinder;


    @BindView(R.id.no_user_foundTV)
    TextView nouser_foundTV;

    private ExploreAdapter exploreAdapter;

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;

    private ArrayList<String> testData;
    private String TAG = "ExploreFragment";

    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    @BindView(R.id.swipeLL)
    LinearLayout swipeLL;
    @BindView(R.id.radarLL)
    RelativeLayout radarLL;
    @BindView(R.id.viewLineSwipe)
    View viewLineSwipe;
    @BindView(R.id.viewLineRadar)
    View viewLineRadar;
    @BindView(R.id.radarRV)
    RecyclerView radarRV;
    @BindView(R.id.radarTv)
    TextView radarTv;
    @BindView(R.id.swipeTv)
    TextView swipeTv;
    boolean scrollingEnable;
    private ExploreRadarAdapter radarAdapter;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    private int last_page_pager = 1;

    private String notiReceiverQbId = "", notiEventType;
    private boolean is_send_notification = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_explore;
    }

    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;

    private Activity context;

    private List<Explore.Data.User> exploreList;

    private Explore.Data data;
    private List<userDatum> exploreListRadar = new ArrayList<>();
    private ExploreRadar.Data dataRadar;

    private boolean isFilter = false;

    private boolean isLLSwipe = true;

    private int radarCurrentPage = 1;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    Double latitude1 = 0.0;
    Double longitude1 = 0.0;
    private String min_age = "18", max_age = "100";
    private int gender;


    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        screenTitleTV.setText(getString(R.string.explore));

        sharedStorage.setSharedData("filter_gender", GlobalsVariables.STORAGE.TYPE_STRING, null);
        sharedStorage.setSharedData("min_age", GlobalsVariables.STORAGE.TYPE_STRING, null);
        sharedStorage.setSharedData("max_age", GlobalsVariables.STORAGE.TYPE_STRING, null);

        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(getActivity());
        loginRegisterProfilePresenter.attachView(this);
        manager = new CardStackLayoutManager(getContext(), this);
        cardStackView = context.findViewById(R.id.cardStack);
        latitude = Double.valueOf(new PreferenceManager().getSharedlatitude());
        longitude = Double.valueOf(new PreferenceManager().getSharedlongitude());
        getLocation();
        testData = new ArrayList<>();
        testData.add("0");
        testData.add("1");
        testData.add("2");
        testData.add("3");
        testData.add("4");
        backIV.setVisibility(View.GONE);
        loginRegisterProfilePresenter.hitGetExplore(page, latitude, longitude);

        exploreList = new ArrayList<>();
    }

    private void initializeRadar() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        radarRV.setLayoutManager(mLayoutManager);
        radarRV.setNestedScrollingEnabled(false);
        radarAdapter = new ExploreRadarAdapter(appUtils, getContext(), exploreListRadar, (view, position, type, o) -> {
            List<userDatum> users = (List<userDatum>) o;
            ExploreRadar.Data.User users2;

            switch (type) {
                case 1:
//                        Toast.makeText(context, ""+users.get(position).getName(), Toast.LENGTH_SHORT).show();
//                        startActivity();
                    String user_id = String.valueOf(users.get(position).getId());
                    Intent intent = new Intent(new Intent(getContext(), ProfileActivity.class)).putExtra("user_id", user_id).putExtra("from", "explore");


                    startActivityForResult(intent, EXPLORE_TASK_CODE);
//                        users2=users.get(position);

            }
//                Toast.makeText(context, ""+getContext(), Toast.LENGTH_SHORT).show();
        });

        radarRV.setAdapter(radarAdapter);

        if (nestedScroll != null) {

            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("NestedScroll", "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("NestedScroll", "Scroll UP");
                    }

                    if (scrollY == 0) {
                        Log.i("NestedScroll", "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {


                        Handler handler = new Handler();
                        handler.postDelayed(() -> {

                            if (radarCurrentPage < last_page_pager) {
                                progressBar.setVisibility(View.VISIBLE);
                                radarCurrentPage++;

                                if (isFilter) {
                                    loginRegisterProfilePresenter.hitGetExploreFilterRadar(radarCurrentPage, min_age, max_age, gender, latitude, longitude);

                                } else {
                                    loginRegisterProfilePresenter.hitGetExploreRadar(radarCurrentPage, latitude, longitude);
                                }

                            }


                        }, 500);

                        // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


/*    private void manageCardData() {
        backIV.setVisibility(View.GONE);
        cardStack.setHardwareAccelerationEnabled(true);
        adapter = new SwipeDeckAdapter(cardStack,exploreList, getActivity());
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Toast.makeText(context, ""+exploreList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                Toast.makeText(context, ""+exploreList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i(TAG, "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i(TAG, "cardActionUp");
            }

        });
       *//* cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);*//*
    }*/


    @OnClick({R.id.backIV, R.id.filterIV, R.id.dislikeIV, R.id.likeIV, R.id.drivkIV, R.id.chat_RequestIV, R.id.tabLLSwipe, R.id.tabLLRadar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dislikeIV:

                isDisLikeFromBtn=true;
                isCardSwapSourceFromDeleteBtn=true;
                isDislike=true;
                sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                is_send_notification = false;
                String dislike = "0";
                loginRegisterProfilePresenter.hitLike_Dislike(exploreList.get(manager.getTopPosition()).getId(), dislike);
//                cardStack.swipeTopCardLeft(180);
                break;
            case R.id.likeIV:
                if (page == exploreAdapter.getItemCount()) {
                    progressBar.setVisibility(View.GONE);
                    exploreListRadar = new ArrayList<>();
                    noDataFoundTV.setVisibility(View.VISIBLE);
                    noDataFoundTV.setText("Data not found");
                } else {
                    isCardSwapSourceFromLikeBtn=true;
                    String like = "1";
                    is_send_notification = true;
                    notiReceiverQbId = exploreList.get(manager.getTopPosition()).getQuickblox_id();
                    notiEventType = "Like";

                    loginRegisterProfilePresenter.hitLike_Dislike(exploreList.get(manager.getTopPosition()).getId(), like);

                }
//                cardStack.swipeTopCardRight(180);

                break;
            case R.id.backIV:
                break;
            case R.id.drivkIV:
                try {
                    is_send_notification = true;
                    notiReceiverQbId = exploreList.get(manager.getTopPosition()).getQuickblox_id();
                    notiEventType = "Connect";
                    loginRegisterProfilePresenter.hitConnect(exploreList.get(manager.getTopPosition()).getId());
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              /*  CardStackLayoutManager.setCanScrollHorizontal(true);
                CardStackLayoutManager.setCanScrollVertical(true);
                sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                SwipeAnimationSetting setting3 = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting3);
                cardStackView.swipe();*/

                break;


            case R.id.filterIV:
                Intent intent = new Intent(getContext(), ExploreFilterActivity.class);
                intent.putExtra("min", min_age);
                intent.putExtra("max", max_age);
                intent.putExtra("gebder", gender);
                intent.putExtra("latitute", latitude1);
                intent.putExtra("longitute", longitude1);
                startActivityForResult(intent, EXPLORE_FILTER_CODE);

//                dialogUtils.openFilterExploreDialog(context);
                break;
            case R.id.chat_RequestIV:
//                Toast.makeText(context, ""+exploreList.get(manager.getTopPosition()).getName(), Toast.LENGTH_SHORT).show();
                //loginRegisterProfilePresenter.hitChatRequest(String.valueOf(exploreList.get(manager.getTopPosition()).getId()));
                break;
            case R.id.tabLLSwipe:
                isLLSwipe = true;
                if (exploreList.size() > 0) {

                    swipeLL.setVisibility(View.VISIBLE);
                } else {
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
                viewLineSwipe.setVisibility(View.VISIBLE);
                swipeTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                radarTv.setTextColor(getResources().getColor(R.color.chat_grey));

                radarLL.setVisibility(View.GONE);
                viewLineRadar.setVisibility(View.GONE);

                break;
            case R.id.tabLLRadar:
                isLLSwipe = false;
                swipeLL.setVisibility(View.GONE);
                if (exploreListRadar.size() == 0)
                    noDataFoundTV.setVisibility(View.VISIBLE);
                else
                    noDataFoundTV.setVisibility(View.GONE);
                viewLineSwipe.setVisibility(View.GONE);
                swipeTv.setTextColor(getResources().getColor(R.color.chat_grey));
                radarTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                radarLL.setVisibility(View.VISIBLE);
                viewLineRadar.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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

        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();


        if (notiReceiverQbId.equalsIgnoreCase("")) {
            notiReceiverQbId = "0";
        }

        userIds.add(Integer.valueOf(notiReceiverQbId));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (serviceMode.equalsIgnoreCase("request")) {
            if (!isDislike)
            alertPopup(test.toString());
            else {

                if (!is_send_left_swap_notification) {
                    CardStackLayoutManager.setCanScrollHorizontal(true);
                    CardStackLayoutManager.setCanScrollVertical(false);
                    sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                    SwipeAnimationSetting setting2 = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Left)
                            .setDuration(Duration.Normal.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting2);
                    cardStackView.swipe();
                    isDislike = false;
                }
            }
        }
        if (test instanceof Explore) {
//            Log.e("RESPONSE", new Gson().toJson(test));
            Explore response = (Explore) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                exploreList.clear();
                exploreList.addAll(response.getData().getData());
                //exploreList=response.getData().getData();

                try {
                    if (exploreList.size() > 0) {

                        noDataFoundTV.setVisibility(View.GONE);
                        if (isLLSwipe) {
                            swipeLL.setVisibility(View.VISIBLE);
                        }
                    } else {
                        noDataFoundTV.setVisibility(View.VISIBLE);
                        swipeLL.setVisibility(View.GONE);
                        noDataFoundTV.setText(response.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                data = response.getData();
                if (page == 1) {
/*
                manageCardData();
*/
                    setupCardStackView();
                }
                if (isFilter) {
                    setupCardStackView();
                } else {
                    exploreAdapter.addAll(exploreList);
                }
//                Toast.makeText(context, ""+exploreList.get(0).getName(), Toast.LENGTH_SHORT).show();

            } else {
                if (isLLSwipe) {
                    exploreList = new ArrayList<>();
//                exploreAdapter.notifyDataSetChanged();
//                exploreAdapter.addAll(exploreList);
                    //alertPopup(response.getMessage());
                    swipeLL.setVisibility(View.GONE);
                    noDataFoundTV.setText(response.getMessage());
                    noDataFoundTV.setVisibility(View.VISIBLE);

                }


            }

        }
        if (test instanceof LikeResponse) {
            LikeResponse response = (LikeResponse) test;
            if (response.getStatus()) {
                if (response.getMessage().equalsIgnoreCase("User already disliked.")) {
                    if (!isDislike)
                        alertPopup(response.getMessage());
                    else
                        isDislike=false;

                } else if(response.getMessage().equals("User disliked successfully") && isDisLikeFromBtn)
                {
                    isDisLikeFromBtn=true;
                    isCardSwapSourceFromDeleteBtn=true;
                    CardStackLayoutManager.setCanScrollHorizontal(true);
                    CardStackLayoutManager.setCanScrollVertical(false);
                    sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                    SwipeAnimationSetting setting2 = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Left)
                            .setDuration(Duration.Normal.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting2);
                    cardStackView.swipe();
                }

                else if (is_send_notification) {
                    is_send_notification=false;


                    CardStackLayoutManager.setCanScrollHorizontal(true);
                    CardStackLayoutManager.setCanScrollVertical(false);
                    sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                    SwipeAnimationSetting setting2 = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Right)
                            .setDuration(Duration.Normal.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting2);
                    cardStackView.swipe();
                    sendMsgPushNotification(notiEventType);

                }
                else if (is_send_right_swap_notification){
                    is_send_right_swap_notification=false;
                    CardStackLayoutManager.setCanScrollHorizontal(true);
                    CardStackLayoutManager.setCanScrollVertical(false);
                    sendMsgPushNotification(notiEventType);
                }
            }


        }

        if (test instanceof GetExploreRadarResponse) {
//            Log.e("RESPONSEExplore", new Gson().toJson(test));
            GetExploreRadarResponse response = (GetExploreRadarResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                if(progressBar != null)
                     progressBar.setVisibility(View.GONE);

                try {
                    if (response.getData() == null) {
//                    appUtils.showToast(response.getMessage());
                        alertPopup("Seems like you have to come back later.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                last_page_pager = response.getData().getLastPage();
                List<userDatum> data = response.getData().getData();
                exploreListRadar.addAll(data);

                if (exploreListRadar.size() > 0) {
                    initializeRadar();
                    if (!isLLSwipe)
                        noDataFoundTV.setVisibility(View.GONE);

                } else {
                    noDataFoundTV.setVisibility(View.VISIBLE);
                    noDataFoundTV.setText(response.getMessage());

                }
                Log.d("GetExploreResponse", "" + exploreListRadar.toString());

                radarAdapter.notifyDataSetChanged();
            } else {
                progressBar.setVisibility(View.GONE);
                exploreListRadar = new ArrayList<>();
                noDataFoundTV.setVisibility(View.VISIBLE);
                noDataFoundTV.setText(response.getMessage());
                //alertPopup(response.getMessage());

            }

        }
    }


    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                (dialog, id) -> {
                    dialog.cancel();
                    if (s.equalsIgnoreCase("User has disabled date request.")) {
                        CardStackLayoutManager.setCanScrollHorizontal(true);
                        CardStackLayoutManager.setCanScrollVertical(false);
                        sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                        SwipeAnimationSetting setting2 = new SwipeAnimationSetting.Builder()
                                .setDirection(Direction.Right)
                                .setDuration(Duration.Normal.duration)
                                .setInterpolator(new AccelerateInterpolator())
                                .build();
                        manager.setSwipeAnimationSetting(setting2);
                        cardStackView.swipe();
                    } else if (s.equalsIgnoreCase("User already disliked.")) {
                        CardStackLayoutManager.setCanScrollHorizontal(true);
                        CardStackLayoutManager.setCanScrollVertical(false);
                        sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
                        SwipeAnimationSetting setting2 = new SwipeAnimationSetting.Builder()
                                .setDirection(Direction.Left)
                                .setDuration(Duration.Normal.duration)
                                .setInterpolator(new AccelerateInterpolator())
                                .build();
                        manager.setSwipeAnimationSetting(setting2);
                        cardStackView.swipe();

                    }
//                        context.onBackPressed();
//                        onBackPressed();
                    //context.startActivity(new Intent(getContext(), DashboardActivity.class));
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public <T> void onApiError(T test) {
        Toast.makeText(context, test.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {


    }

    @Override
    public void onCardSwiped(Direction direction) {
//        Toast.makeText(context, ""+direction+" pos "+manager.getTopPosition(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "list size"+exploreList.size(), Toast.LENGTH_SHORT).show();
//        Log.e("SIZELIST", String.valueOf(exploreList.size()));
//        Log.e("SIZEITEM", String.valueOf(exploreAdapter.getItemCount()));
//        Log.e("SIZEPOSITION", String.valueOf(manager.getTopPosition()));


        Log.e("DIRECTION", String.valueOf(direction));
        String dir = String.valueOf(direction);

        if (manager.getTopPosition() == exploreAdapter.getItemCount()) {
            page++;
            paginate();
        }

            if (page == exploreAdapter.getItemCount()) {
                is_send_notification = false;
                progressBar.setVisibility(View.GONE);
                exploreListRadar = new ArrayList<>();
                noDataFoundTV.setVisibility(View.VISIBLE);
                noDataFoundTV.setText("Data not found");
            }
            else {

                if(dir.equalsIgnoreCase(RIGHT) && !isCardSwapSourceFromLikeBtn ){
                 String like = "1";
                is_send_right_swap_notification = true;
                is_send_notification = false;
                notiReceiverQbId = exploreList.get(manager.getTopPosition()-1).getQuickblox_id();
                notiEventType = "Like";
                loginRegisterProfilePresenter.hitLike_Dislike(exploreList.get(manager.getTopPosition()-1).getId(), like);
        }

        else if(dir.equalsIgnoreCase(LEFT) && !isCardSwapSourceFromDeleteBtn){
            isCardSwapSourceFromDeleteBtn=false;
            isDisLikeFromBtn=false;
            is_send_left_swap_notification=false;
            is_send_notification=false;
            sharedStorage.setSharedData("showDialogs", GlobalsVariables.STORAGE.TYPE_STRING, "no");
            is_send_notification = false;
            String dislike = "0";
            loginRegisterProfilePresenter.hitLike_Dislike(exploreList.get(manager.getTopPosition()-1).getId(), dislike);
        }

        isCardSwapSourceFromLikeBtn=false;
        isCardSwapSourceFromDeleteBtn=false;
        CardStackLayoutManager.setCanScrollHorizontal(true);
        CardStackLayoutManager.setCanScrollVertical(false);

    }

    }

    private void paginate() {
        loginRegisterProfilePresenter.hitGetExploreFilter(1, min_age, max_age, gender, latitude1, longitude1);

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    private void setupCardStackView() {
        initialize();
    }

    private void initialize() {
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        CardStackLayoutManager.setCanScrollHorizontal(true);
        CardStackLayoutManager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
//        cardStackView.layoutManager = manager;
        cardStackView.setLayoutManager(manager);

        manager.setStackFrom(StackFrom.Bottom);
        manager.setVisibleCount(5);
        manager.setTranslationInterval(4.0f);

//        cardStackView.adapter = adapter;
//        nouser_foundTV.setVisibility(View.GONE);
        if (exploreAdapter != null) {

            exploreAdapter.addAll(exploreList);
            exploreAdapter.notifyDataSetChanged();
        } else {
            exploreAdapter = new ExploreAdapter(getContext(), exploreList, (view, position, type, o) -> {
                List<Explore.Data.User> users = (List<Explore.Data.User>) o;
                Explore.Data.User users2;

                switch (type) {
                    case 1:
//                        Toast.makeText(context, ""+users.get(position).getName(), Toast.LENGTH_SHORT).show();
//                        startActivity();
                        String user_id = String.valueOf(users.get(position).getId());
                        Intent intent = new Intent(new Intent(getContext(), ProfileActivity.class)).putExtra("user_id", user_id).putExtra("from", "explore");
                        startActivityForResult(intent, EXPLORE_TASK_CODE);
//                        users2=users.get(position);

                }
//                Toast.makeText(context, ""+getContext(), Toast.LENGTH_SHORT).show();
            });
        }

        cardStackView.setAdapter(exploreAdapter);
/*        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }*/
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        if (!isFilter) {
            loginRegisterProfilePresenter.hitGetExplore(page, latitude, longitude);
            exploreListRadar.clear();
            //  radarAdapter.notifyDataSetChanged();
            radarCurrentPage = 1;
            loginRegisterProfilePresenter.hitGetExploreRadar(radarCurrentPage, latitude, longitude);

        }/*else
            {
                loginRegisterProfilePresenter.hitGetExploreFilter(1,min_age,max_age,gender,latitude1,longitude1);
            }*/


           /*else {
            exploreListRadar.clear();
            radarAdapter.notifyDataSetChanged();
            radarCurrentPage = 1;
            loginRegisterProfilePresenter.hitGetExploreFilterRadar(radarCurrentPage,min_age,max_age,gender,latitude,longitude);
        }*/


        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (EXPLORE_FILTER_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.

//                    String returnValue = data.getStringExtra("some_key");
                    min_age = data.getStringExtra("min_age");
                    max_age = data.getStringExtra("max_age");
                    gender = data.getIntExtra("gender", 0);
                    latitude1 = data.getDoubleExtra("latitude", 0.0);
                    longitude1 = longitude = data.getDoubleExtra("longitude", 0.0);
                    isFilter=true;
                    FilterExplore(gender, min_age, max_age, latitude1, longitude1);
                    FilterExploreRadar(gender, min_age, max_age, latitude1, longitude1);
                }
                break;
            }
            case EXPLORE_TASK_CODE: {
                if (data != null) {

                    if (!isFilter) {
                        loginRegisterProfilePresenter.hitGetExplore(page, latitude, longitude);
                        exploreListRadar.clear();
                        //  radarAdapter.notifyDataSetChanged();
                        radarCurrentPage = 1;
                        loginRegisterProfilePresenter.hitGetExploreRadar(radarCurrentPage, latitude, longitude);

                    } else if (data.getBooleanExtra("explore", false)) {
                        paginate();
                    }

                }
            }

        }
    }

    private void FilterExploreRadar(int gender, String min_age, String max_age, Double latitude1, Double longitude1) {
        exploreListRadar.clear();
        radarAdapter.notifyDataSetChanged();
        radarCurrentPage = 1;
        loginRegisterProfilePresenter.hitGetExploreFilterRadar(radarCurrentPage, min_age, max_age, gender, latitude1, longitude1);
    }

    private void FilterExplore(int gender, String min_age, String max_age, Double latitude1, Double longitude1) {

        //for api
        loginRegisterProfilePresenter.hitGetExploreFilter(1, min_age, max_age, gender, latitude1, longitude1);
    }


    private void getLocation() {
        try {
            GPSTracker gps = new GPSTracker(context);
            // check if GPS location can get

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (latitude == 0.0 && longitude == 0.0) {
                latitude = Double.valueOf(new PreferenceManager().getSharedlatitude());
                longitude = Double.valueOf(new PreferenceManager().getSharedlongitude());
            } else {
                String address = "";
                if (addresses!=null) {
                    if (addresses.size() > 0) {
                        if (addresses.get(0).getAddressLine(0) != null) {
                            address = addresses.get(0).getAddressLine(0);
                        }
                        new PreferenceManager().saveLocation(String.valueOf(latitude), String.valueOf(longitude), address);
                    }
                }
            }


       /* String address = addresses.get(0).getAddressLine(0);

        Log.e("Address",""+addresses +" "+gps.getLatitude()+" "+gps.getLongitude());

        if (!address.equals("")){
            new PreferenceManager().saveLocation(String.valueOf(gps.getLatitude()),String.valueOf(gps.getLongitude()),address);

        }else {

        }
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
*/

            if (gps.canGetLocation()) {
                gps.getLocation();

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private boolean CheckGpsStatus() {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}

