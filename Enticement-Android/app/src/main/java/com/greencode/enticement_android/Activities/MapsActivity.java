package com.greencode.enticement_android.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.greencode.enticement_android.Enticement.EnticementActivity;
import com.greencode.enticement_android.Enticement.EnticementApplication;
import com.greencode.enticement_android.R;

public class MapsActivity extends EnticementActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Toolbar mToolbar;
    private FlatButton mDropButton;
    private final EnticementApplication mInstance = EnticementApplication.getInstance();
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        mToolbar.setTitle(getString(R.string.map_title));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDropButton = (FlatButton) findViewById(R.id.map_dropoff);
        mDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitNewPlace(mMap.getCameraPosition().target);
            }
        });

        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reloadMap();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mDialog.cancel();
            }
        });

        builder.setMessage(R.string.reload_message);
        builder.setTitle(R.string.reload_title);

        mDialog = builder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.map_search:
                Toast.makeText(this, "Map Search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.map_reload:
                Toast.makeText(this, "Map Reload", Toast.LENGTH_SHORT).show();
                mDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
    }

    private void visitNewPlace(LatLng newPlace) {
        CameraPosition newPos = new CameraPosition.Builder()
                .target(newPlace)
                .zoom(12)
                .bearing(300)
                .build();

        Location newLoc = new Location(LocationManager.GPS_PROVIDER);
        newLoc.setLatitude(newPlace.latitude);
        newLoc.setLongitude(newPlace.longitude);
        mInstance.getPrefManager().setVisitLocation(newLoc);

        mMap.clear();

        mMap.addCircle(new CircleOptions()
                .center(newPlace)
                .radius(5000)
                .strokeColor(R.color.wallet_holo_blue_light)
                .fillColor(R.color.white_transparent));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onCancel() {
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }
        });
    }

    private void reloadMap() {
        CameraPosition newPos = new CameraPosition.Builder()
                .target(new LatLng(mInstance.getPrefManager().getCurrentLocation().getLatitude(),
                        mInstance.getPrefManager().getCurrentLocation().getLongitude()))
                .zoom(12)
                .bearing(300)
                .build();

        mMap.clear();

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(mInstance.getPrefManager().getCurrentLocation().getLatitude(),
                        mInstance.getPrefManager().getCurrentLocation().getLongitude()))
                .radius(5000)
                .strokeColor(R.color.wallet_holo_blue_light)
                .fillColor(R.color.white_transparent));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onCancel() {
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }
        });
    }

    private void initMap() {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(mInstance.getPrefManager().getVisitLocation().getLatitude(),
                        mInstance.getPrefManager().getVisitLocation().getLongitude()))
                .radius(5000)
                .strokeColor(R.color.blue_accent500)
                .fillColor(R.color.white_transparent));

        CameraPosition newPos = new CameraPosition.Builder()
                .target(new LatLng(mInstance.getPrefManager().getVisitLocation().getLatitude(),
                        mInstance.getPrefManager().getVisitLocation().getLongitude()))
                .zoom(12)
                .bearing(300)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.getUiSettings().setScrollGesturesEnabled(true);
            }

            @Override
            public void onCancel() {
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }
        });

    }
}
