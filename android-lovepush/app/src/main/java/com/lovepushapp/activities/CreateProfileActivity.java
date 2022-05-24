package com.lovepushapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lovepushapp.R;
import com.lovepushapp.adapters.ProfileImagesAdapter;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.local.LoginRegisterRequest;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.NetworkRequests;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;
import com.lovepushapp.quickBloxTest.utils.SubscribeToNotification;
import com.lovepushapp.quickBloxTest.utils.ToastUtils;
import com.lovepushapp.videoTestJava.services.LoginService;
import com.lovepushapp.videoTestJava.util.QBResRequestExecutor;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
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
import retrofit2.Response;

import static com.lovepushapp.core.utils.Util.getAgeBetweenTwoDate;

public class CreateProfileActivity extends BaseActivity implements LoginMvp {
    private String TAG = LoginActivity.class.getSimpleName();

    int profilePicturePosition=0;

    private static final int RESULT_LOAD_IMAGE = 121;
    @Inject
    Util util;

    String from2;
    String profileImagePath2 = "";
    @BindView(R.id.screenTitleTV)
    TextView screenTitleTV;
    @BindView(R.id.nameET)
    AppCompatEditText nameET;
    @BindView(R.id.ageET)

    AppCompatEditText ageET;
    @BindView(R.id.lovePushET)
    AppCompatEditText lovePushET;
    @BindView(R.id.relationShipET)
    AppCompatEditText relationShipET;
    @BindView(R.id.maleIV)
    ImageView maleIV;

    @BindView(R.id.chooseIV)
    ImageView chooseIV;
    @BindView(R.id.profileIV)
    ImageView profileIV;
    List<Uri> profileImagePath;
    @BindView(R.id.femaleIV)
    ImageView femaleIV;
    @BindView(R.id.transGenderIV)
    ImageView homeSexualIV;
    @BindView(R.id.languageET)
    AppCompatEditText languageET;
    @BindView(R.id.sizeET)
    AppCompatEditText sizeET;
    @BindView(R.id.physiqueET)
    AppCompatEditText physiqueET;
    @BindView(R.id.eyeColorET)
    AppCompatEditText eyeColorET;
    @BindView(R.id.hairColorET)
    AppCompatEditText hairColorET;
    @BindView(R.id.fvrttravel_ET)
    AppCompatEditText fvrt_ET;
    @BindView(R.id.jobEmployerET)
    AppCompatEditText jobEmployerET;
    @BindView(R.id.hobbiesET)
    AppCompatEditText hobbiesET;
    @BindView(R.id.religionET)
    AppCompatEditText religionET;
    @BindView(R.id.aboutUsET)
    AppCompatEditText aboutUsET;
    @BindView(R.id.backIV)
    ImageView backIV;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;
    private String sexualOrientation = "";
    private String gender="";
    String relationship_pos = "",physique_pos = "";

    @BindView(R.id.heteroSexualIV)
    ImageView heteroSexualIV;
    @BindView(R.id.biSexualIV)
    ImageView biSexualIV;
    @BindView(R.id.homeSexualIV)
    ImageView homeSexual2IV;

    @BindView(R.id.usernameTV)
    AppCompatTextView usernameTV;

    @BindView(R.id.usernameLL)
    LinearLayout usernameLL;

    HashMap<Integer, Boolean> hashMap;
    @BindView(R.id.addImagesLL)
    LinearLayout addImagesLL;
    ProfileImagesAdapter adap;
    @BindView(R.id.scrollview)
    ScrollView scrollView;

    @BindView(R.id.profileimages_RV)
    RecyclerView profileimagesRV;
    private GetProfile profileResponse;


    private AlertDialogs progressDialog;
    private QBUser userForSave;
    int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;
    int EXTRA_LOGIN_RESULT_CODE = 1002;
    int SELECT_PHOTO=45;
    protected QBResRequestExecutor requestExecutor;
    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    @Override
    public int getLayoutId() {
        return R.layout.activity_create_profile;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.create_profile));
    }

    private Activity context = CreateProfileActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, CreateProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileImagePath=new ArrayList<>();
        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);
        hashMap=new HashMap<>();
        progressDialog = new AlertDialogs(context);
        requestExecutor = MyApplication.getInstance().getQbResRequestExecutor();
        nameET.setText((String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.name, GlobalsVariables.STORAGE.TYPE_STRING));
        backIV.setVisibility(View.GONE);
        String username =  (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.username, GlobalsVariables.STORAGE.TYPE_STRING);

        if (username == null || username.equals("")){
            usernameLL.setVisibility(View.GONE);
        }else {
            usernameLL.setVisibility(View.VISIBLE);
            usernameTV.setText(username);
        }

        String gender = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.Gender, GlobalsVariables.STORAGE.TYPE_STRING);
        if (gender.equals("1"))
        {
            gender = GlobalsVariables.GENDER.Male;
            manageGender();

        }

        else if (gender.equals("3"))
        {
            gender = GlobalsVariables.GENDER.homo;
            manageGender();
        }
        else
        {
            gender = GlobalsVariables.GENDER.female;
            manageGender();
        }

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

        chooseIV.setOnClickListener(v->{
            String[] permissions1 = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            Permissions.check(context/*context*/, permissions1, null/*rationale*/, null/*options*/, new PermissionHandler() {
                @Override
                public void onGranted() {
                    //Crop.pickImage(CreateProfileActivity.this);
                    from2="chooseIV";
                    ImagePicker.create(CreateProfileActivity.this)
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
                            .start();

        }
    });

        });


        setAdapterRecent(profileImagePath);
    }


    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof GetProfile) {
            GetProfile res = (GetProfile) test;

            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                appUtils.showToast(res.getMessage());
                gotoDashBoard(res.getData().getEmail(),res.getData().getName());
            } else {
                appUtils.setSnackbar(screenTitleTV, res.getMessage());
            }

        }

        if (test instanceof JsonObject) {
            JSONObject object = (JSONObject) test;
            Log.e("onApiResponse: ", object.toString());
        }



    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

   /* @OnClick({R.id.backIV, R.id.submitTV, R.id.relationShipET, R.id.maleLL, R.id.femaleLL, R.id.homeSexualLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relationShipET:


                break;
            case R.id.maleLL:

                break;
            case R.id.femaleLL:

                break;
            case R.id.homeSexualLL:

                break;
            case R.id.backIV:
                goback();
                break;
            case R.id.submitTV:



                break;
        }
    }*/

    private void manageGender() {
        switch (gender) {
            case GlobalsVariables.GENDER.Male:
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.female:
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.GENDER.homo:
                maleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                femaleIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                break;
        }
    }
    private void manageSexualOrientation()
    {
        switch (sexualOrientation) {
            case GlobalsVariables.SEXUAL.Heter:
                heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexual2IV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.SEXUAL.Bi:
                biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexual2IV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                break;
            case GlobalsVariables.SEXUAL.Homos:
                heteroSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                biSexualIV.setImageDrawable(getResources().getDrawable(R.drawable.radio_unfilled));
                homeSexual2IV.setImageDrawable(getResources().getDrawable(R.drawable.radio_filled));
                break;
        }

    }

    private void gotoDashBoard(String email,String name) {
        /*AlertDialogs progressDialog = new AlertDialogs(context);
        progressDialog.show();
        final QBUser user = new QBUser();

        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setPassword(GlobalsVariables.quickblox_password);
        //user.setCustomData(profile_url);
        Log.e("LOGINQUICKBLOX", String.valueOf(user));


        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {


            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);

                SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                progressDialog.dismiss();
                appUtils.startActivity(context,
                        DashboardActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );

                // ProgressDialogFragment.hide(getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
                //ProgressDialogFragment.hide(getFragmentManager());
                ErrorUtils.showSnackbar(nameET, R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoDashBoard();
                            }
                        });
            }
        });*/

        userForSave = createUserWithEnteredData(email,name);
        startSignUpNewUser(userForSave);

    }


    private QBUser createUserWithEnteredData(String email,String name) {
        return createQBUserWithCurrentData(email,name);
    }

    private QBUser createQBUserWithCurrentData(String email,String name) {
        final QBUser user = new QBUser();

        String my_user_id=String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));

        user.setId(Integer.parseInt(my_user_id));
        user.setLogin(my_user_id);
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(GlobalsVariables.quickblox_password);
        return user;
    }

    private void startSignUpNewUser(final QBUser newUser) {
        Log.d(TAG, "SignUp New User");
//        progressDialog.show();
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "SignUp Successful");
                        saveUserData(newUser);
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    progressDialog.dismiss();
                                    startSignUpNewUser(newUser);
                                }
                            },3000);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(GlobalsVariables.quickblox_password);
        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EXTRA_LOGIN_RESULT_CODE) {

            progressDialog.dismiss();

            boolean isLoginSuccess = data.getBooleanExtra(EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);
                signInCreatedUser(userForSave);
            } else {
                ToastUtils.longToast(getString(R.string.login_chat_login_error) + errorMessage);

            }
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
            handleCrop1(resultCode, data);
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: me getting called fro crop image activity code");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Log.d(TAG, "onActivityResult: result "+result.getUri());
                profileImagePath.add(result.getUri());
//                adap.addUri(result.getUri());
                adap.notifyDataSetChanged();

                Glide.with(this)
                        .load(profileImagePath.get(0))
                        .into(profileIV);
            }

        }


    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"+System.currentTimeMillis()));
        Crop.of(source, destination).asSquare().start(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void signInCreatedUser(final QBUser qbUser) {
        Log.d(TAG, "SignIn Started");
        requestExecutor.signInUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.d(TAG, "SignIn Successful");
                // sharedPrefsHelper.saveQbUser(userForSave);
                SharedPrefsHelper.getInstance().saveQbUser(user);
                updateUserOnServer(qbUser);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d(TAG, "Error SignIn" + responseException.getMessage());
                progressDialog.dismiss();
                //hideProgressDialog();
                ToastUtils.longToast(R.string.sign_in_error_with_error);
            }
        });
    }

    private void updateUserOnServer(QBUser user) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                progressDialog.dismiss();

                SubscribeToNotification.getInstance().setSubscribeToNotification(context);
                progressDialog.dismiss();

                appUtils.startActivity(context,
                        DashboardActivity.createIntent(context),
                        false,
                        GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                        false,
                        0
                );
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();

                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }

    private boolean validation() {
        if (appUtils.isFieldEmpty(nameET)) {
            appUtils.validationToast(context, nameET, getString(R.string.error_name));
            return false;
        }
        else if (nameET.getText().toString().length() < 3) {
            appUtils.validationToast(context, nameET, getString(R.string.error_name));
            return false;
        }

        else if (appUtils.isFieldEmpty(ageET)) {
            appUtils.validationToast(context, ageET, getString(R.string.error_dob));
            return false;
        }

      /*  if (appUtils.isFieldEmpty(lovePushET)) {
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
        if (appUtils.isFieldEmpty(eyeColorET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_eye_color));
            return false;
        }
        if (appUtils.isFieldEmpty(hairColorET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_hair_color));
            return false;
        }
        if (appUtils.isFieldEmpty(educationET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_education));
            return false;
        }

        if (appUtils.isFieldEmpty(jobET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_job));
            return false;
        }
        if (appUtils.isFieldEmpty(employerET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_enter_employer));
            return false;
        }
        if (appUtils.isFieldEmpty(hobbiesET)) {
            appUtils.validationToast(context, screenTitleTV, getString(R.string.error_hobbies));
            return false;
        }
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

    @OnClick({R.id.backIV, R.id.ageET, R.id.lovePushET, R.id.relationShipET, R.id.maleLL, R.id.femaleLL, R.id.transGenderLL, R.id.physiqueET, R.id.submitTV,R.id.addImagesLL,R.id.homeSexualLL,R.id.biSexualLL,R.id.heteroSexualLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                goback();
                break;
            case R.id.ageET:

                selectDatePopup(ageET);

                // Select Age Removed and this field changes to Select Date of birth
                /*dialogUtils.openBottomDialog(appUtils, context, appUtils.getAge(), 0, new OnItemClickListenerCutom() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object o) {
                        String res = (String) o;
                        ageET.setText(res);
                    }
                });*/
                break;
            case R.id.lovePushET:
                lovePushIntention();
                /*dialogUtils.openBottomMultiOptionSelectDialog(appUtils, context, appUtils.getLovePushIntention(context), 0, new OnItemClickListenerCutom() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object o) {
                        if (o instanceof String) {
                            String selectedValues = (String) o;
                            ArrayList<MultiOptionModel> optionModels = new Gson().fromJson(selectedValues, new TypeToken<ArrayList<MultiOptionModel>>() {
                            }.getType());

                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < optionModels.size(); i++) {

                                if (optionModels.get(i).isSelected) {
                                    if (TextUtils.isEmpty(builder.toString())) {
                                        builder.append(optionModels.get(i).name);
                                    } else {
                                        builder.append(GlobalsVariables.COMMA_SEPERATOR + optionModels.get(i).name);
                                    }
                                }
                            }
                            lovePushET.setText(builder.toString());
                        }

                    }
                });*/
                break;
            case R.id.relationShipET:
                dialogUtils.openBottomDialog(appUtils, context, appUtils.getRelationShipStatus(context), 0, new OnItemClickListenerCutom() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object o) {
                        String res = (String) o;
                        relationShipET.setText(res);
                        relationship_pos = String.valueOf(position+1);
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

            case R.id.physiqueET:
                dialogUtils.openBottomDialog(appUtils, context, appUtils.getPhysique(context), 0, (view1, position, type, o) -> {
                    String res = (String) o;
                    physiqueET.setText(res);
                    physique_pos = String.valueOf(position+1);


                });
                break;
            case R.id.submitTV:
                if (validation()) {

                    LoginRegisterRequest req = new LoginRegisterRequest();
                    req.name = appUtils.getFieldValue(nameET);
                    req.love_push_intention = appUtils.getFieldValue(lovePushET);
                    req.relationship = relationship_pos;
                    req.language = appUtils.getFieldValue(languageET);
                    req.size = appUtils.getFieldValue(sizeET);
                    req.haircolor = appUtils.getFieldValue(hairColorET);
                    req.education = appUtils.getFieldValue(fvrt_ET);
                    req.job = appUtils.getFieldValue(jobEmployerET);
                    req.hobbies = appUtils.getFieldValue(hobbiesET);
                    req.sexual_orientation = sexualOrientation;
                    req.aboutme = appUtils.getFieldValue(aboutUsET);
                    req.user_id = getLoginModel().getData().getId() + "";
                    req.eyecolor = appUtils.getFieldValue(eyeColorET);
                    req.age = appUtils.getFieldValue(ageET);
                    req.physique = physique_pos;
                    req.religion = appUtils.getFieldValue(religionET);

                    Log.e( "onViewClicked: ",req.physique  +  req.relationship );
                 /*if (profileImagePath==null)
                 {
                     loginRegisterProfilePresenter.hitCreateProfile(req);
                 }
                 else
                 {
                     withImage(req);
                 }*/
                    withImages(req);
                    //loginRegisterProfilePresenter.hitCreateProfile(req);



                }
                break;
            case R.id.addImagesLL :
            String[] permissions1 = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            Permissions.check(context/*context*/, permissions1, null/*rationale*/, null/*options*/, new PermissionHandler() {
                @Override
                public void onGranted() {

                    from2="images";
                    ImagePicker.create(CreateProfileActivity.this)
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
        }
    }

    private void withImage(LoginRegisterRequest req,Uri uri) {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();
        NetworkRequests.createProfile(this, uri, req, new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body() instanceof GetProfile) {
                        GetProfile res = (GetProfile) response.body();

                        if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                            loginRegisterProfilePresenter.manageGetProfile(res);
                            appUtils.showToast(res.getMessage());
                            gotoDashBoard(res.getData().getEmail(),res.getData().getName());
                        } else {
                            appUtils.setSnackbar(screenTitleTV, res.getMessage());
                        }

                    }
                }
            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private void withImages(LoginRegisterRequest req) {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();


        Uri uri = null;
        if(profileImagePath!=null && profileImagePath.size() > 0){
            uri = profileImagePath == null ? null : profileImagePath.get(profilePicturePosition);
            profileImagePath.remove(profilePicturePosition);
        }else {
            loginRegisterProfilePresenter.hitCreateProfile(req);
            dialogs.dismiss();
            return;
        }

        if(profileImagePath!=null && profileImagePath.size() > 0) {
            Uri finalUri = uri;

            NetworkRequests.createProfileImages(this, profileImagePath, new ResponseListner() {
                @Override
                public <T> void onComplete(Response<T> response) {
//                    dialogs.dismiss();

                    if (response.isSuccessful()) {

                        withImage(req, finalUri);

                        if (response.body() instanceof GetProfile) {
                            GetProfile res = (GetProfile) response.body();

                            if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                                loginRegisterProfilePresenter.manageGetProfile(res);
                                appUtils.showToast(res.getMessage());
                                gotoDashBoard(res.getData().getEmail(),res.getData().getName());
                            } else {
                                appUtils.setSnackbar(screenTitleTV, res.getMessage());
                            }

                        }
                    }
                }

                @Override
                public Void onError(String message) {
                    dialogs.dismiss();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    return null;
                }
            });
        }
        else {
            withImage(req,uri);
        }
    }

    private void lovePushIntention() {
        boolean check1 = false;
        boolean check2=false;
        boolean check3=false;
        boolean check4=false;
        boolean check5=false;
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

                if (parts[i].equalsIgnoreCase(" Dating")||parts[i].equalsIgnoreCase("Dating")) {
//                            int1 = parts[0];
                    ar[i] = parts[i];
                    check5=true;
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

//                    int1=stringTokenizer.nextToken();
//                int2=stringTokenizer.nextToken();
//                int3=stringTokenizer.nextToken();
//                int4=stringTokenizer.nextToken();
                /*if (!TextUtils.isEmpty(ar[0]))
                {
                    check1=true;
//                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(ar[1]))
                {
                    check2=true;
//                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

                }
                if (!TextUtils.isEmpty(ar[2]))
                {
                    check3=true;
//                    Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();

                }
                if (!TextUtils.isEmpty(ar[3]))
                {
                    check4=true;
//                    Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();

                }*/

        }

        catch (Exception e)
        {
            Log.e("EXCEPTION", String.valueOf(e));
        }
        openIntention(check1,check2,check3,check4,check5);

    }

    private void openIntention(boolean check1, boolean check2, boolean check3, boolean check4,boolean check5) {
        String[] item = {"Relationship", "Friendship", "Daily Activities", "No Expectation", "Dating"};
        final List<String> itemList = Arrays.asList(item);


        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lovepush Intention");



// add a checkbox list
        String[] animals = {"Relationship", "Friendship", "Daily Activities", "No Expectation"};
        boolean[] checkedItems = {check1, check2, check3, check4, check5};
        builder.setMultiChoiceItems(item, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                Toast.makeText(context, ""+isChecked+which, Toast.LENGTH_SHORT).show();
                hashMap.put(which,isChecked);
                checkedItems[which] = isChecked;



//                itemList[which] = String.valueOf(isChecked);

                /*int count = itemList.size();
                String[] titles = new String[count];
                boolean[] checked = new boolean[count];
                for (int i=0; i<count; i++) {
                    checked[i]=true;
                    if (checked[i]==isChecked) {
                        titles[i] = itemList.get(i);
                        Log.e("LIST", itemList.get(i));
                    }
//                    checked[i] = itemList.get(i);
                }*/

                // Get the current focused item
//                String currentItem = itemList.get(which);
//                lovePushET.setText(currentItem);

//                builder.getContext().get;




                // user checked or unchecked a box
            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder stringBuilder=new StringBuilder();
                // user clicked OK
                int i=0;
                boolean[] in = new boolean[5];
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
                StringBuilder builder=new StringBuilder();
                lovePushET.setText("");
                builder.setLength(0);

                for (int j = 0; j<checkedItems.length; j++){
//                    Toast.makeText(context, ""+checkedItems[j], Toast.LENGTH_SHORT).show();
                    boolean checked = checkedItems[j];
                    if (checked) {
//                        Toast.makeText(context, ""+itemList.get(j), Toast.LENGTH_SHORT).show();
                        String s=itemList.get(j);

                        builder.append(itemList.get(j) + ", ");
                      //  lovePushET.setText(lovePushET.getText() + );
                    }
                }
                lovePushET.setText(builder.substring(0,builder.length()-2));

                /*if (in[0]==true)
                {
                    stringBuilder=new StringBuilder();
                    stringBuilder.append(relationship);
                }
                if (in[1]==true)
                {
                    if (in[0]==true)
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(relationship+", "+friednship);
                    }
                    stringBuilder=new StringBuilder();
                    stringBuilder.append(friednship);
                }
                if (in[2]==true)
                {
                    stringBuilder=new StringBuilder();
                    stringBuilder.append(daily);
                    if (in[0]==true)
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(relationship+", "+daily);

                        if (in[1]==true)
                        {
                            stringBuilder=new StringBuilder();
                            stringBuilder.append(relationship+", "+friednship+", "+daily);
                        }
                    }
                    if (in[1]==true)
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(friednship+", "+daily);
                    }


                }
                if (in[3]==true)
                {
                    stringBuilder=new StringBuilder();
                    stringBuilder.append(Noexpectation);
                    if (in[0]==true)
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(relationship+", "+Noexpectation);

                        if (in[1]==true)
                        {
                            stringBuilder=new StringBuilder();
                            stringBuilder.append(relationship+", "+friednship+", "+Noexpectation);
                            if (in[2])
                            {
                                stringBuilder=new StringBuilder();
                                stringBuilder.append(relationship+", "+friednship+", "+daily+", "+Noexpectation);
                            }
                        }

                    }
                    if (in[1]==true)
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(friednship+", "+Noexpectation);

                        if (in[2])
                        {
                            stringBuilder=new StringBuilder();
                            stringBuilder.append(friednship+", "+daily+", "+Noexpectation);

                        }
                    }
                    if (in[2])
                    {
                        stringBuilder=new StringBuilder();
                        stringBuilder.append(daily+", "+Noexpectation);
                        if (in[0])
                        {
                            stringBuilder=new StringBuilder();
                            stringBuilder.append(relationship+", "+daily+", "+Noexpectation);

                        }
                    }
                }*/

            }
        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

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

    private void setAdapterRecent(List<Uri> uriList) {

        try {
            profileimagesRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            adap = new ProfileImagesAdapter(context,"from_create_profile", uriList, new OnItemClickListenerCutom() {
                @Override
                public void onItemClick(View view, int position, int type, Object o) {
                    Log.e( "onItemClick: ", String.valueOf(type));

                    if(view.getId() == R.id.img_categoryImage_close){
//                        profileImagePath.remove(position);

                        /*if(profilePicturePosition==position){
                            profilePicturePosition=position;

                        }else if(profilePicturePosition > position){
                            profilePicturePosition--;
                        }else {
                            profilePicturePosition++;
                        }*/

                        if(profilePicturePosition==position){
                            profilePicturePosition=0;
                        }

                        Log.d(TAG, "onItemClickDelete profilePicturePosition : "+profilePicturePosition  + "position "+position);
                        Picasso.get()
                                .load(profileImagePath.get(profilePicturePosition))
                                .placeholder(R.drawable.placeholder_male_square)
                                .error(R.drawable.placeholder_male_square)
                                .into(profileIV);
                        adap.notifyDataSetChanged();
                    }

                       // loginRegisterProfilePresenter.deleteprofile(String.valueOf(type));
                    if (view.getId() == R.id.img_categoryImage){
                        Picasso.get()
                                .load(profileImagePath.get(position))
                                .placeholder(R.drawable.placeholder_male_square)
                                .error(R.drawable.placeholder_male_square)
                                .into(profileIV);
                        profilePicturePosition=position;
                        Log.d(TAG, "onItemClick: profile picture position "+profilePicturePosition);
//                        profileImagePath = profileResponse.getData().getProfileImages().get(position).getImage();

                    }


                }
            });
            profileimagesRV.setAdapter(adap);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void handleCrop1(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = Crop.getOutput(result);
            if(from2!=null && from2.equals("chooseIV")){
                profileImagePath.add(0,selectedImageUri);
                Picasso.get()
                        .load(profileImagePath.get(0))
                        .placeholder(R.drawable.placeholder_male_square)
                        .error(R.drawable.placeholder_male_square)
                        .into(profileIV);
                from2="";
                adap.notifyDataSetChanged();
            }else {
                profileImagePath.add(selectedImageUri);
                adap.notifyDataSetChanged();
                if(profileImagePath.size()==1){
                    Picasso.get()
                            .load(profileImagePath.get(0))
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(profileIV);
                }
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}
