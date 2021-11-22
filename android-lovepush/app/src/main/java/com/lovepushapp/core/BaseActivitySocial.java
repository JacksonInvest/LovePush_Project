package com.lovepushapp.core;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lovepushapp.R;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.model.local.SocialLocalModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by vivek on 31/12/118.
 */

public abstract class BaseActivitySocial extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String[] FACEBOOK_PERMISSIONS = {"public_profile", "user_friends", "email"};
    private static final int SIGN_IN_CODE = 007;
    public int from;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleApiClient;
    private ProgressDialog progress_dialog;
    private AlertDialogs progressDialog;

    private FirebaseAuth mAuth;

    private static final String TAG = "BaseActivitySocial";


    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Signing in....");
        progressDialog = new AlertDialogs(this);

        buildNewGoogleApiClient();

    }



    public void facebookLoginStart() {
        from = 1;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        facebookLoginPerform();

        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(FACEBOOK_PERMISSIONS));

    }


    private void facebookLoginPerform() {
        progressDialog.show();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                if (loginResult.getAccessToken() != null) {

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            (object, response) -> {
                                progressDialog.dismiss();

                                Log.e("log_data", "" + response);
                                try {
                                    JSONObject resObj = response.getJSONObject();

                                    SocialLocalModel socialLocalModel = new SocialLocalModel();
                                    socialLocalModel.id = resObj.getString("id");
                                    //checkSocialLogin(fbUserID);

                                    try {
                                        socialLocalModel.fname = resObj.getString("first_name");

                                    } catch (Exception e) {

                                    }
                                    try {
                                        socialLocalModel.lname = resObj.getString("last_name");

                                    } catch (Exception e) {

                                    }
                                    try {
                                        socialLocalModel.email = resObj.getString("email");

                                    } catch (Exception e) {

                                    }
                                    try {
                                        socialLocalModel.image = "http://graph.facebook.com/" + socialLocalModel.id + "/picture?type=large";
                                    } catch (Exception e) {

                                    }

                                    // String gender = response.getJSONObject().getString("gender");

                                    try {
                                        socialLocalModel.gender = response.getJSONObject().getString("gender");
                                    } catch (Exception e) {

                                    }
                                    socialLocalModel.socialType = SocialLocalModel.TYPE.FACEBOOK;
                                    socialLocalModel.accessToken = loginResult.getAccessToken().getToken();
                                    onSocialSuccess(socialLocalModel);
                                    //Toast.makeText(BaseActivitySocial.this, "Hello", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            });
                    Log.e("log_data", " token:  " + loginResult.getAccessToken().getToken());
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

                    Log.e("log_data", "Connected To Facebook");

                } else {
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();

                Log.e("log_data", "Problem logging into cancel");

                onSocialError("Problem logging into cancel", SocialLocalModel.TYPE.FACEBOOK);
                LoginManager.getInstance().logOut();

            }

            @Override
            public void onError(FacebookException e) {
                progressDialog.dismiss();

                Log.e("log_data", "Problem logging into Facebook" + e.getLocalizedMessage());
                onSocialError("Problem logging into Facebook", SocialLocalModel.TYPE.FACEBOOK);
                LoginManager.getInstance().logOut();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (from) {
            case 1:
                try {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                break;

            case 2:
                if (requestCode == SIGN_IN_CODE) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

//                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                    handleSignInResult(result);

//                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                    handleSignInResult2(task);

//                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                    int statusCode = result.getStatus().getStatusCode();
//                    handleSignInResult(result);


                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        e.printStackTrace();
                        progress_dialog.dismiss();
                    }

//                        GoogleSignInAccount account = task.getResult(ApiException.class);
//                        firebaseAuthWithGoogle(account);
                    return;
                }
                break;
        }


    }

    private void handleSignInResult2(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

//            GoogleSignInAccount acct = task.getSignInAccount();
            Toast.makeText(this, ""+account.getEmail(), Toast.LENGTH_SHORT).show();
            progress_dialog.dismiss();

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GMAIL", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithCredential:success");
//                        progress_dialog.dismiss();
//                        FirebaseUser user = mAuth.getCurrentUser();
////                            handleSignInResult();
//                        updateUI(user, account);
//                        String token=account.getIdToken();
////                            Toast.makeText(BaseActivitySocial.this, ""+token, Toast.LENGTH_SHORT).show();
////                            handleSignInResult(user);
//                    } else {
//                        progress_dialog.dismiss();
//                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "signInWithCredential:failure", task.getException());
////                            Snackbar.make(, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(BaseActivitySocial.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                    }
//
//                    // [START_EXCLUDE]
////                        hideProgressDialog();
//                    // [END_EXCLUDE]
//                });


        progress_dialog.dismiss();
        String photoUrl = "";
        callSocialLoginService(account.getEmail(), account.getDisplayName(), account.getId(), photoUrl, "google",account);
        gPlusSignOut();

    }

    private void updateUI(FirebaseUser user, GoogleSignInAccount account) {

        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.signInButton).setVisibility(View.GONE);
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);

            String photoUrl = "";
            if (user.getPhotoUrl() != null) {
                photoUrl = user.getPhotoUrl().toString();
            }

            callSocialLoginService(user.getEmail(), user.getDisplayName(), user.getUid(), photoUrl, "google",account);
            String token;
            token=account.getIdToken();
//            Toast.makeText(this, "name "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
//          Toast.makeText(this, "Name : "+user.getDisplayName(), Toast.LENGTH_LONG).show();
            gPlusSignOut();

        } else {
            gPlusSignOut();
//            onSocialError("Unable to get details", SocialLocalModel.TYPE.GMAIL);

//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }

    public abstract void onSocialSuccess(SocialLocalModel socialLocalModel);

    public abstract void onSocialError(String error, String type);


    private void buildNewGoogleApiClient() {
        try {
           /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail().build();*/

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail().build();
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .enableAutoManage(this, this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();

            mGoogleApiClient = GoogleSignIn.getClient(this, gso);
//            mAuth = FirebaseAuth.getInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gmailLoginStart() {
        from = 2;
        buildNewGoogleApiClient();
        gPlusSignIn();

    }

    private void gPlusSignIn() {
        gPlusSignOut();
        progress_dialog.show();

//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, SIGN_IN_CODE);
        Intent signInIntent = mGoogleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        progress_dialog.dismiss();
        Log.e( "handleSignInResult: ",""+ result.getStatus());
        Log.e( "handleSignInResult: ",""+ result.getSignInAccount());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String photoUrl = "";
            if (acct.getPhotoUrl() != null) {
                photoUrl = acct.getPhotoUrl().toString();
            }
//            callSocialLoginService(acct.getEmail(), acct.getDisplayName(), acct.getId(), photoUrl, "google");
            gPlusSignOut();
        } else {


            gPlusSignOut();
            onSocialError("Unable to get details", SocialLocalModel.TYPE.GMAIL);
        }
    }

    private void callSocialLoginService(String email, String personName, String userID, String personPhotoUrl, String provider, GoogleSignInAccount acct) {

        SocialLocalModel model = new SocialLocalModel();
        model.fname = personName;
        model.lname = "";

        try {
            if (personName.trim().contains(" ")) {
                String[] personNameArray = personName.split(" ");
                model.fname = personNameArray[0];
                model.lname = personNameArray[1];


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.id = userID;
        model.email = email;
        model.image = personPhotoUrl;
        model.socialType = SocialLocalModel.TYPE.GMAIL;

        new RetrieveTokenTask(model).execute();


    }

    public void gPlusSignOut() {
       /* if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
        }*/
        // Firebase sign out
//        mAuth.signOut();
        // Google sign out
        mGoogleApiClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null, null));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class RetrieveTokenTask extends AsyncTask<Void, Void, String> {


        private SocialLocalModel model;

        public RetrieveTokenTask(SocialLocalModel model) {
            this.model = model;
        }


        @Override
        protected String doInBackground(Void... voids) {
            String accountName = model.email;
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), 100);
            } catch (GoogleAuthException e) {
                Log.e("TAG", e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String accessToken) {
            super.onPostExecute(accessToken);
//            Log.e("Token", accessToken);
//
//            Log.e("accessToken", "-------------->  " + accessToken);

            model.accessToken = accessToken;

            onSocialSuccess(model);
            // login(accessToken, URLHelper.GOOGLE_LOGIN, "google");

        }
    }

//    private void initGoogleClient() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
////                .requestIdToken()
//                .requestId()
//                .requestProfile()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//    }
}
