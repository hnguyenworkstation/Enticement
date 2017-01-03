package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.greencode.enticement_android.Interfaces.ListItemOnFragmentInteractionListener;
import com.greencode.enticement_android.LayoutControllers.EventTabAdapter;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.UI.CustomViewpager;
import com.greencode.enticement_android.UI.SlidingTabLayout;

import java.util.Timer;
import java.util.TimerTask;


public class EventContainerFragment extends Fragment implements ListItemOnFragmentInteractionListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SlidingTabLayout mSlidingTabLayout;
    private CustomViewpager mViewpager;
    private boolean isAnimating = false;
    private View horizontalBar;

    private Animation mAnimationOut;
    private Animation mAnimationIn;

    private Timer timer;
    protected int increate = 0;
    private static final long TIMER_PERIOD = 500;
    private static final long TIMER_DELAY = 0;


    public EventContainerFragment() {
        // Required empty public constructor
    }

    public static EventContainerFragment newInstance(String param1, String param2) {
        EventContainerFragment fragment = new EventContainerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_event_container, container, false);

        mViewpager = (CustomViewpager) rootView.findViewById(R.id.eventsfrag_viewpager);

        EventTabAdapter eventTabAdapter = new EventTabAdapter(getChildFragmentManager());
        mViewpager.setAdapter(eventTabAdapter);

        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.eventsfrag_scrolltab);
        mSlidingTabLayout.setCustomTabView(R.layout.custom_event_tab, R.id.content);
        mSlidingTabLayout.setViewPager(mViewpager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onScrollDown(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initSlideBarAnimation(rootView);

        return rootView;
    }

    private void initSlideBarAnimation(View v) {
        horizontalBar = v.findViewById(R.id.eventsfrag_scrolltabview);
        mAnimationIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down_out);
        mAnimationOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_top);
        mAnimationIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                horizontalBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = false;
                deleteTimer();
            }
        });

        mAnimationOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                horizontalBar.setVisibility(View.INVISIBLE);
                isAnimating = false;
                // Start timer
                setupTimer();
            }
        });

    }

    @Override
    public void onScrollDown(boolean isScrollDown) {
        if (isAnimating)
            return;
        if (isScrollDown) {
            if (horizontalBar.getVisibility() == View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationIn);
                    }
                }, 75);
            }
        } else {
            if (horizontalBar.getVisibility() != View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationOut);
                    }
                }, 75);
            }
        }
    }

    private void deleteTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void setupTimer() {
        if (horizontalBar.getVisibility() != View.VISIBLE) {
            deleteTimer();
            timer = new Timer();
            // Create timer task for bottom bar
            TimerTask myTimerTask = new TimerTask() {

                @Override
                public void run() {
                    if (increate >= 3) {
                        onScrollDown(false);
                        increate = 0;
                    } else {
                        increate++;
                    }
                }
            };
            timer.schedule(myTimerTask, TIMER_DELAY, TIMER_PERIOD);
        }
    }


    @Override
    public void resetCountUpAnimation() {
        increate = 0;
    }

    @Override
    public void clickOnItem(int i) {
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
