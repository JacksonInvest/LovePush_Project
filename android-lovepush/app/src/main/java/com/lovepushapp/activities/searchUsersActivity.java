package com.lovepushapp.activities;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.adapters.searchedUsersAdapter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.model.searchUsers.GetSearchUsersResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class searchUsersActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV) AppCompatTextView screenTitleTV;
    @BindView(R.id.textTotalResults) AppCompatTextView textTotalResults;
    @BindView(R.id.searchET) AppCompatEditText searchET;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    @BindView(R.id.radarRV) RecyclerView usersRV;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.nestedScroll) NestedScrollView nestedScroll;
    private int last_page_pager = 1;
    private int current_page = 1;
    String searchString = "";
    List<GetSearchUsersResponse.Data.searchDatum> dataList = new ArrayList<>();
    searchedUsersAdapter searchedUsersAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, searchUsersActivity.class);
    }
    private Activity context = searchUsersActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_users;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.search_result));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textTotalResults.setVisibility(View.INVISIBLE);
        searchET.addTextChangedListener(mTextEditorWatcher);
        appUtils.hideKeyBoard(context);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);

        LinearLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        usersRV.setLayoutManager(mLayoutManager);
        usersRV.setNestedScrollingEnabled(false);
        searchedUsersAdapter = new searchedUsersAdapter(context,dataList);
        usersRV.setAdapter(searchedUsersAdapter);

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
                                textTotalResults.setText(getString(R.string.edit_search)+"...");
                                textTotalResults.setVisibility(View.VISIBLE);
                                /*Get search user with pagination*/
                                loginRegisterProfilePresenter.hitGetSearchUsers(searchString,current_page);

                            }


                        }
                    }, 500);

                    // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetSearchUsersResponse) {
            Log.e("RESPONSEGetUSer",new Gson().toJson(test));
            GetSearchUsersResponse response = (GetSearchUsersResponse) test;
//            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                progressBar.setVisibility(View.GONE);

                try {
                    if (response.getData()==null)
                    {
//                    appUtils.showToast(response.getMessage());
                        //textTotalResults.setText("No result found");
                        textTotalResults.setText(getString(R.string.noResultFound));
                        textTotalResults.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                searchedUsersAdapter.dataList.clear();
                searchedUsersAdapter.notifyDataSetChanged();
                last_page_pager = response.getData().getLastPage();
                List<GetSearchUsersResponse.Data.searchDatum> data = response.getData().getData();
                dataList.addAll(data);
                //dataList = response.getData().getData();
                textTotalResults.setText(response.getData().getTotal()+ " "+getString(R.string.resultsFound));
                textTotalResults.setVisibility(View.VISIBLE);

                searchedUsersAdapter.updateList(dataList);
                searchedUsersAdapter.notifyDataSetChanged();

                Log.d("GetExploreResponse",""+dataList.toString());
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                //textTotalResults.setText("No result found");
                textTotalResults.setText(getString(R.string.noResultFound));
                textTotalResults.setVisibility(View.VISIBLE);

            }

        }
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;

        }
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().trim().length()>=1){
                textTotalResults.setText(getString(R.string.edit_search)+"...");
                textTotalResults.setVisibility(View.VISIBLE);
            }else {
                textTotalResults.setVisibility(View.INVISIBLE);
            }
        }

        public void afterTextChanged(Editable s) {
            searchString = s.toString();
            current_page = 1;
            dataList.clear();
            searchedUsersAdapter.notifyDataSetChanged();
            //searchedUsersAdapter.updateList(dataList);
            loginRegisterProfilePresenter.hitGetSearchUsers(searchString,current_page);
        }
    };
}
