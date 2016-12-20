package com.greencode.enticement_android.Activities;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        mMap.addMarker(new MarkerOptions().position(new LatLng(mInstance.getPrefManager().getVisitLocation().getLatitude(),
                    mInstance.getPrefManager().getVisitLocation().getLongitude()))
                .title("Current Visiting"));

        mMap.addCircle(new CircleOptions()
                .center(newPlace)
                .radius(10000)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

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
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mInstance.getPrefManager().getVisitLocation().getLatitude(),
                        mInstance.getPrefManager().getVisitLocation().getLongitude()))
                .title("Marker in Sydney"));

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(mInstance.getPrefManager().getVisitLocation().getLatitude(),
                        mInstance.getPrefManager().getVisitLocation().getLongitude()))
                .radius(5000)
                .strokeColor(R.color.grape_dark)
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
