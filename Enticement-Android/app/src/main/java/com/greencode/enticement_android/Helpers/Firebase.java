package com.greencode.enticement_android.Helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.Models.MyProfile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hung Nguyen on 11/28/2016.
 */

public class Firebase {
    public static final FirebaseAuth mFBAuth = FirebaseAuth.getInstance();
    public static final DatabaseReference ChatRoomRef = FirebaseDatabase.getInstance().getReference().child("Chatroom");
    public static final DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    public static final StorageReference UserProfileStorage = FirebaseStorage.getInstance().getReference().child("User_Profile");

    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_BIRTHDAY = "user_birthday";
    public static final String USER_CREATEDAT = "user_created_at";
    public static final String USER_PROFILEURL = "user_profileurl";

    public static void setNewUserProfile(MyProfile myProfile) {
        Map<String, Object> mapUser = new HashMap<>();
        String id = mFBAuth.getCurrentUser().getUid();
        mapUser.put(USER_ID, id);
        mapUser.put(USER_EMAIL, myProfile.getEmail());
        mapUser.put(USER_NAME, "Hung Nguyen");
        mapUser.put(USER_NICKNAME, "Administrator");
        mapUser.put(USER_CREATEDAT, String.valueOf(System.currentTimeMillis()));
        mapUser.put(USER_BIRTHDAY, String.valueOf(System.currentTimeMillis() - 34 * 60 * 60 * 1000));

        if (myProfile.getProfile_url() == null) {
            mapUser.put(USER_PROFILEURL, null);
        } else {
            mapUser.put(USER_PROFILEURL, myProfile.getProfile_url());
        }

        UserRef.child(id).push().setValue(mapUser);
    }

    public static void getNewUserProfile() {
        String id = mFBAuth.getCurrentUser().getUid();

        UserRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyProfile newProfile = new MyProfile();

                /*
                * DATA SNAPSHOT in the firebase server is stored as:
                * - user birthday
                * - user created date
                * - user email
                * - user id
                * - user name
                * - user nickname
                * */

                newProfile.setBirthday(dataSnapshot.child(USER_BIRTHDAY).getValue().toString());
                newProfile.setCreated_at(dataSnapshot.child(USER_CREATEDAT).getValue().toString());
                newProfile.setEmail(dataSnapshot.child(USER_EMAIL).getValue().toString());
                newProfile.setId(dataSnapshot.child(USER_ID).getValue().toString());
                newProfile.setName(dataSnapshot.child(USER_NAME).getValue().toString());
                newProfile.setNickname(dataSnapshot.child(USER_NICKNAME).getValue().toString());
                newProfile.setProfile_url(dataSnapshot.child(USER_PROFILEURL).getValue().toString());

                // update new profile to the application to get right preferences
                EnticementApplication.getInstance().getPrefManager().setProfile(newProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void updateImageFromBytes(byte[] data, final Context context) {
        UploadTask uploadTask = UserProfileStorage.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri dbURI = taskSnapshot.getDownloadUrl();

                assert dbURI != null;
                EnticementApplication.getInstance().getPrefManager()
                        .getProfile().setProfile_url(dbURI.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
