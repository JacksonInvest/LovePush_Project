package com.lovepushapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.helper.TimeAgo;
import com.lovepushapp.model.homePosts.addsmodel.DataItem;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class AdFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Activity context;

    private List<DataItem> dataList;
    private boolean isMyPost;
    private String myProfileImage,MyName;
    private String my_user_id;
    public AdFeedAdapter(boolean isMyPost, Activity context, List<DataItem> dataList, OnItemClickListenerCutom listener, String my_user_id) {
        this.listener = listener;
        this.context = context;
        this.dataList = dataList;
        this.my_user_id=my_user_id;
        this.isMyPost = isMyPost;
        this.myProfileImage =String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.profile_image, GlobalsVariables.STORAGE.TYPE_STRING));
        this.MyName =String.valueOf(SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.username, GlobalsVariables.STORAGE.TYPE_STRING));


    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ad_feed_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        if (dataList.get(pos).getUserId()==Integer.parseInt(my_user_id))
            holder.ivReport.setVisibility(View.GONE);
        else
            holder.ivReport.setVisibility(View.VISIBLE);

        if (dataList.get(pos).getMatchId()==0)
            {
                holder.cardChat.setVisibility(View.GONE);
                holder.cardLoveRechatIVquest.setVisibility(View.VISIBLE);
                holder.cardConnectRequest.setVisibility(View.VISIBLE);
            }else
        {
            holder.cardChat.setVisibility(View.VISIBLE);
            holder.cardLoveRechatIVquest.setVisibility(View.GONE);
            holder.cardConnectRequest.setVisibility(View.GONE);
        }

        //Glide.with(context).load(dataList.get(pos).getData().get(pos).getImage()).into(holder.imgCategoryImage);
        if (dataList.get(pos).getImage() == null || dataList.get(pos).getImage().equals("")){
            holder.postIV.setVisibility(View.GONE);
        }else {
            Picasso.get()
                    .load(dataList.get(pos).getImage())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.postIV);
            holder.postIV.setVisibility(View.VISIBLE);
        }


        holder.postTitleTV.setText(dataList.get(pos).getTitle());
        holder.postedDesTV.setText(dataList.get(pos).getDescription());
        holder.postLocationTV.setText(dataList.get(pos).getLocation());


        if (dataList.get(pos).getIsPostAnonymously()==1){
            Picasso.get()
                    .load(R.drawable.placeholder_male_square)
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.postedByImg);
            holder.postedByName.setText("Anonymous");
        }else {

            if (isMyPost){
                Picasso.get()
                        .load(myProfileImage)
                        .placeholder(R.drawable.placeholder_male_square)
                        .error(R.drawable.placeholder_male_square)
                        .into(holder.postedByImg);
                holder.postedByName.setText(MyName);
            }else {

                Picasso.get()
                        .load(dataList.get(pos).getUserInfo().getProfileImage())
                        .placeholder(R.drawable.placeholder_male_square)
                        .error(R.drawable.placeholder_male_square)
                        .into(holder.postedByImg);
                holder.postedByName.setText(dataList.get(pos).getUserInfo().getUsername());
            }

        }


        if (dataList.get(pos).getPostType()==1){
            holder.postTypeTV.setText(context.getString(R.string.love_ad));
            holder.postTypeTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_button_small_blue));
        }else {
            holder.postTypeTV.setText(context.getString(R.string.connect_ad));
            holder.postTypeTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_button_small));
        }
        holder.ivReport.setOnClickListener(v -> {
            alertDialog(pos);
        });
        holder.root_view.setOnClickListener(v -> listener.onItemClick(holder.root_view, pos, 3, dataList));




        String MyFinalValue =  new TimeAgo().covertTimeStringToAgo(dataList.get(pos).getCreatedAt(),context);

        holder.postedTimeTV.setText(MyFinalValue);
    }
    void alertDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You have seen abusive or vulgar content in this photo! Do you want to report photo?");
        builder.setPositiveButton("OK", (dialog, id) -> {
            report(pos);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void report(int pos) {
        AlertDialogs dialogs = new AlertDialogs(context);
        //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
        dialogs.show();
        new PostAdsPresenter(context).reportImage(dataList.get(pos).getId(), "post_ad", new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                dialogs.dismiss();
                if (response.body() != null) {
                    CommonResponse response1 = (CommonResponse) response.body();
                    if (response1.getStatus())
                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(context, "Something went wrong ?", Toast.LENGTH_SHORT).show();

            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.postIV) ImageView postIV;
        @BindView(R.id.ivReport) ImageView ivReport;
        @BindView(R.id.postedByImg) CircleImageView postedByImg;
        @BindView(R.id.postedByName) AppCompatTextView postedByName;
        @BindView(R.id.postedDesTV) AppCompatTextView postedDesTV;
        @BindView(R.id.postedTimeTV) AppCompatTextView postedTimeTV;
        @BindView(R.id.postTitleTV) AppCompatTextView postTitleTV;
        @BindView(R.id.postLocationTV) AppCompatTextView postLocationTV;
        @BindView(R.id.root_view) LinearLayout root_view;
        @BindView(R.id.postTypeTV) AppCompatTextView postTypeTV;
        @BindView(R.id.cardLoveRechatIVquest)
        CardView cardLoveRechatIVquest;
        @BindView(R.id.cardConnectRequest) CardView cardConnectRequest;
        @BindView(R.id.cardChat) CardView cardChat;


        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
