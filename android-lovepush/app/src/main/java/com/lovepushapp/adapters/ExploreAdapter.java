package com.lovepushapp.adapters;



import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.model.response.Explore.Explore;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    private List<Explore.Data.User> data;

    public ExploreAdapter(Context context, List<Explore.Data.User> exploreList, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.data=exploreList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_card2, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        holder.name.setText(data.get(pp).getName());
//        Toast.makeText(context, ""+data.get(pos), Toast.LENGTH_SHORT).show();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.cardView, pos, 1, data);
//                Toast.makeText(context, ""+data.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        if (data.get(pp).getAge()!= null){
            String s1 ="Age: ";
            SpannableString span1 = new SpannableString(s1);
            span1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_color_black)), 0, s1.length(), 0);// set color
            span1.setSpan(new RelativeSizeSpan(1.2f),0,s1.length(),0);
            SpannableString span2 = new SpannableString(data.get(pp).getAge()+"");

            holder.testcard_age.setText(TextUtils.concat(span1,span2));
        }else {
            holder.testcard_age.setText("");

        }


        //holder.testcard_age.setText("");
//        Glide.with(context).load(data.get(pos).getProfileImage()).into(holder.profile_IV);

        Picasso.get()
                .load(data.get(pos).getProfile_image())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.profile_IV);
    }

    @Override
    public int getItemCount() {
//        Toast.makeText(context, ""+data.size(), Toast.LENGTH_SHORT).show();
        return data.size();
    }

    public void addAll(List<Explore.Data.User> exploreList) {
        this.data=exploreList;
        notifyDataSetChanged();

    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
//

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.nameET)
        TextView name;
        @BindView(R.id.testcard_age)
        TextView testcard_age;
        @BindView(R.id.profile_imageIV)
        ImageView profile_IV;
        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
