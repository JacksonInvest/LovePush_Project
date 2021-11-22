package com.lovepushapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.homePosts.addsmodel.DataItem;
import com.lovepushapp.model.local.PostAddRequest;
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class updatePostAdsActivity extends BaseActivity implements PostAdsMvp {

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


    @BindView(R.id.seekBar)
    IndicatorSeekBar seekBar;
    @BindView(R.id.imageTV) AppCompatTextView imageTV;
    private String latitude, longitude, PostType = "", imagePath = "";

    private boolean isAnnony = false;

    PostAdsPresenter adsPresenter;

    DataItem data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_updaate_post_ads;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.update_ads));
    }

    private Activity context = updatePostAdsActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, updatePostAdsActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsPresenter = new PostAdsPresenter(this);
        adsPresenter.attachView(this);

        data = new Gson().fromJson(getIntent().getExtras().getString("data"), DataItem.class);
        titleTV.setText(data.getTitle());
        descriptionTV.setText(data.getDescription());

        if (data.getPostType() ==1){
            PostType = GlobalsVariables.POST_TYPE.LOVE;
            manageAddType();
        }else {
            PostType = GlobalsVariables.POST_TYPE.CONNECT;
            manageAddType();
        }

        if (data.getIsPostAnonymously()==1) {
            isAnnony = true;
            manageAnonyType();
        } else {
            isAnnony = false;
            manageAnonyType();
        }
        locationTV.setText(data.getLocation());
        latitude = data.getLatitude();
        longitude = data.getLongitude();
        appUtils.loadGlideImage(context, data.getImage(), imageIV, R.drawable.image);
        seekBar.setProgress(Float.parseFloat(data.getRadius()));

        radiusTV.setText(seekBar.getProgress()+getString(R.string.km));
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
                        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(context);

                      //  goToImageIntent();
                    }
                });
                break;
            case R.id.locationTV:
                getManualLocation();
                break;
            case R.id.postAnonyLL:
                if (isAnnony) {
                    isAnnony = false;
                } else {
                    isAnnony = true;
                }
                manageAnonyType();
                break;
            case R.id.postTV:

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
                    } if (!isAnnony){
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_NOT;
                    }

                    request.radius = String.valueOf(seekBar.getProgress());
                    request.image = imagePath;


                    adsPresenter.hitUpdatePostAds(request,imagePath,data.getId());
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
        /*if (descriptionTV.getText().toString().trim().length() <20) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.desc_limit));
            return false;
        }*/
        return true;
    }

    public void goToImageIntent() {

        dialogUtils.openBottomDialog(appUtils, context, appUtils.getMediaOption(), 1, new OnItemClickListenerCutom() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                switch (position) {
                    case 0:
                        ImagePicker.cameraOnly().start(context);
                        break;
                    case 1:
                        ImagePicker.create(context)
                                .returnMode(ReturnMode.GALLERY_ONLY)
                                .folderMode(true) // folder mode (false by default)
                                .toolbarFolderTitle("Folder") // folder selection title
                                .toolbarImageTitle("Tap to select") // image selection title
                                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                                .single()
                                .imageDirectory(getString(R.string.app_name))
                                .limit(1)
                                .includeVideo(false)
                                .showCamera(false)// single mode
                                .enableLog(true) // disabling log
                                .start(); // start image picker activity with request code
                        break;
                }
            }
        });
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
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            imagePath = image.getPath();
            Log.e("image path", imagePath);
            appUtils.loadGlideImage(context, imagePath, imageIV, R.drawable.image);


        }
*/

       try {
           if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
               CropImage.ActivityResult result = CropImage.getActivityResult(data);
               if (resultCode == RESULT_OK) {
                   Uri selectedImageUri = result.getUri();
                   imagePath = getRealPathFromURI(selectedImageUri);
                   if (!imagePath.equals("")){
                       appUtils.loadGlideImage(context, imagePath, imageIV, R.drawable.image);
                       String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                       if (!filename.equals("")) {
                           File file = new File(imagePath);
                           int file_size = Integer.parseInt(String.valueOf(file.length()/1024));

                           imageTV.setText(filename+" ("+fileSize(file_size)+")");
                       }
                   }


               }

           }
       }catch (Exception e) {
           e.printStackTrace();
       }
        try {
            if (requestCode == GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getAddress());
                    locationTV.setText(place.getAddress());

                    LatLng latlng = place.getLatLng();
                    latitude = latlng.latitude + "";
                    longitude = latlng.longitude + "";


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String fileSize(int size){
        String hrSize = "";
        double m = size/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof PostAdsResponse) {
            PostAdsResponse response = (PostAdsResponse) test;
            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                goback();

            }else {
                alertPopup(response.getMessage());
            }
        }

    }
    private void alertPopup(String s) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setTitle("Message");
        builder1.setTitle(getString(R.string.app_name));
        builder1.setMessage(s);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // loginRegisterProfilePresenter.hitGetExplore(page,latitude,longitude);
//                        context.onBackPressed();
                        goback();
//                        context.startActivity(new Intent(getContext(), DashboardActivity.class));
                    }
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
}
