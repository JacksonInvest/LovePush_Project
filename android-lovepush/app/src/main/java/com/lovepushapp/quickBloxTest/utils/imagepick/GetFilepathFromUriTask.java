package com.lovepushapp.quickBloxTest.utils.imagepick;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.lovepushapp.core.MyApplication;
import com.lovepushapp.quickBloxTest.async.BaseAsyncTask;
import com.lovepushapp.quickBloxTest.ui.dialog.ProgressDialogFragment;
import com.lovepushapp.quickBloxTest.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class GetFilepathFromUriTask extends BaseAsyncTask<Intent, Void, File> {

    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_CONTENT_GOOGLE = "content://com.google.android";
    private static final String SCHEME_FILE = "file";

    private WeakReference<FragmentManager> fmWeakReference;
    private OnImagePickedListener listener;
    private int requestCode;

    public GetFilepathFromUriTask(FragmentManager fm, OnImagePickedListener listener, int requestCode) {
        this.fmWeakReference = new WeakReference<>(fm);
        this.listener = listener;
        this.requestCode = requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgress();
    }

    @Override
    public File performInBackground(Intent... params) throws Exception {
        Intent data = params[0];

        String imageFilePath = null;
        Uri uri = data.getData();
        String uriScheme = uri.getScheme();

        boolean isFromGoogleApp = uri.toString().startsWith(SCHEME_CONTENT_GOOGLE);
        boolean isKitKatAndUpper = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (SCHEME_CONTENT.equalsIgnoreCase(uriScheme) && !isFromGoogleApp && !isKitKatAndUpper) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = MyApplication.getInstance().getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageFilePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (SCHEME_FILE.equalsIgnoreCase(uriScheme)) {
            imageFilePath = uri.getPath();
        } else {
            imageFilePath = ImageUtils.saveUriToFile(uri);
        }

        if (TextUtils.isEmpty(imageFilePath)) {
            throw new IOException("Can't find a filepath for URI " + uri.toString());
        }

        return new File(imageFilePath);
    }

    @Override
    public void onResult(File file) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onResult listener = " + listener);
        if (listener != null) {
            listener.onImagePicked(requestCode, file);
        }
    }

    @Override
    public void onException(Exception e) {
        hideProgress();
        Log.w(GetFilepathFromUriTask.class.getSimpleName(), "onException listener = " + listener);
        if (listener != null) {
            listener.onImagePickError(requestCode, e);
        }
    }

    private void showProgress() {
        FragmentManager fm = fmWeakReference.get();
        if (fm != null) {
            ProgressDialogFragment.show(fm);
        }
    }

    private void hideProgress() {
        FragmentManager fm = fmWeakReference.get();
        if (fm != null) {
            ProgressDialogFragment.hide(fm);
        }
    }
}