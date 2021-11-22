package com.lovepushapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.model.plans.DataItem;

class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.Holder>{
    DataItem dataItem;
    public PlansAdapter(DataItem dataItem) {
        this.dataItem=dataItem;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.view_plan,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (position==0)
        {
           holder.txtDescription.setText("Message to unknown users");
           holder.txtDuration.setText(dataItem.getMessages());
        }
        else if (position==1)
        {
            holder.txtDescription.setText("Voice memo and all ");
            holder.txtDuration.setText(dataItem.getVoiceCall());
        }
        else if (position==2)
        {
            holder.txtDescription.setText("Likes");
            holder.txtDuration.setText(dataItem.getLikes());
        }
        else if (position==3)
        {
            holder.txtDescription.setText("Personal swiping ");
            holder.txtDuration.setText(dataItem.getSwipe());
        }
        else if (position==4)
        {
            holder.txtDescription.setText("Ads");
            holder.txtDuration.setText(dataItem.getAds());
        }
        else if (position==5)
        {
            holder.txtDescription.setText("See Live Video");
            holder.txtDuration.setText(dataItem.getSeeLiveVideo());
        } else if (position==6)
        {
            holder.txtDescription.setText("Live Video Streaming");
            holder.txtDuration.setText(dataItem.getLiveVideoStreaming());
        } else if (position==7)
        {
            holder.txtDescription.setText("View Storys");
            holder.txtDuration.setText(dataItem.getViewStorys());
        } else if (position==8)
        {
            holder.txtDescription.setText("Network Marketing ");
            holder.txtDuration.setText(dataItem.getNetworkMarketing());
        }




    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtDescription;
        TextView txtDuration;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txtDuration=itemView.findViewById(R.id.txtDuration);
            txtDescription=itemView.findViewById(R.id.txtDescription);
        }
    }
}
