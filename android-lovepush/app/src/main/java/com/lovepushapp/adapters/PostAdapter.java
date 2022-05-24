package com.lovepushapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.helper.TimeAgo;
import com.lovepushapp.model.homePosts.getHomePostResponse;
import com.lovepushapp.model.homePosts.postAdDeleteResponse;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Activity context;
    private List<getHomePostResponse.Data.Datum> dataList;
    private Integer my_user_id;

    public PostAdapter(String my_user_id, Activity context, List<getHomePostResponse.Data.Datum> dataList, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.dataList = dataList;
        this.my_user_id = Integer.valueOf(my_user_id);

    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();

        //Glide.with(context).load(dataList.get(pos).getData().get(pos).getImage()).into(holder.imgCategoryImage);
        if (dataList.get(pp).getImage() == null) {
            holder.card.setVisibility(View.GONE);
            holder.txtcard.setVisibility(View.VISIBLE);
            holder.txtDescription.setText(dataList.get(pos).getDescription());

        } else {
            holder.card.setVisibility(View.VISIBLE);
            holder.txtcard.setVisibility(View.GONE);
            Picasso.get()
                    .load(dataList.get(pos).getImage())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.postIV);
        }

        holder.postedDesTV.setText(dataList.get(pos).getTitle());
        String string_date = "12-December-2012";

        if (dataList.get(pos).getIsPostAnonymously() == 1) {
            Picasso.get()
                    .load(R.drawable.placeholder_male_square)
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.postedByImg);
            holder.postedByName.setText("Anonymous");
           // holder.chatIV.setVisibility(View.GONE);
        } else {
            Picasso.get()
                    .load(dataList.get(pos).getUserInfo().getProfileImage())
                    .placeholder(R.drawable.placeholder_male_square)
                    .error(R.drawable.placeholder_male_square)
                    .into(holder.postedByImg);
            holder.postedByName.setText(dataList.get(pos).getUserInfo().getUsername());

            if (dataList.get(pos).getUserId().equals(my_user_id)) {
                holder.chatIV.setVisibility(View.GONE);
                holder.imgNewMessage.setVisibility(View.GONE);
                holder.ivReport.setVisibility(View.GONE);

            } else {
                if (dataList.get(pos).getBlockedBy().equals(String.valueOf(my_user_id)))
                    holder.chatIV.setVisibility(View.GONE);
                holder.imgDelete.setVisibility(View.GONE);

            }

        }

        if (dataList.get(pos).getIsLiked() == 1) {
            holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_active));
        }
        if (dataList.get(pos).getIsLiked() == 0) {
            holder.likeIV.setImageDrawable(context.getResources().getDrawable(R.drawable.like_unactive));

        }
        if (dataList.get(pos).getIsLiked() == 1) {
            holder.imgnewLIke.setImageDrawable(context.getResources().getDrawable(R.drawable.like_active));
        }
        if (dataList.get(pos).getIsLiked() == 0) {
            holder.imgnewLIke.setImageDrawable(context.getResources().getDrawable(R.drawable.like_unactive));

        }

        holder.ivReport.setOnClickListener(v -> {
            alertDialog(context, pos);
        });


        holder.imgDelete.setOnClickListener(v -> {

            AlertDialogs dialogs = new AlertDialogs(context);
            //Toast.makeText(context, ""+data.getId(), Toast.LENGTH_SHORT).show();
            dialogs.show();
            new PostAdsPresenter(context).hitDeleteMyHomePost(dataList.get(pos).getId(), new ResponseListner() {
                @Override
                public <T> void onComplete(Response<T> response) {
                    if (response.body() instanceof postAdDeleteResponse) {
                        dialogs.dismiss();
                        postAdDeleteResponse res = (postAdDeleteResponse) response.body();
//            appUtils.showToast(response.getMessage());
                        if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                            dataList.remove(pos);
                            notifyDataSetChanged();
                            notifyItemRemoved(pos);
                            Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }

                @Override
                public Void onError(String message) {
                    dialogs.dismiss();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    return null;
                }
            });
        });
        holder.chatIV.setOnClickListener(v -> {
            //if (dataList.get(pos).)
            listener.onItemClick(holder.chatIV, pos, 1, dataList);

        });

        holder.likeIV.setOnClickListener(v -> listener.onItemClick(holder.likeIV, pos, 2, dataList));
        holder.imgNewMessage.setOnClickListener(v -> {
            //if (dataList.get(pos).)
            listener.onItemClick(holder.chatIV, pos, 1, dataList);

        });

        holder.imgnewLIke.setOnClickListener(v -> listener.onItemClick(holder.likeIV, pos, 2, dataList));

        holder.root_view.setOnClickListener(v -> listener.onItemClick(holder.root_view, pos, 3, dataList));


        String MyFinalValue = new TimeAgo().covertTimeStringToAgo(dataList.get(pos).getCreatedAt(), context);

        holder.postedTimeTV.setText(MyFinalValue);
    }

    void alertDialog(Context context, int pos) {
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
        new PostAdsPresenter(context).reportImage(dataList.get(pos).getId(), "post", new ResponseListner() {
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

        @BindView(R.id.postIV)
        ImageView postIV;
        @BindView(R.id.postedByImg)
        CircleImageView postedByImg;
        @BindView(R.id.postedByName)
        AppCompatTextView postedByName;
        @BindView(R.id.postedDesTV)
        AppCompatTextView postedDesTV;
        @BindView(R.id.postedTimeTV)
        AppCompatTextView postedTimeTV;
        @BindView(R.id.chatIV)
        ImageView chatIV;
        @BindView(R.id.likeIV)
        ImageView likeIV;
        @BindView(R.id.txtDescription)
        TextView txtDescription;
        @BindView(R.id.imgDelete)
        ImageView imgDelete;
        @BindView(R.id.imgnewLIke)
        ImageView imgnewLIke;
        @BindView(R.id.imgNewMessage)
        ImageView imgNewMessage;
        @BindView(R.id.ivReport)
        ImageView ivReport;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.txtcard)
        CardView txtcard;
        @BindView(R.id.root_view)
        LinearLayout root_view;


        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
