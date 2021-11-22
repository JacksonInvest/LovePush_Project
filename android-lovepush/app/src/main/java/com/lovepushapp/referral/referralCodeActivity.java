package com.lovepushapp.referral;

import androidx.appcompat.widget.AppCompatTextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.ProfileEditActivity;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.NetworkRequests;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.referral.model.GetMyTeamResponse;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class referralCodeActivity extends BaseActivity  implements PostAdsMvp {
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.codeTV)
    AppCompatTextView codeTV;
    @BindView(R.id.circularProgressBar)
    CircularProgressBar progressBar;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.graphView)
    LinearLayout graphView;
    private GetMyTeamResponse.Data dataList ;
    int level1Count = 0,level2Count = 0,level3Count = 0,level4Count = 0,level5Count = 0;
    @BindView(R.id.txtOther) TextView txtOther;
    @BindView(R.id.txtDirect) TextView txtDirect;

    public static Intent createIntent(Context context) {
        return new Intent(context, referralCodeActivity.class);
    }
    private Activity context = referralCodeActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_referral_code;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.affiliate_overview));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
        String my_refral_code = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.referal_code, GlobalsVariables.STORAGE.TYPE_STRING));
        codeTV.setText(my_refral_code);
        PostAdsPresenter adsPresenter = new PostAdsPresenter(context);
        adsPresenter.attachView(this);

        adsPresenter.hitGetMyTeam();

    }


    @OnClick({R.id.backIV,R.id.llCode,R.id.inviteBtn})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }else if (view.getId() == R.id.llCode) {
            /*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label",codeTV.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(context, "Code copied!", Toast.LENGTH_SHORT).show();*/
           // goback();
        }else if (view.getId() == R.id.inviteBtn){
            hitCode();

        }
    }

    private void hitCode() {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();
        NetworkRequests.appLinks(new ResponseListner() {

            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.isSuccessful())
                {
                    InviteLink link=(InviteLink)response.body();
                    if (link.getStatus()) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey !\n" +
                                "Join our international Community and my team with Code " + codeTV.getText().toString() +
                                "\nLove Push - Connect the World " + "\nDownload from playstore - "+link.getAppLinks().get(0).getValue()+ "\nDownload from appstore - "+link.getAppLinks().get(1).getValue());
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                }


            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                return null;
            }
        });
    }

    private void getProgressStatus(int level1Count,int otherCount) {

        txtOther.setText(String.valueOf(otherCount));
        txtDirect.setText(String.valueOf(level1Count));
        txtTotal.setText(level1Count+otherCount+"");
        float otherCountFloat = (float) otherCount;

        float level1CountFloat = (float) level1Count;


        float presentAfterPertg = (level1CountFloat / otherCountFloat)*100;

        Log.e("PercentageAtt","present "+presentAfterPertg);
        progressBar.setProgressMax(level1Count+otherCount);
        progressBar.setProgress(otherCount);


        //Toast.makeText(mView.getContext(), "Total days "+totalWorkingDays+"\npresent "+totalPresent+"\nabsent "+totalAbsent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetMyTeamResponse) {
            Log.e("RESPONSEGetUSer",new Gson().toJson(test));
            GetMyTeamResponse response = (GetMyTeamResponse) test;

            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                dataList =  response.getData();
                if (dataList!=null){
                    progressBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    getTotalData();

                }else {
                    progressBar.setBackgroundColor(getResources().getColor(R.color.ef_grey));
                }

            }
            else
            {
                progressBar.setBackgroundColor(getResources().getColor(R.color.ef_grey));

            }

        }
    }

    private void getTotalData() {
        if (dataList.getLevel1()!=null){
            level1Count = dataList.getLevel1().size();
        }
        if (dataList.getLevel2()!=null){
            level2Count = dataList.getLevel2().size();
        }
        if (dataList.getLevel3()!=null){
            level3Count = dataList.getLevel3().size();
        }
        if (dataList.getLevel4()!=null){
            level4Count = dataList.getLevel4().size();
        }
        if (dataList.getLevel5()!=null){
            level5Count = dataList.getLevel5().size();
        }


        int others = level2Count+level3Count+level4Count+level5Count;

        getProgressStatus(level1Count,others);


    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }
}
