package com.industrialmaster.farmnet.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.industrialmaster.farmnet.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "SelectLocationActivity";

    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private static final int REQUEST_CODE = 101;
    private SupportMapFragment mMapFragment;
    private SearchView mSearchView;
    private LatLng mLatLng;
    private Button mGetLocationButton;
    String mAddress;
    FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        mSearchView = findViewById(R.id.search_view_location);
        mGetLocationButton = findViewById(R.id.button_get_location);
        ImageButton mMyLocationImageButton = findViewById(R.id.image_button_my_location);

        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(SelectLocationActivity.this);
        fetchLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        mMyLocationImageButton.setOnClickListener(v -> fetchLastLocation());

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGoogleMap.clear();

                String searchLocation = mSearchView.getQuery().toString();
                List<Address> addressList = null;

                if(searchLocation!=null || !searchLocation.equals("")){
                    Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                    try{
                        addressList = geocoder.getFromLocationName(searchLocation, 1);
                    }catch (IOException e){
                        Log.e(TAG, e.toString());
                    }
                    assert addressList != null;
                    Address address = addressList.get(0);
                    mLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    getAddress(mLatLng);
                    mGoogleMap.addMarker(new MarkerOptions().position(mLatLng).title(mAddress));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mGetLocationButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("address", mAddress);
            resultIntent.putExtra("latitude", mLatLng.latitude);
            resultIntent.putExtra("longitude", mLatLng.longitude);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnMapClickListener(latLng -> {
            mLatLng = latLng;
            getAddress(mLatLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(mAddress);
            mGoogleMap.clear();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            mGoogleMap.addMarker(markerOptions);
        });

        mLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        getAddress(mLatLng);
        MarkerOptions markerOptions = new MarkerOptions().position(mLatLng).title(mAddress);
        mGoogleMap.clear();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
        mGoogleMap.addMarker(markerOptions);
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location != null){
                mCurrentLocation = location;
                mMapFragment.getMapAsync(SelectLocationActivity.this);
            }
        });
    }

    private void getAddress (LatLng latLng){
        Geocoder geocoder = new Geocoder(SelectLocationActivity.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            mAddress = addresses.get(0).getAddressLine(0);
            mLatLng = latLng;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation();
        }
    }
}
