package com.greencode.enticement_android.Helpers;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Models.MyProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung Nguyen on 11/28/2016.
 */

public class Firebase {
    public static FirebaseAuth mFBAuth = FirebaseAuth.getInstance();
    public static DatabaseReference ChatRoomRef = FirebaseDatabase.getInstance().getReference().child("Chatroom");
    public static DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_BIRTHDAY = "user_birthday";
    public static final String USER_CREATEDAT = "user_created_at";

    public static void setNewUserProfile(MyProfile myProfile) {
        Map<String, Object> mapUser = new HashMap<>();
        String id = mFBAuth.getCurrentUser().getUid();
        mapUser.put(USER_ID, id);
        mapUser.put(USER_EMAIL, myProfile.getEmail());
        mapUser.put(USER_NAME, "Hung Nguyen");
        mapUser.put(USER_NICKNAME, "Administrator");
        mapUser.put(USER_CREATEDAT, String.valueOf(System.currentTimeMillis()));
        mapUser.put(USER_BIRTHDAY, String.valueOf(System.currentTimeMillis() - 34 * 60 * 60 * 1000));
        UserRef.child(id).push().setValue(mapUser);
    }

    public static void getNewUserProfile() {
        String id = mFBAuth.getCurrentUser().getUid();

        UserRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyProfile newProf = new MyProfile();

                /*
                * DATA SNAPSHOT in the firebase server is stored as:
                * - user birthday
                * - user created date
                * - user email
                * - user id
                * - user name
                * - user nickname
                * */
                newProf.setBirthday(dataSnapshot.getValue().toString());
                newProf.setCreated_at(dataSnapshot.getValue().toString());
                newProf.setEmail(dataSnapshot.getValue().toString());
                newProf.setId(dataSnapshot.getValue().toString());
                newProf.setName(dataSnapshot.getValue().toString());
                newProf.setNickname(dataSnapshot.getValue().toString());

                // update new profile to the application to get right preferences
                EnticementApplication.getInstance().getPrefManager().setProfile(newProf);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
