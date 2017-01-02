package com.greencode.enticement_android.LayoutControllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.Models.ChatRoom;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.Models.UserProfile;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.PeopleAroundFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyPeopleRecyclerViewAdapter extends FirebaseRecyclerAdapter<UserProfile, MyPeopleRecyclerViewAdapter.UserViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private final EnticementApplication mInstance = EnticementApplication.getInstance();

    public MyPeopleRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        super(UserProfile.class, R.layout.user_item_row, MyPeopleRecyclerViewAdapter.UserViewHolder.class, Firebase.UserRef);
        this.mContext = context;
        mListener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_around_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    protected void populateViewHolder(UserViewHolder viewHolder, UserProfile model, int position) {

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyContent.DummyItem mItem;

        public UserViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
