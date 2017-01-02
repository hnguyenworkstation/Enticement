package com.greencode.enticement_android.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Helpers.Firebase;
import com.greencode.enticement_android.LayoutControllers.MessagesAdapter;
import com.greencode.enticement_android.Models.Message;
import com.greencode.enticement_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vc908.stickerfactory.StickersKeyboardController;
import vc908.stickerfactory.StickersManager;
import vc908.stickerfactory.ui.OnStickerSelectedListener;
import vc908.stickerfactory.ui.fragment.StickersFragment;
import vc908.stickerfactory.ui.view.BadgedStickersButton;
import vc908.stickerfactory.ui.view.StickersKeyboardLayout;
import vc908.stickerfactory.utils.CompatUtils;
import vc908.stickerpipe.gcmintegration.NotificationManager;

public class ChatRoomActivity extends EnticementActivity {

    private EditText mInputMsg;
    private RecyclerView mListMsgView;
    private StickersKeyboardController mStickerKeyboardController;
    private MessagesAdapter mAdapter;
    private ImageView buttonSend;
    private LinearLayoutManager mLinearLayoutManager;
    private int mPreviousPositionItemClick = -1;
    private String chatroomID;
    private String chatroomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        chatroomID = intent.getStringExtra("chat_room_id");
        chatroomTitle = intent.getStringExtra("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.cract_toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.chatroom_toolbar));
        toolbar.setTitle(chatroomTitle);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        buttonSend = (ImageView) findViewById(R.id.cract_sendbtn);
        buttonSend.setColorFilter(ContextCompat.getColor(this, R.color.chatroom_toolbar));
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mInputMsg.getText().toString();
                sendMessage(message, false, System.currentTimeMillis());
            }
        });

        mListMsgView = (RecyclerView) findViewById(R.id.cract_msgrecycler);
        initMessagesRecycler();

        mInputMsg = (EditText) findViewById(R.id.cract_inputtext);
        transformSendBtn(mInputMsg);

        mInputMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                transformSendBtn(mInputMsg);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        StickersFragment stickersFragment = (StickersFragment) getSupportFragmentManager().findFragmentById(R.id.cract_stickercontainer);
        if (stickersFragment == null) {
            stickersFragment = new StickersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.cract_stickercontainer, stickersFragment).commit();
        }
        stickersFragment.setOnStickerSelectedListener(stickerSelectedListener);

        BadgedStickersButton stickerButton = ((BadgedStickersButton) findViewById(R.id.cract_stickerbtn));
        View stickersFrame = findViewById(R.id.cract_stickercontainer);
        View chatContentGroup = findViewById(R.id.cract_contentlayout);
        RecyclerView suggestsList = (RecyclerView) findViewById(R.id.cract_suggestion);
        StickersKeyboardLayout stickersLayout = (StickersKeyboardLayout) findViewById(R.id.cract_layout);

        mStickerKeyboardController = new StickersKeyboardController.Builder(this)
                .setStickersKeyboardLayout(stickersLayout)
                .setStickersFragment(stickersFragment)
                .setStickersFrame(stickersFrame)
                .setContentContainer(chatContentGroup)
                .setStickersButton(stickerButton)
                .setChatEdit(mInputMsg)
                .setSuggestContainer(suggestsList)
                .build();


        mStickerKeyboardController.setKeyboardVisibilityChangeListener(new StickersKeyboardController.KeyboardVisibilityChangeListener() {
            @Override
            public void onTextKeyboardVisibilityChanged(boolean isVisible) {
                scrollToBottomIfNeed();
            }

            @Override
            public void onStickersKeyboardVisibilityChanged(boolean isVisible) {

            }
        });

        processIntent(getIntent());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        overridePendingTransition(R.anim.fix_anim, R.anim.fade_out_to_right);
        return false;
    }

    private void transformSendBtn(EditText currentInput) {
        String currentMessage = currentInput.getText().toString();
        if (currentMessage.length() > 0) {
            buttonSend.setEnabled(true);
        } else {
            buttonSend.setEnabled(false);
        }
    }

    private void initMessagesRecycler() {
        mAdapter = new MessagesAdapter(getBaseContext(), chatroomID);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mListMsgView.scrollToPosition(positionStart);
                }
            }
        });
        mAdapter.setOnChatScreenClickListener(new MessagesAdapter.OnClickChatScreenListener() {
            @Override
            public void onErrorMessageClick(View view) {

            }

            @Override
            public void onMessageClick(View view, int position) {
                showStatus(position);
                Message item = mAdapter.getItem(position);
                if (item != null) {
                    boolean isSet = item.getVisibilityDate();
                    item.setVisibilityDate(!isSet);
                    mAdapter.notifyDataSetChanged();
                }
                mPreviousPositionItemClick = position;
            }

            @Override
            public void onStickerMessageClick(View view, int position) {

            }

            @Override
            public void onMessageImageClick(View view, int position) {

            }
        });

        mListMsgView.setLayoutManager(mLinearLayoutManager);
        mListMsgView.setAdapter(mAdapter);
    }

    private void showStatus(int currentPos) {
        if (currentPos != mPreviousPositionItemClick &&
                mPreviousPositionItemClick != -1 && mAdapter.getItemCount() > mPreviousPositionItemClick) {
            Message previousItem = mAdapter.getItem(mPreviousPositionItemClick);
            if (previousItem != null)
                previousItem.setVisibilityDate(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        NotificationManager.processIntent(this, intent, mStickerKeyboardController);
    }

    private OnStickerSelectedListener stickerSelectedListener = new OnStickerSelectedListener() {
        @Override
        public void onStickerSelected(String code) {
            sendMessage(code, false, System.currentTimeMillis());
        }

        @Override
        public void onEmojiSelected(String emoji) {
            mInputMsg.append(emoji);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.crmenu_phonecall:
                Toast.makeText(this, "Phone Call", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.crmenu_videocall:
                Toast.makeText(this, "Video Call", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.crmenu_more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendMessage(String message, boolean isFromMe, long time) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Message msg;
        if (StickersManager.isSticker(message)) {
            msg = new Message(Firebase.mFBAuth.getCurrentUser().getUid(), Message.MessageType.STICKER, message, time);
            StickersManager.onUserMessageSent(true);
        } else {
            msg = new Message(Firebase.mFBAuth.getCurrentUser().getUid(), Message.MessageType.MESSAGE, message, time);
            StickersManager.onUserMessageSent(false);
            if (!isFromMe) {
                mInputMsg.setText("");
            }
        }

        Firebase.sendPlainMessage(chatroomID, msg);
        updateList(!isFromMe);
    }

    private void updateList(boolean forceScroll) {
        mAdapter.notifyDataSetChanged();
        scrollToBottomIfNeed(forceScroll);
    }

    private void scrollToBottomIfNeed() {
        scrollToBottomIfNeed(false);
    }

    private void scrollToBottomIfNeed(boolean force) {
        // if (mListMsgView.getLastVisiblePosition() + 1 >= mMessageAdapter.getCount() - 1 || force) {
        //    mListMsgView.smoothScrollToPosition(mMessageAdapter.getCount() - 1);
        // }
    }

    @Override
    public void onBackPressed() {
        if (!mStickerKeyboardController.hideStickersKeyboard()) {

        }

        if (mStickerKeyboardController.hideStickersKeyboard()){
            NavUtils.navigateUpFromSameTask(this);
        }
    }

}
