package com.greencode.enticement_android.Helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.greencode.enticement_android.Models.MyProfile;

/**
 * Created by Hung Nguyen on 11/28/2016.
 */

public class Firebase {
    public static DatabaseReference ChatRoomRef = FirebaseDatabase.getInstance().getReference().child("Chatroom");

    public static void registerNewUser(MyProfile myProfile) {

    }
}
