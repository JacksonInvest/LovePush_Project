package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRecentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Context context;
    private List<UserChatList.Data> datalist;

    private String my_user_id;

    public ChatRecentAdapter(String my_user_id, List<UserChatList.Data> data2List, Context context, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.datalist=data2List;
        this.my_user_id=my_user_id;
//        my_user_id=String.valueOf(sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING));
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_recent_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
//        Log.e("")


        if (datalist.get(pos).getUser_info2() !=null || my_user_id.equalsIgnoreCase(datalist.get(pos).getUser_info2().getId()))
        {
            String name = datalist.get(pos).getUser_info1().getName();
            if (name.contains(" ")) {
                String[] splited = name.split("\\s+");
                holder.nameTV.setText(splited[0].toString());
            }else {

                holder.nameTV.setText(name);
            }
//                Glide.with(context).load(datalist.get(pos).getUser_info1().getProfile_image()).apply(new RequestOptions()
//                        .placeholder(R.drawable.user)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.user_IV);

            Picasso.get()
                    .load(datalist.get(pos).getUser_info1().getProfile_image())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.user_IV);


        }

        if (my_user_id.equalsIgnoreCase(datalist.get(pos).getUser_info1().getId()))
        {
           // holder.nameTV.setText(datalist.get(pos).getUser_info2().getName());
//            Glide.with(context).load(datalist.get(pos).getUser_info2().getProfile_image()).apply(new RequestOptions()
//                    .placeholder(R.drawable.user)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)).into(holder.user_IV);

            String name = datalist.get(pos).getUser_info2().getName();
            if (name.contains(" ")) {
                String[] splited = name.split("\\s+");
                holder.nameTV.setText(splited[0].toString());
            }else {

                holder.nameTV.setText(name);
            }

            Picasso.get()
                    .load(datalist.get(pos).getUser_info2().getProfile_image())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.user_IV);


        }


        holder.allView.setOnClickListener(v -> listener.onItemClick(holder.allView, pos, 11, datalist));


        if (datalist.get(pos).getEvent_type().equalsIgnoreCase("L"))
        {
            holder.user_IV.setBorderColor(context.getResources().getColor(R.color.colorAccent));
//            Blue
        }
        else
            if (datalist.get(pos).getEvent_type().equalsIgnoreCase("C"))
            {
//                red
                holder.user_IV.setBorderColor(context.getResources().getColor(R.color.blue));
            }
            else
                if (datalist.get(pos).getEvent_type().equalsIgnoreCase(""))
                {
//                    grey
                    holder.user_IV.setBorderColor(context.getResources().getColor(R.color.grey));
                }


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.allView)
        View allView;

        CircleImageView user_IV;
        AppCompatTextView nameTV;


        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTV);
            user_IV=itemView.findViewById(R.id.img_profile);
            ButterKnife.bind(this, itemView);
        }


    }
}
