package com.lovepushapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.local.PostAddRequest;
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.utils.PlacesUtils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.soundcloud.android.crop.Crop;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class PostAdsActivity extends BaseActivity implements PostAdsMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.titleTV)
    AppCompatEditText titleTV;
    @BindView(R.id.descriptionTV)
    AppCompatEditText descriptionTV;
    @BindView(R.id.loveAdIV)
    ImageView loveAdIV;
    @BindView(R.id.connectAdIV)
    ImageView connectAdIV;
    @BindView(R.id.imageIV)
    CircleImageView imageIV;
    @BindView(R.id.locationTV)
    AppCompatTextView locationTV;


    @BindView(R.id.radiusTV)
    AppCompatTextView radiusTV;

    @BindView(R.id.postAnonyIV)
    ImageView postAnonyIV;
    @BindView(R.id.imageTV)
    AppCompatTextView imageTV;

    @BindView(R.id.seekBar)
    IndicatorSeekBar seekBar;
    PostAdsPresenter adsPresenter;
    private String latitude, longitude, PostType = "", imagePath = "";
    private boolean isAnnony = false;
    private Activity context = PostAdsActivity.this;
    private AlertDialogs progressDialog;

    public static Intent createIntent(Context context) {
        return new Intent(context, PostAdsActivity.class);
    }

    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_post_ads;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.post_ad));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsPresenter = new PostAdsPresenter(this);
        adsPresenter.attachView(this);
        progressDialog = new AlertDialogs(context);

        seekBar.setProgress(0);

        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                radiusTV.setText(String.valueOf(seekParams.progress) + getString(R.string.km));


            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });


    }

    @OnClick({R.id.backIV, R.id.loveAdLL, R.id.connectAdLL, R.id.imageLL, R.id.locationTV, R.id.postAnonyLL, R.id.postTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.loveAdLL:
                PostType = GlobalsVariables.POST_TYPE.LOVE;
                manageAddType();
                break;
            case R.id.connectAdLL:
                PostType = GlobalsVariables.POST_TYPE.CONNECT;
                manageAddType();
                break;
            case R.id.imageLL:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        ImagePicker.create(PostAdsActivity.this)
                                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                                .folderMode(true) // folder mode (false by default)
                                .toolbarFolderTitle("Folder") // folder selection title
                                .toolbarImageTitle("Tap to select") // image selection title
                                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                                .includeVideo(false) // Show video on image picker
                                .single() // single mode
                                .limit(1) // max images can be selected (99 by default)
                                .showCamera(true) // show camera or not (true by default)
                                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                                .enableLog(false) // disabling log
                                .start(); // start image picker activity with request code
                    }
                });
                break;
            case R.id.locationTV:
                getManualLocation();
                break;
            case R.id.postAnonyLL:
                isAnnony = !isAnnony;
                manageAnonyType();
                break;
            case R.id.postTV:

/*//                For Simulating the location
                locationTV.setText("New York");
                latitude = "28.6219";
                longitude = "77.0878";
//             End of the  For Simulating the location*/


                if (validation()) {
                    PostAddRequest request = new PostAddRequest();
                    request.title = titleTV.getText().toString().trim();
                    request.description = descriptionTV.getText().toString().trim();
                    request.post_type = PostType;
                    request.location = locationTV.getText().toString().trim();
                    request.latitude = latitude;
                    request.longitude = longitude;

                    if (isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_IS;
                    }
                    if (!isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_NOT;
                    }
                    request.radius = String.valueOf(seekBar.getProgress());
                    request.image = imagePath;
                    /*hit add post api*/
                    progressDialog.show();

                    adsPresenter.hitAddPostAds(request, imagePath, new ResponseListner() {
                        @Override
                        public <T> void onComplete(Response<T> response) {

                            progressDialog.dismiss();

                            if (response.isSuccessful()) {
                                PostAdsResponse postAdsResponse = (PostAdsResponse) response.body();
                                appUtils.showToast(postAdsResponse.getMessage(), Toast.LENGTH_LONG);
                                goback();

                            } else {

                                appUtils.showToast(appUtils.parseErrorMessage(response));
                            }
                        }

                        @Override
                        public Void onError(String message) {
                            progressDialog.dismiss();
                            return null;
                        }
                    });
                }

                break;
        }
    }

    private boolean validation() {
        if (appUtils.isFieldEmpty(titleTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_title));
            return false;
        }
        if (appUtils.isFieldEmpty(descriptionTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_desc));
            return false;
        }

        if (TextUtils.isEmpty(PostType)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_ad_type));
            return false;
        }
        /*if (TextUtils.isEmpty(imagePath)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_image));
            return false;
        }*/
        if (appUtils.isFieldEmpty(locationTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_location));
            return false;
        }
       /* if (descriptionTV.getText().toString().trim().length() <20) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.desc_limit));

            return false;
        }*/
        return true;
    }

    private void manageAnonyType() {
        if (isAnnony) {
            postAnonyIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_active));
        } else {
            postAnonyIV.setImageDrawable(getResources().getDrawable(R.drawable.toogle_unactive));
        }
    }

    private void manageAddType() {

        switch (PostType) {
            case GlobalsVariables.POST_TYPE.LOVE:
                loveAdIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                connectAdIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.POST_TYPE.CONNECT:
                connectAdIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                loveAdIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
        }


    }

    private void getManualLocation() {
        try {
            startActivityForResult(PlacesUtils.getPlacesIntent(this), GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            imagePath = image.getPath();
            Log.e("image path", imagePath);
            appUtils.loadGlideImage(context, imagePath, imageIV, R.drawable.image);


        }*/
        try {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                // Get a list of picked images
                List<Image> images = ImagePicker.getImages(data);
                // or get a single image only
                Image image = ImagePicker.getFirstImageOrNull(data);
                beginCrop(Uri.fromFile(new File(image.getPath())));
            }

            if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                beginCrop(data.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (requestCode == GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacesUtils.parsePlacesData(data);
                    locationTV.setText(place.getAddress());

                    LatLng latlng = place.getLatLng();
                    latitude = latlng.latitude + "";
                    longitude = latlng.longitude + "";
                    setAddress(latlng.latitude, latlng.longitude);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(ExploreFilterActivity.class.getSimpleName(), "onActivityResult: " + addresses);

        Log.d("AddressAutomatic", "" + addresses);
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        String address = addresses.get(0).getAddressLine(0);

        locationTV.setText(address);
    }

    private void beginCrop(Uri source) {
        long time=new Date().getTime();
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"+time));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
           /* Picasso.get()
                    .load(Crop.getOutput(result))
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(profileIV);*/
            Uri selectedImageUri = Crop.getOutput(result);
            imagePath = getRealPathFromURI(selectedImageUri);
            if (!imagePath.equals("")) {
                appUtils.loadGlideImage(context, imagePath, imageIV, R.drawable.image);
                String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                if (!filename.equals("")) {
                    File file = new File(imagePath);
                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));

                    imageTV.setText("lovepush" + generateRandomIntIntRange(1500, 2500));
                }
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof PostAdsResponse) {
            PostAdsResponse response = (PostAdsResponse) test;

            appUtils.showToast(response.getMessage(), Toast.LENGTH_LONG);
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                goback();
            } else {
                Toast.makeText(context, "Your Ads Quota is Over", Toast.LENGTH_SHORT).show();
                Log.d("onApiResponse", "onApiResponse: onApiResponse" + response.getMessage() + "getStatus " + response.getStatus());
            }
        }else {
          //  Toast.makeText(context, "Your Ads Quota is Over ",Toast.LENGTH_SHORT).show();
           // appUtils.validationToast(context, screenTitleTV, "Your Ads Quota is Over calling form If");

        }

        Log.e("RESPONSEGetUSer",new Gson().toJson(test));

    }

    @Override
    public <T> void onApiError(T test) {
        Toast.makeText(context, test.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public String fileSize(int size) {
        String hrSize = "";
        double m = size / 1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }
}
