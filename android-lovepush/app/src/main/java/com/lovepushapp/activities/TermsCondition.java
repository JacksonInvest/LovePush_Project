package com.lovepushapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TermsCondition extends BaseActivity {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.txtDisagree)
    TextView txtDisagree;

    @BindView(R.id.txtAgree)
    TextView txtAgree;

    String from = "";

    String url_link = "";

    //String legacy = "https://www.love-push.com/legacy";
   // public static final String LEGACY = "http://lovepush.alcax.com/legacy";

    public static final String LEGACY = "file:///android_asset/legacy.html";

    String communityGuidlines = "https://www.love-push.com/community_guidelines";
    String user_tips = "http://14.141.175.109/lovepush/public/UserTips.pdf";

    public static Intent createIntent(Context context) {
        return new Intent(context, PreviewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");

            url_link = getIntent().getStringExtra("url");

        }


        try {
            if (from.equalsIgnoreCase("privacy_policy")) {

                screenTitleTV.setText("LEGACY");
                titleTV.setText(getString(R.string.privacy_policy));

            }
            if (from.equalsIgnoreCase("guidelines")) {
                titleTV.setText(getString(R.string.community_guide));

                screenTitleTV.setText("GUIDELINES");

            }
            if (from.equalsIgnoreCase("user_tips")) {
                titleTV.setText(getString(R.string.addUserTips));

                screenTitleTV.setText("User Tips");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        screenTitleTV.setText(from);


        webview.getSettings().setJavaScriptEnabled(true);

        if (from.equalsIgnoreCase("privacy_policy")) {
            webview.loadUrl(LEGACY);
        } else if (from.equalsIgnoreCase("guidelines")) {
            webview.loadUrl(communityGuidlines);
        } else if (from.equalsIgnoreCase("user_tips")) {
            webview.loadUrl("https://docs.google.com/viewerng/viewer?url=14.141.175.109/lovepush/public/UserTips.pdf");
        }

//        webview.loadUrl("http://www.google.com");

        webview.setWebViewClient(new WebViewController());


        txtDisagree.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.putExtra("term",false);
            setResult(2,intent);
            finish();//finishing activity
        });
        txtAgree.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.putExtra("term",true);
            setResult(2,intent);
            finish();//finishing activity
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_terms_condition;
    }

    @Override
    public void manageToolBar() {

    }

    @OnClick({R.id.backIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
        }
    }
    /*WebView*/
    private class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
}
