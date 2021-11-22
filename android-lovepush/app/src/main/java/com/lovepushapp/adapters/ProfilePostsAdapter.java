package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.homePosts.getHomePostResponse;

import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    Util util = new Util();
    private List<getHomePostResponse.Data.Datum> path;

    public ProfilePostsAdapter(Context context, List<getHomePostResponse.Data.Datum> list, OnItemClickListenerCutom listener) {

        this.listener = listener;
        this.path = list;
        this.context = context;

    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_posts, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        holder.close.setVisibility(View.GONE);


        holder.imgCategoryImage.setOnClickListener(v -> {


            listener.onItemClick(holder.imgCategoryImage, pos, 1, path);

//                    goToDashboardScreen();

//                    Toast.makeText(context, "single", Toast.LENGTH_SHORT).show();
        });
        //util.loadGlideImage(context, path.get(pos).getImage(), holder.imgCategoryImage, R.drawable.default_image);
        if (path.get(pos).getImage() == null)
            holder.itemLayout.setVisibility(View.GONE);
        else
            Glide.with(context).load(path.get(pos).getImage()).into(holder.imgCategoryImage);
    }


    @Override
    public int getItemCount() {

        return path.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategoryImage, close;
        CardView cardView;
        RelativeLayout itemLayout;

        public CommonViewHolder(View itemView) {
            super(itemView);

            imgCategoryImage = itemView.findViewById(R.id.img_categoryImage);
            close = itemView.findViewById(R.id.img_categoryImage_close);
            cardView = itemView.findViewById(R.id.card_view);
            itemLayout = itemView.findViewById(R.id.itemView);
        }


    }


}
