package com.greencode.enticement_android.LayoutControllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.greencode.enticement_android.ViewFragments.FeaturedFragment;

/**
 * Created by Hung Nguyen on 1/3/2017.
 */

public class EventTabAdapter extends FragmentStatePagerAdapter {
    public EventTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FeaturedFragment.newInstance(1);
    }

    @Override
    public int getCount() {
        return 5;
    }

    public String getPageTitle(int i) {
        return "Event Tab: " + (i + 1);
    }

    public interface StickerClickListener {
        public void onStickerClick(int page, int position);
    }
}
