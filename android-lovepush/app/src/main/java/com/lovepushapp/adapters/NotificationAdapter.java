package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.model.response.NotificationResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;

    List<NotificationResponse.Data> dataList;

    public NotificationAdapter(List<NotificationResponse.Data> dataList, Context context, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.dataList=dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        if(dataList.get(pos).getNotification_message() !=null && dataList.get(pos).getNotification_message().getMessage()!=null){
            holder.messageTV.setText(dataList.get(pos).getNotification_message().getMessage());
        }

   /*     Glide.with(context).load(dataList.get(pos).getUser_info().getProfile_image()).placeholder(R.drawable.ef_image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageView);*/

        String event_type = dataList.get(pos).getEvent_type();

        if (event_type.equalsIgnoreCase("S_C_R") || event_type.equalsIgnoreCase("S_M_R") || event_type.equalsIgnoreCase("S_L_R")){
            holder.llButton.setVisibility(View.VISIBLE);
        }else {
            holder.llButton.setVisibility(View.GONE);

        }
        Picasso.get()
                .load(dataList.get(pos).getUser_info().getProfileImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(holder.imageView);
        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(pos).getEvent_type().equalsIgnoreCase("L"))
                {
                    return;
                }
                else
                {
                    listener.onItemClick(holder.mainLL,pos,12,dataList);
                }
            }
        });

        holder.mainLL.setOnClickListener(v -> {
            if (dataList.get(pos).getEvent_type().equalsIgnoreCase("L"))
            {
                return;
            }
            else
            {

                listener.onItemClick(holder.mainLL,pos,1,dataList);
            }

        });
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.acceptBtn,pos,2,dataList);

            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.rejectBtn,pos,3,dataList);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView messageTV;
        CircleImageView imageView;

        LinearLayout mainLL,llButton;
        Button acceptBtn,rejectBtn;

        public CommonViewHolder(View itemView) {
            super(itemView);
            messageTV=itemView.findViewById(R.id.messageTV);
            imageView=itemView.findViewById(R.id.userIV);
            mainLL=itemView.findViewById(R.id.LinearL);
            llButton = itemView.findViewById(R.id.llButton);
            rejectBtn =  itemView.findViewById(R.id.rejectBtn);
            acceptBtn =  itemView.findViewById(R.id.acceptBtn);
            ButterKnife.bind(this, itemView);
        }


    }
}
