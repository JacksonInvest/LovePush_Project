package com.lovepushapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.helper.TimeAgo;
import com.lovepushapp.model.response.Chat.UserChatList;
import com.lovepushapp.model.response.CommonResponse;
import com.lovepushapp.model.response.blockUserResponse;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.quickblox.chat.model.QBChatDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Response;

public class ChatDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;

    private List<QBChatDialog> dialogs;
    private List<UserChatList.Data> dataList;
    private List<UserChatList.Data> actualList;
    private String myUserId = "";
    private Integer my_qb_id = 0;
    OnItemClickListenerCutom listener;

    public ChatDialogAdapter(String my_user_id, Integer my_qb_id, Activity context, List<QBChatDialog> dialogs, List<UserChatList.Data> filteredChatList, OnItemClickListenerCutom listener) {
        this.listener = listener;

        this.myUserId = my_user_id;
        this.my_qb_id = my_qb_id;
        this.context = context;
        this.dialogs = dialogs;
        this.actualList= new ArrayList<>();
        this.dataList = filteredChatList;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_list, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();
        holder.sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.sample1.addDrag(SwipeLayout.DragEdge.Left, holder.sample1.findViewById(R.id.bottom_wrapper));
        holder.sample1.addDrag(SwipeLayout.DragEdge.Right, holder.sample1.findViewById(R.id.bottom_wrapper_2));


        holder.sample1.getSurfaceView().setOnClickListener(v -> listener.onItemClick(holder.nameTextView, pos, 101, dataList));
        holder.sample1.getSurfaceView().setOnLongClickListener(v -> {
            //Toast.makeText(MyActivity.this, "longClick on surface", Toast.LENGTH_SHORT).show();
            return true;
        });
        holder.sample1.findViewById(R.id.report).setOnClickListener(v -> {
            //Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show();
            listener.onItemClick(holder.nameTextView, pos, 102, dataList);

        });
        holder.sample1.findViewById(R.id.block).setOnClickListener(v -> {
            //alertDialog(pp);
            listener.onItemClick(holder.nameTextView, pos, 112, actualList);

        });

        holder.sample1.findViewById(R.id.blockTV).setOnClickListener(v -> {
            //Toast.makeText(context, "Block", Toast.LENGTH_SHORT).show();
            listener.onItemClick(holder.nameTextView, pos, 103, dataList);
        });

        holder.sample1.findViewById(R.id.removeTV).setOnClickListener(v -> {
            //Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
            listener.onItemClick(holder.nameTextView, pos, 104, dataList);
        });

//        Log.e("")
        QBChatDialog dialog = dialogs.get(pos);
//        Log.d("DialogAdapter", "getDataList: "+dataList.get(pos).getQbChatDialog().getQb_dialog_id() +" dialogid "+ dialog.getDialogId());

        for (UserChatList.Data item: dataList) {
            if(item.getQbChatDialog().getQb_dialog_id().equals(dialog.getDialogId())){
                actualList.add(item);
                if (!myUserId.equalsIgnoreCase(item.getUser_info1().getId()))
                {
                    holder.nameTextView.setText(item.getUser_info1().getName());

                    Picasso.get()
                            .load(item.getUser_info1().getProfile_image())
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(holder.dialogImageView);

                    if (item.getEvent_type().equalsIgnoreCase("L"))
                    {
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
//            Blue
                    }
                    else
                    if (item.getEvent_type().equalsIgnoreCase("C"))
                    {
//                red
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.blue));
                    }
                    else {
//                    grey
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.grey));
                    }


                }else{
                    holder.nameTextView.setText(item.getUser_info2().getName());
                    Picasso.get()
                            .load(item.getUser_info2().getProfile_image())
                            .placeholder(R.drawable.placeholder_male_square)
                            .error(R.drawable.placeholder_male_square)
                            .into(holder.dialogImageView);

                    if (item.getEvent_type().equalsIgnoreCase("L"))
                    {
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
//            Blue
                    }
                    else
                    if (item.getEvent_type().equalsIgnoreCase("C"))
                    {
//                red
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.blue));
                    }
                    else
                        {
//                    grey
                        holder.dialogImageView.setBorderColor(context.getResources().getColor(R.color.grey));
                    }
                }

                break;
            }
        }


       /* if (dialog.getType().equals(QBDialogType.GROUP)) {
           // holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
            holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);
        } else {
           // holder.dialogImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
            holder.dialogImageView.setImageDrawable(null);
        }*/
        //holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);

        // holder.nameTextView.setText(QbDialogUtils.getDialogName(dialog));

        if (dialog.getLastMessageUserId() != null){
            if (dialog.getLastMessageUserId().equals(my_qb_id)){

                holder.lastMessageTextView.setText("You: "+prepareTextLastMessage(dialog));
            }else {
                holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));

            }
        }


        String MyFinalValue =  new TimeAgo().covertTimeToText(dialog.getLastMessageDateSent(),context);
        //holder.lastMsgTimeTextView.setText(prepareLastMsgTime(dialog.getLastMessageDateSent()));
        holder.lastMsgTimeTextView.setText(MyFinalValue);


        int unreadMessagesCount = getUnreadMsgCount(dialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? "99+" : unreadMessagesCount));
        }

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

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        CircleImageView dialogImageView;
        EmojiconTextView lastMessageTextView;
        AppCompatTextView nameTextView,lastMsgTimeTextView,unreadCounterTextView;
        SwipeLayout sample1;

        public CommonViewHolder(View itemView) {
            super(itemView);
            nameTextView =  itemView.findViewById(R.id.sender_name_tv);
            lastMessageTextView =  itemView.findViewById(R.id.text_dialog_last_message);
            dialogImageView =  itemView.findViewById(R.id.image_dialog_icon);
            unreadCounterTextView =  itemView.findViewById(R.id.text_dialog_unread_count);
            lastMsgTimeTextView = itemView.findViewById(R.id.lastMsgTimeTextView);
            sample1 = itemView.findViewById(R.id.sample1);
            ButterKnife.bind(this, itemView);
        }


    }
}
