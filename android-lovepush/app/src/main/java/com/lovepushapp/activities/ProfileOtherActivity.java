package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.model.response.Explore.Explore;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileOtherActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.imagesRV)
    RecyclerView imagesRV;

    Explore.Data.User users2;


    List<Explore.Data.User> exploreList;
    Explore.Data data;

    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_profile_other;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.love_push));
    }

    private Activity context = ProfileOtherActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileOtherActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(ProfileOtherActivity.this);
        loginRegisterProfilePresenter.attachView(this);

        getBundleData();

//        setAdapterProfileOther();

    }

    private void getBundleData() {

        if (getIntent().getExtras() != null) {
            String user=getIntent().getStringExtra("user");

            users2=new Gson().fromJson(user, Explore.Data.User.class);
            Log.e("USER",user);
            Log.e("USERJSON", String.valueOf(new Gson().toJson(users2)));

            Toast.makeText(context, "user_id "+users2.getId(), Toast.LENGTH_SHORT).show();
        }

    }


//    private void setAdapterProfileOther() {
//        try {
//            imagesRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            ProfileImagesOtherAdapter adap = new ProfileImagesOtherAdapter(context, new OnItemClickListenerCutom() {
//                @Override
//                public void onItemClick(View view, int position, int type, Object o) {
//
//                }
//            });
//            imagesRV.setAdapter(adap);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }


    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;

        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
