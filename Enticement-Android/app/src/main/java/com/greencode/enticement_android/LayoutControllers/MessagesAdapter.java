package com.greencode.enticement_android.LayoutControllers;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.greencode.enticement_android.Activities.ChatRoomActivity;
import com.greencode.enticement_android.Interfaces.MessageClickListener;
import com.greencode.enticement_android.Models.Message;
import com.greencode.enticement_android.R;

import org.w3c.dom.Text;

import vc908.stickerfactory.StickersManager;

/**
 * Created by jasonnguyen on 11/28/16.
 */

public class MessagesAdapter extends FirebaseRecyclerAdapter<Message,MessagesAdapter.MessageViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;

    private Context mContext;

    public MessagesAdapter(DatabaseReference ref, Context context) {
        super(Message.class, R.layout.message_from_me, MessagesAdapter.MessageViewHolder.class, ref);
        this.mContext = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_from_me,parent,false);
            return new MessageViewHolder(view);
        }else if (viewType == LEFT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_from_friend,parent,false);
            return new MessageViewHolder(view);
        }else if (viewType == RIGHT_MSG_IMG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_from_me,parent,false);
            return new MessageViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_from_friend,parent,false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message model = getItem(position);
        if (model.getType() == Message.MessageType.MESSAGE_IN){
            return LEFT_MSG;
        } else if (model.getType() == Message.MessageType.MESSAGE_OUT) {
            return RIGHT_MSG;
        } else if (model.getType() == Message.MessageType.STICKER_IN) {
            return LEFT_MSG_IMG;
        } else {
            return RIGHT_MSG_IMG;
        }
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
        // viewHolder.setUserProfile(model.getUserModel().getPhoto_profile());

        if (model.getType() == Message.MessageType.STICKER_IN || model.getType() == Message.MessageType.STICKER_OUT) {
            viewHolder.loadSticker(model.getMessage());
            viewHolder.setTimestamp(String.valueOf(model.getTime()));
        } else {
            viewHolder.setTxtMessage(model.getMessage());
            viewHolder.setTimestamp(String.valueOf(model.getTime()));
        }
//        if (model.getFile() != null){
//            viewHolder.tvIsLocation(View.GONE);
//            viewHolder.setIvChatPhoto(model.getFile().getUrl_file());
//        }else if(model.getMapModel() != null){
//            viewHolder.setIvChatPhoto(alessandro.firebaseandroid.util.Util.local(model.getMapModel().getLatitude(),model.getMapModel().getLongitude()));
//            viewHolder.tvIsLocation(View.VISIBLE);
//        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTimeStamp;
        TextView mContent;
        TextView mStatus;
        ImageView mUserProfile;
        ImageView mSticker;
        ImageView mCheckBox;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTimeStamp = (TextView)itemView.findViewById(R.id.message_timestamp);
            mContent = (TextView)itemView.findViewById(R.id.message_content);
            mStatus = (TextView) itemView.findViewById(R.id.message_status);
            mSticker = (ImageView) itemView.findViewById(R.id.message_sticker);
            mUserProfile = (ImageView)itemView.findViewById(R.id.message_userimg);
            mCheckBox = (ImageView)itemView.findViewById(R.id.message_checkbox);
        }

        public void loadSticker(String message) {
            StickersManager.with(mContext)
                    .loadSticker(message)
                    .into(mSticker);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Message model = getItem(position);
//            if (model.getMapModel() != null){
//                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
//            }else{
//                mClickListenerChatFirebase.clickImageChat(view,position,model.getUserModel().getName(),model.getUserModel().getPhoto_profile(),model.getFile().getUrl_file());
//            }
        }

        public void setTxtMessage(String message){
            if (mContent == null)return;
                mContent.setText(message);
        }

        public void setUserProfile(String urlPhotoUser){
            if (mUserProfile == null)return;
            // Glide.with(mUserProfile.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40,40).into(ivUser);
        }

        public void setTimestamp(String timestamp){
            if (mTimeStamp == null)return;
                mTimeStamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url){
//            if (ivChatPhoto == null)return;
//            Glide.with(ivChatPhoto.getContext()).load(url)
//                    .override(100, 100)
//                    .fitCenter()
//                    .into(ivChatPhoto);
//            ivChatPhoto.setOnClickListener(this);
        }

        public void tvIsLocation(int visible){
//            if (tvLocation == null)return;
//            tvLocation.setVisibility(visible);
        }

    }

    private CharSequence converteTimestamp(String mileSegundos){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

}

