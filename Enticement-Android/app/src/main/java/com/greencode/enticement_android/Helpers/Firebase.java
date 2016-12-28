package com.greencode.enticement_android.Helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.greencode.enticement_android.Enticement.EnticementPreferenceManager;
import com.greencode.enticement_android.Models.ChatRoom;
import com.greencode.enticement_android.Models.Message;
import com.greencode.enticement_android.Models.MyProfile;
import com.greencode.enticement_android.Models.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import vc908.stickerfactory.User;

/**
 * Created by Hung Nguyen on 11/28/2016.
 */

public class Firebase {
    /*USER FIELDS*/
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NAME = "user_name";
    private static final String USER_NICKNAME = "user_nickname";
    private static final String USER_BIRTHDAY = "user_birthday";
    private static final String USER_CREATEDAT = "user_created_at";
    private static final String USER_PROFILEURL = "user_profileurl";

    /*CHATROOM FIELDS*/
    private static final String CHATROOM_CREATEDAT = "chatroom_created_at";
    private static final String CHATROOM_USER1 = "chatroom_user1";
    private static final String CHATROOM_USER2 = "chatroom_user2";
    private static final String CHATROOM_LIST_MESSAGES = "chatroom_list_messages";

    /*FINAL STATIC FIELDS OF FIREBASE SERVER*/
    public static final FirebaseAuth mFBAuth = FirebaseAuth.getInstance();
    public static final DatabaseReference ChatRoomRef = FirebaseDatabase.getInstance().getReference().child("Chatroom");
    public static final DatabaseReference ListMessagesRef = ChatRoomRef.child(CHATROOM_LIST_MESSAGES);
    public static final DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    public static final StorageReference UserProfileStorage = FirebaseStorage.getInstance().getReference().child("User_Profile");
    public static final EnticementPreferenceManager mInstance = EnticementApplication.getInstance().getPrefManager();

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

        UserRef.child(id).updateChildren(mapUser);
    }

    public static void getNewUserProfile() {
        UserRef.child(mFBAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                * DATA SNAPSHOT in the firebase server is stored as:
                * - user birthday
                * - user created date
                * - user email
                * - user id
                * - user name
                * - user nickname
                * */

                mInstance.getProfile().setBirthday(dataSnapshot.child(USER_BIRTHDAY).getValue().toString());
                mInstance.getProfile().setCreated_at(dataSnapshot.child(USER_CREATEDAT).getValue().toString());
                mInstance.getProfile().setEmail(dataSnapshot.child(USER_EMAIL).getValue().toString());
                mInstance.getProfile().setId(dataSnapshot.child(USER_ID).getValue().toString());
                mInstance.getProfile().setName(dataSnapshot.child(USER_NAME).getValue().toString());
                mInstance.getProfile().setNickname(dataSnapshot.child(USER_NICKNAME).getValue().toString());
                mInstance.getProfile().setId(mFBAuth.getCurrentUser().getUid());
                // newProfile.setProfile_url(dataSnapshot.child(USER_PROFILEURL).getValue().toString());

                Log.d("Lo: ", "Name: " +  EnticementApplication.getInstance().getPrefManager().getProfile().getName()
                        +  "\nNickname: " + EnticementApplication.getInstance().getPrefManager().getProfile().getNickname()
                        + "\nUID:" + EnticementApplication.getInstance().getPrefManager().getProfile().getId());
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

    public static void createChatroom(String withID) {
        Map<String, Object> mapChatroom = new HashMap<>();

        String id = mFBAuth.getCurrentUser().getUid();
        String tempID = ChatRoomRef.push().getKey();
//        mapChatroom.put(CHATROOM_CREATEDAT, String.valueOf(System.currentTimeMillis()));
//        mapChatroom.put(CHATROOM_USER1, id);
//        mapChatroom.put(CHATROOM_USER2, withID);
//        mapChatroom.put(CHATROOM_LIST_MESSAGES, null);

        ChatRoom newChatroom = new ChatRoom(tempID, id, withID, "Hello", String.valueOf(System.currentTimeMillis()));
        ChatRoomRef.child(tempID).setValue(newChatroom);
    }

    public static void pushPlainMessage(String roomId, Message message) {
        ChatRoomRef.child(roomId).child(CHATROOM_LIST_MESSAGES).push().setValue(message);
    }

    public static List<ChatRoom> retrieveChatRooms() {
        List<ChatRoom> list = new ArrayList<>();



        return list;
    }

    public static void getUserProfileToChatRoom(final ChatRoom room) {
        String id = mFBAuth.getCurrentUser().getUid();
        String chatToId;
        if (room.getUserID1() == id) {
            chatToId = room.getUserID2();
        } else {
            chatToId = room.getUserID1();
        }

        UserRef.child(chatToId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile newProfile = new UserProfile();
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

                room.setUser(newProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void sendPlainMessage(String roomId, Message msg) {
        ChatRoomRef.child(roomId).child(CHATROOM_LIST_MESSAGES).push().setValue(msg);
    }
}
