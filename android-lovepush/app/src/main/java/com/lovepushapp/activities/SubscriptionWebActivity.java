package com.lovepushapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.PlanUrl;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;

import retrofit2.Response;

public class SubscriptionWebActivity extends AppCompatActivity {
    protected WebView webView;
    ImageView backIV;
    String id = "";
    GetProfile profile;
    private AlertDialogs progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_web);
        backIV = findViewById(R.id.backIV);
        webView = findViewById(R.id.webview);
        profile = getProfileModel();
        id = getIntent().getStringExtra("id");
        progressDialog = new AlertDialogs(this);
        backIV.setOnClickListener(v -> {
            onBackPressed();
        });
        getLink();
    }

    public GetProfile getProfileModel() {
        GetProfile response = null;
        try {
            response = new Gson().fromJson((String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    GetProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void getLink() {
        progressDialog.show();
        new LoginRegisterProfilePresenter(this).subscriptionOrCancel(profile.getToken(), id, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    PlanUrl url = (PlanUrl) response.body();
                    if (url.getStatus() == 200 && url.getData() != null)
                        initUI(url.getData());
                    else
                        alertDialog(url.getMessage(), false);
                } else
                    Toast.makeText(SubscriptionWebActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(SubscriptionWebActivity.this, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    //sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());

    void alertDialog(String message, boolean b) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
            if (b) {
                Intent intent = new Intent();
                intent.putExtra("payment", true);
                setResult(21, intent);
            }
            finish();
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initUI(String data) {

        progressDialog.show();
        //webView = findViewById(R.id.webview);
        // Initialize the WebView if necessary,

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(android.webkit.WebSettings.PluginState.ON);
        webView.getSettings().setLoadsImagesAutomatically(true);

        // Load the URLs inside the WebView, not in the external web browser
        webView.setWebViewClient(new SetWebClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Load a page
        webView.loadUrl(data);


        // Attach the WebView to its placeholder
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    private void getProfile() {
        progressDialog.show();
        new LoginRegisterProfilePresenter(this).hitGetProfile(profile.getData().getId().toString(), new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetProfile en = (GetProfile) response.body();
                    en.setToken(profile.getToken());
                    new SharedStorage().setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(en));
                    alertDialog("Plan has been subscribed successfully", true);
                }

            }

            @Override
            public Void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(SubscriptionWebActivity.this, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private class SetWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            progressDialog.show();

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            progressDialog.dismiss();
            if (url.contains("return?token="))
                getProfile();

           /* if (pd.isShowing()) {
            }*/

        }

    }

}


