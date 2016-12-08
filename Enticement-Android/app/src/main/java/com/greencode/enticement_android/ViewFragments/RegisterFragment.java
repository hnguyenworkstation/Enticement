package com.greencode.enticement_android.ViewFragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.greencode.enticement_android.Activities.MainActivity;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.Helpers.StringChecker;
import com.greencode.enticement_android.R;

public class RegisterFragment extends Fragment implements GetNameFragment.OnFragmentInteractionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final EnticementApplication mInstance = EnticementApplication.getInstance();

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
                // startActivity(new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                if (validateAllFields()) {
                    showProgress(true);
                    Firebase.mFBAuth.createUserWithEmailAndPassword(
                            mEmailField.getText().toString()
                            , mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(getContext(), "createUserWithEmail:onComplete:"
                                    + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                if (task.getException()
                                        .getMessage()
                                        .contains(getString(R.string.error_duplicate_email))){
                                    // Todo: show a dialog asking for reset password
                                }
                            } else {
                                mInstance.getPrefManager().getProfile().setEmail(mEmailField.getText().toString());
                                ft = getFragmentManager().beginTransaction();
                                ft.setCustomAnimations(R.anim.fade_in_from_right, R.anim.fade_out_to_left);
                                ft.replace(R.id.contentFragment, new GetNameFragment(), "GetNameFragment");
                                ft.commit();
                            }
                        }
                    });
                }
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean validateAllFields() {
        String email = mEmailField.getText().toString();
        String password = mPassword.getText().toString();
        String repass = mConfirmPassword.getText().toString();

        if (email.isEmpty()) {
            mEmailField.setError("Email is required!");
            mEmailField.setFocusable(true);
            return false;
        } else if (password.isEmpty()) {
            mPassword.setError("Password is required!");
            mPassword.setFocusable(true);
            return false;
        } else if (repass.isEmpty()) {
            mConfirmPassword.setError("You must confirm your password!");
            mConfirmPassword.setFocusable(true);
            return false;
        } else if (!password.equals(repass)) {
            mPassword.setError("Password is required!");
            mPassword.setFocusable(true);
            mConfirmPassword.setError("Confirm password doesn't match!");
            return false;
        } else {
            if (StringChecker.validateEmail(email)) {
                return true;
            } else {
                mEmailField.setError("Invalid email address!");
                mEmailField.setFocusable(true);
                return false;
            }
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
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
