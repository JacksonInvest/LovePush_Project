package com.lovepushapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;

import butterknife.BindView;
import butterknife.OnClick;


public class userTipsPdfFullPreview extends BaseActivity {
    protected FrameLayout webViewPlaceholder;
    protected WebView webView;

    String pdfLink = "https://www.love-push.com/UserTips.pdf";
    private AlertDialogs progressDialog;

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    public static Intent createIntent(Context context) {
        return new Intent(context, userTipsPdfFullPreview.class);
    }
    private Activity context = userTipsPdfFullPreview.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        progressDialog = new AlertDialogs(context);

        progressDialog.show();
        screenTitleTV.setText(getString(R.string.addUserTips));


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_tips_pdf_preview;

    }

    @Override
    public void manageToolBar() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initUI(){

        // Retrieve UI elements
        webViewPlaceholder = findViewById(R.id.frame);
        //webView = findViewById(R.id.webview);
        // Initialize the WebView if necessary,
        if (webView == null)
        {
            // Create the webview
            webView = new WebView(this);
            webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            //webView.getSettings().setSupportZoom(true);
            //webView.getSettings().setBuiltInZoomControls(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(android.webkit.WebSettings.PluginState.ON);
            webView.getSettings().setLoadsImagesAutomatically(true);

            // Load the URLs inside the WebView, not in the external web browser
            webView.setWebViewClient(new SetWebClient());
            webView.setWebChromeClient(new WebChromeClient());

            // Load a page
            webView.loadUrl("https://docs.google.com/viewerng/viewer?url="+pdfLink);
        }

        // Attach the WebView to its placeholder
        webViewPlaceholder.addView(webView);
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
           /* if (pd.isShowing()) {
            }*/

        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        if (webView != null){
            // Remove the WebView from the old placeholder
            webViewPlaceholder.removeView(webView);
        }

        super.onConfigurationChanged(newConfig);

        // Load the layout resource for the new configuration
        setContentView(R.layout.activity_dashboard);

        // Reinitialize the UI
        initUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        // Save the state of the WebView
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the state of the WebView
        webView.restoreState(savedInstanceState);
    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backIV) {
            goback();
        }
    }
}
