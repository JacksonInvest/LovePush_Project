package com.lovepushapp.adapters;

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
import com.lovepushapp.model.FAQModel.GetFaqResponse;

import java.util.List;

import butterknife.ButterKnife;

public class faqListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    private List<GetFaqResponse.Faq.QsnList> dataList;


    public faqListAdapter(Context context, List<GetFaqResponse.Faq.QsnList> qusDataList, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.dataList = qusDataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq_qus, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        holder.qusTv.setText(dataList.get(pos).getQuestion());
        holder.ansTv.setText(dataList.get(pos).getAnswer());

        holder.qusLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.onItemClick(holder.qusLL, pos, 1, dataList);


            }
        });
        if (dataList.get(pos).isShowAns()){
            holder.ansTv.setVisibility(View.VISIBLE);
            holder.imgArrow.setRotation((float) 90.0);
        }
        if (!dataList.get(pos).isShowAns()){
            holder.ansTv.setVisibility(View.GONE);
            holder.imgArrow.setRotation((float) 0.0);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {


        AppCompatTextView qusTv,ansTv;
        LinearLayout qusLL;
        ImageView imgArrow;

        public CommonViewHolder(View itemView) {
            super(itemView);
            qusTv = itemView.findViewById(R.id.qusTv);
            ansTv = itemView.findViewById(R.id.ansTv);
            qusLL = itemView.findViewById(R.id.qusLL);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            ButterKnife.bind(this, itemView);
        }

    }
}
