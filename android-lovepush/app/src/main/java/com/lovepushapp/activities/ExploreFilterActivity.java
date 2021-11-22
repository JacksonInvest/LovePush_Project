package com.lovepushapp.activities;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.helper.PreferenceManager;
import com.lovepushapp.model.response.updateUserLocationResponse;
import com.lovepushapp.network.RestCallback;
import com.lovepushapp.network.RestProcess;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ExploreFilterActivity extends BaseActivity {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.left_age_tv)
    TextView left_age_tv;

    @BindView(R.id.right_age_tv)
    TextView right_age_tv;

    @BindView(R.id.submitTV)
    AppCompatButton submitTV;

    private String gender="";

    @BindView(R.id.maleIV)
    ImageView maleIV;
    @BindView(R.id.femaleIV)
    ImageView femaleIV;
    @BindView(R.id.transGenderIV)
    ImageView transGenderIV;
    @BindView(R.id.locationLL)
    LinearLayout locationLL;
    @BindView(R.id.etLocation)
    AppCompatTextView etLocation;
    int gen;
    private static final int REQUEST_location= 101;


    LocationManager locationManager;

    CrystalRangeSeekbar rangeSeekbar;

    double latitude = 0.0;
    double longitude = 0.0;

    private Activity context = ExploreFilterActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_explore_filter);
        rangeSeekbar = findViewById(R.id.rangeSeekbar1);




         int min_age= Integer.valueOf(getIntent().getStringExtra("min"));
         int max_age= Integer.valueOf(getIntent().getStringExtra("max"));
        gender = String.valueOf(getIntent().getIntExtra("gebder",0));
        if (latitude==getIntent().getDoubleExtra("latitute",0.0))
        {
            latitude = Double.valueOf(new PreferenceManager().getSharedlatitude());
            longitude = Double.valueOf(new PreferenceManager().getSharedlongitude());
            String Address = new PreferenceManager().getSharedAddress();
            etLocation.setText(Address);
        }else
        {
            manageGender();
            latitude = getIntent().getDoubleExtra("latitute",0.0);
            longitude = getIntent().getDoubleExtra("longitute",0.0);
            setAddress(latitude,longitude);
        }
        /*if (filter_type.equalsIgnoreCase("swipe")){
            locationLL.setVisibility(View.GONE);
        }*/
        // set listener


        //rangeSeekbar.setMinValue(min_age);
     //   rangeSeekbar.setMaxValue(max_age);


      //  rangeSeekbar.setMinValue(min_age);
     //   rangeSeekbar.setMaxValue(max_age);


        rangeSeekbar.setMaxStartValue(min_age).apply();
        rangeSeekbar.setMaxStartValue(max_age).apply();


        rangeSeekbar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            left_age_tv.setText(String.valueOf(minValue));
            right_age_tv.setText(String.valueOf(maxValue));
        });



    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_explore_filter;
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, ChangePassActivity.class);
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.filter));

    }

    @OnClick({R.id.backIV,R.id.submitTV,R.id.maleLL,R.id.femaleLL,R.id.transGenderLL,R.id.locationLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.submitTV:
                ExploreSet();
//                Toast.makeText(context, "Testing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.maleLL:
                gender = GlobalsVariables.GENDER.Male;
                manageGender();
                break;
            case R.id.femaleLL:
                gender = GlobalsVariables.GENDER.female;
                manageGender();
                break;
            case R.id.transGenderLL:
                gender = GlobalsVariables.GENDER.homo;
                manageGender();
                break;
            case R.id.locationLL:
                if (CheckGpsStatus()) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(context), REQUEST_location);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Please Enable your GPS location.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void ExploreSet() {
        Intent resultIntent = new Intent();
        sharedStorage.setSharedData("filter_gender",GlobalsVariables.STORAGE.TYPE_STRING,String.valueOf(gen));
        sharedStorage.setSharedData("min_age",GlobalsVariables.STORAGE.TYPE_STRING,left_age_tv.getText().toString());
        sharedStorage.setSharedData("max_age",GlobalsVariables.STORAGE.TYPE_STRING,right_age_tv.getText().toString());
// TODO Add extras or a data URI to this intent as appropriate.
//        sharedStorage.setSharedData("gender",GlobalsVariables.STORAGE.TYPE_STRING,gen);
        resultIntent.putExtra("gender", gen);
        resultIntent.putExtra("latitude",latitude);
        resultIntent.putExtra("longitude",longitude);

        resultIntent.putExtra("min_age",left_age_tv.getText().toString());
        resultIntent.putExtra("max_age",right_age_tv.getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    /*Manage gender basis on user selection*/
    private void manageGender() {
        switch (gender) {
            case GlobalsVariables.GENDER.Male:
                gen=1;
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.female:
                gen=2;
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.homo:
                gen=3;
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_location) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, ExploreFilterActivity.this);


                latitude = selectedPlace.getLatLng().latitude;
                longitude = selectedPlace.getLatLng().longitude;
                setAddress(latitude, longitude);

/*
                LocationNameAutomatic.setText(getLocality()+", "+getCountry());
                LocationNameManual.setText(getLocality()+", "+getCountry());
                LocationLatLong.setText(getLatitude()+" , "+getLongitude());*/
                // Do something with the place
            }
        }
    }

    private void setAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(ExploreFilterActivity.class.getSimpleName(), "onActivityResult: "+addresses);

        Log.d("AddressAutomatic",""+addresses);
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        String address = addresses.get(0).getAddressLine(0);


        etLocation.setText(address);
     //   hitUpdateLocation(String.valueOf(latitude),String.valueOf(longitude),address);


    }

    public void hitUpdateLocation(String lat, String lng, String address) {

        if (appUtils.isInternetConnection(context)) {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap = appUtils.getApiHeaders(context);

            String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);

//            String userId="2";


            Call<updateUserLocationResponse> call = apiInterface.apiUpdateLocation(headerMap,userId,lat,lng,address);
            call.enqueue(new RestProcess<updateUserLocationResponse>(API_GLOBALS.API_NAME.updateLocation, new RestCallback() {
                @Override
                public void onSuccess(Call call, Response model, String serviceMode) {
                    switch (serviceMode){
                        case API_GLOBALS.API_NAME.updateLocation:
                            //getMvpView().onApiResponse(model.body(),serviceMode);

                            Log.d("Update location", "onSuccess: Address Updated to server");
                            break;
                        default:Log.d("Update location ","Update Location default sectino");
                    }

                }

                @Override
                public void onTimeOut(Call call, Throwable t, String serviceMode) {

                }

                @Override
                public void onFailure(Call call, Throwable t, String serviceMode) {

                }
            }, context, false));
        }
        else {
            appUtils.showToast(context.getString(R.string.internet_not_available));
        }
    }


    private boolean CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
