package com.lovepushapp.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lovepushapp.R;

public class ImageDialog extends DialogFragment {
    LinearLayout viewCamera;
    LinearLayout viewGallery;
    static OnOptionClick listner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.imagepicker_view,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCamera=view.findViewById(R.id.viewCamera);
        viewGallery=view.findViewById(R.id.viewGallery);
        viewCamera.setOnClickListener(v -> {
            listner.onClick(1);
        });
    }

    public static ImageDialog instanece(OnOptionClick lis){
        listner=lis;
        return new ImageDialog();
    }
    public  interface OnOptionClick{
        void  onClick(int i);
    }
}
