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

public class GetNameFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FlatButton mNext;
    private EditText mNameField;
    private View rootView;

    public GetNameFragment() {
        // Required empty public constructor
    }

    public static GetNameFragment newInstance(String param1, String param2) {
        GetNameFragment fragment = new GetNameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_get_name, container, false);
        mNameField = (EditText) rootView.findViewById(R.id.gn_put_name);

        mNext = (FlatButton) rootView.findViewById(R.id.gn_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameField.getText().toString();
                if (isValidName(name)) {
                    pushName(name);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFragment, new GetNicknameFragment(), "GetNickname");
                    ft.commit();
                } else {
                    mNameField.setError(getString(R.string.error_field_required));
                }
            }
        });

        return rootView;
    }

    private void pushName(String name) {
        EnticementApplication.getInstance().getPrefManager()
                .getProfile().setName(name);
    }

    private boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        return true;
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