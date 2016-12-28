package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.greencode.enticement_android.Activities.TopicActivity;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.LayoutControllers.MyPostModelRecyclerViewAdapter;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.Models.DummyContent.DummyItem;

public class FeaturedFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefLayout;
    private RecyclerView recyclerView;
    private MyPostModelRecyclerViewAdapter mAdapter;


    public FeaturedFragment() {
    }

    @SuppressWarnings("unused")
    public static FeaturedFragment newInstance(int columnCount) {
        FeaturedFragment fragment = new FeaturedFragment();
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
        View view = inflater.inflate(R.layout.fragment_featured, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.featured_recycler);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mAdapter = new MyPostModelRecyclerViewAdapter(DummyContent.ITEMS, mListener);
        recyclerView.setAdapter(mAdapter);

        mSwipeRefLayout = (SwipeRefreshLayout) view.findViewById(R.id.featured_swiperef);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.featured_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.featuredmenu_topic:
                try {
                    AppUtils.showToast("Add Clicked", getContext());
                    startActivity(new Intent(getActivity(), TopicActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } catch (Exception e) {

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        void onListFragmentInteraction(DummyItem item);
    }
}
