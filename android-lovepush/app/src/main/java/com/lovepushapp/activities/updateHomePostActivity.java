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
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.local.PostAddRequest;
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class updateHomePostActivity extends BaseActivity implements PostAdsMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.titleTV)
    AppCompatEditText titleTV;
    @BindView(R.id.descriptionTV)
    AppCompatEditText descriptionTV;

    @BindView(R.id.imageIV)
    CircleImageView imageIV;


    @BindView(R.id.postAnonyIV)
    ImageView postAnonyIV;

    private String  imagePath = "";

    private boolean isAnnony = false;

    PostAdsPresenter adsPresenter;
    @BindView(R.id.imageTV) AppCompatTextView imageTV;
    getHomePostResponse.Data.Datum data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_post;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.update_post));
    }

    private Activity context = updateHomePostActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, updateHomePostActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsPresenter = new PostAdsPresenter(this);
        adsPresenter.attachView(this);
        data = new Gson().fromJson(getIntent().getExtras().getString("data"),  getHomePostResponse.Data.Datum.class);
        titleTV.setText(data.getTitle());
        descriptionTV.setText(data.getDescription());
        if (data.getIsPostAnonymously()==1) {
            isAnnony = true;
            manageAnonyType();
        } else {
            isAnnony = false;
            manageAnonyType();
        }
        appUtils.loadGlideImage(context, data.getImage(), imageIV, R.drawable.image);

    }

    @OnClick({R.id.backIV, R.id.imageLL, R.id.postAnonyLL, R.id.postTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;

            case R.id.imageLL:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(context);

                        //goToImageIntent();
                    }
                });
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


                    if (isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_IS;
                    }
                    if (!isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_NOT;
                    }

                    request.image = imagePath;


                    adsPresenter.hitUpdateHomePost(request,imagePath,data.getId());
                }

                break;
        }
    }

    /*Check required fields*/
    private boolean validation() {
        if (appUtils.isFieldEmpty(titleTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_title));
            return false;
        }
        if (appUtils.isFieldEmpty(descriptionTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_desc));
            return false;
        }

        /*if (descriptionTV.getText().toString().trim().length() <20) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.desc_limit));
            return false;
        }*/


        /*if (TextUtils.isEmpty(imagePath)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_image));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            imagePath = image.getPath();
            Log.e("image path", imagePath);
            appUtils.loadGlideImage(context, imagePath, imageIV, R.drawable.image);


        }*/

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

    /*Get image path */
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
}
