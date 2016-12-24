package com.greencode.enticement_android.LayoutControllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencode.enticement_android.Models.ChatRoom;
import com.greencode.enticement_android.R;

import java.util.ArrayList;
import java.util.List;

public class MyChatRoomRecyclerViewAdapter extends  RecyclerView.Adapter<MyChatRoomRecyclerViewAdapter.ViewHolder>{
    private List<ChatRoom> listRooms = new ArrayList<>();
    private Context mContext;

    public MyChatRoomRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chatroom_item_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listRooms.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatRoom roomModel = listRooms.get(position);
        viewHolder.setAvatar(roomModel.getUser().getImageURL());
        viewHolder.setName(roomModel.getUser().getName());
        viewHolder.setLastMsg(roomModel.getLastMessage());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView avatar;
        TextView name;
        TextView message;
        TextView timestamp;

        public ViewHolder(View view) {
            super(view);
            avatar = (ImageView) view.findViewById(R.id.chatroom_avatar);
            name = (TextView) view.findViewById(R.id.chatroom_name);
            message = (TextView) view.findViewById(R.id.chatroom_lastmsg);
            timestamp = (TextView) view.findViewById(R.id.chatroom_time);
        }

        @Override
        public void onClick(View view) {

        }

        void setAvatar(String link) {

        }

        void setLastMsg(String msg) {
            this.message.setText(msg);
        }

        void setName(String name) {
            this.name.setText(name);
        }
    }
}
