package com.lovepushapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.view.GridSpacingItemDecoration;
import com.lovepushapp.R;
import com.lovepushapp.adapters.RecommendProfileAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.response.RecmmendProflie_Package.RecommendedProfile;
import com.lovepushapp.model.response.RecommendedProfileResponse;
import com.lovepushapp.model.response.UserDislikeResponse;
import com.lovepushapp.model.response.UserLikeResponse;
import com.lovepushapp.modules.RecommProfile.RecommMvp;
import com.lovepushapp.modules.RecommProfile.RecommPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class RecommendedProfileActivity extends BaseActivity implements RecommMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.recProRV)
    RecyclerView recProRV;

    RecommPresenter recommPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_recommended_profiles;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.recommended_pro));
    }

    private Activity context = RecommendedProfileActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, RecommendedProfileActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recommPresenter = new RecommPresenter(this);
        recommPresenter.attachView(this);
        recommPresenter.hitRecommendedProfile();

        Log.d("TAG", "onCreate: User id : "+ GlobalsVariables.user_id);


    }

    private void setAdapter(RecommendedProfile profile) {
        try {
            // recProRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            /*Random random = new Random();
            for (int i=0; i<profile.getData().size();i++)
            {

                int randomNum = random.nextInt((100 - 50) + 1) + 50;
                profile.getData().get(i).setPercentage(randomNum);
            }*/
            recProRV.setLayoutManager(new GridLayoutManager(this, 2));
            recProRV.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(4), true));

            RecommendProfileAdapter adap = new RecommendProfileAdapter(appUtils, context,profile , new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {


                    if (o instanceof RecommendedProfileResponse.RecommendeusersBean.DataBean) {
                        RecommendedProfileResponse.RecommendeusersBean.DataBean model = (RecommendedProfileResponse.RecommendeusersBean.DataBean) o;
                        switch (type) {
                            case 1:
                                recommPresenter.hitUserLike(model.getId() + "");
                                break;
                            case 2:
                                recommPresenter.hitUserDislike(model.getId() + "");
                                break;
                        }
                    }

                }
            });
            recProRV.setAdapter(adap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.backIV, R.id.screenTitleTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.screenTitleTV:
                break;
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof RecommendedProfile) {
            RecommendedProfile response = (RecommendedProfile) test;
            try {
                if (response.getData()==null)
                {
//                    appUtils.showToast(response.getMessage());
                    alertPopup("Seems like you have to come back later.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            setAdapter(response);
        }
        if (test instanceof UserLikeResponse) {
            UserLikeResponse likeResponse = (UserLikeResponse) test;
            appUtils.showToast(likeResponse.getMessage());
        }
        if (test instanceof UserDislikeResponse) {
            UserDislikeResponse userDislikeResponse = (UserDislikeResponse) test;
            appUtils.showToast(userDislikeResponse.getMessage());
        }

    }

    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                (dialog, id) -> {
                    dialog.cancel();
                    onBackPressed();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
