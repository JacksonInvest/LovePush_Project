package com.lovepushapp.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.model.response.LoginResponse;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.videoTestJava.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public Util appUtils;
    @Inject
    public DialogUtils dialogUtils;
    @Inject
    public SharedStorage sharedStorage;
    @Named(GlobalsVariables.DAGGERS.AUTH)
    @Inject
    public
    RestApiInterface apiInterface;
    @Named(GlobalsVariables.DAGGERS.NON_AUTH)
    @Inject
    public
    RestApiInterface apiInterfaceWithAuth;
    protected QBResRequestExecutor requestExecutor;
    protected SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        MyApplication.getAppComponent(this).inject(this);
        manageToolBar();
        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
    }


    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract void manageToolBar();

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void goback() {
        super.onBackPressed();
    }


    public ArrayList<String> getAddressDetails(double lat, double lng) {
        ArrayList<String> arrayList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            arrayList.add(0, obj.getCountryName());
            arrayList.add(1, obj.getLocality());

            return arrayList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            arrayList.add(0, "DummyCountry");
            arrayList.add(1, "DummyCity");
            return arrayList;
        }
    }

    public LoginResponse getLoginModel() {
        LoginResponse response = null;
        try {
            response = new Gson().fromJson((String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.LoginResponse,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    LoginResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public GetProfile getProfileModel() {
        GetProfile response = null;
        try {
            response = new Gson().fromJson((String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    GetProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void showErrorSnackbar(@StringRes int resId, Exception e,
                                     View.OnClickListener clickListener) {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null) {
            ErrorUtils.showSnackbar(rootView, resId, e, R.string.dlg_retry, clickListener);
        }
    }

    protected void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }
        progressDialog.setMessage(getString(messageId));
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

}
