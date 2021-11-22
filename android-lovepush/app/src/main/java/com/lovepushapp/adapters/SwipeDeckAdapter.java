package com.lovepushapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daprlabs.cardstack.SwipeDeck;
import com.lovepushapp.R;
import com.lovepushapp.model.response.Explore.Explore;

import java.util.List;

public class SwipeDeckAdapter extends BaseAdapter {

    private List<Explore.Data.User> data;
    private Context context;
    private SwipeDeck swipeDeck;

    public SwipeDeckAdapter(SwipeDeck cardStack, List<Explore.Data.User> data, Context context) {
        this.data = data;
        this.context = context;
        this.swipeDeck=cardStack;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.test_card2, parent, false);
        }
        //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
        ImageView imageView = (ImageView) v.findViewById(R.id.profile_imageIV);
        Glide.with(context).load(data.get(position).getProfileImage()).into(imageView);
        TextView textView = (TextView) v.findViewById(R.id.sample_text);
        TextView name=(TextView)v.findViewById(R.id.nameET);
//        String item = (String) data.get(position).getName();
//        textView.setText(item);
        name.setText(data.get(position).getName());





        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    public void addAll(List<Explore.Data.User> exploreList) {

    }
}

