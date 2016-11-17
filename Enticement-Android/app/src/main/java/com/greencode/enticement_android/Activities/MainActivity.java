package com.greencode.enticement_android.Activities;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.LayoutControllers.ViewPagerAdapter;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.FeaturedFragment;
import com.greencode.enticement_android.ViewFragments.ListChatroomFragment;
import com.greencode.enticement_android.ViewFragments.MoreFragment;
import com.greencode.enticement_android.ViewFragments.AroundFragment;
import com.greencode.enticement_android.ViewFragments.PeopleAroundFragment;
import com.greencode.enticement_android.ViewFragments.UpdatesFragment;

public class MainActivity extends EnticementActivity
        implements View.OnClickListener, FeaturedFragment.OnListFragmentInteractionListener,
            AroundFragment.OnFragmentInteractionListener,
            ListChatroomFragment.OnListFragmentInteractionListener,
            UpdatesFragment.OnListFragmentInteractionListener,
            MoreFragment.OnFragmentInteractionListener,
            PeopleAroundFragment.OnListFragmentInteractionListener {
    private ViewPager mViewPager;

    private View rootLayout;
    private boolean isTransforming;
    private ViewPagerAdapter adapter;

    private FloatingActionButton mFloatBtn;
    private FloatingActionButton fab_1;
    private FloatingActionButton fab_2;
    private FloatingActionButton fab_3;

    private Animation showFabAnim1;
    private Animation closeFabAnim1;
    private Animation showFabAnim2;
    private Animation closeFabAnim2;
    private Animation showFabAnim3;
    private Animation closeFabAnim3;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private TextView mActionProfileName;
    private TextView mActionProfileNickname;
    private ImageView mActionProfileImg;
    private ImageButton mActionProfileBtn;

    private final int EVENT_AROUND_POS = 0;
    private final int CHATROOM_POS = 1;

    private final int[] mTabsIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_group_white_24dp,
            R.drawable.ic_chat_white_24dp,
            R.drawable.ic_update_white_24dp,
            R.drawable.ic_more_horiz_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.mainact_root_layout);
        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);
            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            circularRevealActivity();
                        }

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.mainact_ViewPager);

        if (mViewPager != null)
            setupViewPager(mViewPager);

        mFloatBtn = (FloatingActionButton) findViewById(R.id.spyFloatBtn);
        mFloatBtn.setOnClickListener(this);

        fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab_3 = (FloatingActionButton) findViewById(R.id.fab_3);

        fab_1.setOnClickListener(this);
        fab_2.setOnClickListener(this);
        fab_3.setOnClickListener(this);

        showFabAnim1 = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab1);
        closeFabAnim1 = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_fab1);

        showFabAnim2 = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab2);
        closeFabAnim2 = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_fab2);

        showFabAnim3 = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab3);
        closeFabAnim3 = AnimationUtils.loadAnimation(getApplication(), R.anim.hide_fab3);

        mToolbar = (Toolbar) findViewById(R.id.mainact_toolbar);
        setSupportActionBar(mToolbar);

        initActionBar();

        mTabLayout = (TabLayout) findViewById(R.id.mainact_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                mTabLayout.getTabAt(i).setIcon(mTabsIcons[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.spyFloatBtn:
                AppUtils.showToast("Float Clicked", getBaseContext());
                // case float button
                if (isTransforming) {
                    hideFab();
                    isTransforming = false;
                } else {
                    transformFab();
                    isTransforming = true;
                }
                break;
            case R.id.actionbar_mapbutton:
                Toast.makeText(getBaseContext(), "Map Clicked", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(null);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_profile, null);

        mActionProfileBtn = (ImageButton) v.findViewById(R.id.actionbar_mapbutton);
        mActionProfileBtn.setOnClickListener(this);
        mActionProfileImg = (ImageView) v.findViewById(R.id.actionbar_profileimage);
        mActionProfileImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(getBaseContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();
                triggerMyProfileAcitivty(view);
                return true;
            }
        });

        mActionProfileName = (TextView) v.findViewById(R.id.actionbar_profilename);
        mActionProfileNickname = (TextView) v.findViewById(R.id.actionbar_nickname);

        actionBar.setCustomView(v);
    }

    private void triggerMyProfileAcitivty(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                MyProfileActivity.startUserProfileFromLocation(startingLocation, MainActivity.this);
                overridePendingTransition(0, 0);
            }
        }, 200);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getBaseContext(), getSupportFragmentManager());
        adapter.addFragment(new FeaturedFragment(), "Featured");
        adapter.addFragment(new AroundFragment(), "Around");
        adapter.addFragment(new ListChatroomFragment(), "Chatrooms");
        adapter.addFragment(new UpdatesFragment(), "Updates");
        adapter.addFragment(new MoreFragment(), "More");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < adapter.getCount(); i++){
            adapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }

    private void circularRevealActivity() {
        // make the radius longer to cover the inches
        int cx = rootLayout.getWidth();
        int cy = rootLayout.getHeight();

        float finalRadius = Math.max(rootLayout.getWidth() + 20, rootLayout.getHeight() + 20);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        }
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    private void transformFab() {
        FrameLayout.LayoutParams layoutParams
                = (FrameLayout.LayoutParams) fab_1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab_1.getWidth() * 1.7);
        layoutParams.bottomMargin +=  (int) (fab_1.getHeight() * 0.25);
        fab_1.setLayoutParams(layoutParams);
        fab_1.setClickable(true);
        fab_1.startAnimation(showFabAnim1);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab_2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab_2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab_2.getHeight() * 1.5);
        fab_2.setLayoutParams(layoutParams2);
        fab_2.startAnimation(showFabAnim2);
        fab_2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab_3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab_3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab_3.getHeight() * 1.7);
        fab_3.setLayoutParams(layoutParams3);
        fab_3.startAnimation(showFabAnim3);
        fab_3.setClickable(true);
    }

    private void hideFab() {
        FrameLayout.LayoutParams layoutParams
                = (FrameLayout.LayoutParams) fab_1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab_1.getWidth() * 1.7);
        layoutParams.bottomMargin -=  (int) (fab_1.getHeight() * 0.25);
        fab_1.setLayoutParams(layoutParams);
        fab_1.setClickable(true);
        fab_1.startAnimation(closeFabAnim1);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab_2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab_2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab_2.getHeight() * 1.5);
        fab_2.setLayoutParams(layoutParams2);
        fab_2.startAnimation(closeFabAnim2);
        fab_2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab_3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab_3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab_3.getHeight() * 1.7);
        fab_3.setLayoutParams(layoutParams3);
        fab_3.startAnimation(closeFabAnim3);
        fab_3.setClickable(false);

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
