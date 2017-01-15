package com.greencode.enticement_android.ViewFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.greencode.enticement_android.Activities.MainActivity;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Enticement.EnticementPreferenceManager;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.Databases.Firebase;
import com.greencode.enticement_android.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReviewRegFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_REQUEST = 1;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final EnticementPreferenceManager mPref = EnticementApplication.getInstance().getPrefManager();
    private EditText mNameEtxt;
    private EditText mNicknameEtxt;
    private EditText mBirthdayEtxt;
    private ImageView mImageView;
    private FlatButton mFinishBtn;
    private ImageView mProfileView;
    private Uri mImageURI;

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
                    mPref.getProfile().setNickname(mNicknameEtxt.getText().toString());
                    mPref.getProfile().setName(mNameEtxt.getText().toString());
                    mPref.getProfile().setBirthday(mBirthdayEtxt.getText().toString());

                    try {
                        if (mImageURI != null) {
                            // pushing Profile Image to server
                            Bitmap bitmap = null;
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageURI);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            Firebase.updateImageFromBytes(data, getContext());
                        }

                        // register current profile to Firebase
                        Firebase.setNewUserProfile(EnticementApplication.getInstance()
                                .getPrefManager()
                                .getProfile());

                        Firebase.getNewUserProfile();
                        getActivity().finish();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
                    } catch (RuntimeException e) {
                        AppUtils.showToast("Failed to register!", getContext(), Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        e.printStackTrace();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            mImageURI = data.getData();
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
