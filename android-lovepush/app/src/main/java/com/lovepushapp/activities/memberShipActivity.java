package com.lovepushapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.lovepushapp.R;
import com.lovepushapp.adapters.PaymentPagerAdapter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.model.plans.PlansEntity;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.videoTestJava.utils.Consts;
import com.lovepushapp.videoTestJava.utils.SharedPrefsHelper;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class memberShipActivity extends BaseActivity {
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.pager)
    ViewPager pager;

    public static Intent createIntent(Context context) {
        return new Intent(context, memberShipActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_ship;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.membership));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getPlans();
    }

    private void getPlans() {
        AlertDialogs dialogs = new AlertDialogs(this);
        dialogs.show();
        new LoginRegisterProfilePresenter(this).getPlans(new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.isSuccessful()) {
                    PlansEntity plansEntity = (PlansEntity) response.body();
                    SharedPrefsHelper.getInstance().savePlan(Consts.PLANS,plansEntity);
                    if (plansEntity.getStatus() == 200)
                        setAdapter();
                    else
                        Toast.makeText(memberShipActivity.this, plansEntity.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(memberShipActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(memberShipActivity.this, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private void setAdapter() {
        pager.setAdapter(new PaymentPagerAdapter(getSupportFragmentManager(), 3));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==21 && data!=null)
        {
           Boolean isDone = data.getBooleanExtra("payment",false);
           if (isDone)
           finish();
        }
    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }
    }
}