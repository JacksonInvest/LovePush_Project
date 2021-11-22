package com.lovepushapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.local.PostAddRequest;
import com.lovepushapp.model.response.PostAdsResponse;
import com.lovepushapp.modules.PostAds.PostAdsMvp;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class createPostActivity extends BaseActivity implements PostAdsMvp {

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
    PostAdsPresenter adsPresenter;
    @BindView(R.id.imageTV)
    AppCompatTextView imageTV;
    private String imagePath = "";
    private boolean isAnnony = false;
    private Activity context = createPostActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, createPostActivity.class);
    }

    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_post;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.post));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsPresenter = new PostAdsPresenter(this);
        adsPresenter.attachView(this);
    }

    @OnClick({R.id.backIV, R.id.imageLL, R.id.postAnonyLL, R.id.postTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;

            case R.id.imageLL:
                /*Select Image*/
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(context);

                        ImagePicker.create(createPostActivity.this)
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

            case R.id.postAnonyLL:
                isAnnony = !isAnnony;
                manageAnonyType();
                break;
            case R.id.postTV:

                if (validation()) {

                    PostAddRequest request = new PostAddRequest();


                    if (!descriptionTV.getText().toString().isEmpty())
                        request.description = descriptionTV.getText().toString().trim();
                    if (!titleTV.getText().toString().isEmpty())
                        request.title = titleTV.getText().toString().trim();
                     if (isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_IS;
                    }
                     if (!isAnnony) {
                        request.is_post_anonymously = GlobalsVariables.ANNONY_TYPE.ANNONY_NOT;
                    }

                     if (!imagePath.isEmpty()) {
                        request.image = imagePath;
                    }

                    adsPresenter.hitAddPostHome(request);
                }

                break;
        }
    }

    private boolean validation() {
        if (appUtils.isFieldEmpty(titleTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_title));
            return false;
        }
       /* else if (appUtils.isFieldEmpty(descriptionTV)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_desc));
            return false;
        }*/
         if (imagePath.isEmpty()) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_post_image));
            return false;
        }

        /*if (descriptionTV.getText().toString().trim().length() <20) {
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
                appUtils.loadGlideImage(context, selectedImageUri, imageIV, R.drawable.image);
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
            appUtils.showToast(response.getMessage());
            if (response.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                goback();

            }
        }

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

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


}
