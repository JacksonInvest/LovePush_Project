package com.lovepushapp.matchList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.model.response.NotificationResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class matchRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    List<NotificationResponse.Data> dataList;

    public matchRequestsAdapter(List<NotificationResponse.Data> dataList, Context context, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.dataList=dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_request, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        holder.nameTV.setText(dataList.get(pos).getNotification_message().getMessage());

   /*     Glide.with(context).load(dataList.get(pos).getUser_info().getProfile_image()).placeholder(R.drawable.ef_image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageView);*/

        String event_type = dataList.get(pos).getEvent_type();


        Picasso.get()
                .load(dataList.get(pos).getUser_info().getProfileImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(holder.userIV);

        if (dataList.get(pos).getEvent_type().equals("S_L_R")) {
            holder.userIV.setBorderColor(context.getResources().getColor(R.color.colorPrimary));

        }else if (dataList.get(pos).getEvent_type().equals("S_C_R")){
            holder.userIV.setBorderColor(context.getResources().getColor(R.color.blue));

        } else{
            holder.userIV.setBorderColor(context.getResources().getColor(R.color.grey));

        }


        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.root_view,pos,1,dataList);

            }
        });
        holder.acceptIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.acceptIv,pos,2,dataList);

            }
        });
        holder.rejectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.rejectIV,pos,3,dataList);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userIV;

        AppCompatTextView nameTV;
        LinearLayout root_view;
        ImageView acceptIv,rejectIV;

        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTV =  itemView.findViewById(R.id.nameTV);
            acceptIv =  itemView.findViewById(R.id.acceptIv);
            userIV =  itemView.findViewById(R.id.userIV);
            rejectIV =  itemView.findViewById(R.id.rejectIV);
            root_view = itemView.findViewById(R.id.root_view);
        }

    }
}
