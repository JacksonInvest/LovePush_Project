package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.local.MultiOptionModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenBottonDialogMultiSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    ArrayList<MultiOptionModel> nameList = new ArrayList<>();
    Context context;
    Util util;

    public OpenBottonDialogMultiSelectAdapter(Util util, Context context, ArrayList<MultiOptionModel> nameList, OnItemClickListenerCutom listener) {
        this.nameList = nameList;
        this.listener = listener;
        this.context = context;
        this.util = util;


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
        final MultiOptionModel model = nameList.get(pos);

        if (model.isSelected) {
            holder.nameTV.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.nameTV.setTextColor(ContextCompat.getColor(context, R.color.black_light));
        }

        holder.nameTV.setText(model.name);

        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (model.isSelected) {
                    model.isSelected = false;
                } else {
                    int a = 0;
                    for (int i = 0; i < nameList.size(); i++) {
                        if (nameList.get(i).isSelected) {
                            a = a + 1;
                        }
                    }

                    if (a == 3) {
                        util.showToast(context.getString(R.string.max_three_select));
                    } else {
                        model.isSelected = true;
                    }
                }
                listener.onItemClick(v, pos, 1, new Gson().toJson(nameList).toString());
                notifyDataSetChanged();
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
