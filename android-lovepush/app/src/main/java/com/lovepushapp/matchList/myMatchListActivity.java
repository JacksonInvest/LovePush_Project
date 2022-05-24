package com.lovepushapp.matchList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.matchList.adapter.connectMatchAdapter;
import com.lovepushapp.matchList.adapter.loveMatchAdapter;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.lovepushapp.model.response.blockUserResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class myMatchListActivity  extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV) AppCompatTextView screenTitleTV;
    @BindView(R.id.radarRV)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    private int last_page_pager = 1;
    private int current_page = 1;
    String searchString = "";
    @BindView(R.id.noDataFoundTV)
    AppCompatTextView noDataFoundTV;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    List<UserChatList.Data> dataList = new ArrayList<>();
    List<UserChatList.Data> loveMatchList = new ArrayList<>();
    List<UserChatList.Data> connectMatchList = new ArrayList<>();
    private boolean isLLLove = true;


    @BindView(R.id.viewLineLove) View viewLineLove;
    @BindView(R.id.viewLineConnect) View viewLineConnect;

    public static Intent createIntent(Context context) {
        return new Intent(context, myMatchListActivity.class);
    }
    private Activity context = myMatchListActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_match_list;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.match_list));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);

        recyclerView.setNestedScrollingEnabled(false);


        if (nestedScroll != null) {

            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (current_page < last_page_pager ){
                                    progressBar.setVisibility(View.VISIBLE);
                                    current_page++;

                                    /*Pagination hit get match api*/
                                    getMatches();


                                }


                            }
                        }, 500);

                        // Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void getMatches() {
        AlertDialogs dialogs=new AlertDialogs(this);
        //dialogs.show();
        loginRegisterProfilePresenter.hitGetMyMatches1(current_page, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
               // dialogs.dismiss();
                UserChatList userChatList;
                Log.e("RESPONSEMESSAGEF",response.body().toString());
                if (response.body() instanceof UserChatList)
                {
                    userChatList=(UserChatList) response.body();

                    List<UserChatList.Data> data = userChatList.getData();
                    dataList.addAll(data);
                    if (dataList.size()>0) {
                        setAdapterRecent();
                        noDataFoundTV.setVisibility(View.GONE);
                    }else {
                        noDataFoundTV.setVisibility(View.VISIBLE);
                    }

                    Log.d("GetMessageResponse",""+ dataList.toString());

//            Toast.makeText(getContext(), ""+userChatList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public Void onError(String message) {
               // dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //noDataFoundTV.setVisibility(View.GONE);
        current_page = 1;
        dataList.clear();
        getMatches();


    }



    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        UserChatList userChatList;
        Log.e("RESPONSEMESSAGEF",serviceMode);
        if (test instanceof UserChatList)
        {
            userChatList=(UserChatList) test;

            List<UserChatList.Data> data = userChatList.getData();
            dataList.addAll(data);
            if (dataList.size()>0) {
                setAdapterRecent();
                noDataFoundTV.setVisibility(View.GONE);
            }else {
                noDataFoundTV.setVisibility(View.VISIBLE);
            }

            Log.d("GetMessageResponse",""+ dataList.toString());

//            Toast.makeText(getContext(), ""+userChatList.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (test instanceof blockUserResponse){
            blockUserResponse res = (blockUserResponse) test;
            noDataFoundTV.setVisibility(View.GONE);
            current_page = 1;
            dataList.clear();
            getMatches();
        }
    }

    private void setAdapterRecent() {

        loveMatchList.clear();
        connectMatchList.clear();
        for (UserChatList.Data item : dataList) {
            if (item.getEvent_type().equals("L")){
                loveMatchList.add(item);

            }
            if (item.getEvent_type().equals("C")){
                connectMatchList.add(item);

            }
        }

        if (isLLLove){
            setLoveAdapter();
        }else {
            setConnectAdapter();
        }
    }

    @SuppressLint("WrongConstant")
    private void setConnectAdapter() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            connectMatchAdapter adap = new connectMatchAdapter(connectMatchList,context, (view, position, type, o) -> {
                List<UserChatList.Data> list = (List<UserChatList.Data>) o;
                String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
                String receiver_user_id = "";
                if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info2().getId())) {
                    receiver_user_id = list.get(position).getUser_info1().getId();
                }else if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info1().getId()))
                {
                    receiver_user_id = list.get(position).getUser_info2().getId();


                }
                if (type == 1) {

                  unblock(receiver_user_id);
                }

                if (type == 2){
                    context.startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id", receiver_user_id).putExtra("from", "explore"));

                }

                if (type == 3){
                    unmatch(list.get(position).getId());
                }

            });
            recyclerView.setAdapter(adap);

        } catch (Exception e) {

            e.printStackTrace();
        }
        if (connectMatchList.size() == 0){
            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void unblock(String receiver_user_id) {
        String finalReceiver_user_id = receiver_user_id;
        new AlertDialog.Builder(context)
                .setTitle("Love Push")
                .setMessage("Are you sure, you want to unblock this person?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //Toast.makeText(getContext(), ""+deleteNeededDialogId+"  pos "+deleteNeededPostion, Toast.LENGTH_SHORT).show();

                    //loginRegisterProfilePresenter.hit_delete_match(deleteNeededMAtchId);
                    AlertDialogs dialogs=new AlertDialogs(myMatchListActivity.this);
                    dialogs.show();
                    loginRegisterProfilePresenter.hit_block_user("unblock", finalReceiver_user_id, new ResponseListner() {
                        @Override
                        public <T> void onComplete(Response<T> response) {

                            dialogs.dismiss();
                            if (response.isSuccessful())
                            {
                                blockUserResponse res = (blockUserResponse) response.body();
                                noDataFoundTV.setVisibility(View.GONE);
                                current_page = 1;
                                dataList.clear();
                                getMatches();
                            }
                        }

                        @Override
                        public Void onError(String message) {
                            dialogs.dismiss();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });//block, unblock

                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void unmatch(String matchId) {
        new AlertDialog.Builder(context)
                .setTitle("Love Push")
                .setMessage("Are you sure, you want to un-match this person?")

                .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                    AlertDialogs dialogs=new AlertDialogs(myMatchListActivity.this);
                    dialogs.show();
                    loginRegisterProfilePresenter.hit_unmatch_user( matchId, new ResponseListner() {
                        @Override
                        public <T> void onComplete(Response<T> response) {

                            dialogs.dismiss();
                            if (response.isSuccessful())
                            {
                                noDataFoundTV.setVisibility(View.GONE);
                                current_page = 1;
                                dataList.clear();
                                getMatches();
                            }
                        }

                        @Override
                        public Void onError(String message) {
                            dialogs.dismiss();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });

                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    @SuppressLint("WrongConstant")
    private void setLoveAdapter() {
        if (loveMatchList.size() == 0){
            noDataFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noDataFoundTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            loveMatchAdapter adap = new loveMatchAdapter(loveMatchList,context, (view, position, type, o) -> {

                List<UserChatList.Data> list = (List<UserChatList.Data>) o;

                String my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
                String receiver_user_id = "";
                if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info2().getId())) {
                    receiver_user_id = list.get(position).getUser_info1().getId();
                }else if (my_user_id.equalsIgnoreCase(list.get(position).getUser_info1().getId()))
                {
                    receiver_user_id = list.get(position).getUser_info2().getId();


                }
                if (type == 1) {
                    unblock(receiver_user_id);
                }

                if (type == 2){
                    context.startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id", receiver_user_id).putExtra("from", "explore"));
                }
                if (type == 3){
                    unmatch(list.get(position).getId());
                }

            });
            recyclerView.setAdapter(adap);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }



    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }



    @OnClick({R.id.backIV,R.id.tabLLLove,R.id.tabLLConnect})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.backIV:
                goback();
                break;
            case R.id.tabLLLove:
                isLLLove = true;
                viewLineLove.setVisibility(View.VISIBLE);
                viewLineConnect.setVisibility(View.GONE);
                setLoveAdapter();
                break;
            case R.id.tabLLConnect:
                isLLLove = false;
                viewLineLove.setVisibility(View.GONE);
                viewLineConnect.setVisibility(View.VISIBLE);
                setConnectAdapter();
                break;
        }
    }


}
