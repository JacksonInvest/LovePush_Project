package com.lovepushapp.videoTestJava.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.lovepushapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {

    }

    private static boolean isEnteredTextValid(Context context, EditText editText, int resFieldName, int maxLength, boolean checkLogin) {
        boolean isCorrect = false;
        Pattern pattern;
        if (checkLogin) {
            pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{2," + (maxLength - 1) + "}+$");
        } else {
            pattern = Pattern.compile("^[a-zA-Z][a-zA-Z 0-9]{2," + (maxLength - 1) + "}+$");
        }

        if (!TextUtils.isEmpty(editText.getText().toString())) {
            Matcher matcher = pattern.matcher(editText.getText().toString().trim());
            isCorrect = matcher.matches();
        }

        if (!isCorrect) {
            editText.setError(String.format(context.getString(R.string.error_name_must_not_contain_special_characters_from_app), context.getString(resFieldName), String.valueOf(maxLength)));
        }

        return isCorrect;
    }


}