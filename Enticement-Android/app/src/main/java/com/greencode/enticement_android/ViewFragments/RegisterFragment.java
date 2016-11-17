package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cengalabs.flatui.views.FlatEditText;
import com.greencode.enticement_android.Activities.MainActivity;
import com.greencode.enticement_android.R;

public class RegisterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FlatEditText mEmailField;
    private FlatEditText mPassword;
    private FlatEditText mConfirmPassword;
    private View rootView;
    private Button mRegButton;
    private View mRegisterView;
    private View mProgressView;
    private ImageButton mBack;
    private ImageButton mClose;
    private FragmentTransaction ft;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        mRegisterView = rootView.findViewById(R.id.email_register_form);
        mProgressView = rootView.findViewById(R.id.register_progress);

        mEmailField = (FlatEditText) rootView.findViewById(R.id.reg_emailinput);
        mPassword = (FlatEditText) rootView.findViewById(R.id.register_passwordinput);
        mConfirmPassword = (FlatEditText) rootView.findViewById(R.id.register_repasswordinput);
        mRegButton = (Button) rootView.findViewById(R.id.register_regbtn);
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        mBack = (ImageButton)  rootView.findViewById(R.id.reg_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
                ft.replace(R.id.contentFragment, new LoginFragment(), "LoginFragment");
                ft.commit();
            }
        });

        return rootView;
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
