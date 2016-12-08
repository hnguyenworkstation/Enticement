package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.greencode.enticement_android.Activities.MainActivity;
import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.Manifest;
import com.greencode.enticement_android.R;

public class ReviewRegFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_REQUEST = 1;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText mNameEtxt;
    private EditText mNicknameEtxt;
    private EditText mBirthdayEtxt;
    private FlatButton mFinishBtn;
    private ImageView mProfileView;

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
        mNicknameEtxt = (EditText) view.findViewById(R.id.review_nickname);
        mBirthdayEtxt = (EditText) view.findViewById(R.id.review_birthday);
        mFinishBtn = (FlatButton) view.findViewById(R.id.review_finish);

        mProfileView = (ImageView) view.findViewById(R.id.review_profileimg);
        mProfileView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // request permission until user accept it
                    getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
                    return false;
                }
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                return true;
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAllField()) {
                    try {
                        // register current profile to Firebase
                        Firebase.setNewUserProfile(EnticementApplication.getInstance()
                                .getPrefManager()
                                .getProfile());
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.sp_slide_out_down, R.anim.slide_out_to_bottom);
                    } catch (RuntimeException e) {
                        AppUtils.showToast("Failed to register!", getContext(), Toast.LENGTH_LONG);
                    }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
