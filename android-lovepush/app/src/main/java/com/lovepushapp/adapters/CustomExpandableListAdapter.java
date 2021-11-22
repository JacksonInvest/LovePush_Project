package com.lovepushapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovepushapp.R;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;

    private HashMap<String, List<String>> expandableListDetail;
    List<Integer> drawables;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail,List<Integer> drawables) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;

        this.expandableListDetail = expandableListDetail;
        this.drawables = drawables;
        Log.e( "CustomExpandableListAdapter: ", ""+expandableListTitle+"  "+expandableListDetail);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (expandableListDetail.get(expandableListTitle.get(listPosition))!=null)
        {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
        }
        return 0;

    }


    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_group, null);



        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        //create groupIndicator using TextView drawable
        if (getChildrenCount(listPosition)>0) {
            Drawable zzz ;
            if (isExpanded) {
                zzz = context.getResources().getDrawable(R.drawable.up_arrow);
            } else {
                zzz = context.getResources().getDrawable(R.drawable.arrow_down2);
            }
            zzz.setBounds(0, 0, 25, 25);

            listTitleTextView.setCompoundDrawables(null, null,zzz, null);
        }
        else {
            listTitleTextView.setCompoundDrawables(null, null,null, null);
        }
        convertView.setTag(getGroup(listPosition));






        ImageView img = (ImageView) convertView
                .findViewById(R.id.expandablegroup_menu);
        img.setImageResource(drawables.get(listPosition));

        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}