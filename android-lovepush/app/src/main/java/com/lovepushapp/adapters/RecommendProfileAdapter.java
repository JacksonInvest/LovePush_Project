package com.lovepushapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.model.response.RecmmendProflie_Package.RecommendedProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    RecommendedProfile recList;
    Util appUtils;


    public RecommendProfileAdapter(Util appUtils, Context context, RecommendedProfile recList, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.appUtils = appUtils;
        this.recList = recList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rec_profile_list_updated, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        double totalp = 0;
        try {
            if (recList.getData().get(pos).getUserTest()!=null) {
                double p = Double.parseDouble(recList.getData().get(pos).getUserTest().getTestScore().getScores());
                totalp = p / 340 * 100;
            }
        }
        catch (NumberFormatException e){}
         String lastcal = String.format("%.2f",totalp);
        holder.nameTV.setText(recList.getData().get(pos).getName());
        holder.lovePushIntensionTV.setText(Math.round(totalp)+" %");

        appUtils.loadGlideImage(context,recList.getData().get(pos).getProfileImage(), holder.userIV, R.drawable.placeholder_male_square);

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("user_id",""+recList.getData().get(pos).getId());
            intent.putExtra("from","explore");
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        try {
            return recList.getData()!=null?recList.getData().size():0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userIV)
        ImageView userIV;
        @BindView(R.id.nameTV)
        AppCompatTextView nameTV;
        @BindView(R.id.lovePushIntensionTV)
        AppCompatTextView lovePushIntensionTV;


        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
