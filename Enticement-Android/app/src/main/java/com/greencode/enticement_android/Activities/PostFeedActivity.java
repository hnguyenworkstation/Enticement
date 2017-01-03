package com.greencode.enticement_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Models.Message;
import com.greencode.enticement_android.R;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import java.util.ArrayList;
import java.util.List;

public class PostFeedActivity extends EnticementActivity implements View.OnClickListener {
    private final String TAG = "New Event";

    private ImageView userImage;
    private ImageButton locationBtn;
    private ImageButton galleryBtn;
    private ImageButton cameraBtn;
    private ImageButton closeKeyboardBtn;

    private FlatButton addTagBtn;
    private FlatTextView listTopic;

    private TextView userNameTv;
    private TextView locationTv;
    private TextView mainTopicTv;
    private EditText postMessageEt;

    private GalleryConfig galleryConfig;
    private List<String> photos;
    private List<String> videos;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int LOCATION_REQUEST = 3;
    private static final int RECORDVOID_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);

        photos = new ArrayList<>();
        videos = new ArrayList<>();

        setupToolbar();
        initElements();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.postfeed_toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.chatroom_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TAG);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void initElements() {
        userImage = (ImageView) findViewById(R.id.postfeed_userimg);
        locationBtn = (ImageButton) findViewById(R.id.postfeed_location);
        galleryBtn = (ImageButton) findViewById(R.id.postfeed_gallery);
        cameraBtn = (ImageButton) findViewById(R.id.postfeed_camera);
        closeKeyboardBtn = (ImageButton) findViewById(R.id.postfeed_closekeyboard);

        closeKeyboardBtn.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        userImage.setOnClickListener(this);

        addTagBtn = (FlatButton) findViewById(R.id.postfeed_addtag);
        listTopic = (FlatTextView) findViewById(R.id.postfeed_topiclistview);

        userNameTv = (TextView) findViewById(R.id.postfeed_username);
        locationTv = (TextView) findViewById(R.id.postfeed_locationname);
        mainTopicTv = (TextView) findViewById(R.id.postfeed_maintopicname);
        postMessageEt = (EditText) findViewById(R.id.postfeed_inputcontent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        overridePendingTransition(R.anim.fix_anim, R.anim.slide_down_out);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.postfeed_menu_post:
                Toast.makeText(this, "Post Click", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.postfeed_gallery:
                openGallery();
                break;
            case R.id.postfeed_location:
                openLocationIntent();
                break;
            case R.id.postfeed_addtag:
                break;
            case R.id.postfeed_closekeyboard:
                hideKeyboard();
                break;
            default:
                break;
        }
    }

    private void openGallery() {
        galleryConfig = new GalleryConfig.Build()
                .limitPickPhoto(9)
                .singlePhoto(false)
                .hintOfPick("GALLERY")
                .filterMimeTypes(new String[]{"image/jpeg"})
                .build();
        GalleryActivity.openActivity(PostFeedActivity.this, GALLERY_REQUEST, galleryConfig);
    }

    private void openLocationIntent(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), LOCATION_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place != null) {
                    LatLng latLng = place.getLatLng();
                } else {
                    //PLACE IS NULL
                }
            }
        } else if (requestCode == GALLERY_REQUEST) {
            //list of photos of seleced
            photos = ((List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS));
            //list of videos of seleced
            videos = ((List<String>) data.getSerializableExtra(GalleryActivity.VIDEO));

            Log.d("Post Feed", "Photos Picked: " + photos.size() + "\nVideos Picked: " + videos.size());
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
