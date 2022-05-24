package com.lovepushapp.core.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lovepushapp.R;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.core.MyApplication;
import com.lovepushapp.model.response.LoginResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


public class Util {

    public ArrayList<String> getAppMinutes() {
        ArrayList<String> item = new ArrayList<>();
        for (int i = 1; i <= 59; i++) {

            if (i < 10) {
                item.add("0" + i);
            } else {
                item.add(i + "");
            }


        }
        return item;
    }

    public static String getAgeBetweenTwoDate(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(month == (today.get(Calendar.MONTH)+1) && day > today.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return String.valueOf(age);
    }


    public static DisplayMetrics display(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
    public ArrayList<String> getAppYears() {
        ArrayList<String> item = new ArrayList<>();
        for (int i = 1980; i < 2020; i++) {
            item.add(i + "");
        }
        return item;
    }

    public ArrayList<String> getAppMileage() {
        ArrayList<String> item = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            item.add(i + "");
        }
        return item;
    }


    public ArrayList<String> getAppHours() {
        ArrayList<String> item = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {

            if (i < 10) {
                item.add("0" + i);
            } else {
                item.add(i + "");
            }
        }
        return item;
    }

    public ArrayList<String> getAppAmPm() {
        ArrayList<String> item = new ArrayList<>();

        item.add("AM");
        item.add("PM");

        return item;
    }

    public ArrayList<String> getMediaOption() {
        ArrayList<String> item = new ArrayList<>();
        item.add("Camera");
        item.add("Gallery");

        return item;
    }


    public ArrayList<String> getAge() {
        ArrayList<String> item = new ArrayList<>();
        for (int i = 18; i < 99; i++) {
            item.add(i + "");
        }
        return item;
    }

    public ArrayList<String> getLang() {
        ArrayList<String> item = new ArrayList<>();
        item.add("English");
        item.add("Hindi");
        return item;
    }


    public ArrayList<String> getLovePushIntention(Context context) {
        ArrayList<String> item = new ArrayList<>();
        item.add(context.getResources().getString(R.string.relationship));
        item.add(context.getResources().getString(R.string.friendship));
        item.add(context.getResources().getString(R.string.daily_activities));
        item.add(context.getResources().getString(R.string.no_expactations));
        return item;
    }

    public ArrayList<String> getRelationShipStatus(Context context) {
        ArrayList<String> item = new ArrayList<>();
        item.add(context.getResources().getString(R.string.single));
        item.add(context.getResources().getString(R.string.in_relationship));
        item.add(context.getResources().getString(R.string.married));
        item.add(context.getResources().getString(R.string.recently_separated));
        item.add(context.getResources().getString(R.string.open_relationship));
        return item;
    }

    public ArrayList<String> getSexualOrientation(Context context) {
        ArrayList<String> item = new ArrayList<>();
        item.add(context.getResources().getString(R.string.heterosexual));
        item.add(context.getResources().getString(R.string.bisexual));
        item.add(context.getResources().getString(R.string.homosexual));
        return item;
    }


    public ArrayList<String> getPhysique(Context context) {
        ArrayList<String> item = new ArrayList<>();
        item.add(context.getResources().getString(R.string.plump));
        item.add(context.getResources().getString(R.string.thin));
        item.add(context.getResources().getString(R.string.athletic));
        item.add(context.getResources().getString(R.string.muscular));
        item.add(context.getResources().getString(R.string.normal));

        return item;
    }


    public String getParseDate(String inputDateStr) {
        try {
            DateFormat formatServerDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat formatTarget = new SimpleDateFormat("MM-dd-yyyy");

            Date serverDate = formatServerDate.parse(inputDateStr);


            return formatTarget.format(serverDate);


        } catch (Exception e) {
            return inputDateStr
                    ;
        }
    }


    public RequestBody toRequestBodyForImage(File value) {
        if (value.getPath().contains(".png")) {
            return RequestBody.create(MediaType.parse("image/png"), value);
        } else {
            return RequestBody.create(MediaType.parse("image/jpg"), value);
        }
    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void addFragment(Fragment fragment, FragmentManager fragmentManager, int containerId, boolean isAdd) {
        if (isAdd) {
            BaseFragment.CURRENT_FRAGMENT = fragment.getClass().getName();
        }
        String backStateName = fragment.getClass().getSimpleName();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerId, fragment);
        // ft.addToBackStack(backStateName);
        ft.commit();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    public static int getDiffYears(Date first) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(new Date());
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }

    public static Date convertUtcToGMT(Date date) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    public static RequestBody toRequestBodyMultipartText(String value) {
        if (TextUtils.isEmpty(value)) {

            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void loadGlideImageBitmap(Context context, String url, View view, int placeHolder) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(placeHolder);
//        requestOptions.error(placeHolder);
        if (view instanceof CircleImageView) {
            Glide.with(context).load(url).into((CircleImageView) view);
        } else if (view instanceof ImageView) {
            Glide.with(context).load(url).into((ImageView) view);
        }
    }


    public void loadGlideImage(Context context, Uri uri, View view, int placeHolder) {
        if (TextUtils.isEmpty(uri.toString())) {
            return;
        }
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(placeHolder);
//        requestOptions.error(placeHolder);
        if (view instanceof CircleImageView) {
            Glide.with(context).load(uri).into((CircleImageView) view);
        } else if (view instanceof ImageView) {
            Glide.with(context).load(uri).into((ImageView) view);
        }
    }

    public void loadGlideImage(Context context, String url, View view, int placeHolder) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(placeHolder);
//        requestOptions.error(placeHolder);
        if (view instanceof CircleImageView) {
            Glide.with(context).load(url).into((CircleImageView) view);
        } else if (view instanceof ImageView) {
            Glide.with(context).load(url).into((ImageView) view);
        }
    }

    public void startActivity(Activity context, Intent intent, boolean isFinish, int animType, boolean isOnActivity, int code) {
        if (isOnActivity) {
            context.startActivityForResult(intent, code);
        } else {
            context.startActivity(intent);
        }
        if (isFinish) {
            context.finish();
        }
        anim(animType, context);
    }

/*
    public void startActivity(Activity context, View view, String transitionName, Intent intent, boolean isOnActivity, int code) {
        ActivityOptions options = null;
        intent.putExtra(GlobalsVariables.BUNDLE.ARG_TRANSITION_IMAGE, transitionName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(context, view, transitionName);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (isOnActivity) {
                context.startActivityForResult(intent, code, options.toBundle());
            } else {
                context.startActivity(intent, options.toBundle());
            }
        } else {
            if (isOnActivity) {
                context.startActivityForResult(intent, code);
            } else {
                context.startActivity(intent);
            }

        }
    }
*/

    public void anim(int b, Activity context) {
        switch (b) {
            case GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT:
                context.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;
            case GlobalsVariables.ANIM_TYPE.LEFT_TO_RIGHT:
                context.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
            case GlobalsVariables.ANIM_TYPE.DOWN_TO_TOP:
                context.overridePendingTransition(R.anim.pull_in_down, R.anim.push_out_up);
                break;
            case GlobalsVariables.ANIM_TYPE.TOP_TO_DOWN:
                context.overridePendingTransition(R.anim.pull_in_up, R.anim.push_out_down);
                break;
            case GlobalsVariables.ANIM_TYPE.ONLY_DOWN:
                context.overridePendingTransition(R.anim.stay, R.anim.push_out_down);
                break;
            case GlobalsVariables.ANIM_TYPE.ONLY_TOP:
                context.overridePendingTransition(R.anim.pull_in_down, R.anim.stay);
                break;
            case GlobalsVariables.ANIM_TYPE.NONE:
                context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    public void validationToast(Context context, View view, String message) {

        try {
            // hideKeyBoard(context, view);
            Snackbar snackbar1 = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar1.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            TextView tv = (TextView) snackbarView.findViewById(R.id.snackbar_text);
            tv.setTextColor(ContextCompat.getColor(context, R.color.white));
            snackbar1.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyBoard(Context activity, View view) {

        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String parseErrorMessage(Response response) {
        try {
            String string = response.errorBody().string();
            JSONObject jsonObject = new JSONObject(string);
            return jsonObject.optString("message");
        } catch (Exception e) {
            return "Something went wrong";
        }
    }


    public void showToast(String message) {
        if (message == null || message.isEmpty()) return;
     /*   AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setTitle("Message");
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();*/
        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message, int length) {
        Toast.makeText(MyApplication.getInstance(), message, length).show();
    }

    public void setLog(String message) {
        Log.e(ResourceUtils.getString(R.string.app_name), message);
    }

    public void setSnackbar(View view, String message) {
        Snackbar snackbar1 = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar1.show();
    }


    public int getScreenSizeHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        return height;
    }

    public int getScreenSizeWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        return width;
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public boolean isContainHttpUrl(String datum) {
        if (datum.contains("https") || datum.contains("http")) {
            return true;
        }
        return false;
    }

    public String getDoubleInt(int result) {
        DecimalFormat form = new DecimalFormat("00");
        return form.format(result);
    }

    public int getInt(boolean b) {
        return b ? 1 : 0;
    }

    public int getInt(int b) {
        return b <= 0 ? 0 : b;
    }

    public boolean getBoolean(int b) {
        return b == 1 ? true : false;
    }

    public String objectToJson(Object o) {
        return new Gson().toJson(o);
    }

    public String objectToJsonString(Object o) {
        return new Gson().toJson(o).toString();
    }

    public Object jsonTOObject(String json, TypeToken token) {
        // TypeToken typeToken = new TypeToken<Post>() {
        //};
        return new Gson().fromJson(json, token.getType());
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public String getEditTextValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public String getTextViewValue(TextView textView) {
        return textView.getText().toString().trim();
    }

    public String distance(double destination, boolean isMiles)
            throws IllegalArgumentException {
        if (destination < 0) {
            throw new IllegalArgumentException();
        }
        return String.format(new DecimalFormat("#.##").format(
                isMiles ? destination / 0.621371192 : destination * 1.609344));
    }


    public void hideKeyBoard(Activity activity) {

        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getToken(String timeStamp, String sessionID) {
        Log.e("Tag", " SECRET_KEY " + API_GLOBALS.SECRET_KEY + " timeStamp " + timeStamp + " Id " + sessionID);
        try {
            String combinedString = "";
            if (TextUtils.isEmpty(sessionID)) {
                combinedString = API_GLOBALS.SECRET_KEY + timeStamp;
            } else {
                combinedString = sessionID + API_GLOBALS.SECRET_KEY + timeStamp;
            }
            return SHA1(combinedString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateAndTime(Date date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String currentDateTime = dateFormat.format(date); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getRandom(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = null;
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public String get_photo_pathString(Context context, Uri path) {
        String mCurrentPhotoPath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(path, filePathColumn, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            mCurrentPhotoPath = null;
            return mCurrentPhotoPath;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (columnIndex < 0) {
            mCurrentPhotoPath = null;
            return mCurrentPhotoPath;
        }
        File mCurrentPhoto = new File(cursor.getString(columnIndex));
        mCurrentPhotoPath = mCurrentPhoto.getAbsolutePath();
        cursor.close();
        return mCurrentPhotoPath;
    }

   /* public void openDialog(Activity activity, final String[] name, int[] icons, final OnBottomDialogItemListener listener, final int action) {
        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_list_botton_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        int height = activity.getResources().getDisplayMetrics().heightPixels;
        RecyclerView list_view = (RecyclerView) child.findViewById(R.id.list_view);
        int listHeight = dpToPx(name.length * 60);
        list_view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, listHeight < (height / 2) ? listHeight : height / 2));
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setAdapter(new OpenBottonDialogAdapter(name, icons, new OnItemClickListenerCutom() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                switch (type) {
                    case GlobalsVariables.TYPE.APAPTER_BOTTOM_DIALOG_CLICK:
                        String s = (String) o;
                        listener.onItemClick(view, position, action, o);
                        mBottomSheetDialog.dismiss();
                        break;
                }
            }
        }));
    }*/

    /*public void showTwoButtonDialog(final Activity c, String dialog_info, final String cancel_info, String ok_info, final OnAlertDialogItemListener dialogInterface, final int type) {
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.view_alert_two_button);
        RelativeLayout btnOK = (RelativeLayout) dialog.findViewById(R.id.btnOK);
        RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.btnCancel);
        CustomTextView txtMessage = (CustomTextView) dialog.findViewById(R.id.txtMessage);
        CustomTextView txtButtonOK = (CustomTextView) dialog.findViewById(R.id.txtButtonOK);
        CustomTextView txtButtonCancel = (CustomTextView) dialog.findViewById(R.id.txtButtonCancel);
        txtMessage.setText(dialog_info);
        txtButtonOK.setText(ok_info);
        txtButtonCancel.setText(cancel_info);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogInterface.onClickDone(type, null);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogInterface.onClickCancel(type, null);
            }
        });
        dialog.show();
    }

    public void showOneButtonDialog(final Activity c, String dialog_info, String ok_info, final OnAlertDialogItemListener dialogInterface, final int type) {
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.view_alert_one_button);
        RelativeLayout btnOK = (RelativeLayout) dialog.findViewById(R.id.btnOK);
        CustomTextView txtMessage = (CustomTextView) dialog.findViewById(R.id.txtMessage);
        CustomTextView txtButtonOK = (CustomTextView) dialog.findViewById(R.id.txtButtonOK);
        txtMessage.setText(dialog_info);
        txtButtonOK.setText(ok_info);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogInterface.onClickDone(type, null);
            }
        });
        dialog.show();
    }*/

    public void setImage(String datum, Object profile_image, Context context) {
        if (datum.length() == 0 || datum == null) {
            return;
        }
        if (datum.contains("https") || datum.contains("http")) {
            if (datum.length() != 0)
                datum = datum.replace("\\", "").replace("[", "").replace("]", "").replace("\"", "");

//            RequestOptions requestOptions = new RequestOptions();
          /*  requestOptions.placeholder(R.drawable.cam_full_copy);
            requestOptions.error(R.drawable.cam_full_copy);*/
            if (profile_image instanceof CircleImageView) {
                Glide.with(context).load(datum).into((CircleImageView) profile_image);
                //ImageLoader.getInstance().displayImage(datum, (CircleImageView) profile_image);
            } else {
                // ImageLoader.getInstance().displayImage(datum, (ImageView) profile_image);
                Glide.with(context).load(datum).into((ImageView) profile_image);
            }

        } else {
            Bitmap bitmap = getBitmap(datum);
            if (profile_image instanceof CircleImageView) {
                CircleImageView imageView = (CircleImageView) profile_image;
                imageView.setImageBitmap(null);
                imageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = (ImageView) profile_image;
                imageView.setImageBitmap(null);
                imageView.setImageBitmap(bitmap);
            }

        }
    }

    public String getLanguage() {
        return "eng";
    }

    public boolean isFieldEmpty(View view) {
        if (view instanceof TextView) {
            return TextUtils.isEmpty(((TextView) view).getText().toString().trim());
        } else if (view instanceof EditText) {
            return TextUtils.isEmpty(((EditText) view).getText().toString().trim());
        } else {
            return false;
        }

    }


    public boolean isValidEmail(View view) {
        if (view instanceof AppCompatTextView) {
            return Patterns.EMAIL_ADDRESS.matcher(((AppCompatTextView) view).getText().toString()).matches();
        } else if (view instanceof AppCompatEditText) {
            return Patterns.EMAIL_ADDRESS.matcher(((AppCompatEditText) view).getText().toString()).matches();
        } else {
            return false;
        }

    }

    public boolean isNotValidPassword(View view) {
        if (view instanceof AppCompatTextView) {
            if (((AppCompatTextView) view).getText().toString().trim().length() >= 6) {
                return false;
            } else {
                return true;
            }
        } else if (view instanceof AppCompatEditText) {
            if (((AppCompatEditText) view).getText().toString().trim().length() >= 6) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isValidPhone(View view) {
        if (view instanceof AppCompatTextView) {
            int len = ((AppCompatTextView) view).getText().toString().trim().length();
            if (len < 10 || len > 16) {
                return true;
            } else {
                return false;
            }

        } else if (view instanceof AppCompatEditText) {
            int len = ((AppCompatEditText) view).getText().toString().trim().length();
            if (len < 10 || len > 16) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public String getFieldValue(View view) {
        if (view instanceof AppCompatTextView) {
            return ((AppCompatTextView) view).getText().toString().trim();
        } else if (view instanceof EditText) {
            return ((AppCompatEditText
                    ) view).getText().toString().trim();
        } else {
            return "";
        }
    }

    public HashMap<String, String> getApiHeaders(Context context) {
        SharedStorage sharedStorage = new SharedStorage();
        String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
        String sessionId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.SessionId, GlobalsVariables.STORAGE.TYPE_STRING);
        String current_locale = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.CURRENT_LANGUAGE, GlobalsVariables.STORAGE.TYPE_STRING);

        HashMap<String, String> headers = new HashMap<>();

        String timeStamp = getCurrentTimeStamp();
        String deviceToken = "deviceToken";
        String uniqueDeviceID = "uniqueDeviceID";
        String deviceType = "1";
        String clientHash = "";

        if (sessionId.equalsIgnoreCase("null")) {
            sessionId = "";
        }

        if (!TextUtils.isEmpty(sessionId)) {
            clientHash = getToken(timeStamp, sessionId + "");
            headers.put("ClientHash", clientHash);
            headers.put("UserId", userId);
            headers.put("SessionId", sessionId);
        } else {
            clientHash = getToken(timeStamp, "");
            headers.put("ClientHash", clientHash);
        }

        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("DeviceType", deviceType);
        headers.put("UniqueDeviceId", uniqueDeviceID);
        headers.put("TimeStamp", timeStamp);
        headers.put("DeviceToken", deviceToken);


        headers.put("locale", current_locale);

        String token;
        try {

            LoginResponse response = new Gson().fromJson((String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.LoginResponse,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    LoginResponse.class);

            token = response.getToken();
            if (token == null)
                token = SharedStorage.getString(GlobalsVariables.STORAGE.STORAGE_KEYS.token, GlobalsVariables.STORAGE.TYPE_STRING);

            headers.put("Authorization", "Bearer " + token);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return headers;

    }

    public MultipartBody.Part prepareFilePart(Context con, String partName, Uri fileUri) {

        File file = new File(getPathFromUri(con, fileUri));

        //RequestBody requestBody = RequestBody.create(MediaType.parse(con.getContentResolver().getType(fileUri)), file);
        RequestBody requestFile = toRequestBodyForImage(file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    public static void transparentflag(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public String getDeviceToken() {
        return "testdevice_Token";
    }

    public String getDateShow(String date) {
        return date;
    }



    /*class to convert image url to byte for futture use*/

    private class DownloadImageTask extends AsyncTask<String, Void, byte[]> {


        protected byte[] doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            byte[] byteArray = new byte[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

            }
            return byteArray;
        }

        protected void onPostExecute(byte[] result) {

            Log.e("onPostExecute: ", result + "");
        }
    }
}
