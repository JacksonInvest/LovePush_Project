package com.lovepushapp.matchList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class loveMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListenerCutom listener;
    private Context context;
    private List<UserChatList.Data> datalist ;
    private String my_user_id;

    public loveMatchAdapter(List<UserChatList.Data> loveMatchList, Context context, OnItemClickListenerCutom listener) {
        this.datalist = loveMatchList;
        this.listener = listener;
        this.context = context;
        this.my_user_id = String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        if (my_user_id.equalsIgnoreCase(datalist.get(pos).getUser_info2().getId()))
        {
            holder.nameTV.setText(datalist.get(pos).getUser_info1().getName());
//                Glide.with(context).load(datalist.get(pos).getUser_info1().getProfile_image()).apply(new RequestOptions()
//                        .placeholder(R.drawable.user)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.user_IV);

            Picasso.get()
                    .load(datalist.get(pos).getUser_info1().getProfile_image())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.imageProfile);


        }

        if (my_user_id.equalsIgnoreCase(datalist.get(pos).getUser_info1().getId()))
        {
            holder.nameTV.setText(datalist.get(pos).getUser_info2().getName());
//            Glide.with(context).load(datalist.get(pos).getUser_info2().getProfile_image()).apply(new RequestOptions()
//                    .placeholder(R.drawable.user)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.user_IV);

            Picasso.get()
                    .load(datalist.get(pos).getUser_info2().getProfile_image())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.imageProfile);


        }
        holder.imageProfile.setBorderColor(context.getResources().getColor(R.color.colorAccent));

        if (datalist.get(pos).getIsBlocked() == 1){
            holder.textUnblock.setVisibility(View.VISIBLE);
        }else {
            holder.textUnblock.setVisibility(View.GONE);
        }


        holder.textUnblock.setOnClickListener(v -> listener.onItemClick(holder.textUnblock, pos, 1, datalist));

        holder.root_view.setOnClickListener(v -> listener.onItemClick(holder.textUnblock, pos, 2, datalist));

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageProfile;

        AppCompatTextView nameTV,userName,textUnblock;

        RelativeLayout root_view;

        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTV =  itemView.findViewById(R.id.nameTV);
            userName =  itemView.findViewById(R.id.userName);
            imageProfile =  itemView.findViewById(R.id.imageProfile);
            textUnblock =  itemView.findViewById(R.id.textUnblock);
            root_view = itemView.findViewById(R.id.root_view);
        }

    }
}
