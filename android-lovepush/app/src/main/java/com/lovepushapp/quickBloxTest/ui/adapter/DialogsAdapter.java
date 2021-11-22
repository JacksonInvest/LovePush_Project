package com.lovepushapp.quickBloxTest.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.lovepushapp.model.response.Chat.UserChatList;
import com.quickblox.chat.model.QBChatDialog;
import com.lovepushapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class DialogsAdapter extends BaseAdapter {
    private Context context;

    private List<QBChatDialog> dialogs;
    private List<UserChatList.Data> dataList;
    private String myUserId = "";
    public DialogsAdapter(String my_user_id, Context context, List<QBChatDialog> dialogs, List<UserChatList.Data> filteredChatList) {
        this.context = context;
        this.dialogs = dialogs;
        this.myUserId = my_user_id;
        //this.dataList = filteredChatList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);

            holder = new ViewHolder();
            holder.rootLayout =  convertView.findViewById(R.id.allView);
            holder.nameTextView =  convertView.findViewById(R.id.sender_name_tv);
            holder.lastMessageTextView =  convertView.findViewById(R.id.text_dialog_last_message);
            holder.dialogImageView =  convertView.findViewById(R.id.image_dialog_icon);
            holder.unreadCounterTextView =  convertView.findViewById(R.id.text_dialog_unread_count);
            holder.lastMsgTimeTextView = convertView.findViewById(R.id.lastMsgTimeTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        QBChatDialog dialog = getItem(position);
        Log.d("DialogAdapter", "getDataList: "+dataList);

        for (UserChatList.Data item: dataList) {
            if(item.getQbChatDialog().getQb_dialog_id().equals(dialog.getDialogId())){

                if (!myUserId.equalsIgnoreCase(item.getUser_info1().getId())){
                    holder.nameTextView.setText(item.getUser_info1().getName());

                    Picasso.get()
                            .load(item.getUser_info1().getProfile_image())
                            .placeholder(R.drawable.profile_bg)
                            .error(R.drawable.profile_bg)
                            .into(holder.dialogImageView);

                }else{
                    holder.nameTextView.setText(item.getUser_info2().getName());
                    Picasso.get()
                            .load(item.getUser_info2().getProfile_image())
                            .placeholder(R.drawable.profile_bg)
                            .error(R.drawable.profile_bg)
                            .into(holder.dialogImageView);
                }

                break;
            }
        }

       /* if (dataList.get(position).getEvent_type().equalsIgnoreCase("L"))
        {
            holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.blue));
//            Blue
        }
        else
        if (dataList.get(position).getEvent_type().equalsIgnoreCase("C"))
        {
//                red
            holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        }
        else
        if (dataList.get(position).getEvent_type().equalsIgnoreCase(""))
        {
//                    grey
            holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.grey));
        }*/
       /* if (dialog.getType().equals(QBDialogType.GROUP)) {
           // holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
            holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);
        } else {
           // holder.dialogImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
            holder.dialogImageView.setImageDrawable(null);
        }*/
        //holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);


        holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));
        holder.lastMsgTimeTextView.setText(prepareLastMsgTime(dialog.getLastMessageDateSent()));


        int unreadMessagesCount = getUnreadMsgCount(dialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? "99+" : unreadMessagesCount));
        }

        /*holder.rootLayout.setBackgroundColor(isItemSelected(position) ? ResourceUtils.getColor(R.color.selected_list_item_color) :
                ResourceUtils.getColor(android.R.color.transparent));
*/
        return convertView;
    }

    @Override
    public QBChatDialog getItem(int position) {
        return dialogs.get(position);
    }

    @Override
    public long getItemId(int id) {
        return (long) id;
    }

    @Override
    public int getCount() {
        return dialogs != null ? dialogs.size() : 0;
    }


    private int getUnreadMsgCount(QBChatDialog chatDialog) {
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            unreadMessageCount = 0;
        }
        return unreadMessageCount;
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog) {
        if (isLastMessageAttachment(chatDialog)) {
            return context.getString(R.string.chat_attachment);
        } else {
            return chatDialog.getLastMessage();
        }
    }

    private String prepareLastMsgTime(long miliseconds) {

        Log.d("GetTime", "prepareLastMsgTime: "+miliseconds);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        String msgData = dateFormat.format(new Date(miliseconds * 1000));
        String todayDate = dateFormat.format(date);
        String yesterDayDate = dateFormat.format(cal.getTime());
        if (msgData.equals(todayDate)) {
            return dateFormat1.format(new Date(miliseconds * 1000));
        }else if (msgData.equals(yesterDayDate)){
            return "Yesterday";
        }else {
            return dateFormat.format(new Date(miliseconds * 1000));
        }

    }

    public void updateList(List<QBChatDialog> dialogs, List<UserChatList.Data> userDetailsList) {
        this.dialogs = dialogs;
        this.dataList = userDetailsList;

        notifyDataSetChanged();
    }


    private static class ViewHolder {
        ViewGroup rootLayout;
        CircleImageView dialogImageView;
        EmojiconTextView lastMessageTextView;
        AppCompatTextView nameTextView,lastMsgTimeTextView,unreadCounterTextView;

    }
}