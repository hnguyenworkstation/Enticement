package com.greencode.enticement_android.Interfaces;

/**
 * Created by Hung Nguyen on 1/3/2017.
 */

public interface ListItemOnFragmentInteractionListener {
    void onScrollDown(boolean isScrollDown);
    void resetCountUpAnimation();
    void clickOnItem(int i);
}
