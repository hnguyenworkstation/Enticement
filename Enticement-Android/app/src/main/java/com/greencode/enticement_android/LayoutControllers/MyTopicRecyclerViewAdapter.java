package com.greencode.enticement_android.LayoutControllers;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.TopicFragment.OnListFragmentInteractionListener;
import com.greencode.enticement_android.Models.Topics.Topic;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTopicRecyclerViewAdapter extends RecyclerView.Adapter<MyTopicRecyclerViewAdapter.ViewHolder> {
    private final List<Topic> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTopicRecyclerViewAdapter(List<Topic> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTopicIcon.setImageResource(mValues.get(position).logo);
        holder.mTopicName.setText(mValues.get(position).details);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mTopicIcon;
        public final TextView mTopicName;
        public Topic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTopicIcon = (CircleImageView) view.findViewById(R.id.topic_avatar);
            mTopicName = (TextView) view.findViewById(R.id.topic_content);
        }
    }
}
