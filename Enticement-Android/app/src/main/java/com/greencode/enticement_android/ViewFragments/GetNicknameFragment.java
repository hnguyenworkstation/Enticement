package com.greencode.enticement_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cengalabs.flatui.views.FlatButton;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.R;


public class GetNicknameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FlatButton mNext;
    private EditText mNicknameField;
    private View rootView;

    public GetNicknameFragment() {
    }

    public static GetNicknameFragment newInstance(String param1, String param2) {
        GetNicknameFragment fragment = new GetNicknameFragment();
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
        rootView = inflater.inflate(R.layout.fragment_get_nickname, container, false);
        mNicknameField = (EditText) rootView.findViewById(R.id.getnickname_name);

        mNext = (FlatButton) rootView.findViewById(R.id.getnickname_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mNicknameField.getText().toString();
                if (isValidNickName(nickname)) {
                    pushNickname(nickname);
                } else {
                    mNicknameField.setError(getString(R.string.error_field_required));
                }
            }
        });

        return rootView;
    }

    private void pushNickname(String nickname) {
        EnticementApplication.getInstance().getPrefManager()
                .getProfile().setNickname(nickname);
    }

    private boolean isValidNickName(String name) {
        if (name == null) {
            return false;
        }
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