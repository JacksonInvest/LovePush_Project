package com.lovepushapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.model.FAQModel.GetFaqResponse;

import java.util.List;

import butterknife.ButterKnife;

public class faqHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    private List<GetFaqResponse.Faq> dataList;


    public faqHeaderAdapter(Context context, List<GetFaqResponse.Faq> dataList, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq_header, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();


        holder.headerTV.setText(dataList.get(pos).getCategoryName());


        if (dataList.get(pos).isSelected()){
            holder.headerTV.setBackgroundResource(R.drawable.level_filled);
            holder.headerTV.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.headerTV.setBackgroundResource(R.drawable.level_unfill);
            holder.headerTV.setTextColor(Color.parseColor("#999999"));
        }

        holder.headerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.headerTV, pos, 1, dataList);


            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView headerTV;

        public CommonViewHolder(View itemView) {
            super(itemView);
            headerTV = itemView.findViewById(R.id.headerTV);

            ButterKnife.bind(this, itemView);
        }

    }
}
