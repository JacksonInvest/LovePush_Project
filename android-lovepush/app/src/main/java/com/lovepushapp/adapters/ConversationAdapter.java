package com.lovepushapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;

import butterknife.ButterKnife;


public class ConversationAdapter extends RecyclerView.Adapter /*implements
        StickyHeaderAdapter */ {

    private static final String TAG = ConversationAdapter.class.getSimpleName();
    final int VIEW_TYPE_MY = 0;
    final int VIEW_TYPE_FRIEND = 1;

    Context context;


    public ConversationAdapter(Activity context, OnItemClickListenerCutom onItemClickListenerCutom) {
        this.context = context;

    }


    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_FRIEND;
        } else {
            return VIEW_TYPE_MY;
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FRIEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout_recivier, parent, false);
            return new FriendViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout_sender, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return 12;/*chatList == null || chatList.size() == 0 ? 0 : chatList.size();*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int post) {
        if (holder instanceof MyViewHolder) {

        } else {
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public FriendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

}
