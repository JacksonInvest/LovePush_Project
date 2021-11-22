package com.lovepushapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.lovepushapp.R;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Response;

public class ProfileAdapter extends PagerAdapter {

    List<ProfileImage> images;
    Activity context;
    boolean isMyProfile;

    LayoutInflater mLayoutInflater;
    public ProfileAdapter(List<ProfileImage> images, Activity context, boolean isMyProfile) {
        this.images = images;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isMyProfile=isMyProfile;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.profile_horizonal, container, false);

        ImageView imageView = itemView.findViewById(R.id.profileIV);
        ImageView ivReport = itemView.findViewById(R.id.ivReport);
        if (isMyProfile)
            ivReport.setVisibility(View.GONE);
        else
            ivReport.setVisibility(View.VISIBLE);
       /* Glide.with(context).
                load(images.get(position).
                        getImage()).into(imageView); */

        Picasso.get()
                .load(images.get(position).getImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(imageView);
        container.addView(itemView);
        ivReport.setOnClickListener(v -> {
            alertDialog(position);
        });
        return itemView;
    }
    void alertDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You have seen abusive or vulgar content in this photo! Do you want to report photo?");
        builder.setPositiveButton("OK", (dialog, id) -> {
            report(pos);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void report(int pos) {
        AlertDialogs dialogs = new AlertDialogs(context);
        //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
        dialogs.show();
        new PostAdsPresenter(context).reportImage(images.get(pos).getId(), "profile_image", new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.body() != null) {
                    CommonResponse response1 = (CommonResponse) response.body();
                    if (response1.getStatus())
                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
