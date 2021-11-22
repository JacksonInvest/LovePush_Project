package com.lovepushapp.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.DashboardActivity;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.model.response.Social_login_model.SocialLoginModel;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.ApiHitter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishal on 6/5/19.
 */

public class InstagramLogin_Dialog implements LoginMvp {
    Context context;
    String id;
    String token;
    String access_token;
    LoginRegisterProfilePresenter presenter;

    private final String redirect_url;
    private final String request_url;

    public InstagramLogin_Dialog(Context context) {
        this.context = context;


        this.redirect_url = context.getResources().getString(R.string.callback_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&scope=user_profile";

        presenter = new LoginRegisterProfilePresenter(((AppCompatActivity) context));
        presenter.attachView(this);

        showdialog();
    }

    Dialog dialog;

    private void showdialog() {
        dialog = new Dialog(context, R.style.Custom_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.instagram_login_dialog);
        WebView myWebView = (WebView) dialog.findViewById(R.id.webview);
        ProgressBar progressbar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        TextView textView = dialog.findViewById(R.id.dialog_toolbartxt);
        ImageView back = dialog.findViewById(R.id.insta_backArrow);

        back.setOnClickListener(v -> dialog.dismiss());

        myWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        }
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);

        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView.clearFormData();
        Log.e("showdialog: ", request_url);
        myWebView.loadUrl(request_url);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setWebViewClient(new AppWebViewClients(progressbar));

        //myWebView.setLayoutParams(FILL);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof SocialLoginModel) {

            SocialLoginModel res = (SocialLoginModel) test;
            setSnackbar(dialog.getCurrentFocus(), res.getMessage());

            /*if (res.getData().getIsProfileComplete() == 0) {
                new Social_AcceptDetail_Dialog(context,"4");
            } else {

                gotoDashBoard();
            }
*/
            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                if (res.getData().getIsProfileComplete() == 0) {
                    new Social_AcceptDetail_Dialog(context, "4", res.getData().getEmail());
                } else {
                    gotoDashBoard();
                }

            } else {
                Toast.makeText(context, "" + res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Log.e("onApiResponse: ", res.getMessage());

        }


    }

    @Override
    public <T> void onApiError(T test) {
    }

    @Override
    public void onException(Exception e) {
    }

    private void gotoDashBoard() {


        Intent intent = new Intent(context, DashboardActivity.class);
        context.startActivity(intent);

    }

    public void setSnackbar(View view, String message) {
        Snackbar snackbar1 = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar1.show();
    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;

        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            Log.e("shouldOverrideUrl: ", url);

            if (url.startsWith(redirect_url)) {
                dialog.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            String thisUrl = url;
            Log.e("DATA", thisUrl);
            if (url.contains("code%3D")) {
                String access_token;
                String tempUrl = Uri.parse(Uri.parse(thisUrl).getQueryParameter("u")).toString();
                access_token = Uri.parse(Uri.parse(thisUrl).getQueryParameter("u")).getQueryParameter("code");

                token = access_token;
//
                RequestTokenInsta();
                Log.e("onPageFinished: ", access_token);
            }

        }
    }


    private void RequestTokenInsta() {
        RestApiInterface apiInterface = ApiHitter.getInstaToken();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("client_id", context.getResources().getString(R.string.client_id));
        hashMap.put("client_secret", "d6d0f429f6d29870f1140c0a9c53e678");
        hashMap.put("grant_type", "authorization_code");
        hashMap.put("redirect_uri", redirect_url);
        hashMap.put("code", token);
        apiInterface.getInstaToken(hashMap).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                access_token = response.body().get("access_token").toString();
                RequestInstagramAPI();
            }

            @Override
            public void onFailure(Call<Map> call, Throwable throwable) {
                Log.e("DATA", new Gson().toJson(throwable));
            }
        });
    }

    private void RequestInstagramAPI() {
        RestApiInterface apiInterface = ApiHitter.getInstaToken();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fields","username");
        hashMap.put("access_token",access_token);
        apiInterface.getUserName(hashMap).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Log.e("DATA", new Gson().toJson(response));
            }

            @Override
            public void onFailure(Call<Map> call, Throwable throwable) {
                Log.e("DATA", new Gson().toJson(throwable));
            }
        });

    }

   /* private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(context.getResources().getString(R.string.get_user_info_url) + access_token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                Log.e("instaResponse", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {

                        String name = jsonData.getString("full_name");
                        String id = jsonData.getString("id");

//                        presenter.hit_insta_social_login("4",id,name);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }*/


}



