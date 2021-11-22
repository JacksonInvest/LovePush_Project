package com.lovepushapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ProfileImagesAdapter";

    OnItemClickListenerCutom listener;
    Context context;
    List<Uri> list;
    public  List<ProfileImage> path;
    Util util = new Util();
    String from = "";
    int chk;
    int selectedImage = -1;
    String selectedImageUrl = "";
    boolean isImageNotClicked=true;
    boolean isImageDeleted=false;

    public ProfileImagesAdapter(String from, Context context, List<ProfileImage> list, OnItemClickListenerCutom listener) {

        this.listener = listener;
        this.path = list;
        this.from = from;
        this.context = context;

    }

    public ProfileImagesAdapter(Context context,String from, List<Uri> list, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.list=list;
        this.from=from;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_images_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        // Log.d("item", pp + "" + path.get(pp).getImage() + " " + path.size());
        CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        if (chk == 1) {
            holder.imgCategoryImage.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
        }

        holder.imgCategoryImage.setOnClickListener(v -> {
            if (chk == 1) {

                listener.onItemClick(holder.imgCategoryImage, pos, 150, path);

//                    goToDashboardScreen();

//                    Toast.makeText(context, "single", Toast.LENGTH_SHORT).show();
            }
        });


        if (from.equals("")) {

            //util.loadGlideImage(context, list.get(pos), holder.imgCategoryImage, R.drawable.default_image);

            Picasso.get()
                    .load(list.get(pos).toString())
                    .placeholder(R.drawable.gradient_attachment)
                    .error(R.drawable.gradient_attachment)
                    .into(holder.imgCategoryImage);
            holder.close.setOnClickListener(v -> {
                list.remove(pos);
                notifyDataSetChanged();


            });


        }else if(from.equals("from_create_profile")) {

            Picasso.get()
                    .load(list.get(pos))
                    .placeholder(R.drawable.gradient_attachment)
                    .error(R.drawable.gradient_attachment)
                    .into(holder.imgCategoryImage);


            Log.d(TAG, "onBindViewHolder: "+list.get(pp)  +": pp "+pp);
            if(selectedImage==pos && !isImageDeleted){
                holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.red));
                holder.imgCategoryImage.setPadding(7, 7, 7, 7);
            }else if (selectedImage==0 && isImageDeleted){
                isImageDeleted=false;
                holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.red));
                holder.imgCategoryImage.setPadding(7, 7, 7, 7);
            }
            else{
                holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.imgCategoryImage.setPadding(0,0,  0, 0);
            }


            holder.imgCategoryImage.setOnClickListener(v ->{
                selectedImage=pos;
                isImageNotClicked=false;
                listener.onItemClick(holder.imgCategoryImage,pos,pos,null);
                notifyDataSetChanged();
            });

            holder.close.setOnClickListener(v -> {
                if (list.size() == 1) {
                    DialogUtils.showAlert(context, context.getString(R.string.oneImageRequired), () -> {

                    });
                } else {
                    DialogUtils.showAlert(context, context.getString(R.string.deleteImageAlert), () -> {
                        if(selectedImage==pos){
                           selectedImage=0;
                           isImageDeleted=true;
                        }
                            listener.onItemClick(holder.close, pos, pos, path);
                            list.remove(pos);
                            notifyDataSetChanged();
                    });

                }
            });


        }
        else {

            if (from.equals("from_profile")) {
                holder.close.setVisibility(View.VISIBLE);
//                util.loadGlideImage(context, path.get(pos).getImage(), holder.imgCategoryImage, R.drawable.default_image);
                // Glide.with(context).load(path.get(pos).getImage()).into(holder.imgCategoryImage);
               if (path.get(pos).getImageUri()==null) {
                   Picasso.get()
                           .load(path.get(pos).getImage())
                           .placeholder(R.drawable.gradient_attachment)
                           .error(R.drawable.gradient_attachment)
                           .into(holder.imgCategoryImage);



               }
               else
                   Picasso.get()
                           .load(path.get(pos).getImageUri())
                           .placeholder(R.drawable.gradient_attachment)
                           .error(R.drawable.gradient_attachment)
                           .into(holder.imgCategoryImage);
            /*    if (pos == 0 && selectedImage==0) {
                    holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.red));
                    holder.imgCategoryImage.setPadding(7, 7, 7, 7);
                }


                else*/

                 if(path.get(pos).getImage().equalsIgnoreCase(selectedImageUrl) &&  isImageNotClicked){
                       Log.d(TAG, "onBindViewHolder: inside true");
                       holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.red));
                       holder.imgCategoryImage.setPadding(7, 7, 7, 7);
                   }
                else if(selectedImage==pos){
                    holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.red));
                    holder.imgCategoryImage.setPadding(7, 7, 7, 7);
                }
                else{
                    holder.imgCategoryImage.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.imgCategoryImage.setPadding(0,0,  0, 0);
                }

                 holder.imgCategoryImage.setOnClickListener(v ->{
                          selectedImage=pos;
                          isImageNotClicked=false;
                          listener.onItemClick(holder.imgCategoryImage,pos,path.get(pos).getId(),null);
                          notifyDataSetChanged();
                });
            } else {
                holder.imgCategoryImage.setImageResource(R.drawable.default_image);
                holder.close.setVisibility(View.GONE);
            }
            holder.close.setOnClickListener(v -> {
                if (path.size() == 1) {
                    DialogUtils.showAlert(context, context.getString(R.string.oneImageRequired), () -> {

                    });
                } else {
                    DialogUtils.showAlert(context, context.getString(R.string.deleteImageAlert), () -> {
                        if (path.get(pos).getImageUri()!= null) {
                            path.remove(pos);
                            notifyDataSetChanged();
                        } else {

                            listener.onItemClick(holder.close, pos, path.get(pos).getId(), path);
                            path.remove(pos);
                            notifyDataSetChanged();
                        }


                    });

                }
            });
        }
    }


    @Override
    public int getItemCount() {

        if (from.isEmpty())
            return list.size();
        else if (from.equals("from_profile"))
            return path == null ? 0 : path.size();
        else if (from.equals("from_create_profile")){
            return list == null ? 0 : list.size();
        }
        else {
            chk = 1;
            return 1;
        }
    }

    public void add(ProfileImage uri) {

        path.add(uri);
        notifyDataSetChanged();
    }

    public void addUri(Uri uri){
        list.add(uri);
        notifyDataSetChanged();
    }

    public void setSelectedImageUrl(String selectedImageUrl){
        this.selectedImageUrl=selectedImageUrl;
        notifyDataSetChanged();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategoryImage, close;
        CardView cardView;

        public CommonViewHolder(View itemView) {
            super(itemView);

            imgCategoryImage = itemView.findViewById(R.id.img_categoryImage);
            close = itemView.findViewById(R.id.img_categoryImage_close);
            cardView = itemView.findViewById(R.id.card_view);
        }


    }


}
