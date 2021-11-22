package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenBottonDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    ArrayList<String> nameList = new ArrayList<>();
    Context context;
    int selectedPos = -1;

    public OpenBottonDialogAdapter(Context context, ArrayList<String> nameList, OnItemClickListenerCutom listener) {
        this.nameList = nameList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_bottom_dialog, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        if (selectedPos == pos) {
            holder.nameTV.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.nameTV.setTextColor(ContextCompat.getColor(context, R.color.black_light));
        }
        holder.nameTV.setText(nameList.get(pos));
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = pos;
                notifyDataSetChanged();
                listener.onItemClick(v, pos, 1, nameList.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return nameList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTV)
        TextView nameTV;


        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
