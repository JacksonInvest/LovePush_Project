package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.adapters.AdFeedAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.homePosts.addsmodel.DataItem;
import com.lovepushapp.model.homePosts.addsmodel.GetPostsAdResponse;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class myAddFeedListingActivity extends BaseActivity implements PostAdsMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    PostAdsPresenter adsPresenter;

    @BindView(R.id.radarRV) RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    private int last_page_pager = 1;
    private int current_page = 1;
    String searchString = "";
    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;
    private List<DataItem> dataList = new ArrayList<>();
    private List<DataItem> loveList = new ArrayList<>();
    private List<DataItem> connectList = new ArrayList<>();
    AdFeedAdapter adap;
    private final int FILTER_CODE=11;
    private String latitude = "";
    private String longitude = "";
    private boolean isFilter=false;
    String my_user_id;
    String loveConnectType = "";
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_feed_list;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.my_feed));
    }

    private Activity context = myAddFeedListingActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, myAddFeedListingActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setAdapter();
        adsPresenter = new PostAdsPresenter(context);
        adsPresenter.attachView(this);


        my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        recyclerView.setNestedScrollingEnabled(false);

        /*for list pagination*/
        if (nestedScroll != null) {

            nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (current_page < last_page_pager ){
                                progressBar.setVisibility(View.VISIBLE);
                                current_page++;

                                adsPresenter.hitGetMyAdPosts(current_page, latitude, longitude, false);

                            }


                        }
                    }, 500);

                    // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        noDataFoundTV.setVisibility(View.GONE);
        if (!isFilter){
            current_page = 1;
            dataList.clear();
            adsPresenter.hitGetMyAdPosts(current_page,latitude,longitude,true);
        }


    }

    private void setAdapter(List<DataItem> dataList) {
        loveList.clear();
        connectList.clear();
        for (DataItem item : dataList) {
            if (item.getPostType()==1){
                loveList.add(item);
            }else {
                connectList.add(item);
            }
        }



        /*if (selectedFilterType == 0){
            setBothTypesList();
        }
        if (selectedFilterType == 1){
            setLoveTypeList();
        }
        if (selectedFilterType == 2){
            setConnectTypeList();
        }*/

        if (loveConnectType.equalsIgnoreCase("0")){
            setLoveTypeList();
        }else
        if (loveConnectType.equalsIgnoreCase("1")){
            setConnectTypeList();
        }else {
            setBothTypesList();
        }



    }

    private void setConnectTypeList() {
        try {
            ArrayList<DataItem> filteredList = new ArrayList<>();

            for(DataItem s: connectList){
                if(s.getBlockedBy()==0){
                    filteredList.add(s);
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adap = new AdFeedAdapter(true,context, filteredList, (view, position, type, o) -> {

                if (type==1){
                    Toast.makeText(context, "open chat", Toast.LENGTH_SHORT).show();
                }
                else if (type==3)
                {
                    List<DataItem> list =  (List<DataItem>) o;
                    Intent intent = new Intent(context, singlePostAdView.class);
                    intent.putExtra("data", new Gson().toJson(list.get(position)));
                    startActivity(intent);

                }



            },my_user_id);
            recyclerView.setAdapter(adap);


        } catch (Exception e) {

            e.printStackTrace();
        }
        if (connectList.size() == 0){
            noDataFoundTV.setText("No data found.");
            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setLoveTypeList() {
        try {
            ArrayList<DataItem> filteredList = new ArrayList<>();

            for(DataItem s: loveList){
                if(s.getBlockedBy()==0){
                    filteredList.add(s);
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adap = new AdFeedAdapter(true,context, filteredList, (view, position, type, o) -> {
                if (type==1){
                    Toast.makeText(context, "open chat", Toast.LENGTH_SHORT).show();
                }
                else if (type==3)
                {
                    List<DataItem> list =  (List<DataItem>) o;
                    Intent intent = new Intent(context, singlePostAdView.class);
                    intent.putExtra("data", new Gson().toJson(list.get(position)));
                    startActivity(intent);

                }



            }, my_user_id);
            recyclerView.setAdapter(adap);


        } catch (Exception e) {

            e.printStackTrace();
        }
        if (loveList.size() == 0){
            noDataFoundTV.setText(getString(R.string.noDataFound));

            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setBothTypesList() {
        try {
            ArrayList<DataItem> filteredList = new ArrayList<>();

            for(DataItem s: loveList){
                if(s.getBlockedBy()==0){
                    filteredList.add(s);
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adap = new AdFeedAdapter(true,context, dataList, (view, position, type, o) -> {
                if (type==1){
                    Toast.makeText(context, "open chat", Toast.LENGTH_SHORT).show();
                }
                else if (type==3)
                {
                    List<DataItem> list =  (List<DataItem>) o;
                    Intent intent = new Intent(context, singlePostAdView.class);
                    intent.putExtra("data", new Gson().toJson(list.get(position)));
                    startActivity(intent);

                }
            }, my_user_id);
            recyclerView.setAdapter(adap);


        } catch (Exception e) {

            e.printStackTrace();
        }
        if (dataList.size() == 0){
            noDataFoundTV.setText(getString(R.string.noDataFound));
            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @OnClick({R.id.backIV, R.id.filterIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.filterIV:
               /* dialogUtils.openFilterDialog(context, selectedFilterType, new OnItemClickListenerDialog() {
                    @Override
                    public void onItemClick(View view, int type, Object o) {
                        int res = (int) o;
                        selectedFilterType = res;
                        if (selectedFilterType == 0){
                            setBothTypesList();
                        }
                        if (selectedFilterType == 1){
                            setLoveTypeList();
                        }
                        if (selectedFilterType == 2){
                            setConnectTypeList();
                        }
                    }
                });*/
                Intent intent = new Intent(context,FilterPstAdActivity.class);
                intent.putExtra("loveConnectType",loveConnectType);
                startActivityForResult(intent,FILTER_CODE);

                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case (FILTER_CODE):{
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.

//
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    loveConnectType = data.getStringExtra("loveConnectType");
                    isFilter=true;

                    current_page = 1;
                    dataList.clear();
                    adsPresenter.hitGetMyAdPosts(current_page,latitude,longitude,true);

                }
                break;
            }
        }
    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetPostsAdResponse) {
//            Log.e("RESPONSEGetUSer",new Gson().toJson(test));
            GetPostsAdResponse response = (GetPostsAdResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                progressBar.setVisibility(View.GONE);
                last_page_pager = response.getData().getLastPage();
                List<DataItem> data =  response.getData().getData();
                dataList.addAll(data);

                if (dataList.size()>0){
                    setAdapter(dataList);
                }else {
                    noDataFoundTV.setText(getString(R.string.noDataFound));
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
                Log.d("GetExploreResponse",""+dataList.toString());
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                noDataFoundTV.setText(response.getMessage());
                noDataFoundTV.setVisibility(View.VISIBLE);


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

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }


}
