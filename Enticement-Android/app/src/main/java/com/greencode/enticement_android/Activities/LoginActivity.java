package com.greencode.enticement_android.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.LayoutControllers.ViewPagerAdapter;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.LoginFragment;
import com.greencode.enticement_android.ViewFragments.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends EnticementActivity
        implements View.OnClickListener,
            LoginFragment.OnFragmentInteractionListener,
            RegisterFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button registerBtn;
    private Fragment loginFragment;
    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(R.anim.fix_anim, R.anim.fix_anim);
        setContentView(R.layout.activity_login);

        loginFragment = new LoginFragment();
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        mFragTransition.replace(R.id.contentFragment, loginFragment);
        mFragTransition.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getBaseContext(), getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Login");
        adapter.addFragment(new RegisterFragment(), "Register");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                viewPager.setCurrentItem(1, true);
                System.out.println("pressed");
                return;
            default:
                return;
        }
    }
}
