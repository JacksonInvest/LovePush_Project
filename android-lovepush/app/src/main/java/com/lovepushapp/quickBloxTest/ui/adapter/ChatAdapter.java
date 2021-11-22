package com.lovepushapp.quickBloxTest.ui.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lovepushapp.quickBloxTest.utils.ResourceUtils;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.helper.CollectionsUtil;
import com.lovepushapp.R;
import com.lovepushapp.quickBloxTest.managers.DialogsManager;
import com.lovepushapp.quickBloxTest.ui.adapter.listeners.AttachClickListener;
import com.lovepushapp.quickBloxTest.ui.adapter.listeners.MessageLinkClickListener;
import com.lovepushapp.quickBloxTest.utils.LinkUtils;
import com.lovepushapp.quickBloxTest.utils.MessageTextClickMovement;
import com.lovepushapp.quickBloxTest.utils.TimeUtils;
import com.lovepushapp.quickBloxTest.utils.chat.ChatHelper;
import com.lovepushapp.quickBloxTest.utils.qb.PaginationHistoryListener;
import com.lovepushapp.quickBloxTest.utils.qb.QbUsersHolder;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private static final String TAG = ChatAdapter.class.getSimpleName();
    private final QBChatDialog chatDialog;
    private PaginationHistoryListener paginationListener;
    private int previousGetCount = 0;

    private static final int CUSTOM_VIEW_TYPE = -1;

    private static final int TYPE_TEXT_RIGHT = 1;
    private static final int TYPE_TEXT_LEFT = 2;
    private static final int TYPE_ATTACH_RIGHT = 3;
    private static final int TYPE_ATTACH_LEFT = 4;
    private static final int TYPE_NOTIFICATION_CENTER = 5;
    private static final int TYPE_AUDIO_RIGHT= 6;
    private static final int TYPE_AUDIO_LEFT = 7;


    //Message TextView click listener
    //
    private MessageLinkClickListener messageTextViewLinkClickListener;
    private boolean overrideOnClick;

    private AttachClickListener attachImageClickListener;

    private SparseIntArray containerLayoutRes = new SparseIntArray() {
        {
            put(TYPE_TEXT_RIGHT, R.layout.list_item_text_right);
            put(TYPE_TEXT_LEFT, R.layout.list_item_text_left);
            put(TYPE_ATTACH_RIGHT, R.layout.list_item_attach_right);
            put(TYPE_ATTACH_LEFT, R.layout.list_item_attach_left);
            put(TYPE_NOTIFICATION_CENTER, R.layout.list_item_notif_center);
            put(TYPE_AUDIO_RIGHT, R.layout.list_item_audio_right);
            put(TYPE_AUDIO_LEFT, R.layout.list_item_audio_left);

        }
    };

    private MessageViewHolder viewHolder;
    private List<QBChatMessage> chatMessages;
    private LayoutInflater inflater;
    protected Context context;

    public ChatAdapter(Context context, QBChatDialog chatDialog, List<QBChatMessage> chatMessages) {
        this.chatDialog = chatDialog;
        this.context = context;
        this.chatMessages = chatMessages;
        this.inflater = LayoutInflater.from(context);
    }

    public void updateStatusDelivered(String messageID, Integer userId) {
        for (int position = 0; position < chatMessages.size(); position++) {
            QBChatMessage message = chatMessages.get(position);
            if (message.getId().equals(messageID)) {
                ArrayList<Integer> deliveredIds = new ArrayList<>();
                if (message.getDeliveredIds() != null) {
                    deliveredIds.addAll(message.getDeliveredIds());
                }
                deliveredIds.add(userId);
                message.setDeliveredIds(deliveredIds);
                notifyItemChanged(position);
            }
        }
    }

    public void updateStatusRead(String messageID, Integer userId) {
        for (int position = 0; position < chatMessages.size(); position++) {
            QBChatMessage message = chatMessages.get(position);
            if (message.getId().equals(messageID)) {
                ArrayList<Integer> readIds = new ArrayList<>();
                if (message.getReadIds() != null) {
                    readIds.addAll(message.getReadIds());
                }
                readIds.add(userId);
                message.setReadIds(readIds);
                notifyItemChanged(position);
            }
        }
    }

    /**
     * Sets listener for handling pressed links on message text.
     *
     * @param textViewLinkClickListener listener to set. Must to implement {@link MessageLinkClickListener}
     * @param overrideOnClick           set 'true' if have to himself manage onLinkClick event or set 'false' for delegate
     *                                  onLinkClick event to {@link android.text.util.Linkify}
     */
    public void setMessageTextViewLinkClickListener(MessageLinkClickListener textViewLinkClickListener, boolean overrideOnClick) {
        this.messageTextViewLinkClickListener = textViewLinkClickListener;
        this.overrideOnClick = overrideOnClick;
    }

    public void setAttachImageClickListener(AttachClickListener clickListener) {
        attachImageClickListener = clickListener;
    }

    public void removeAttachImageClickListener() {
        attachImageClickListener = null;
    }

    public void addToList(List<QBChatMessage> items) {
        chatMessages.addAll(0, items);
        notifyItemRangeInserted(0, items.size());
    }

    public void addList(List<QBChatMessage> items) {
        chatMessages.clear();
        chatMessages.addAll(items);
        notifyDataSetChanged();
    }

    public void add(QBChatMessage item) {
        this.chatMessages.add(item);
        this.notifyItemInserted(chatMessages.size() - 1);
    }

    public List<QBChatMessage> getList() {
        return chatMessages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NOTIFICATION_CENTER:
                viewHolder = new NotificationHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.msg_text_message,
                        R.id.msg_text_time_message);
                return viewHolder;
            case TYPE_TEXT_RIGHT:
                viewHolder = new TextMessageHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.msg_text_message,
                        R.id.msg_text_time_message, R.id.msg_link_preview, R.id.msg_text_status_message);
                return viewHolder;
            case TYPE_TEXT_LEFT:
                viewHolder = new TextMessageHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.msg_text_message,
                        R.id.msg_text_time_message, R.id.msg_link_preview);
                return viewHolder;
            case TYPE_ATTACH_RIGHT:
                viewHolder = new ImageAttachHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.msg_image_attach, R.id.msg_progressbar_attach,
                        R.id.msg_text_time_attach, R.id.msg_signs_attach, R.id.msg_attach_status_message);
                return viewHolder;
            case TYPE_ATTACH_LEFT:
                viewHolder = new ImageAttachHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.msg_image_attach, R.id.msg_progressbar_attach,
                        R.id.msg_text_time_attach, R.id.msg_signs_attach);
                return viewHolder;

            case TYPE_AUDIO_RIGHT:
                viewHolder = new AudioMessageHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.playPauseAudio,
                        R.id.msg_text_time_message, R.id.msg_text_status_message,R.id.audio_progess,R.id.audio_duration_tv);
                return viewHolder;
            case TYPE_AUDIO_LEFT:
                viewHolder = new AudioMessageHolder(inflater.inflate(containerLayoutRes.get(viewType), parent, false), R.id.playPauseAudio,
                        R.id.msg_text_time_message,R.id.audio_progess,R.id.audio_duration_tv);
                return viewHolder;
            default:
                Log.d(TAG, "onCreateViewHolder case default");
                // resource must be set manually by creating custom adapter
                return onCreateCustomViewHolder(parent, viewType);
        }
    }

    @Override
    public void onViewRecycled(MessageViewHolder holder) {
        if (holder.getItemViewType() == TYPE_TEXT_LEFT || holder.getItemViewType() == TYPE_TEXT_RIGHT) {
            TextMessageHolder textMessageHolder = (TextMessageHolder) holder;

            if (textMessageHolder.linkPreviewLayout.getTag() != null) {
                textMessageHolder.linkPreviewLayout.setTag(null);
            }
        }

        /*//abort loading avatar before setting new avatar to view
        if (containerLayoutRes.get(holder.getItemViewType()) != 0 && holder.avatar != null) {
            Glide.clear(holder.avatar);
        }*/

        super.onViewRecycled(holder);
    }

    private MessageViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "You must create ViewHolder by your own");
        return null;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        downloadMore(position);
        QBChatMessage chatMessage = getItem(position);
        if (isIncoming(chatMessage) && !isReadByCurrentUser(chatMessage)) {
            readMessage(chatMessage);
        }
        int valueType = getItemViewType(position);
        Log.d(TAG, "onBindViewHolder on position " + position);
        switch (valueType) {
            case TYPE_NOTIFICATION_CENTER:
                onBindViewNotificationHolder((NotificationHolder) holder, chatMessage, position);
                break;
            case TYPE_TEXT_RIGHT:
                onBindViewMsgRightHolder((TextMessageHolder) holder, chatMessage, position);
                break;
            case TYPE_TEXT_LEFT:
                onBindViewMsgLeftHolder((TextMessageHolder) holder, chatMessage, position);
                break;
            case TYPE_ATTACH_RIGHT:
                onBindViewAttachRightHolder((ImageAttachHolder) holder, chatMessage, position);
                break;
            case TYPE_ATTACH_LEFT:
                onBindViewAttachLeftHolder((ImageAttachHolder) holder, chatMessage, position);
                break;
            case TYPE_AUDIO_RIGHT:
                onBindViewAudioRightHolder((AudioMessageHolder) holder, chatMessage, position);

            case TYPE_AUDIO_LEFT:
                onBindViewAudioLeftHolder((AudioMessageHolder) holder, chatMessage, position);

            default:
                onBindViewCustomHolder(holder, chatMessage, position);
                break;
        }
    }

    private void onBindViewNotificationHolder(NotificationHolder holder, QBChatMessage chatMessage, int position) {
        holder.messageTextView.setText(chatMessage.getBody());
        holder.messageTimeTextView.setText(getTime(chatMessage.getDateSent()));
    }

    private void onBindViewMsgRightHolder(TextMessageHolder holder, QBChatMessage chatMessage, int position) {
        fillTextMessageHolder(holder, chatMessage, position, false);
    }

    private void onBindViewMsgLeftHolder(TextMessageHolder holder, QBChatMessage chatMessage, int position) {
        //holder.messageTimeTextView.setVisibility(View.v);
       /* setOpponentsName(holder, chatMessage, false);

        TextView customMessageTimeTextView = holder.itemView.findViewById(R.id.custom_msg_text_time_message);
        customMessageTimeTextView.setText(getTime(chatMessage.getDateSent()));
*/
        fillTextMessageHolder(holder, chatMessage, position, true);
    }

    private void onBindViewAttachRightHolder(ImageAttachHolder holder, QBChatMessage chatMessage, int position) {
        fillAttachmentHolder(holder, chatMessage, position, false);
    }

    private void onBindViewAttachLeftHolder(ImageAttachHolder holder, QBChatMessage chatMessage, int position) {
       // setOpponentsName(holder, chatMessage, true);
        fillAttachmentHolder(holder, chatMessage, position, true);
        setItemAttachClickListener(getAttachListenerByType(position), holder, getAttachment(position), position);
    }

    private void onBindViewAudioRightHolder(AudioMessageHolder holder, QBChatMessage chatMessage, int position) {
        fillAudioHolder(holder, chatMessage, position, false);
        playAudio(holder, position);

    }
    private void onBindViewAudioLeftHolder(AudioMessageHolder holder, QBChatMessage chatMessage, int position) {
        fillAudioHolder(holder, chatMessage, position, true);

        playAudio(holder, position);

    }

    private void onBindViewCustomHolder(MessageViewHolder holder, QBChatMessage chatMessage, int position) {
    }

    private void fillAttachmentHolder(ImageAttachHolder holder, QBChatMessage chatMessage, int position, boolean isLeftMessage) {
        setDateSentAttach(holder, chatMessage);
        displayAttachment(holder, position);

        int valueType = getItemViewType(position);
        String avatarUrl = obtainAvatarUrl(valueType, chatMessage);
        if (avatarUrl != null) {
            displayAvatarImage(avatarUrl, holder.avatar);
        }

        setItemAttachClickListener(getAttachListenerByType(position), holder, getAttachment(position), position);

        if (!isLeftMessage) {
            boolean read = isRead(chatMessage);
            boolean delivered = isDelivered(chatMessage);
            if (read) {
                holder.attachStatusTextView.setText(R.string.statuses_read);
            } else if (delivered) {
                holder.attachStatusTextView.setText(R.string.statuses_delivered);
            } else {
                holder.attachStatusTextView.setText(R.string.statuses_sent);
            }
        }

    }

    private void fillAudioHolder(AudioMessageHolder holder, QBChatMessage chatMessage, int position, boolean isLeftMessage) {
        holder.audioTimeTextView.setText(getTime(chatMessage.getDateSent()));

        if (!isLeftMessage) {
            boolean read = isRead(chatMessage);
            boolean delivered = isDelivered(chatMessage);
            if (read) {
                holder.audioStatusTextView.setText(R.string.statuses_read);
            } else if (delivered) {
                holder.audioStatusTextView.setText(R.string.statuses_delivered);
            } else {
                holder.audioStatusTextView.setText(R.string.statuses_sent);
            }
        }

    }

    private void playAudio(AudioMessageHolder holder, int position) {
        QBAttachment attachment = getAttachment(position);

        if (QBAttachment.AUDIO_TYPE.equalsIgnoreCase(attachment.getType())) {
            loadAudio(holder, position,attachment.getDuration());
        }
    }

    private void loadAudio(AudioMessageHolder holder, int position, int duration) {
        final boolean[] isPlay = {false};
        String audioUrl = getImageUrl(position);
        MediaPlayer mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        duration=duration/1000;
        long minute=duration/(60);
        long second=duration-(minute*60);
        String minutes = (minute == 0) ? "0" + minute : "" + minute;
        String seconds = (second >= 0) ? "0" + second : "" + second;

        if (duration==0){
            holder.audio_duration_tv.setText("00:00");
        }else {
            holder.audio_duration_tv.setText(minutes+":"+seconds);
        }

        holder.audioImageView.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            //holder.audioImageView.setVisibility(View.GONE);
            //holder.msg_progressbar_attach.setVisibility(View.VISIBLE);
            try {

                mediaplayer.setDataSource(audioUrl);
                mediaplayer.prepare();


            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!isPlay[0]){

                /*if (mediaplayer.isPlaying ()) {
                    mediaplayer.stop ();
                    holder.audioImageView.setImageResource(R.drawable.exo_icon_play);
                    isPlay[0] = false;
                } else {
                }*/
                //holder.audioImageView.setVisibility(View.VISIBLE);
                //holder.msg_progressbar_attach.setVisibility(View.GONE);
                mediaplayer.start ();
                holder.audioImageView.setImageResource(R.drawable.exo_icon_pause);
                isPlay[0] = true;

                // mediaplayer.start();

            }else {
                mediaplayer.pause();
                holder.audioImageView.setImageResource(R.drawable.exo_icon_play);
                isPlay[0] = false;
            }

            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    holder.audioImageView.setImageResource(R.drawable.exo_icon_play);
                    isPlay[0] = false;
                }

            });

          /*  mediaplayer.setOnErrorListener((mp, what, extra) -> {
                mp.reset();

                holder.msg_progressbar_attach.setVisibility(View.GONE);
                holder.audioImageView.setVisibility(View.VISIBLE);
                return false;
            });

            mediaplayer.setOnPreparedListener(mp -> {
                *//*holder.msg_progressbar_attach.setVisibility(View.GONE);
                holder.audioImageView.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();*//*

                *//*if (mediaplayer.isPlaying ()) {
                    mediaplayer.stop ();
                    holder.audioImageView.setImageResource(R.drawable.exo_icon_play);
                    isPlay[0] = false;
                } else {
                    mediaplayer.start ();
                    holder.audioImageView.setImageResource(R.drawable.exo_icon_pause);
                    isPlay[0] = true;
                }*//*
            });*/

        });

    }



    private void fillTextMessageHolder(TextMessageHolder holder, QBChatMessage chatMessage, int position, boolean isLeftMessage) {
        holder.linkPreviewLayout.setVisibility(View.GONE);
        holder.messageTextView.setText(chatMessage.getBody());
        holder.messageTimeTextView.setText(getTime(chatMessage.getDateSent()));

        setMessageTextViewLinkClickListener(holder, position);

       /* int valueType = getItemViewType(position);
        String avatarUrl = obtainAvatarUrl(valueType, chatMessage);
        if (avatarUrl != null) {
            displayAvatarImage(avatarUrl, holder.avatar);
        }*/

        List<String> urlsList = LinkUtils.extractUrls(chatMessage.getBody());
        if (urlsList.isEmpty()) {
            holder.messageTextView.setMaxWidth(context.getResources().getDisplayMetrics().widthPixels);
        } else {
            holder.messageTextView.setMaxWidth((int) context.getResources().getDimension(R.dimen.link_preview_width));
            holder.linkPreviewLayout.setTag(chatMessage.getId());
        }

        if (!isLeftMessage) {
            boolean read = isRead(chatMessage);
            boolean delivered = isDelivered(chatMessage);
            if (read) {
                holder.messageStatusTextView.setText(R.string.statuses_read);
            } else if (delivered) {
                holder.messageStatusTextView.setText(R.string.statuses_delivered);
            } else {
                holder.messageStatusTextView.setText(R.string.statuses_sent);
            }
        }
    }


    private String getSenderName(QBChatMessage chatMessage) {
        QBUser sender = QbUsersHolder.getInstance().getUserById(chatMessage.getSenderId());
        String fullName = "";
        if (sender != null && !TextUtils.isEmpty(sender.getFullName())) {
            fullName = sender.getFullName();
        }
        return fullName;
    }

    private void readMessage(QBChatMessage chatMessage) {
        try {
            chatDialog.readMessage(chatMessage);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private boolean isReadByCurrentUser(QBChatMessage chatMessage) {
        Integer currentUserId = ChatHelper.getCurrentUser().getId();
        return !CollectionsUtil.isEmpty(chatMessage.getReadIds()) && chatMessage.getReadIds().contains(currentUserId);
    }

    private boolean isRead(QBChatMessage chatMessage) {
        boolean read = false;
        Integer recipientId = chatMessage.getRecipientId();
        Integer currentUserId = ChatHelper.getCurrentUser().getId();
        Collection<Integer> readIds = chatMessage.getReadIds();
        if (readIds == null) {
            return false;
        }
        if (recipientId != null && !recipientId.equals(currentUserId) && readIds.contains(recipientId)) {
            read = true;
        } else if (readIds.size() == 1 && readIds.contains(currentUserId)) {
            read = false;
        } else if (readIds.size() > 0) {
            read = true;
        }
        return read;
    }

    private boolean isDelivered(QBChatMessage chatMessage) {
        Integer recipientId = chatMessage.getRecipientId();
        Integer currentUserId = ChatHelper.getCurrentUser().getId();
        Collection<Integer> deliveredIds = chatMessage.getDeliveredIds();
        if (deliveredIds == null) {
            return false;
        }
        boolean delivered = false;
        if (recipientId != null && !recipientId.equals(currentUserId) && deliveredIds.contains(recipientId)) {
            delivered = true;
        } else if (deliveredIds.size() == 1 && deliveredIds.contains(currentUserId)) {
            delivered = false;
        } else if (deliveredIds.size() > 0) {
            delivered = true;
        }
        return delivered;
    }

    public void setPaginationHistoryListener(PaginationHistoryListener paginationListener) {
        this.paginationListener = paginationListener;
    }

    private void downloadMore(int position) {
        if (position == 0) {
            if (getItemCount() != previousGetCount) {
                paginationListener.downloadMore();
                previousGetCount = getItemCount();
            }
        }
    }

    @Override
    public long getHeaderId(int position) {
        QBChatMessage chatMessage = getItem(position);
        return TimeUtils.getDateAsHeaderId(chatMessage.getDateSent() * 1000);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_chat_message_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        TextView dateTextView = view.findViewById(R.id.header_date_textview);

        QBChatMessage chatMessage = getItem(position);
        dateTextView.setText(getDate(chatMessage.getDateSent()));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) dateTextView.getLayoutParams();
        lp.topMargin = 0;
        dateTextView.setLayoutParams(lp);
    }

    private void setMessageTextViewLinkClickListener(TextMessageHolder holder, int position) {
        if (messageTextViewLinkClickListener != null) {
            MessageTextClickMovement customClickMovement =
                    new MessageTextClickMovement(messageTextViewLinkClickListener, overrideOnClick, context);
            customClickMovement.setPositionInAdapter(position);

            holder.messageTextView.setMovementMethod(customClickMovement);
        }
    }

    private AttachClickListener getAttachListenerByType(int position) {
        QBAttachment attachment = getAttachment(position);
        if (QBAttachment.PHOTO_TYPE.equalsIgnoreCase(attachment.getType()) ||
                QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType())) {
            return attachImageClickListener;
        }
        return null;
    }

    private void setDateSentAttach(ImageAttachHolder holder, QBChatMessage chatMessage) {
        holder.attachTimeTextView.setText(getTime(chatMessage.getDateSent()));
    }

    @Nullable
    private String obtainAvatarUrl(int valueType, QBChatMessage chatMessage) {
        return null;
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    private QBChatMessage getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        QBChatMessage chatMessage = getItem(position);

        if (chatMessage.getProperty(DialogsManager.PROPERTY_NOTIFICATION_TYPE) != null) {
            return TYPE_NOTIFICATION_CENTER;
        }

        if (hasAttachments(chatMessage)) {
            QBAttachment attachment = getAttachment(position);

            if (QBAttachment.PHOTO_TYPE.equalsIgnoreCase(attachment.getType()) ||
                    QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType())) {
                return isIncoming(chatMessage) ? TYPE_ATTACH_LEFT : TYPE_ATTACH_RIGHT;
            }
            if (QBAttachment.AUDIO_TYPE.equalsIgnoreCase(attachment.getType())) {
                return isIncoming(chatMessage) ? TYPE_AUDIO_LEFT : TYPE_AUDIO_RIGHT;
            }
        } else {
            return isIncoming(chatMessage) ? TYPE_TEXT_LEFT : TYPE_TEXT_RIGHT;
        }
        return customViewType(position);
    }

    private int customViewType(int position) {
        return CUSTOM_VIEW_TYPE;
    }

    private boolean isIncoming(QBChatMessage chatMessage) {
        QBUser currentUser = QBChatService.getInstance().getUser();
        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }

    private boolean hasAttachments(QBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }

    /**
     * @return string in "Hours:Minutes" format, i.e. <b>10:15</b>
     */
    private String getTime(long seconds) {
        Log.d("GetTime","SendTime "+seconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date(seconds * 1000));
    }

    /**
     * @return string in "Month Day" format, i.e. <b>APRIL 25</b>
     */
    private static String getDate(long milliseconds) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        String msgData = dateFormat.format(new Date(milliseconds * 1000));
        String todayDate = dateFormat.format(date);
        String yesterDayDate = dateFormat.format(cal.getTime());
        if (msgData.equals(todayDate)) {
            return "Today";
        }else if (msgData.equals(yesterDayDate)){
            return "YesterDay";
        }else {
            return dateFormat.format(new Date(milliseconds * 1000));
        }

    }


    private static Calendar clearTimes(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c;
    }

    private void displayAttachment(MessageViewHolder holder, int position) {
        QBAttachment attachment = getAttachment(position);

        if (QBAttachment.PHOTO_TYPE.equalsIgnoreCase(attachment.getType()) ||
                QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType())) {
            showPhotoAttach(holder, position);
        }
    }

    private void showPhotoAttach(MessageViewHolder holder, int position) {
        String imageUrl = getImageUrl(position);
        showImageByURL(holder, imageUrl, position);
    }

    private String getImageUrl(int position) {
        QBAttachment attachment = getAttachment(position);
        return QBFile.getPrivateUrlForUID(attachment.getId());
    }

    private QBAttachment getAttachment(int position) {
        QBChatMessage chatMessage = getItem(position);
        return chatMessage.getAttachments().iterator().next();
    }

    private void showImageByURL(MessageViewHolder holder, String url, int position) {
        int preferredImageWidth = (int) context.getResources().getDimension(R.dimen.attach_image_width_preview);
        int preferredImageHeight = (int) context.getResources().getDimension(R.dimen.attach_image_height_preview);

       /* Glide.with(context)
                .load(url)
                *//*.listener(this.<String, GlideDrawable>getRequestListener(holder, position))
                .override(preferredImageWidth, preferredImageHeight)
                .dontTransform()
                .error(R.drawable.ic_error)*//*
                .into(((ImageAttachHolder) holder).attachImageView);*/


        // This is for loading the low quality images
        /* Picasso.get().load(url).resize(ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size), ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size)).into(((ImageAttachHolder) holder).attachImageView);
        ((ImageAttachHolder) holder).attachProgressBar.setVisibility(View.GONE);
*/

        // It will load image with original quality
        Picasso.get().load(url).into(((ImageAttachHolder) holder).attachImageView);
        ((ImageAttachHolder) holder).attachProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "showImageByURL: Url : "+url);

       /* RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.gradient_attachment)
                .error(R.drawable.gradient_attachment)
                .priority(Priority.HIGH);

        new GlideImageLoader(((ImageAttachHolder) holder).attachImageView,
                ((ImageAttachHolder) holder).attachProgressBar).load(url,options);*/
        /*((ImageAttachHolder) holder).attachProgressBar.setVisibility(View.VISIBLE);

        Glide.with(context).load(url).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                ((ImageAttachHolder) holder).attachProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                //if you want to convert the drawable to ImageView
                Bitmap bitmapImage  = ((BitmapDrawable) resource).getBitmap();

                ((ImageAttachHolder) holder).attachProgressBar.setVisibility(View.GONE);


                return false;
            }
        }).into(((ImageAttachHolder) holder).attachImageView);*/
    }

    /*private RequestListener getRequestListener(MessageViewHolder holder, int position) {
        return new ImageLoadListener<>((ImageAttachHolder) holder);
    }
*/
    private void displayAvatarImage(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);


        Log.d(TAG, "displayAvatarImage: "+url);
    }

    private void setItemAttachClickListener(AttachClickListener listener, MessageViewHolder holder, QBAttachment qbAttachment, int position) {
        if (listener != null) {
            holder.bubbleFrame.setOnClickListener(new ItemClickListenerFilter(listener, qbAttachment, position));
        }
    }

    private static class NotificationHolder extends MessageViewHolder {
        private TextView messageTextView;
        private TextView messageTimeTextView;

        private NotificationHolder(View itemView, @IdRes int msgId, @IdRes int timeId) {
            super(itemView);
            messageTextView = itemView.findViewById(msgId);
            messageTimeTextView = itemView.findViewById(timeId);
        }
    }

    private static class TextMessageHolder extends MessageViewHolder {
        private View linkPreviewLayout;
        private EmojiconTextView messageTextView;
        private TextView messageTimeTextView;
        private TextView messageStatusTextView;

        private TextMessageHolder(View itemView, @IdRes int msgId, @IdRes int timeId, @IdRes int linkPreviewLayoutId, @IdRes int statusId) {
            super(itemView);
            messageTextView = itemView.findViewById(msgId);
            messageTimeTextView = itemView.findViewById(timeId);
            linkPreviewLayout = itemView.findViewById(linkPreviewLayoutId);
            messageStatusTextView = itemView.findViewById(statusId);
        }

        private TextMessageHolder(View itemView, @IdRes int msgId, @IdRes int timeId, @IdRes int linkPreviewLayoutId) {
            super(itemView);
            messageTextView = itemView.findViewById(msgId);
            messageTimeTextView = itemView.findViewById(timeId);
            linkPreviewLayout = itemView.findViewById(linkPreviewLayoutId);
        }
    }

    private static class ImageAttachHolder extends MessageViewHolder {
        private ImageView attachImageView;
        private ProgressBar attachProgressBar;
        private TextView attachTimeTextView;
        private TextView attachStatusTextView;

        private ImageAttachHolder(View itemView, @IdRes int attachId, @IdRes int progressBarId, @IdRes int timeId, @IdRes int signId) {
            super(itemView);
            attachImageView = itemView.findViewById(attachId);
            attachProgressBar = itemView.findViewById(progressBarId);
            attachTimeTextView = itemView.findViewById(timeId);
        }

        private ImageAttachHolder(View itemView, @IdRes int attachId, @IdRes int progressBarId, @IdRes int timeId, @IdRes int signId, @IdRes int statusId) {
            super(itemView);
            attachImageView = itemView.findViewById(attachId);
            attachProgressBar = itemView.findViewById(progressBarId);
            attachTimeTextView = itemView.findViewById(timeId);
            attachStatusTextView = itemView.findViewById(statusId);
        }
    }

    private static class AudioMessageHolder extends MessageViewHolder {
        private ImageView audioImageView;
        private TextView audioTimeTextView;
        private TextView audioStatusTextView,audio_duration_tv;
        private ProgressBar msg_progressbar_attach;

        private AudioMessageHolder(View itemView, @IdRes int attachID, @IdRes int timeId, @IdRes int statusId, @IdRes int progressId,@IdRes int audioDurationId) {
            super(itemView);
            audioImageView = itemView.findViewById(attachID);
            audioTimeTextView = itemView.findViewById(timeId);
            audioStatusTextView = itemView.findViewById(statusId);
            msg_progressbar_attach = itemView.findViewById(progressId);
            audio_duration_tv = itemView.findViewById(audioDurationId);
        }

        private AudioMessageHolder(View itemView, @IdRes int attachId, @IdRes int timeId, @IdRes int progressId,@IdRes int audioDurationId) {
            super(itemView);
            audioImageView = itemView.findViewById(attachId);
            audioTimeTextView = itemView.findViewById(timeId);
            msg_progressbar_attach = itemView.findViewById(progressId);
            audio_duration_tv = itemView.findViewById(audioDurationId);

        }
    }

    public abstract static class MessageViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public View bubbleFrame;

        private MessageViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.msg_image_avatar);
            bubbleFrame = itemView.findViewById(R.id.msg_bubble_background);
        }
    }

    /*protected static class ImageLoadListener<M, P> implements RequestListener<M, P> {
        private ImageAttachHolder holder;

        private ImageLoadListener(ImageAttachHolder holder) {
            this.holder = holder;
            holder.attachProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onException(Exception e, M model, Target<P> target, boolean isFirstResource) {
            Log.e(TAG, "ImageLoadListener Exception= " + e);
            holder.attachImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.attachProgressBar.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(P resource, M model, Target<P> target, boolean isFromMemoryCache, boolean isFirstResource) {
            holder.attachImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.attachProgressBar.setVisibility(View.GONE);
            return false;
        }
    }*/

    private class ItemClickListenerFilter implements View.OnClickListener {
        protected int position;
        private QBAttachment attachment;
        private AttachClickListener chatAttachClickListener;

        ItemClickListenerFilter(AttachClickListener attachClickListener, QBAttachment attachment, int position) {
            this.position = position;
            this.attachment = attachment;
            this.chatAttachClickListener = attachClickListener;
        }

        @Override
        public void onClick(View view) {
            chatAttachClickListener.onLinkClicked(attachment, position);
        }
    }
}