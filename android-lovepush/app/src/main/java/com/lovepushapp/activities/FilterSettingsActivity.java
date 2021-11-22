package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FilterSettingsActivity extends BaseActivity {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.ageBRB)
    BubbleThumbRangeSeekbar ageBRB;

    @BindView(R.id.ageTV)
    TextView ageTV;

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter_settings;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.filter));
    }

    private Activity context = FilterSettingsActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, FilterSettingsActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ageBRB.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            ageTV.setText(getString(R.string.select_age) + " (" + minValue + " - " + maxValue + ")");

        });


    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;

        }
    }
}
