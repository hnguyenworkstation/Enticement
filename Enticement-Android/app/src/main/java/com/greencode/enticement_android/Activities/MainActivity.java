package com.greencode.enticement_android.Activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Helpers.AppUtils;
import com.greencode.enticement_android.Databases.Firebase;
import com.greencode.enticement_android.LayoutControllers.ViewPagerAdapter;
import com.greencode.enticement_android.Models.DummyContent;
import com.greencode.enticement_android.R;
import com.greencode.enticement_android.ViewFragments.FeaturedFragment;
import com.greencode.enticement_android.ViewFragments.GroupAroundFragment;
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
            PeopleAroundFragment.OnListFragmentInteractionListener,
            GroupAroundFragment.OnListFragmentInteractionListener {

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
    private static final int REQUEST_LOCATION_ACCESS = 0;
    private final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private final EnticementApplication mInstance = EnticementApplication.getInstance();

    private final int[] mTabsIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_group_white_24dp,
            R.drawable.ic_chat_white_24dp,
            R.drawable.ic_update_white_24dp,
            R.drawable.ic_more_horiz_white_24dp
    };

    @Override
    @RequiresPermission(anyOf = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fetch data from server
        receiveDataFromServer();
        Log.d("On Create ", "Name: " +  EnticementApplication.getInstance().getPrefManager().getProfile().getName()
                +  "\nNickname: " + EnticementApplication.getInstance().getPrefManager().getProfile().getNickname()
                + "\nUID:" + EnticementApplication.getInstance().getPrefManager().getProfile().getId());

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

        // then initialize the actionbar again
        initActionBar();

        mTabLayout = (TabLayout) findViewById(R.id.mainact_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                mTabLayout.getTabAt(i).setIcon(mTabsIcons[i]);
            }
        }

        if (mInstance.getPrefManager().getCurrentLocation() == null) {
            fetchLocation();
        }
    }

    private void receiveDataFromServer() {
        // get user profile again
        Firebase.getNewUserProfile();

        // Gettting user list
        Firebase.receiveUsersList();
    }

    @Override
    public void onResume(){
        super.onResume();

        // fetch data from server
        receiveDataFromServer();

        Log.d("On Resume", "Name: " +  EnticementApplication.getInstance().getPrefManager().getProfile().getName()
                +  "\nNickname: " + EnticementApplication.getInstance().getPrefManager().getProfile().getNickname()
                + "\nUID:" + EnticementApplication.getInstance().getPrefManager().getProfile().getId());

        // then initialize the actionbar again
        initActionBar();
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.d("On Restart", "Name: " +  EnticementApplication.getInstance().getPrefManager().getProfile().getName()
                +  "\nNickname: " + EnticementApplication.getInstance().getPrefManager().getProfile().getNickname()
                + "\nUID:" + EnticementApplication.getInstance().getPrefManager().getProfile().getId());

        // then initialize the actionbar again
        initActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("On Start", "Name: " +  EnticementApplication.getInstance().getPrefManager().getProfile().getName()
                +  "\nNickname: " + EnticementApplication.getInstance().getPrefManager().getProfile().getNickname()
                + "\nUID:" + EnticementApplication.getInstance().getPrefManager().getProfile().getId());

        // then initialize the actionbar again
        initActionBar();
    }

    @Nullable
    private boolean fetchLocation() {
        if (mayRequestPermissions()) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                }

                Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocationGPS != null) {
                    mInstance.getPrefManager().setCurrentLocation(lastKnownLocationGPS);
                    mInstance.getPrefManager().setVisitLocation(lastKnownLocationGPS);
                    return true;
                } else {
                    mInstance.getPrefManager().setCurrentLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
                    mInstance.getPrefManager().setVisitLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
                    return true;
                }
            }
        } else {
            requestPermission();
        }

        return false;
    }

    private boolean mayRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermission();
        } else {
            requestPermission();
        }
        return false;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                },
                REQUEST_LOCATION_ACCESS);
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
                if (mayRequestPermissions()) {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                break;
            case R.id.fab_1:
                hideFab();
                isTransforming = false;
                startActivity(new Intent(this, PostFeedActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_bottom_up, R.anim.fix_anim);
                break;
            case R.id.fab_2:
                hideFab();
                isTransforming = false;
                break;
            case R.id.fab_3:
                hideFab();
                isTransforming = false;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {

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
        mActionProfileName.setText(EnticementApplication.getInstance().getPrefManager().getProfile().getName());
        mActionProfileNickname = (TextView) v.findViewById(R.id.actionbar_nickname);
        mActionProfileNickname.setText(EnticementApplication.getInstance().getPrefManager().getProfile().getNickname());

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
        adapter.addFragment(new FeaturedFragment(), "Events");
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
