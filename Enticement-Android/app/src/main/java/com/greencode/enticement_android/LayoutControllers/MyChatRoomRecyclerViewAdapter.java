package com.greencode.enticement_android.LayoutControllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.Models.ChatRoom;
import com.greencode.enticement_android.R;

import java.util.ArrayList;
import java.util.List;

public class MyChatRoomRecyclerViewAdapter extends FirebaseRecyclerAdapter<ChatRoom, MyChatRoomRecyclerViewAdapter.ViewHolder> {
    private List<ChatRoom> listRooms = new ArrayList<>();
    private Context mContext;

    public MyChatRoomRecyclerViewAdapter(Context context) {
        super(ChatRoom.class, R.layout.chatroom_item_row, MyChatRoomRecyclerViewAdapter.ViewHolder.class, Firebase.ChatRoomRef);
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
    protected void populateViewHolder(ViewHolder viewHolder, ChatRoom model, int position) {
        viewHolder.setAvatar(model.getUser().getImageURL());
        viewHolder.setName(model.getUser().getName());
        viewHolder.setLastMsg(model.getLastMessage());
        viewHolder.setUnreadcount(model.getUnreadCount());
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MyChatRoomRecyclerViewAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MyChatRoomRecyclerViewAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView avatar;
        TextView name;
        TextView message;
        TextView timestamp;
        TextView unreadcount;

        ViewHolder(View view) {
            super(view);
            avatar = (ImageView) view.findViewById(R.id.chatroom_avatar);
            name = (TextView) view.findViewById(R.id.chatroom_name);
            message = (TextView) view.findViewById(R.id.chatroom_lastmsg);
            timestamp = (TextView) view.findViewById(R.id.chatroom_time);
            unreadcount = (TextView) view.findViewById(R.id.chatroom_count);
        }

        void setAvatar(String link) {

        }

        void setLastMsg(String msg) {
            this.message.setText(msg);
        }

        void setName(String name) {
            this.name.setText(name);
        }

        void setUnreadcount(int count) {
            if (count == 0) {
                this.unreadcount.setVisibility(View.GONE);
            } else {
                this.unreadcount.setText(String.valueOf(count));
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
