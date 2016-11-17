package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.R;

public class AroundFragment extends Fragment implements
        View.OnClickListener,
        PeopleAroundFragment.OnListFragmentInteractionListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RadioButton mPeopleRad;
    private RadioButton mGroupRad;
    private FrameLayout mLayoutContainer;
    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;
    private PeopleAroundFragment mPeopleFragment;
    private OnFragmentInteractionListener mListener;

    public AroundFragment() {
        // Required empty public constructor
    }

    public static AroundFragment newInstance(String param1, String param2) {
        AroundFragment fragment = new AroundFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_around, container, false);

        mPeopleFragment = new PeopleAroundFragment();

        mPeopleRad = (RadioButton) rootView.findViewById(R.id.around_peoplerad);
        mPeopleRad.setChecked(true);
        mGroupRad = (RadioButton) rootView.findViewById(R.id.around_grouprad);

        mPeopleRad.setOnClickListener(this);
        mGroupRad.setOnClickListener(this);

        mFragManager = getActivity().getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        initLayoutView();

        return rootView;
    }

    private void initLayoutView() {
        if (mPeopleRad.isChecked()) {
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
            mFragTransition.replace(R.id.around_container, mPeopleFragment);
            mFragTransition.commit();
        }

        if (mGroupRad.isChecked()) {
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.around_container, mPeopleFragment, "PeopleFragment");
            mFragTransition.commit();
        }
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

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.around_peoplerad:
                if (mPeopleRad.isChecked()) {
                    break;
                }
                mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
                mFragTransition.replace(R.id.around_container, mPeopleFragment, "PeopleFragment");
                mFragTransition.commit();
                break;
            case R.id.around_grouprad:
                if (mGroupRad.isChecked()) {
                    break;
                }
                mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
                mFragTransition.replace(R.id.around_container, mPeopleFragment);
                mFragTransition.commit();
                break;
            default:
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
