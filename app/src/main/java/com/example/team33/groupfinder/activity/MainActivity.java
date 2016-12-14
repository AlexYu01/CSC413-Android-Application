package com.example.team33.groupfinder.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team33.groupfinder.R;
import com.example.team33.groupfinder.adapter.RecyclerViewAdapter;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.controller.JsonController;
import com.example.team33.groupfinder.model.Group;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;


import android.location.Location;


public class MainActivity extends AppCompatActivity
        implements
        SearchView.OnQueryTextListener,
        RecyclerViewAdapter.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "MainActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    JsonController controller;
    TextView textView;
    RecyclerView recyclerView;
    private double latitude;
    private double longitude;
    private RecyclerViewAdapter adapter;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set default latitude and longitude values that don't exist in real world
        latitude = -91.0;
        longitude = -181.0;

        textView = (TextView) findViewById(R.id.tvEmptyRecyclerView);
        textView.setText(R.string.search_guide);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(new ArrayList<Group>());
        adapter.setListener(this);

        // ask user to allow location tracking
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }

        // build the Google API client
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        showSettingsAlert();

        controller = new JsonController(
                new JsonController.OnResponseListener() {
                    @Override
                    public void onSuccess(List<Group> groups) {
                        if (groups.size() > 0) {
                            textView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.invalidate();
                            adapter.updateDataSet(groups);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Failed to retrieve data");
                        Toast.makeText(MainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    /**
     * Connect to Google Play Services if not already.
     * Check if permission to track location was granted. If so get last recorded location.
     */

    public void getLocation() {
        if (googleApiClient != null) {
            googleApiClient.connect();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.i(MainActivity.class.getSimpleName(), "Permission was granted!");
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (lastLocation != null) {
                    //Log.i(MainActivity.class.getSimpleName(), "Location is not null!");
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                }
            }
        }
    }

    /**
     * Use Google Services API to check if location on device is on and prompt the user if it is not
     */
    public void showSettingsAlert() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    /**
     * Check the result of whether user allowed permission to track location
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * create options from menu/menu_activity_main.xml where we have searchView as one of the menu items
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search");
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    /**
     * this method is invoked when user presses search button in soft keyboard
     *
     * @param query query text in search view
     * @return boolean
     * <p> - true  - query handled </p>
     * <p> - false - query not handled (returning false will collapse soft keyboard)</p>
     */
    @Override
    public boolean onQueryTextSubmit(String query) {

        getLocation();

        if (query.length() > 1) {
            controller.cancelAllRequests();
            controller.sendRequest(query, latitude, longitude);

            return false;
        } else {
            Toast.makeText(MainActivity.this, "Must provide more than one character", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Must provide more than one character to search");
            return true;
        }
    }

    /**
     * this method is invoked on every key press of soft keyboard while user is typing
     *
     * @param newText newText is updated query text on every input of user from soft keyboard
     * @return boolean
     */
    @Override
    public boolean onQueryTextChange(String newText) {

        getLocation();

        if (newText.length() > 1) {
            controller.cancelAllRequests();
            controller.sendRequest(newText, latitude, longitude);
        } else if (newText.equals("")) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    /**
     * Interface Implementation
     * <p>This method will be invoked when user press anywhere on cardview</p>
     */
    @Override
    public void onCardClick(Group group) {
        Intent intent = GroupActivity.newIntent(App.getContext(), group.getUuid());
        startActivity(intent);
    }

}
