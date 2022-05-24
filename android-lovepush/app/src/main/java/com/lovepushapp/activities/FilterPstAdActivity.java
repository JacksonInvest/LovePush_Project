package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.libraries.places.api.model.Place;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.utils.PlacesUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class FilterPstAdActivity extends BaseActivity {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;


    @BindView(R.id.submitTV)
    AppCompatButton submitTV;

    private String loveConnectType="";

    @BindView(R.id.loveIV)
    ImageView loveIV;
    @BindView(R.id.connectIV)
    ImageView connectIV;
    @BindView(R.id.locationLL)
    LinearLayout locationLL;
    @BindView(R.id.etLocation)
    AppCompatTextView etLocation;
    private static final int REQUEST_location= 101;

    LocationManager locationManager;

    double latitude = 0.0;
    double longitude = 0.0;

    private Activity context = FilterPstAdActivity.this;

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter_post_ad;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.filter));
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, FilterPstAdActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loveConnectType = getIntent().getStringExtra("loveConnectType");
        manageRadio();
        latitude = Double.valueOf(new PreferenceManager().getSharedlatitude());
        longitude = Double.valueOf(new PreferenceManager().getSharedlongitude());
        String Address = new PreferenceManager().getSharedAddress();
        etLocation.setText(Address);
    }

    @OnClick({R.id.backIV,R.id.submitTV,R.id.loveLL,R.id.connectLL,R.id.locationLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.submitTV:
                ExploreSet();
//                Toast.makeText(context, "Testing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.loveLL:
                loveConnectType = "0";
                manageRadio();
                break;
            case R.id.connectLL:
                loveConnectType = "1";
                manageRadio();
                break;

            case R.id.locationLL:
                if (CheckGpsStatus()) {
                    startActivityForResult(PlacesUtils.getPlacesIntent(this), REQUEST_location);
                } else {
                    Toast.makeText(context, "Please Enable your GPS location.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void ExploreSet() {

        String lat ="", lng = "";
        if (latitude != 0.0){
            lat = String.valueOf(latitude);
        }
        if (longitude != 0.0){
            lng = String.valueOf(longitude);
        }
        Intent resultIntent = new Intent();

        resultIntent.putExtra("loveConnectType", loveConnectType);
        resultIntent.putExtra("latitude",lat);
        resultIntent.putExtra("longitude",lng);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    private void manageRadio() {
        switch (loveConnectType) {
            case "0":
                loveIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                connectIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case "1":
                loveIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                connectIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));

                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_location) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacesUtils.parsePlacesData(data);

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                latitude = selectedPlace.getLatLng().latitude;
                longitude = selectedPlace.getLatLng().longitude;

                List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                try {
                    addresses = geocoder.getFromLocation(selectedPlace.getLatLng().latitude, selectedPlace.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(ExploreFilterActivity.class.getSimpleName(), "onActivityResult: "+addresses);

                Log.d("AddressAutomatic",""+addresses);
                String address = addresses.get(0).getAddressLine(0);
                etLocation.setText(address);

              /*  LocationNameAutomatic.setText(getLocality()+", "+getCountry());
                LocationNameManual.setText(getLocality()+", "+getCountry());
                LocationLatLong.setText(getLatitude()+" , "+getLongitude());*/

                // Do something with the place
            }
        }
    }

    private boolean CheckGpsStatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
