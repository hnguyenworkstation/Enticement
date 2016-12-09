package com.greencode.enticement_android.Interfaces;

import android.view.View;

/**
 * Created by Hung Nguyen on 11/28/2016.
 */

public interface MessageClickListener {
    void clickImageChat(View view, int position, String nameUser, String urlPhotoUser, String urlPhotoClick);

    void clickImageMapChat(View view, int position,String latitude,String longitude);


}
