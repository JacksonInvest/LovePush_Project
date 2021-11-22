package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.response.ExploreRadar.userDatum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreRadarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    private List<userDatum> exploreListRadar ;

    Util appUtils;


    public ExploreRadarAdapter(Util appUtils, Context context, List<userDatum> exploreListRadar, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.appUtils = appUtils;
        this.exploreListRadar = exploreListRadar;
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

        holder.nameTV.setText(exploreListRadar.get(pos).getName());
        if (exploreListRadar.get(pos).getDistance()==null){

            holder.lovePushIntensionTV.setText("0 KM");
        }else {
            holder.lovePushIntensionTV.setText(String.format("%.1f", exploreListRadar.get(pos).getDistance())+" KM");

        }

        appUtils.loadGlideImage(context,exploreListRadar.get(pos).getProfileImage(), holder.userIV, R.drawable.ef_image_placeholder);

        holder.itemView.setOnClickListener(v -> {

            listener.onItemClick(holder.itemView, pos, 1, exploreListRadar);
        });


    }

    @Override
    public int getItemCount() {
        try {
            return exploreListRadar!=null?exploreListRadar.size():0;
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
