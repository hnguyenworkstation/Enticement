package com.greencode.enticement_android.Enticement;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.greencode.enticement_android.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.User;
import vc908.stickerfactory.billing.Prices;
import vc908.stickerfactory.utils.Utils;
import vc908.stickerpipe.gcmintegration.GcmManager;
import vc908.stickerpipe.jpushintegration.JpushManager;

/**
 * Created by Hung Nguyen on 11/15/2016.
 */

public class EnticementApplication extends Application {
    public static final String TAG = EnticementApplication.class
            .getSimpleName();
    private static EnticementApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = EnticementApplication.this;

        FirebaseApp.initializeApp(this);

        StickersManager.initialize(getString(R.string.chatroom_stickerapi),this);
        Map<String, String> meta = new HashMap<>();
        meta.put(User.KEY_GENDER, User.GENDER_MALE);
        meta.put(User.KEY_AGE, "33");

        // Put your user id when you know it
        StickersManager.setUser(Utils.getDeviceId(this), meta);
        // Set sender id for push notifications
        // GcmManager.setGcmSenderId(this, "86472317986");
        // Set push notification listener
        // GcmManager.setPushNotificationManager(new PushNotificationManager());
        // Set custom shop class
        //StickersManager.setShopClass(ShopActivity.class);
        // Set prices
//        StickersManager.setPrices(new Prices()
//                        .setPricePointB("$0.99", 9.99f)
//                        .setPricePointC("$1.99", 19.99f)
//                // sku used for inapp purchases
//                       .setSkuB("pack_b")
//                       .setSkuC("pack_c")
//        );

        // licence key for inapp purchases
        StickersManager.setLicenseKey("YOUR LICENCE KEY");
        JpushManager.init(this);
    }

    public static synchronized EnticementApplication getInstance() {
        return mInstance;
    }
}
