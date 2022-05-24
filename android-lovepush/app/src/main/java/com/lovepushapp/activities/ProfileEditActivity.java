package com.lovepushapp.activities;

import static com.lovepushapp.core.utils.Util.getAgeBetweenTwoDate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lovepushapp.R;
import com.lovepushapp.adapters.ProfileImagesAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.NetworkRequests;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.utils.PlacesUtils;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import hari.bounceview.BounceView;
import retrofit2.Response;

public class ProfileEditActivity extends BaseActivity implements LoginMvp {

    private static final String TAG = "ProfileEditActivity";
    private static final int RESULT_LOAD_IMAGE = 121;
    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;
    @BindView(R.id.ageET)
    AppCompatEditText ageET;
    @BindView(R.id.edit_root)
    LinearLayout editRoot;
    @BindView(R.id.jobEmployerET)
    AppCompatEditText jobEmployerET;
    @BindView(R.id.profileimages_RV)
    RecyclerView profileimagesRV;
    private String sexualOrientation = "";
    private String gender="";
    /*@BindView(R.id.imagesRV)
    RecyclerView imagesRV;*/
    @BindView(R.id.profileIV)
    CircleImageView profileIV;
    private String profileImagePath = "";
    @BindView(R.id.addressET)
    AppCompatTextView addressET;
    @BindView(R.id.lovePushET)
    AppCompatEditText lovePushET;
    @BindView(R.id.relationShipET)
    AppCompatEditText relationShipET;
    @BindView(R.id.languageET)
    AppCompatEditText languageET;
    @BindView(R.id.sizeET)
    AppCompatEditText sizeET;
    @BindView(R.id.eyeColorET)
    AppCompatEditText eyeColorET;
    @BindView(R.id.hairColorET)
    AppCompatEditText hairColorET;
    @BindView(R.id.educationET)
    AppCompatEditText educationET;
    @BindView(R.id.hobbiesET)
    AppCompatEditText hobbiesET;
    @BindView(R.id.maleIV)
    ImageView maleIV;
    @BindView(R.id.femaleIV)
    ImageView femaleIV;
    @BindView(R.id.transGenderIV)
    ImageView transGenderIV;
    @BindView(R.id.heteroSexualIV)
    ImageView heteroSexualIV;
    @BindView(R.id.biSexualIV)
    ImageView biSexualIV;
    @BindView(R.id.homeSexualIV)
    ImageView homeSexualIV;
    @BindView(R.id.aboutUsET)
    AppCompatEditText aboutUsET;
    @BindView(R.id.nameET)
    AppCompatEditText nameET;


    @BindView(R.id.usernameET)
    AppCompatEditText usernameET;

    //List<Uri> path = new ArrayList<>();

    @BindView(R.id.addImagesLL)
    LinearLayout addImagesLL;
    ProfileImagesAdapter adap;
    @BindView(R.id.scrollview)
    ScrollView scrollView;

    String from2;

    HashMap<Integer, Boolean> hashMap;

    @BindView(R.id.physiqueET)
    AppCompatEditText physiqueET;

    int physique_pos;



    @Inject
    Util util;

    private String latitude = "123456", longitude = "654321";

    private GetProfile profileResponse;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    private ArrayList<String> locationList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_profile_edit;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.edit_profile));
    }

    private Activity context = ProfileEditActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileEditActivity.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getAppComponent(this).inject(this);

        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);
        profileResponse = getProfileModel();
        Log.e("onCreate: ", "" + profileResponse.getData().getAge());
        if (profileResponse == null) {
            onBackPressed();
        } else {

            setPreviousData();
        }

        Button submitTV;
        submitTV=findViewById(R.id.submitTV);

        BounceView.addAnimTo(submitTV);





        hashMap=new HashMap<>();


        aboutUsET.setOnClickListener(v -> {


        });
        aboutUsET.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.aboutUsET) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()&MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;

        });



        lovePushET.setOnClickListener(v -> {
            boolean check1 = false;
            boolean check2=false;
            boolean check3=false;
            boolean check4=false;
            boolean[] check=new boolean[5];
            String love_int=lovePushET.getText().toString().trim();
            String int1 = "",int2="",int3="",int4="";
            String[] ar = new String[5];



/*                String[] array=new String[3];
            array[0]="Relationship";
            array[1]="Friendship";
            array[2]="Daily Activities";
            array[3]="No Expectation";*/

            try {
//                StringTokenizer stringTokenizer=new StringTokenizer(love_int,",");

                String[] parts = love_int.split(",");
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                Log.e("LENGTH", Arrays.toString(parts)+love_int);

//                    [Relationship,  Daily Activities,  NO Expectation]

                for (int i=0;i<parts.length;i++) {
                    Log.e("SPLITE",i+parts[i]);

//                        Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();

                    if (parts[i].equalsIgnoreCase("Daily Activities")||parts[i].equalsIgnoreCase(" Daily Activities")) {
//                            int1 = parts[0];
//                            int2 = parts[1];
                        ar[i] = parts[i];
                        Log.e("DEl","TRUE");
                        check3=true;


                    }

                    if (parts[i].equalsIgnoreCase("Relationship")||parts[i].equalsIgnoreCase(" Relationship")) {
                        ar[i] = parts[i];
                        check1=true;
                        Log.e("Rel","TRUE");

                    }
                    if (parts[i].equalsIgnoreCase(" Friendship")||parts[i].equalsIgnoreCase("Friendship")) {
//                            int1 = parts[0];
                        ar[i] = parts[i];
                        check2=true;
                        Log.e("Fri","TRUE");

                    }

                    if (parts[i].equalsIgnoreCase("NO Expectation")||parts[i].equalsIgnoreCase(" NO Expectation")) {

//                            int1 = parts[0];
//                            int2 = parts[1];
//                            int3 = parts[2];
                        ar[i] = parts[i];
                        check4=true;
                        Log.e("No","TRUE");

                    }
                }

                Log.e("TAG",ar[0]+ar[1]+ar[2]+ar[3]);

            }

            catch (Exception e)
            {
                Log.e("EXCEPTION", String.valueOf(e));
            }
            openIntention(check1,check2,check3,check4);

        });



    }

    private void openIntention(boolean check1, boolean check2, boolean check3, boolean check4) {
        String[] item = {"Relationship", "Friendship", "Daily Activities", "No Expectation"};
        final List<String> itemList = Arrays.asList(item);


        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lovepush Intention");



// add a checkbox list
        String[] animals = {"Relationship", "Friendship", "Daily Activities", "No Expectation"};
        boolean[] checkedItems = {check1, check2, check3, check4};
        builder.setMultiChoiceItems(item, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                Toast.makeText(context, ""+isChecked+which, Toast.LENGTH_SHORT).show();
                hashMap.put(which,isChecked);
                checkedItems[which] = isChecked;



            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", (dialog, which) -> {

            StringBuilder stringBuilder=new StringBuilder();
            // user clicked OK
            int i=0;
            boolean[] in = new boolean[4];
            for (Map.Entry<Integer, Boolean> entry : hashMap.entrySet()) {
                Integer key = entry.getKey();
                Boolean value = entry.getValue();
                Log.e("value",""+key+value);
                in[key]=value;
//                    String s= String.valueOf(entry.getKey());
//                    boolean e=hashMap.containsKey(i);
//                    i++;

                // do stuff
            }
            String relationship="Relationship",friednship="Friendship",daily="Daily Activities",Noexpectation="NO Expectation";
            lovePushET.setText("");
            for (int j = 0; j<checkedItems.length; j++){
//                    Toast.makeText(context, ""+checkedItems[j], Toast.LENGTH_SHORT).show();
                boolean checked = checkedItems[j];
                if (checked) {
//                        Toast.makeText(context, ""+itemList.get(j), Toast.LENGTH_SHORT).show();
                    String s=itemList.get(j);

                    lovePushET.setText(lovePushET.getText() + itemList.get(j) + ", ");
                }
            }
//                lovePushET.setText("asdfdasf");






        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setPreviousData() {
        try {
            if (profileResponse.getData().getPhysique().equals(1))
            {

                physiqueET.setText(getString(R.string.plump));

            }
            if (profileResponse.getData().getPhysique().equals(2))
            {
                physiqueET.setText(getString(R.string.thin));

            }
            if (profileResponse.getData().getPhysique().equals(3))
            {
                physiqueET.setText(getString(R.string.athletic));

            }
            if (profileResponse.getData().getPhysique().equals(4))
            {
                physiqueET.setText(getString(R.string.muscular));

            }
            if (profileResponse.getData().getPhysique().equals(5))
            {
                physiqueET.setText(getString(R.string.normal));

            }
        }
        catch (Exception e)
        {
            Log.e("EXCEPTION", String.valueOf(e));
        }

        Log.e("PHYSIQUE", String.valueOf(profileResponse.getData().getPhysique()));
//        physiqueET.setText((Integer) profileResponse.getData().getPhysique());
        nameET.setText(profileResponse.getData().getName());
        usernameET.setText(profileResponse.getData().getUsername());
        lovePushET.setText(profileResponse.getData().getLovePushIntention());

//        item.add(context.getResources().getString(R.string.single));
//        item.add(context.getResources().getString(R.string.in_relationship));
//        item.add(context.getResources().getString(R.string.married));
//        item.add(context.getResources().getString(R.string.recently_separated));
//        item.add(context.getResources().getString(R.string.open_relationship));

        try {
            if (profileResponse.getData().getRelationship()==0)
            {
                relationShipET.setText(getString(R.string.single));
            }
            if (profileResponse.getData().getRelationship()==1)
            {
                relationShipET.setText(getString(R.string.in_relationship));
            }
            if (profileResponse.getData().getRelationship()==2)
            {
                relationShipET.setText(getString(R.string.married));
            }
            if (profileResponse.getData().getRelationship()==3)
            {
                relationShipET.setText(getString(R.string.recently_separated));
            }
            if (profileResponse.getData().getRelationship()==4)
            {
                relationShipET.setText(getString(R.string.open_relationship));
            }

        }
        catch (Exception e)
        {
            Log.e("Exception", String.valueOf(e));
        }

        ageET.setText("" + profileResponse.getData().getAge());
//        languageET.setText(profileResponse.get());
        sizeET.setText(profileResponse.getData().getSize());
        eyeColorET.setText(profileResponse.getData().getEyecolor());

        hairColorET.setText(profileResponse.getData().getHaircolor());
        educationET.setText(profileResponse.getData().getEducation());
        jobEmployerET.setText(profileResponse.getData().getJob());

        hobbiesET.setText(profileResponse.getData().getHobbies());
        aboutUsET.setText(profileResponse.getData().getAboutme());

        addressET.setText((String) profileResponse.getData().getAddress());
        locationList = new ArrayList<>();
        locationList.add(0, (String) profileResponse.getData().getCountry());
        locationList.add(1, (String) profileResponse.getData().getCity());

        languageET.setText(profileResponse.getData().getLanguage());

        gender = profileResponse.getData().getGender() + "";
        manageGender();
        sexualOrientation=profileResponse.getData().getSexualOrientation()+"";
//        Toast.makeText(context, ""+sexualOrientation, Toast.LENGTH_SHORT).show();
        manageSexualOrientation();
//        profileResponse.getData().getSexualOrientation();

        setAdapterRecent(profileResponse.getData().getProfileImages());
//        Toast.makeText(context, ""+profileResponse.getData().getProfileImage(), Toast.LENGTH_SHORT).show();
        Log.e("PROFILESIZE",""+profileResponse.getData().getProfileImages().size());
        /*if (profileResponse.getData().getProfileImage().equalsIgnoreCase("http://14.141.175.109/lovepush/public/images/system/default_user.png"))
        {
            addImagesLL.setVisibility(View.GONE);
        }*/
        //appUtils.loadGlideImage(context, profileResponse.getData().getProfileImage(), profileIV, R.drawable.profile_bg);


        Picasso.get()
                .load(profileResponse.getData().getProfileImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(profileIV);
        /*if (profileResponse.getData().getProfileImages().isEmpty()){
        }else {
            Picasso.get()
                    .load(profileResponse.getData().getProfileImages().get(0).getImage())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(profileIV);
        }*/
    }

    private void setAdapterRecent(List<ProfileImage> pathlist) {
       /* Collections.sort(pathlist, new Comparator<ProfileImage>() {
            @Override
            public int compare(ProfileImage lhs, ProfileImage rhs) {
                return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
            }
        });
*/
        try {
            profileimagesRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            adap = new ProfileImagesAdapter("from_profile", context, pathlist, new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    Log.e( "onItemClick: ", String.valueOf(type));

                    if(view.getId() == R.id.img_categoryImage_close)
                          loginRegisterProfilePresenter.deleteprofile(String.valueOf(type));

                    if (view.getId() == R.id.img_categoryImage){
                        Picasso.get()
                                .load(profileResponse.getData().getProfileImages().get(position).getImage())
                                .placeholder(R.drawable.placeholder_male_square)
                                .error(R.drawable.placeholder_male_square)
                                .into(profileIV);

                        NetworkRequests.selectProfileImage(ProfileEditActivity.this, profileResponse.getData().getProfileImages().get(position).getId(), new ResponseListner() {
                            @Override
                            public <T> void onComplete(Response<T> response) {

                                Toast.makeText(context, "Profile Updated successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public Void onError(String message) {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                                return null;
                            }
                        });
//                        profileImagePath = profileResponse.getData().getProfileImages().get(position).getImage();


                    }


                }
            });

            String profileImageUrl = profileResponse.getData().getProfileImage() != null ? profileResponse.getData().getProfileImage() : "";
            adap.setSelectedImageUrl(profileImageUrl);
            profileimagesRV.setAdapter(adap);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    private void setAdapterProfileOther(Uri uri) {

        update(uri,false);
/*
        NetworkRequests.updateProfileImages(this, path, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {

                profileResponse.getData().setProfileImages();
                loginRegisterProfilePresenter.updateProfile(req,new ResponseListner() {
                    @Override
                    public <T> void onComplete(Response<T> response) {
                        GetProfile res = (GetProfile) response.body();
                        appUtils.setSnackbar(editRoot, res.getMessage());
                        sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());
                        dialogs.dismiss();
                        // Toast.makeText(ProfileEditActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public Void onError(String message) {
                        dialogs.dismiss();
                        Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
*/
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent data2=data;

        try {
            if (requestCode == GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacesUtils.parsePlacesData(data);
                    String toastMsg = String.format("Place: %s", place.getAddress());
                    addressET.setText(place.getAddress());

                    LatLng latlng = place.getLatLng();
                    latitude = latlng.latitude + "";
                    longitude = latlng.longitude + "";

                    locationList = getAddressDetails(latlng.latitude, latlng.longitude);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            //path.add(selectedImage);

            setAdapterProfileOther(selectedImage);
//            Toast.makeText(context, ""+selectedImage, Toast.LENGTH_SHORT).show();
            Log.e("image path", util.getPathFromUri(context, selectedImage));
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (from2.equalsIgnoreCase("profile")) {
                    Uri selectedImageUri = result.getUri();
                    profileImagePath = getRealPathFromURI(selectedImageUri);
                    //Toast.makeText(context, ""+profileImagePath, Toast.LENGTH_SHORT).show();
                    if (!profileImagePath.equals("")){
                        appUtils.loadGlideImage(context, profileImagePath, profileIV, R.drawable.placeholder_male_square);

                    }
                }
                else if (from2.equalsIgnoreCase("images"))
                {
                    Uri selectedImageUri = result.getUri();
                    setAdapterProfileOther(selectedImageUri);
                }



            }

        }

        try {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                if (from2.equalsIgnoreCase("profile")) {
                    // or get a single image only
                    Image image = ImagePicker.getFirstImageOrNull(data);
                    profileImagePath = image.getPath();
                    Log.e("image path", profileImagePath);
                   // appUtils.loadGlideImage(context, profileImagePath, profileIV, R.drawable.ef_image_placeholder);
                    Picasso.get()
                            .load(profileResponse.getData().getProfileImage())
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(profileIV);
                }
                else if (from2.equalsIgnoreCase("images"))
                {
//                    try {
//                        Image image=ImagePicker.getFirstImageOrNull(data);
//
//                        Uri selectedImage = Uri.parse(image.getPath());
//                    Toast.makeText(context, "image path "+selectedImage, Toast.LENGTH_SHORT).show();
//
//                    path.add(selectedImage);
//                    setAdapterProfileOther(path);
////                    Log.e("image path", util.getPathFromUri(context, selectedImage));
//                    }
//                    catch (Exception e)
//                    {
//                        Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
//                    }

                }
              /*  Intent returnIntent = new Intent();
                returnIntent.putExtra("result", image.getPath());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
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
            setAdapterProfileOther(selectedImageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick({R.id.ageET, R.id.backIV, R.id.addressET, R.id.submitTV, R.id.relationShipET, R.id.maleLL, R.id.femaleLL, R.id.transGenderLL, R.id.addImagesLL, R.id.profileIV,R.id.languageET,R.id.physiqueET,R.id.heteroSexualLL,R.id.homeSexualLL,R.id.biSexualLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.ageET:

                selectDatePopup(ageET);

          /*dialogUtils.openBottomDialog(appUtils, context, appUtils.getAge(), 0, (view12, position, type, o) -> {
                    String res = (String) o;
                    ageET.setText(res);
                });*/
                break;

            case R.id.languageET:
                /*dialogUtils.openBottomDialog(appUtils, context, appUtils.getLang(), 0, new OnItemClickListenerCutom() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object o) {
                        String language=(String) o;
                        languageET.setText(language);
                    }
                });*/
//                pickCountry();
                break;
            case R.id.physiqueET:
                dialogUtils.openBottomDialog(appUtils, context, appUtils.getPhysique(context), 0, (view1, position, type, o) -> {
                    String res = (String) o;
                    physiqueET.setText(res);
                    physique_pos = position+1;


                });
                break;
            case R.id.addressET:

                getManualLocation();

                break;
            case R.id.addImagesLL:

                String[] permissions1 = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(context/*context*/, permissions1, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
//                        from2="images";
//                        goToImageIntent();
//                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(i, RESULT_LOAD_IMAGE);

                        /*Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
//                        intent.setAction( );
                        startActivityForResult(Intent.createChooser(intent, "Select picture"), RESULT_LOAD_IMAGE );*/
                        from2="images";
                        //goToImageIntent();
                        //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(150,150).start(context);

                        //Crop.pickImage(ProfileEditActivity.this);
                        ImagePicker.create(ProfileEditActivity.this)
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
            case R.id.profileIV:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(context/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        from2="profile";
                        //goToImageIntent();
                        //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(context);

                    }
                });
                break;
            case R.id.relationShipET:
                dialogUtils.openBottomDialog(appUtils, context, appUtils.getRelationShipStatus(context), 0, new OnItemClickListenerCutom() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object o) {
                        String res = (String) o;
                        relationShipET.setText(res);
                    }
                });

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
            case R.id.heteroSexualLL:
                sexualOrientation=GlobalsVariables.SEXUAL.Heter;
                manageSexualOrientation();
                break;
            case R.id.biSexualLL:
                sexualOrientation=GlobalsVariables.SEXUAL.Bi;
                manageSexualOrientation();
                break;
            case R.id.homeSexualLL:
                sexualOrientation=GlobalsVariables.SEXUAL.Homos;
                manageSexualOrientation();
                break;
            case R.id.backIV:
                update(null, false);
                break;
            case R.id.submitTV:

               update(null, false);

                break;
        }
    }

    private void update(Uri uri, boolean isDelete) {
        if (validation()) {

            LoginRegisterRequest req = new LoginRegisterRequest();

            req.name = appUtils.getFieldValue(nameET);
            req.love_push_intention = appUtils.getFieldValue(lovePushET);
                    /*if (relationShipET.getText().toString().equalsIgnoreCase(getString(R.string.single)))
                    {
                        req.relationship="0";
                    }
                    if (relationShipET.getText().toString().equalsIgnoreCase(getString(R.string.in_relationship)))
                    {
                        req.relationship="1";
                    }
                    if (relationShipET.getText().toString().equalsIgnoreCase(getString(R.string.married)))
                    {
                        req.relationship="2";
                    }
                    if (relationShipET.getText().toString().equalsIgnoreCase(getString(R.string.recently_separated)))
                    {
                        req.relationship="3";
                    }
                    if (relationShipET.getText().toString().equalsIgnoreCase(getString(R.string.open_relationship)))
                    {
                        req.relationship="4";
                    }*/
            req.relationship=relationShipET.getText().toString();
            req.love_push_intention=lovePushET.getText().toString();

//                    req.relationship = appUtils.getFieldValue(relationShipET);

//                    Toast.makeText(context, ""+req.relationship+appUtils.getFieldValue(relationShipET), Toast.LENGTH_SHORT).show();

            // req.language = appUtils.getFieldValue(languageET);

            for (Map.Entry<Integer, Boolean> entry : hashMap.entrySet()) {
                Integer key = entry.getKey();
                Boolean value = entry.getValue();
                Log.e("value",""+key+value);
                // do stuff
            }
            req.size = appUtils.getFieldValue(sizeET);
            req.haircolor = appUtils.getFieldValue(hairColorET);
            req.age = appUtils.getFieldValue(ageET);
            req.education = appUtils.getFieldValue(educationET);
            req.job = appUtils.getFieldValue(jobEmployerET);



            req.hobbies = appUtils.getFieldValue(hobbiesET);
            req.sexual_orientation = sexualOrientation;
            req.gender=gender;

            req.aboutme = appUtils.getFieldValue(aboutUsET);

            req.eyecolor = appUtils.getFieldValue(eyeColorET);
            req.address = appUtils.getFieldValue(addressET);

            req.language=languageET.getText().toString();

            req.physique = String.valueOf(physique_pos);

            if (locationList != null) {
                req.country = locationList.get(0);
                req.city = locationList.get(1);
            }
                  /*  req.latitude = latitude;
                    req.longitude = longitude;
*/


            req.profileImage = profileImagePath;

            Log.d("profileImagePath", "update: profileImagePath :"+req.profileImage);
            req.gender = gender;

            AlertDialogs dialogs=new AlertDialogs(this);
            dialogs.show();
            /*List<Uri> path=new ArrayList<>();
            for (int a=0; a<adap.path.size();a++)
            {
                if (adap.path.get(a).getImageUri()!=null)
                {
                    path.add(adap.path.get(a).getImageUri());
                }
            }*/

            if (uri!=null) {
                NetworkRequests.updateProfileImages(this, uri, new ResponseListner() {
                    @Override
                    public <T> void onComplete(Response<T> response) {
                        loginRegisterProfilePresenter.updateProfile(req,new ResponseListner() {
                            @Override
                            public <T> void onComplete(Response<T> response) {
                                GetProfile res = (GetProfile) response.body();
                               if (res.getData()!=null)
                               {
                                   appUtils.setSnackbar(editRoot, res.getMessage());
                                   Picasso.get()
                                           .load(res.getData().getProfileImage())
                                           .placeholder(R.drawable.placeholder_male_square)
                                           .error(R.drawable.placeholder_male_square)
                                           .into(profileIV);
                                   sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res));
                                   adap.add(res.getData().getProfileImages().get(res.getData().getProfileImages().size()-1));
                                   adap.notifyDataSetChanged();
                               }
                                dialogs.dismiss();

                            }

                            @Override
                            public Void onError(String message) {
                                dialogs.dismiss();
                                Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                                return null;
                            }
                        });
                    }

                    @Override
                    public Void onError(String message) {
                        dialogs.dismiss();
                        Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
/*
                        appUtils.loadGlideImage(context, path.toString(), profileIV, R.drawable.placeholder_male_square);
                        loginRegisterProfilePresenter.ProfileAddImages(path);*/
            }else
            {
                /*Update profile*/
                loginRegisterProfilePresenter.updateProfile(req, new ResponseListner() {
                    @Override
                    public <T> void onComplete(Response<T> response) {
                        GetProfile res = (GetProfile) response.body();
                        appUtils.setSnackbar(editRoot, res.getMessage());
                        sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(res).toString());
                        dialogs.dismiss();
                        // Toast.makeText(ProfileEditActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        if (!isDelete)
                        onBackPressed();
                        else
                            NetworkRequests.selectProfileImage(ProfileEditActivity.this, profileResponse.getData().getProfileImages().get(0).getId(), new ResponseListner() {
                                @Override
                                public <T> void onComplete(Response<T> response) {
                                    Log.d(TAG, "onComplete: Profile updated Successfully.");
                                }

                                @Override
                                public Void onError(String message) {
                                    return null;
                                }
                            });
                            Picasso.get()
                                    .load(profileResponse.getData().getProfileImages().get(0).getImage())
                                    .placeholder(R.drawable.placeholder_male_square)
                                    .error(R.drawable.placeholder_male_square)
                                    .into(profileIV);

                    }

                    @Override
                    public Void onError(String message) {
                        dialogs.dismiss();
                        Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
            }




        }
    }


    private void pickCountry() {
        CountryPicker countryPicker =   CountryPicker.newInstance("Select Country");
        countryPicker.show(getSupportFragmentManager(),"COUNTRY_PICKER");
        countryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String s, String s1, String s2, int i) {
//                Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();
                countryPicker.dismiss();
                languageET.setText(s);
//                CountryPicker picker = CountryPicker.newInstance("Select Country");
//                Country country=picker.g
            }
        });




//        countryPicker.showDialog(getSupportFragmentManager()); // Show the dialog
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


    private void manageGender() {
        switch (gender) {
            case GlobalsVariables.GENDER.Male:
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.female:
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.homo:
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                transGenderIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                break;
        }
    }
    private void manageSexualOrientation()
        {
            switch (sexualOrientation) {
                case GlobalsVariables.SEXUAL.Heter:
                    heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                    biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    break;
                case GlobalsVariables.SEXUAL.Bi:
                    biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                    heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    break;
                case GlobalsVariables.SEXUAL.Homos:
                    heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                    homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                    break;
            }

        }



    private boolean validation() {
        if (appUtils.isFieldEmpty(nameET)) {
            appUtils.validationToast(context, nameET, getString(R.string.error_name));
            return false;
        }
        if (sexualOrientation.isEmpty()) {
            Toast.makeText(context, "Select Sexual Orientation", Toast.LENGTH_SHORT).show();
            return false;
        }

       /* if (appUtils.isFieldEmpty(lovePushET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_love_push));
            return false;
        }

        if (appUtils.isFieldEmpty(relationShipET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_relationship));
            return false;
        }
        if (appUtils.isFieldEmpty(languageET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_language));
            return false;
        }
        if (appUtils.isFieldEmpty(sizeET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_please_enter_size));
            return false;
        }

        if (appUtils.isFieldEmpty(educationET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_education));
            return false;
        }

        if (appUtils.isFieldEmpty(jobEmployerET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_job));
            return false;
        }

*//*        if (appUtils.isFieldEmpty(hobbiesET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_hobbies));
            return false;
        }*//*
        if (TextUtils.isEmpty(gender)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_sexual_orientataion));
            return false;
        }
        if (appUtils.isFieldEmpty(aboutUsET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_about));
            return false;
        }*/
        return true;
    }

    private void getManualLocation() {
                  startActivityForResult(PlacesUtils.getPlacesIntent(this), GlobalsVariables.REQUEST_CODE.PLACE_PICKER_REQUEST);
    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {

        if (test instanceof JsonObject) {
            JSONObject object = (JSONObject) test;
            Log.e("onApiResponse: ", object.toString());
        }

        if (test instanceof GetProfile)
        {
            GetProfile deleteProfile = (GetProfile) test;
            appUtils.setSnackbar(editRoot, deleteProfile.getMessage());
            sharedStorage.setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(deleteProfile).toString());
            update(null, true);

        }


    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    public void selectDatePopup(TextView textView) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //For showing date of birth uncomment this
//                        textView.setText(dayOfMonth + " - " + (monthOfYear + 1) + " - " + year);
                        textView.setText(""+getAgeBetweenTwoDate(year,(monthOfYear + 1),dayOfMonth));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

}
