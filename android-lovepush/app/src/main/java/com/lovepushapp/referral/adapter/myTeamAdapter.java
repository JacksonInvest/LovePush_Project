package com.lovepushapp.referral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.referral.model.Level;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class myTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListenerCutom listener;
    private Context context;
    private List<Level> dataList ;

    public myTeamAdapter(List<Level> loveMatchList, Context context, OnItemClickListenerCutom listener) {
        this.dataList = loveMatchList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_searched_users, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        String profile_image = dataList.get(pos).getProfileImage();
        String name = dataList.get(pos).getName();

        if (!profile_image.equals("")){
            Picasso.get()
                    .load(profile_image)
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.imageProfile);
            holder.imageProfile.setBorderColor(context.getResources().getColor(R.color.grey));
        }


        if (!name.equals("")){
            holder.nameTV.setText(name);
        }else {
            holder.nameTV.setText("");
        }

        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.root_view, pos, 1, dataList);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageProfile;

        AppCompatTextView nameTV,userName,textKm;
        RelativeLayout root_view;


        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTV =  itemView.findViewById(R.id.nameTV);
            userName =  itemView.findViewById(R.id.userName);
            imageProfile =  itemView.findViewById(R.id.imageProfile);
            textKm =  itemView.findViewById(R.id.textKm);
            root_view = itemView.findViewById(R.id.root_view);
            textKm.setVisibility(View.GONE);
        }

    }
}
