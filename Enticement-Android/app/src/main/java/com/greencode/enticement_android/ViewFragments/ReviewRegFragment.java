package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cengalabs.flatui.views.FlatButton;
import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Enticement.EnticementPreferenceManager;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.R;

public class ReviewRegFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final EnticementPreferenceManager mPref = EnticementApplication.getInstance().getPrefManager();
    private EditText mNameEtxt;
    private EditText mNicknameEtxt;
    private EditText mBirthdayEtxt;
    private ImageView mImageView;
    private FlatButton mFinishBtn;

    public ReviewRegFragment() {
        // Required empty public constructor
    }

    public static ReviewRegFragment newInstance(String param1, String param2) {
        ReviewRegFragment fragment = new ReviewRegFragment();
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
        View view = inflater.inflate(R.layout.fragment_review_reg, container, false);

        mNameEtxt = (EditText) view.findViewById(R.id.review_name);
        mImageView = (ImageView) view.findViewById(R.id.review_profileimg);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mNicknameEtxt = (EditText) view.findViewById(R.id.review_nickname);
        mBirthdayEtxt = (EditText) view.findViewById(R.id.review_birthday);
        mFinishBtn = (FlatButton) view.findViewById(R.id.review_finish);

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAllField()) {
                    mPref.getProfile().setNickname(mNicknameEtxt.getText().toString());
                    mPref.getProfile().setName(mNameEtxt.getText().toString());
                    mPref.getProfile().setBirthday(mBirthdayEtxt.getText().toString());

                    // register current profile to Firebase
                    Firebase.registerNewUser(mPref.getProfile());
                }
            }
        });

        return view;
    }

    private boolean validateAllField() {
        return true;
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
