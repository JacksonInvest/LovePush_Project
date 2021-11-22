package com.lovepushapp.activities;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lovepushapp.R;
import com.lovepushapp.adapters.faqHeaderAdapter;
import com.lovepushapp.adapters.faqListAdapter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.FAQModel.GetFaqResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class faqActivity extends BaseActivity implements LoginMvp {
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerViewHeader)
    RecyclerView recyclerViewHeader;

    @BindView(R.id.noDataFoundTV) AppCompatTextView noDataFoundTV;

    private List<GetFaqResponse.Faq> dataList = new ArrayList<>();
    private List<GetFaqResponse.Faq.QsnList> qusDataList = new ArrayList<>();

    private faqHeaderAdapter adapHeader;
    private faqListAdapter adapQustions;
    public static Intent createIntent(Context context) {
        return new Intent(context, faqActivity.class);
    }
    private Activity context = faqActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_faq;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.faq));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginRegisterProfilePresenter loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(context);
        loginRegisterProfilePresenter.attachView(this);

        String current_locale = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING);

        String lang_id = "1";
        if (current_locale.equalsIgnoreCase("de"))
        {
         lang_id = "2";
        }

        loginRegisterProfilePresenter.hitGetFAQ(lang_id);


    }

    private void setAdapter() {
        for (int i = 0; i < dataList.size(); i++) {
            if (i == 0){
                dataList.get(i).setSelected(true);
            }else {
                dataList.get(i).setSelected(false);

            }
        }

        try {
            recyclerViewHeader.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            adapHeader = new faqHeaderAdapter(context,dataList, (view, position, type, o) -> {
                List<GetFaqResponse.Faq> list = (List<GetFaqResponse.Faq>) o;;
                for (int i = 0; i < dataList.size(); i++) {
                    if (i == position){
                        dataList.get(i).setSelected(true);
                    }else {
                        dataList.get(i).setSelected(false);

                    }
                }
                adapHeader.notifyDataSetChanged();

                setQuestionAdapter(position);



            });
            recyclerViewHeader.setAdapter(adapHeader);
        } catch (Exception e) {

            e.printStackTrace();
        }

        setQuestionAdapter(0);

    }

    private void setQuestionAdapter(int position) {

        qusDataList = dataList.get(position).getQsnList();
        for (int i = 0; i < qusDataList.size(); i++) {
            qusDataList.get(i).setShowAns(false);
        }
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapQustions = new faqListAdapter(context,qusDataList, (view, pos, type, o) -> {
                if (qusDataList.get(pos).isShowAns()) {
                    qusDataList.get(pos).setShowAns(false);
                }else {
                    qusDataList.get(pos).setShowAns(true);
                }
                adapQustions.notifyDataSetChanged();
            });
            recyclerView.setAdapter(adapQustions);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetFaqResponse)
        {

            GetFaqResponse res =(GetFaqResponse) test;
            /*List<GetFaqResponse.Faq> data = res.getFaq();
            for (GetFaqResponse.Faq item: data){
                if (item.)
            }*/
            dataList=res.getFaq();
            if (dataList.size()>0) {




                setAdapter();
                noDataFoundTV.setVisibility(View.GONE);
            }else {
                noDataFoundTV.setText(getString(R.string.noMsgYet));
                noDataFoundTV.setVisibility(View.VISIBLE);

            }

            Log.d("GetMessageResponse",""+dataList.toString());

//            Toast.makeText(getContext(), ""+userChatList.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
