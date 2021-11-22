package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class withdrawalActivity extends BaseActivity {
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    public static Intent createIntent(Context context) {
        return new Intent(context, withdrawalActivity.class);
    }
    private Activity context = withdrawalActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_commisions;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.withdrawal));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
       /* try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            faqListAdapter adap = new faqListAdapter(context, new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {

                }
            });
            recyclerView.setAdapter(adap);
        } catch (Exception e) {

            e.printStackTrace();
        }*/

    }


    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }
    }
}
