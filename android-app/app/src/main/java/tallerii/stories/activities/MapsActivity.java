package tallerii.stories.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.helpers.LocationHelper;
import tallerii.stories.interfaces.StoriesAware;
import tallerii.stories.network.apimodels.Storie;

public class MapsActivity extends StoriesLoggedInActivity implements OnMapReadyCallback, StoriesAware {

    private static final int MY_PERMISSIONS_REQUEST_LOCATIONS = 5454;
    public static final float DEFAULT_LOCAL_ZOOM = 12.0f;
    private GoogleMap mMap;
    private StoriesController controller;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        controller = new StoriesController(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        controller.getStories(getProfile().getUserId());
    }

    @Override
    public StoriesLoggedInActivity getLoggedInActivity() {
        return this;
    }

    @Override
    public void populateStories(List<Storie> stories) {
        for (Storie story :stories) {
            if (story.getLocation() == null) continue;
            LatLng location = LocationHelper.getLocation(story.getLocation());
            mMap.addMarker(new MarkerOptions().position(location).title(story.getTitle()));
        }
        goToCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void goToCurrentLocation() {
        if (hasLocationPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            goToLocation(location);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATIONS);
        }
    }

    private void goToLocation(Location location) {
        if (location != null) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_LOCAL_ZOOM));
        }
    }

    private boolean hasLocationPermissions() {
        int fineLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED
                || coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }
}
