package com.greencode.enticement_android.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.Models.Topics;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.TopicFragment;

public class TopicActivity extends EnticementActivity
    implements TopicFragment.OnListFragmentInteractionListener {

    private static final String TAG = TopicActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TopicFragment mTopicFragment;
    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        mToolbar = (Toolbar) findViewById(R.id.topicact_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Topic Selector");

        mTopicFragment = new TopicFragment();
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        mFragTransition.add(R.id.topicact_container, mTopicFragment)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Topics.Topic item) {

    }
}
