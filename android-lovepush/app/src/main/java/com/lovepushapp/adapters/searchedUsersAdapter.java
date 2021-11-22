package com.lovepushapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lovepushapp.R;
import com.lovepushapp.fragments.ProfileActivity;
import com.lovepushapp.model.searchUsers.GetSearchUsersResponse;

import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class searchedUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public List<GetSearchUsersResponse.Data.searchDatum> dataList;

    public searchedUsersAdapter(Context context, List<GetSearchUsersResponse.Data.searchDatum> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_searched_users, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        //GetSearchUsersResponse.Data.searchDatum searchDatum = dataList.get(pos);
        holder.nameTV.setText(dataList.get(pos).getName());
        if (dataList.get(pos).getUsername()!=null){
            holder.userName.setText("("+dataList.get(pos).getUsername()+")");
        }
        Picasso.get()
                .load(dataList.get(pos).getProfileImage())
                .placeholder(R.drawable.placeholder_male_square)
                .error(R.drawable.placeholder_male_square)
                .into(holder.imageProfile);

        /*if (dataList.get(pos).getDistance()==null){

            holder.textKm.setText("0 KM");
        }else {
            holder.textKm.setText(String.format("%.2f",dataList.get(pos).getDistance())+" KM");

        }*/

        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id= String.valueOf(dataList.get(pos).getId());
                context.startActivity(new Intent(new Intent(context, ProfileActivity.class)).putExtra("user_id",user_id).putExtra("from","explore"));
//
            }
        });

    }




    public void updateList(List<GetSearchUsersResponse.Data.searchDatum> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageProfile;

        AppCompatTextView nameTV,userName,textKm;

        RelativeLayout root_view;

        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            userName = itemView.findViewById(R.id.userName);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textKm = itemView.findViewById(R.id.textKm);
            root_view = itemView.findViewById(R.id.root_view);
            ButterKnife.bind(this, itemView);
            textKm.setVisibility(View.GONE);
        }


    }
}
