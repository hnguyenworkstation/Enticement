package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.LayoutControllers.MyChatRoomRecyclerViewAdapter;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.R;


public class ListChatroomFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefLayout;
    private RecyclerView mRecycler;
    private MyChatRoomRecyclerViewAdapter mAdapter;

    public ListChatroomFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListChatroomFragment newInstance(int columnCount) {
        ListChatroomFragment fragment = new ListChatroomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom_list, container, false);

        Context context = view.getContext();
        mRecycler = (RecyclerView) view.findViewById(R.id.chatroom_recycler);
        if (mColumnCount <= 1) {
            mRecycler.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mAdapter = new MyChatRoomRecyclerViewAdapter(getContext());
        mRecycler.setAdapter(mAdapter);

        mSwipeRefLayout = (SwipeRefreshLayout) view.findViewById(R.id.chatroom_swiperef);
        mSwipeRefLayout.setColorSchemeResources(R.color.orange1, R.color.green1, R.color.blue1);
        mSwipeRefLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppUtils.showToast("Refreshed", getContext());
                        mSwipeRefLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }
}
