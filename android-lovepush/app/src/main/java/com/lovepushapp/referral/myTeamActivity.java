package com.lovepushapp.referral;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.referral.adapter.myTeamAdapter;
import com.lovepushapp.referral.model.GetMyTeamResponse;
import com.lovepushapp.referral.model.Level;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class myTeamActivity extends BaseActivity implements PostAdsMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    PostAdsPresenter adsPresenter;

    private GetMyTeamResponse.Data dataList ;
    @BindView(R.id.level1TV) TextView level1TV;
    private boolean imagelevel1 = false;
    @BindView(R.id.level2TV) TextView level2TV;
    private boolean imagelevel2 = false;
    @BindView(R.id.level3TV) TextView level3TV;
    private boolean imagelevel3 = false;
    @BindView(R.id.level4TV) TextView level4TV;
    private boolean imagelevel4 = false;
    @BindView(R.id.level5TV)
    TextView level5TV;
    private boolean imagelevel5 = false;
    private Activity context = myTeamActivity.this;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;
    private List<Level> proceessedDataList = new ArrayList<>();
    int selectedType = 1;


    public static Intent createIntent(Context context) {
        return new Intent(context, myTeamActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsPresenter = new PostAdsPresenter(context);
        adsPresenter.attachView(this);

        adsPresenter.hitGetMyTeam();

        initilize();

    }

    private void initilize() {
        level1TV.setText(getString(R.string.level)+" 1");
        level2TV.setText(getString(R.string.level)+" 2");
        level3TV.setText(getString(R.string.level)+" 3");
        level4TV.setText(getString(R.string.level)+" 4");
        level5TV.setText(getString(R.string.level)+" 5");

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.my_team));
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetMyTeamResponse) {
            Log.e("RESPONSEGetUSer",new Gson().toJson(test));
            GetMyTeamResponse response = (GetMyTeamResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                /*progressBar.setVisibility(View.GONE);
                last_page_pager = response.getData().getLastPage();
                List<GetMyTeamResponse.Data> data =  response.getData();
                dataList.addAll(data);

                if (dataList.size()>0){

                    setAdapter(dataList);
                }else {
                    noDataFoundTV.setText("No data found.");
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }
                Log.d("GetExploreResponse",""+dataList.toString());*/
                dataList =  response.getData();
                if (dataList!=null){
                    noDataFoundTV.setVisibility(View.GONE);

                    setAdapter();
                }else {
                    noDataFoundTV.setText(getString(R.string.noDataFound));
                    noDataFoundTV.setVisibility(View.VISIBLE);
                }

            }
            else
            {
                /*progressBar.setVisibility(View.GONE);
                noDataFoundTV.setText(response.getMessage());
                noDataFoundTV.setVisibility(View.VISIBLE);*/
                noDataFoundTV.setText(response.getMessage());
                noDataFoundTV.setVisibility(View.VISIBLE);


            }

        }
    }

    @OnClick({R.id.backIV, R.id.level1TV, R.id.level2TV,R.id.level3TV, R.id.level4TV,R.id.level5TV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.level1TV:
                selectedType = 1;
                activeLevel1();
                showLevelData(1);
                break;
            case R.id.level2TV:
                selectedType = 2;
                activeLevel2();
                showLevelData(2);
                //Toast.makeText(getActivity(),""+imageFlatSelection, Toast.LENGTH_SHORT).show();
                break;
            case R.id.level3TV:
                selectedType = 3;
                activeLevel3();
                showLevelData(3);
                //Toast.makeText(getActivity(),""+imagePlotsSelection, Toast.LENGTH_SHORT).show();
                break;
            case R.id.level4TV:
                selectedType = 4;
                activeLevel4();
                showLevelData(4);
                //Toast.makeText(getActivity(),""+imagePentHouseSelection, Toast.LENGTH_SHORT).show();
                break;
            case R.id.level5TV:
                selectedType = 5;
                activeLevel5();
                showLevelData(5);
                //  Toast.makeText(getActivity(),""+imageFarmHouseSelection, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void showLevelData(int type) {
        switch (type){
            case 1:
                if (dataList.getLevel1()!=null && dataList.getLevel1().size()>0){
                    proceessedDataList = dataList.getLevel1();
                }
                break;
            case 2:
                if (dataList.getLevel2()!=null && dataList.getLevel2().size()>0){
                    proceessedDataList = dataList.getLevel2();
                }
                break;
            case 3:
                if (dataList.getLevel3()!=null && dataList.getLevel3().size()>0 ){
                    proceessedDataList =dataList.getLevel3();
                }
                break;
            case 4:
                if (dataList.getLevel4()!=null && dataList.getLevel4().size()>0){
                    proceessedDataList = dataList.getLevel4();
                }
                break;
            case 5:
                if (dataList.getLevel5()!=null && dataList.getLevel5().size()>0){
                    proceessedDataList = dataList.getLevel5();
                }
                break;
        }


        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            myTeamAdapter adap = new myTeamAdapter(proceessedDataList,context, new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    List<Level> users= (List<Level>) o;
                    Level users2;

                    switch (type)
                    {
                        case 1:
//                        Toast.makeText(context, ""+users.get(position).getName(), Toast.LENGTH_SHORT).show();
//                        startActivity();
                            String user_id= String.valueOf(users.get(position).getId());
                            startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id",user_id).putExtra("from","explore"));
//                        users2=users.get(position);

                    }

                }
            });
            recyclerView.setAdapter(adap);

        } catch (Exception e) {

            e.printStackTrace();
        }
        if (proceessedDataList.size() == 0){
            noDataFoundTV.setText(getString(R.string.noDataFound));
            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void activeLevel5() {
        if(imagelevel5) {
            if (selectedType != 5) {

                level5TV.setBackgroundResource(R.drawable.level_unfill);
                level5TV.setTextColor(Color.parseColor("#999999"));
                imagelevel5 = false;
            }
        } else {
            level5TV.setBackgroundResource(R.drawable.level_filled);
            level5TV.setTextColor(Color.parseColor("#ffffff"));
            imagelevel5=true;
            //layoutBedRooms.setVisibility(View.VISIBLE);
            level1TV.setBackgroundResource(R.drawable.level_unfill);
            level1TV.setTextColor(Color.parseColor("#999999"));
            imagelevel1=false;

            level2TV.setBackgroundResource(R.drawable.level_unfill);
            level2TV.setTextColor(Color.parseColor("#999999"));
            imagelevel2=false;

            level3TV.setBackgroundResource(R.drawable.level_unfill);
            level3TV.setTextColor(Color.parseColor("#999999"));
            imagelevel3=false;

            level4TV.setBackgroundResource(R.drawable.level_unfill);
            level4TV.setTextColor(Color.parseColor("#999999"));
            imagelevel4=false;
        }
    }

    private void activeLevel4() {

        if (imagelevel4) {
            if (selectedType != 4) {
                level4TV.setBackgroundResource(R.drawable.level_unfill);
                level4TV.setTextColor(Color.parseColor("#999999"));
                imagelevel4 = false;
            }
        } else {
            level4TV.setBackgroundResource(R.drawable.level_filled);
            level4TV.setTextColor(Color.parseColor("#ffffff"));
            imagelevel4 = true;
            //layoutBedRooms.setVisibility(View.VISIBLE);
            level1TV.setBackgroundResource(R.drawable.level_unfill);
            level1TV.setTextColor(Color.parseColor("#999999"));
            imagelevel1 = false;

            level2TV.setBackgroundResource(R.drawable.level_unfill);
            level2TV.setTextColor(Color.parseColor("#999999"));
            imagelevel2 = false;

            level3TV.setBackgroundResource(R.drawable.level_unfill);
            level3TV.setTextColor(Color.parseColor("#999999"));
            imagelevel3 = false;

            level5TV.setBackgroundResource(R.drawable.level_unfill);
            level5TV.setTextColor(Color.parseColor("#999999"));
            imagelevel5 = false;
        }
    }

    private void activeLevel3() {
        if (imagelevel3) {
            if (selectedType != 3) {

                level3TV.setBackgroundResource(R.drawable.level_unfill);
                level3TV.setTextColor(Color.parseColor("#999999"));
                imagelevel3 = false;
            }
        } else {
            level3TV.setBackgroundResource(R.drawable.level_filled);
            level3TV.setTextColor(Color.parseColor("#ffffff"));
            imagelevel3 = true;
            //layoutBedRooms.setVisibility(View.VISIBLE);
            level1TV.setBackgroundResource(R.drawable.level_unfill);
            level1TV.setTextColor(Color.parseColor("#999999"));
            imagelevel1 = false;

            level2TV.setBackgroundResource(R.drawable.level_unfill);
            level2TV.setTextColor(Color.parseColor("#999999"));
            imagelevel2 = false;

            level4TV.setBackgroundResource(R.drawable.level_unfill);
            level4TV.setTextColor(Color.parseColor("#999999"));
            imagelevel4 = false;

            level5TV.setBackgroundResource(R.drawable.level_unfill);
            level5TV.setTextColor(Color.parseColor("#999999"));
            imagelevel5 = false;
        }

    }

    private void activeLevel2() {
        if (imagelevel2) {
            if (selectedType != 2) {

                level2TV.setBackgroundResource(R.drawable.level_unfill);
                level2TV.setTextColor(Color.parseColor("#999999"));
                imagelevel2 = false;
            }
        } else {
            level2TV.setBackgroundResource(R.drawable.level_filled);
            level2TV.setTextColor(Color.parseColor("#ffffff"));
            imagelevel2 = true;
            //layoutBedRooms.setVisibility(View.VISIBLE);
            level1TV.setBackgroundResource(R.drawable.level_unfill);
            level1TV.setTextColor(Color.parseColor("#999999"));
            imagelevel1 = false;

            level3TV.setBackgroundResource(R.drawable.level_unfill);
            level3TV.setTextColor(Color.parseColor("#999999"));
            imagelevel3 = false;

            level4TV.setBackgroundResource(R.drawable.level_unfill);
            level4TV.setTextColor(Color.parseColor("#999999"));
            imagelevel4 = false;
            level5TV.setBackgroundResource(R.drawable.level_unfill);
            level5TV.setTextColor(Color.parseColor("#999999"));
            imagelevel5 = false;
        }
    }

    private void activeLevel1() {
        if(imagelevel1) {
            if (selectedType != 1) {
                level1TV.setBackgroundResource(R.drawable.level_unfill);
                level1TV.setTextColor(Color.parseColor("#999999"));
                imagelevel1 = false;
            }
        } else {
            level1TV.setBackgroundResource(R.drawable.level_filled);
            level1TV.setTextColor(Color.parseColor("#ffffff"));
            imagelevel1=true;
            //layoutBedRooms.setVisibility(View.VISIBLE);
            level2TV.setBackgroundResource(R.drawable.level_unfill);
            level2TV.setTextColor(Color.parseColor("#999999"));
            imagelevel2=false;

            level3TV.setBackgroundResource(R.drawable.level_unfill);
            level3TV.setTextColor(Color.parseColor("#999999"));
            imagelevel3=false;

            level4TV.setBackgroundResource(R.drawable.level_unfill);
            level4TV.setTextColor(Color.parseColor("#999999"));
            imagelevel4=false;

            level5TV.setBackgroundResource(R.drawable.level_unfill);
            level5TV.setTextColor(Color.parseColor("#999999"));
            imagelevel5=false;
        }

    }

    private void setAdapter() {

        if (dataList.getLevel1()!=null && dataList.getLevel1().size()>0){
           // imagelevel1=true;
            level1TV.setVisibility(View.VISIBLE);
            activeLevel1();
            showLevelData(1);
        }
        if (dataList.getLevel2()!=null && dataList.getLevel2().size()>0){
            level2TV.setVisibility(View.VISIBLE);
        }
        if (dataList.getLevel3()!=null && dataList.getLevel3().size()>0){
            level3TV.setVisibility(View.VISIBLE);
        }
        if (dataList.getLevel4()!=null && dataList.getLevel4().size()>0){
            level4TV.setVisibility(View.VISIBLE);
        }
        if (dataList.getLevel5()!=null && dataList.getLevel5().size()>0){
            level5TV.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
